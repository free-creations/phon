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
import java.util.Objects;
import java.util.logging.Level;
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
 * @author Harald Postner<harald at free-creations.de>
 */
public class AllocatePersonForEvent implements CheckedAction {

  static final private Logger logger = Logger.getLogger(AllocatePersonForEvent.class.getName());

  private final Integer eventId;
  private final Integer newPersonId;
  private final String jobId;
  private final String  planner;

  public AllocatePersonForEvent(Integer eventId, Integer newPersonId, String jobId, String planner) {
    this.eventId = eventId;
    assert (eventId != null);
    this.newPersonId = newPersonId; // if null, any existing allocation shall be removed
    this.jobId = jobId;
    this.planner = planner;
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
    logger.log(Level.FINER, "allocate Person[{0}] to {1}, {2}", new Object[]{newPersonId, event, job});
    logger.log(Level.FINER, "...event: {0},{1}", new Object[]{event.getContest(),event.getTimeSlot()});

    // remove all persons-allocation previously assigned to this job (should  be 0 or 1);
    List<Allocation> aTemp = Manager.getAllocationCollection().findAll(event, job);
    // the requested allocation might already be realized.
    if (isRequestedAllocation(aTemp)) {
      logger.log(Level.FINER, "Person[{0}] is allready allocated to {1}, {2}. Nothing to do.", new Object[]{newPersonId, event, job});
      return; // OK there is nothing to do.
    }
    ArrayList<Allocation> aSnapShot = new ArrayList<>(aTemp);
    for (Allocation a : aSnapShot) {
      logger.log(Level.FINER, "removing other persons allocation {0}, {1}, {2}", new Object[]{a.getPerson(), a.getEvent(), a.getJob()});
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
      logger.log(Level.FINER, "removing this persons simultaneous allocation {0}, {1}, {2}", new Object[]{a.getPerson(), a.getEvent(), a.getJob()});
      Manager.getAllocationCollection().removeEntity(a);
    }

    // now proceed to the new allocation
    Manager.getAllocationCollection().newEntity(person, event, job, planner);

  }

  /**
   * Check if the given allocation represents the wanted situation. (note we
   * assume that the the given allocations are linked to the event and job in
   * question)
   *
   * @param aTemp
   * @return
   */
  private boolean isRequestedAllocation(List<Allocation> aTemp) {
    if (newPersonId == null) {
      if (aTemp.isEmpty()) {
        return true;
      }
    }
    if (aTemp.size() != 1) {
      return false; // aTemp.size >  1 is always wrong!
    }
    Allocation alloc = aTemp.get(0);
    Person person = alloc.getPerson();
    if (person == null) {
      return false;
    }

    return Objects.equals(person.getPersonId(), newPersonId);
  }

}
