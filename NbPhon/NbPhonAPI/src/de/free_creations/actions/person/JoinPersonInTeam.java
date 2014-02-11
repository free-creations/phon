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
import de.free_creations.dbEntities.JobType;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.Team;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This rule applies when two persons should be in the same team.
 *
 * 1) if both persons are not yet members of a team, a new team is created.
 *
 * 2) if one of the two persons is already member of a team and the other is
 * still a singleton; the singleton will be added to the existing team.
 *
 * 3) if both persons are already members of a team and the sum of all members
 * does not exceed 15; the two groups are joined into one team.
 *
 * 4) if both persons are already members of a team and the sum of all members
 * exceeds 15; the member from the larger team is transfered into the smaller
 * team.
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class JoinPersonInTeam implements CheckedAction {

  public static final int maxTeamSize = 15;

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
    if (person_1.equals(person_2)) {
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
    if(team_1.equals(team_2)){
      return;
    }
    List<Person> personList_1 = team_1.getPersonList();
    List<Person> personList_2 = team_2.getPersonList();
    int size_1 = (personList_1 == null) ? 0 : personList_1.size();
    int size_2 = (personList_2 == null) ? 0 : personList_2.size();

    if ((size_1 + size_2) < maxTeamSize) {
      joinTeams(team_1, team_2);
      return;
    }

    if (size_1 > size_2) {
      movePerson(team_1, team_2, person_1);
    } else {
      movePerson(team_2, team_1, person_2);
    }

  }

  private void createNewTeam(Person person_1, Person person_2) throws DataBaseNotReadyException {
    assert (person_1 != null);
    assert (person_2 != null);
    assert (!person_1.equals(person_2));
    assert (person_1.getTeam() == null);
    assert (person_2.getTeam() == null);
    Team newTeam = Manager.getTeamCollection().newEntity();
    String teamName = String.format("Team: %s", person_1.getSurname());
    if (isTeacher(person_2)) {
      teamName = String.format("Team: %s", person_2.getSurname());
    }
    newTeam.setName(teamName);
    person_1.setTeam(newTeam);
    person_2.setTeam(newTeam);
  }

  private void addPerson(Person person, Team team) throws DataBaseNotReadyException {
    assert (person != null);
    assert (team != null);
    assert (person.getTeam() == null);
    person.setTeam(team);

    if (findTeacher(team) == null) {
      if (isTeacher(person)) {
        team.setName(String.format("Team[%s]", person.getSurname()));
      }
    }

  }

  private void joinTeams(Team team_1, Team team_2) throws DataBaseNotReadyException {
    assert (team_1 != null);
    assert (team_2 != null);
    if (team_1.equals(team_2)) {
      return;
    }
    Team teamToKeep = team_1;
    Team teamToRemove = team_2;
    // prefer to keep the larger team
    if (teamToKeep.getPersonList().size() < teamToRemove.getPersonList().size()) {
      // swap
      Team temp = teamToKeep;
      teamToKeep = teamToRemove;
      teamToRemove = temp;
    }
    // prefer to keep the team with a teacher
    if (findTeacher(teamToKeep) == null) {
      if (findTeacher(teamToRemove) != null) {
        // swap
        Team temp = teamToKeep;
        teamToKeep = teamToRemove;
        teamToRemove = temp;
      }
    }

    ArrayList<Person> personsToMove = new ArrayList<>(teamToRemove.getPersonList());

    for (Person p : personsToMove) {
      p.setTeam(teamToKeep);
    }
    Manager.getTeamCollection().removeEntity(teamToRemove.getTeamId());
  }

  private void movePerson(Team team_1, Team team_2, Person person) {
    assert (team_1 != null);
    assert (team_2 != null);
    assert (person != null);
    assert (!team_1.equals(team_2));
    assert (Objects.equals(team_1, person.getTeam()));
    person.setTeam(team_2);
  }

  private boolean isTeacher(Person p) throws DataBaseNotReadyException {
    assert (p != null);
    JobType jopTeacher = Manager.getJobTypeCollection().findEntity("LEHRER");
    return Objects.equals(jopTeacher, p.getJobType());
  }

  /**
   * searches for a teacher in the given team.
   *
   * @param t the team to search trough.
   * @return a teacher or null.
   * @throws DataBaseNotReadyException
   */
  private Person findTeacher(Team t) throws DataBaseNotReadyException {
    assert (t != null);
    List<Person> pp = t.getPersonList();
    for (Person p : pp) {
      if (isTeacher(p)) {
        return p;
      }
    }
    return null;
  }

}
