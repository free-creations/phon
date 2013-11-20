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

import de.free_creations.actions.CheckedAction;
import static de.free_creations.actions.CheckedAction.Severity.irrecoverable;
import static de.free_creations.actions.CheckedAction.Severity.ok;
import static de.free_creations.actions.CheckedAction.Severity.recoverable;
import de.free_creations.actions.person.SetGroupleaderRule;
import de.free_creations.dbEntities.Person;
import de.free_creations.nbPhon4Netbeans.PersonNode;
import de.free_creations.nbPhon4Netbeans.PersonsRootNode;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.Exceptions;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.datatransfer.PasteType;

/**
 * The PersonGroupPanel shows all persons who are linked to the same group.
 * 
 *
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class PersonGroupPanel extends javax.swing.JPanel
        implements ExplorerManager.Provider {

  private static class PasteAction extends PasteType {

    private final Integer newMemberId;
    private final Integer groupLeaderId;

    public PasteAction(Integer groupLeaderId, Integer newMemberId) {
      this.newMemberId = newMemberId;
      this.groupLeaderId = groupLeaderId;
    }

    @Override
    public Transferable paste() throws IOException {
      try {
        CheckedAction setGroupleaderRule = SetGroupleaderRule.setGroupleader(newMemberId, groupLeaderId);
        switch (setGroupleaderRule.level()) {
          case ok: {
            setGroupleaderRule.apply();
            return ExTransferable.EMPTY;
          }
          case recoverable: {
            NotifyDescriptor.Confirmation message = new NotifyDescriptor.Confirmation(
                    setGroupleaderRule.problemDescription() + "!\n\n"
                    + setGroupleaderRule.proposedSolution() + "?",
                    NotifyDescriptor.OK_CANCEL_OPTION,
                    NotifyDescriptor.WARNING_MESSAGE);
            Object reply = DialogDisplayer.getDefault().notify(message);
            if (NotifyDescriptor.OK_OPTION.equals(reply)) {
              setGroupleaderRule.apply();
            }

            return ExTransferable.EMPTY;
          }
          case irrecoverable: {
            NotifyDescriptor.Confirmation message = new NotifyDescriptor.Confirmation(
                    setGroupleaderRule.problemDescription() + "!\n\n"
                    + setGroupleaderRule.proposedSolution() + "?",
                    NotifyDescriptor.OK_CANCEL_OPTION,
                    NotifyDescriptor.ERROR_MESSAGE);
            Object reply = DialogDisplayer.getDefault().notify(message);
            if (NotifyDescriptor.OK_OPTION.equals(reply)) {
              setGroupleaderRule.apply();
            }

            return ExTransferable.EMPTY;


          }
        }

      } catch (DataBaseNotReadyException ex) {
        return ExTransferable.EMPTY;
      }
      return ExTransferable.EMPTY;
    }
  };

  /**
   * A specialized kind of root node to display all the persons that have chosen
   * the same group.
   */
  private static class PersonGroupRootNode extends PersonsRootNode {

    final Integer groupLeaderId;

    public PersonGroupRootNode(GroupMemberCollection groupMemberCollection) {
      super(groupMemberCollection);
      groupLeaderId = groupMemberCollection.getLeaderId();
    }

    /**
     * Can this root-node be copied.
     *
     * Currently it seems not to make sense to copy the whole group of favored
     * "colleagues" to some other node.
     *
     * @return always false.
     */
    @Override
    public boolean canCopy() {
      return false;
    }

    /**
     * Can this root-node be cut.
     *
     * same argumentation as in canCopy.
     *
     * @return always false.
     */
    @Override
    public boolean canCut() {
      return false;
    }

    /**
     * Can this root-node be destroyed.
     *
     * same argumentation as in canCopy.
     *
     * @return always false.
     * @return
     */
    @Override
    public boolean canDestroy() {
      return false;
    }

    /**
     * This function is called when the user attempts to paste or drop something
     * into the list.
     *
     * @param t the transferable that the user attempts to drop.
     * @param s
     */
    @Override
    protected void createPasteTypes(Transferable t,
            List<PasteType> s) {
      super.createPasteTypes(t, s);
      assert (s != null);
      if (t.isDataFlavorSupported(PersonNode.PERSON_NODE_FLAVOR)) {
        try {
          Object transferData = t.getTransferData(PersonNode.PERSON_NODE_FLAVOR);
          if (transferData instanceof Integer) {
            s.add(new PasteAction(groupLeaderId, (Integer) transferData));
          }
        } catch (UnsupportedFlavorException | IOException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
    }
  }
  private ExplorerManager explorerManager = new ExplorerManager();
  private GroupMemberCollection groupMemberCollection;

  private class GroupMemberCollection implements
          MutableEntityCollection<Person, Integer>,
          PropertyChangeListener {

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(GroupMemberCollection.this);
    private final Integer leaderId;

    public GroupMemberCollection(Integer leaderId) {
      this.leaderId = leaderId;
    }

    public Integer getLeaderId() {
      return leaderId;
    }

    public void startListening() {
      if (leaderId != null) {
        Person.addPropertyChangeListener(this, leaderId);
      }
    }

    public void stopListening() {
      if (leaderId != null) {
        Person.removePropertyChangeListener(this, leaderId);
      }
    }

    @Override
    public Person newEntity() throws DataBaseNotReadyException {
      // do not call this function. If a new member is added to group list,
      // the GroupMemberCollection is atomatically updated
      throw new UnsupportedOperationException();
    }

    @Override
    public void removeEntity(Integer key) throws DataBaseNotReadyException {
      // do not call this function. If a member is removed from the group list,
      // the GroupMemberCollection is atomatically updated
      throw new UnsupportedOperationException();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
      propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
      propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public List<Person> getAll() {
      final List<Person> emptyResult = Collections.emptyList();
      Person p;
      try {
        p = Manager.getPersonCollection().findEntity(leaderId);
      } catch (DataBaseNotReadyException ex) {
        p = null;
      }
      if (p != null) {
        List<Person> groupList = p.getGroupList();
        // if the team leader appears in the groupList, remove him to avoid recursion.
        groupList.remove(p);
        return groupList;
      } else {
        return emptyResult;
      }
    }

    @Override
    public Person findEntity(Integer key) throws DataBaseNotReadyException {
      return Manager.getPersonCollection().findEntity(key);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (Person.PROP_REMOVE_GROUPMEMBER.equals(evt.getPropertyName())
              || Person.PROP_ADD_GROUPMEMBER.equals(evt.getPropertyName())) {
        // this will inform the root-node which will in turn inform the ExplorerManager
        // which will in turn update the display
        propertyChangeSupport.firePropertyChange(PROP_ITEM_LIST_CHANGED, null, null);
      }
    }
  }

  /**
   * Creates new form PersonGroupPanel
   */
  public PersonGroupPanel() {
    initComponents();
    if (java.beans.Beans.isDesignTime()) {
      return;
    }
    BeanTreeView beanTreeView = (BeanTreeView) scrollPane;
    beanTreeView.setRootVisible(false);
    beanTreeView.setDropTarget(true);
    groupMemberCollection = new GroupMemberCollection(null);

  }

  /**
   * Set the newMemberId of the person who's team should be displayed.
   *
   * @param groupleaderId
   */
  public void setGroupleader(Integer groupleaderId) {
    Integer old = groupMemberCollection.getLeaderId();
    if (!Objects.equals(old, groupleaderId)) {
      groupMemberCollection.stopListening();
      groupMemberCollection = new GroupMemberCollection(groupleaderId);
      display(groupMemberCollection);
    }
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    scrollPane = new BeanTreeView();

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
    );
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane scrollPane;
  // End of variables declaration//GEN-END:variables

  @Override
  public ExplorerManager getExplorerManager() {
    return explorerManager;
  }

  /**
   * Display the collection in the scrollPane.
   *
   * @param groupMemberCollection
   *
   * Note: creating new Root nodes all the time will leave the old nodes as
   * Zombie listeners.
   * @see {@link de.free_creations.dbEntities.PropertyChangeManager}
   */
  private void display(GroupMemberCollection groupMemberCollection) {
    PersonsRootNode rootNode = new PersonGroupRootNode(groupMemberCollection);
    explorerManager.setRootContext(rootNode);
    groupMemberCollection.startListening();
  }
}
