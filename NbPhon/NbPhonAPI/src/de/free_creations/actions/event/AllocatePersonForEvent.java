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
package de.free_creations.actions.event;

import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.util.List;
import java.util.logging.Logger;

/**
 * This rule applies when a person is assigned for a job in a contest.
 *
 * 1) All persons that where allocated to this job must first be freed from the
 * job.
 *
 * 2) If the given person was allocated to an other job at the given time the
 * person must be freed from this other job.
 *
 * Note: on purpose it is not checked whether the person is available, thus
 * giving the user the possibility to overrule the availability.
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AllocatePersonForEvent {

  static final private Logger logger = Logger.getLogger(AllocatePersonForEvent.class.getName());

  static public class AllocationException extends Exception {

    AllocationException(String message) {
      super(message);
    }
  }

  private final Integer eventId;
  private final Integer newPersonId;
  private final String jobId;
  private final String planner;
  private final boolean removeClashing;

  public AllocatePersonForEvent(
          boolean removeClashing,
          Integer eventId,
          Integer newPersonId,
          String jobId,
          String planner) {
    this.removeClashing = removeClashing;
    this.eventId = eventId;
    assert (eventId != null);
    this.newPersonId = newPersonId; // if null, any existing allocation shall be removed
    this.jobId = jobId;
    this.planner = planner;
    assert (jobId != null);
  }

  public void apply() throws DataBaseNotReadyException, AllocationException {

    Event event = Manager.getEventCollection().findEntity(eventId);
    if (event == null) {
      throw new RuntimeException(String.format("Invalid Event Id %s.", eventId));
    }
    Job job = Manager.getJobCollection().findEntity(jobId);
    if (job == null) {
      throw new RuntimeException(String.format("Invalid Job Id %s.", jobId));
    }

    if (removeClashing) {
      //free all other persons that where allocated to the given job.
      removeAllocations(event, job);
    }

    if (newPersonId == null) {
      return;
    }
    // --- from here: newPersonId != null
    Person person = Manager.getPersonCollection().findEntity(newPersonId);
    if (person == null) {
      throw new RuntimeException(String.format("Invalid Person Id %s.", newPersonId));
    }

    if (removeClashing) {
      // remove all allocation for this person at the given time
      removeAllocations(person, event.getTimeSlot());
    }

    canAllocate(person, event);
    canAllocate(event, job);

    // now proceed to the new allocation
    Manager.getAllocationCollection().newEntity(person, event, job, planner);

  }

  private void removeAllocations(Person p, TimeSlot t) throws DataBaseNotReadyException {
    List<Allocation> clashingAllocs = Manager.getAllocationCollection().findAll(p, t);
    Manager.getAllocationCollection().removeAll(clashingAllocs);
  }

  private void removeAllocations(Event e, Job j) throws DataBaseNotReadyException {
    List<Allocation> clashingAllocs = Manager.getAllocationCollection().findAll(e, j);
    Manager.getAllocationCollection().removeAll(clashingAllocs);
  }

  /**
   * Double check whether the person can really be allocated to the given event.
   * In other words we verify whether the person does not have an other
   * allocation at the same time.
   *
   * @throws
   * de.free_creations.actions.event.AllocatePersonForEvent.AllocationException
   * if the person has not free time
   */
  private void canAllocate(Person person, Event event) throws AllocationException {
    TimeSlot timeSlot = event.getTimeSlot();
    if (timeSlot == null) {
      throw new AllocationException("Event with null timeSlot " + event);
    }

    List<Allocation> aa = person.getAllocationList();
    for (Allocation a : aa) {
      Event e = a.getEvent();
      if (e == null) {
        throw new AllocationException("Allocation with null event " + a);
      }
      if (timeSlot.equals(e.getTimeSlot())) {
        throw new AllocationException("Attempt to allocate " + person + " to " + event
                + " clashes with " + e);
      }
    }
  }

  private void canAllocate(Event event, Job job) throws AllocationException {
    List<Allocation> aa = event.getAllocationList();
    for (Allocation a : aa) {
      Job j = a.getJob();
      if (j == null) {
        throw new AllocationException("Allocation with null job " + a);
      }
      if (j.equals(job)) {
        throw new AllocationException("Attempt to allocate an other person to "
                + event + " for job " + job);
      }
    }

  }

}
