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
package de.free_creations.actions.automaticAllocation;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AutomaticAllocationExecutor {

  public static class ProgressIndicator {

    /**
     * percentage of the currently finished work.
     */
    public final int percentFinished;
    /**
     * A message indicating the next step.
     */
    public final String message;

    public ProgressIndicator(int percentFinished, String message) {
      this.percentFinished = percentFinished;
      this.message = message;
    }
  }

  private final boolean fullReAllocation;

  public AutomaticAllocationExecutor(boolean fullReAllocation) {
    this.fullReAllocation = fullReAllocation;
  }

  /**
   * Execute the next step.
   *
   * @return true if there is more work to be done, false otherwise.
   * @throws Exception
   */
  public boolean doNext() throws Exception {
    if (step < 100) {
      System.out.println("starting step:" + step);
      if ((!fullReAllocation) && (step > 50)) {
        throw new Exception("Just for fun.");
      }

      Thread.sleep(100);

      System.out.println("ended step:" + step);
            step++;
      return true;
    } else {
      return false;
    }

  }
  private int step = 0;

  public ProgressIndicator getProgress() {
    return new ProgressIndicator(step, String.format("step %s of 100", step + 1));
  }

}
