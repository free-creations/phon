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

import de.free_creations.dbEntities.ContestType;
import de.free_creations.nbPhon4Netbeans.IconManager;
import de.free_creations.nbPhonAPI.ContestTypeCollection;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Objects;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import org.openide.util.Exceptions;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class ContestTypeComboBox extends JComboBox<ContestTypeItem> {

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
                  setSelectedItem(null);
              }
            }
          };

  public ContestTypeComboBox() {
    super();
    ContestTypeComboBoxModel model = new ContestTypeComboBoxModel();
    setModel(model);
    setRenderer(new ContestTypeComboBoxRenderer());
  }

  public String getSelectedContestTypeId() {
    Object selectedItem = getSelectedItem();
    if (selectedItem instanceof ContestTypeItem) {
      return ((ContestTypeItem) selectedItem).contestTypeId;
    } else {
      return null;
    }
  }

  public void setSelectedContestTypeId(String id) {
    if (id == null) {
      setSelectedItem(null);
      return;
    }
    for (int i = 0; i < getItemCount(); i++) {
      ContestTypeItem item = getItemAt(i);
      if (item != null) {
        if (id.equals(item.contestTypeId)) {
          setSelectedIndex(i);
          return;
        }
      }
    }
  }

  public void setSelectedContestType(ContestType ct) {
    if (ct == null) {
      setSelectedItem(null);
    } else {
      setSelectedContestTypeId(ct.getContestTypeId());
    }
  }

  public ContestType getSelectedContestType() {
    String s = getSelectedContestTypeId();
    if (s == null) {
      return null;
    } else {
      ContestTypeCollection contestTypeCollection = Manager.getContestTypeCollection();
      try {
        return contestTypeCollection.findEntity(s);
      } catch (DataBaseNotReadyException ex) {
        return null;
      }
    }
  }

  public class ContestTypeComboBoxRenderer extends BasicComboBoxRenderer {

    @Override
    public Component getListCellRendererComponent(JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
      Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      if (c instanceof JLabel) {
        JLabel l = (JLabel) c;
        if (value instanceof ContestTypeItem) {
          ContestTypeItem i = (ContestTypeItem) value;
          l.setText(i.name);
          l.setIcon(i.icon);
        }
        if (value == null) {
          l.setText(null);
          l.setIcon(null);
        }
      }
      return c;
    }

  }

  public class ContestTypeComboBoxModel extends DefaultComboBoxModel<ContestTypeItem> {

    public ContestTypeComboBoxModel() {
      super();
      addElement(null);
      if (java.beans.Beans.isDesignTime()) {
        ContestTypeItem l = new ContestTypeItem("ENSEMBLE", "Ensemble Playing", null);
        addElement(l);
        ContestTypeComboBoxModel.this.setSelectedItem(l);
      } else {
        ContestTypeCollection contestTypeCollection = Manager.getContestTypeCollection();
        List<ContestType> all = contestTypeCollection.getAll();
        for (ContestType ct : all) {
          ContestTypeItem i = new ContestTypeItem(
                  ct.getContestTypeId(),
                  ct.getName(),
                  IconManager.iconManager().image2icon(
                          IconManager.iconManager().getContestTypeImage(ct.getIcon())));
          addElement(i);
        }
        addKeyListener(keyListener);
      }
    }
  }
}

class ContestTypeItem {

  public final String contestTypeId;
  public final String name;
  public final Icon icon;

  public ContestTypeItem(String contestTypeId, String name, Icon icon) {
    this.contestTypeId = contestTypeId;
    this.name = name;
    this.icon = icon;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 53 * hash + Objects.hashCode(this.contestTypeId);
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
    final ContestTypeItem other = (ContestTypeItem) obj;
    if (!Objects.equals(this.contestTypeId, other.contestTypeId)) {
      return false;
    }
    return true;
  }

}
