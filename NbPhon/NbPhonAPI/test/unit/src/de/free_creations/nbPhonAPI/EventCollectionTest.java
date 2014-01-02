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

import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Location;
import de.free_creations.dbEntities.TimeSlot;
import java.util.List;
import java.util.Objects;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class EventCollectionTest {

  public EventCollectionTest() {
  }

  /**
   * Test of getAll method, of class EventCollection.
   */
  @Test
  public void testGetAll() {
    assertTrue("did you start the database-server?", Manager.isOpen());
    EventCollection eventCollection = Manager.getEventCollection();
    assertNotNull(eventCollection);
    List<Event> result = eventCollection.getAll();
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  /**
   * Test of findEntity method, of class EventCollection.
   */
  @Test
  public void testFindEntity_Integer() throws Exception {
  }

  /**
   * Test of findEntity method, of class EventCollection.
   */
  @Test
  public void testFindEntity_Location_TimeSlot() throws Exception {
    assertTrue("did you start the database-server?", Manager.isOpen());
    EventCollection eventCollection = Manager.getEventCollection();
    List<Event> ee = eventCollection.getAll();
    Event testEvent = ee.get(0);
    Location l = testEvent.getLocation();
    assertNotNull("Bad test Data?", l);
    TimeSlot t = testEvent.getTimeSlot();
    assertNotNull("Bad test Data?", t);

    // Here is the statement that we test:
    Event result = eventCollection.findEntity(l, t);
    assertNotNull(result);
    assertTrue(Objects.equals(testEvent, result));

  }

  /**
   * Test of findEntity method, of class EventCollection.
   */
  @Test
  public void testFindEntity_Contest_TimeSlot() throws Exception {
    assertTrue("did you start the database-server?", Manager.isOpen());
    EventCollection eventCollection = Manager.getEventCollection();
    List<Event> ee = eventCollection.getAll();
    Event testEvent = ee.get(0);
    Contest c = testEvent.getContest();
    assertNotNull("Bad test Data?", c);
    TimeSlot t = testEvent.getTimeSlot();
    assertNotNull("Bad test Data?", t);

    // Here is the statement that we test:
    Event result = eventCollection.findEntity(c, t);
    assertNotNull(result);
    assertTrue(Objects.equals(testEvent, result));
  }

  /**
   * Test of newEntity method, of class EventCollection.
   */
  @Test
  public void testNewEntity_Contest_TimeSlot() throws Exception {
  }

  /**
   * Test of removeEntity method, of class EventCollection.
   */
  @Test
  public void testRemoveEntity() throws Exception {
  }

  /**
   * Test of addPropertyChangeListener method, of class EventCollection.
   */
  @Test
  public void testAddPropertyChangeListener() {
  }

  /**
   * Test of removePropertyChangeListener method, of class EventCollection.
   */
  @Test
  public void testRemovePropertyChangeListener() {
  }

  /**
   * Test of newEntity method, of class EventCollection.
   */
  @Test
  public void testNewEntity_0args() throws Exception {
  }

}
