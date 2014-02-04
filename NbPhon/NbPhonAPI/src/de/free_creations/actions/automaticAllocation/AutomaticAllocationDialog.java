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
package de.free_creations.actions.automaticAllocation;

import de.free_creations.actions.automaticAllocation.AutomaticAllocationExecutor.ProgressIndicator;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import org.openide.util.Exceptions;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AutomaticAllocationDialog extends javax.swing.JDialog {

  private class Allocator extends SwingWorker<Void, ProgressIndicator> {

    private final boolean fullReAllocation;
    private boolean halted;

    public Allocator(boolean fullReAllocation) {
      this.fullReAllocation = fullReAllocation;
      halted = false;
    }

    @Override
    protected Void doInBackground() throws Exception {
      AutomaticAllocationExecutor exe = new AutomaticAllocationExecutor(fullReAllocation);
      boolean more = true;
      while (more && (!halted)) {
        publish(exe.getProgress());
        more = exe.doNext();
      }
      return null;
    }

    public void halt() {
      halted = true;
    }

    @Override
    protected void process(List<ProgressIndicator> chunks) {
      ProgressIndicator last = chunks.get(chunks.size() - 1);
      progressBar.setValue(last.percentFinished);
      lblMessage.setText(last.message);
    }

    @Override
    protected void done() {
      try {
        get(0, TimeUnit.MICROSECONDS);
        doClose(); // normal end
      } catch (ExecutionException ex) {
        // The Automatic Allocation Executor had a problem.
        Throwable cause = ex.getCause();
        lblError.setText(cause.getMessage());
        btnAllocateRemaining.setEnabled(false);
        btnFullAllocation.setEnabled(false);
      } catch (CancellationException | InterruptedException | TimeoutException ex) {
        // This should never happen!!!
        Exceptions.printStackTrace(ex);
      }

    }
  }

  private Allocator action = null;

  /**
   * Creates new form NewOkCancelDialog
   */
  public AutomaticAllocationDialog(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();

    if (parent != null) {
      setLocationRelativeTo(parent);
    }
    lblMessage.setText("");
    lblError.setText("");
    // Close the dialog when Esc is pressed
    String cancelName = "cancel";
    InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
    ActionMap actionMap = getRootPane().getActionMap();
    actionMap.put(cancelName, new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        CancelAndClose(null);
      }
    });
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    btnFullAllocation = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();
    btnAllocateRemaining = new javax.swing.JButton();
    progressBar = new javax.swing.JProgressBar();
    lblMessage = new javax.swing.JLabel();
    lblError = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        CancelAndClose(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnFullAllocation, org.openide.util.NbBundle.getMessage(AutomaticAllocationDialog.class, "AutomaticAllocationDialog.btnFullAllocation.text")); // NOI18N
    btnFullAllocation.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnFullAllocationActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(cancelButton, org.openide.util.NbBundle.getMessage(AutomaticAllocationDialog.class, "AutomaticAllocationDialog.cancelButton.text")); // NOI18N
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(btnAllocateRemaining, org.openide.util.NbBundle.getMessage(AutomaticAllocationDialog.class, "AutomaticAllocationDialog.btnAllocateRemaining.text")); // NOI18N
    btnAllocateRemaining.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnAllocateRemainingActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(lblMessage, org.openide.util.NbBundle.getMessage(AutomaticAllocationDialog.class, "AutomaticAllocationDialog.lblMessage.text")); // NOI18N

    lblError.setForeground(java.awt.Color.red);
    org.openide.awt.Mnemonics.setLocalizedText(lblError, org.openide.util.NbBundle.getMessage(AutomaticAllocationDialog.class, "AutomaticAllocationDialog.lblError.text")); // NOI18N
    lblError.setVerticalAlignment(javax.swing.SwingConstants.TOP);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(lblError, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addComponent(lblMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addGap(0, 67, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(btnFullAllocation, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(btnAllocateRemaining, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 68, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGap(65, 65, 65)
        .addComponent(btnFullAllocation)
        .addGap(18, 18, 18)
        .addComponent(btnAllocateRemaining)
        .addGap(48, 48, 48)
        .addComponent(cancelButton)
        .addGap(18, 18, 18)
        .addComponent(lblMessage)
        .addGap(4, 4, 4)
        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(lblError)
        .addContainerGap())
    );

    getRootPane().setDefaultButton(btnFullAllocation);

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void btnFullAllocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFullAllocationActionPerformed
      if (action == null) {
        action = new Allocator(true);
        action.execute();
      }
    }//GEN-LAST:event_btnFullAllocationActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
      CancelAndClose(null);
    }//GEN-LAST:event_cancelButtonActionPerformed

  /**
   * Closes the dialog and cancels further allocation work.
   */
    private void CancelAndClose(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_CancelAndClose
      if (action != null) {
        action.halt();
        // wait until the last chunk has been done
        try {
          // wait at maximum for three seconds
          action.get(3000, TimeUnit.MILLISECONDS);
        } catch (CancellationException | InterruptedException | ExecutionException ex) {
          //Exceptions.printStackTrace(ex);
        } catch (TimeoutException ex) {
          // did not finish the last chunk within 3 seconds. Try to kill the process as the last resort.
          action.cancel(true);
          Exceptions.printStackTrace(ex);
        }
      }
      doClose();
    }//GEN-LAST:event_CancelAndClose

  private void btnAllocateRemainingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllocateRemainingActionPerformed
    if (action == null) {
      action = new Allocator(false);
      action.execute();
    }
  }//GEN-LAST:event_btnAllocateRemainingActionPerformed

  private void doClose() {
    setVisible(false);
    dispose();
  }

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
      java.util.logging.Logger.getLogger(AutomaticAllocationDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(AutomaticAllocationDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(AutomaticAllocationDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(AutomaticAllocationDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the dialog */
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        AutomaticAllocationDialog dialog = new AutomaticAllocationDialog(new javax.swing.JFrame(), true);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
          @Override
          public void windowClosing(java.awt.event.WindowEvent e) {
            System.exit(0);
          }
        });
        dialog.setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnAllocateRemaining;
  private javax.swing.JButton btnFullAllocation;
  private javax.swing.JButton cancelButton;
  private javax.swing.JLabel lblError;
  private javax.swing.JLabel lblMessage;
  private javax.swing.JProgressBar progressBar;
  // End of variables declaration//GEN-END:variables

}