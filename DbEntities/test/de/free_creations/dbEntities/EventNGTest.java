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
public class EventNGTest {

  private class TestListener implements PropertyChangeListener {

    public int called = 0;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      called++;
    }
  }

  static final String PersistenceUnitName = "DbEntitiesPU";
  private static EntityManager entityManager = null;

  private Event testEvent;


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
    TypedQuery<Event> q = entityManager.createNamedQuery("Event.findAll", Event.class);
    List<Event> ee = q.getResultList();
    assertNotNull(ee);
    assertFalse(ee.isEmpty());

    // the first item will be used for further testing (we assume that a suitable test-database is used)
    testEvent = ee.get(0);

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
    EntityIdentity expected = new EntityIdentity(testEvent.getClass(), testEvent.getEventId());
    assertEquals(expected, testEvent.identity());
  }

  @Test
  public void testPropertyChangeCallback() throws InterruptedException, InvocationTargetException {

    Event testItem = new Event(Integer.MAX_VALUE, null, null);
    final TestListener testListener = new TestListener();
    testItem.addPropertyChangeListener(testListener);

    testItem.setScheduled(true);

    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        assertTrue(testListener.called > 0);
      }
    });
  }

  @Test
  public void testSetLocation() throws InterruptedException, InvocationTargetException {
    // verify that the one to many relations is correctly updated
    Event testItem = new Event(Integer.MAX_VALUE, null, null);
    Location testLocation = new Location(Integer.MAX_VALUE);
    entityManager.persist(testLocation);
    entityManager.flush();

    testItem.setLocation(testLocation);
    List<Event> eventList = testLocation.getEventList();
    assertNotNull(eventList);
    assertTrue(eventList.contains(testItem));

    //verify that callbacks are executed
    final TestListener eventListener = new TestListener();
    testItem.addPropertyChangeListener(eventListener);
    final TestListener locationListener = new TestListener();
    testLocation.addPropertyChangeListener(locationListener);

    testItem.setLocation(null);

    assertFalse(testLocation.getEventList().contains(testItem));
    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        assertEquals(eventListener.called, 1);
        assertEquals(locationListener.called, 1);
      }
    });

  }

  @Test
  public void testSetContest() throws InterruptedException, InvocationTargetException {
    // verify that the one to many relations is correctly updated
    Event testItem = new Event(Integer.MAX_VALUE, null, null);
    Contest testContest = new Contest(Integer.MAX_VALUE);
    entityManager.persist(testContest);
    entityManager.flush();

    testItem.setContest(testContest);
    List<Event> eventList = testContest.getEventList();
    assertNotNull(eventList);
    assertTrue(eventList.contains(testItem));

    //verify that callbacks are executed
    final TestListener eventListener = new TestListener();
    testItem.addPropertyChangeListener(eventListener);
    final TestListener locationListener = new TestListener();
    testContest.addPropertyChangeListener(locationListener);

    testItem.setContest(null);

    assertFalse(testContest.getEventList().contains(testItem));
    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        assertEquals(eventListener.called, 1);
        assertEquals(locationListener.called, 1);
      }
    });
  }

  @Test
  public void testSetTimeSlot() throws InterruptedException, InvocationTargetException {
    // verify that the one to many relations is correctly updated
    Event testItem = new Event(Integer.MAX_VALUE, null, null);
    TimeSlot testTimeSlot = new TimeSlot(Integer.MAX_VALUE);
    entityManager.persist(testTimeSlot);
    entityManager.flush();

    testItem.setTimeSlot(testTimeSlot);
    List<Event> eventList = testTimeSlot.getEventList();
    assertNotNull(eventList);
    assertTrue(eventList.contains(testItem));

    //verify that callbacks are executed
    final TestListener eventListener = new TestListener();
    testItem.addPropertyChangeListener(eventListener);
    final TestListener locationListener = new TestListener();
    testTimeSlot.addPropertyChangeListener(locationListener);

    testItem.setTimeSlot(null);

    assertFalse(testTimeSlot.getEventList().contains(testItem));
    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        assertEquals(eventListener.called, 1);
        assertEquals(locationListener.called, 1);
      }
    });
  }

}
