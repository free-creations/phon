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
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhon4Netbeans.ContestJobNode;
import de.free_creations.nbPhon4Netbeans.ContestNode;
import de.free_creations.nbPhon4Netbeans.IconManager;
import java.awt.Color;
import javax.swing.JLabel;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import org.openide.util.Exceptions;

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
        case (Allocation.PROP_COMMITED):
          allocIsCommitted = evt.getNewValue() instanceof Boolean ? (boolean) evt.getNewValue() : false;
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
  /**
   * plannerIsAutomat indicates that the allocation was generated automatically,
   * and should be shown in gray.
   */
  private boolean plannerIsAutomat = false;

  private boolean allocIsCommitted = false;

  private static class ColorPair {

    public final Color foreground;
    public final Color background;

    public ColorPair(Color foreground, Color background) {
      this.foreground = foreground;
      this.background = background;
    }
  }

  private final ColorPair defaultColors;
  private final ColorPair defaultAutomatColors;
  private final ColorPair disabledColors;
  private final ColorPair errorColors;
  private final ColorPair doubleErrorColors;

  private long allocationId = -1L;
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
    this.defaultAutomatColors = new ColorPair(new Color(48, 57, 187), Color.white);
    this.disabledColors = new ColorPair(Color.black, disabledColor);
    this.errorColors = new ColorPair(Color.red, new Color(235, 235, 235));
    this.doubleErrorColors = new ColorPair(Color.red, disabledColor);

    setOpaque(true);
    setSelected(false);

    if (!java.beans.Beans.isDesignTime()) {
      refresh();
    }
  }

  public long getAllocationId() {
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
      this.setComponentPopupMenu(makeComponentPopupMenu(contestId, availabilityId));
      refresh();
    }
  }

  void setValue(Object value) {
    if (value instanceof Long) {
      setAllocationId((Long) value);
    } else {
      setAllocationId(-1);
    }
  }

  /**
   * Displays the given allocation-record.
   *
   * The given allocation recode is used to retrieve the information to display.
   * This is: the event (scheduling) and the contest name.
   *
   * @param allocationId
   */
  public void setAllocationId(long allocationId) {
    long oldAllocationId = this.allocationId;
    this.allocationId = allocationId;
    if (oldAllocationId == allocationId) {
      return;
    }
    Integer oldEventId = eventId;
    Integer oldContestId = contestId;
    eventId = null;
    jobId = null;
    contestId = null;
    plannerIsAutomat = false; //the default
    allocIsCommitted = false;

    Allocation.removePropertyChangeListener(nodeListener, oldAllocationId);
    Event.removePropertyChangeListener(nodeListener, oldEventId);
    Contest.removePropertyChangeListener(nodeListener, oldContestId);

    try {
      Allocation alloc = Manager.getAllocationCollection().findEntity(allocationId);
      if (alloc != null) {
        alloc.addPropertyChangeListener(nodeListener);
        plannerIsAutomat = Allocation.PLANNER_AUTOMAT.equals(alloc.getPlanner());
        allocIsCommitted = alloc.isCommited();
        Job job = alloc.getJob();
        jobId = (job == null) ? null : job.getJobId();
        Event event = alloc.getEvent();
        if (event != null) {
          eventId = event.getEventId();
          Contest contest = event.getContest();
          if (contest != null) {
            contestId = contest.getContestId();
            contest.addPropertyChangeListener(nodeListener);
          }
          event.addPropertyChangeListener(nodeListener);
        }
      }
    } catch (DataBaseNotReadyException ex) {
    }

    this.setComponentPopupMenu(makeComponentPopupMenu(contestId, availabilityId));
    refresh();
  }

  /**
   * The popup menu permits the user to assign an (other) Task to a person.
   *
   * Implementation Note: we need the availability here to determine the
   * personId and to time-slot. This only works if "startListening" is called
   * before "setAllocationId". The concept of virtual Allocation would be a much
   * better solution. See notes in package nbPhonAPI on this subject.
   *
   * @param contestId
   * @return
   */
  private JPopupMenu makeComponentPopupMenu(Integer contestId, Integer availabiltyId) {
    JPopupMenu popupMenu = null;
    Integer personId = null;
    Integer timeSlotId = null;
    try {
      Availability a = Manager.getAvailabilityCollection().findEntity(availabiltyId);
      if (a != null) {
        Person p = a.getPerson();
        TimeSlot t = a.getTimeSlot();
        personId = (p == null) ? null : p.getPersonId();
        timeSlotId = (t == null) ? null : t.getTimeSlotId();
      }
    } catch (DataBaseNotReadyException ex) {
      Exceptions.printStackTrace(ex);
    }

    ProposeAssignmentAction action = null;
    if ((personId != null) && (timeSlotId != null)) {
      action = new ProposeAssignmentAction(personId, timeSlotId);
    }

    if (contestId != null) {
      ContestNode tempNode = new ContestNode(contestId, Manager.getContestCollection());
      popupMenu = tempNode.getContextMenu();
      tempNode.destroy();
      if (action != null) {
        popupMenu.insert(action, 0);
        popupMenu.insert(new JPopupMenu.Separator(), 1);
      }
    } else {
      if (action != null) {
        popupMenu = new JPopupMenu();
        popupMenu.add(action);
      }
    }
    return popupMenu;
  }

  private void refresh() {
    Icon icon = null;
    String text = null;
    if (allocationId != -1) {
      Image image = ContestJobNode.getIcon(contestId, jobId);

      if (allocIsCommitted) {
        if (image instanceof BufferedImage) {
          image = IconManager.iconManager().getLockedImage((BufferedImage) image);
        }
      }
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

    if (allocationId == -1) {
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
          if (plannerIsAutomat) {
            return defaultAutomatColors;
          } else {
            return defaultColors;
          }
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
