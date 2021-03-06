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
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.TimeSlotCollection;
import org.junit.Test;
import org.openide.util.Exceptions;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class TimeTableCellVisualTest extends javax.swing.JFrame {

  /**
   * Creates new form TimeTableVisualTest
   */
  public TimeTableCellVisualTest() {
  }

  protected void initialize() {
    boolean open = Manager.assertOpen();
    initComponents();
    if(open){
      lblWarning.setText("");     
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

    setContestId10 = new javax.swing.JButton();
    setContestIdNull = new javax.swing.JButton();
    lblWarning = new javax.swing.JLabel();
    timeTableCellPanel1 = new de.free_creations.editors.location.TimeTableCellPanel();
    btnSelected = new javax.swing.JToggleButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    org.openide.awt.Mnemonics.setLocalizedText(setContestId10, org.openide.util.NbBundle.getMessage(TimeTableCellVisualTest.class, "TimeTableCellVisualTest.setContestId10.text")); // NOI18N
    setContestId10.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        setContestId10ActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(setContestIdNull, org.openide.util.NbBundle.getMessage(TimeTableCellVisualTest.class, "TimeTableCellVisualTest.setContestIdNull.text")); // NOI18N
    setContestIdNull.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        setContestIdNullActionPerformed(evt);
      }
    });

    lblWarning.setForeground(new java.awt.Color(255, 0, 0));
    org.openide.awt.Mnemonics.setLocalizedText(lblWarning, org.openide.util.NbBundle.getMessage(TimeTableCellVisualTest.class, "TimeTableCellVisualTest.lblWarning.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(btnSelected, org.openide.util.NbBundle.getMessage(TimeTableCellVisualTest.class, "TimeTableCellVisualTest.btnSelected.text")); // NOI18N
    btnSelected.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSelectedActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(timeTableCellPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
          .addComponent(lblWarning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(setContestId10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(setContestIdNull, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(timeTableCellPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(setContestId10)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(setContestIdNull)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnSelected)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(lblWarning)
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void setContestId10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setContestId10ActionPerformed
    // 
    timeTableCellPanel1.setContestId(10);
  }//GEN-LAST:event_setContestId10ActionPerformed

  private void setContestIdNullActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setContestIdNullActionPerformed
    // 
        timeTableCellPanel1.setContestId(null);
  }//GEN-LAST:event_setContestIdNullActionPerformed

  private void btnSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectedActionPerformed
    // TODO add your handling code here:
     timeTableCellPanel1.setSelected(btnSelected.isSelected());
  }//GEN-LAST:event_btnSelectedActionPerformed

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
      java.util.logging.Logger.getLogger(TimeTableCellVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(TimeTableCellVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(TimeTableCellVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(TimeTableCellVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        TimeTableCellVisualTest timeTableVisualTest = new TimeTableCellVisualTest();
        timeTableVisualTest.initialize();
        timeTableVisualTest.setVisible(true);
      }
    });
  }

  @Test
  public void showIt() {
    System.out.println("Use \"Run File\" to see this test.");
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JToggleButton btnSelected;
  private javax.swing.JLabel lblWarning;
  private javax.swing.JButton setContestId10;
  private javax.swing.JButton setContestIdNull;
  private de.free_creations.editors.location.TimeTableCellPanel timeTableCellPanel1;
  // End of variables declaration//GEN-END:variables
}
