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
package de.free_creations.actions.person;

import de.free_creations.dbEntities.Person;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.EntityCollection;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static de.free_creations.actions.person.SetGroupleaderRule.setGroupleader;
import static de.free_creations.actions.CheckedAction.Severity.*;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class SetGroupleaderRuleTest {

  private class CollectionMock implements EntityCollection<Person, Integer> {

    public final Person[] items = new Person[]{
      new Person(0),//<- a virgin person
      new Person(1),//<- a virgin person
      new Person(2),//<- a normal group representant
      new Person(3),//<- a member in the group of item[2]
      new Person(4),//<- a slightly invalid group representative
      new Person(5),//<- a member in the group of item[4]
      new Person(6),//<- a member in the group of item[4]

      new Person(7),//<- an invalid group representative (cyclic with 8)
      new Person(8),//<- an invalid group representative (cyclic with 7)
    };

    public CollectionMock() {
      // item 2 is a groupleader as it should be (references to itself)
      items[2].setGewuenschterkollege(items[2]);
      // item 3 is a group member in group of item 2
      items[3].setGewuenschterkollege(items[2]);
      // items 5 and 6 are a group members in group of item 4
      items[5].setGewuenschterkollege(items[4]);
      items[6].setGewuenschterkollege(items[4]);

      // items 7 and 8 are cyclic
      items[7].setGewuenschterkollege(items[8]);
      items[8].setGewuenschterkollege(items[7]);
    }

    @Override
    public List<Person> getAll() {
      return Arrays.asList(items);
    }

    @Override
    public Person findEntity(Integer key) throws DataBaseNotReadyException {
      if (key == null) {
        return null;
      }
      if (key < 0) {
        return null;
      }
      if (key >= items.length) {
        return null;
      }
      return items[key];
    }
  }
  CollectionMock pp;

  public SetGroupleaderRuleTest() {
  }

  @Before
  public void setUp() {
    // setup a fresh collection for each test
    pp = new CollectionMock();
  }

  /**
   * OK => the new groupLeader is not member of an other group AND self is not a
   * group-leader.
   */
  @Test
  public void okCase_Test_1() throws DataBaseNotReadyException {
    System.out.println("okCase_Test_1");
    //test with virgin group-member and virgin group-leader.
    SetGroupleaderRule setGroupleader = setGroupleader(0, 1, pp);
    System.out.println("...Problem:  " + setGroupleader.problemDescription());
    System.out.println("...Solution: " + setGroupleader.proposedSolution());
    assertEquals(ok, setGroupleader.level());

    setGroupleader.apply();
    assertEquals(pp.items[1], pp.items[0].getGewuenschterkollege());
    assertEquals(pp.items[1], pp.items[1].getGewuenschterkollege());
    assertTrue(pp.items[1].getGroupList().contains(pp.items[0]));

    // make shure that undo works as well
    SetGroupleaderRule removeGroupleader = setGroupleader(0, null, pp);
    System.out.println("...Problem:  " + removeGroupleader.problemDescription());
    System.out.println("...Solution: " + removeGroupleader.proposedSolution());
    assertEquals(ok, removeGroupleader.level());


    removeGroupleader.apply();
    assertEquals(null, pp.items[0].getGewuenschterkollege());
    assertEquals(pp.items[1], pp.items[1].getGewuenschterkollege());
    assertFalse(pp.items[1].getGroupList().contains(pp.items[0]));

  }

  /**
   * OK => the new groupLeader is not member of an other group AND self is not a
   * group-leader.
   */
  @Test
  public void okCase_Test_2() throws DataBaseNotReadyException {
    System.out.println("okCase_Test_2");
    //test with virgin group-member and initilized group-leader.
    SetGroupleaderRule setGroupleader = setGroupleader(0, 2, pp);
    System.out.println("...Problem:  " + setGroupleader.problemDescription());
    System.out.println("...Solution: " + setGroupleader.proposedSolution());
    assertEquals(ok, setGroupleader.level());

    setGroupleader.apply();
    assertEquals(pp.items[2], pp.items[0].getGewuenschterkollege());
    assertEquals(pp.items[2], pp.items[2].getGewuenschterkollege());
    assertTrue(pp.items[2].getGroupList().contains(pp.items[0]));

    // make shure that undo works as well
    SetGroupleaderRule removeGroupleader = setGroupleader(0, null, pp);
    System.out.println("...Problem:  " + removeGroupleader.problemDescription());
    System.out.println("...Solution: " + removeGroupleader.proposedSolution());
    assertEquals(ok, removeGroupleader.level());

    removeGroupleader.apply();
    assertEquals(null, pp.items[0].getGewuenschterkollege());
    assertFalse(pp.items[2].getGroupList().contains(pp.items[0]));
  }

  /**
   * OK => the new groupLeader is not member of an other group AND self is not a
   * group-leader.
   */
  @Test
  public void okCase_Test_3() throws DataBaseNotReadyException {
    System.out.println("okCase_Test_3");
    //test to set an item to be its own group-leader.
    SetGroupleaderRule setGroupleader = setGroupleader(0, 0, pp);
    System.out.println("...Problem:  " + setGroupleader.problemDescription());
    System.out.println("...Solution: " + setGroupleader.proposedSolution());
    assertEquals(ok, setGroupleader.level());

    setGroupleader.apply();
    assertEquals(pp.items[0], pp.items[0].getGewuenschterkollege());
    assertTrue(pp.items[0].getGroupList().contains(pp.items[0]));

    // make shure that undo works as well
    SetGroupleaderRule removeGroupleader = setGroupleader(0, null, pp);
    System.out.println("...Problem:  " + removeGroupleader.problemDescription());
    System.out.println("...Solution: " + removeGroupleader.proposedSolution());
    assertEquals(ok, removeGroupleader.level());

    removeGroupleader.apply();
    assertEquals(null, pp.items[0].getGewuenschterkollege());
    assertFalse(pp.items[0].getGroupList().contains(pp.items[0]));
  }

  /**
   * OK => it is also OK the new groupLeader is member of his own group although
   * self is not a is not yet marked as his own group-representative.
   */
  @Test
  public void okCase_Test_4() throws DataBaseNotReadyException {
    System.out.println("okCase_Test_4");
    //test to set an item to be its own group-leader.
    SetGroupleaderRule setGroupleader = setGroupleader(4, 4, pp);
    System.out.println("...Problem:  " + setGroupleader.problemDescription());
    System.out.println("...Solution: " + setGroupleader.proposedSolution());
    assertEquals(ok, setGroupleader.level());

    setGroupleader.apply();
    assertEquals(pp.items[4], pp.items[4].getGewuenschterkollege());
    assertEquals(pp.items[4], pp.items[5].getGewuenschterkollege());
    assertEquals(pp.items[4], pp.items[6].getGewuenschterkollege());


    // there no "undo" for this, but we can try to dissove the group.
    SetGroupleaderRule removeGroupleader = setGroupleader(4, null, pp);
    System.out.println("...Problem:  " + removeGroupleader.problemDescription());
    System.out.println("...Solution: " + removeGroupleader.proposedSolution());
    assertEquals(recoverable, removeGroupleader.level());

    removeGroupleader.apply();
    assertEquals(null, pp.items[4].getGewuenschterkollege());
    assertEquals(null, pp.items[5].getGewuenschterkollege());
    assertEquals(null, pp.items[6].getGewuenschterkollege());

  }

  /**
   * Recoverable => if the new groupLeader is member of an other group, propose
   * to attach "self" to this other group.
   */
  @Test
  public void recoverableCase_Test_1() throws DataBaseNotReadyException {
    System.out.println("recoverableCase_Test_1");
    // the new group leader (item 3) is member of item_2's group.
    SetGroupleaderRule setGroupleader = setGroupleader(1, 3, pp);
    System.out.println("...Problem:  " + setGroupleader.problemDescription());
    System.out.println("...Solution: " + setGroupleader.proposedSolution());
    assertEquals(recoverable, setGroupleader.level());

    setGroupleader.apply();
    assertEquals(pp.items[2], pp.items[1].getGewuenschterkollege());
    assertTrue(pp.items[2].getGroupList().contains(pp.items[1]));

    // make shure that undo works as well
    SetGroupleaderRule removeGroupleader = setGroupleader(1, null, pp);
    System.out.println("...Problem:  " + removeGroupleader.problemDescription());
    System.out.println("...Solution: " + removeGroupleader.proposedSolution());
    assertEquals(ok, removeGroupleader.level());

    removeGroupleader.apply();
    assertEquals(null, pp.items[1].getGewuenschterkollege());
    assertFalse(pp.items[2].getGroupList().contains(pp.items[1]));
  }

  /**
   * Recoverable => if self is a group leader by his own, propose to move all
   * his group members to the new group.
   */
  @Test
  public void recoverableCase_Test_2() throws DataBaseNotReadyException {
    System.out.println("recoverableCase_Test_2");
    // item_2 is a group leader, try to move to item_1
    SetGroupleaderRule setGroupleader = setGroupleader(2, 1, pp);
    System.out.println("...Problem:  " + setGroupleader.problemDescription());
    System.out.println("...Solution: " + setGroupleader.proposedSolution());
    assertEquals(recoverable, setGroupleader.level());

    setGroupleader.apply();
    assertEquals(pp.items[1], pp.items[2].getGewuenschterkollege());
    assertEquals(pp.items[1], pp.items[3].getGewuenschterkollege());
    assertEquals(pp.items[1], pp.items[1].getGewuenschterkollege());
  }

  /**
   * Recoverable => if self is a group leader by his own, propose to move all
   * his group members to the new group.
   */
  @Test
  public void recoverableCase_Test_3() throws DataBaseNotReadyException {
    System.out.println("recoverableCase_Test_3");
    // item_2 is a group leader, try set his group leader to null (removing the group)
    SetGroupleaderRule setGroupleader = setGroupleader(2, null, pp);
    System.out.println("...Problem:  " + setGroupleader.problemDescription());
    System.out.println("...Solution: " + setGroupleader.proposedSolution());
    assertEquals(recoverable, setGroupleader.level());

    setGroupleader.apply();
    assertEquals(null, pp.items[2].getGewuenschterkollege());
    assertEquals(null, pp.items[3].getGewuenschterkollege());

  }

  /**
   * Recoverable => if self is a group leader by his own, propose to move all
   * his group members to the new group.
   */
  @Test
  public void irrecoverableCase_Test_1() throws DataBaseNotReadyException {
    System.out.println("irrecoverableCase_Test_1");
    // item_2 is a group leader, try set his group leader to item 3 (who member of item 2)
    SetGroupleaderRule setGroupleader = setGroupleader(2, 3, pp);
    System.out.println("...Problem:  " + setGroupleader.problemDescription());
    System.out.println("...Solution: " + setGroupleader.proposedSolution());
    assertEquals(irrecoverable, setGroupleader.level());

    setGroupleader.apply();
    assertEquals(null, pp.items[2].getGewuenschterkollege());
    assertEquals(null, pp.items[3].getGewuenschterkollege());
  }
    /**
   * Irrecoverable => if the solution gets cyclic.
   */
  @Test
  public void irrecoverableCase_Test_2() throws DataBaseNotReadyException {
    System.out.println("irrecoverableCase_Test_2");
    // try to remove a member of a cyclic group
    SetGroupleaderRule setGroupleader = setGroupleader(7, null, pp);
    System.out.println("...Problem:  " + setGroupleader.problemDescription());
    System.out.println("...Solution: " + setGroupleader.proposedSolution());
    assertEquals(irrecoverable, setGroupleader.level());

    setGroupleader.apply();
    assertEquals(null, pp.items[7].getGewuenschterkollege());
    assertEquals(null, pp.items[8].getGewuenschterkollege());
  }
}