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
package de.free_creations.actions.person;

import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Person;
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
public class AllocatePersonToJobTest {

  public AllocatePersonToJobTest() {
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
   * Test of level method, of class AllocatePersonToJob.
   */
  @Test
  public void testLevel() {
  }

  /**
   * Test of problemDescription method, of class AllocatePersonToJob.
   */
  @Test
  public void testProblemDescription() {
  }

  /**
   * Test of proposedSolution method, of class AllocatePersonToJob.
   */
  @Test
  public void testProposedSolution() {
  }

  /**
   * Test of workaroundCount method, of class AllocatePersonToJob.
   */
  @Test
  public void testWorkaroundCount() {
  }

  /**
   * Test of apply method, of class AllocatePersonToJob.
   *
   * Before a person can be allocated to a new job, she/he must be freed from
   * any job that would take place at the same time.
   */
  @Test
  public void testApply() throws Exception {

    List<Allocation> all = Manager.getAllocationCollection().getAll();
    assertFalse("Bad TestData", all.isEmpty());
    Allocation a = all.get(0);

    Person person = a.getPerson();
    Event oldEvent = a.getEvent();
    Job oldJob = a.getJob();
    Job newJob = oldJob;
    TimeSlot timeSlot = oldEvent.getTimeSlot();

    // lets create a new contest on which the person should be allocated
    Contest contest = Manager.getContestCollection().newEntity();
    // here is the statement that we want to test
    AllocatePersonToJob action
            = new AllocatePersonToJob(person.getPersonId(),
                    contest.getContestId(),
                    newJob.getJobId(),
                    timeSlot.getTimeSlotId(), "USER");
    action.apply(0);

    // now, check whether it is all OK
    List<Allocation> personAtTime = Manager.getAllocationCollection().findAll(person, timeSlot);
    assertTrue(personAtTime.size() == 1);
    assertEquals(personAtTime.get(0).getJob(), newJob);
    assertEquals(personAtTime.get(0).getEvent().getContest(), contest);

    List<Allocation> oldEventJob = Manager.getAllocationCollection().findAll(oldEvent, oldJob);
    assertTrue(oldEventJob.isEmpty());

  }

}
