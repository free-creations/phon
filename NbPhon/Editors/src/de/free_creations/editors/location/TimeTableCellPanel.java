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

import de.free_creations.nbPhon4Netbeans.ContestNode;
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
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class TimeTableCellPanel extends javax.swing.JPanel {

  /**
   * @Todo move color management to a central place
   */
  private static final Color disabledColor = new Color(170, 170, 170);
  private static final Color selectedBackgroundColor = new Color(57, 105, 138);
  private static final Color selectedForegroundColor = Color.WHITE;

  private ContestNode node = null;
  private final PropertyChangeListener nodeListener = new PropertyChangeListener() {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      showNode(node);
    }
  };

  public TimeTableCellPanel() {
    initComponents();
    if (!java.beans.Beans.isDesignTime()) {
      setContestId(null);
    }
  }

  private Integer contestId = Integer.MIN_VALUE;

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
    Integer old = this.contestId;
    this.contestId = contestId;
    if (!Objects.equals(old, contestId)) {
      if (node != null) {
        node.removePropertyChangeListener(nodeListener);
        try {
          node.destroy();
        } catch (IOException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
      node = new ContestNode(contestId, Manager.getContestCollection());
      node.addPropertyChangeListener(nodeListener);
      showNode(node);
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
      setBackground(Color.WHITE);
      lblContest.setForeground(Color.BLACK);
    }
  }



}
