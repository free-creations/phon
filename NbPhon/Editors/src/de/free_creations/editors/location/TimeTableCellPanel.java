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

import de.free_creations.editors.contest.*;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class TimeTableCellPanel extends javax.swing.JPanel {

  /**
   * Creates new form TimeTableCellPanel
   */
  public TimeTableCellPanel() {
    initComponents();
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    lblLocation = new javax.swing.JLabel();

    setBackground(new java.awt.Color(255, 255, 255));

    lblLocation.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    lblLocation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/free_creations/editors/resources/chairs.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(lblLocation, org.openide.util.NbBundle.getMessage(TimeTableCellPanel.class, "TimeTableCellPanel.lblLocation.text")); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(lblLocation, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(lblLocation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel lblLocation;
  // End of variables declaration//GEN-END:variables
}
