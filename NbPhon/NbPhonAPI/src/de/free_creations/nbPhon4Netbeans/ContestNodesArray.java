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
import de.free_creations.dbEntities.Contest;
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
public class ContestNodesArray extends Children.SortedArray {

  private final Comparator<Node> juryComparator = new Comparator<Node>() {
    @Override
    public int compare(Node n1, Node n2) {

      // make sure both nodes are ContestNodes
      int typeCheck = Utils.typeCheckCompare(n1, n2, ContestNode.class);
      if (typeCheck != Utils.bothValid) {
        // OOps, one or both nodes were not of the expected type..
        return typeCheck;
      }
      ContestNode jn1 = (ContestNode) n1;
      ContestNode jn2 = (ContestNode) n2;

      // try to get the corresponding entities
      try {
        Contest j1 = juryCollection.findEntity(jn1.getJuryId());
        Contest j2 = juryCollection.findEntity(jn2.getJuryId());
        int notNullCheck = Utils.typeCheckCompare(j1, j2, Contest.class);
        if (notNullCheck != Utils.bothValid) {
          // OOps, one or both entities could not be foud in the database...
          return notNullCheck;
        }

        String w1 = j1.getName();
        String w2 = j1.getName();
        int result = Utils.stringCompareNull(w1, w2);
        if (result == 0) {
          // OOps, they have both the same long description (probably both null)
          // So we'll discriminate through the primary key.
          return Utils.integerCompareNull(jn1.getJuryId(), jn2.getJuryId());
        }

        // OK, this is the result we wanted.
        return result;

      } catch (DataBaseNotReadyException ex) {
        // OOps, database not ready.
        // So we'll discriminate through the primary key.
        return Utils.integerCompareNull(jn1.getJuryId(), jn2.getJuryId());
      }
    }
  };
  private final MutableEntityCollection<Contest, Integer> juryCollection;
  private final PropertyChangeListener personCollectionListener = new PropertyChangeListener() {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (MutableEntityCollection.PROP_ITEM_ADDED.equals(evt.getPropertyName())) {
        // a new Contest has been added to the to the  juryCollection,
        // this jury must now be added to the internal "nodes" list.
        Object o = evt.getNewValue();
        if (o instanceof EntityIdentity) {
          EntityIdentity newContest = (EntityIdentity) o;
          Integer juryId = (Integer) newContest.primaryKey;
          //create a node for this new Contest
          ContestNode newNode = new ContestNode(juryId, juryCollection, true);
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
        // a  Contest has been removed from the  juryCollection,
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

  public ContestNodesArray(MutableEntityCollection<Contest, Integer> juryCollection) {
    super();
    super.setComparator(juryComparator);
    this.juryCollection = juryCollection;
    this.juryCollection.addPropertyChangeListener(personCollectionListener);
  }

  @Override
  protected ArrayList<Node> initCollection() {
    List<Contest> jj = juryCollection.getAll();
    ArrayList<Node> result = new ArrayList<>(jj.size());

    for (Contest j : jj) {
      assert (j != null);
      Integer juryid = j.getContestId();
      assert (juryid != null);
      result.add(new ContestNode(juryid, juryCollection, true));
    }

    Comparator<? super Node> comparator = getComparator();
    if (comparator != null) {
      Collections.sort(result, comparator);
    }
    return result;
  }

//  public void setComparator(ContestCompare.ContestComparator c) {
//    super.setComparator(c);
//  }
  /**
   * Find the current position of a given node.
   *
   * @param key the ContestId that is searched for.
   * @return returns the index of the current position. Returns -1 if no
 ContestNode with the searched key could be found.
   */
  int findIndexForNode(Integer key) {
    int nodesCount = getNodesCount();
    for (int i = 0; i < nodesCount; i++) {
      Node n = getNodeAt(i);
      if (n instanceof ContestNode) {
        ContestNode pn = (ContestNode) n;
        if (Objects.equals(pn.getJuryId(), key)) {
          return i;
        }
      }
    }
    return -1;
  }
}
