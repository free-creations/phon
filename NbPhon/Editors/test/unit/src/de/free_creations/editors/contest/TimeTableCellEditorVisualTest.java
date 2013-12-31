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
package de.free_creations.editors.contest;

import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Location;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import org.junit.Test;
import org.openide.util.Exceptions;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class TimeTableCellEditorVisualTest extends javax.swing.JFrame {

  /**
   * Creates new form TimeTableCellPanelVisiualTest
   */
  public TimeTableCellEditorVisualTest() {
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

    borderPanel = new javax.swing.JPanel();
    timeTableCellEditor1 = new de.free_creations.editors.contest.TimeTableCellEditorPanel();
    btnSetNull = new javax.swing.JButton();
    btnSetEvent_5 = new javax.swing.JButton();
    btnSelected = new javax.swing.JToggleButton();
    btnSetEvent45 = new javax.swing.JButton();
    btnSetEventMinusOne = new javax.swing.JButton();
    btnChangeLocation = new javax.swing.JButton();
    btnChangeEvent = new javax.swing.JButton();
    btnEventLocation = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    borderPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    timeTableCellEditor1.setPreferredSize(new java.awt.Dimension(30, 43));

    javax.swing.GroupLayout borderPanelLayout = new javax.swing.GroupLayout(borderPanel);
    borderPanel.setLayout(borderPanelLayout);
    borderPanelLayout.setHorizontalGroup(
      borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(timeTableCellEditor1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    borderPanelLayout.setVerticalGroup(
      borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(timeTableCellEditor1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    org.openide.awt.Mnemonics.setLocalizedText(btnSetNull, org.openide.util.NbBundle.getMessage(TimeTableCellEditorVisualTest.class, "TimeTableCellEditorVisualTest.btnSetNull.text")); // NOI18N
    btnSetNull.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSetNullActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnSetEvent_5, org.openide.util.NbBundle.getMessage(TimeTableCellEditorVisualTest.class, "TimeTableCellEditorVisualTest.btnSetEvent_5.text")); // NOI18N
    btnSetEvent_5.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSetEvent_5ActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnSelected, org.openide.util.NbBundle.getMessage(TimeTableCellEditorVisualTest.class, "TimeTableCellEditorVisualTest.btnSelected.text")); // NOI18N
    btnSelected.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSelectedActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnSetEvent45, org.openide.util.NbBundle.getMessage(TimeTableCellEditorVisualTest.class, "TimeTableCellEditorVisualTest.btnSetEvent45.text")); // NOI18N
    btnSetEvent45.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSetEvent45ActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnSetEventMinusOne, org.openide.util.NbBundle.getMessage(TimeTableCellEditorVisualTest.class, "TimeTableCellEditorVisualTest.btnSetEventMinusOne.text")); // NOI18N
    btnSetEventMinusOne.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSetEventMinusOneActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnChangeLocation, org.openide.util.NbBundle.getMessage(TimeTableCellEditorVisualTest.class, "TimeTableCellEditorVisualTest.btnChangeLocation.text")); // NOI18N
    btnChangeLocation.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnChangeLocationActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnChangeEvent, org.openide.util.NbBundle.getMessage(TimeTableCellEditorVisualTest.class, "TimeTableCellEditorVisualTest.btnChangeEvent.text")); // NOI18N
    btnChangeEvent.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnChangeEventActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnEventLocation, org.openide.util.NbBundle.getMessage(TimeTableCellEditorVisualTest.class, "TimeTableCellEditorVisualTest.btnEventLocation.text")); // NOI18N
    btnEventLocation.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnEventLocationActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(borderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnSetNull, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnSetEvent_5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnSetEvent45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnSetEventMinusOne, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnChangeLocation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnChangeEvent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnEventLocation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(borderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGap(12, 12, 12)
        .addComponent(btnSetNull)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnSetEvent_5)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnSetEvent45)
        .addGap(7, 7, 7)
        .addComponent(btnSetEventMinusOne)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnSelected)
        .addGap(19, 19, 19)
        .addComponent(btnChangeEvent)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(btnChangeLocation)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnEventLocation)
        .addGap(35, 35, 35))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectedActionPerformed

    timeTableCellEditor1.setSelected(btnSelected.isSelected());
  }//GEN-LAST:event_btnSelectedActionPerformed

  private void btnSetEvent_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetEvent_5ActionPerformed

    timeTableCellEditor1.setValue(5);
  }//GEN-LAST:event_btnSetEvent_5ActionPerformed

  private void btnSetNullActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetNullActionPerformed
    // TODO add your handling code here:
    timeTableCellEditor1.setValue(null);
  }//GEN-LAST:event_btnSetNullActionPerformed

  private void btnSetEvent45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetEvent45ActionPerformed

    timeTableCellEditor1.setValue(45);
  }//GEN-LAST:event_btnSetEvent45ActionPerformed

  private void btnSetEventMinusOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetEventMinusOneActionPerformed

    timeTableCellEditor1.setValue(-1);
  }//GEN-LAST:event_btnSetEventMinusOneActionPerformed

  private void btnChangeEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeEventActionPerformed

    Integer eventId = timeTableCellEditor1.getEventId();
    if (eventId != null) {
      try {
        Event e = Manager.getEventCollection().findEntity(eventId);
        if (e != null) {
          e.setScheduled(!e.isScheduled());
        }
      } catch (DataBaseNotReadyException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  }//GEN-LAST:event_btnChangeEventActionPerformed

  private void btnChangeLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeLocationActionPerformed
    // TODO add your handling code here:
    Integer locationId = timeTableCellEditor1.getLocationId();
    if (locationId != null) {
      try {
        Location l = Manager.getLocationCollection().findEntity(locationId);
        l.setName(l.getName()+" Test");
      } catch (DataBaseNotReadyException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  }//GEN-LAST:event_btnChangeLocationActionPerformed

  private void btnEventLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEventLocationActionPerformed
    // TODO add your handling code here:
    Integer eventId = timeTableCellEditor1.getEventId();
    if (eventId != null) {
      try {
        Event e = Manager.getEventCollection().findEntity(eventId);
        if (e != null) {
          e.setLocation(null);
        }
      } catch (DataBaseNotReadyException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  }//GEN-LAST:event_btnEventLocationActionPerformed

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
      java.util.logging.Logger.getLogger(TimeTableCellEditorVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(TimeTableCellEditorVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(TimeTableCellEditorVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(TimeTableCellEditorVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new TimeTableCellEditorVisualTest().setVisible(true);
      }
    });
  }

  @Test
  public void showIt() {
    System.out.println("Use \"Run File\" to see this test.");
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel borderPanel;
  private javax.swing.JButton btnChangeEvent;
  private javax.swing.JButton btnChangeLocation;
  private javax.swing.JButton btnEventLocation;
  private javax.swing.JToggleButton btnSelected;
  private javax.swing.JButton btnSetEvent45;
  private javax.swing.JButton btnSetEventMinusOne;
  private javax.swing.JButton btnSetEvent_5;
  private javax.swing.JButton btnSetNull;
  private de.free_creations.editors.contest.TimeTableCellEditorPanel timeTableCellEditor1;
  // End of variables declaration//GEN-END:variables
}
