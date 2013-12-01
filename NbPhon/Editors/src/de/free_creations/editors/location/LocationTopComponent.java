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
package de.free_creations.editors.location;

import de.free_creations.dbEntities.Location;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.LocationCollection;
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
        dtd = "-//de.free_creations.editors.location//Location//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "LocationTopComponent",
        iconBase = "de/free_creations/editors/location/house.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "de.free_creations.editors.location.LocationTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_LocationAction",
        preferredID = "LocationTopComponent"
)
@Messages({
  "CTL_LocationAction=Location",
  "CTL_LocationTopComponent=Location Window",
  "HINT_LocationTopComponent=Shows the details of a Location"
})
public final class LocationTopComponent extends CloneableTopComponent {

  private Integer currentKey = null;
  private final LocationCollection locationCollection = Manager.getLocationCollection();
  private final PropertyChangeListener listener = new PropertyChangeListener() {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      Location p = thisLocation();
      if (p != null) {
        refreshView(p);
      }

    }
  };

  public LocationTopComponent() {
    initComponents();
    setName(Bundle.CTL_LocationTopComponent());
    //setToolTipText(Bundle.HINT_LocationTopComponent());
  }

  LocationTopComponent(Integer key) {
    this();
    viewLocationRecord(key);
   }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    jPanel1 = new javax.swing.JPanel();
    edLocationId = new javax.swing.JLabel();
    lblName = new javax.swing.JLabel();
    edName = new javax.swing.JTextField();
    lblRoom = new javax.swing.JLabel();
    edRoom = new javax.swing.JTextField();
    lblBuilding = new javax.swing.JLabel();
    edBuilding = new javax.swing.JTextField();
    lblStreet = new javax.swing.JLabel();
    edStreet = new javax.swing.JTextField();
    lblTown = new javax.swing.JLabel();
    edTown = new javax.swing.JTextField();
    lblGridnumber = new javax.swing.JLabel();
    edGridnumber = new javax.swing.JTextField();

    org.openide.awt.Mnemonics.setLocalizedText(edLocationId, org.openide.util.NbBundle.getMessage(LocationTopComponent.class, "LocationTopComponent.edLocationId.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(lblName, org.openide.util.NbBundle.getMessage(LocationTopComponent.class, "LocationTopComponent.lblName.text")); // NOI18N

    edName.setText(org.openide.util.NbBundle.getMessage(LocationTopComponent.class, "LocationTopComponent.edName.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(lblRoom, org.openide.util.NbBundle.getMessage(LocationTopComponent.class, "LocationTopComponent.lblRoom.text")); // NOI18N

    edRoom.setText(org.openide.util.NbBundle.getMessage(LocationTopComponent.class, "LocationTopComponent.edRoom.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(lblBuilding, org.openide.util.NbBundle.getMessage(LocationTopComponent.class, "LocationTopComponent.lblBuilding.text")); // NOI18N

    edBuilding.setText(org.openide.util.NbBundle.getMessage(LocationTopComponent.class, "LocationTopComponent.edBuilding.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(lblStreet, org.openide.util.NbBundle.getMessage(LocationTopComponent.class, "LocationTopComponent.lblStreet.text")); // NOI18N

    edStreet.setText(org.openide.util.NbBundle.getMessage(LocationTopComponent.class, "LocationTopComponent.edStreet.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(lblTown, org.openide.util.NbBundle.getMessage(LocationTopComponent.class, "LocationTopComponent.lblTown.text")); // NOI18N

    edTown.setText(org.openide.util.NbBundle.getMessage(LocationTopComponent.class, "LocationTopComponent.edTown.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(lblGridnumber, org.openide.util.NbBundle.getMessage(LocationTopComponent.class, "LocationTopComponent.lblGridnumber.text")); // NOI18N

    edGridnumber.setText(org.openide.util.NbBundle.getMessage(LocationTopComponent.class, "LocationTopComponent.edGridnumber.text")); // NOI18N

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(lblBuilding)
          .addComponent(lblName)
          .addComponent(lblRoom)
          .addComponent(lblTown)
          .addComponent(lblGridnumber)
          .addComponent(lblStreet))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(edName)
          .addComponent(edRoom)
          .addComponent(edBuilding)
          .addComponent(edStreet)
          .addComponent(edTown)
          .addComponent(edGridnumber))
        .addContainerGap())
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(12, 12, 12)
        .addComponent(edLocationId, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(423, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(edLocationId)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblName)
          .addComponent(edName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblRoom)
          .addComponent(edRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblBuilding)
          .addComponent(edBuilding, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblStreet)
          .addComponent(edStreet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblTown)
          .addComponent(edTown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblGridnumber)
          .addComponent(edGridnumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(303, Short.MAX_VALUE))
    );

    jScrollPane1.setViewportView(jPanel1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane1)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
    );
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField edBuilding;
  private javax.swing.JTextField edGridnumber;
  private javax.swing.JLabel edLocationId;
  private javax.swing.JTextField edName;
  private javax.swing.JTextField edRoom;
  private javax.swing.JTextField edStreet;
  private javax.swing.JTextField edTown;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel lblBuilding;
  private javax.swing.JLabel lblGridnumber;
  private javax.swing.JLabel lblName;
  private javax.swing.JLabel lblRoom;
  private javax.swing.JLabel lblStreet;
  private javax.swing.JLabel lblTown;
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

  private Location thisLocation() {
    Location l = null;
    try {
      l = locationCollection.findEntity(currentKey);
    } catch (DataBaseNotReadyException ex) {
      Exceptions.printStackTrace(ex);
    }
    return l;
  }
   public void viewLocationRecord(Integer newKey) {
    if (!Objects.equals(currentKey, newKey)) {
      Location.removePropertyChangeListener(listener, currentKey);
      currentKey = newKey;

      Location newLocation = thisLocation();
      if (newLocation != null) {
        Location.addPropertyChangeListener(listener, newKey);
        refreshView(newLocation);
      }
    }
  }

  private void refreshView(Location location) {
    assert (location != null);
    String displayName = noNull(location.getName()).trim();
    if (displayName.length() > 0) {
      setDisplayName(displayName);
    } else {
      setDisplayName(String.format("Location[%s]", currentKey));
    }
    edLocationId.setText(String.format("%s", currentKey));
    edBuilding.setText(noNull(location.getBuilding()));
    edGridnumber.setText(noNull(location.getGridnumber()));
    edName.setText(noNull(location.getName()));
    edRoom.setText(noNull(location.getRoom()));
    edStreet.setText(noNull(location.getStreet()));
    edTown.setText(noNull(location.getTown()));
  }

  private String noNull(String s) {
    if (s == null) {
      return "";
    } else {
      return s;
    }
  }
}
