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
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Location;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This rule applies when the location is assigned to a contest.
 *
 * All events that were located in this location at the given time-slot must first
 * be de-located, before locating the new contest.
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AllocateLocationForEvent implements CheckedAction {

  private final Integer eventId;
  private final Integer newLocationId;

  public AllocateLocationForEvent(Integer eventId, Integer newLocationId) {
    this.eventId = eventId;
    assert (eventId != null);
    this.newLocationId = newLocationId;

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
    Location oldLocation = event.getLocation();
    Integer oldLocationId = (oldLocation == null)?null:oldLocation.getLocationId();
    if(Objects.equals(oldLocationId, newLocationId)){
      return;
    }


    if (newLocationId == null) {
      // the assignment is always possible
      event.setLocation(null);
      return; // out
    }
    // from here on: newLocationId != null

    Location newLocation = Manager.getLocationCollection().findEntity(newLocationId);
    if (newLocation == null) {
      throw new RuntimeException(String.format("Invalid Location Id %s.", newLocation));
    }

    // first de-locate (remove location) for any event that was planed in the given new location
    //at the given time.
    List<Event> eventListL = newLocation.getEventList();
    ArrayList<Event> eventListSnapShot = new ArrayList<>(eventListL);

    for (Event e : eventListSnapShot) {
      if (Objects.equals(e.getTimeSlot(), event.getTimeSlot())) {
        // should be a warning 
        e.setLocation(null);
      }
    }    
    
    event.setLocation(newLocation);
  }

}
