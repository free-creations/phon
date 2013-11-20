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
package de.free_creations.nbPhonAPI;

import de.free_creations.dbEntities.Jury;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class ContestCollectionTest {

  ContestCollection contestCollection = null;

  public ContestCollectionTest() {
  }

  @Before
  public void setUp() throws Exception {
    assertTrue("did you start the database-server?", Manager.isOpen());
    contestCollection = Manager.getContestCollection();
    assertNotNull(contestCollection);

  }

  /**
   * Test of getJuryTypes method, of class ContestCollection.
   */
  @Test
  public void testContestTypes() {
    System.out.println("testGetJuryTypes");
    Set<String> juryTypes = contestCollection.contestTypes();
    assertNotNull(juryTypes);
    assertFalse(juryTypes.isEmpty());
    for (String jt : juryTypes) {
      System.out.println("..." + jt);
    }
  }

  /**
   * Test of getAll method, of class ContestCollection.
   */
  @Test
  public void testGetAll() {
    List<Jury> result = contestCollection.getAll();
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  /**
   * Test of findEntity method, of class ContestCollection.
   */
  @Test
  public void testFindEntity() throws Exception {

    Jury entity = contestCollection.findEntity(1);
    assertNotNull(entity);
    assertEquals(1, entity.getJuryid());
  }

  /**
   * Test of newEntity method, of class ContestCollection.
   */
  @Test
  public void testNewEntity() throws Exception {
    System.out.println("testNewEntity");
    Jury entity = contestCollection.newEntity();
    assertNotNull(entity);
    System.out.println("..."+entity.getJuryid());
  }

  /**
   * Test of removeEntity method, of class ContestCollection.
   */
  @Test
  public void testRemoveEntity() throws Exception {
  }

  /**
   * Test of addPropertyChangeListener method, of class ContestCollection.
   */
  @Test
  public void testAddPropertyChangeListener() {
  }

  /**
   * Test of removePropertyChangeListener method, of class ContestCollection.
   */
  @Test
  public void testRemovePropertyChangeListener() {
  }

  /**
   * Test of contestTypes method, of class ContestCollection.
   */
  @Test 
  public void testJuryTypes() {
     //Ignore("Tested with testNewEntity")
  }
}