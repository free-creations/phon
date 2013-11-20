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

  private class CollectionMock implements MutableEntityCollection<Jury, Integer> {

    public Jury j1;
    public Jury j2;

    @Override
    public List<Jury> getAll() {
      ArrayList<Jury> result = new ArrayList<>();
      j1 = new Jury(1);
      j2 = new Jury(2);
      j1.setWertung("j1 jury");
      j2.setWertung("j2 jury");

      result.add(j1);
      result.add(j2);
      return result;
    }

    @Override
    public Jury findEntity(Integer key) throws DataBaseNotReadyException {
      switch (key) {
        case 1:
          return j1;
        case 2:
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
    public void removeEntity(Integer key) throws DataBaseNotReadyException {
      throw new UnsupportedOperationException("Not supported (not tested) yet.");

    }
  };
  private final CollectionMock juryCollectionMock = new CollectionMock();

  public JuryRootNodeTest() {
  }

  /**
   * Test of getActions method, of class JuryRootNode.
   */
  @Test
  public void testGetActions() {
      //@Ignore("function is trivial")
  }

  /**
   * Test of sortByName method, of class JuryRootNode.
   */
  @Test
  public void testSortByName() {
      //@Ignore("function is trivial")
  }

  /**
   * Test of sortByPriority method, of class JuryRootNode.
   */
  @Test
  public void testSortByPriority() {
     // @Ignore("function is trivial")
  }

  /**
   * Test of getNodeAt method, of class JuryRootNode.
   */
  @Test
  public void testGetNodeAt() {
    JuryRootNode testRoot = new JuryRootNode(juryCollectionMock);
    JuryNode node1 = testRoot.getNodeAt(0);
    assertEquals((int)juryCollectionMock.j1.getJuryid(), (int)node1.getJuryId());
    JuryNode node2 = testRoot.getNodeAt(1);
    assertEquals((int)juryCollectionMock.j2.getJuryid(), (int)node2.getJuryId());


    JuryNode nullJury = testRoot.getNodeAt(4711);
    assertNull(nullJury);
  }

  /**
   * Test of getNodeKeyAt method, of class JuryRootNode.
   */
  @Test
  public void testGetNodeKeyAt() {
    JuryRootNode testRoot = new JuryRootNode(juryCollectionMock);
    int nodeKey1 = testRoot.getNodeKeyAt(0);
    assertEquals(1, nodeKey1);
    int nodeKey2 = testRoot.getNodeKeyAt(1);
    assertEquals(2, nodeKey2);

    Integer notFoundKey = testRoot.getNodeKeyAt(4711);
    assertNull(notFoundKey);
  }

  /**
   * Test of findIndexForNode method, of class JuryRootNode.
   */
  @Test
  public void testFindIndexForNode() {
    JuryRootNode testRoot = new JuryRootNode(juryCollectionMock);
    int indexForNode1 = testRoot.findIndexForNode(1);
    assertEquals(0, indexForNode1);
    int indexForNode2 = testRoot.findIndexForNode(2);
    assertEquals(1, indexForNode2);

    int notFindable = testRoot.findIndexForNode(47114711);
    assertEquals(-1, notFindable);
  }


}