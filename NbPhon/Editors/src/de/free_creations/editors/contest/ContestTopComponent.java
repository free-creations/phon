/*
 * Copyright 2013 harald.
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

import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.ContestType;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.ContestCollection;
import de.free_creations.nbPhonAPI.Manager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.CloneableTopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//de.free_creations.editors.contest//Contest//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "ContestTopComponent",
        iconBase = "de/free_creations/editors/contest/chairs.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "de.free_creations.editors.contest.ContestTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ContestAction",
        preferredID = "ContestTopComponent")
@Messages({
  "CTL_ContestAction=Contest",
  "CTL_ContestTopComponent=Contest",
  "HINT_ContestTopComponent=Shows the details of a Contest"
})
public final class ContestTopComponent extends CloneableTopComponent {

  private Integer currentKey = null;
  private final ContestCollection contestCollection = Manager.getContestCollection();
  private final PropertyChangeListener listener = new PropertyChangeListener() {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      Contest j = thisContest();
      if (j != null) {
        refreshView(j);
      }

    }
  };

  public ContestTopComponent() {
    initComponents();
    setName(Bundle.CTL_ContestTopComponent());
    //setToolTipText(Bundle.HINT_ContestTopComponent());

  }

  ContestTopComponent(Integer key) {
    this();
    viewContestRecord(key);

  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    edContestId = new javax.swing.JLabel();
    edDescription = new javax.swing.JTextField();
    jLabel2 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    allocationTable = new de.free_creations.editors.contest.AllocationTable();
    jScrollPane2 = new javax.swing.JScrollPane();
    timeTable1 = new de.free_creations.editors.contest.TimeTable();
    jLabel1 = new javax.swing.JLabel();
    edName = new javax.swing.JTextField();
    contestTypeComboBox1 = new de.free_creations.editors.contest.ContestTypeComboBox();
    edContestType = new de.free_creations.editors.contest.ContestTypeComboBox();

    org.openide.awt.Mnemonics.setLocalizedText(edContestId, org.openide.util.NbBundle.getMessage(ContestTopComponent.class, "ContestTopComponent.edContestId.text")); // NOI18N

    edDescription.setText(org.openide.util.NbBundle.getMessage(ContestTopComponent.class, "ContestTopComponent.edDescription.text")); // NOI18N
    edDescription.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        edDescriptionFocusLost(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ContestTopComponent.class, "ContestTopComponent.jLabel2.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(ContestTopComponent.class, "ContestTopComponent.jLabel4.text")); // NOI18N

    jScrollPane1.setViewportView(allocationTable);

    jScrollPane2.setViewportView(timeTable1);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ContestTopComponent.class, "ContestTopComponent.jLabel1.text")); // NOI18N

    edName.setText(org.openide.util.NbBundle.getMessage(ContestTopComponent.class, "ContestTopComponent.edName.text")); // NOI18N
    edName.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        edNameFocusLost(evt);
      }
    });

    edContestType.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        edContestTypeActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(edContestId, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
              .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel1)
                  .addComponent(jLabel4)
                  .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(edDescription)
                  .addComponent(edName)
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(edContestType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))))
            .addContainerGap())))
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addGap(0, 0, Short.MAX_VALUE)
          .addComponent(contestTypeComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGap(0, 0, Short.MAX_VALUE)))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(edContestId)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(edContestType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(edName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(edDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel4))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(68, Short.MAX_VALUE))
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addGap(0, 0, Short.MAX_VALUE)
          .addComponent(contestTypeComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGap(0, 0, Short.MAX_VALUE)))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void edDescriptionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edDescriptionFocusLost
    Contest c = thisContest();
    if (c != null) {
      String oldS = noNull(c.getDescription());
      String newS = edDescription.getText();
      if (!oldS.equals(newS)) {
        c.setDescription(newS);
      }
    }
  }//GEN-LAST:event_edDescriptionFocusLost

  private void edNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edNameFocusLost
    Contest c = thisContest();
    if (c != null) {
      String oldS = noNull(c.getName());
      String newS = edName.getText();
      if (!oldS.equals(newS)) {
        c.setName(newS);
      }
    }
  }//GEN-LAST:event_edNameFocusLost

  private void edContestTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edContestTypeActionPerformed
    Contest c = thisContest();
    if (c != null) {
      ContestType oldCt = c.getContestType();
      ContestType newCt = edContestType.getSelectedContestType();
      if (!Objects.equals(oldCt, newCt)) {
        c.setContestType(newCt);
      }
    }
  }//GEN-LAST:event_edContestTypeActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private de.free_creations.editors.contest.AllocationTable allocationTable;
  private de.free_creations.editors.contest.ContestTypeComboBox contestTypeComboBox1;
  private javax.swing.JLabel edContestId;
  private de.free_creations.editors.contest.ContestTypeComboBox edContestType;
  private javax.swing.JTextField edDescription;
  private javax.swing.JTextField edName;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private de.free_creations.editors.contest.TimeTable timeTable1;
  // End of variables declaration//GEN-END:variables

  @Override
  public void componentOpened() {
    // TODO add custom code on component opening
  }

  @Override
  public void componentClosed() {
    // TODO add custom code on component closing
  }

  void writeProperties(java.util.Properties p) {
    // better to version settings since initial version as advocated at
    // http://wiki.apidesign.org/wiki/PropertyFiles
    p.setProperty("version", "1.0");
    // TODO store your settings
  }

  void readProperties(java.util.Properties p) {
    String version = p.getProperty("version");
    // TODO read your settings according to their version
  }

  void viewContestRecord(Integer newKey) {
    if (!Objects.equals(currentKey, newKey)) {
      Contest.removePropertyChangeListener(listener, currentKey);
      currentKey = newKey;

      Contest newContest = thisContest();
      if (newContest != null) {
        Contest.addPropertyChangeListener(listener, newKey);
        refreshView(newContest);
      }
      edContestId.setText(String.format("%s", newKey));
      allocationTable.setJuryId(newKey);
    }
  }

  private Contest thisContest() {
    Contest j = null;
    try {
      j = contestCollection.findEntity(currentKey);
    } catch (DataBaseNotReadyException ex) {
      Exceptions.printStackTrace(ex);
    }
    return j;
  }

  private void refreshView(Contest contest) {
    assert (contest != null);
    String name = noNull(contest.getName()).trim();
    if (name.length() > 0) {
      setDisplayName(name);
    } else {
      setDisplayName(String.format("Contest[%s]", currentKey));
    }

    edContestType.setSelectedContestType(contest.getContestType());
    edContestId.setText(String.format("%s", currentKey));
    edName.setText(name);
    edDescription.setText(noNull(contest.getDescription()));

  }

  private String noNull(String s) {
    if (s == null) {
      return "";
    } else {
      return s;
    }
  }
}
