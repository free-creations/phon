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
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;

/**
 * This rule applies when an event changes schedule.
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class SetEventScheduled implements CheckedAction {

  private final Integer eventId;
  private final boolean newValue;

  public SetEventScheduled(Integer eventId, boolean newValue) {
    this.eventId = eventId;
    assert (eventId != null);
    this.newValue = newValue;

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
    boolean oldvalue = event.isScheduled();

    if (oldvalue == newValue) {
      return;
    }
    event.setScheduled(newValue);
  }

}
