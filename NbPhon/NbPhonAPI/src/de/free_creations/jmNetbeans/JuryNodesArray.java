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
package de.free_creations.jmNetbeans;

import de.free_creations.dbEntities.EntityIdentity;
import de.free_creations.dbEntities.Jury;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class JuryNodesArray extends Children.SortedArray {

  private final Comparator<Node> juryComparator = new Comparator<Node>() {
    @Override
    public int compare(Node n1, Node n2) {

      // make sure both nodes are JuryNodes
      int typeCheck = Utils.typeCheckCompare(n1, n2, JuryNode.class);
      if (typeCheck != Utils.bothValid) {
        // OOps, one or both nodes were not of the expected type..
        return typeCheck;
      }
      JuryNode jn1 = (JuryNode) n1;
      JuryNode jn2 = (JuryNode) n2;

      // try to get the corresponding entities
      try {
        Jury j1 = juryCollection.findEntity(jn1.getJuryId());
        Jury j2 = juryCollection.findEntity(jn2.getJuryId());
        int notNullCheck = Utils.typeCheckCompare(j1, j2, Jury.class);
        if (notNullCheck != Utils.bothValid) {
          // OOps, one or both entities could not be foud in the database...
          return notNullCheck;
        }

        String w1 = j1.getWertung();
        String w2 = j1.getWertung();
        int result = Utils.stringCompareNull(w1, w2);
        if (result == 0) {
          // OOps, they have both the same long description (probably both null)
          // So we'll discriminate through the primary key.
          return Utils.stringCompareNull(jn1.getJuryId(), jn2.getJuryId());
        }

        // OK, this is the result we wanted.
        return result;

      } catch (DataBaseNotReadyException ex) {
        // OOps, database not ready.
        // So we'll discriminate through the primary key.
        return Utils.stringCompareNull(jn1.getJuryId(), jn2.getJuryId());
      }
    }
  };
  private final MutableEntityCollection<Jury, String> juryCollection;
  private final PropertyChangeListener personCollectionListener = new PropertyChangeListener() {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (MutableEntityCollection.PROP_ITEM_ADDED.equals(evt.getPropertyName())) {
        // a new Jury has been added to the to the  juryCollection,
        // this jury must now be added to the internal "nodes" list.
        Object o = evt.getNewValue();
        if (o instanceof EntityIdentity) {
          EntityIdentity newJury = (EntityIdentity) o;
          String juryId = (String) newJury.primaryKey;
          //create a node for this new Jury
          JuryNode newNode = new JuryNode(juryId, juryCollection);
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
        // a  Jury has been removed from the  juryCollection,
        // we re-initialize the list from scratch. (Removing the item from
        // the existing list might be a little more efficient but not worth the hassle.)
        nodes = initCollection();
        refresh();
      }
      if (MutableEntityCollection.PROP_ITEM_LIST_CHANGED.equals(evt.getPropertyName())) {
        // The juryCollection has entirly changed,
        // we re-initialize the list from scratch. 
        nodes = initCollection();
        refresh();
      }
    }
  };

  public JuryNodesArray(MutableEntityCollection<Jury, String> juryCollection) {
    super();
    super.setComparator(juryComparator);
    this.juryCollection = juryCollection;
    this.juryCollection.addPropertyChangeListener(personCollectionListener);
  }

  @Override
  protected ArrayList<Node> initCollection() {
    List<Jury> jj = juryCollection.getAll();
    ArrayList<Node> result = new ArrayList<>(jj.size());

    for (Jury j : jj) {
      assert (j != null);
      String juryid = j.getJuryid();
      assert (juryid != null);
      result.add(new JuryNode(juryid, juryCollection));
    }

    Comparator<? super Node> comparator = getComparator();
    if (comparator != null) {
      Collections.sort(result, comparator);
    }
    return result;
  }

//  public void setComparator(JuryCompare.JuryComparator c) {
//    super.setComparator(c);
//  }
  /**
   * Find the current position of a given node.
   *
   * @param key the JuryId that is searched for.
   * @return returns the index of the current position. Returns -1 if no
   * JuryNode with the searched key could be found.
   */
  int findIndexForNode(String key) {
    int nodesCount = getNodesCount();
    for (int i = 0; i < nodesCount; i++) {
      Node n = getNodeAt(i);
      if (n instanceof JuryNode) {
        JuryNode pn = (JuryNode) n;
        if (Objects.equals(pn.getJuryId(), key)) {
          return i;
        }
      }
    }
    return -1;
  }
}
