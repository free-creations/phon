/*
 * Copyright 2013 Harald Postner<harald at free-creations.de>.
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

import de.free_creations.dbEntities.Crew;
import de.free_creations.dbEntities.Person;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class CrewCollectionTest {

  private CrewCollection crewCollection;

  public CrewCollectionTest() {
  }

  @Before
  public void setUp() throws Exception {
    assertTrue("did you start the database-server?", Manager.isOpen());
    crewCollection = Manager.getCrewCollection();
    assertNotNull(crewCollection);
  }

  @After
  public void tearDown() {
    Manager.close(false);
  }

  /**
   * Test of getAll method, of class CrewCollection.
   */
  @Test
  public void testGetAll() {
    List<Crew> cc = crewCollection.getAll();
    assertNotNull(cc);
    assertFalse(cc.isEmpty());
  }

  /**
   * Test of findEntity method, of class CrewCollection.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testFindEntity() throws Exception {
    // we assume that the test database contains at least one record
    Crew c = crewCollection.findEntity(1);
    assertNotNull("Bad test data?", c);
    assertEquals((int) c.getCrewId(), 1);
  }

  /**
   * Test of newEntity method, of class CrewCollection.
   */
  @Test
  public void testNewEntity() throws Exception {
    Crew c = crewCollection.newEntity();
    assertNotNull(c);
    assertNotNull(c.getCrewId());
    assertTrue(c.getCrewId() > 1);
  }

  /**
   * Test of removeEntity method, of class CrewCollection.
   */
  @Test
  public void testRemoveEntity() throws Exception {
    // we assume that the test database contains at least one record
    // and that this crew has some members attached.
    Crew c = crewCollection.findEntity(1);
    ArrayList<Person> personList = new ArrayList<>(c.getPersonList());
    assertFalse("Bad test data?", personList.isEmpty());
    
    crewCollection.removeEntity(1);
    
    assertNull(crewCollection.findEntity(1));
    for(Person p:personList){
      assertNull(p.getCrew());
    }

  }

  /**
   * Test of addPropertyChangeListener method, of class CrewCollection.
   */
  @Test
  public void testAddPropertyChangeListener() {
    // trivial
  }

  /**
   * Test of removePropertyChangeListener method, of class CrewCollection.
   */
  @Test
  public void testRemovePropertyChangeListener() {
    // trivial
  }

}
