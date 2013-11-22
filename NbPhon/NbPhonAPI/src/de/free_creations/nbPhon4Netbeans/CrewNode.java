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

import de.free_creations.dbEntities.Crew;
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
public class CrewNode extends AbstractNode {

  private final Integer crewId;
  private final MutableEntityCollection<Crew, Integer> crewManager;

  private static class PasteAction extends PasteType {

    private final Integer crewId;
    private final Integer newMemberId;

    private PasteAction(Integer crewId, Integer newMemberId) {
      this.crewId = crewId;
      this.newMemberId = newMemberId;
    }

    @Override
    public Transferable paste() throws IOException {
      System.out.println("#### pasting Person("+newMemberId+") into crew ("+crewId+")");
      return ExTransferable.EMPTY;
    }

  }

  private static class CrewMembers extends Children.Array {

    private final Integer crewId;
    private final MutableEntityCollection<Crew, Integer> crewManager;
    private final MutableEntityCollection<Person, Integer> personManager;
    private final Collection<Node> empty = Collections.emptyList();

    private CrewMembers(Integer crewId,
            MutableEntityCollection<Crew, Integer> crewManager,
            MutableEntityCollection<Person, Integer> personManager) {
      this.crewId = crewId;
      this.crewManager = crewManager;
      this.personManager = personManager;
    }

    @Override
    protected Collection<Node> initCollection() {
      if (crewId == null) {
        return singletonCrew();
      } else {
        return regularCrew();
      }
    }

    private Collection<Node> regularCrew() {
      try {
        Crew c = crewManager.findEntity(crewId);
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

    private Collection<Node> singletonCrew() {
      List<Person> pp = personManager.getAll();
      ArrayList<Node> result = new ArrayList<>();
      for (Person p : pp) {
        if (p.getCrew() == null) {
          PersonNode pn = new PersonNode(p.getPersonid(), personManager);
          result.add(pn);
        }
      }
      return result;
    }

  };

  protected CrewNode(Children ch, MutableEntityCollection<Crew, Integer> crewManager, Integer crewId) {
    super(ch);
    this.crewId = crewId;
    this.crewManager = crewManager;
  }

  /**
   * Creates a node showing all persons within a crew.
   *
   * @param crewId the identity of the crew. Note: if null, the node will show
   * all persons that are NOT in a crew.
   * @param crewManager
   * @param personManager
   */
  public CrewNode(Integer crewId,
          MutableEntityCollection<Crew, Integer> crewManager,
          MutableEntityCollection<Person, Integer> personManager) {
    this(makeCrewMembers(crewId, crewManager, personManager), crewManager, crewId);
  }

  protected static Children makeCrewMembers(Integer crewId,
          MutableEntityCollection<Crew, Integer> crewManager,
          MutableEntityCollection<Person, Integer> personManager) {
    return new CrewMembers(crewId, crewManager, personManager);
  }

  @Override
  public String getName() {
    if (crewId == null) {
      return "Singletons";
    }
    try {
      Crew c = crewManager.findEntity(crewId);
      return String.format("%s", c.getName());
    } catch (DataBaseNotReadyException ex) {
      return String.format("Crew[%s]", crewId);
    }
  }

  @Override
  public Image getIcon(int type) {
    if (crewId == null) {
      return iconManager().iconNobody;
    }
    BufferedImage result = iconManager().iconCrew;
    return result;
  }

  @Override
  public Image getOpenedIcon(int type) {
    if (crewId == null) {
      return iconManager().iconNobody;
    }
    BufferedImage result = iconManager().iconCrewOpened;
    return result;
  }

  /**
   * Stop listening on the crew entity.
   */
  public void detach() {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   * This function is called when the user attempts to paste or drop something
   * into this crew list.
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
          s.add(new PasteAction(crewId, (Integer) transferData));
        }
      } catch (UnsupportedFlavorException | IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  }
}
