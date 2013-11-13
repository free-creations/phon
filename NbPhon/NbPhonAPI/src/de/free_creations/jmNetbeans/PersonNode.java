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
package de.free_creations.jmNetbeans;

import de.free_creations.dbEntities.Funktionen;
import de.free_creations.dbEntities.Personen;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.cookies.EditCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;
import static de.free_creations.jmNetbeans.IconManager.*;
import java.awt.image.BufferedImage;

/**
 *
 * @see http://netbeans.dzone.com/nb-how-to-drag-drop-with-nodes-api for a DnD
 * example.
 * @author Harald Postner <Harald at free-creations.de>
 */
public class PersonNode extends AbstractNode implements CommittableNode {

  /**
   * The data flavor for a Drag and Drop action.
   */
  public static class PersonNodeFlavor extends DataFlavor {

    public PersonNodeFlavor() {
      super(PersonNode.class, "Person");
    }
  }

  /**
   * The transferable that is transfered in a Drag and Drop action.
   */
  public class PersonNodeTransferable extends ExTransferable.Single {

    private final Integer transferablePersonId;

    public PersonNodeTransferable(Integer personId) {
      super(PERSON_NODE_FLAVOR);
      this.transferablePersonId = personId;
    }

    /**
     * The person node transfers the primary key of the record it represents.
     *
     * @return
     */
    @Override
    protected Integer getData() {
      return transferablePersonId;
    }
  }
  public static final DataFlavor PERSON_NODE_FLAVOR = new PersonNodeFlavor();

