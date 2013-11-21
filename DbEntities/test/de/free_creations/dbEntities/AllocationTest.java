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
public class AllocationTest {

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
  private Allocation testAllocation;
  private Person testPerson;

  @Before
  public void setUp() {
    entityManager.getTransaction().begin();
    TypedQuery<Allocation> qt = entityManager.createNamedQuery("Allocation.findAll", Allocation.class);
    List<Allocation> tt = qt.getResultList();
    assertNotNull(tt);
    assertFalse(tt.isEmpty());
    testAllocation = tt.get(0);

    TypedQuery<Person> qp = entityManager.createNamedQuery("Person.findAll", Person.class);
    List<Person> pp = qp.getResultList();
    assertNotNull(pp);
    assertFalse(pp.isEmpty());
    testPerson = pp.get(0);

  }

  @After
  public void tearDown() {
    entityManager.getTransaction().rollback();
    testAllocation = null;
  }

  /**
   * Test of identity method, of class Allocation.
   */
  @Test
  public void testIdentity() {
    EntityIdentity expected = new EntityIdentity(testAllocation.getClass(), testAllocation.getAllocationid());
    assertEquals(expected, testAllocation.identity());
  }

  @Test
  public void testSetGetPerson() {
    Person p = testAllocation.getPersonid();
    assertNotNull("Oops this is a bad test item.", p);
    assertTrue(p.getTeameinteilungList().contains(testAllocation));

    testAllocation.setPersonid(null);
    assertFalse(p.getTeameinteilungList().contains(testAllocation));

    testAllocation.setPersonid(p);
    assertTrue(p.getTeameinteilungList().contains(testAllocation));

    testAllocation.prepareRemoval();
    assertFalse(p.getTeameinteilungList().contains(testAllocation));
  }

  @Test
  public void testCreationDestruction() {
    TimeSlot z = new TimeSlot(1001);
    entityManager.persist(z);

    Contest j = new Contest();
    entityManager.persist(j);

    Job f = new Job("TestF");
    entityManager.persist(f);
    entityManager.flush(); // give f,j,z their primary keys.

    Allocation testItem = new Allocation(z, j, f);
    testItem.setPersonid(testPerson);
    entityManager.persist(testItem);

    entityManager.flush();

    assertTrue(z.getTeameinteilungList().contains(testItem));
    assertTrue(j.getTeameinteilungList().contains(testItem));
    assertTrue(f.getTeameinteilungList().contains(testItem));
    assertTrue(testPerson.getTeameinteilungList().contains(testItem));

    testItem.prepareRemoval();

    assertFalse(z.getTeameinteilungList().contains(testItem));
    assertFalse(j.getTeameinteilungList().contains(testItem));
    assertFalse(f.getTeameinteilungList().contains(testItem));
    assertFalse(testPerson.getTeameinteilungList().contains(testItem));



  }
}