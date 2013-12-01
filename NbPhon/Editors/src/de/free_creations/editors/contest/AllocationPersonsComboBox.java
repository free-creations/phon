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

import de.free_creations.dbEntities.Person;
import de.free_creations.nbPhon4Netbeans.PersonNode;
import de.free_creations.nbPhon4Netbeans.PersonsRootNode;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.PersonCollection;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.TransferHandler;
import org.openide.explorer.view.ChoiceView;
import org.openide.explorer.view.NodeListModel;

/**
 * This combo box appears when the user edits a cell in the 
 * {@link AllocationTable}.
 * 
 * @author Harald Postner <Harald at free-creations.de>
 */
public class AllocationPersonsComboBox extends ChoiceView {

  private final PersonsRootNode personsRootNode;
  private final KeyListener keyListener =
          new KeyAdapter() {
    private final static char deleteChar = '\u007F';
    private final static char backspaceChar = '\u0008';

    @Override
    public void keyTyped(KeyEvent evt) {
      char c = evt.getKeyChar();
      switch (c) {
        case backspaceChar:
        case deleteChar:
          setSelectedPersonId(null);
      }
    }
  };
  private final TransferHandler transferHandler = new TransferHandler() {
    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
      if (support.isDataFlavorSupported(PersonNode.PERSON_NODE_FLAVOR)) {
        return true;
      } else {
        return false;
      }
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
      try {
        Integer key = (Integer) support.getTransferable().getTransferData(PersonNode.PERSON_NODE_FLAVOR);
        setSelectedPersonId(key);
        return true;
      } catch (ClassCastException | UnsupportedFlavorException | IOException ex) {
        return false;
      }
    }
  };

  @SuppressWarnings("unchecked")
  public AllocationPersonsComboBox() {
    super();

    if (!java.beans.Beans.isDesignTime()) {
      if (!Manager.isOpen()) {
        personsRootNode = null;
        return;
      }
      PersonCollection pp = Manager.getPersonCollection();
      personsRootNode = new PersonsRootNode(pp, true);
      NodeListModel nodeListModel = new NodeListModel(personsRootNode);
      setModel(nodeListModel);
    } else {
      personsRootNode = null;
    }
    setTransferHandler(transferHandler);
    addKeyListener(keyListener);
  }

  /**
   * Returns the person id of the selected item. Returns null if no item is
   * selected or if the nobody item is selected.
   *
   * @return
   */
  public Integer getSelectedPersonId() {
    if (personsRootNode == null) {
      return null;
    }
    int selectedIndex = getSelectedIndex();

    int key = personsRootNode.getNodeKeyAt(selectedIndex);
    if (key == PersonNode.nullKey) {
      return null;
    }
    if (key == -1) {
      return null;
    }
    return key;

  }

  public void setSelectedPersonId(Integer personId) {
    if (personsRootNode == null) {
      return;
    }
    int key = PersonNode.personIdToKey(personId);
    int selectedIndex = personsRootNode.findIndexForNode(key);
    setSelectedIndex(selectedIndex);
  }

  public void setSelectedPerson(Person p) {
    if (p == null) {
      setSelectedPersonId(null);
    } else {
      setSelectedPersonId(p.getPersonId());
    }
  }
}
