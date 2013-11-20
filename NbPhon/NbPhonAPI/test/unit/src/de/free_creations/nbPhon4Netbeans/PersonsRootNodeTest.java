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
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class PersonsRootNodeTest {

  private class CollectionMock implements MutableEntityCollection<Person, Integer> {

    public Person p1;
    public Person p2;

    @Override
    public List<Person> getAll() {
      ArrayList<Person> result = new ArrayList<>();
      p1 = new Person();
      p2 = new Person();
      p1.setPersonid(1);
      p1.setFamilienname("A");
      p2.setPersonid(2);
      p2.setFamilienname("B");
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

  public PersonsRootNodeTest() {
  }

  /**
   * Test of getActions method, of class PersonsRootNode.
   */
  @Test
  public void testGetActions() {
    //  @Ignore("function is trivial")
  }

  /**
   * Test of sortByName method, of class PersonsRootNode.
   */
  @Test
  public void testSortByName() {
    //   @Ignore("function is trivial")
  }

  /**
   * Test of sortByPriority method, of class PersonsRootNode.
   */
  @Test
  public void testSortByPriority() {
    //   @Ignore("function is trivial")
  }

  /**
   * Test of getNodeAt method, of class PersonsRootNode.
   */
  @Test
  public void testGetNodeAt() {
    PersonsRootNode testRoot = new PersonsRootNode(personCollectionMock);
    PersonNode node1 = testRoot.getNodeAt(0);
    assertEquals((int) personCollectionMock.p1.getPersonid(), node1.getKey());
    PersonNode node2 = testRoot.getNodeAt(1);
    assertEquals((int) personCollectionMock.p2.getPersonid(), node2.getKey());

    PersonNode nullPerson = testRoot.getNodeAt(4711);
    assertNull(nullPerson);
  }

  /**
   * Test of getNodeKeyAt method, of class PersonsRootNode.
   */
  @Test
  public void testGetNodeKeyAt() {
    PersonsRootNode testRoot = new PersonsRootNode(personCollectionMock);
    int nodeKey1 = testRoot.getNodeKeyAt(0);
    assertEquals(1, nodeKey1);
    int nodeKey2 = testRoot.getNodeKeyAt(1);
    assertEquals(2, nodeKey2);

    int notFoundKey = testRoot.getNodeKeyAt(4711);
    assertEquals(-1, notFoundKey);
  }

  /**
   * Test of findIndexForNode method, of class PersonsRootNode.
   */
  @Test
  public void testFindIndexForNode() {
    PersonsRootNode testRoot = new PersonsRootNode(personCollectionMock);
    int indexForNode1 = testRoot.findIndexForNode(1);
    assertEquals(0, indexForNode1);
    int indexForNode2 = testRoot.findIndexForNode(2);
    assertEquals(1, indexForNode2);

    int notFindable = testRoot.findIndexForNode(4711);
    assertEquals(-1, notFindable);
  }

  /**
   * Test of findIndexForNode method, of class PersonsRootNode.
   */
  @Test
  public void testFindIndexForNobodyNode() {
    PersonsRootNode testRoot = new PersonsRootNode(personCollectionMock, true);

    // the nobody node must be the first one
    int indexNobody = testRoot.findIndexForNode(PersonNode.nullKey);
    assertEquals(0, indexNobody);

    // all other must be shifted by one
    int indexForNode1 = testRoot.findIndexForNode(1);
    assertEquals(1, indexForNode1);
    int indexForNode2 = testRoot.findIndexForNode(2);
    assertEquals(2, indexForNode2);

    int notFindable = testRoot.findIndexForNode(4711);
    assertEquals(-1, notFindable);
  }
}