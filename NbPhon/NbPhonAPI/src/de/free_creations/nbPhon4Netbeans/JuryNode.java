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

import de.free_creations.dbEntities.Jury;

import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.openide.nodes.Node;

/**
 *
 * @see http://netbeans.dzone.com/nb-how-to-drag-drop-with-nodes-api for a DnD
 * example.
 * @author Harald Postner <Harald at free-creations.de>
 */
public class JuryNode extends AbstractNode implements CommittableNode {

  private static class LChildren extends Children.Array {

    private final Collection<Node> ch;

    public LChildren(Collection<Node> ch) {
      super();
      assert (ch != null);
      this.ch = ch;
    }

    @Override
    protected Collection<Node> initCollection() {
      return ch;
    }
  };

  /**
   * The data flavor for a Drag and Drop action.
   */
  public static class JuryNodeFlavor extends DataFlavor {

    public JuryNodeFlavor() {
      super(JuryNode.class, "Jury");
    }
  }

  /**
   * The transferable that is transfered in a Drag and Drop action.
   */
  public class JuryNodeTransferable extends ExTransferable.Single {

    private final String transferablePersonId;

    public JuryNodeTransferable(String personId) {
      super(JURY_NODE_FLAVOR);
      this.transferablePersonId = personId;
    }

    /**
     * The jury node transfers the primary key of the record it represents.
     *
     * @return
     */
    @Override
    protected String getData() {
      return transferablePersonId;
    }
  }
  public static final DataFlavor JURY_NODE_FLAVOR = new JuryNodeFlavor();
  private boolean pendingChanges = false;
  private final String key;
  private final PropertyChangeListener listener = new PropertyChangeListener() {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      notifyPendingChanges();

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

  /**
   * Returns the juryId.
   *
   * @return returns the juryId.
   */
  public String getJuryId() {
    return key;
  }
  private final MutableEntityCollection<Jury, String> juryManager;
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
        JuryEditorProvider provider =
                Lookup.getDefault().lookup(
                JuryEditorProvider.class);
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
        JuryEditorProvider provider =
                Lookup.getDefault().lookup(
                JuryEditorProvider.class);
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

  public JuryNode(String juryId, MutableEntityCollection<Jury, String> juryManager) {
    super(makeChildren());

    this.key = juryId;

    this.juryManager = juryManager;
    if (key != null) {
      Jury.addPropertyChangeListener(listener, juryId);
      getCookieSet().add(editCookie);
    }
  }

  private static Children makeChildren() {
    Node[] nodes = new Node[]{
      new AbstractNode(Children.LEAF) {
        @Override
        public String getName() {
          return "Lehrkraft";
        }
      },
      new AbstractNode(Children.LEAF) {
        @Override
        public String getName() {
          return "Empfang";
        }
      },
      new AbstractNode(Children.LEAF) {
        @Override
        public String getName() {
          return "Saaldienst";
        }
      },
      new AbstractNode(Children.LEAF) {
        @Override
        public String getName() {
          return "Springer";
        }
      },};
    ArrayList<Node> aNodes = new ArrayList<>();
    aNodes.addAll(Arrays.asList(nodes));
    LChildren children = new LChildren(aNodes);
    return children;
  }

  @Override
  public Action[] getActions(boolean context) {
    if (key != null) {
      return allActions;
    } else {
      return null;
    }
  }

  @Override
  public Action getPreferredAction() {
    if (key != null) {
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
    return String.format("Jury[ %s ]", key);
  }

  @Override
  public String getDisplayName() {
    if (key == null) {
      return "";
    }
    try {
      Jury j = juryManager.findEntity(key);
      if (j != null) {
        String wertung = j.getWertung();
        if (wertung != null) {
          return String.format("%s", wertung);
        } else {
          return getName();
        }
      } else {
        return getName();
      }
    } catch (DataBaseNotReadyException ex) {
      Exceptions.printStackTrace(ex);
      return getName();
    }
  }

  @Override
  public Image getIcon(int type) {
    BufferedImage result = iconManager().iconJury;
    if (key != null) {
      try {
        Jury j = juryManager.findEntity(key);
        if (j != null) {
          String wertungstyp = j.getWertungstyp();
          result = iconManager().getInstrumentedImage(result, wertungstyp);
        }
      } catch (DataBaseNotReadyException ex) {
      }
    }
    if (pendingChanges) {
      result = iconManager().getStaredImage(result);
    }
    return result;
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
   * transferable plus the specific JuryNodeTransferable.
   *
   * @return the interface for classes that can be used to provide data for a
   * transfer operation.
   * @throws IOException
   */
  @Override
  public Transferable clipboardCopy() throws IOException {
    Transferable nbDefault = super.clipboardCopy();
    ExTransferable added = ExTransferable.create(nbDefault);
    added.put(new JuryNodeTransferable(getJuryId()));
    return added;
  }
}
