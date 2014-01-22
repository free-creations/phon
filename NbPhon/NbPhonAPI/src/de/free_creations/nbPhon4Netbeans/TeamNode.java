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

import de.free_creations.dbEntities.EntityIdentity;
import de.free_creations.dbEntities.Team;
import de.free_creations.dbEntities.Person;
import static de.free_creations.nbPhon4Netbeans.IconManager.iconManager;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.datatransfer.PasteType;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class TeamNode extends AbstractNode {

  private final Integer teamId;
  private final MutableEntityCollection<Team, Integer> teamManager;
  private final MutableEntityCollection<Person, Integer> personManager;

  private static class PasteAction extends PasteType {

    private final Integer teamId;
    private final Integer newMemberId;

    private PasteAction(Integer teamId, Integer newMemberId) {
      this.teamId = teamId;
      this.newMemberId = newMemberId;
    }

    @Override
    public Transferable paste() throws IOException {
      System.out.println("#### pasting Person(" + newMemberId + ") into team (" + teamId + ")");
      return ExTransferable.EMPTY;
    }

  }

  private static class TeamMembers extends Children.Array {

    private final PropertyChangeListener teamListener = new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (propertyName == null) {
          return;
        }
        switch (propertyName) {
          case (Team.PROP_ADD_PERSON):
            TeamMembers.this.addItem(evt.getNewValue());
            break;
          case (Team.PROP_REMOVE_PERSON):
            TeamMembers.this.removeItem(evt.getOldValue());
            break;
        }
      }
    };

    private final Integer teamId;
    private final MutableEntityCollection<Team, Integer> teamManager;
    private final MutableEntityCollection<Person, Integer> personManager;
    private final Collection<Node> empty = Collections.emptyList();

    private TeamMembers(Integer teamId,
            MutableEntityCollection<Team, Integer> teamManager,
            MutableEntityCollection<Person, Integer> personManager) {
      this.teamId = teamId;
      this.teamManager = teamManager;
      this.personManager = personManager;
      Team.addPropertyChangeListener(teamListener, teamId);

    }

    @Override
    protected Collection<Node> initCollection() {
      if (teamId == null) {
        return singletonTeam();
      } else {
        return regularTeam();
      }
    }

    private Collection<Node> regularTeam() {
      try {
        Team t = teamManager.findEntity(teamId);
        if (t == null) {
          return empty;
        }
        ArrayList<Node> result = new ArrayList<>();
        for (Person p : t.getPersonList()) {
          PersonNode pn = new PersonNode(p.getPersonId(), personManager);
          result.add(pn);
        }
        return result;
      } catch (DataBaseNotReadyException ex) {
        return empty;
      }
    }

    private Collection<Node> singletonTeam() {
      List<Person> pp = personManager.getAll();
      ArrayList<Node> result = new ArrayList<>();
      for (Person p : pp) {
        if (p.getTeam() == null) {
          PersonNode pn = new PersonNode(p.getPersonId(), personManager);
          result.add(pn);
        }
      }
      return result;
    }

    public void destroy() {
      Team.removePropertyChangeListener(teamListener, teamId);
    }

    private void addItem(Object o) {
      // Iam too lazzy to code the adding to the list. Init again will do.
      nodes = initCollection();
      refresh();
//      if (o instanceof EntityIdentity) {
//        EntityIdentity newPerson = (EntityIdentity) o;
//        Integer personid = (Integer) newPerson.primaryKey;
//        //create a node for this new person
//        PersonNode newNode = new PersonNode(personid, personManager);
//        newNode.notifyPendingChanges();
//        if (nodes instanceof ArrayList) {
//          // if the nodes- list is an ArrayList (as created in initCollection)
//          // we can insert on top of the list.
//          ((ArrayList<Node>) nodes).add(0, newNode);
//        } else {
//          // otherways, we just insert at the end.
//          nodes.add(newNode);
//        }
//        refresh();
//      }
    }

    private void removeItem(Object o) {
      // Iam too lazzy to code the removal from the list. Init again will do.
      nodes = initCollection();
      refresh();
    }
  };

  protected TeamNode(Children ch,
          MutableEntityCollection<Team, Integer> teamManager,
          MutableEntityCollection<Person, Integer> personManager,
          Integer teamId) {
    super(ch);
    this.teamId = teamId;
    this.teamManager = teamManager;
    this.personManager = personManager;
  }

  /**
   * Creates a node showing all persons within a team.
   *
   * @param teamId the identity of the team. Note: if null, the node will show
   * all persons that are NOT in a team.
   * @param teamManager
   * @param personManager
   */
  public TeamNode(Integer teamId,
          MutableEntityCollection<Team, Integer> teamManager,
          MutableEntityCollection<Person, Integer> personManager) {
    this(makeTeamMembers(teamId, teamManager, personManager), teamManager, personManager, teamId);
  }

  protected static Children makeTeamMembers(Integer teamId,
          MutableEntityCollection<Team, Integer> teamManager,
          MutableEntityCollection<Person, Integer> personManager) {
    return new TeamMembers(teamId, teamManager, personManager);
  }

  @Override
  public String getName() {
    if (teamId == null) {
      return "Singletons";
    }
    try {
      Team c = teamManager.findEntity(teamId);
      return String.format("%s", c.getName());
    } catch (DataBaseNotReadyException ex) {
      return String.format("Team[%s]", teamId);
    }
  }

  @Override
  public Image getIcon(int type) {
    if (teamId == null) {
      return iconManager().iconNobody;
    }
    BufferedImage result = iconManager().iconTeam;
    return result;
  }

  @Override
  public Image getOpenedIcon(int type) {
    if (teamId == null) {
      return iconManager().iconNobody;
    }
    BufferedImage result = iconManager().iconTeamOpened;
    return result;
  }

  /**
   * Stop listening on the team entity.
   */
  @Override
  public void destroy() {
    Children children = getChildren();
    if (children instanceof TeamMembers) {
      ((TeamMembers) children).destroy();
    }

    try {
      super.destroy();
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  /**
   * This function is called when the user attempts to paste or drop something
   * into this team list.
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
          s.add(new PasteAction(teamId, (Integer) transferData));
        }
      } catch (UnsupportedFlavorException | IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  }
}
