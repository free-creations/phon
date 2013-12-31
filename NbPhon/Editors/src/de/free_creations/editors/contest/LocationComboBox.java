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


import de.free_creations.dbEntities.Location;
import de.free_creations.nbPhon4Netbeans.LocationNode;
import de.free_creations.nbPhon4Netbeans.LocationRootNode;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.LocationCollection;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.TransferHandler;
import org.openide.explorer.view.ChoiceView;
import org.openide.explorer.view.NodeListModel;

/**
 * A combo box that displays all locations.
 * 
 * This combo-box is used in the contest editor window (ContestTopComponent)
 * Within this window in the timeTable. Within the timeTable
 * in the TimeTableCellEditorPanel.
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class LocationComboBox extends ChoiceView {

  private final LocationRootNode locationRootNode;
  private final KeyListener keyListener
          = new KeyAdapter() {
            private final static char deleteChar = '\u007F';
            private final static char backspaceChar = '\u0008';

            @Override
            public void keyTyped(KeyEvent evt) {
              char c = evt.getKeyChar();
              switch (c) {
                case backspaceChar:
                case deleteChar:
                  setSelectedLocationId(null);
              }
            }
          };
  private final TransferHandler transferHandler = new TransferHandler() {
    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
      if (support.isDataFlavorSupported(LocationNode.LOCATION_NODE_FLAVOR)) {
        return true;
      } else {
        return false;
      }
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
      try {
        Integer key = (Integer) support.getTransferable().getTransferData(LocationNode.LOCATION_NODE_FLAVOR);
        setSelectedLocationId(key);
        return true;
      } catch (ClassCastException | UnsupportedFlavorException | IOException ex) {
        return false;
      }
    }
  };

  public LocationComboBox() {
    super();

    if (!java.beans.Beans.isDesignTime()) {
      if (!Manager.isOpen()) {
        locationRootNode = null;
        return;
      }
      LocationCollection pp = Manager.getLocationCollection();
      locationRootNode = new LocationRootNode(pp, true);
      NodeListModel nodeListModel = new NodeListModel(locationRootNode);
      setModel(nodeListModel);
      setSelectedLocationId(null);
    } else {
      locationRootNode = null; // design time.
    }
    setTransferHandler(transferHandler);
    addKeyListener(keyListener);
  }

  /**
   * Returns the location id of the selected item. Returns null if no item is
   * selected or if the nobody item is selected.
   *
   * @return
   */
  public Integer getSelectedLocationId() {
    if (locationRootNode == null) {
      return null;
    }
    int selectedIndex = getSelectedIndex();

    Integer key = locationRootNode.getNodeKeyAt(selectedIndex);
//    if (key == LocationNode.nullKey) {
//      return null;
//    }

    return key;

  }

  public final void setSelectedLocationId(Integer locationId) {
    if (locationRootNode == null) {
      return;
    }
//    int key = LocationNode.locationIdToKey(locationId);
    int selectedIndex = locationRootNode.findIndexForNode(locationId);
    if (selectedIndex == -1) {
      setSelectedIndex(locationRootNode.findIndexForNode(null));
    } else {
      setSelectedIndex(selectedIndex);
    }
  }

  public void setSelectedLocation(Location p) {
    if (p == null) {
      setSelectedLocationId(null);
    } else {
      setSelectedLocationId(p.getLocationId());
    }
  }
}
