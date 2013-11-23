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
package de.free_creations.dbEntities;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class TeamTest {

  static final String PersistenceUnitName = "DbEntitiesPU";
  private static EntityManager entityManager = null;

  @BeforeClass
  public static void setUpClass() {
    try {
      EntityManagerFactory factory = Persistence.createEntityManagerFactory(PersistenceUnitName);
      entityManager = factory.createEntityManager();
    } catch (javax.persistence.PersistenceException ex) {
      fail("+++ Please start the database server and try again.");

    }
  }

  @AfterClass
  public static void tearDownClass() {
    if (entityManager != null) {
      entityManager.close();
    }
  }
  private Team testTeam;
  private Person testPerson;

  @Before
  public void setUp() {
    entityManager.getTransaction().begin();
    TypedQuery<Team> qt = entityManager.createNamedQuery("Team.findAll", Team.class);
    List<Team> cc = qt.getResultList();
    assertNotNull(cc);
    assertFalse(cc.isEmpty());
    testTeam = cc.get(0);
  }

  @After
  public void tearDown() {
    entityManager.getTransaction().rollback();
    testTeam = null;
  }

  /**
   * Test of identity method, of class Allocation.
   */
  @Test
  public void testIdentity() {
    EntityIdentity expected = new EntityIdentity(testTeam.getClass(), testTeam.getTeamId());
    assertEquals(expected, testTeam.identity());
  }

  @Test
  public void testSetGetPerson() {
    Person p = new Person();
    entityManager.persist(p);
    entityManager.flush(); // give p its primary keys.
    assertFalse(testTeam.getPersonList().contains(p));

    p.setTeam(testTeam);
    assertTrue(testTeam.getPersonList().contains(p));    
    
    p.setTeam(null);
    assertFalse(testTeam.getPersonList().contains(p));
    
  }

}
