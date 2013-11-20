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
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.PersonCollection;
import de.free_creations.actions.CheckedAction;
import static de.free_creations.actions.CheckedAction.Severity.*;
import java.util.logging.Logger;

/**
 * This rule set the value of the field Person.gewuenschterkollege. Rules:
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
public class AbstractRule implements CheckedAction {

  private static final Logger logger = Logger.getLogger(AbstractRule.class.getName());
  private final Person selfP;
  private final Person newMember;
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
  protected AbstractRule(Integer self, Integer groupLeaderId, EntityCollection<Person, Integer> personCollection) throws DataBaseNotReadyException {
    this(personCollection.findEntity(self), personCollection.findEntity(groupLeaderId), 0);
  }

  protected AbstractRule(Person selfP, Person newMember, int recursionDepth) {
    this.selfP = selfP;
    this.newMember = newMember;
    this.recursionDepth = recursionDepth;

    this.level = irrecoverable; // preset in case something goes wrong
    problemDescription = "unknown";
    proposedSolution = "unknown";
    setup();
  }

  public static AbstractRule addGroupMember(Integer self, Integer newMemberId) throws DataBaseNotReadyException {
    PersonCollection pp = Manager.getPersonCollection();
    return new AbstractRule(self, newMemberId, pp);
  }

  /**
   * Factory method which permits to setup with a different personCollection.
   * This method is used for tests.
   *
   * @param self
   * @param newMemberId
   * @param pp
   * @return
   */
  static AbstractRule addGroupMember(Integer self, Integer newMemberId, EntityCollection<Person, Integer> pp) throws DataBaseNotReadyException {
    return new AbstractRule(self, newMemberId, pp);
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
   *
   * @return false if the OK case could not be fulfilled.
   */
  private boolean okCase() {
    assert (selfP != null);

    return false;

  }

  /**
   * tries to fulfill the recoverable case.
   *
   * @return false if the recoverable case could not be fulfilled.
   */
  private boolean recoverableCase() {

    return false;
  }

  /**
   * sets the rule as being impossible to fulfill.
   */
  private void irrecoverableCase() {
    // as a last resort delete the group
    level = irrecoverable;
    
    problemDescription = "the rule is not implemented yet";

    applyer = new Runnable() {
      @Override
      public void run() {

        
      }
    };
  }
}
