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
package de.free_creations.nbPhonAPI;

import de.free_creations.dbEntities.TimeSlot;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class TimeSlotCollectionTest {

  public TimeSlotCollectionTest() {
  }

  @Before
  public void setUp() throws Exception {
  }

  /**
   * Test of getAll method, of class TimeSlotCollection.
   */
  @Test
  public void testGetAll() {
    assertTrue("did you start the database-server?", Manager.isOpen());
    TimeSlotCollection timeSlotCollection = Manager.getTimeSlotCollection();
    assertNotNull(timeSlotCollection);
    List<TimeSlot> result = timeSlotCollection.getAll();
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  /**
   * Test of dayNames method, of class TimeSlotCollection.
   */
  @Test
  public void testTimeOfDayNames() {
    System.out.println("testTimeOfDayNames");
    assertTrue("did you start the database-server?", Manager.isOpen());
    TimeSlotCollection timeSlotCollection = Manager.getTimeSlotCollection();
    String[] timeOfDayNames = timeSlotCollection.timeOfDayNames();
    assertNotNull(timeOfDayNames);
    assertTrue(timeOfDayNames.length > 0);
    for (String s : timeOfDayNames) {
      System.out.println("..." + s);
    }
  }

  /**
   * Test of dayNames method, of class TimeSlotCollection.
   */
  @Test
  public void testDayNames() {
    System.out.println("testDayNames");
    assertTrue("did you start the database-server?", Manager.isOpen());
    TimeSlotCollection timeSlotCollection = Manager.getTimeSlotCollection();
    String[] dayNames = timeSlotCollection.dayNames();
    assertNotNull(dayNames);
    assertTrue(dayNames.length > 0);
    for (String s : dayNames) {
      System.out.println("..." + s);
    }
  }

  /**
   * Test of findEntity method, of class TimeSlotCollection.
   */
  @Test
  public void testFindEntity_int_int() throws Exception {
    System.out.println("testFindEntity_int_int");
    assertTrue("did you start the database-server?", Manager.isOpen());
    TimeSlotCollection timeSlotCollection = Manager.getTimeSlotCollection();
    int dayCount = timeSlotCollection.dayNames().length;
    int timeOfDayCount = timeSlotCollection.timeOfDayNames().length;

    for (int day = 0; day < dayCount; day++) {
      for (int timeOfDay = 0; timeOfDay < timeOfDayCount; timeOfDay++) {
        TimeSlot t = timeSlotCollection.findEntity(day, timeOfDay);
        assertEquals(day+1, (int) t.getDayIdx());
        assertEquals(timeOfDay+1, (int) t.getTimeOfDayIdx());
      }
    }
    
    TimeSlot t = timeSlotCollection.findEntity(-1, -1);
    assertNull(t);
  }

  /**
   * Test of findEntity method, of class TimeSlotCollection.
   */
  @Test
  public void testFindEntity_Integer() throws Exception {
  }
}