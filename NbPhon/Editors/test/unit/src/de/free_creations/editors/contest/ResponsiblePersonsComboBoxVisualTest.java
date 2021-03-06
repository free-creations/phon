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
package de.free_creations.editors.contest;



import de.free_creations.nbPhonAPI.Manager;
import org.junit.Test;

/**
 * 
 * @author Harald Postner <Harald at free-creations.de>
 */
public class ResponsiblePersonsComboBoxVisualTest extends javax.swing.JFrame {

  /**
   * Creates new form TimeTableVisualTest
   */
  public ResponsiblePersonsComboBoxVisualTest() {
    
  }
  protected void initialize() {
    initComponents();
    if (Manager.isOpen()) {
      jLabel1.setText("Connection to DB OK.");
    } else {
      jLabel1.setText("DB not connected!!!");
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

    jLabel1 = new javax.swing.JLabel();
    btnSetKeyTo12 = new javax.swing.JButton();
    btnSetKeyTo50000 = new javax.swing.JButton();
    btnSetKeyToNull = new javax.swing.JButton();
    responsiblePersonsComboBox1 = new de.free_creations.editors.contest.ResponiblePersonsComboBox();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ResponsiblePersonsComboBoxVisualTest.class, "ResponsiblePersonsComboBoxVisualTest.jLabel1.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(btnSetKeyTo12, org.openide.util.NbBundle.getMessage(ResponsiblePersonsComboBoxVisualTest.class, "ResponsiblePersonsComboBoxVisualTest.btnSetKeyTo12.text")); // NOI18N
    btnSetKeyTo12.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSetKeyTo12ActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnSetKeyTo50000, org.openide.util.NbBundle.getMessage(ResponsiblePersonsComboBoxVisualTest.class, "ResponsiblePersonsComboBoxVisualTest.btnSetKeyTo50000.text")); // NOI18N
    btnSetKeyTo50000.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSetKeyTo50000ActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnSetKeyToNull, org.openide.util.NbBundle.getMessage(ResponsiblePersonsComboBoxVisualTest.class, "ResponsiblePersonsComboBoxVisualTest.btnSetKeyToNull.text")); // NOI18N
    btnSetKeyToNull.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSetKeyToNullActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(114, 114, 114)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
            .addComponent(responsiblePersonsComboBox1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnSetKeyToNull, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnSetKeyTo50000, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
            .addComponent(btnSetKeyTo12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        .addContainerGap(138, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(responsiblePersonsComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(btnSetKeyTo12)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnSetKeyTo50000)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnSetKeyToNull)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
        .addComponent(jLabel1)
        .addGap(54, 54, 54))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnSetKeyTo12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetKeyTo12ActionPerformed
    // TODO add your handling code here:
    responsiblePersonsComboBox1.setSelectedPersonId(12);
  }//GEN-LAST:event_btnSetKeyTo12ActionPerformed

  private void btnSetKeyTo50000ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetKeyTo50000ActionPerformed
    // TODO add your handling code here:
    responsiblePersonsComboBox1.setSelectedPersonId(50000);
  }//GEN-LAST:event_btnSetKeyTo50000ActionPerformed

  private void btnSetKeyToNullActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetKeyToNullActionPerformed
    // TODO add your handling code here:
    responsiblePersonsComboBox1.setSelectedPersonId(null);
  }//GEN-LAST:event_btnSetKeyToNullActionPerformed

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
      java.util.logging.Logger.getLogger(ResponsiblePersonsComboBoxVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(ResponsiblePersonsComboBoxVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(ResponsiblePersonsComboBoxVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(ResponsiblePersonsComboBoxVisualTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        ResponsiblePersonsComboBoxVisualTest personsComboBoxVisualTest = new ResponsiblePersonsComboBoxVisualTest();
        personsComboBoxVisualTest.initialize();                
        personsComboBoxVisualTest.setVisible(true);
      }
    });
  }

  @Test
  public void showIt() {
    System.out.println("Use \"Run File\" to see this test.");
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnSetKeyTo12;
  private javax.swing.JButton btnSetKeyTo50000;
  private javax.swing.JButton btnSetKeyToNull;
  private javax.swing.JLabel jLabel1;
  private de.free_creations.editors.contest.ResponiblePersonsComboBox responsiblePersonsComboBox1;
  // End of variables declaration//GEN-END:variables
}
