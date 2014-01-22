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
package de.free_creations.dbEntities;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class TeamNGTest {

  private class TestListener implements PropertyChangeListener {

    public int called = 0;
    public PropertyChangeEvent lastEvent = null;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      called++;
      lastEvent = evt;
    }
  }

  static final String PersistenceUnitName = "DbEntitiesPU";
  private static EntityManager entityManager = null;

  private Team testTeam;

  @BeforeClass
  public static void setUpClass() throws Exception {
    try {
      EntityManagerFactory factory = Persistence.createEntityManagerFactory(PersistenceUnitName);
      entityManager = factory.createEntityManager();
    } catch (javax.persistence.PersistenceException ex) {
      fail("+++ Please start the database server and try again.");

    }
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    if (entityManager != null) {
      entityManager.close();
    }
  }

  @BeforeMethod
  public void setUpMethod() throws Exception {
    entityManager.getTransaction().begin();
    // verify that we can retrieve the enties from the database (we assume that a suitable test-database is used)
    TypedQuery<Team> q = entityManager.createNamedQuery("Team.findAll", Team.class);
    List<Team> ll = q.getResultList();
    assertNotNull(ll);
    assertFalse(ll.isEmpty());

    // the first item will be used for further testing (we assume that a suitable test-database is used)
    testTeam = ll.get(0);
  }

  @AfterMethod
  public void tearDownMethod() throws Exception {
    entityManager.getTransaction().rollback();
  }

  @Test
  public void testIdentity() {
    EntityIdentity expected = new EntityIdentity(testTeam.getClass(), testTeam.getTeamId());
    assertEquals(expected, testTeam.identity());
  }

  @Test
  public void testPropertyChangeCallbackForName() throws Throwable {

    Team testItem = new Team(Integer.MAX_VALUE);
    final TestListener testListener = new TestListener();
    testItem.addPropertyChangeListener(testListener);

    testItem.setName("Abc"); // 1

    final int expectedCallbackCount = 1;

    try {
      EventQueue.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          assertEquals(testListener.called, expectedCallbackCount);
        }
      });
    } catch (InvocationTargetException ex) {
      throw ex.getCause();
    }

  }

  /**
   * Verify that when a singleton person gets added to a team, the virtual
   * NULL_TEAM sends a property change (person removed).
   *
   * @throws Throwable
   */
  @Test
  public void testPropertyChangeCallbackForNULL_TEAM_1() throws Throwable {

    Person testPerson = new Person(Integer.MAX_VALUE);
    final TestListener testListener = new TestListener();
    Team.addPropertyChangeListener(testListener, Team.NULL_TEAM_ID);

    testPerson.setTeam(testTeam);

    final int expectedCallbackCount = 1;

    try {
      EventQueue.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          assertEquals(testListener.called, expectedCallbackCount);
          assertEquals(testListener.lastEvent.getPropertyName(),
                  Team.PROP_REMOVE_PERSON);
        }
      });
    } catch (InvocationTargetException ex) {
      throw ex.getCause();
    }
  }

  /**
   * Verify that when a person gets removed from any team, the virtual NULL_TEAM
   * sends a property change (person added).
   *
   * @throws Throwable
   */
  @Test
  public void testPropertyChangeCallbackForNULL_TEAM_2() throws Throwable {

    Person testPerson = new Person(Integer.MAX_VALUE);
    testPerson.setTeam(testTeam);

    final TestListener testListener = new TestListener();
    Team.addPropertyChangeListener(testListener, Team.NULL_TEAM_ID);

    testPerson.setTeam(null);
    final int expectedCallbackCount = 1;

    try {
      EventQueue.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          assertEquals(testListener.called, expectedCallbackCount);
          assertEquals(testListener.lastEvent.getPropertyName(),
                  Team.PROP_ADD_PERSON);
        }
      });
    } catch (InvocationTargetException ex) {
      throw ex.getCause();
    }
  }
}