  private boolean pendingChanges = false;
  private final int key;
  public final static int nullKey = Integer.MIN_VALUE;
  private final PropertyChangeListener listener = new PropertyChangeListener() {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      notifyPendingChanges();
      if (Personen.PROP_HERRFRAU.equals(evt.getPropertyName())) {
        fireDisplayNameChange(null, getDisplayName());
      }
      if (Personen.PROP_FAMILIENNAME.equals(evt.getPropertyName())) {
        fireDisplayNameChange(null, getDisplayName());
      }
      if (Personen.PROP_VORNAME.equals(evt.getPropertyName())) {
        fireDisplayNameChange(null, getDisplayName());
      }
      if (Personen.PROP_VERFUEGBARKEIT.equals(evt.getPropertyName())) {
        fireDisplayNameChange(null, getDisplayName());
        fireIconChange();
      }
      if (Personen.PROP_GEWUENSCHTEWERTUNG.equals(evt.getPropertyName())) {
        fireIconChange();
      }

    }
  };

  /**
   * Notify that this node has pending changes that need to be saved to the
   * database.
   */
  public void notifyPendingChanges() {
    pendingChanges = true;
    Committer.requestCommit(this);
    fireIconChange();
  }

  public static int personIdToKey(Integer personId) {
    if (personId == null) {
      return nullKey;
    } else {
      return personId;
    }
  }

  public static Integer keyToPersonId(int aKey) {
    if (aKey == nullKey) {
      return null;
    } else {
      return aKey;
    }
  }

  /**
   * Returns the internal key used to represent a person.
   *
   * This normally equals the Person id, but for a null person id it will return
   * Integer.minValue.
   *
   * @return
   */
  public int getKey() {
    return key;
  }

  /**
   * Returns the personId, will return null for the "nobody" node.
   *
   * @return
   */
  public int getPersonId() {
    return keyToPersonId(key);
  }
  private final MutableEntityCollection<Personen, Integer> personsManager;
  private final EditCookie editCookie = new EditCookie() {
    @Override
    public void edit() {
      editAction.actionPerformed(null);
    }
  };
  private final Action editAction = new AbstractAction("Edit") {
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        PersonEditorProvider provider =
                Lookup.getDefault().lookup(
                PersonEditorProvider.class);
        if (provider != null) {
          provider.getEditor(false, key);
        } else {
          throw new RuntimeException("No Editor provider found.");
        }
      } catch (Exception ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  };
  private final Action editNewWindowAction = new AbstractAction("Edit in new Window") {
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        PersonEditorProvider provider =
                Lookup.getDefault().lookup(
                PersonEditorProvider.class);
        if (provider != null) {
          provider.getEditor(true, key);
        } else {
          throw new RuntimeException("No Editor provider found.");
        }
      } catch (Exception ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  };
  private final Action[] allActions = new Action[]{editAction, editNewWindowAction};

  public PersonNode(Integer personId, MutableEntityCollection<Personen, Integer> personsManager) {
    super(Children.LEAF);
    assert (personsManager != null);
    this.key = personIdToKey(personId);
    this.personsManager = personsManager;
    if (key != nullKey) {
      Personen.addPropertyChangeListener(listener, personId);
      getCookieSet().add(editCookie);
    }
  }

  @Override
  public Action[] getActions(boolean context) {
    if (key != nullKey) {
      return allActions;
    } else {
      return null;
    }
  }

  @Override
  public Action getPreferredAction() {
    if (key != nullKey) {
      return editAction;
    } else {
      return null;
    }
  }

  @Override
  public boolean canCopy() {
    return true;
  }

  @Override
  public boolean canCut() {
    return false;
  }

  @Override
  public boolean canRename() {
    return false;
  }

  @Override
  public String getName() {
    return String.format("Personen[ personid=%d ]", key);
  }

  @Override
  public String getDisplayName() {
    if (key == nullKey) {
      return "";
    }
    try {
      Personen p = personsManager.findEntity(key);
      if (p != null) {
        return String.format("%s, %s", p.getFamilienname(), p.getVorname());
      } else {
        return getName();
      }
    } catch (DataBaseNotReadyException ex) {
      Exceptions.printStackTrace(ex);
      return getName();
    }
  }

  @Override
  public String getHtmlDisplayName() {
    if (isAvailable()) {
      return String.format("<html>%s</html>", getDisplayName());
    } else {
      return String.format("<html><s>%s</s></html>", getDisplayName());
    }

  }

  @Override
  public Image getIcon(int type) {
    if (key == nullKey) {
      return iconManager().iconNobody;
    }
    BufferedImage result = getBaseIcon();
    if (!isAvailable()) {
     result = iconManager().getDisabledImage(result);
    }
    if (pendingChanges) {
      result = iconManager().getStaredImage(result);
    }
    return result;
  }


  /**
   * Creates a grayed version of the given image and caches it for further use.
   *
   * @param enabledImage
   * @return
   */
  private static Image getDisabledImage(Image enabledImage) {
    return enabledImage;
  }

  /**
   * @return true if the person is available for at least one time slot;
   */
  private boolean isAvailable() {
    try {
      Personen p = personsManager.findEntity(key);
      if (p != null) {
        return p.isAvailable();
      }
    } catch (DataBaseNotReadyException ex) {
    }
    return false;
  }

  public BufferedImage getBaseIcon() {
    if (key == nullKey) {
      return iconManager().iconNobody;
    }
    try {
      Personen p = personsManager.findEntity(key);
      if (p != null) {
        BufferedImage personsIcon = getPersonsIcon(p);
        String gewuenschtewertung = p.getGewuenschtewertung();
        return iconManager().getInstrumentedImage(personsIcon, gewuenschtewertung);
      }
    } catch (DataBaseNotReadyException ex) {
      Exceptions.printStackTrace(ex);
    }
    return iconManager().iconMan; // the default
  }

  private BufferedImage getPersonsIcon(Personen p) {
    assert (p != null);
    if (isGroupleader(p)) {
      return iconManager().iconGroupleader;
    }
    Funktionen function = p.getGewuenschtefunktion();
    if (function != null) {
      if ("LEHRER".equalsIgnoreCase(function.getFunktionid())) {
        return iconManager().iconTeacher;
      }
    }
    boolean isfemale = "Fr.".equals(p.getHerrfrau());
    if ("KIND".equals(p.getAltersgruppe())) {
      if (isfemale) {
        return iconManager().iconChildFemale;
      } else {
        return iconManager().iconChildMale;
      }
    } else {
      if (isfemale) {
        return iconManager().iconWoman;
      } else {
        return iconManager().iconMan;
      }
    }
  }

  private boolean isGroupleader(Personen p) {
    if (p == null) {
      return false;
    }
    List<Personen> groupList = p.getGroupList();
    if (groupList == null) {
      return false;
    }
    if (groupList.isEmpty()) {
      return false;
    }
    return true;
  }

  /**
   * This function is called by the Committer class every time this Record is
   * committed.
   */
  @Override
  public void notifyCommittment() {
    pendingChanges = false;
    fireIconChange();
  }

  /**
   * The transferable in a Drag and Drop is the default Netbeans node
   * transferable plus the specific PersonNodeTransferable.
   *
   * @return the interface for classes that can be used to provide data for a
   * transfer operation.
   * @throws IOException
   */
  @Override
  public Transferable clipboardCopy() throws IOException {
    Transferable nbDefault = super.clipboardCopy();
    ExTransferable added = ExTransferable.create(nbDefault);
    added.put(new PersonNodeTransferable(getPersonId()));
    return added;
  }
}
