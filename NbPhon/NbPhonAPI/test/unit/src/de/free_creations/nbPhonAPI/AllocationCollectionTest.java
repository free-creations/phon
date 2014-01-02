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

import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.TimeSlot;
import java.util.List;
import java.util.Objects;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harald Postner<harald at free-areations.de>
 */
public class AllocationCollectionTest {

  private AllocationCollection allocationCollection;

  public AllocationCollectionTest() {
  }

  @Before
  public void setUp() throws Exception {
    assertTrue("did you start the database-server?", Manager.isOpen());
    allocationCollection = Manager.getAllocationCollection();
    assertNotNull(allocationCollection);
  }

  @After
  public void tearDown() {
    Manager.close(false);
  }

  /**
   * Test of getAll method, of class AllocationCollection.
   */
  @Test
  public void testGetAll() {
    List<Allocation> cc = allocationCollection.getAll();
    assertNotNull(cc);
    assertFalse(cc.isEmpty());
  }

  /**
   * Test of findEntity method, of class AllocationCollection.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testFindEntity_Integer() throws Exception {
    // we assume that the test database contains at least one record
    Allocation c = allocationCollection.findEntity(1);
    assertNotNull("Bad test data?", c);
    assertEquals((int) c.getAllocationId(), 1);
  }

  /**
   * Test of findEntity method, of class AllocationCollection.
   */
  @Test
  public void testFindEntity_Person_TimeSlot() throws Exception {
    // check whether the database is set-up as expected.
    Person p = Manager.getPersonCollection().findEntity(1);
    assertNotNull("Bad test data?", p);
    List<Allocation> pa = p.getAllocationList();
    assertFalse("Bad test data?", pa.isEmpty());
    Allocation a = pa.get(0);
    Event e = a.getEvent();
    assertNotNull("Bad test data?", e);
    TimeSlot t = e.getTimeSlot();
    assertNotNull("Bad test data?", t);

    // Here is the statement that we want to test:
    Allocation result = Manager.getAllocationCollection().findEntity(p, t);
    assertNotNull(result);
    assertTrue(Objects.equals(result, a));

  }

  /**
   * Test of newEntity method, of class AllocationCollection.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testNewEntity() throws Exception {

  }

  /**
   * Test of addPropertyChangeListener method, of class AllocationCollection.
   */
  @Test
  public void testAddPropertyChangeListener() {
    // trivial
  }

  /**
   * Test of removePropertyChangeListener method, of class AllocationCollection.
   */
  @Test
  public void testRemovePropertyChangeListener() {
    // trivial
  }

  /**
   * Test of newEntity method, of class AllocationCollection.
   */
  @Test
  public void testNewEntity_0args() {
    // newEntity() is not implemented!
  }

  /**
   * Test of newEntity method, of class AllocationCollection.
   *
   * @throws de.free_creations.nbPhonAPI.DataBaseNotReadyException
   */
  @Test
  public void testNewEntity_3args() throws DataBaseNotReadyException {
    // Verify that our test database is set-up as expected.
    // Access test-person , test-event and test-job.
    // Verify that such items exist in the database
    // and that the test-person has yet no allocations.
    Person p = Manager.getPersonCollection().findEntity(2);
    assertNotNull("Bad test data?", p);
    Event e = Manager.getEventCollection().findEntity(2);
    assertNotNull("Bad test data?", e);
    Job j = Manager.getJobCollection().findEntity(2);
    assertNotNull("Bad test data?", j);
    List<Allocation> allocationListBefore = p.getAllocationList();
    assertNotNull(allocationListBefore);
    assertTrue("Bad test data?", allocationListBefore.isEmpty());

    // Here is the statement that we want to test:
    Allocation testItem = allocationCollection.newEntity(p, e, j);

    // Verify that the result is as expected.
    assertNotNull(testItem);
    assertNotNull(testItem.getAllocationId());
    assertTrue(testItem.getAllocationId() > 0);

    assertEquals(p, testItem.getPerson());
    assertEquals(e, testItem.getEvent());
    assertEquals(j, testItem.getJob());

    List<Allocation> allocationListAfter = p.getAllocationList();
    assertTrue(allocationListAfter.contains(testItem));
  }

  /**
   * Test of newEntity method, of class AllocationCollection.
   *
   * @throws de.free_creations.nbPhonAPI.DataBaseNotReadyException
   */
  @Test(expected = javax.persistence.PersistenceException.class)
  public void testNewEntity_Boom() throws DataBaseNotReadyException {
    // Verify that our test database is set-up as expected.
    // Access test-person , test-event and test-job.
    // Verify that such items exist in the database
    // and that the test-person has yet no allocations.
    Person p = Manager.getPersonCollection().findEntity(3);
    assertNotNull("Bad test data?", p);
    Event e = Manager.getEventCollection().findEntity(4);
    assertNotNull("Bad test data?", e);
    Job j1 = Manager.getJobCollection().findEntity(1);
    assertNotNull("Bad test data?", j1);
    Job j2 = Manager.getJobCollection().findEntity(2);
    assertNotNull("Bad test data?", j2);

    Allocation testItem1 = allocationCollection.newEntity(p, e, j1);

    // Here is the statement that should throw the exception:
    Allocation testItem2 = allocationCollection.newEntity(p, e, j2);

  }

  /**
   * Test of removeEntity method, of class AllocationCollection.
   *
   * @throws de.free_creations.nbPhonAPI.DataBaseNotReadyException
   */
  @Test
  public void testRemoveEntity_Integer() throws DataBaseNotReadyException {
    // we assume that the test database contains at least one record
    // and that this allocation has some members attached.
    Integer key = 1;
    Allocation a = allocationCollection.findEntity(key);
    assertNotNull("Bad test data?", a);
    Person p = a.getPerson();
    assertNotNull("Bad test data?", p);

    allocationCollection.removeEntity(key);

    assertNull(allocationCollection.findEntity(key));
    for (Allocation pa : p.getAllocationList()) {
      assertFalse(Objects.equals(a, pa));
    }
  }

  /**
   * Test of removeEntity method, of class AllocationCollection.
   *
   * @throws de.free_creations.nbPhonAPI.DataBaseNotReadyException
   */
  @Test
  public void testRemoveEntity_Allocation() throws DataBaseNotReadyException {
    // we assume that the test database contains at least one record
    // and that this allocation has some members attached.
    Allocation a = allocationCollection.findEntity(1);
    assertNotNull("Bad test data?", a);
    Person p = a.getPerson();
    assertNotNull("Bad test data?", p);

    allocationCollection.removeEntity(a);

    assertNull(allocationCollection.findEntity(1));
    for (Allocation pa : p.getAllocationList()) {
      assertFalse(Objects.equals(a, pa));
    }
  }

}
