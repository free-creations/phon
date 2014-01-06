/*
 * Copyright 2014 Harald Postner<harald at free-creations.de>.
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

import de.free_creations.dbEntities.Availability;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.TimeSlot;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AvailabilityCollectionTest {

  public AvailabilityCollectionTest() {
  }

  @Before
  public void setUp()  {
    assertTrue("did you start the database-server?", Manager.isOpen());
  }

  @After
  public void tearDown() {
    Manager.close(false);
  }

  /**
   * Test of getAll method, of class AvailabilityCollection.
   */
  @Test
  public void testGetAll() {
  }

  /**
   * Test of findEntity method, of class AvailabilityCollection.
   */
  @Test
  public void testFindEntity_Integer()  {
  }

  /**
   * Test of findEntity method, of class AvailabilityCollection.
   * @throws java.lang.Exception
   */
  @Test
  public void testFindEntity_Person_TimeSlot() throws Exception {
    AvailabilityCollection availabilityCollection = Manager.getAvailabilityCollection();
    List<Availability> ee = availabilityCollection.getAll();
    Availability testAvailability = ee.get(0);
    Person p = testAvailability.getPerson();
    assertNotNull("Bad test Data?", p);
    TimeSlot t = testAvailability.getTimeSlot();
    assertNotNull("Bad test Data?", t);

    // Here is the statement that we test:
    Availability result = availabilityCollection.findEntity(p, t);
    assertNotNull(result);
    assertEquals(testAvailability, result);

  }

  /**
   * Test of findAll method, of class AvailabilityCollection.
   */
  @Test
  public void testFindAll()  {
  }

  /**
   * Test of removeEntity method, of class AvailabilityCollection.
   */
  @Test
  public void testRemoveEntity()  {
  }

  /**
   * Test of addPropertyChangeListener method, of class AvailabilityCollection.
   */
  @Test
  public void testAddPropertyChangeListener() {
  }

  /**
   * Test of removePropertyChangeListener method, of class
   * AvailabilityCollection.
   */
  @Test
  public void testRemovePropertyChangeListener() {
  }

  /**
   * Test of newEntity method, of class AvailabilityCollection.
   */
  @Test
  public void testNewEntity()  {
  }

}
