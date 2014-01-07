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
 * @author Harald Postner<harald at free-creations.de>
 */
public class AllocationNGTest {

  private class TestListener implements PropertyChangeListener {

    public int called = 0;
    public String lastEvt;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      called++;
      lastEvt = evt.getPropertyName();
    }
  }

  static final String PersistenceUnitName = "DbEntitiesPU";
  private static EntityManager entityManager = null;

  private Allocation testAllocation;
  private Job testJob;
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
    TypedQuery<Allocation> q = entityManager.createNamedQuery("Allocation.findAll", Allocation.class);
    List<Allocation> aa = q.getResultList();
    assertNotNull(aa);
    assertFalse(aa.isEmpty());

    // the first item will be used for further testing (we assume that a suitable test-database is used)
    testAllocation = aa.get(0);
    testJob = testAllocation.getJob();
    assertNotNull(testJob);
    testEvent = testAllocation.getEvent();
    assertNotNull(testEvent);

  }

  @AfterMethod
  public void tearDownMethod() throws Exception {
    entityManager.getTransaction().rollback();
  }

  /**
   * Test of identity method.
   */
  @Test(enabled = true)
  public void testIdentity() {
    EntityIdentity expected = new EntityIdentity(testAllocation.getClass(), testAllocation.getAllocationId());
    assertEquals(expected, testAllocation.identity());
  }

  @Test
  public void testPropertyChangeCallback() throws InterruptedException, InvocationTargetException {

    Allocation testItem = new Allocation(Integer.MAX_VALUE);
    final TestListener testListener = new TestListener();
    testItem.addPropertyChangeListener(testListener);

    testItem.setNote("bla bla");
    testItem.setPlanner("Dschingis Khan");
    final int expectedCallbacks = 2;

    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        assertEquals(testListener.called, expectedCallbacks);
      }
    });
  }

  @Test(enabled = true)
  public void testnewAllocation() throws InterruptedException, InvocationTargetException {
    // verify that the one to many relations are correctly updated

    Person testPerson = new Person(Integer.MAX_VALUE);
    entityManager.persist(testPerson);
    entityManager.flush();

    Allocation testItem = Allocation.newAllocation(entityManager, testPerson, testEvent, testJob);
    Integer allocationId = testItem.getAllocationId();
    assertNotNull(allocationId);

    List<Allocation> pAllocationList = testPerson.getAllocationList();
    assertNotNull(pAllocationList);
    assertTrue(pAllocationList.contains(testItem));

    List<Allocation> eAllocationList = testEvent.getAllocationList();
    assertNotNull(eAllocationList);
    assertTrue(eAllocationList.contains(testItem));

    List<Allocation> jAllocationList = testJob.getAllocationList();
    assertNotNull(jAllocationList);
    assertTrue(jAllocationList.contains(testItem));

//    testItem.remove(entityManager);
//
//    pAllocationList = testPerson.getAllocationList();
//    assertFalse(pAllocationList.contains(testItem));
//
//    eAllocationList = testEvent.getAllocationList();
//    assertFalse(eAllocationList.contains(testItem));
//
//    jAllocationList = testJob.getAllocationList();
//    assertFalse(jAllocationList.contains(testItem));
//
//    Allocation find = entityManager.find(Allocation.class, allocationId);
//    assertNull(find);
  }

  @Test(enabled = true)
  public void testQuery_findByPersonAndTimeslot() {
    //prepare the database
    Person testPerson = new Person(Integer.MAX_VALUE);
    entityManager.persist(testPerson);
    entityManager.flush();
    TimeSlot testTimeSlot = testEvent.getTimeSlot();
    Allocation testItem = Allocation.newAllocation(entityManager, testPerson, testEvent, testJob);

    TypedQuery<Allocation> query = entityManager.createNamedQuery("Allocation.findByPersonAndTimeslot", Allocation.class);
    query.setParameter("timeSlot", testTimeSlot);
    query.setParameter("person", testPerson);
    List<Allocation> resultList = query.getResultList();

    assertNotNull(resultList);
    assertEquals(resultList.size(), 1);

    Allocation resultItem = resultList.get(0);
    assertTrue(Objects.equals(resultItem, testItem));

  }

  @Test(enabled = true)
  public void testQuery_findByPersonAndTimeslot_2() {
    //what happens if one or both parameters are null?

    TypedQuery<Allocation> query = entityManager.createNamedQuery("Allocation.findByPersonAndTimeslot", Allocation.class);
    query.setParameter("timeSlot", null);
    query.setParameter("person", null);
    List<Allocation> resultList = query.getResultList();
    assertNotNull(resultList);
    assertTrue(resultList.isEmpty());

  }

  @Test(enabled = true)
  public void testQuery_findByPersonAndTimeslot_3() {
    //what happens if the entity does not exist
    Person testPerson = new Person(Integer.MAX_VALUE);
    entityManager.persist(testPerson);
    entityManager.flush();
    TimeSlot testTimeSlot = testEvent.getTimeSlot();
    // Allocation.newAllocation(entityManager, testPerson, testEvent, testJob) removed!;

    TypedQuery<Allocation> query = entityManager.createNamedQuery("Allocation.findByPersonAndTimeslot", Allocation.class);
    query.setParameter("timeSlot", testTimeSlot);
    query.setParameter("person", testPerson);
    List<Allocation> resultList = query.getResultList();

    assertNotNull(resultList);
    assertTrue(resultList.isEmpty());

  }

  @Test(enabled = true)
  public void testQuery_findByEventAndJob() {
    //prepare the database
    Person testPerson = new Person(Integer.MAX_VALUE);
    entityManager.persist(testPerson);
    entityManager.flush();

    Job tempJob = new Job("TEMP", testJob.getJobType());
    entityManager.persist(tempJob);
    entityManager.flush();

    Allocation testItem = Allocation.newAllocation(entityManager, testPerson, testEvent, tempJob);

    TypedQuery<Allocation> query = entityManager.createNamedQuery("Allocation.findByEventAndJob", Allocation.class);
    query.setParameter("event", testEvent);
    query.setParameter("job", tempJob);
    List<Allocation> resultList = query.getResultList();

    assertNotNull(resultList);
    assertEquals(resultList.size(), 1);

    Allocation resultItem = resultList.get(0);
    assertTrue(Objects.equals(resultItem, testItem));

  }

  /**
   * Test of newAllocation method, of class Allocation.
   */
  @Test
  public void testNewAllocation() {
    Person testPerson = new Person(Integer.MAX_VALUE);
    entityManager.persist(testPerson);
    entityManager.flush();

    Job tempJob = new Job("TEMP", testJob.getJobType());
    entityManager.persist(tempJob);
    entityManager.flush();

    Allocation newItem = Allocation.newAllocation(entityManager, testPerson, testEvent, tempJob);
    assertNotNull(newItem);
    assertEquals(testPerson, newItem.getPerson());
    assertEquals(testEvent, newItem.getEvent());
    assertEquals(tempJob, newItem.getJob());

  }

  @Test
  public void testNewAllocationPropertyChangeCallback() throws Throwable {

    // preparation ---
    Person testPerson = new Person(Integer.MAX_VALUE);
    entityManager.persist(testPerson);
    entityManager.flush();

    Job tempJob = new Job("TEMP", testJob.getJobType());
    entityManager.persist(tempJob);
    entityManager.flush();

    final TestListener testListener = new TestListener();
    testPerson.addPropertyChangeListener(testListener);

    // the statement we want to test:
    Allocation newItem = Allocation.newAllocation(entityManager, testPerson, testEvent, tempJob);

    // verification
    final int expectedCallbackCount = 1;

    try {
      EventQueue.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          assertEquals(testListener.called, expectedCallbackCount);
          assertEquals(testListener.lastEvt, Person.PROP_ALLOCATIONADDED);
        }
      });
    } catch (InvocationTargetException ex) {
      throw ex.getCause();
    }
  }

  /**
   * Test of remove method, of class Allocation.
   */
  @Test
  public void testRemove() {
    Person testPerson = testAllocation.getPerson();
    testAllocation.remove(entityManager);

    Allocation find = entityManager.find(Allocation.class, testAllocation.getAllocationId());
    assertNull(find);

    List<Allocation> pAllocationList = testPerson.getAllocationList();
    assertFalse(pAllocationList.contains(testAllocation));

    List<Allocation> eAllocationList = testEvent.getAllocationList();
    assertFalse(eAllocationList.contains(testAllocation));

    List<Allocation> jAllocationList = testJob.getAllocationList();
    assertFalse(jAllocationList.contains(testAllocation));
  }

  @Test
  public void testRemovePropertyChangeCallback() throws Throwable {

    Person testPerson = testAllocation.getPerson();
    testAllocation.remove(entityManager);

    final TestListener testListener = new TestListener();
    testPerson.addPropertyChangeListener(testListener);

    testAllocation.remove(entityManager);

    final int expectedCallbackCount = 1;

    try {
      EventQueue.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          assertEquals(testListener.called, expectedCallbackCount);
          assertEquals(testListener.lastEvt, Person.PROP_ALLOCATIONREMOVED);
        }
      });
    } catch (InvocationTargetException ex) {
      throw ex.getCause();
    }
  }

  /**
   * Test of getAllocationId method, of class Allocation.
   */
  @Test
  public void testGetAllocationId() {
  }

  /**
   * Test of getLastchange method, of class Allocation.
   */
  @Test
  public void testGetLastchange() {
  }

  /**
   * Test of setLastchange method, of class Allocation.
   */
  @Test
  public void testSetLastchange() {
  }

  /**
   * Test of getPlanner method, of class Allocation.
   */
  @Test
  public void testGetPlanner() {
  }

  /**
   * Test of setPlanner method, of class Allocation.
   */
  @Test
  public void testSetPlanner() {
  }

  /**
   * Test of getNote method, of class Allocation.
   */
  @Test
  public void testGetNote() {
  }

  /**
   * Test of setNote method, of class Allocation.
   */
  @Test
  public void testSetNote() {
  }

  /**
   * Test of getPerson method, of class Allocation.
   */
  @Test
  public void testGetPerson() {
  }

  /**
   * Test of getJob method, of class Allocation.
   */
  @Test
  public void testGetJob() {
  }

  /**
   * Test of setJob method, of class Allocation.
   */
  @Test
  public void testSetJob() {
  }

  /**
   * Test of setPerson method, of class Allocation.
   */
  @Test
  public void testSetPerson() {
  }

  /**
   * Test of setEvent method, of class Allocation.
   */
  @Test
  public void testSetEvent() {
  }

  /**
   * Test of getEvent method, of class Allocation.
   */
  @Test
  public void testGetEvent() {
  }

  /**
   * Test of hashCode method, of class Allocation.
   */
  @Test
  public void testHashCode() {
  }

  /**
   * Test of equals method, of class Allocation.
   */
  @Test
  public void testEquals() {
  }

  /**
   * Test of toString method, of class Allocation.
   */
  @Test
  public void testToString() {
  }

  /**
   * Test of addPropertyChangeListener method, of class Allocation.
   */
  @Test
  public void testAddPropertyChangeListener_PropertyChangeListener() {
  }

  /**
   * Test of addPropertyChangeListener method, of class Allocation.
   */
  @Test
  public void testAddPropertyChangeListener_PropertyChangeListener_Integer() {
  }

  /**
   * Test of removePropertyChangeListener method, of class Allocation.
   */
  @Test
  public void testRemovePropertyChangeListener_PropertyChangeListener() {
  }

  /**
   * Test of removePropertyChangeListener method, of class Allocation.
   */
  @Test
  public void testRemovePropertyChangeListener_PropertyChangeListener_Integer() {
  }
}
