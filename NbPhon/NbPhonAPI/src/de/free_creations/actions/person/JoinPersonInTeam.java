/*
 * Copyright 2013 Harald Postner<harald at free-creations.de>.
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
package de.free_creations.actions.person;

import de.free_creations.actions.CheckedAction;
import static de.free_creations.actions.CheckedAction.Severity.*;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.Team;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.util.List;

/**
 * This rule applies when two persons should be in the same team.
 *
 * 1) if both persons are not yet members of a group, a new group is created.
 *
 * 2) if one of the two persons is already member of a group and the other is
 * still a singleton; the singleton will be added to the existing group.
 *
 * 3) if both persons are already members of a group and the sum of all members
 * does not exceed 15; the two groups are joined into one group.
 *
 * 4) if both persons are already members of a group and the sum of all members
 * exceeds 15; the member from the larger team is transfered into the smaller
 * group.
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class JoinPersonInTeam implements CheckedAction {

  private static final int maxSize = 15;

  private final Integer personId_1;
  private final Integer personId_2;

  public JoinPersonInTeam(Integer person_1, Integer person_2) {
    this.personId_1 = person_1;
    this.personId_2 = person_2;
  }

  @Override
  public Severity level() {
    return ok;
  }

  @Override
  public String problemDescription() {
    return "";
  }

  @Override
  public String[] proposedSolution() {
    return new String[]{};
  }

  @Override
  public int workaroundCount() {
    return 0;
  }

  @Override
  public void apply(int ignored) throws DataBaseNotReadyException {

    Person person_1 = Manager.getPersonCollection().findEntity(personId_1);
    if (person_1 == null) {
      return;
    }
    Person person_2 = Manager.getPersonCollection().findEntity(personId_2);
    if (person_2 == null) {
      return;
    }
    Team team_1 = person_1.getTeam();
    Team team_2 = person_2.getTeam();

    if (team_1 == null) {
      if (team_2 == null) {
        // team_1 == null; team_2 == null;
        createNewTeam(person_1, person_2);
        return;
      } else {
        // team_1 == null; team_2 != null;
        addPerson(person_1, team_2);
        return;
      }
    } else {
      if (team_2 == null) {
        // team_1 != null; team_2 == null;
        addPerson(person_2, team_1);
        return;
      }
    }
    // team_1 != null; team_2 != null;
    List<Person> personList_1 = team_1.getPersonList();
    List<Person> personList_2 = team_2.getPersonList();
    int size_1 = (personList_1 == null) ? 0 : personList_1.size();
    int size_2 = (personList_2 == null) ? 0 : personList_2.size();

    if ((size_1 + size_2) < maxSize) {
      joinTeams(team_1, team_2);
      return;
    }

    if (size_1 > size_2) {
      movePerson(team_1, team_2, person_1);
    } else {
      movePerson(team_2, team_1, person_2);
    }

  }

  private void createNewTeam(Person person_1, Person person_2) {
    System.out.printf("### create new team for %s %s \n ", person_1 ,person_2);
  }

  private void addPerson(Person person, Team team) {
    System.out.printf("### add %s to %s\n", person ,team);
  }

  private void joinTeams(Team team_1, Team team_2) {
    System.out.printf("### join %s with %s\n", team_1 ,team_2);
  }

  private void movePerson(Team team_1, Team team_2, Person person) {
    System.out.printf("### move %s from %s to %s\n",person, team_1 ,team_2);
  }

}
