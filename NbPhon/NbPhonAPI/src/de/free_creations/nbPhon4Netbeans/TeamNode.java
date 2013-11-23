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

import de.free_creations.dbEntities.Team;
import de.free_creations.dbEntities.Person;
import static de.free_creations.nbPhon4Netbeans.IconManager.iconManager;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
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

  private static class PasteAction extends PasteType {

    private final Integer teamId;
    private final Integer newMemberId;

    private PasteAction(Integer teamId, Integer newMemberId) {
      this.teamId = teamId;
      this.newMemberId = newMemberId;
    }

    @Override
    public Transferable paste() throws IOException {
      System.out.println("#### pasting Person("+newMemberId+") into team ("+teamId+")");
      return ExTransferable.EMPTY;
    }

  }

  private static class TeamMembers extends Children.Array {

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
        Team c = teamManager.findEntity(teamId);
        if (c == null) {
          return empty;
        }
        ArrayList<Node> result = new ArrayList<>();
        for (Person p : c.getPersonList()) {
          PersonNode pn = new PersonNode(p.getPersonid(), personManager);
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
          PersonNode pn = new PersonNode(p.getPersonid(), personManager);
          result.add(pn);
        }
      }
      return result;
    }

  };

  protected TeamNode(Children ch, MutableEntityCollection<Team, Integer> teamManager, Integer teamId) {
    super(ch);
    this.teamId = teamId;
    this.teamManager = teamManager;
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
    this(makeTeamMembers(teamId, teamManager, personManager), teamManager, teamId);
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
  public void detach() {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
