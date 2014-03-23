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
import de.free_creations.dbEntities.Allocation;
import de.free_creations.nbPhonAPI.AllocationCollection;
import de.free_creations.nbPhonAPI.Manager;
import java.awt.Color;
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

  private static final String txtStop = "Stop";
  private static final String txtClose = "Close";

  private class Allocator extends SwingWorker<Void, ProgressIndicator> {

    private final boolean onlyRemoval;
    private boolean halted;
    private String endMessage = "";

    public Allocator(boolean onlyRemoval) {
      this.onlyRemoval = onlyRemoval;
      halted = false;
    }

    @Override
    protected Void doInBackground() throws Exception {
      AutomaticAllocationExecutor exe = new AutomaticAllocationExecutor(onlyRemoval);
      boolean more = true;
      while (more && (!halted)) {
        publish(exe.getProgress());
        Thread.sleep(1); // unfortunate, but we must give the AWT thread some time to refresh
        more = exe.doNext();
      }
      endMessage = exe.getProcessQuality();
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
        get(0, TimeUnit.MICROSECONDS); // to make sure the thread has finished

        setAllActionsEnabled(true);
        lblProcessQuality.setForeground(Color.black);
        lblProcessQuality.setText(endMessage);
        stopCloseButton.setText(txtClose);
        action = null;

      } catch (ExecutionException ex) {
        // The Automatic Allocation Executor had a problem.
        Throwable cause = ex.getCause();
        lblProcessQuality.setForeground(Color.red);
        lblProcessQuality.setText("<html>" + cause.getMessage() + "/<html>");
        btnImprove.setEnabled(false);
        btnAllocationFromScratch.setEnabled(false);
        stopCloseButton.setText(txtClose);
      } catch (CancellationException | InterruptedException | TimeoutException ex) {
        // This should never happen!!!
        Exceptions.printStackTrace(ex);
      }

    }
  }

  private Allocator action = null;

  /**
   * Creates new form NewOkCancelDialog
   *
   * @param parent
   * @param modal
   */
  public AutomaticAllocationDialog(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();

    if (parent != null) {
      setLocationRelativeTo(parent);
    }
    lblMessage.setText("");
    lblProcessQuality.setText("");
    stopCloseButton.setText(txtStop);
    setAllActionsEnabled(true);
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

    btnImprove = new javax.swing.JButton();
    btnAllocationFromScratch = new javax.swing.JButton();
    stopCloseButton = new javax.swing.JButton();
    progressBar = new javax.swing.JProgressBar();
    lblMessage = new javax.swing.JLabel();
    lblProcessQuality = new javax.swing.JLabel();
    btnCommit = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        CancelAndClose(evt);
      }
    });

    btnImprove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/free_creations/actions/automaticAllocation/allocate.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(btnImprove, org.openide.util.NbBundle.getMessage(AutomaticAllocationDialog.class, "AutomaticAllocationDialog.btnImprove.text")); // NOI18N
    btnImprove.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnImprove.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnImproveActionPerformed(evt);
      }
    });

    btnAllocationFromScratch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/free_creations/actions/automaticAllocation/deAllocate16.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(btnAllocationFromScratch, org.openide.util.NbBundle.getMessage(AutomaticAllocationDialog.class, "AutomaticAllocationDialog.btnAllocationFromScratch.text")); // NOI18N
    btnAllocationFromScratch.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnAllocationFromScratch.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnAllocationFromScratchActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(stopCloseButton, org.openide.util.NbBundle.getMessage(AutomaticAllocationDialog.class, "AutomaticAllocationDialog.stopCloseButton.text")); // NOI18N
    stopCloseButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        stopCloseButtonActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(lblMessage, org.openide.util.NbBundle.getMessage(AutomaticAllocationDialog.class, "AutomaticAllocationDialog.lblMessage.text")); // NOI18N

    lblProcessQuality.setForeground(java.awt.Color.red);
    org.openide.awt.Mnemonics.setLocalizedText(lblProcessQuality, org.openide.util.NbBundle.getMessage(AutomaticAllocationDialog.class, "AutomaticAllocationDialog.lblProcessQuality.text")); // NOI18N
    lblProcessQuality.setVerticalAlignment(javax.swing.SwingConstants.TOP);

    btnCommit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/free_creations/actions/automaticAllocation/lock16.png"))); // NOI18N
    org.openide.awt.Mnemonics.setLocalizedText(btnCommit, org.openide.util.NbBundle.getMessage(AutomaticAllocationDialog.class, "AutomaticAllocationDialog.btnCommit.text")); // NOI18N
    btnCommit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnCommit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCommitActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(lblProcessQuality, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addComponent(lblMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addGap(0, 62, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(btnImprove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(stopCloseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(btnAllocationFromScratch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(btnCommit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(0, 63, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGap(51, 51, 51)
        .addComponent(btnImprove)
        .addGap(18, 18, 18)
        .addComponent(btnCommit)
        .addGap(19, 19, 19)
        .addComponent(btnAllocationFromScratch)
        .addGap(18, 18, 18)
        .addComponent(stopCloseButton)
        .addGap(18, 18, 18)
        .addComponent(lblMessage)
        .addGap(4, 4, 4)
        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(lblProcessQuality, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
        .addGap(27, 27, 27))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void stopCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopCloseButtonActionPerformed

      if (action == null) {
        CancelAndClose(null);
      } else {
        stop();
      }
    }//GEN-LAST:event_stopCloseButtonActionPerformed

  private void startAllocationAction(boolean onlyRemoval) {
    if (action == null) {
      progressBar.setValue(0);
      lblMessage.setText("");
      lblProcessQuality.setText("");
      setAllActionsEnabled(false);
      stopCloseButton.setText(txtStop);
      action = new Allocator(onlyRemoval);
      action.execute();
    }
  }

  private void setAllActionsEnabled(boolean value) {
    btnImprove.setEnabled(value);
    btnAllocationFromScratch.setEnabled(value);
    btnCommit.setEnabled(value);
  }

  /**
   * stop the running action
   */
  private void stop() {
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
    action = null;
  }

  /**
   * Closes the dialog and cancels further allocation work.
   */
    private void CancelAndClose(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_CancelAndClose
      stop();
      doClose();
    }//GEN-LAST:event_CancelAndClose

  private void btnImproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImproveActionPerformed
    startAllocationAction(false);
  }//GEN-LAST:event_btnImproveActionPerformed

  private void btnAllocationFromScratchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllocationFromScratchActionPerformed
    startAllocationAction(true);
  }//GEN-LAST:event_btnAllocationFromScratchActionPerformed

  private void btnCommitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCommitActionPerformed
    setAllActionsEnabled(false);
    progressBar.setValue(0);
    lblMessage.setText("");
    repaint();

    List<Allocation> aa = Manager.getAllocationCollection().getAll();
    for (Allocation a : aa) {
      a.setCommited(true);
    }
    setAllActionsEnabled(true);
    progressBar.setValue(100);
    lblMessage.setText("All Allocations committed.");


  }//GEN-LAST:event_btnCommitActionPerformed

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
  private javax.swing.JButton btnAllocationFromScratch;
  private javax.swing.JButton btnCommit;
  private javax.swing.JButton btnImprove;
  private javax.swing.JLabel lblMessage;
  private javax.swing.JLabel lblProcessQuality;
  private javax.swing.JProgressBar progressBar;
  private javax.swing.JButton stopCloseButton;
  // End of variables declaration//GEN-END:variables

}
