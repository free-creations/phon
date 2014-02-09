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
package de.free_creations.editors.contest;

import de.free_creations.actions.event.AllocatePersonForEvent;
import de.free_creations.actions.rating.AllocationRating;
import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Person;
import de.free_creations.nbPhon4Netbeans.PersonNode;
import de.free_creations.nbPhon4Netbeans.PersonNodesArray;
import de.free_creations.nbPhon4Netbeans.PersonsRootNode;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.PersonCollection;
import org.netbeans.api.progress.ProgressHandle;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class ProposeAllocationDialog extends javax.swing.JDialog
        implements ExplorerManager.Provider {

  private static final Logger logger = Logger.getLogger(ProposeAllocationDialog.class.getName());
  private static final ExplorerManager explorerManager = new ExplorerManager();
  private final Integer eventId;
  private final String jobId;

  private static class AvailablePersonFilter implements PersonNodesArray.Filter {

    private final Integer filterEventId;
    private final String filterJobId;
    private final boolean filterOnlyFree;

    public AvailablePersonFilter(Integer eventId, String jobId, boolean onlyFree) {
      this.filterEventId = eventId;
      this.filterJobId = jobId;
      this.filterOnlyFree = onlyFree;
    }

    @Override
    public boolean take(Person p) {
      if (p == null) {
        return false;
      }
      AllocationRating rating = new AllocationRating(p.getPersonId(), filterEventId, filterJobId);
      if (rating.isAvailable()) {
        if (filterOnlyFree) {
          return (!rating.isClashing());
        } else {
          return true;
        }
      } else {
        return false;
      }
    }

  }
  
  private final Action doubleClickAction = new AbstractAction("Allocate") {
    
    @Override
    public void actionPerformed(ActionEvent e) {  
      allocateSelectedNode();
      doClose(RET_OK);
    }
  };

  private class DatabaseActivationTask extends SwingWorker<Void, Void> {

    private final ProgressHandle progressHandle;

    private final boolean onlyFree;
    PersonsRootNode personsRootNode = null;

    public DatabaseActivationTask(boolean onlyFree) {
      super();

      this.onlyFree = onlyFree;
      progressHandle = ProgressHandleFactory.createHandle("Connecting to Database.");
    }

    @Override
    protected Void doInBackground() throws Exception {
      try {
        progressHandle.start();
        PersonCollection pp = Manager.getPersonCollection();
        AvailablePersonFilter filter = new AvailablePersonFilter(eventId, jobId, onlyFree);
        personsRootNode = new PersonsRootNode(pp, false, filter, doubleClickAction);
      } catch (Throwable ex) {
        logger.log(Level.SEVERE, "Could not access the database.", ex);
      }
      return null;
    }

    @Override
    protected void done() {
      Manager.assertOpen();
      if (personsRootNode != null) {
        explorerManager.setRootContext(personsRootNode);
      }
      scrollPane.setCursor(null);
      progressHandle.finish();
    }
  }

  /**
   * A return status code - returned if Cancel button has been pressed
   */
  public static final int RET_CANCEL = 0;
  /**
   * A return status code - returned if OK button has been pressed
   */
  public static final int RET_OK = 1;

  /**
   * Creates new form ProposeAllocationDialog
   */
  public ProposeAllocationDialog(Frame parent, Integer eventId, String jobId) {
    super(parent, true);
    initComponents();
    this.eventId = eventId;
    this.jobId = jobId;

    Point mousePos = MouseInfo.getPointerInfo().getLocation();
    settleWindowPosition(mousePos);

    // Close the dialog when Esc is pressed
    String cancelName = "cancel";
    InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
    ActionMap actionMap = getRootPane().getActionMap();
    actionMap.put(cancelName, new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        doClose(RET_CANCEL);
      }
    });
    BeanTreeView beanTreeView = (BeanTreeView) scrollPane;
    beanTreeView.setRootVisible(false);
    //  associateLookup(ExplorerUtils.createLookup(explorerManager, getActionMap()));
    refreshView(true);
  }

  private void refreshView(boolean onlyFree) {
    scrollPane.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    DatabaseActivationTask databaseActivationTask = new DatabaseActivationTask(onlyFree);
    databaseActivationTask.execute();

  }

  /**
   * @return the return status of this dialog - one of RET_OK or RET_CANCEL
   */
  public int getReturnStatus() {
    return returnStatus;
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    freeAllGroup = new javax.swing.ButtonGroup();
    okButton = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();
    btnOnlyFree = new javax.swing.JRadioButton();
    btnAll = new javax.swing.JRadioButton();
    scrollPane = new org.openide.explorer.view.BeanTreeView();

    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        closeDialog(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(okButton, org.openide.util.NbBundle.getMessage(ProposeAllocationDialog.class, "ProposeAllocationDialog.okButton.text")); // NOI18N
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okButtonActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(cancelButton, org.openide.util.NbBundle.getMessage(ProposeAllocationDialog.class, "ProposeAllocationDialog.cancelButton.text")); // NOI18N
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });

    freeAllGroup.add(btnOnlyFree);
    btnOnlyFree.setSelected(true);
    org.openide.awt.Mnemonics.setLocalizedText(btnOnlyFree, org.openide.util.NbBundle.getMessage(ProposeAllocationDialog.class, "ProposeAllocationDialog.btnOnlyFree.text")); // NOI18N
    btnOnlyFree.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnOnlyFreeActionPerformed(evt);
      }
    });

    freeAllGroup.add(btnAll);
    org.openide.awt.Mnemonics.setLocalizedText(btnAll, org.openide.util.NbBundle.getMessage(ProposeAllocationDialog.class, "ProposeAllocationDialog.btnAll.text")); // NOI18N
    btnAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnAllActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap(22, Short.MAX_VALUE)
        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cancelButton)
        .addContainerGap())
      .addGroup(layout.createSequentialGroup()
        .addComponent(btnOnlyFree)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(btnAll)
        .addGap(0, 0, Short.MAX_VALUE))
      .addComponent(scrollPane)
    );

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnOnlyFree)
          .addComponent(btnAll))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(cancelButton)
          .addComponent(okButton))
        .addContainerGap())
    );

    getRootPane().setDefaultButton(okButton);

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
      allocateSelectedNode();
      doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
      doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

  /**
   * Closes the dialog
   */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
      doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

  private void btnOnlyFreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOnlyFreeActionPerformed

    refreshView(true);
  }//GEN-LAST:event_btnOnlyFreeActionPerformed

  private void btnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllActionPerformed
    refreshView(false);
  }//GEN-LAST:event_btnAllActionPerformed

  private void doClose(int retStatus) {
    returnStatus = retStatus;
    setVisible(false);
    dispose();
  }


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JRadioButton btnAll;
  private javax.swing.JRadioButton btnOnlyFree;
  private javax.swing.JButton cancelButton;
  private javax.swing.ButtonGroup freeAllGroup;
  private javax.swing.JButton okButton;
  private javax.swing.JScrollPane scrollPane;
  // End of variables declaration//GEN-END:variables

  private int returnStatus = RET_CANCEL;

  private void settleWindowPosition(Point mousePos) {
    int y = mousePos.y - getHeight();
    if (y < 0) {
      y = mousePos.y;
    }

    int x = mousePos.x;
    if (protrudesScreenAtRight(x)) {
      x = mousePos.x - getWidth();
    }
    setLocation(new Point(x, y));
  }

  private boolean protrudesScreenAtRight(int x) {
    Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    int screenX = screenSize.width;
    return (x + getWidth()) > screenX;
  }

  @Override
  public ExplorerManager getExplorerManager() {
    return explorerManager;
  }

  private void allocateSelectedNode() {
    Node[] selectedNodes = explorerManager.getSelectedNodes();
    for (Node n : selectedNodes) {
      if (n instanceof PersonNode) {
        allocatePerson((PersonNode) n);
        break;
      }
    }
  }

  private void allocatePerson(PersonNode personNode) {
    int key = personNode.getKey();
    AllocatePersonForEvent exe = new AllocatePersonForEvent(
            true, // removeClashing
            eventId, //  eventId
            key, //  newPersonId
            jobId, //  jobId
            Allocation.PLANNER_USER);//  planner
    try {
      exe.apply();
    } catch (DataBaseNotReadyException | AllocatePersonForEvent.AllocationException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

}
