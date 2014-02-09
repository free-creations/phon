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
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.nodes.Node;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class PersonNodesArrayTest {

  private class CollectionMock implements MutableEntityCollection<Person, Integer> {

    public Person p1;
    public Person p2;

    @Override
    public List<Person> getAll() {
      ArrayList<Person> result = new ArrayList<>();
      p1 = new Person(1);
      p2 = new Person(2);
      p1.setSurname("A");
      p2.setSurname("B");
      result.add(p1);
      result.add(p2);
      return result;
    }

    @Override
    public Person findEntity(Integer key) throws DataBaseNotReadyException {
      switch (key) {
        case 1:
          return p1;
        case 2:
          return p2;
      }
      return null;

    }

    @Override
    public Person newEntity() throws DataBaseNotReadyException {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    @Override
    public void removeEntity(Integer key) throws DataBaseNotReadyException {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  };
  private CollectionMock personCollectionMock = new CollectionMock();

  /**
   * Test of initCollection method, of class PersonNodesArray.
   */
  @Test
  public void testInitCollectionWithoutNobody() {
    PersonNodesArray testItem = new PersonNodesArray(personCollectionMock);
    ArrayList<Node> result = testItem.initCollection();
    assertNotNull(result);
    assertEquals(2, result.size());
  }

  @Test
  public void testInitCollectionWithFilter() {
    // a filter that takes only the first item
    PersonNodesArray.Filter filter = new PersonNodesArray.Filter() {

      @Override
      public boolean take(Person p) {
        return (p.getPersonId() == 1);
      }
    };
    PersonNodesArray testItem = new PersonNodesArray(personCollectionMock, false, filter, null);
    ArrayList<Node> result = testItem.initCollection();
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  /**
   * Test of initCollection method, of class PersonNodesArray.
   */
  @Test
  public void testInitCollectionWithNobody() {
    PersonNodesArray testItem = new PersonNodesArray(personCollectionMock, true);
    ArrayList<Node> result = testItem.initCollection();
    assertNotNull(result);
    assertEquals(3, result.size());
  }

  /**
   * Test of setComparator method, of class PersonNodesArray.
   */
  @Test
  public void testSetGetComparator() {
    PersonNodesArray testItem = new PersonNodesArray(personCollectionMock);
    Comparator<? super Node> comparator = PersonCompare.byName(personCollectionMock);
    testItem.setComparator(comparator);
    Comparator<? super Node> comparatorBack = testItem.getComparator();
    assertTrue(comparator == comparatorBack);
  }

  /**
   * Test of findIndexForNode method, of class PersonNodesArray.
   */
  @Test
  public void testfindIndexForNode() {
    PersonNodesArray testItem = new PersonNodesArray(personCollectionMock);

    int firstNode = testItem.findIndexForNode(1);
    assertEquals(0, firstNode);
    int secondNode = testItem.findIndexForNode(2);
    assertEquals(1, secondNode);

    int noNode = testItem.findIndexForNode(4711);
    assertEquals(-1, noNode);

  }
}
