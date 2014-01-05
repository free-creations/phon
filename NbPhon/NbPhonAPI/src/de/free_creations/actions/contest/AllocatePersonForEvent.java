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
package de.free_creations.actions.contest;

import de.free_creations.actions.CheckedAction;
import static de.free_creations.actions.CheckedAction.Severity.*;
import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.util.ArrayList;
import java.util.List;

/**
 * This rule applies when a person is assigned for a job in a contest.
 *
 * 1) All persons that where allocated to this job must first be freed from the
 * job.
 *
 * 2) If the given person was allocated to an other job at the given time the
 * person must be freed from this other job.
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AllocatePersonForEvent implements CheckedAction {

  private final Integer eventId;
  private final Integer newPersonId;
  private final String jobId;

  public AllocatePersonForEvent(Integer eventId, Integer newPersonId, String jobId) {
    this.eventId = eventId;
    assert (eventId != null);
    this.newPersonId = newPersonId; // can be null
    this.jobId = jobId;
    assert (jobId != null);
  }

  @Override
  public Severity level() {
    return ok;
  }

  @Override
  public String problemDescription() {
    return "";
  }

  @Override
  public String[] proposedSolution() {
    return new String[]{};
  }

  @Override
  public int workaroundCount() {
    return 0;
  }

  @Override
  public void apply(int ignored) throws DataBaseNotReadyException {

    Event event = Manager.getEventCollection().findEntity(eventId);
    if (event == null) {
      throw new RuntimeException(String.format("Invalid Event Id %s.", eventId));
    }
    Job job = Manager.getJobCollection().findEntity(jobId);
    if (job == null) {
      throw new RuntimeException(String.format("Invalid Job Id %s.", jobId));
    }
    // remove all persons-allocation previously assigned to this job (should  be 0 or 1);
    List<Allocation> aTemp = Manager.getAllocationCollection().findAll(event, job);
    ArrayList<Allocation> aSnapShot = new ArrayList<>(aTemp);
    for (Allocation a : aSnapShot) {
      Manager.getAllocationCollection().removeEntity(a);
    }

    if (newPersonId == null) {
      return;
    }
    // --- from here: newPersonId != null
    Person person = Manager.getPersonCollection().findEntity(newPersonId);
    if (person == null) {
      throw new RuntimeException(String.format("Invalid Person Id %s.", newPersonId));
    }

    // remove all allocation for this person at the given time
    TimeSlot timeSlot = event.getTimeSlot();
    assert (timeSlot != null);
    aTemp = Manager.getAllocationCollection().findAll(person, timeSlot);
    aSnapShot = new ArrayList<>(aTemp);
    for (Allocation a : aSnapShot) {
      Manager.getAllocationCollection().removeEntity(a);
    }
    
    // now proceed to the new allocation
    Manager.getAllocationCollection().newEntity(person, event, job);
    
  }

}
