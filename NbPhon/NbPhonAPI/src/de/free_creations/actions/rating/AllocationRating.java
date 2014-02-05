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
package de.free_creations.actions.rating;

import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Availability;
import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.ContestType;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.JobType;
import de.free_creations.dbEntities.Location;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.Team;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AllocationRating {

  /**
   * the threshold which we consider an impossible allocation.
   */
  public static final int veryGood = 124;
  public static final int good = 32;
  public static final int veryInconvenient = -124;
  public static final int veryVeryInconvenient = -256;
  public static final int inconvenient = -32;
  public static final int neutral = 0;
  public static final int badThreshold = good;

  private final Person person;
  private final Event event;
  private final Job job;
  private final TimeSlot timeSlot;
  private int score = neutral;

  /**
   * Constructs a new rating calculator.
   *
   * Note the functions getScore() isClashing() must be called within the same
   * thread as this constructor.
   *
   * @param personId
   * @param eventId
   * @param jobId
   */
  public AllocationRating(Integer personId, Integer eventId, String jobId) {

    Person _person = null;
    Event _event = null;
    Job _job = null;
    TimeSlot _timeSlot = null;
    try {
      _person = Manager.getPersonCollection().findEntity(personId);
      _event = Manager.getEventCollection().findEntity(eventId);
      _job = Manager.getJobCollection().findEntity(jobId);
      _timeSlot = _event.getTimeSlot();
    } catch (DataBaseNotReadyException ex) {
    } finally {
      person = _person;
      event = _event;
      job = _job;
      timeSlot = _timeSlot;
    }
    if (person == null) {
      throw new RuntimeException("Invalid Person[" + personId + "]");
    }
    if (event == null) {
      throw new RuntimeException("Invalid Event[" + eventId + "]");
    }
    if (job == null) {
      throw new RuntimeException("Invalid Job[" + jobId + "]");
    }
    if (timeSlot == null) {
      throw new RuntimeException("Event[" + eventId + "] has timeslot null.");
    }
  }

  /**
   * Constructs a new rating calculator.
   *
   * Note the functions getScore() isClashing() must be called within the same
   * thread as this constructor.
   *
   * @param person
   * @param event
   * @param job
   */
  public AllocationRating(Person person, Event event, Job job) {
    this.person = person;
    this.event = event;
    this.job = job;
    this.timeSlot = event.getTimeSlot();
    if (person == null) {
      throw new RuntimeException("Null Person.");
    }
    if (event == null) {
      throw new RuntimeException("Null Event.");
    }
    if (job == null) {
      throw new RuntimeException("Null Job.");
    }
    if (timeSlot == null) {
      throw new RuntimeException("Null Timeslot.");
    }
  }

  public AllocationRating(Allocation allocation) {
    this.person = allocation.getPerson();
    this.event = allocation.getEvent();
    this.job = allocation.getJob();
    this.timeSlot = event.getTimeSlot();
    if (person == null) {
      throw new RuntimeException("Null Person.");
    }
    if (event == null) {
      throw new RuntimeException("Null Event.");
    }
    if (job == null) {
      throw new RuntimeException("Null Job.");
    }
    if (timeSlot == null) {
      throw new RuntimeException("Null Timeslot.");
    }
  }

  public int getScore() {
    return scoreCalc();
  }

  private int scoreCalc() {
    score = neutral;
    //---
    score += jobAssessmemnt();
    score += contestTypeAssessmemnt();
    score += teamAssessmemnt();
    score += locationAssessmemnt();
    return score;
  }

  /**
   * Good if the proposed job is what the person wanted, very inconvenient if it
   * is not;
   *
   * @return
   * @throws DataBaseNotReadyException
   */
  private int jobAssessmemnt() {
    JobType wantedJobType = person.getJobType();
    if (wantedJobType == null) {
      return neutral;
    }
    if (Objects.equals(job.getPriority(), 1)) {
      // prio one should mostly go to those who have oped for
      if (Objects.equals(wantedJobType, job.getJobType())) {
        return veryGood;
      } else {
        // prio one might go to adults
        if (Objects.equals(person.getAgegroup(), "ERWACHSEN")) {
          return inconvenient;
        } else {
          // but never to children
          return veryVeryInconvenient;
        }
      }
    } else {
      if (Objects.equals(wantedJobType, job.getJobType())) {
        return good;
      } else {
        return inconvenient;
      }
    }
  }

  /**
   * Good if the proposed contest is what the person wanted, inconvenient if it
   * is not;
   *
   * @return
   * @throws DataBaseNotReadyException
   */
  private int contestTypeAssessmemnt() {
    Contest proposedContest = event.getContest();
    if (proposedContest == null) {
      // this should never happen.
      return neutral;
    }
    // if this person is contest leader, we want him/her to be allocated those contests
    List<Contest> contestLeaderList = person.getContestList();
    if (!contestLeaderList.isEmpty()) {
      if (contestLeaderList.contains(proposedContest)) {
        return veryGood;
      }
    }
    ContestType wantedContestType = person.getContestType();
    if (wantedContestType == null) {
      return neutral;
    }

    ContestType proposedContestType = proposedContest.getContestType();
    if (Objects.equals(wantedContestType, proposedContestType)) {
      return good;
    } else {
      return inconvenient;
    }
  }

  private int teamAssessmemnt() {
    Team wantedTeam = person.getTeam();
    if (wantedTeam == null) {
      // no team preference that's OK
      return neutral;
    }

    // check who else is allocated with me. 
    List<Allocation> aa = event.getAllocationList();
    if (aa.isEmpty()) {
      // I am the first person of my team on this event. That's fine.
      return good;
    }
    int tempScore = neutral;
    for (Allocation a : aa) {
      Person other = a.getPerson();
      if (!Objects.equals(other, person)) {
        Team otherPersonsTeam = other.getTeam();
        if (otherPersonsTeam != null) {
          if (otherPersonsTeam.equals(wantedTeam)) {
            // youpy an other person of my team
            tempScore += good;
          } else {
            // grrr. a person from an other team
            tempScore += veryInconvenient;
          }
        }
      }
    }
    return tempScore;
  }

  private int locationAssessmemnt() {
    Location proposedLocation = event.getLocation();
    if (proposedLocation == null) {
      return neutral;
    }
    Integer dayIdx = timeSlot.getDayIdx();
    ArrayList<Allocation> allocSameDay = new ArrayList<>();
    List<Allocation> aa = person.getAllocationList();
    for (Allocation a : aa) {
      if (Objects.equals(dayIdx, a.getEvent().getTimeSlot().getDayIdx())) {
        allocSameDay.add(a);
      }
    }
    int tempScore = neutral;
    for (Allocation a : allocSameDay) {
      if (!Objects.equals(a.getEvent().getLocation(), proposedLocation)) {
        tempScore += inconvenient;
      }
    }
    return tempScore;

  }

  /**
   * Determine whether the proposed task can be allocated to the person.
   *
   * @return true if the proposed task does not clash with an other allocation
   * and the person is available at given time.
   */
  public boolean isRealisable() {
    return (!isClashing()) && isAvailable();
  }

  /**
   * Determine whether the person is available at the given time.
   *
   * @return true if the person has marked the given time-slot as available.
   */
  public boolean isAvailable() {
    List<Availability> availabilityList = person.getAvailabilityList();
    for (Availability a : availabilityList) {
      if (timeSlot.equals(a.getTimeSlot())) {
        return a.isAvailable();
      }
    }
    // should never happen. Database in error.
    return false;
  }

  /**
   * Determines whether the proposed allocation is clashing with an other
   * allocation.
   *
   * Note: if the person is already allocated to the given event, this function
   * will always return true, in other words an existing allocation is always
   * clashing with itself.
   *
   * @return true if the person has an other allocation at the proposed time.
   * Note: true is also returned if the required entities can not be accessed.
   */
  public boolean isClashing() {

    List<Allocation> aa = person.getAllocationList();
    for (Allocation a : aa) {
      Event e = a.getEvent();
      if (e == null) {
        // should never happen. Database in error.
        return true;
      }
      TimeSlot t = e.getTimeSlot();
      if (timeSlot.equals(t)) {
        // well this IS a clash.
        return true;
      }
    }
    // OK no clash.
    return false;
  }

  /**
   * Check if the allocation would be (or is) redundant.
   *
   * @return true if the event does not take place.
   */
  public boolean isRedundant() {
    return !event.isScheduled();
  }

}
