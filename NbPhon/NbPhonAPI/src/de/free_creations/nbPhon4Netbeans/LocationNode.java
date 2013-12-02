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

import de.free_creations.dbEntities.Location;
import static de.free_creations.nbPhon4Netbeans.IconManager.iconManager;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.cookies.EditCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class LocationNode extends AbstractNode implements CommittableNode {

  private final Integer locationId;
  private boolean pendingChanges = false;

  private final PropertyChangeListener listener = new PropertyChangeListener() {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      notifyPendingChanges();
      if (Location.PROP_NAME.equals(evt.getPropertyName())) {
        fireDisplayNameChange(null, getDisplayName());
      }
    }
  };

  private final Action editAction = new AbstractAction("Edit") {
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        LocationEditorProvider provider
                = Lookup.getDefault().lookup(
                        LocationEditorProvider.class);
        if (provider != null) {
          provider.getEditor(false, locationId);
        } else {
          throw new RuntimeException("No Editor provider found.");
        }
      } catch (RuntimeException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  };
  private final Action editNewWindowAction = new AbstractAction("Edit in new Window") {
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        LocationEditorProvider provider
                = Lookup.getDefault().lookup(
                        LocationEditorProvider.class);
        if (provider != null) {
          provider.getEditor(true, locationId);
        } else {
          throw new RuntimeException("No Editor provider found.");
        }
      } catch (RuntimeException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  };
  private final Action[] allActions = new Action[]{editAction, editNewWindowAction};
  private final MutableEntityCollection<Location, Integer> locationManager;
  private final EditCookie editCookie = new EditCookie() {
    @Override
    public void edit() {
      editAction.actionPerformed(null);
    }
  };

  public LocationNode(Integer locationId, MutableEntityCollection<Location, Integer> locationManager) {
    super(Children.LEAF);
    this.locationId = locationId;
    this.locationManager = locationManager;
    if (locationId != null) {
      Location.addPropertyChangeListener(listener, locationId);
      getCookieSet().add(editCookie);
    }
  }

  /**
   * @return a name without accessing the DAO.
   */
  @Override
  public String getName() {
    return String.format("Location[%s]", locationId);
  }

  @Override
  public Image getIcon(int type) {
    BufferedImage result = iconManager().iconLocation;
    if (pendingChanges) {
      result = iconManager().getStaredImage(result);
    }
    return result;
  }

  @Override
  public Action[] getActions(boolean context) {
    if (locationId != null) {
      return allActions;
    } else {
      return null;
    }
  }

  @Override
  public Action getPreferredAction() {
    if (locationId != null) {
      return editAction;
    } else {
      return null;
    }
  }

  @Override
  public String getDisplayName() {
    if (locationId == null) {
      return "";
    }
    try {
      Location l = locationManager.findEntity(locationId);
      if (l != null) {
        return l.getName();
      } else {
        return getName();
      }
    } catch (DataBaseNotReadyException ex) {
      Exceptions.printStackTrace(ex);
      return getName();
    }
  }

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
  public void notifyCommittment() {
    pendingChanges = false;
    fireIconChange();
  }
}
