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
public class ContestNGTest {

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

  private Contest testContest;
  private ContestType testContestType;

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
    TypedQuery<Contest> q = entityManager.createNamedQuery("Contest.findAll", Contest.class);
    List<Contest> cc = q.getResultList();
    assertNotNull(cc);
    assertFalse(cc.isEmpty());

    // the first item will be used for further testing (we assume that a suitable test-database is used)
    testContest = cc.get(0);

    // verify that the one to many relations works (we assume that a suitable test-database is used)
    testContestType = testContest.getContestType();
    List<Contest> contestList = testContestType.getContestList();
    assertNotNull(contestList);
    assertFalse(contestList.isEmpty());
    assertTrue(contestList.contains(testContest));
  }

  @AfterMethod
  public void tearDownMethod() throws Exception {
    entityManager.getTransaction().rollback();
  }

  /**
   * Test of identity method.
   */
  @Test
  public void testIdentity() {
    EntityIdentity expected = new EntityIdentity(testContest.getClass(), testContest.getContestId());
    assertEquals(expected, testContest.identity());
  }

  @Test
  public void testPropertyChangeCallback() throws InterruptedException, InvocationTargetException {

    Contest testItem = new Contest(Integer.MAX_VALUE);
    final TestListener testListener = new TestListener();
    testItem.addPropertyChangeListener(testListener);

    testItem.setName("Didgeridoo");

    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        assertTrue(testListener.called > 0);
      }
    });
  }

  @Test
  public void testSetContestType() {
    // verify that the one to many relations is correctly updated
    Contest testItem = new Contest(Integer.MAX_VALUE);
    testItem.setContestType(testContestType);
    assertTrue(testContestType.getContestList().contains(testItem));

    testItem.setContestType(null);
    assertFalse(testContestType.getContestList().contains(testItem));

  }

  @Test
  public void testSetPerson() throws InterruptedException, InvocationTargetException {
    Person testPerson = new Person();
    Contest testItem = new Contest(Integer.MAX_VALUE);
    entityManager.persist(testPerson);
    entityManager.persist(testItem);
    entityManager.flush();
    // verify that the one to many relations is correctly updated

    testItem.setPerson(testPerson);
    assertTrue(testPerson.getContestList().contains(testItem));

    //verify that callbacks are executed
    final TestListener contestListener = new TestListener();
    testItem.addPropertyChangeListener(contestListener);
    final TestListener personListener = new TestListener();
    testPerson.addPropertyChangeListener(personListener);
    
    testItem.setPerson(null);
    
    assertFalse(testPerson.getContestList().contains(testItem));
    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        assertEquals(contestListener.called ,1);
        assertEquals(personListener.called ,1);
      }
    });
  }

}
