/*
 * Copyright 2013 Harald Postner <Harald at free-creations.de>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.free_creations.nbPhon4Netbeans;

import de.free_creations.dbEntities.EntityIdentity;
import de.free_creations.dbEntities.Person;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class PersonNodesArray extends Children.SortedArray {

  private final Filter filter;
  private final Action preferedAction;

  /**
   * A filter can be used to exclude some items from Nodes Array.
   */
  public static interface Filter {

    /**
     * Must return true if the item should be added to the Array.
     * @param p
     * @return 
     */
    boolean take(Person p);
  }

  public static Filter takeAllFilter = new Filter() {

    @Override
    public boolean take(Person p) {
      return true;
    }
  };

  private final MutableEntityCollection<Person, Integer> personCollection;
  private final PropertyChangeListener personCollectionListener = new PropertyChangeListener() {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (MutableEntityCollection.PROP_ITEM_ADDED.equals(evt.getPropertyName())) {
        // a new person has been added to the to the  personCollection,
        // this person must also be added to the internal "nodes" list.
        Object o = evt.getNewValue();
        if (o instanceof EntityIdentity) {
          EntityIdentity newPerson = (EntityIdentity) o;
          Integer personid = (Integer) newPerson.primaryKey;
          //create a node for this new person
          PersonNode newNode = new PersonNode(personid, personCollection);
          newNode.notifyPendingChanges();
          if (nodes instanceof ArrayList) {
            // if the nodes- list is an ArrayList (as created in initCollection)
            // we can insert on top of the list.
            ((ArrayList<Node>) nodes).add(0, newNode);
          } else {
            // otherways, we just insert at the end.
            nodes.add(newNode);
          }
          refresh();
        }
      }
      if (MutableEntityCollection.PROP_ITEM_REMOVED.equals(evt.getPropertyName())) {
        throw new UnsupportedOperationException("Not supported yet.");
      }
      if (MutableEntityCollection.PROP_ITEM_LIST_CHANGED.equals(evt.getPropertyName())) {
        nodes = initCollection();
        refresh();
      }
    }
  };
  private final boolean hasNobodyNode;

  public PersonNodesArray(MutableEntityCollection<Person, Integer> personCollection) {
    this(personCollection, false);
  }

  public PersonNodesArray(MutableEntityCollection<Person, Integer> personCollection, boolean withNobody) {
    this(personCollection, withNobody, takeAllFilter, null);
  }

  public PersonNodesArray(MutableEntityCollection<Person, Integer> personCollection, boolean withNobody, Filter filter, Action preferedAction) {
    super();
    assert (filter != null);
    this.filter = filter;
    hasNobodyNode = withNobody;
    super.setComparator(PersonCompare.byName(personCollection));
    this.personCollection = personCollection;
    this.personCollection.addPropertyChangeListener(personCollectionListener);
    this.preferedAction = preferedAction;
  }

  @Override
  protected ArrayList<Node> initCollection() {
    List<Person> pp = personCollection.getAll();
    ArrayList<Node> result = new ArrayList<>(pp.size());
    if (hasNobodyNode) {
      result.add(new PersonNode(PersonNode.nullKey, personCollection));
    }
    for (Person p : pp) {
      assert (p != null);
      if (filter.take(p)) {
        Integer personid = p.getPersonId();
        assert (personid != null);
        result.add(new PersonNode(personid, personCollection, preferedAction));
      }
    }
    Comparator<? super Node> comparator = getComparator();
    if (comparator != null) {
      Collections.sort(result, comparator);
    }
    return result;
  }

  public void setComparator(PersonCompare.PersonComparator c) {
    super.setComparator(c);
  }

  @Override
  public Comparator<? super Node> getComparator() {
    return super.getComparator();
  }

  /**
   * Find the current position of a given node.
   *
   * @param key the PersonId that is searched for.
   * @return returns the index of the current position. Returns -1 if no
   * PersonNode with the searched key could be found.
   */
  int findIndexForNode(int key) {
    int nodesCount = getNodesCount();
    for (int i = 0; i < nodesCount; i++) {
      Node n = getNodeAt(i);
      if (n instanceof PersonNode) {
        PersonNode pn = (PersonNode) n;
        if (pn.getKey() == key) {
          return i;
        }
      }
    }
    return -1;
  }
}
