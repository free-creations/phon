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
package de.free_creations.editors.contest;

import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Person;
import java.awt.Color;
import javax.swing.JLabel;
import de.free_creations.editors.contest.AllocationTable.CellKey;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.EventCollection;
import de.free_creations.nbPhonAPI.Manager;
import java.util.List;
import java.util.Objects;
import org.openide.util.Exceptions;

/**
 * Displays one single cell in the Allocation table.
 *
 * Each cell shows who is allocated to a given event for a given job.
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AllocationTableCellPanel extends JLabel {

  /**
   * Indicates that the value of displayed by this panel has changed.
   */
  public static final String PROP_VALUE_CHANGED = "PROP_VALUE_CHANGED";
  /**
   * @Todo move color management to a central place
   */
  private Color disabledColor;
  private Color selectedBackgroundColor;
  private Color selectedForegroundColor;
  private CellKey cellKey = null;
  private Integer personId = null;

  /**
   * Creates a AllocationTableCellPanel with default colors (only for test)
   */
  public AllocationTableCellPanel() {
    this(new Color(170, 170, 170), new Color(57, 105, 138), Color.WHITE);
  }

  /**
   * Creates a TimeTableCellPanel and permits to define the colors for the
   * "selected" state (selected within the table).
   */
  public AllocationTableCellPanel(Color disabledColor, Color selectedBackgroundColor, Color selectedForegroundColor) {
    this.disabledColor = disabledColor;
    this.selectedBackgroundColor = selectedBackgroundColor;
    this.selectedForegroundColor = selectedForegroundColor;
    setOpaque(true);
    setSelected(false);
    refresh();

    if (!java.beans.Beans.isDesignTime()) {

    }
  }

  public void setKey(CellKey cellKey) {
    CellKey oldKey = this.cellKey;
    if (Objects.equals(oldKey, cellKey)) {
      return;
    }
    this.cellKey = cellKey;
    Person person = findPerson(cellKey);
    if(person != null){
      personId = person.getPersonId();
    }else{
      personId = null;
    }

    refresh();
  }

  private Person findPerson(CellKey cellKey) {
    if (cellKey == null) {
      return null;
    }
    Integer eventId = cellKey.eventId;
    if (eventId == null) {
      return null;
    }
    String jobId = cellKey.jobId;
    if (jobId == null) {
      return null;
    }
    EventCollection ee = Manager.getEventCollection();
    try {
      Event event = ee.findEntity(eventId);
      if (event != null) {
        List<Allocation> aa = event.getAllocationList();
        for (Allocation a : aa) {
          Job job = a.getJob();
          if (job != null) {
            if (Objects.equals(job.getJobId(), jobId)) {
              return a.getPerson();

            }
          }
        }
      }
    } catch (DataBaseNotReadyException ex) {
    }
    return null;
  }

  public CellKey getKey() {
    return cellKey;
  }

  private void refresh() {
    Person p = null;
    try {
      p= Manager.getPersonCollection().findEntity(personId);
    } catch (DataBaseNotReadyException ex) {
    }
    if(p!=null){
      setText(p.getSurname());
    }else{
      setText("none");
    }

  }

  public final void setSelected(boolean selected) {
    if (selected) {
      setBackground(selectedBackgroundColor);
      setForeground(selectedForegroundColor);
    } else {
      setBackground(Color.WHITE);
      setForeground(Color.BLACK);
    }
  }
}
