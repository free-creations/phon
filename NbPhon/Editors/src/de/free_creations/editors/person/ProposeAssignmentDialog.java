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

import de.free_creations.actions.event.AllocatePersonForEvent;
import de.free_creations.actions.rating.AllocationRating;
import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.openide.util.Exceptions;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class ProposeAssignmentDialog extends javax.swing.JDialog {

  private static final Logger logger = Logger.getLogger(ProposeAssignmentDialog.class.getName());

  private final Integer personId;
  private final Integer timeSlotId;
  /**
   * A return status code - returned if Cancel button has been pressed
   */
  public static final int RET_CANCEL = 0;
  /**
   * A return status code - returned if OK button has been pressed
   */
  public static final int RET_OK = 1;

  private class VirtualAllocation implements Comparable<VirtualAllocation> {

    public final Integer eventId;
    public final String jobId;
    public final int score;
    public final boolean redundant;
    public final boolean vacant;
    public final String description;

    public VirtualAllocation(Person person, Event event, Job job) {
      eventId = event.getEventId();
      jobId = job.getJobId();
      AllocationRating rating = new AllocationRating(person, event, job);
      score = rating.getScore();
      redundant = rating.isRedundant();
      vacant = rating.isVacant();
      Contest contest = event.getContest();
      String contestName = (contest == null) ? "" : contest.getName();
      description = String.format("%s, %s", contestName, job.getName());

    }

    @Override
    public String toString() {
      return description;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 79 * hash + Objects.hashCode(this.eventId);
      hash = 79 * hash + Objects.hashCode(this.jobId);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final VirtualAllocation other = (VirtualAllocation) obj;
      if (!Objects.equals(this.eventId, other.eventId)) {
        return false;
      }
      if (!Objects.equals(this.jobId, other.jobId)) {
        return false;
      }
      return true;
    }

    @Override
    public int compareTo(VirtualAllocation other) {
      int compOnScore = -Integer.compare(score, other.score); // high score first
      if (compOnScore != 0) {
        return compOnScore;
      } else {
        // make it compatible with equals
        return Integer.compare(hashCode(), other.hashCode());
      }
    }
  }

  private class AllocationList extends TreeSet<VirtualAllocation> {
  };

  private class DatabaseActivationTask extends SwingWorker<AllocationList, Void> {

    private final boolean onlyVacant;

    public DatabaseActivationTask(boolean onlyVacant) {
      this.onlyVacant = onlyVacant;
    }

    @Override
    protected AllocationList doInBackground() throws Exception {
      AllocationList result = new AllocationList();
      Person person = Manager.getPersonCollection().findEntity(personId);
      List<Job> jj = Manager.getJobCollection().getAll();
      TimeSlot timeSlot = Manager.getTimeSlotCollection().findEntity(timeSlotId);
      List<Event> ee = timeSlot.getEventList();
      for (Event e : ee) {
        if (e.isScheduled()) {
          for (Job j : jj) {
            VirtualAllocation va = new VirtualAllocation(person, e, j);
            if (onlyVacant) {
              if (va.vacant) {
                result.add(va);
              }
            } else {
              result.add(va);
            }
          }
        }
      }
      return result;
    }

    @Override
    protected void done() {
      try {
        DefaultListModel<VirtualAllocation> listModel = new DefaultListModel<>();
        AllocationList allocs = get();
        for (VirtualAllocation va : allocs) {
          listModel.addElement(va);
        }
        tasksList.setModel(listModel);

      } catch (InterruptedException | ExecutionException ex) {
        Exceptions.printStackTrace(ex);
      }
      showWorkInProgress(false);
    }
  }

  private void showWorkInProgress(boolean working) {
    if (working) {
      this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
      progressBar.setIndeterminate(true);
      okButton.setEnabled(false);

    } else {
      this.setCursor(null);
      progressBar.setIndeterminate(false);
      okButton.setEnabled(true);
    }
  }

  /**
   * Creates new form ProposeAllocationDialog
   */
  public ProposeAssignmentDialog(Frame parent, Integer personId, Integer timeSlotId) {

    super(parent, true);
    this.personId = personId;
    this.timeSlotId = timeSlotId;
    initComponents();

    // analyse given variables to assemble the window title
    String givenname = "";
    String surname = "";
    String time = "";

    Person person;
    try {
      person = Manager.getPersonCollection().findEntity(personId);
      TimeSlot timeSlot = Manager.getTimeSlotCollection().findEntity(timeSlotId);
      givenname = (person == null) ? "" : person.getGivenname();
      surname = (person == null) ? "" : person.getSurname();
      time = (timeSlot == null) ? "" : timeSlot.getLabel();
    } catch (DataBaseNotReadyException ex) {
      Exceptions.printStackTrace(ex);
    }

    setTitle(String.format("New task for %s, %s at %s", surname, givenname, time));

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

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        refreshView(true);
      }
    });

  }

  private void refreshView(boolean onlyVacant) {
    showWorkInProgress(true);
    DatabaseActivationTask databaseActivationTask = new DatabaseActivationTask(onlyVacant);
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
    tasksList = new javax.swing.JList();
    progressBar = new javax.swing.JProgressBar();

    setTitle(org.openide.util.NbBundle.getMessage(ProposeAssignmentDialog.class, "ProposeAssignmentDialog.title")); // NOI18N
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        closeDialog(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(okButton, org.openide.util.NbBundle.getMessage(ProposeAssignmentDialog.class, "ProposeAssignmentDialog.okButton.text")); // NOI18N
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okButtonActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(cancelButton, org.openide.util.NbBundle.getMessage(ProposeAssignmentDialog.class, "ProposeAssignmentDialog.cancelButton.text")); // NOI18N
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });

    freeAllGroup.add(btnOnlyFree);
    btnOnlyFree.setSelected(true);
    org.openide.awt.Mnemonics.setLocalizedText(btnOnlyFree, org.openide.util.NbBundle.getMessage(ProposeAssignmentDialog.class, "ProposeAssignmentDialog.btnOnlyFree.text")); // NOI18N
    btnOnlyFree.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnOnlyFreeActionPerformed(evt);
      }
    });

    freeAllGroup.add(btnAll);
    org.openide.awt.Mnemonics.setLocalizedText(btnAll, org.openide.util.NbBundle.getMessage(ProposeAssignmentDialog.class, "ProposeAssignmentDialog.btnAll.text")); // NOI18N
    btnAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnAllActionPerformed(evt);
      }
    });

    tasksList.setModel(new javax.swing.AbstractListModel() {
      String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
      public int getSize() { return strings.length; }
      public Object getElementAt(int i) { return strings[i]; }
    });
    scrollPane.setViewportView(tasksList);

    progressBar.setIndeterminate(true);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(btnOnlyFree)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(btnAll)
        .addGap(0, 0, Short.MAX_VALUE))
      .addComponent(scrollPane)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGap(0, 10, Short.MAX_VALUE)
            .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cancelButton))
          .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnOnlyFree)
          .addComponent(btnAll))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
        .addGap(0, 0, 0)
        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
      assignSelectedNode();
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
  private javax.swing.JProgressBar progressBar;
  private javax.swing.JScrollPane scrollPane;
  private javax.swing.JList tasksList;
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

  private void assignSelectedNode() {
    Object selectedObject = tasksList.getSelectedValue();
    if (selectedObject instanceof VirtualAllocation) {
      VirtualAllocation selectedAlloc = (VirtualAllocation) selectedObject;
      AllocatePersonForEvent exe = new AllocatePersonForEvent(
              true, // removeClashing
              selectedAlloc.eventId, //  eventId
              personId, //  newPersonId
              selectedAlloc.jobId, //  jobId
              Allocation.PLANNER_USER);//  planner
      try {
        exe.apply();
      } catch (DataBaseNotReadyException | AllocatePersonForEvent.AllocationException ex) {
        Exceptions.printStackTrace(ex);
      }
    }

  }
}
