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

import de.free_creations.dbEntities.Person;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class PersonsRootNode extends AbstractNode {

  private final PersonNodesArray children;
  private final MutableEntityCollection<Person, Integer> personCollection;
  private final Action sortByNameAction = new AbstractAction("Sort by Name") {
    @Override
    public void actionPerformed(ActionEvent e) {
      sortByName();
    }
  };
  private final Action sortByPriorityAction = new AbstractAction("Sort by Priority") {
    @Override
    public void actionPerformed(ActionEvent e) {
      sortByPriority();
    }
  };
  private final Action newItemAction = new NewPersonAction();
  private final Action[] allActions = new Action[]{newItemAction, sortByNameAction, sortByPriorityAction};

  private PersonsRootNode(PersonNodesArray children, MutableEntityCollection<Person, Integer> pp) {
    super(children);
    this.children = children;
    this.personCollection = pp;

  }

  public PersonsRootNode(MutableEntityCollection<Person, Integer> pp) {
    this(pp,false);
  }

  public PersonsRootNode(MutableEntityCollection<Person, Integer> pp, boolean withNobody) {
    this(new PersonNodesArray(pp, withNobody), pp);
  }

  @Override
  public Action[] getActions(boolean context) {
    return allActions;
  }

  public void sortByName() {
    children.setComparator(PersonCompare.byName(personCollection));
  }

  public void sortByPriority() {
    children.setComparator(PersonCompare.byPriority(personCollection));
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
   * @return the PersonNode that is currently at the position given by index or
   * null if the index is invalid.
   */
  public PersonNode getNodeAt(int index) {
    if (index < 0) {
      return null;
    }
    if (index >= children.getNodesCount()) {
      return null;
    }
    Node result = children.getNodeAt(index);
    if (result instanceof PersonNode) {
      return (PersonNode) result;
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
   * If a child with such index does not exists it returns -1. Note: it also
   *
   *
   * @param index
   * @return the PersonNode that is currently at the position given by index.
   * Returns null if the index is invalid.
   */
  public int getNodeKeyAt(int index) {
    PersonNode node = getNodeAt(index);
    if (node != null) {
      return node.getKey();
    } else {
      return -1;
    }
  }

  /**
   * Find the position of a person node with a given key.
   *
   * @param key
   * @return return the current position. If there is no PersonNode with the
   * given key the function will return -1.
   */
  public int findIndexForNode(int key) {
    return children.findIndexForNode(key);
  }
}
