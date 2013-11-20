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
package de.free_creations.editors.person;

import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Personen;
import de.free_creations.actions.CheckedAction;
import static de.free_creations.actions.CheckedAction.Severity.*;
import static de.free_creations.actions.person.SetGroupleaderRule.setGroupleader;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.PersonCollection;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.CloneableTopComponent;

/**
 * A Window which permits to edit a PERSONEN record.
 */
@ConvertAsProperties(
        dtd = "-//de.free_creations.editors//Person//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "PersonTopComponent",
        iconBase = "de/free_creations/editors/resources/address.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "de.free_creations.editors.person.PersonTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_PersonAction",
        preferredID = "PersonTopComponent")
@Messages({
  "CTL_PersonAction=Address",
  "CTL_PersonTopComponent=Address",
  "HINT_PersonTopComponent=This is a Person window"
})
@SuppressWarnings({"rawtypes", "unchecked"})
public final class PersonTopComponent extends CloneableTopComponent {

  private Integer currentKey = null;
  private final PersonCollection personCollection = Manager.getPersonCollection();
  private final PropertyChangeListener listener = new PropertyChangeListener() {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      Personen p = thisPerson();
      if (p != null) {
        refreshView(p);
      }

    }
  };

  public PersonTopComponent() {
    initComponents();
    setName(Bundle.CTL_PersonTopComponent());
  }

  PersonTopComponent(Integer key) {
    this();
    viewPersonRecord(key);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  //@SuppressWarnings({})
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    PersonId = new javax.swing.JTextField();
    edAlter = new javax.swing.JComboBox();
    edHerrFrau = new javax.swing.JComboBox();
    edNachname = new javax.swing.JTextField();
    edVorname = new javax.swing.JTextField();
    edPlz = new javax.swing.JTextField();
    edWohnort = new javax.swing.JTextField();
    edStrasse = new javax.swing.JTextField();
    edFestnetz = new javax.swing.JTextField();
    edHandy = new javax.swing.JTextField();
    edEMail = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    jLabel8 = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();
    jPanel1 = new javax.swing.JPanel();
    jLabel10 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel9 = new javax.swing.JLabel();
    jScrollPane3 = new javax.swing.JScrollPane();
    timeTable = new de.free_creations.editors.person.TimeTable();
    edFunction = new de.free_creations.editors.person.FunctionsComboBox();
    edWertung = new de.free_creations.editors.person.JuryComboBox();
    jPanel2 = new javax.swing.JPanel();
    edKollege = new de.free_creations.editors.person.PersonsComboBox();
    pnlGroupMembers = new de.free_creations.editors.person.PersonGroupPanel();
    jLabel11 = new javax.swing.JLabel();
    jScrollPane2 = new javax.swing.JScrollPane();
    edNotiz = new javax.swing.JTextArea();
    jLabel12 = new javax.swing.JLabel();
    jLabel13 = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    assignmentTable = new de.free_creations.editors.person.PersonAssignmentTable();

    PersonId.setEditable(false);
    PersonId.setText(org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.PersonId.text")); // NOI18N

    edAlter.setEditable(true);
    edAlter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "KIND", "JUGENDLICH", "ERWACHSEN" }));

    edHerrFrau.setEditable(true);
    edHerrFrau.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hr.", "Fr." }));

    edNachname.setText(org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.edNachname.text")); // NOI18N
    edNachname.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        edNachnameFocusLost(evt);
      }
    });

    edVorname.setText(org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.edVorname.text")); // NOI18N
    edVorname.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        edVornameFocusLost(evt);
      }
    });

    edPlz.setText(org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.edPlz.text")); // NOI18N
    edPlz.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        edPlzFocusLost(evt);
      }
    });

    edWohnort.setText(org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.edWohnort.text")); // NOI18N
    edWohnort.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        edWohnortFocusLost(evt);
      }
    });

    edStrasse.setText(org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.edStrasse.text")); // NOI18N
    edStrasse.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        edStrasseFocusLost(evt);
      }
    });

    edFestnetz.setText(org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.edFestnetz.text")); // NOI18N
    edFestnetz.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        edFestnetzFocusLost(evt);
      }
    });

    edHandy.setText(org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.edHandy.text")); // NOI18N
    edHandy.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        edHandyFocusLost(evt);
      }
    });

    edEMail.setText(org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.edEMail.text")); // NOI18N
    edEMail.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        edEMailFocusLost(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jLabel4.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jLabel5.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jLabel2.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jLabel6.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jLabel7.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jLabel8.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jLabel1.text")); // NOI18N

    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jPanel1.border.title"))); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jLabel10.text")); // NOI18N
    jLabel10.setPreferredSize(new java.awt.Dimension(70, 15));

    org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jLabel3.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jLabel9.text")); // NOI18N
    jLabel9.setPreferredSize(new java.awt.Dimension(70, 15));

    jScrollPane3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jScrollPane3.setViewportView(timeTable);

    edFunction.setMinimumSize(new java.awt.Dimension(112, 26));
    edFunction.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        edFunctionActionPerformed(evt);
      }
    });

    edWertung.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        edWertungActionPerformed(evt);
      }
    });

    jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    edKollege.setMinimumSize(new java.awt.Dimension(32, 26));
    edKollege.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        edKollegeActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addGap(0, 0, 0)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(edKollege, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(pnlGroupMembers, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGap(0, 0, 0))
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addGap(0, 0, 0)
        .addComponent(edKollege, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, 0)
        .addComponent(pnlGroupMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
    );

    org.openide.awt.Mnemonics.setLocalizedText(jLabel11, org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jLabel11.text")); // NOI18N

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane3)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel3)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel11))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
              .addComponent(edFunction, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(edWertung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel3)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel11))
        .addGap(0, 0, 0)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(edFunction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(edWertung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 106, Short.MAX_VALUE))
          .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );

    edNotiz.setColumns(20);
    edNotiz.setRows(5);
    jScrollPane2.setViewportView(edNotiz);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel12, org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jLabel12.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel13, org.openide.util.NbBundle.getMessage(PersonTopComponent.class, "PersonTopComponent.jLabel13.text")); // NOI18N

    jScrollPane1.setViewportView(assignmentTable);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(PersonId, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(121, 121, 121)
            .addComponent(edAlter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jLabel12)
          .addComponent(jScrollPane1)
          .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel7)
                  .addComponent(edPlz, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(edFestnetz, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel8)
                  .addComponent(edHandy)))
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(edHerrFrau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel5)
                  .addComponent(jLabel1)
                  .addComponent(edNachname)
                  .addComponent(edWohnort))))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(edEMail)
              .addComponent(edStrasse)
              .addComponent(edVorname, javax.swing.GroupLayout.Alignment.TRAILING)
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel13)
                  .addComponent(jLabel6)
                  .addComponent(jLabel2))
                .addGap(0, 0, Short.MAX_VALUE)))))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(PersonId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(edAlter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel2)
          .addComponent(jLabel1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(edNachname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(edHerrFrau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(edVorname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel4)
          .addComponent(jLabel5)
          .addComponent(jLabel6))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(edPlz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(edWohnort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(edStrasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel7)
          .addComponent(jLabel8)
          .addComponent(jLabel13))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(edFestnetz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(edHandy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(edEMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel12)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void edNachnameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edNachnameFocusLost
    Personen p = thisPerson();
    if (p != null) {
      String oldS = noNull(p.getFamilienname());
      String newS = edNachname.getText();
      if (!oldS.equals(newS)) {
        p.setFamilienname(newS);
      }
    }
  }//GEN-LAST:event_edNachnameFocusLost

  private void edVornameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edVornameFocusLost
    Personen p = thisPerson();
    if (p != null) {
      String oldS = noNull(p.getVorname());
      String newS = edVorname.getText();
      if (!oldS.equals(newS)) {
        p.setVorname(newS);
      }
    }
  }//GEN-LAST:event_edVornameFocusLost

  private void edPlzFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edPlzFocusLost
    Personen p = thisPerson();
    if (p != null) {
      String oldS = noNull(p.getPlz());
      String newS = edPlz.getText();
      if (!oldS.equals(newS)) {
        p.setPlz(newS);
      }
    }
  }//GEN-LAST:event_edPlzFocusLost

  private void edWohnortFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edWohnortFocusLost
    Personen p = thisPerson();
    if (p != null) {
      String oldS = noNull(p.getOrt());
      String newS = edWohnort.getText();
      if (!oldS.equals(newS)) {
        p.setOrt(newS);
      }
    }
  }//GEN-LAST:event_edWohnortFocusLost

  private void edStrasseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edStrasseFocusLost
    Personen p = thisPerson();
    if (p != null) {
      String oldS = noNull(p.getStrasse());
      String newS = edStrasse.getText();
      if (!oldS.equals(newS)) {
        p.setStrasse(newS);
      }
    }
  }//GEN-LAST:event_edStrasseFocusLost

  private void edFestnetzFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edFestnetzFocusLost
    Personen p = thisPerson();
    if (p != null) {
      String oldS = noNull(p.getTelnr());
      String newS = edFestnetz.getText();
      if (!oldS.equals(newS)) {
        p.setTelnr(newS);
      }
    }
  }//GEN-LAST:event_edFestnetzFocusLost

  private void edHandyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edHandyFocusLost
    Personen p = thisPerson();
    if (p != null) {
      String oldS = noNull(p.getHandy());
      String newS = edHandy.getText();
      if (!oldS.equals(newS)) {
        p.setHandy(newS);
      }
    }
  }//GEN-LAST:event_edHandyFocusLost

  private void edEMailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_edEMailFocusLost
    Personen p = thisPerson();
    if (p != null) {
      String oldS = noNull(p.getEmail());
      String newS = edEMail.getText();
      if (!oldS.equals(newS)) {
        p.setEmail(newS);
      }
    }
  }//GEN-LAST:event_edEMailFocusLost

  private void edFunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edFunctionActionPerformed
    Personen p = thisPerson();
    if (p != null) {
      Job oldF = p.getGewuenschtefunktion();
      Job newF = edFunction.getSelectedItemFromPersistency();
      if (!Objects.equals(oldF, newF)) {
        p.setGewuenschtefunktion(newF);
      }
    }

  }//GEN-LAST:event_edFunctionActionPerformed

  private void edKollegeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edKollegeActionPerformed

    Personen p = thisPerson();
    if (p != null) {
      Integer selectedKey = edKollege.getSelectedPersonId();
      try {
        Personen newC = personCollection.findEntity(selectedKey);
        Personen oldC = p.getGewuenschterkollege();
        if (!Objects.equals(oldC, newC)) {
          CheckedAction setGroupleaderRule = setGroupleader(p.getPersonid(), selectedKey);
          switch (setGroupleaderRule.level()) {
            case ok: {
              setGroupleaderRule.apply();
              return;
            }
            case recoverable: {
              NotifyDescriptor.Confirmation message = new NotifyDescriptor.Confirmation(
                      setGroupleaderRule.problemDescription() + "!\n\n"
                      + setGroupleaderRule.proposedSolution() + "?",
                      NotifyDescriptor.OK_CANCEL_OPTION,
                      NotifyDescriptor.WARNING_MESSAGE);
              Object reply = DialogDisplayer.getDefault().notify(message);
              if (NotifyDescriptor.OK_OPTION.equals(reply)) {
                setGroupleaderRule.apply();
              }
              edKollege.setSelectedPerson(p.getGewuenschterkollege());
              return;
            }
            case irrecoverable: {
              NotifyDescriptor.Confirmation message = new NotifyDescriptor.Confirmation(
                      setGroupleaderRule.problemDescription() + "!\n\n"
                      + setGroupleaderRule.proposedSolution() + "?",
                      NotifyDescriptor.OK_CANCEL_OPTION,
                      NotifyDescriptor.ERROR_MESSAGE);
              Object reply = DialogDisplayer.getDefault().notify(message);
              if (NotifyDescriptor.OK_OPTION.equals(reply)) {
                setGroupleaderRule.apply();
              }
              edKollege.setSelectedPerson(p.getGewuenschterkollege());
              return;

            }
          }
        }
      } catch (DataBaseNotReadyException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  }//GEN-LAST:event_edKollegeActionPerformed

  private void edWertungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edWertungActionPerformed
    Personen p = thisPerson();
    if (p != null) {
      String oldW = p.getGewuenschtewertung();
      String newW = toString(edWertung.getSelectedItem());
      if (!Objects.equals(oldW, newW)) {
        p.setGewuenschtewertung(newW);
      }
    }
  }//GEN-LAST:event_edWertungActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField PersonId;
  private de.free_creations.editors.person.PersonAssignmentTable assignmentTable;
  private javax.swing.JComboBox edAlter;
  private javax.swing.JTextField edEMail;
  private javax.swing.JTextField edFestnetz;
  private de.free_creations.editors.person.FunctionsComboBox edFunction;
  private javax.swing.JTextField edHandy;
  private javax.swing.JComboBox edHerrFrau;
  private de.free_creations.editors.person.PersonsComboBox edKollege;
  private javax.swing.JTextField edNachname;
  private javax.swing.JTextArea edNotiz;
  private javax.swing.JTextField edPlz;
  private javax.swing.JTextField edStrasse;
  private javax.swing.JTextField edVorname;
  private de.free_creations.editors.person.JuryComboBox edWertung;
  private javax.swing.JTextField edWohnort;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private de.free_creations.editors.person.PersonGroupPanel pnlGroupMembers;
  private de.free_creations.editors.person.TimeTable timeTable;
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

  public void viewPersonRecord(Integer newKey) {
    if (currentKey != newKey) {

      Personen.removePropertyChangeListener(listener, currentKey);
      currentKey = newKey;
      Personen person = thisPerson();

      if (person != null) {
        Personen.addPropertyChangeListener(listener, newKey);
        refreshView(person);
      }
      timeTable.setPersonId(newKey);
      assignmentTable.setPersonId(newKey);
    }
  }

  private Personen thisPerson() {
    Personen p = null;
    try {
      p = personCollection.findEntity(currentKey);
    } catch (DataBaseNotReadyException ex) {
      Exceptions.printStackTrace(ex);
    }
    return p;
  }

  private void refreshView(Personen person) {
    setDisplayName(String.format("%s, %s", person.getFamilienname(), person.getVorname()));
    PersonId.setText(noNull(person.getPersonid()));
    edAlter.setSelectedItem(noNull(person.getAltersgruppe()));
    edEMail.setText(noNull(person.getEmail()));
    edFestnetz.setText(noNull(person.getTelnr()));
    edFunction.setSelectedItem(person.getGewuenschtefunktion());
    edHandy.setText(noNull(person.getHandy()));
    edHerrFrau.setSelectedItem(noNull(person.getHerrfrau()));
    edKollege.setSelectedPerson(person.getGewuenschterkollege());
    edNachname.setText(noNull(person.getFamilienname()));
    edNotiz.setText(noNull(person.getNotiz()));
    edPlz.setText(noNull(person.getPlz()));
    edStrasse.setText(noNull(person.getStrasse()));
    edVorname.setText(noNull(person.getVorname()));
    edWertung.setSelectedItem(person.getGewuenschtewertung());
    edWohnort.setText(noNull(person.getOrt()));
    pnlGroupMembers.setGroupleader(findTeamleaderId(person));
  }

  private Integer findTeamleaderId(Personen person) {
    if (person == null) {
      return null;
    }
    Personen p = person.getGewuenschterkollege();
    if (p == null) {
      return null;
    }
    return p.getPersonid();

  }

  private String noNull(Integer i) {
    if (i == null) {
      return "";
    } else {
      return i.toString();
    }
  }

  private String noNull(String s) {
    if (s == null) {
      return "";
    } else {
      return s;
    }
  }

  /**
   * A toString() function that preserves null values.
   *
   * @param o
   * @return
   */
  private String toString(Object o) {
    if (o == null) {
      return null;
    } else {
      return o.toString();
    }
  }
}
