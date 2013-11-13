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
import org.openide.nodes.Node;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class JuryNodesArrayTest {

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

  /**
   * Test of initCollection method, of class JuryNodesArray.
   */
  @Test
  public void testInitCollection() {
    JuryNodesArray testItem = new JuryNodesArray(juryCollectionMock);
    ArrayList<Node> result = testItem.initCollection();
    assertNotNull(result);
    assertEquals(2, result.size());
  }

  /**
   * Test of findIndexForNode method, of class JuryNodesArray.
   */
  @Test
  public void testfindIndexForNode() {
    JuryNodesArray testItem = new JuryNodesArray(juryCollectionMock);

    int indexFirstNode = testItem.findIndexForNode("J1");
    assertEquals(0, indexFirstNode);
    int indexSecondNode = testItem.findIndexForNode("J2");
    assertEquals(1, indexSecondNode);

    int indexNotFound = testItem.findIndexForNode("CannotBeFound");
    assertEquals(-1, indexNotFound);

    int indexNotFound2 = testItem.findIndexForNode(null);
    assertEquals(-1, indexNotFound2);
  }
}