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
import de.free_creations.dbEntities.Availability;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Person;
import java.awt.Color;
import javax.swing.JLabel;
import de.free_creations.editors.contest.AllocationTable.CellKey;
import de.free_creations.nbPhon4Netbeans.PersonNode;
import de.free_creations.nbPhon4Netbeans.IconManager;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.BeanInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
public class AllocationTableCellPanel extends JLabel {

  /**
   * Indicates that the value of displayed by this panel has changed.
   */
  public static final String PROP_VALUE_CHANGED = "PROP_VALUE_CHANGED";
  private static long allocationId = -1;

  private final PropertyChangeListener nodeListener = new PropertyChangeListener() {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      switch (evt.getPropertyName()) {
        case (Allocation.PROP_COMMITED):
          allocIsCommitted = evt.getNewValue() instanceof Boolean ? (boolean) evt.getNewValue() : false;

        case (Event.PROP_SCHEDULED):
        case (Event.PROP_ALLOCATIONADDED):
        case (Event.PROP_ALLOCATIONREMOVED):
        case (Person.PROP_GENDER):
        case (Person.PROP_AGEGROUP):
        case (Person.PROP_AVAILABILITY):
        case (Person.PROP_GIVENNAME):
        case (Person.PROP_SURNAME):
        case (Person.PROP_CONTESTTYPE):
        case (Person.PROP_JOBTYPE):
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
  private CellKey cellKey = null;
  private Integer personId = null;

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

    this.selectedBackgroundColor = selectedBackgroundColor;
    this.selectedForegroundColor = selectedForegroundColor;
    this.defaultColors = new ColorPair(Color.black, Color.white);
    this.defaultAutomatColors = new ColorPair(new Color(48, 57, 187), Color.white);
    this.disabledColors = new ColorPair(Color.black, disabledColor);
    this.errorColors = new ColorPair(Color.red, new Color(235, 235, 235));
    this.doubleErrorColors = new ColorPair(Color.red, disabledColor);
    setOpaque(true);
    setSelected(false);
    refresh();

    if (!java.beans.Beans.isDesignTime()) {

    }
  }

  public void setKey(CellKey cellKey) {
    CellKey oldKey = this.cellKey;

    this.cellKey = cellKey;
    Integer oldEventId = findEventId(oldKey);
    Integer newEventId = findEventId(cellKey);
    if (oldEventId != null) {
      Event.removePropertyChangeListener(nodeListener, oldEventId);
    }
    if (newEventId != null) {
      Event.addPropertyChangeListener(nodeListener, newEventId);
    }

    Integer oldPersonId = personId;
    Integer newPersonId = findPersonIdAndAllocation(cellKey);

    if (oldPersonId != null) {
      Person.removePropertyChangeListener(nodeListener, oldPersonId);
    }
    if (newPersonId != null) {
      Person.addPropertyChangeListener(nodeListener, newPersonId);
    }
    personId = newPersonId;
    refresh();
  }

  private Integer findEventId(CellKey cellKey) {
    if (cellKey == null) {
      return null;
    }
    return cellKey.eventId;
  }

  /**
   * Determine which person is allocated to a given job for a given event. The
   * job and the event are given by the cell key.
   *
   * Note this procedure is used by the AllocationTableCellEditor (defined in
   * AllocationTable) therfore it is public static. Sorry for the hack.
   *
   * @param cellKey
   * @return
   */
  public static Integer findPersonId(CellKey cellKey) {
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
    try {
      Event event = Manager.getEventCollection().findEntity(eventId);
      if (event == null) {
        return null; //oops
      }
      Job job = Manager.getJobCollection().findEntity(jobId);
      if (job == null) {
        return null; //oops
      }

      Allocation allocation = Manager.getAllocationCollection().findEntity(event, job);
      if (allocation == null) {
        return null; //that's OK.
      }

      Person person = allocation.getPerson();
      if (person == null) {
        return null; //oops
      }

      return person.getPersonId();

    } catch (DataBaseNotReadyException ex) {
    }
    return null;
  }

  /**
   * Determine which person is allocated to a given job for a given event. The
   * job and the event are given by the cell key.
   *
   * This procedure is a paste and copy of findPersonId, but sets some private
   * variables related to the allocation. Sorry for the hack.
   *
   * @param cellKey
   * @return
   */
  private Integer findPersonIdAndAllocation(CellKey cellKey) {
    allocIsCommitted = false;
    plannerIsAutomat = false;
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
    try {
      Event event = Manager.getEventCollection().findEntity(eventId);
      if (event == null) {
        return null; //oops
      }
      Job job = Manager.getJobCollection().findEntity(jobId);
      if (job == null) {
        return null; //oops
      }
      Allocation.removePropertyChangeListener(nodeListener, allocationId);
      Allocation allocation = Manager.getAllocationCollection().findEntity(event, job);
      if (allocation == null) {
        return null; //that's OK.
      }
      allocationId = allocation.getAllocationId();
      allocation.addPropertyChangeListener(nodeListener);
      Person person = allocation.getPerson();
      if (person == null) {
        return null; //oops
      }
      plannerIsAutomat = Allocation.PLANNER_AUTOMAT.equals(allocation.getPlanner());
      allocIsCommitted = allocation.isCommited();

      return person.getPersonId();

    } catch (DataBaseNotReadyException ex) {
    }
    return null;
  }

  public CellKey getKey() {
    return cellKey;
  }

  private void refresh() {
    PersonNode tempNode = new PersonNode(personId, Manager.getPersonCollection());
    String htmlDisplayName = tempNode.getHtmlDisplayName();
    if (htmlDisplayName != null) {
      setText(htmlDisplayName);
    } else {
      setText(tempNode.getDisplayName());
    }
    Image image = tempNode.getIcon(BeanInfo.ICON_COLOR_16x16);

    if (allocIsCommitted) {
      if (image instanceof BufferedImage) {
        image = IconManager.iconManager().getLockedImage((BufferedImage) image);
      }
    }
    Icon icon = null;
    if (image != null) {
      icon = new ImageIcon(image);
    }
    ColorPair colors = getColors();
    setBackground(colors.background);
    setForeground(colors.foreground);
    setIcon(icon);

    JPopupMenu popupMenu = null;
    if (personId != null) {
      popupMenu = tempNode.getContextMenu();
    }
    if (cellKey != null) {
      if (popupMenu == null) {
        popupMenu = new JPopupMenu();
      }
      popupMenu.insert(new ProposeAllocationAction(cellKey.eventId, cellKey.jobId), 0);
      if (personId != null) {
        popupMenu.insert(new JPopupMenu.Separator(), 1);
      }
    }
    this.setComponentPopupMenu(popupMenu);
    tempNode.destroy();
  }

  private ColorPair getColors() {
    if (cellKey == null) {
      return disabledColors;
    }
    try {
      Event event = Manager.getEventCollection().findEntity(cellKey.eventId);
      if (event == null) {
        return disabledColors;
      }
      Person person = Manager.getPersonCollection().findEntity(personId);
      if (person == null) {
        if (event.isScheduled()) {
          return defaultColors;
        } else {
          return disabledColors;
        }
      }
      Availability a = Manager.getAvailabilityCollection().findEntity(person, event.getTimeSlot());
      if (a == null) {
        return errorColors; // severe data problem detected!!!
      }
      if (a.isAvailable()) {
        if (event.isScheduled()) {
          if (plannerIsAutomat) {
            return defaultAutomatColors;
          } else {
            return defaultColors;
          }
        } else {
          return disabledColors;
        }
      } else {
        if (event.isScheduled()) {
          return errorColors;
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
   * inform the surrounding table that something has changed and the panel needs
   * to be repainted.
   */
  private void fireValueChanged() {
    firePropertyChange(PROP_VALUE_CHANGED, null, null);
  }
}
