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
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.Team;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AllocationRating {

  public static final int unrealizable = Integer.MIN_VALUE / 1000;
  public static final int veryInconvenient = -124;
  public static final int inconvenient = -32;
  public static final int neutral = 0;
  public static final int good = 32;

  private final Integer personId;
  private final Integer eventId;
  private final String jobId;
  private int score = neutral;

  public AllocationRating(Integer personId, Integer eventId, String jobId) {
    this.personId = personId;
    this.eventId = eventId;
    this.jobId = jobId;
  }

  public int getScore() {
    try {
      return scoreCalc();
    } catch (DataBaseNotReadyException ex) {
      return neutral;
    }
  }

  Person person;
  Event event;
  Job job;

  private int scoreCalc() throws DataBaseNotReadyException {
    score = neutral;
    person = Manager.getPersonCollection().findEntity(personId);
    if (person == null) {
      return 0;
    }
    event = Manager.getEventCollection().findEntity(eventId);
    if (event == null) {
      return 0;
    }
    job = Manager.getJobCollection().findEntity(jobId);
    if (job == null) {
      return 0;
    }

    //---
    score += availabilityAssessmemnt();
    score += allocationAssessmemnt();
    score += jobAssessmemnt();
    score += contestTypeAssessmemnt();
    score += teamAssessmemnt();    
    score += eventAssessmemnt();
    return score;
  }

  /**
   * unrealizable if the person is not available for the event.
   *
   * @return
   */
  private int availabilityAssessmemnt() throws DataBaseNotReadyException {
    TimeSlot timeSlot = event.getTimeSlot();
    List<Availability> availabilityList = person.getAvailabilityList();
    Availability availability = null;
    for (Availability a : availabilityList) {
      if (timeSlot.equals(a.getTimeSlot())) {
        availability = a;
        break;
      }
    }
    if (availability == null) {
      return unrealizable;
    }
    if (availability.isAvailable()) {
      return neutral;
    } else {
      return unrealizable;
    }
  }

  /**
   * unrealizable if the person has an other appointment.
   *
   * @return
   */
  private int allocationAssessmemnt() throws DataBaseNotReadyException {
    TimeSlot timeSlot = event.getTimeSlot();
    List<Allocation> allocationList = person.getAllocationList();
    for (Allocation a : allocationList) {
      Event e = a.getEvent();
      if (Objects.equals(e.getTimeSlot(), timeSlot)) {
        if (!Objects.equals(e, event)) {
          return unrealizable;
        }
      }
    }

    return neutral;
  }

  /**
   * Good if the proposed job is what the person wanted, very inconvenient if it
   * is not;
   *
   * @return
   * @throws DataBaseNotReadyException
   */
  private int jobAssessmemnt() throws DataBaseNotReadyException {
    JobType wantedJobType = person.getJobType();
    if (wantedJobType == null) {
      return neutral;
    }
    if (Objects.equals(wantedJobType, job.getJobType())) {
      return good;
    } else {
      return veryInconvenient;
    }
  }

  /**
   * Good if the proposed contest is what the person wanted, inconvenient if it
   * is not;
   *
   * @return
   * @throws DataBaseNotReadyException
   */
  private int contestTypeAssessmemnt() throws DataBaseNotReadyException {
    ContestType wantedContestType = person.getContestType();
    if (wantedContestType == null) {
      return neutral;
    }
    Contest c = event.getContest();
    if (c == null) {
      return neutral;
    }
    ContestType proposedContestType = c.getContestType();
    if (Objects.equals(wantedContestType, proposedContestType)) {
      return good;
    } else {
      return inconvenient;
    }
  }

  private int teamAssessmemnt() throws DataBaseNotReadyException {
    Team wantedTeam = person.getTeam();
    if (wantedTeam == null) {
      return neutral;
    }
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
            tempScore += good;
          } else {
            tempScore += veryInconvenient;
          }
        }
      }
    }
    return tempScore;
  }

  private int eventAssessmemnt() throws DataBaseNotReadyException {
    if (event.isScheduled()) {
      return neutral;
    } else {
      return unrealizable;
    }
  }

}
