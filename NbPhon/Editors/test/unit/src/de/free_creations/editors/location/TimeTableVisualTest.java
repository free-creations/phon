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
public class TimeTableVisualTest extends javax.swing.JFrame {

  /**
   * Creates new form TimeTableVisualTest
   */
  public TimeTableVisualTest() {
  }

  protected void initialize() {
    boolean open = Manager.assertOpen();
//    if(open){
//      try {
//        Manager.commit();
//        TimeSlotCollection.addTestItem();
//      } catch (DataBaseNotReadyException ex) {
//        Exceptions.printStackTrace(ex);
//      }
//      
//    }
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

    jScrollPane1 = new javax.swing.JScrollPane();
    timeTable1 = new de.free_creations.editors.location.TimeTable();
    btnSetLocationId_3 = new javax.swing.JButton();
    btnSetLocationId_null = new javax.swing.JButton();
    btnSetLocationId_4 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jScrollPane1.setViewportView(timeTable1);

    org.openide.awt.Mnemonics.setLocalizedText(btnSetLocationId_3, org.openide.util.NbBundle.getMessage(TimeTableVisualTest.class, "TimeTableVisualTest.btnSetLocationId_3.text")); // NOI18N
    btnSetLocationId_3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSetLocationId_3ActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnSetLocationId_null, org.openide.util.NbBundle.getMessage(TimeTableVisualTest.class, "TimeTableVisualTest.btnSetLocationId_null.text")); // NOI18N
    btnSetLocationId_null.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSetLocationId_nullActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnSetLocationId_4, org.openide.util.NbBundle.getMessage(TimeTableVisualTest.class, "TimeTableVisualTest.btnSetLocationId_4.text")); // NOI18N
    btnSetLocationId_4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSetLocationId_4ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
      .addGroup(layout.createSequentialGroup()
        .addGap(45, 45, 45)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(btnSetLocationId_null, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnSetLocationId_3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnSetLocationId_4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGap(140, 140, 140))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnSetLocationId_3)
          .addComponent(btnSetLocationId_4))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnSetLocationId_null)
        .addGap(38, 38, 38))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnSetLocationId_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetLocationId_3ActionPerformed
    // TODO add your handling code here:
    timeTable1.setLocationId(3);
  }//GEN-LAST:event_btnSetLocationId_3ActionPerformed

  private void btnSetLocationId_nullActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetLocationId_nullActionPerformed
    // TODO add your handling code here:
        timeTable1.setLocationId(null);
  }//GEN-LAST:event_btnSetLocationId_nullActionPerformed

  private void btnSetLocationId_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetLocationId_4ActionPerformed
    // TODO add your handling code here:
    timeTable1.setLocationId(4);
  }//GEN-LAST:event_btnSetLocationId_4ActionPerformed

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
      java.util.logging.Logger.getLogger(TimeTableVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(TimeTableVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(TimeTableVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(TimeTableVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        TimeTableVisualTest timeTableVisualTest = new TimeTableVisualTest();
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
  private javax.swing.JButton btnSetLocationId_3;
  private javax.swing.JButton btnSetLocationId_4;
  private javax.swing.JButton btnSetLocationId_null;
  private javax.swing.JScrollPane jScrollPane1;
  private de.free_creations.editors.location.TimeTable timeTable1;
  // End of variables declaration//GEN-END:variables
}
