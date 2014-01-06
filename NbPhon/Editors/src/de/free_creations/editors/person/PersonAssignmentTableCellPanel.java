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
package de.free_creations.editors.person;

import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Availability;
import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.nbPhon4Netbeans.ContestJobNode;
import de.free_creations.nbPhon4Netbeans.ContestNode;
import java.awt.Color;
import javax.swing.JLabel;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

/**
 * Displays one single cell in the Allocation table.
 *
 * Each cell shows who is allocated to a given event for a given job.
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class PersonAssignmentTableCellPanel extends JLabel {

  /**
   * Indicates that the value of displayed by this panel has changed.
   */
  public static final String PROP_VALUE_CHANGED = "PROP_VALUE_CHANGED";

  private final PropertyChangeListener nodeListener = new PropertyChangeListener() {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      switch (evt.getPropertyName()) {
        case (Event.PROP_SCHEDULED):
        case (Availability.PROP_AVAILABLE):
        case (Contest.PROP_CONTESTTYPE):
        case (Contest.PROP_DESCRIPTION):
        case (Contest.PROP_NAME):
          refresh();
          fireValueChanged();
      }
    }
  };
  /**
   * @Todo move color management to a central place
   */

  private Color selectedBackgroundColor;
  private Color selectedForegroundColor;

  private static class ColorPair {

    public final Color foreground;
    public final Color background;

    public ColorPair(Color foreground, Color background) {
      this.foreground = foreground;
      this.background = background;
    }
  }

  private final ColorPair defaultColors;
  private final ColorPair disabledColors;
  private final ColorPair errorColors;
  private final ColorPair doubleErrorColors;

  private Integer allocationId = null;
  private Integer eventId;
  private Integer availabilityId;
  private Integer contestId;
  private String jobId;

  /**
   * Creates a AllocationTableCellPanel with default colors (only for test)
   */
  public PersonAssignmentTableCellPanel() {
    this(new Color(170, 170, 170), new Color(57, 105, 138), Color.WHITE);
  }

  /**
   * Creates a PersonAssignemtTableCellPanel and permits to define the colors
   * for the "selected" state (selected within the table).
   */
  public PersonAssignmentTableCellPanel(Color disabledColor, Color selectedBackgroundColor, Color selectedForegroundColor) {

    this.selectedBackgroundColor = selectedBackgroundColor;
    this.selectedForegroundColor = selectedForegroundColor;
    this.defaultColors = new ColorPair(Color.black, Color.white);
    this.disabledColors = new ColorPair(Color.black, disabledColor);
    this.errorColors = new ColorPair(Color.red, new Color(235, 235, 235));
    this.doubleErrorColors = new ColorPair(Color.red, disabledColor);

    setOpaque(true);
    setSelected(false);

    if (!java.beans.Beans.isDesignTime()) {
      refresh();
    }
  }

  public Integer getAllocationId() {
    return allocationId;
  }

  /**
   * Listens on the given availability-record and changes the background color
   * accordingly.
   *
   * @param availabilityId
   */
  public void startListening(Integer availabilityId) {
    Integer oldAvailabilityId = this.availabilityId;
    this.availabilityId = availabilityId;
    if (!Objects.equals(oldAvailabilityId, availabilityId)) {
      Availability.removePropertyChangeListener(nodeListener, oldAvailabilityId);
      if (availabilityId != null) {
        Availability.addPropertyChangeListener(nodeListener, availabilityId);
      }
    }
  }

  /**
   * Displays the given allocation-record. The information retrieved is:
   *
   * 1) the event (scheduling) 2) the contest name.
   *
   * @param allocationId
   */
  public void setAllocationId(Integer allocationId) {
    Integer oldAllocationId = this.allocationId;
    this.allocationId = allocationId;
    if (Objects.equals(oldAllocationId, allocationId)) {
      return;
    }
    Integer oldEventId = eventId;
    Integer oldContestId = contestId;
    eventId = null;
    jobId = null;
    contestId = null;
    JPopupMenu popupMenu = null;
    Event.removePropertyChangeListener(nodeListener, oldEventId);
    Contest.removePropertyChangeListener(nodeListener, oldContestId);

    if (allocationId != null) {
      try {
        Allocation alloc = Manager.getAllocationCollection().findEntity(allocationId);
        if (alloc != null) {
          Job job = alloc.getJob();
          jobId = (job == null) ? null : job.getJobId();
          Event event = alloc.getEvent();
          if (event != null) {
            eventId = event.getEventId();
            Contest contest = event.getContest();
            if (contest != null) {
              contestId = contest.getContestId();
              contest.addPropertyChangeListener(nodeListener);
              ContestNode tempNode = new ContestNode(contestId, Manager.getContestCollection());
              popupMenu = tempNode.getContextMenu();
              tempNode.destroy();
            }
            event.addPropertyChangeListener(nodeListener);
          }
        }
      } catch (DataBaseNotReadyException ex) {
      }
    }
    this.setComponentPopupMenu(popupMenu);
    refresh();
  }

  private void refresh() {
    Icon icon = null;
    String text = null;
    if (allocationId != null) {
      Image image = ContestJobNode.getIcon(contestId, jobId);
      icon = (image == null) ? null : new ImageIcon(image);
      text = ContestJobNode.getLongHtmlDescription(contestId, jobId);
    }
    setIcon(icon);
    setText(text);
    ColorPair colors = getColors();
    setBackground(colors.background);
    setForeground(colors.foreground);
  }

  private ColorPair getColors() {
    boolean isAvailable = false;

    try {
      Availability a = Manager.getAvailabilityCollection().findEntity(availabilityId);
      isAvailable = (a == null) ? false : a.isAvailable();
    } catch (DataBaseNotReadyException ex) {
    }

    if (allocationId == null) {
      if (isAvailable) {
        return defaultColors;
      } else {
        return disabledColors;
      }
    }

    try {
      Event event = Manager.getEventCollection().findEntity(eventId);
      if (event == null) {
        return disabledColors;
      }
      if (isAvailable) {
        if (event.isScheduled()) {
          return defaultColors;
        } else {
          return errorColors;
        }
      } else {
        if (event.isScheduled()) {
          return disabledColors;
        } else {
          return doubleErrorColors;
        }
      }
    } catch (DataBaseNotReadyException ex) {
      return errorColors;
    }
  }

  public final void setSelected(boolean selected) {
    if (selected) {
      setBackground(selectedBackgroundColor);
      setForeground(selectedForegroundColor);
    } else {
      ColorPair colors = getColors();
      setBackground(colors.background);
      setForeground(colors.foreground);
    }
  }

  /**
   * Inform the surrounding table that something has changed and the panel needs
   * to be repainted.
   */
  private void fireValueChanged() {
    firePropertyChange(PROP_VALUE_CHANGED, null, null);
  }
}
