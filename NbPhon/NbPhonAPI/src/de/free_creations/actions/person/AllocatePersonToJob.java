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
package de.free_creations.actions.person;

import de.free_creations.actions.CheckedAction;
import static de.free_creations.actions.CheckedAction.Severity.*;
import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

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
public class AllocatePersonToJob implements CheckedAction {

  private final Integer personId;
  private final Integer contestId;
  private final String jobId;
  private final Integer timeSlotId;
  private final String planner;

  /**
   *
   * @param personId the person (never null)
   * @param contestId if null, the person will be freed from all jobs at
   * time-slot time.
   * @param jobId the job (irrelevant if contestId is null)
   * @param timeSlotId the time (never null)
   * @param planner tells who introduced the allocation. Must be "USER" or
   * "AUTOMAT".
   */
  public AllocatePersonToJob(Integer personId, Integer contestId, String jobId, Integer timeSlotId, String planner) {
    this.personId = personId;
    assert (personId != null);
    this.contestId = contestId;
    this.jobId = jobId;
    this.timeSlotId = timeSlotId;
    assert (timeSlotId != null);
    this.planner = planner;
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

    Person person = Manager.getPersonCollection().findEntity(personId);
    if (person == null) {
      throw new RuntimeException(String.format("Invalid Person Id %s.", personId));
    }
    TimeSlot timeSlot = Manager.getTimeSlotCollection().findEntity(timeSlotId);
    if (timeSlot == null) {
      throw new RuntimeException(String.format("Invalid TimeSlot Id %s.", timeSlotId));
    }

    ArrayList<Allocation> clashingAllocs = new ArrayList<>();
    //Collect all persons-allocation previously assigned to this person for the given time (should  be 0 or 1);
    // These will later be deleted.
    clashingAllocs.addAll(Manager.getAllocationCollection().findAll(person, timeSlot));

    // the requested allocation might already be realized (it is then in the clashing allocs).
    if (isRequestedAllocation(clashingAllocs)) {
      return; // OK there is nothing to do.
    }

    // remove all persons-allocation previously assigned to this person for the given time (should  be 0 or 1);
//    ArrayList<Allocation> aSnapShot = new ArrayList<>(aTemp);
//    for (Allocation a : aSnapShot) {
//      Manager.getAllocationCollection().removeEntity(a);
//    }
    if (contestId == null) {
      removeAllocations(clashingAllocs);
      return;
    }

    // --- from here: contestId != null
    Contest contest = Manager.getContestCollection().findEntity(contestId);
    if (contest == null) {
      throw new RuntimeException(String.format("Invalid Contest Id %s.", contestId));
    }
    Job job = Manager.getJobCollection().findEntity(jobId);
    if (job == null) {
      throw new RuntimeException(String.format("Invalid Job Id %s.", jobId));
    }

    Event event = Manager.getEventCollection().findEntity(contest, timeSlot);
    if (event == null) {
      throw new RuntimeException(String.format("No event for %s, %s.", contest, timeSlot));
    }

    //Collect all other allocations for the given job at the given time.
    // These will later be deleted.
    clashingAllocs.addAll(Manager.getAllocationCollection().findAll(event, job));

    // try to remove all clashing allocations.
    boolean removalOK = removeAllocations(clashingAllocs);
    if (!removalOK) {
      return;
    }

    // sight, finally we can proceed to the new allocation
    Manager.getAllocationCollection().newEntity(person, event, job, planner);
  }

  /**
   * Removes all allocations in the given list.
   *
   * If the list contains one or more committed allocations, the user is asked
   * whether he really wants to remove the allocation.
   *
   * @param clashingAllocs
   * @return true if the action was performed. Returns false if the user has
   * objected to the removal.
   * @throws DataBaseNotReadyException
   */
  private boolean removeAllocations(List<Allocation> clashingAllocs) throws DataBaseNotReadyException {

    // check if there is not any commited allocation among the clashing allocations
    for (Allocation a : clashingAllocs) {
      if (a.isCommited()) {
        boolean removeAnyway = warnAndAskUser(a);
        if (!removeAnyway) {
          return false;
        }
      }
    }
    Manager.getAllocationCollection().removeAll(clashingAllocs);
    return true;
  }

  /**
   * Check if the given allocation represents the wanted situation. (note we
   * assume that the the given allocations are linked to the person and timeSlot
   * in question)
   *
   * @param aTemp
   * @return
   */
  private boolean isRequestedAllocation(List<Allocation> aTemp) {
    if (contestId == null) {
      if (aTemp.isEmpty()) {
        return true;
      }
    }
    if (aTemp.size() != 1) {
      return false; // aTemp.size >  1 is always wrong!
    }
    Allocation alloc = aTemp.get(0);
    Event event = alloc.getEvent();
    if (event == null) {
      return false;
    }
    Contest contest = event.getContest();
    if (contest == null) {
      return false;
    }
    if (!Objects.equals(contest.getContestId(), contestId)) {
      return false;
    }
    Job job = alloc.getJob();
    if (job == null) {
      return false;
    }
    if (!Objects.equals(job.getJobId(), jobId)) {
      return false;
    }

    //
    return true;
  }

  /**
   * Warn the the user about the possible removal of a committed allocation.
   *
   * @param a the allocation that might be removed
   * @return true if the users allows the removal.
   */
  private boolean warnAndAskUser(Allocation a) {
    Person person = a.getPerson();
    String givenname = person == null ? "N" : person.getGivenname();
    String surname = person == null ? "N" : person.getSurname();
    
    String message = String.format("Job currently allocated and COMMITTED to %s, %s. "
            + "Do really want to want to change?",
            givenname,
            surname);

    NotifyDescriptor d
            = new NotifyDescriptor.Confirmation(
                    message,
                    "Allocate Person to Job",
                    NotifyDescriptor.OK_CANCEL_OPTION);
    return (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.OK_OPTION);
  }
}
