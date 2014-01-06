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

import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.ContestType;
import de.free_creations.dbEntities.Job;

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
import de.free_creations.nbPhonAPI.Manager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.openide.nodes.Node;

/**
 *
 * @see http://netbeans.dzone.com/nb-how-to-drag-drop-with-nodes-apifor a DnD
 * example.
 * @author Harald Postner <Harald at free-creations.de>
 */
public class ContestNode extends AbstractNode implements CommittableNode {

  private static class JobChildren extends Children.Array {

    private final Collection<Node> ch;

    public JobChildren(Collection<Node> ch) {
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
  public static class ContestNodeFlavor extends DataFlavor {

    public ContestNodeFlavor() {
      super(ContestNode.class, "Contest");
    }
  }

  /**
   * The transferable that is transfered in a Drag and Drop action.
   */
  public class ContestNodeTransferable extends ExTransferable.Single {

    private final Integer contestId;

    public ContestNodeTransferable(Integer contestId) {
      super(CONTEST_NODE_FLAVOR);
      this.contestId = contestId;
    }

    /**
     * The contest node transfers the primary key of the record it represents.
     *
     * @return
     */
    @Override
    protected Integer getData() {
      return contestId;
    }
  }
  public static final DataFlavor CONTEST_NODE_FLAVOR = new ContestNodeFlavor();
  private boolean pendingChanges = false;
  private final Integer key;
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
   * Returns the contestId.
   *
   * @return returns the contestId.
   */
  public Integer getContestId() {
    return key;
  }
  private final MutableEntityCollection<Contest, Integer> contestManager;
  private final EditCookie editCookie = new EditCookie() {
    @Override
    public void edit() {
      editAction.actionPerformed(null);
    }
  };
  private final Action editAction = new AbstractAction("Edit") {
    @Override
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public void actionPerformed(ActionEvent e) {
      try {
        ContestEditorProvider provider
                = Lookup.getDefault().lookup(
                        ContestEditorProvider.class);
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
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public void actionPerformed(ActionEvent e) {
      try {
        ContestEditorProvider provider
                = Lookup.getDefault().lookup(
                        ContestEditorProvider.class);
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

  public ContestNode(Integer contestId, MutableEntityCollection<Contest, Integer> contestManager, boolean showJobTypes) {
    super(makeChildren(contestId, showJobTypes));
    this.key = contestId;
    this.contestManager = contestManager;
    if (key != null) {
      Contest.addPropertyChangeListener(listener, contestId);
      getCookieSet().add(editCookie);
    }
  }

  public ContestNode(Integer contestId, MutableEntityCollection<Contest, Integer> contestManager) {
    this(contestId, contestManager, false);
  }

  @Override
  public void destroy() {
    Contest.removePropertyChangeListener(listener, key);
    try {
      super.destroy();
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  private static Children makeChildren(Integer contestId, boolean showJobs) {
    if (!showJobs) {
      return Children.LEAF;
    }
    ArrayList<Node> childNodes = new ArrayList<>();
    List<Job> jj = Manager.getJobCollection().getAll();
    for (Job j : jj) {
      ContestJobNode newNode = new ContestJobNode(contestId, j.getJobId());
      childNodes.add(newNode);
    }
    JobChildren children = new JobChildren(childNodes);
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
    return String.format("Contest[ %s ]", key);
  }

  @Override
  public String getDisplayName() {
    if (key == null) {
      return "";
    }
    try {
      Contest j = contestManager.findEntity(key);
      if (j != null) {
        String wertung = j.getName();
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
    if (key == null) {
      return iconManager().iconNullContest;
    }
    BufferedImage result = iconManager().iconContest;
    if (key != null) {
      try {
        Contest j = contestManager.findEntity(key);
        if (j != null) {
          ContestType contestType = j.getContestType();
          if (contestType != null) {
            result = iconManager().underLayContestTypeImage(result, contestType.getIcon());
          }
        }
      } catch (DataBaseNotReadyException ex) {
      }
    }
    if (pendingChanges) {
      result = iconManager().getStaredImage(result);
    }
    return result;
  }

  @Override
  public Image getOpenedIcon(int type) {
    return getIcon(type);
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
   * transferable plus the specific ContestNodeTransferable.
   *
   * @return the interface for classes that can be used to provide data for a
   * transfer operation.
   * @throws IOException
   */
  @Override
  public Transferable clipboardCopy() throws IOException {
    Transferable nbDefault = super.clipboardCopy();
    ExTransferable added = ExTransferable.create(nbDefault);
    added.put(new ContestNodeTransferable(getContestId()));
    return added;
  }
}
