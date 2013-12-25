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

import de.free_creations.dbEntities.Contest;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class ContestRootNode extends AbstractNode {

  private final ContestNodesArray children;
  private final MutableEntityCollection<Contest, Integer> contestCollection;

  private final Action newItemAction = new NewContestAction();
  private final Action[] allActions = new Action[]{newItemAction};

  private ContestRootNode(ContestNodesArray children, MutableEntityCollection<Contest, Integer> cc) {
    super(children);
    this.children = children;
    this.contestCollection = cc;

  }

  /**
   * Create a root node containing contest- nodes for all contest-keys found in
   * the given contestCollection.
   *
   * @param cc for every item found in this collection a node object will be
   * created.
   * @param withNullItem add an extra item with null key (used to show in drop
   * down boxes)
   * @param attachJobtypes attach job-type-nodes to each contest-node.
   */
  public ContestRootNode(MutableEntityCollection<Contest, Integer> cc,
          boolean withNullItem, 
          boolean attachJobtypes) {
    this(new ContestNodesArray(cc, withNullItem, attachJobtypes), cc);
  }

  public ContestRootNode(MutableEntityCollection<Contest, Integer> cc) {
    this(cc, false, true);
  }

  @Override
  public Action[] getActions(boolean context) {
    return allActions;
  }

  /**
   * Getter for a child at a given position.
   *
   * Note: the position of a child node depends on the currently chosen sort
   * order.
   *
   * If a child with such index does not exists it returns null.
   *
   * @param index
   * @return the ContestNode that is currently at the position given by index or
   * null if the index is invalid.
   */
  public ContestNode getNodeAt(int index) {
    if (index < 0) {
      return null;
    }
    if (index >= children.getNodesCount()) {
      return null;
    }
    Node result = children.getNodeAt(index);
    if (result instanceof ContestNode) {
      return (ContestNode) result;
    } else {
      return null;
    }
  }

  /**
   * Returns the key for the node currently located at the given position.
   *
   * Note: the position of a child node depends on the currently chosen sort
   * order.
   *
   * If a child with such index does not exists it returns null.
   *
   *
   * @param index
   * @return the ContestNode that is currently at the position given by index.
   * Returns null if the index is invalid.
   */
  public Integer getNodeKeyAt(int index) {
    ContestNode node = getNodeAt(index);
    if (node != null) {
      return node.getContestId();
    } else {
      return null;
    }
  }

  /**
   * Find the position of a Contest node with a given key.
   *
   * @param key
   * @return return the current position. If there is no ContestNode with the
   * given key the function will return -1.
   */
  public int findIndexForNode(Integer key) {
    return children.findIndexForNode(key);
  }
}
