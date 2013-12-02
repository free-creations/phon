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

import de.free_creations.dbEntities.Location;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.openide.nodes.Children;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class LocationRootNodeTest {

  private class CollectionMock implements MutableEntityCollection<Location, Integer> {

    public Location l1;
    public Location l2;

    @Override
    public List<Location> getAll() {
      ArrayList<Location> result = new ArrayList<>();
      l1 = new Location(1);
      l2 = new Location(2);
      l1.setName("l1 location");
      l2.setName("l2 location");

      result.add(l1);
      result.add(l2);
      return result;
    }

    @Override
    public Location findEntity(Integer key) throws DataBaseNotReadyException {
      switch (key) {
        case 1:
          return l1;
        case 2:
          return l2;
      }
      return null;

    }

    @Override
    public Location newEntity() throws DataBaseNotReadyException {
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
  private final CollectionMock locationCollectionMock = new CollectionMock();

  public LocationRootNodeTest() {
  }

  /**
   * Test of getChildren method, of class LocationRootNode.
   */
  @Test
  public void testGetChildren() {
    LocationRootNode testRoot = new LocationRootNode(locationCollectionMock);
    Children children = testRoot.getChildren();
    assertNotNull(children);
    assertEquals(2, children.getNodesCount());
    
  }

  /**
   * Test of getActions method, of class LocationRootNode.
   */
  @Test
  public void testGetActions() {
    //@Ignore("function is trivial")
  }

  /**
   * Test of sortByName method, of class LocationRootNode.
   */
  @Test
  public void testSortByName() {
    //@Ignore("function is trivial")
  }

  /**
   * Test of sortByPriority method, of class LocationRootNode.
   */
  @Test
  public void testSortByPriority() {
    // @Ignore("function is trivial")
  }

  /**
   * Test of getNodeAt method, of class LocationRootNode.
   */
  @Test
  @Ignore("Not implemented yet")
  public void testGetNodeAt() {
//    LocationRootNode testRoot = new LocationRootNode(locationCollectionMock);
//    LocationNode node1 = testRoot.getNodeAt(0);
//    assertEquals((int)locationCollectionMock.l1.getLocationId(), (int)node1.getLocationId());
//    LocationNode node2 = testRoot.getNodeAt(1);
//    assertEquals((int)locationCollectionMock.l2.getLocationId(), (int)node2.getLocationId());
//
//
//    LocationNode nullLocation = testRoot.getNodeAt(4711);
//    assertNull(nullLocation);
  }

  /**
   * Test of getNodeKeyAt method, of class LocationRootNode.
   */
  @Test
  @Ignore("Not implemented yet")
  public void testGetNodeKeyAt() {
//    LocationRootNode testRoot = new LocationRootNode(locationCollectionMock);
//    int nodeKey1 = testRoot.getNodeKeyAt(0);
//    assertEquals(1, nodeKey1);
//    int nodeKey2 = testRoot.getNodeKeyAt(1);
//    assertEquals(2, nodeKey2);
//
//    Integer notFoundKey = testRoot.getNodeKeyAt(4711);
//    assertNull(notFoundKey);
  }

  /**
   * Test of findIndexForNode method, of class LocationRootNode.
   */
  @Test
  @Ignore("Not implemented yet")
  public void testFindIndexForNode() {
//    LocationRootNode testRoot = new LocationRootNode(locationCollectionMock);
//    int indexForNode1 = testRoot.findIndexForNode(1);
//    assertEquals(0, indexForNode1);
//    int indexForNode2 = testRoot.findIndexForNode(2);
//    assertEquals(1, indexForNode2);
//
//    int notFindable = testRoot.findIndexForNode(47114711);
//    assertEquals(-1, notFindable);
  }

}
