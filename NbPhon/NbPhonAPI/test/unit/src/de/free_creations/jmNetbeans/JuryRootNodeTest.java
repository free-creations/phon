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

import de.free_creations.dbEntities.Jury;
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
public class JuryRootNodeTest {

  private class CollectionMock implements MutableEntityCollection<Jury, String> {

    public Jury j1;
    public Jury j2;

    @Override
    public List<Jury> getAll() {
      ArrayList<Jury> result = new ArrayList<>();
      j1 = new Jury("J1");
      j2 = new Jury("J2");
      j1.setWertung("j1 jury");
      j2.setWertung("j2 jury");

      result.add(j1);
      result.add(j2);
      return result;
    }

    @Override
    public Jury findEntity(String key) throws DataBaseNotReadyException {
      switch (key) {
        case "J1":
          return j1;
        case "J2":
          return j2;
      }
      return null;

    }

    @Override
    public Jury newEntity() throws DataBaseNotReadyException {
      throw new UnsupportedOperationException("Not supported (not tested) yet.");
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    @Override
    public void removeEntity(String key) throws DataBaseNotReadyException {
      throw new UnsupportedOperationException("Not supported (not tested) yet.");

    }
  };
  private CollectionMock juryCollectionMock = new CollectionMock();

  public JuryRootNodeTest() {
  }

  /**
   * Test of getActions method, of class JuryRootNode.
   */
  @Test
  @Ignore("function is trivial")
  public void testGetActions() {
  }

  /**
   * Test of sortByName method, of class JuryRootNode.
   */
  @Test
  @Ignore("function is trivial")
  public void testSortByName() {
  }

  /**
   * Test of sortByPriority method, of class JuryRootNode.
   */
  @Test
  @Ignore("function is trivial")
  public void testSortByPriority() {
  }

  /**
   * Test of getNodeAt method, of class JuryRootNode.
   */
  @Test
  public void testGetNodeAt() {
    JuryRootNode testRoot = new JuryRootNode(juryCollectionMock);
    JuryNode node1 = testRoot.getNodeAt(0);
    assertEquals(juryCollectionMock.j1.getJuryid(), node1.getJuryId());
    JuryNode node2 = testRoot.getNodeAt(1);
    assertEquals(juryCollectionMock.j2.getJuryid(), node2.getJuryId());


    JuryNode nullJury = testRoot.getNodeAt(4711);
    assertNull(nullJury);
  }

  /**
   * Test of getNodeKeyAt method, of class JuryRootNode.
   */
  @Test
  public void testGetNodeKeyAt() {
    JuryRootNode testRoot = new JuryRootNode(juryCollectionMock);
    String nodeKey1 = testRoot.getNodeKeyAt(0);
    assertEquals("J1", nodeKey1);
    String nodeKey2 = testRoot.getNodeKeyAt(1);
    assertEquals("J2", nodeKey2);

    String notFoundKey = testRoot.getNodeKeyAt(4711);
    assertNull(notFoundKey);
  }

  /**
   * Test of findIndexForNode method, of class JuryRootNode.
   */
  @Test
  public void testFindIndexForNode() {
    JuryRootNode testRoot = new JuryRootNode(juryCollectionMock);
    int indexForNode1 = testRoot.findIndexForNode("J1");
    assertEquals(0, indexForNode1);
    int indexForNode2 = testRoot.findIndexForNode("J2");
    assertEquals(1, indexForNode2);

    int notFindable = testRoot.findIndexForNode("J4711");
    assertEquals(-1, notFindable);
  }


}