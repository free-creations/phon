/*
 * Copyright 2014 Harald Postner<harald at free-creations.de>.
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

import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.Team;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.TeamCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class JoinPersonInTeamTest {

  public JoinPersonInTeamTest() {
  }

  @Before
  public void setUp() throws Exception {
    assertTrue("did you start the database-server?", Manager.isOpen());
  }

  @After
  public void tearDown() {
    Manager.close(false);
  }

  /**
   * Test of level method, of class JoinPersonInTeam.
   */
  @Test
  public void testLevel() {
  }

  /**
   * Test of problemDescription method, of class JoinPersonInTeam.
   */
  @Test
  public void testProblemDescription() {
  }

  /**
   * Test of proposedSolution method, of class JoinPersonInTeam.
   */
  @Test
  public void testProposedSolution() {
  }

  /**
   * Test of workaroundCount method, of class JoinPersonInTeam.
   */
  @Test
  public void testWorkaroundCount() {
  }

  /**
   * Test of apply method, of class JoinPersonInTeam.
   *
   * 1) if both persons are not yet members of a team; a new team shall be
   * created.
   */
  @Test
  public void testApply_1() throws Exception {
    // preparation
    Person p1 = findSingleton(null);
    Person p2 = findSingleton(p1);
    List<Team> tt = Manager.getTeamCollection().getAll();
    ArrayList<Team> teamsBefore = new ArrayList<>(tt);

    // the statement we want to test
    JoinPersonInTeam action = new JoinPersonInTeam(p1.getPersonId(), p2.getPersonId());
    action.apply(0);

    // verification
    assertNotNull(p1.getTeam()); // is p1 member of a team
    assertNotNull(p2.getTeam()); // is p2 member of a team
    assertEquals(p1.getTeam(), p2.getTeam()); // are both members of the same team
    assertFalse(teamsBefore.contains(p1.getTeam())); // is the team new?

  }

  /**
   * Test of apply method, of class JoinPersonInTeam.
   *
   * 2) if one of the two persons is already member of a team and the other is
   * still a singleton; the singleton shall be added to the existing team.
   */
  @Test
  public void testApply_2() throws Exception {
    // preparation
    Person p1 = findSingleton(null);
    Person p2 = findPersonInSmallTeam(null);

    // the statement we want to test
    JoinPersonInTeam action = new JoinPersonInTeam(p1.getPersonId(), p2.getPersonId());
    action.apply(0);

    // verification
    assertNotNull(p1.getTeam()); // is p1 member of a team
    assertNotNull(p2.getTeam()); // is p2 member of a team
    assertEquals(p1.getTeam(), p2.getTeam()); // are both members of the same team
  }

  /**
   * Test of apply method, of class JoinPersonInTeam.
   *
   * 3) if both persons are already members of a team and the sum of all members
   * does not exceed 15; the two groups are joined into one team.
   */
  @Test
  public void testApply_3() throws Exception {
    // preparation
    Person p1 = findPersonInSmallTeam(null);
    Person p2 = findPersonInSmallTeam(p1.getTeam());
    int originalTeam_1 = p1.getTeam().getTeamId();
    int originalTeam_2 = p2.getTeam().getTeamId();
    int memberCount_1 = p1.getTeam().getPersonList().size();
    int memberCount_2 = p2.getTeam().getPersonList().size();

    // the statement we want to test
    JoinPersonInTeam action = new JoinPersonInTeam(p1.getPersonId(), p2.getPersonId());
    action.apply(0);

    //-- verification
    assertNotNull(p1.getTeam()); // is p1 member of a team
    assertNotNull(p2.getTeam()); // is p2 member of a team
    assertEquals(p1.getTeam(), p2.getTeam()); // are both members of the same team
    // does the new team  have all members of the old teams.
    assertEquals((memberCount_1 + memberCount_2), p1.getTeam().getPersonList().size());

    // has one of the two teams been removed.
    int destroyedTeamId = originalTeam_1;
    if (p1.getTeam().getTeamId() == originalTeam_1) {
      destroyedTeamId = originalTeam_2;
    }
    Team destroyedTeam = Manager.getTeamCollection().findEntity(destroyedTeamId);
    assertNull(destroyedTeam);

  }

  /**
   * Test of apply method, of class JoinPersonInTeam.
   *
   * 4) if both persons are already members of a team and the sum of all members
   * exceeds 15; the member from the larger team is transfered into the smaller
   * team.
   */
  @Test
  public void testApply_4() throws Exception {
    // preparation
    Person p1 = findPersonInLargeTeam(null);
    Person p2 = findPersonInLargeTeam(p1.getTeam());

    // the statement we want to test
    JoinPersonInTeam action = new JoinPersonInTeam(p1.getPersonId(), p2.getPersonId());
    action.apply(0);

    //-- verification
    assertNotNull(p1.getTeam()); // is p1 member of a team
    assertNotNull(p2.getTeam()); // is p2 member of a team
    assertEquals(p1.getTeam(), p2.getTeam()); // are both members of the same team

  }

  /**
   * Helper function that searches for a person which is not yet assigned to a
   * team.
   *
   * @param exclude a person to be excluded from the search (may be null)
   * @return
   */
  private Person findSingleton(Person exclude) {
    List<Person> pp = Manager.getPersonCollection().getAll();
    for (Person p : pp) {
      if (!Objects.equals(p, exclude)) {
        if (p.getTeam() == null) {
          return p;
        }
      }
    }
    fail("Bad Test Data");
    return null;
  }

  /**
   * Helper function that searches for a person which is assigned to a small
   * team.
   *
   * @param exclude a person to be excluded from the search (may be null)
   * @return
   */
  private Person findPersonInSmallTeam(Team exclude) {
    List<Person> pp = Manager.getPersonCollection().getAll();
    for (Person p : pp) {
      if (!Objects.equals(p.getTeam(), exclude)) {
        Team team = p.getTeam();
        if (team != null) {
          if (team.getPersonList().size() < (JoinPersonInTeam.maxTeamSize / 2)) {
            return p;
          }
        }
      }
    }
    fail("Bad Test Data");
    return null;
  }

  /**
   * Helper function that searches for a person which is assigned to a large
   * team.
   *
   * @param exclude a person to be excluded from the search (may be null)
   * @return
   */
  private Person findPersonInLargeTeam(Team exclude) {
    List<Person> pp = Manager.getPersonCollection().getAll();
    for (Person p : pp) {
      if (!Objects.equals(p.getTeam(), exclude)) {
        Team team = p.getTeam();
        if (team != null) {
          if (team.getPersonList().size() > (JoinPersonInTeam.maxTeamSize / 2)) {
            return p;
          }
        }
      }
    }
    fail("Bad Test Data");
    return null;
  }
}
