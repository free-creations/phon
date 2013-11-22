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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class CrewNode extends AbstractNode {

  private final Integer crewId;

  private static class CrewMembers extends Children.Array {

    private final Integer crewId;
    private final MutableEntityCollection<Crew, Integer> crewManager;
    private final MutableEntityCollection<Person, Integer> personManager;

    private CrewMembers(Integer crewId,
            MutableEntityCollection<Crew, Integer> crewManager,
            MutableEntityCollection<Person, Integer> personManager) {
      this.crewId = crewId;
      this.crewManager = crewManager;
      this.personManager = personManager;
    }

    @Override
    protected Collection<Node> initCollection() {
      final Collection<Node> empty = Collections.emptyList();
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

  };

  private CrewNode(Children ch, Integer crewId) {
    super(ch);
    this.crewId = crewId;
  }

  public CrewNode(Integer crewId,
          MutableEntityCollection<Crew, Integer> crewManager,
          MutableEntityCollection<Person, Integer> personManager) {
    this(new CrewMembers(crewId, crewManager, personManager), crewId);
  }

  @Override
  public String getName() {
    return String.format("%s", crewId);
  }

  @Override
  public Image getIcon(int type) {
    BufferedImage result = iconManager().iconCrew;
    return result;
  }

  @Override
  public Image getOpenedIcon(int type) {
    BufferedImage result = iconManager().iconCrewOpened;
    return result;
  }
}
