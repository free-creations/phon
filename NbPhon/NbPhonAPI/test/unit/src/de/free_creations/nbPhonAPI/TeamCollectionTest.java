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

import de.free_creations.dbEntities.Team;
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
public class TeamCollectionTest {

  private TeamCollection teamCollection;

  public TeamCollectionTest() {
  }

  @Before
  public void setUp() throws Exception {
    assertTrue("did you start the database-server?", Manager.isOpen());
    teamCollection = Manager.getTeamCollection();
    assertNotNull(teamCollection);
  }

  @After
  public void tearDown() {
    Manager.close(false);
  }

  /**
   * Test of getAll method, of class TeamCollection.
   */
  @Test
  public void testGetAll() {
    List<Team> cc = teamCollection.getAll();
    assertNotNull(cc);
    assertFalse(cc.isEmpty());
  }

  /**
   * Test of findEntity method, of class TeamCollection.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testFindEntity() throws Exception {
    // we assume that the test database contains at least one record
    Team c = teamCollection.findEntity(1);
    assertNotNull("Bad test data?", c);
    assertEquals((int) c.getTeamId(), 1);
  }

  /**
   * Test of newEntity method, of class TeamCollection.
   */
  @Test
  public void testNewEntity() throws Exception {
    Team c = teamCollection.newEntity();
    assertNotNull(c);
    assertNotNull(c.getTeamId());
    assertTrue(c.getTeamId() > 1);
  }

  /**
   * Test of removeEntity method, of class TeamCollection.
   */
  @Test
  public void testRemoveEntity() throws Exception {
    // we assume that the test database contains at least one record
    // and that this team has some members attached.
    Team c = teamCollection.findEntity(1);
    ArrayList<Person> personList = new ArrayList<>(c.getPersonList());
    assertFalse("Bad test data?", personList.isEmpty());
    
    teamCollection.removeEntity(1);
    
    assertNull(teamCollection.findEntity(1));
    for(Person p:personList){
      assertNull(p.getTeam());
    }

  }

  /**
   * Test of addPropertyChangeListener method, of class TeamCollection.
   */
  @Test
  public void testAddPropertyChangeListener() {
    // trivial
  }

  /**
   * Test of removePropertyChangeListener method, of class TeamCollection.
   */
  @Test
  public void testRemovePropertyChangeListener() {
    // trivial
  }

}
