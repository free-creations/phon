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
package de.free_creations.editors.jury;

import org.junit.Test;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class AllocationTableVisualTest extends javax.swing.JFrame {

  /**
   * Creates new form AllocationTableVisualTest
   */
  public AllocationTableVisualTest() {
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

    jScrollPane2 = new javax.swing.JScrollPane();
    allocationTable1 = new de.free_creations.editors.jury.AllocationTable();
    btnBlockfloete = new javax.swing.JButton();
    btnContestNull = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jScrollPane2.setViewportView(allocationTable1);

    org.openide.awt.Mnemonics.setLocalizedText(btnBlockfloete, org.openide.util.NbBundle.getMessage(AllocationTableVisualTest.class, "AllocationTableVisualTest.btnBlockfloete.text")); // NOI18N
    btnBlockfloete.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnBlockfloeteActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnContestNull, org.openide.util.NbBundle.getMessage(AllocationTableVisualTest.class, "AllocationTableVisualTest.btnContestNull.text")); // NOI18N
    btnContestNull.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnContestNullActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(btnBlockfloete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(btnContestNull, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(0, 0, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnBlockfloete)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnContestNull)
        .addGap(43, 43, 43))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnBlockfloeteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlockfloeteActionPerformed
    allocationTable1.setJuryId(1);
  }//GEN-LAST:event_btnBlockfloeteActionPerformed

  private void btnContestNullActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContestNullActionPerformed
    allocationTable1.setJuryId(null);
  }//GEN-LAST:event_btnContestNullActionPerformed

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
      java.util.logging.Logger.getLogger(AllocationTableVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(AllocationTableVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(AllocationTableVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(AllocationTableVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        new AllocationTableVisualTest().setVisible(true);
      }
    });
  }
    @Test
  public void showIt() {
    System.out.println("Use \"Run File\" to see this test.");
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private de.free_creations.editors.jury.AllocationTable allocationTable1;
  private javax.swing.JButton btnBlockfloete;
  private javax.swing.JButton btnContestNull;
  private javax.swing.JScrollPane jScrollPane2;
  // End of variables declaration//GEN-END:variables
}
