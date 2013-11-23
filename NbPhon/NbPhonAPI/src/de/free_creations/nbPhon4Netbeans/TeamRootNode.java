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
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class TeamRootNode extends AbstractNode {

  private static class TeamMembers extends Children.Array {

    private final MutableEntityCollection<Team, Integer> teamManager;
    private final MutableEntityCollection<Person, Integer> personManager;

    private TeamMembers(
            MutableEntityCollection<Team, Integer> teamManager,
            MutableEntityCollection<Person, Integer> personManager) {

      this.teamManager = teamManager;
      this.personManager = personManager;
    }

    @Override
    protected Collection<Node> initCollection() {
      List<Team> cc = teamManager.getAll();
      ArrayList<Node> result = new ArrayList<>();
      for(Team c:cc){
        TeamNode cn = new TeamNode(c.getTeamId(), teamManager, personManager);
        result.add(cn);
      }
      // add a special node showing all those persons that are not in a team
      TeamNode cn = new TeamNode(null, teamManager, personManager);
      result.add(cn);
      return result;
    }

  };

  public TeamRootNode(MutableEntityCollection<Team, Integer> teamManager,
          MutableEntityCollection<Person, Integer> personManager) {
    super(new TeamMembers(teamManager, personManager));
  }

  @Override
  public String getName() {
    return String.format("Team Root");
  }

}
