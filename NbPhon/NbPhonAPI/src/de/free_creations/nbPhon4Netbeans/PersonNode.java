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
package de.free_creations.nbPhon4Netbeans;

import de.free_creations.dbEntities.ContestType;
import de.free_creations.dbEntities.JobType;
import de.free_creations.dbEntities.Person;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.cookies.EditCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;
import static de.free_creations.nbPhon4Netbeans.IconManager.*;
import java.awt.image.BufferedImage;

/**
 *
 * @see http://netbeans.dzone.com/nb-how-to-drag-drop-with-nodes-apifor a DnD
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
      if (Person.PROP_GENDER.equals(evt.getPropertyName())) {
        fireDisplayNameChange(null, getDisplayName());
      }
      if (Person.PROP_SURNAME.equals(evt.getPropertyName())) {
        fireDisplayNameChange(null, getDisplayName());
      }
      if (Person.PROP_GIVENNAME.equals(evt.getPropertyName())) {
        fireDisplayNameChange(null, getDisplayName());
      }
      if (Person.PROP_AVAILABILITY.equals(evt.getPropertyName())) {
        fireDisplayNameChange(null, getDisplayName());
        fireIconChange();
      }
      if (Person.PROP_CONTESTTYPE.equals(evt.getPropertyName())) {
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

  @Override
  public void destroy() {
    if (key != nullKey) {
      Person.removePropertyChangeListener(listener, keyToPersonId(key));
    }
    try {
      super.destroy();
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
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
  private final MutableEntityCollection<Person, Integer> personsManager;
  private final EditCookie editCookie = new EditCookie() {
    @Override
    public void edit() {
      editAction.actionPerformed(null);
    }
  };
  private final Action preferedAction;
  private final Action editAction = new AbstractAction("Edit") {
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        PersonEditorProvider provider
                = Lookup.getDefault().lookup(
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
        PersonEditorProvider provider
                = Lookup.getDefault().lookup(
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

  public PersonNode(Integer personId, MutableEntityCollection<Person, Integer> personsManager) {
    this(personId, personsManager, null);
  }

  public PersonNode(Integer personId, MutableEntityCollection<Person, Integer> personsManager, Action preferedAction) {
    super(Children.LEAF);
    assert (personsManager != null);
    this.key = personIdToKey(personId);
    this.personsManager = personsManager;
    this.preferedAction = preferedAction;
    if (key != nullKey) {
      Person.addPropertyChangeListener(listener, personId);
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
      if (preferedAction != null) {
        return preferedAction;
      } else {
        return editAction;
      }
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
    return String.format("Person[ personid=%d ]", key);
  }

  @Override
  public String getDisplayName() {
    if (key == nullKey) {
      return "";
    }
    try {
      Person p = personsManager.findEntity(key);
      if (p != null) {
        return String.format("%s, %s", p.getSurname(), p.getGivenname());
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
   * @return true if the person is available for at least one time slot;
   */
  private boolean isAvailable() {
    try {
      Person p = personsManager.findEntity(key);
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
      Person p = personsManager.findEntity(key);
      if (p != null) {
        BufferedImage personsIcon = getPersonsIcon(p);
        ContestType contestType = p.getContestType();
        if (contestType != null) {
          return iconManager().underLayContestTypeImage(personsIcon, contestType.getIcon());
        } else {
          return personsIcon;
        }
      }
    } catch (DataBaseNotReadyException ex) {
      Exceptions.printStackTrace(ex);
    }
    return iconManager().iconMan; // the default
  }

  private BufferedImage getPersonsIcon(Person p) {
    assert (p != null);

    JobType jobType = p.getJobType();
    if (jobType != null) {
      if ("LEHRER".equalsIgnoreCase(jobType.getJobTypeId())) {
        return iconManager().iconTeacher;
      }
    }
    boolean isfemale = "Fr.".equals(p.getGender());
    if ("KIND".equals(p.getAgegroup())) {
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
