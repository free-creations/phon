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
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class FunctionsComboBox extends JComboBox<Job> {

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
          setSelectedItem(null);
      }
    }
  };

  public FunctionsComboBox() {
    super();
    FunctionsComboBoxModel model = new FunctionsComboBoxModel();
    setModel(model);

  }

  public Job getSelectedItemFromPersistency() {
    return refreshItem(getSelectedItem());
  }

  /**
   * make sure the returned item is in the current persistency context.
   *
   * @param anItem
   * @return
   */
  private Job refreshItem(Object anItem) {
    if (anItem instanceof Job) {
      Job mayBeZomby = (Job) anItem;
      try {
        return Manager.getJobCollection().findEntity(mayBeZomby.getFunktionid());
      } catch (DataBaseNotReadyException ex) {
        return null;
      }
    }
    return null;
  }

  public class FunctionsComboBoxModel extends DefaultComboBoxModel<Job> {

    public FunctionsComboBoxModel() {
      super();
      if (java.beans.Beans.isDesignTime()) {
        Job f0 = new Job("LEHRER", "LehrkraftXX", 1);
        addElement(f0);
        FunctionsComboBoxModel.this.setSelectedItem(f0);

      } else {
        List<Job> ff = Manager.getJobCollection().getAll();
        for (Job f : ff) {
          addElement(f);
        }
        addElement(null);
        addKeyListener(keyListener);
      }
    }
  }
}
