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

import de.free_creations.dbEntities.Personen;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.EntityCollection;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.PersonCollection;
import de.free_creations.actions.CheckedAction;
import static de.free_creations.actions.CheckedAction.Severity.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * This rule set the value of the field Personen.gewuenschterkollege. Rules:
 *
 * OK => the new groupLeader is not member of an other group AND self is not a
 * group-leader.
 *
 * Recoverable => if the new groupLeader is member of an other group, propose to
 * attach self to this other group.
 *
 * Recoverable => if "self" is a group-leader by himself, propose to merge all
 * his group members to the new group (if the new group-leader is null the group
 * will be dissolved!)
 *
 * Irrecoverable => if the proposed reconfiguration would again introduce a
 * problem.
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class SetGroupleaderRule implements CheckedAction {

  private static final Logger logger = Logger.getLogger(SetGroupleaderRule.class.getName());
  private final Personen selfP;
  private final Personen leaderP;
  private Severity level;
  private String problemDescription;
  private String proposedSolution;
  private Runnable applyer;
  private final int recursionDepth;

  /**
   * A constructor that permits to pass a different PersonCollection.
   *
   * This constructor is manly used for tests.
   *
   * @param self
   * @param groupLeaderId
   * @param personCollection
   * @throws DataBaseNotReadyException
   */
  protected SetGroupleaderRule(Integer self, Integer groupLeaderId, EntityCollection<Personen, Integer> personCollection) throws DataBaseNotReadyException {
    this(personCollection.findEntity(self), personCollection.findEntity(groupLeaderId), 0);
  }

  protected SetGroupleaderRule(Personen selfP, Personen groupLeaderP, int recursionDepth) {
    this.selfP = selfP;
    this.leaderP = groupLeaderP;
    this.recursionDepth = recursionDepth;

    this.level = irrecoverable; // preset in case something goes wrong
    problemDescription = "unknown";
    proposedSolution = "unknown";
    setup();
  }

  public static SetGroupleaderRule setGroupleader(Integer self, Integer groupLeaderId) throws DataBaseNotReadyException {
    PersonCollection pp = Manager.getPersonCollection();
    return new SetGroupleaderRule(self, groupLeaderId, pp);
  }

  /**
   * Factory method which permits to setup with a different personCollection.
   * This method is used for tests.
   *
   * @param self
   * @param groupLeaderId
   * @param pp
   * @return
   */
  static SetGroupleaderRule setGroupleader(Integer self, Integer groupLeaderId, EntityCollection<Personen, Integer> pp) throws DataBaseNotReadyException {
    return new SetGroupleaderRule(self, groupLeaderId, pp);
  }

  @Override
  public Severity level() {
    return this.level;
  }

  @Override
  public String problemDescription() {
    return this.problemDescription;
  }

  @Override
  public String proposedSolution() {
    return this.proposedSolution;
  }

  @Override
  public void apply() {
    // if (level() != irrecoverable) {
    if (applyer != null) {
      applyer.run();
    }
    //}
  }

  private void setup() {
    if (!okCase()) {
      if (!recoverableCase()) {
        irrecoverableCase();
      }
    }






  }

  /**
   * tries to fulfill the OK case.
   *
   * OK => the new groupLeader is not member of an other group AND self is not a
   * group-leader.
   *
   * @return false if the OK case could not be fulfilled.
   */
  private boolean okCase() {
    assert (selfP != null);
    //---
    // is the new groupLeader member of an other group?
    Personen leadersLeader = (leaderP == null) ? null : leaderP.getGewuenschterkollege();

    if (leadersLeader != null) {
      if (!leadersLeader.equals(leaderP)) {
        problemDescription = String.format("\"%s\" is member of \"group %s\"", leaderP, leadersLeader);
        return false;
      }
    }
    //---
    // is "self" a group-leader by his own?
    List<Personen> groupList = selfP.getGroupList();
    if (groupList != null) {
      if (groupList.isEmpty()) {
        // OK, self has no group list, so it is for sure not
        // a group representant. We can proceed below.
      } else {
        // Hmm, self has a group list. But maybe we want to 
        // to reassign the existing group to his leader, this would be OK.
        if (groupList.size() > 1) {
          for (Personen member : groupList) {
            if (!Objects.equals(member.getGewuenschterkollege(), leaderP)) {
              problemDescription = String.format("\"%s\" is a group-representative", selfP);
              return false; // self is truely a group leader
            }
          }
        } else {
          if (!groupList.get(0).equals(selfP)) {
            problemDescription = String.format("\"%s\" is a group-representative", selfP);
            return false; // self is a strange a group leader
          }
        }
      }
    }

    problemDescription = "none";
    proposedSolution = String.format("Assign \"%s\" to \"group %s\"", selfP, leaderP);
    level = ok;
    applyer = new Runnable() {
      @Override
      public void run() {
        selfP.setGewuenschterkollege(leaderP);
        if (leaderP != null) {
          leaderP.setGewuenschterkollege(leaderP);
        }
      }
    };
    return true;

  }

  /**
   * tries to fulfill the recoverable case.
   *
   * @return false if the recoverable case could not be fulfilled.
   */
  private boolean recoverableCase() {
    //---
    // is the new groupLeader member of an other group?
    Personen leadersLeader = (leaderP == null) ? null : leaderP.getGewuenschterkollege();

    if (leadersLeader != null) {
      if (!leadersLeader.equals(leaderP)) {
        // try to use the leader's leader as new group leader
        if (recursionDepth > 1) {
          problemDescription = String.format("Tried to assign \"%s\" to \"group %s\". But ended in a cycle", selfP, leadersLeader);
          return false;
        }
        SetGroupleaderRule alternativeAssignment = new SetGroupleaderRule(selfP, leadersLeader, recursionDepth + 1);
        if (alternativeAssignment.level == ok) {
          // alternative asignement is OK, use it.
          if (Objects.equals(selfP, leadersLeader)) {
            problemDescription = String.format("\"%s\" is already in a group with \"%s\"", selfP, leaderP);
            return false;
          } else {
            proposedSolution = String.format("Assign \"%s\" to \"group %s\"", selfP, leadersLeader);
          }
          level = recoverable;
          applyer = alternativeAssignment.applyer;
          return true;
        } else {
          // alternative assignment is not OK, abandon.
          return false;
        }
      }
    }
    //---
    // is "self" a group-leader by him-self?
    final List<Personen> groupList = selfP.getGroupList();
    if (groupList != null) {
      if (!groupList.isEmpty()) {
        if (recursionDepth > 1) {
          problemDescription = problemDescription + String.format(
                  " Tried to move all members "
                  + "of \"group %s\" to \"group %s\". Ending in a cycle",
                  selfP, leaderP);
          proposedSolution = String.format("Disolve (delete) \"group %s\"", selfP);
          return false;
        }

        // check if all members can be reassigned
        final ArrayList<SetGroupleaderRule> toDoList = new ArrayList<>();
        for (Personen p : groupList) {
          if (!p.equals(selfP)) {

            SetGroupleaderRule moveTo = new SetGroupleaderRule(p, leaderP, recursionDepth + 1);
            if (moveTo.level == ok) {
              toDoList.add(moveTo);
            } else {
              problemDescription = problemDescription + String.format(
                      " Tried to move all members "
                      + "of \"group %s\" to \"group %s\". But: %s",
                      selfP, leaderP, moveTo.problemDescription);

              return false;
            }
          }
        }
        if (leaderP == null) {
          proposedSolution = String.format("Disolve (delete) \"group %s\"", selfP);
        } else {
          proposedSolution = String.format("Move all members of \"group %s\" to \"group %s\"", selfP, leaderP);
        }
        level = recoverable;
        applyer = new Runnable() {
          @Override
          public void run() {
            for (SetGroupleaderRule moveTo : toDoList) {
              moveTo.apply();
            }
            selfP.setGewuenschterkollege(leaderP);
            if (leaderP != null) {
              leaderP.setGewuenschterkollege(leaderP);
            }
          }
        };
        return true;

      }
    }
    return false;
  }

  /**
   * sets the rule as being impossible to fulfill.
   */
  private void irrecoverableCase() {
    // as a last resort delete the group
    level = irrecoverable;
    Personen groupRepresentative = (selfP.getGewuenschterkollege() == null)
            ? selfP : selfP.getGewuenschterkollege();

    proposedSolution = String.format("Disolve (delete) \"group %s\"", groupRepresentative);

    applyer = new Runnable() {
      @Override
      public void run() {
        selfP.setGewuenschterkollege(null);
        // before we can iterate through the groupList we'll have to copy it into new array
        List<Personen> groupList = selfP.getGroupList();
        Personen[] groupListSave = new Personen[groupList.size()];
        groupListSave = groupList.toArray(groupListSave);
        for (Personen p : groupListSave) {
          p.setGewuenschterkollege(null);
        }
      }
    };
  }
}
