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
public class AllocationNGTest {

  private class TestListener implements PropertyChangeListener {

    public int called = 0;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      called++;
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
  @Test
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

    testItem.remove(entityManager);

    pAllocationList = testPerson.getAllocationList();
    assertFalse(pAllocationList.contains(testItem));

    eAllocationList = testEvent.getAllocationList();
    assertFalse(eAllocationList.contains(testItem));

    jAllocationList = testJob.getAllocationList();
    assertFalse(jAllocationList.contains(testItem));
    
    
    Allocation find = entityManager.find(Allocation.class, allocationId);
    assertNull(find);

  }

}
