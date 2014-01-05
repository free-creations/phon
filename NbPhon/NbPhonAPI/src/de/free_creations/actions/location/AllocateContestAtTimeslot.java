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
package de.free_creations.actions.location;

import de.free_creations.actions.CheckedAction;
import static de.free_creations.actions.CheckedAction.Severity.*;
import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Location;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This rule applies when a location is used at a given time for a specific
 * contest.
 *
 * All events that were allocated to this room at the given time-slot first
 * de-allocated, before allocating the new contest.
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AllocateContestAtTimeslot implements CheckedAction {

  private final Integer locationId;
  private final Integer contestId;
  private final Integer timeSlotId;

  public AllocateContestAtTimeslot(Integer locationId, Integer contestId, Integer timeSlotId) {
    this.locationId = locationId;
    assert (locationId != null);
    this.contestId = contestId;
    this.timeSlotId = timeSlotId;
    assert (timeSlotId != null);
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

    Location location = Manager.getLocationCollection().findEntity(locationId);
    if (location == null) {
      throw new RuntimeException(String.format("Invalid Location Id %s.", locationId));
    }
    
    // first de-locate all events that were planed at the given time in this loacation
    List<Event> eventListL = location.getEventList();
    ArrayList<Event> eventListSnapShot = new ArrayList<>(eventListL);

    for (Event e : eventListSnapShot) {
      if (Objects.equals(e.getTimeSlot().getTimeSlotId(), timeSlotId)) {
        // could be worth a warning 
        e.setLocation(null);
      }
    }

    if (contestId != null) {
      // 
      Contest contest = Manager.getContestCollection().findEntity(contestId);
      if (contest == null) {
        throw new RuntimeException(String.format("Invalid Contest Id %s.", contestId));
      }
      List<Event> eventListC = contest.getEventList();
      int count = 0;
      for (Event e : eventListC) {
        if (Objects.equals(e.getTimeSlot().getTimeSlotId(), timeSlotId)) {
          // if (e.getLocation != null) could be worth a warning
          e.setLocation(location);
          count++;
        }
      }
      assert (count == 1);
    }
  }

}
