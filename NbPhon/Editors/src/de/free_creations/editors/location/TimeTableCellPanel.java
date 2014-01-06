/*
 * Copyright 2013 Harald Postner <Harald at free-creations.de>.
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
package de.free_creations.editors.location;

import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhon4Netbeans.ContestNode;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.awt.Color;
import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Objects;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 * Shows a single contest in the time table on the Location top component.
 *
 * Implementation Note: this was an attempt to use the (NetBeans) ContestNode
 * directly for display. This implementation is kludgy because a time table does
 * not display contests them selves but events for contests.
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class TimeTableCellPanel extends javax.swing.JPanel {

  /**
   * Indicates that the value of displayed by this panel has changed.
   */
  public static final String PROP_VALUE_CHANGED = "PROP_VALUE_CHANGED";
  private final Integer timeslotId;

  private Integer contestId = Integer.MIN_VALUE;
  private Integer eventId;
  /**
   * @Todo move color management to a central place
   */
  private Color disabledColor;
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
  private ContestNode node = null;
  private final PropertyChangeListener nodeListener = new PropertyChangeListener() {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      fireValueChanged();
      showNode(node);
    }
  };

  public TimeTableCellPanel() {
    this(null, new Color(170, 170, 170), new Color(57, 105, 138), Color.WHITE);
  }

  public TimeTableCellPanel(Integer timeslotId, Color disabledColor, Color selectedBackgroundColor, Color selectedForegroundColor) {
    this.disabledColor = disabledColor;
    this.selectedBackgroundColor = selectedBackgroundColor;
    this.selectedForegroundColor = selectedForegroundColor;
    this.defaultColors = new ColorPair(Color.black, Color.white);
    this.disabledColors = new ColorPair(Color.black, disabledColor);
    this.timeslotId = timeslotId;
    initComponents();
    if (!java.beans.Beans.isDesignTime()) {
      setContestId(null);
    }
  }

  /**
   * Get the value of contestId
   *
   * @return the value of contestId
   */
  public Integer getContestId() {
    return contestId;
  }

  /**
   * Set the value of contestId
   *
   * @param contestId new value of contestId
   */
  public final void setContestId(Integer contestId) {
    Integer oldContestId = this.contestId;
    this.contestId = contestId;
    if (!Objects.equals(oldContestId, contestId)) {
      if (node != null) {
        try {
          node.destroy();
        } catch (IOException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
      if (oldContestId != null) {
        Contest.removePropertyChangeListener(nodeListener, oldContestId);
      }
      Integer oldEventId = this.eventId;
      Integer newEventId = findEventId(); // in a better implementation this should be passed as param
      if (!Objects.equals(oldEventId, newEventId)) {
        if (oldEventId != null) {
          Event.removePropertyChangeListener(nodeListener, oldEventId);
        }
        if (newEventId != null) {
          Event.addPropertyChangeListener(nodeListener, newEventId);
        }
        this.eventId = newEventId;
      }

      node = new ContestNode(contestId, Manager.getContestCollection());

      Contest.addPropertyChangeListener(nodeListener, contestId);
      showNode(node);
      fireValueChanged();
    }
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    lblContest = new javax.swing.JLabel();

    setBackground(new java.awt.Color(255, 255, 255));

    lblContest.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    lblContest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/free_creations/editors/resources/chairs.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(lblContest, org.openide.util.NbBundle.getMessage(TimeTableCellPanel.class, "TimeTableCellPanel.lblContest.text")); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(lblContest, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(lblContest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel lblContest;
  // End of variables declaration//GEN-END:variables

  private void showNode(Node node) {

    if (node == null) {
      lblContest.setText("null");
      lblContest.setIcon(null);
      this.setComponentPopupMenu(null);
      return;
    }

    String htmlDisplayName = node.getHtmlDisplayName();
    if (htmlDisplayName != null) {
      lblContest.setText(htmlDisplayName);
    } else {
      lblContest.setText(node.getDisplayName());
    }
    Image image = node.getIcon(BeanInfo.ICON_COLOR_16x16);
    Icon icon = null;
    if (image != null) {
      icon = new ImageIcon(image);
    }

    lblContest.setIcon(icon);
    JPopupMenu popupMenu = null;
    if (contestId != null) {
      popupMenu = node.getContextMenu();
      popupMenu.addSeparator();
    }
    this.setComponentPopupMenu(popupMenu);
    ColorPair colors = getColors();
    setBackground(colors.background);
    lblContest.setForeground(colors.foreground);
  }

  void setValue(Object value) {
    if (value instanceof Integer) {
      setContestId((Integer) value);
    } else {
      setContestId(null);
    }
  }

  void setSelected(boolean selected) {
    if (selected) {
      setBackground(selectedBackgroundColor);
      lblContest.setForeground(selectedForegroundColor);
    } else {
      ColorPair colors = getColors();
      setBackground(colors.background);
      lblContest.setForeground(colors.foreground);
    }
  }

  private Integer findEventId() {
    try {
      Contest contest = Manager.getContestCollection().findEntity(contestId);
      if (contest == null) {
        return null;
      }
      TimeSlot timeSlot = Manager.getTimeSlotCollection().findEntity(timeslotId);
      if (timeSlot == null) {
        return null;
      }
      Event event = Manager.getEventCollection().findEntity(contest, timeSlot);
      if (event == null) {
        return null;
      }
      return event.getEventId();

    } catch (DataBaseNotReadyException ex) {
      return null;
    }
  }

  private ColorPair getColors() {
    if (contestId == null) {
      return disabledColors;
    }
    try {
      Event event = Manager.getEventCollection().findEntity(eventId);
      if (event == null) {
        return disabledColors;
      }
      if (event.isScheduled()) {
        return defaultColors;
      } else {
        return disabledColors;
      }

    } catch (DataBaseNotReadyException ex) {
    }

    return defaultColors;
  }

  private void fireValueChanged() {
    firePropertyChange(PROP_VALUE_CHANGED, null, null);
  }

}
