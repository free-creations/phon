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
package de.free_creations.editors.location;

import de.free_creations.dbEntities.Contest;
import de.free_creations.nbPhon4Netbeans.ContestNode;
import de.free_creations.nbPhon4Netbeans.ContestRootNode;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.ContestCollection;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.TransferHandler;
import org.openide.explorer.view.ChoiceView;
import org.openide.explorer.view.NodeListModel;

/**
 * This combo box appears when the user edits a cell in the {@link TimeTable}.
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class TimeTableContestComboBox extends ChoiceView {

  private final ContestRootNode contestRootNode;
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
                  setSelectedContestId(null);
              }
            }
          };
  private final TransferHandler transferHandler = new TransferHandler() {
    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
      if (support.isDataFlavorSupported(ContestNode.CONTEST_NODE_FLAVOR)) {
        return true;
      } else {
        return false;
      }
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
      try {
        Integer key = (Integer) support.getTransferable().getTransferData(ContestNode.CONTEST_NODE_FLAVOR);
        setSelectedContestId(key);
        return true;
      } catch (ClassCastException | UnsupportedFlavorException | IOException ex) {
        return false;
      }
    }
  };

  public TimeTableContestComboBox() {
    super();

    if (!java.beans.Beans.isDesignTime()) {
      if (!Manager.isOpen()) {
        contestRootNode = null;
        return;
      }
      ContestCollection pp = Manager.getContestCollection();
      contestRootNode = new ContestRootNode(pp, true, false);
      NodeListModel nodeListModel = new NodeListModel(contestRootNode);
      setModel(nodeListModel);
      setSelectedContestId(null);
    } else {
      contestRootNode = null;
    }
    setTransferHandler(transferHandler);
    addKeyListener(keyListener);
  }

  /**
   * Returns the contest id of the selected item. Returns null if no item is
   * selected or if the nobody item is selected.
   *
   * @return
   */
  public Integer getSelectedContestId() {
    if (contestRootNode == null) {
      return null;
    }
    int selectedIndex = getSelectedIndex();

    Integer key = contestRootNode.getNodeKeyAt(selectedIndex);
//    if (key == ContestNode.nullKey) {
//      return null;
//    }

    return key;

  }

  public final void setSelectedContestId(Integer contestId) {
    if (contestRootNode == null) {
      return;
    }
//    int key = ContestNode.contestIdToKey(contestId);
    int selectedIndex = contestRootNode.findIndexForNode(contestId);
    if (selectedIndex == -1) {
      setSelectedIndex(contestRootNode.findIndexForNode(null));
    } else {
      setSelectedIndex(selectedIndex);
    }
  }

  public void setSelectedContest(Contest p) {
    if (p == null) {
      setSelectedContestId(null);
    } else {
      setSelectedContestId(p.getContestId());
    }
  }
}
