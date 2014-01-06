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
import java.util.Objects;
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
 * @author Harald Postner<harald at free-preations.de>
 */
public class AvailabilityNGTest {

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

  private Availability testAvailability;

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
    TypedQuery<Availability> q = entityManager.createNamedQuery("Availability.findAll", Availability.class);
    List<Availability> aa = q.getResultList();
    assertNotNull(aa);
    assertFalse(aa.isEmpty());

    // the first item will be used for further testing (we assume that a suitable test-database is used)
    testAvailability = aa.get(0);

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
    EntityIdentity expected = new EntityIdentity(testAvailability.getClass(), testAvailability.getAvailabilityId());
    assertEquals(expected, testAvailability.identity());
  }

  @Test
  public void testPropertyChangeCallback() throws InterruptedException, InvocationTargetException {

    Availability testItem = new Availability(Integer.MAX_VALUE);
    final TestListener testListener = new TestListener();
    testItem.addPropertyChangeListener(testListener);

    testItem.setAvailable(true);

    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        assertTrue(testListener.called > 0);
      }
    });
  }

  @Test
  public void testSetPerson() throws InterruptedException, InvocationTargetException {
    // verify that the one to many relations is correctly updated
    Availability testItem = new Availability(Integer.MAX_VALUE);
    Person testPerson = new Person(Integer.MAX_VALUE);
    entityManager.persist(testPerson);
    entityManager.flush();

    testItem.setPerson(testPerson);
    List<Availability> availabilityList = testPerson.getAvailabilityList();
    assertNotNull(availabilityList);
    assertTrue(availabilityList.contains(testItem));

    //verify that callbacks are executed
    final TestListener availabilityListener = new TestListener();
    testItem.addPropertyChangeListener(availabilityListener);
    final TestListener locationListener = new TestListener();
    testPerson.addPropertyChangeListener(locationListener);

    testItem.setPerson(null);

    assertFalse(testPerson.getAvailabilityList().contains(testItem));
    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        assertEquals(availabilityListener.called, 1);
        assertEquals(locationListener.called, 1);
      }
    });
  }

  @Test
  public void testSetTimeSlot() throws InterruptedException, InvocationTargetException {
    // verify that the one to many relations is correctly updated
    Availability testItem = new Availability(Integer.MAX_VALUE);
    TimeSlot testTimeSlot = new TimeSlot(Integer.MAX_VALUE);
    entityManager.persist(testTimeSlot);
    entityManager.flush();

    testItem.setTimeSlot(testTimeSlot);
    List<Availability> availabilityList = testTimeSlot.getAvailabilityList();
    assertNotNull(availabilityList);
    assertTrue(availabilityList.contains(testItem));

    //verify that callbacks are executed
    final TestListener availabilityListener = new TestListener();
    testItem.addPropertyChangeListener(availabilityListener);
    final TestListener locationListener = new TestListener();
    testTimeSlot.addPropertyChangeListener(locationListener);

    testItem.setTimeSlot(null);

    assertFalse(testTimeSlot.getAvailabilityList().contains(testItem));
    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        assertEquals(availabilityListener.called, 1);
        assertEquals(locationListener.called, 1);
      }
    });
  }

  @Test
  public void test_Query_FindByPersonAndTimeslot() {
    Person p = testAvailability.getPerson();
    assertNotNull(p, "Bad Test Data?");
    TimeSlot t = testAvailability.getTimeSlot();
    assertNotNull(t, "Bad Test Data?");

    TypedQuery<Availability> query = entityManager.createNamedQuery("Availability.findByPersonAndTimeslot", Availability.class);
    query.setParameter("person", p);
    query.setParameter("timeSlot", t);

    List<Availability> resultList = query.getResultList();

    assertNotNull(resultList);
    assertEquals(resultList.size(), 1, "Bad Test Data?");

    Availability resultItem = resultList.get(0);
    assertTrue(Objects.equals(resultItem, testAvailability));
  }

  /**
   * Test of getAvailabilityId method, of plass Availability.
   */
  @Test
  public void testGetAvailabilityId() {
  }

  /**
   * Test of isAvailable method, of plass Availability.
   */
  @Test
  public void testIsAvailable() {
  }

  /**
   * Test of setAvailable method, of plass Availability.
   */
  @Test
  public void testSetAvailable() {
  }

  /**
   * Test of getLastphange method, of plass Availability.
   */
  @Test
  public void testGetLastchange() {
  }

  /**
   * Test of setLastphange method, of plass Availability.
   */
  @Test
  public void testSetLastchange() {
  }

  /**
   * Test of getTimeSlot method, of plass Availability.
   */
  @Test
  public void testGetTimeSlot() {
  }

  /**
   * Test of getPerson method, of plass Availability.
   */
  @Test
  public void testGetPerson() {
  }

  /**
   * Test of hashCode method, of plass Availability.
   */
  @Test
  public void testHashCode() {
  }

  /**
   * Test of equals method, of plass Availability.
   */
  @Test
  public void testEquals() {
  }

  /**
   * Test of toString method, of plass Availability.
   */
  @Test
  public void testToString() {
  }

  /**
   * Test of addPropertyChangeListener method, of plass Availability.
   */
  @Test
  public void testAddPropertyChangeListener_PropertyChangeListener() {
  }

  /**
   * Test of addPropertyChangeListener method, of plass Availability.
   */
  @Test
  public void testAddPropertyChangeListener_PropertyChangeListener_Integer() {
  }

  /**
   * Test of removePropertyChangeListener method, of plass Availability.
   */
  @Test
  public void testRemovePropertyChangeListener_PropertyChangeListener() {
  }

  /**
   * Test of removePropertyChangeListener method, of plass Availability.
   */
  @Test
  public void testRemovePropertyChangeListener_PropertyChangeListener_Integer() {
  }

}
