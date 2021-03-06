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

  private final boolean withNullItem;
  private final boolean attachJobtypes;

  private final Comparator<Node> contestComparator = new Comparator<Node>() {
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
        Contest j1 = contestCollection.findEntity(jn1.getContestId());
        Contest j2 = contestCollection.findEntity(jn2.getContestId());
        int notNullCheck = Utils.typeCheckCompare(j1, j2, Contest.class);
        if (notNullCheck != Utils.bothValid) {
          // OOps, one or both entities could not be foud in the database...
          return notNullCheck;
        }

        // first let's compare the names only taking into account the letters
        String w1 = cleanString(j1.getName());
        String w2 = cleanString(j2.getName());
        int result = Utils.stringCompareNull(w1, w2);
        if (result == 0) {
          // the letters are the same, than compre the full name
          String full1 = j1.getName();
          String full2 = j2.getName();
          result = Utils.stringCompareNull(full1, full2);
          if (result == 0) {
          // OOps, they have both the same name (probably both null)
            // So we'll discriminate through the primary key.
            return Utils.integerCompareNull(jn1.getContestId(), jn2.getContestId());
          }
        }

        // OK, this is the result we wanted.
        return result;

      } catch (DataBaseNotReadyException ex) {
        // OOps, database not ready.
        // So we'll discriminate through the primary key.
        return Utils.integerCompareNull(jn1.getContestId(), jn2.getContestId());
      }
    }

    /**
     * will replace any non-letter characters with nothing.
     *
     * @param dirty
     * @return
     */
    private String cleanString(String dirty) {
      if (dirty != null) {
        return dirty.replaceAll("\\P{L}+", "");
      } else {
        return "";
      }
    }

  };
  private final MutableEntityCollection<Contest, Integer> contestCollection;
  private final PropertyChangeListener contestCollectionListener = new PropertyChangeListener() {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (MutableEntityCollection.PROP_ITEM_ADDED.equals(evt.getPropertyName())) {
        // a new Contest has been added to the to the  contestCollection,
        // this contest must now be added to the internal "nodes" list.
        Object o = evt.getNewValue();
        if (o instanceof EntityIdentity) {
          EntityIdentity newContest = (EntityIdentity) o;
          Integer contestId = (Integer) newContest.primaryKey;
          //create a node for this new Contest
          ContestNode newNode = new ContestNode(contestId, contestCollection, true);
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
        // a  Contest has been removed from the  contestCollection,
        // we re-initialize the list from scratch. (Removing the item from
        // the existing list might be a little more efficient but not worth the hassle.)
        nodes = initCollection();
        refresh();
      }
      if (MutableEntityCollection.PROP_ITEM_LIST_CHANGED.equals(evt.getPropertyName())) {
        // The contestCollection has entirly changed,
        // we re-initialize the list from scratch. 
        nodes = initCollection();
        refresh();
      }
    }
  };

  /**
   * Create nodes for all contest keys found in the given contestCollection.
   *
   * @param contestCollection for every item found in this collection a node
   * object will be created.
   */
//  public ContestNodesArray(MutableEntityCollection<Contest, Integer> contestCollection) {
//    this(contestCollection, false, true);
//  }
  /**
   * Create nodes for all contest keys found in the given contestCollection.
   *
   * @param contestCollection for every item found in this collection a node
   * object will be created.
   * @param withNullItem add an extra item with null key (used to show in drop
   * down boxes)
   * @param attachJobtypes attach job type nodes to each contest node.
   */
  public ContestNodesArray(MutableEntityCollection<Contest, Integer> contestCollection, boolean withNullItem, boolean attachJobtypes) {
    super();
    this.withNullItem = withNullItem;
    this.attachJobtypes = attachJobtypes;
    super.setComparator(contestComparator);
    this.contestCollection = contestCollection;
    this.contestCollection.addPropertyChangeListener(contestCollectionListener);
  }

  @Override
  protected ArrayList<Node> initCollection() {
    List<Contest> jj = contestCollection.getAll();
    ArrayList<Node> result = new ArrayList<>(jj.size());

    for (Contest j : jj) {
      assert (j != null);
      Integer contestId = j.getContestId();
      assert (contestId != null);
      result.add(new ContestNode(contestId, contestCollection, attachJobtypes));
    }
    if (withNullItem) {
      result.add(new ContestNode(null, null, false));
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
   * ContestNode with the searched key could be found.
   */
  int findIndexForNode(Integer key) {
    int nodesCount = getNodesCount();
    for (int i = 0; i < nodesCount; i++) {
      Node n = getNodeAt(i);
      if (n instanceof ContestNode) {
        ContestNode pn = (ContestNode) n;
        if (Objects.equals(pn.getContestId(), key)) {
          return i;
        }
      }
    }
    return -1;
  }
}
