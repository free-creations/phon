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
package de.free_creations.actions;

/**
 * A checked-action is an action than performs some verifications before
 * performing the change.
 *
 * A checked-action that might not be possible can propose some alternative
 * action.
 *
 * Interface implemented by all rules.
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public interface CheckedAction {

  public enum Severity {

    /**
     * OK means: the change can be applied without problems.
     *
     * The functions problemDescription() and proposedSolution() will return
     * empty strings.
     *
     * The function apply() will apply the change.
     */
    ok,
    /**
     * Recoverable means: the change cannot be applied exactly as the user
     * wants, but there is a similar solution.
     *
     * The functions problemDescription() will describe why the change cannot be
     * applied.
     *
     * The function proposedSolution() will describe workarounds which might be
     * applied instead.
     *
     * The function apply() will apply the the proposed solution.
     */
    recoverable,
    /**
     * Irrecoverable means: the change cannot be applied and there is no
     * workaround.
     *
     * The functions problemDescription() will describe why the change cannot be
     * applied.
     *
     * The function apply() will return without doing anything.
     */
    irrecoverable
  }

  /**
   *
   * @return the level of the problem if any.
   */
  public Severity level();

  /**
   *
   * @return a description why the requested change cannot be performed.
   */
  public String problemDescription();

  /**
   *
   * @return a description of what could be done instead of the requested
   * change.
   */
  public String[] proposedSolution();

  public int workaroundCount();

  /**
   * Applies the requested change or a workaround.
   *
   * If the requested change is impossible to fulfill the function does nothing.
   *
   * @param workaroundIdx the index of the workaround. If the requested change
   * was OK the index is ignored.
   * @throws java.lang.Exception
   */
  public void apply(int workaroundIdx) throws Exception;
}
