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
package de.free_creations.actions.contest;


import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Location;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhonAPI.Manager;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AllocateLocationForEventTest {

  public AllocateLocationForEventTest() {
  }

  @Before
  public void setUp() throws Exception {
    assertTrue("did you start the database-server?", Manager.isOpen());
  }

  @After
  public void tearDown() {
    Manager.close(false);
  }

  /**
   * Test of level method, of class AllocateLocationForEvent.
   */
  @Test
  public void testLevel() {
  }

  /**
   * Test of problemDescription method, of class AllocateLocationForEvent.
   */
  @Test
  public void testProblemDescription() {
  }

  /**
   * Test of proposedSolution method, of class AllocateLocationForEvent.
   */
  @Test
  public void testProposedSolution() {
  }

  /**
   * Test of workaroundCount method, of class AllocateLocationForEvent.
   */
  @Test
  public void testWorkaroundCount() {
  }

  /**
   * Test of apply method, of class AllocateLocationForEvent.
   * 
   * Rule: if an other event happens at the given time in the given 
   * location it has to be de-located.
   */
  @Test
  public void testApply() throws Exception {
    // we assume that our test database contains at least two
    // one located event.
    
    //first search a candidate for de-location.
    List<Event> all = Manager.getEventCollection().getAll();
    Event otherEvent = null;
    for(Event e: all){
      if(e.getLocation() != null){
        otherEvent = e;
        break;
      }
    }
    assertNotNull("Bad TestData, no event with an attached location found",otherEvent);
    
    // this is the time and the location we want to use
    Location location = otherEvent.getLocation();
    TimeSlot timeSlot = otherEvent.getTimeSlot();
    
    // lets create a new contest that should move into this location
    Contest contest = Manager.getContestCollection().newEntity();
    Event testEvent = Manager.getEventCollection().findEntity(contest, timeSlot);
    assertNotNull(testEvent);
    assertEquals(contest, testEvent.getContest());
    
    // here is the statement that we want to test
    AllocateLocationForEvent action = 
            new AllocateLocationForEvent(testEvent.getEventId(), location.getLocationId());
    action.apply(0);
    
    // now, check whether it is all OK
    assertNull(otherEvent.getLocation()); // other event is de-located?
    assertEquals(testEvent.getLocation(), location); // test event happens at the new location
    
    
    
  }

}
