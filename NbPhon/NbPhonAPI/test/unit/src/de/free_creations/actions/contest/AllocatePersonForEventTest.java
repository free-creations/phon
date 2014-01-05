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

import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhonAPI.Manager;
import java.util.List;
import java.util.Objects;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AllocatePersonForEventTest {

  public AllocatePersonForEventTest() {
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
   * Test of level method, of class AllocatePersonForEvent.
   */
  @Test
  public void testLevel() {
  }

  /**
   * Test of problemDescription method, of class AllocatePersonForEvent.
   */
  @Test
  public void testProblemDescription() {
  }

  /**
   * Test of proposedSolution method, of class AllocatePersonForEvent.
   */
  @Test
  public void testProposedSolution() {
  }

  /**
   * Test of workaroundCount method, of class AllocatePersonForEvent.
   */
  @Test
  public void testWorkaroundCount() {
  }

  /**
   * Test of apply method, of class AllocatePersonForEvent.
   *
   * Before a oldPerson can be allocated to a new job, she/he must be freed from
   * any job that would take place at the same time.
   *
   * @throws java.lang.Exception
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

    // lets create a new contest on which the oldPerson should be allocated
    Contest contest = Manager.getContestCollection().newEntity();
    Event newEvent = Manager.getEventCollection().findEntity(contest, timeSlot);
    assertNotNull(newEvent);
    assertEquals(contest, newEvent.getContest());

    // here is the statement that we want to test
    AllocatePersonForEvent action
            = new AllocatePersonForEvent(newEvent.getEventId(), person.getPersonId(), newJob.getJobId());
    action.apply(0);

    // now, check whether it is all OK
    List<Allocation> personAtTime = Manager.getAllocationCollection().findAll(person, timeSlot);
    assertTrue(personAtTime.size() == 1);
    assertEquals(personAtTime.get(0).getEvent(), newEvent);

    List<Allocation> testEventJob = Manager.getAllocationCollection().findAll(newEvent, newJob);
    assertTrue(testEventJob.size() == 1);
    assertEquals(testEventJob.get(0).getPerson(), person);

    List<Allocation> otherEventJob = Manager.getAllocationCollection().findAll(oldEvent, oldJob);
    assertTrue(otherEventJob.isEmpty());

  }

  /**
   * Test of apply method, of class AllocatePersonForEvent.
   *
   * same as before but now the oldPerson changes the job.
   *
   * Before a oldPerson can be allocated to a new job, she/he must be freed from
   * any job that would take place at the same time.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testApply_2() throws Exception {
    List<Allocation> all = Manager.getAllocationCollection().getAll();
    assertFalse("Bad TestData", all.isEmpty());
    Allocation a = all.get(0);

    Person person = a.getPerson();
    Event oldEvent = a.getEvent();
    Job oldJob = a.getJob();
    Job newJob = null;
    List<Job> jj = Manager.getJobCollection().getAll();
    for (Job j : jj) {
      if (!oldJob.equals(j)) {
        newJob = j; // a complicated way to find  a new job
        break;
      }
    }
    assertNotNull(newJob);
    assertFalse(Objects.equals(oldJob, newJob));
    TimeSlot timeSlot = oldEvent.getTimeSlot();

    // lets create a new contest on which the oldPerson should be allocated
    Contest contest = Manager.getContestCollection().newEntity();
    Event newEvent = Manager.getEventCollection().findEntity(contest, timeSlot);
    assertNotNull(newEvent);
    assertEquals(contest, newEvent.getContest());

    // here is the statement that we want to test
    AllocatePersonForEvent action
            = new AllocatePersonForEvent(newEvent.getEventId(), person.getPersonId(), newJob.getJobId());
    action.apply(0);

    // now, check whether it is all OK
    List<Allocation> personAtTime = Manager.getAllocationCollection().findAll(person, timeSlot);
    assertTrue(personAtTime.size() == 1);
    assertEquals(personAtTime.get(0).getEvent(), newEvent);

    List<Allocation> testEventJob = Manager.getAllocationCollection().findAll(newEvent, newJob);
    assertTrue(testEventJob.size() == 1);
    assertEquals(testEventJob.get(0).getPerson(), person);

    List<Allocation> oldEventJob = Manager.getAllocationCollection().findAll(oldEvent, oldJob);
    assertTrue(oldEventJob.isEmpty());

  }

  /**
   * Test of apply method, of class AllocatePersonForEvent.
   *
   *
   * Before a oldPerson can be allocated to a given job, any other oldPerson who
   * had that job must be freed from this job.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testApply_3() throws Exception {
    List<Allocation> all = Manager.getAllocationCollection().getAll();
    assertFalse("Bad TestData", all.isEmpty());
    Allocation a = all.get(0);

    Person oldPerson = a.getPerson();
    Event event = a.getEvent();
    Job job = a.getJob();
    Person newPerson = null;
    List<Person> pp = Manager.getPersonCollection().getAll();
    for (Person p : pp) {
      if (!oldPerson.equals(p)) {
        newPerson = p;
        break;
      }
    }
    assertNotNull(newPerson);
    assertFalse(Objects.equals(oldPerson, newPerson));
    TimeSlot timeSlot = event.getTimeSlot();

    // here is the statement that we want to test
    AllocatePersonForEvent action
            = new AllocatePersonForEvent(event.getEventId(), newPerson.getPersonId(), job.getJobId());
    action.apply(0);

    // now, check whether it is all OK
    List<Allocation> newPersonAtTime = Manager.getAllocationCollection().findAll(newPerson, timeSlot);
    assertTrue(newPersonAtTime.size() == 1);
    assertEquals(newPersonAtTime.get(0).getEvent(), event);

    List<Allocation> oldPersonAtTime = Manager.getAllocationCollection().findAll(oldPerson, timeSlot);
    assertTrue(oldPersonAtTime.isEmpty());

    List<Allocation> testEventJob = Manager.getAllocationCollection().findAll(event, job);
    assertTrue(testEventJob.size() == 1);
    assertEquals(testEventJob.get(0).getPerson(), newPerson);

  }
}
