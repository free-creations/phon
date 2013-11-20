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
import javax.persistence.Query;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class TimeSlotTest {

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
  private Query query;
  private TimeSlot testZeit;
  private Person testPerson;

  @Before
  public void setUp() {
    entityManager.getTransaction().begin();
    query = entityManager.createNamedQuery("TimeSlot.findAll");
    List<TimeSlot> zz = query.getResultList();
    assertNotNull(zz);
    assertTrue(zz.size() > 1);
    testZeit = zz.get(0);

    query = entityManager.createNamedQuery("Person.findAll");
    List<Person> pp = query.getResultList();
    assertNotNull(pp);
    assertTrue(pp.size() > 1);
    testPerson = pp.get(0);
  }

  @After
  public void tearDown() {
    entityManager.getTransaction().rollback();
    query = null;
    testZeit = null;
  }

  /**
   */
  @Test
  public void testAddVerfuegbarkeit() {

    Availability v1 = new Availability();
    v1.setPersonid(testPerson);
    entityManager.persist(v1);
    v1.setZeitid(testZeit);
    //testZeit.addVerfuegbarkeit(v1);
    entityManager.flush(); //

    Availability v2 = new Availability();
    v2.setPersonid(testPerson);
    entityManager.persist(v2);
    v2.setZeitid(testZeit);
   // testZeit.addVerfuegbarkeit(v2);
    entityManager.flush(); //

    assertTrue(testZeit.getVerfuegbarkeitList().contains(v1));
    assertEquals(testZeit, v1.getZeitid());
  }

  /**
   * A disponibility record (Availability) can only be
 added to a persisted time-slot record. 
   * 
   * An attempt to add to an un-persisted time-slot results in an
   * exception as this test demonstrates.
   */
  @Test(expected=RuntimeException.class)  
  public void testAddVerfuegbarkeitToNewTimeSlot() {

    TimeSlot newZeit = new TimeSlot();
    newZeit.setZeitid(12345);


    Availability v1 = new Availability();
    v1.setPersonid(testPerson);
    entityManager.persist(v1);
    newZeit.addVerfuegbarkeit(v1); //<<< boom

  }
}