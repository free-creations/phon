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
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import org.junit.Test;
import org.openide.util.Exceptions;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class PersonAssignmentTableCellPanelVisualTest extends javax.swing.JFrame {

  /**
   * Creates new form PersonAssignmentTableCellPanelVisualTest
   */
  public PersonAssignmentTableCellPanelVisualTest() {
    boolean open = Manager.assertOpen();
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

    jPanel1 = new javax.swing.JPanel();
    personAssignmentTableCellPanel1 = new de.free_creations.editors.person.PersonAssignmentTableCellPanel();
    btnSetNull = new javax.swing.JButton();
    btnSet_1 = new javax.swing.JButton();
    btnEventScheduled = new javax.swing.JToggleButton();
    btnAvailability = new javax.swing.JToggleButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.controlShadow));

    org.openide.awt.Mnemonics.setLocalizedText(personAssignmentTableCellPanel1, org.openide.util.NbBundle.getMessage(PersonAssignmentTableCellPanelVisualTest.class, "PersonAssignmentTableCellPanelVisualTest.personAssignmentTableCellPanel1.text")); // NOI18N

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(personAssignmentTableCellPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(personAssignmentTableCellPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
    );

    org.openide.awt.Mnemonics.setLocalizedText(btnSetNull, org.openide.util.NbBundle.getMessage(PersonAssignmentTableCellPanelVisualTest.class, "PersonAssignmentTableCellPanelVisualTest.btnSetNull.text")); // NOI18N
    btnSetNull.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSetNullActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnSet_1, org.openide.util.NbBundle.getMessage(PersonAssignmentTableCellPanelVisualTest.class, "PersonAssignmentTableCellPanelVisualTest.btnSet_1.text")); // NOI18N
    btnSet_1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSet_1ActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnEventScheduled, org.openide.util.NbBundle.getMessage(PersonAssignmentTableCellPanelVisualTest.class, "PersonAssignmentTableCellPanelVisualTest.btnEventScheduled.text")); // NOI18N
    btnEventScheduled.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnEventScheduledActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnAvailability, org.openide.util.NbBundle.getMessage(PersonAssignmentTableCellPanelVisualTest.class, "PersonAssignmentTableCellPanelVisualTest.btnAvailability.text")); // NOI18N
    btnAvailability.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnAvailabilityActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnSetNull, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnSet_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnEventScheduled, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnAvailability, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGap(29, 29, 29)
        .addComponent(btnSetNull)
        .addGap(18, 18, 18)
        .addComponent(btnSet_1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(btnEventScheduled)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnAvailability)
        .addGap(67, 67, 67))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnSetNullActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetNullActionPerformed
    // TODO add your handling code here:
    personAssignmentTableCellPanel1.setAllocationId(null);
  }//GEN-LAST:event_btnSetNullActionPerformed
  private Event event = null;
  Availability avail = null;
  private void btnSet_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSet_1ActionPerformed
    // TODO add your handling code here:
    personAssignmentTableCellPanel1.setAllocationId(1);
    event = null;
    try {
      Allocation a = Manager.getAllocationCollection().findEntity(1);
      event = (a == null) ? null : a.getEvent();
      TimeSlot timeSlot = (event == null) ? null : event.getTimeSlot();
      Person person = (a == null) ? null : a.getPerson();
      avail = Manager.getAvailabilityCollection().findEntity(person, timeSlot);
      Integer availabilityId = (avail == null) ? null : avail.getAvailabilityId();
      personAssignmentTableCellPanel1.startListening(availabilityId);
    } catch (DataBaseNotReadyException ex) {
    }
  }//GEN-LAST:event_btnSet_1ActionPerformed

  private void btnEventScheduledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEventScheduledActionPerformed
    // TODO add your handling code here:
    if (event != null) {
      event.setScheduled(btnEventScheduled.isSelected());
    }
  }//GEN-LAST:event_btnEventScheduledActionPerformed

  private void btnAvailabilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAvailabilityActionPerformed
    // TODO add your handling code here:
    if (avail != null) {
      avail.setAvailable(btnAvailability.isSelected());
    }
  }//GEN-LAST:event_btnAvailabilityActionPerformed

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(PersonAssignmentTableCellPanelVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(PersonAssignmentTableCellPanelVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(PersonAssignmentTableCellPanelVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(PersonAssignmentTableCellPanelVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new PersonAssignmentTableCellPanelVisualTest().setVisible(true);
      }
    });
  }

  @Test
  public void showIt() {
    System.out.println("Use \"Run File\" to see this test.");
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JToggleButton btnAvailability;
  private javax.swing.JToggleButton btnEventScheduled;
  private javax.swing.JButton btnSetNull;
  private javax.swing.JButton btnSet_1;
  private javax.swing.JPanel jPanel1;
  private de.free_creations.editors.person.PersonAssignmentTableCellPanel personAssignmentTableCellPanel1;
  // End of variables declaration//GEN-END:variables
}