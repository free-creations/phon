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

package de.free_creations.actions.rating;

import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Person;
import de.free_creations.nbPhonAPI.Manager;
import java.util.List;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AllocationPerformance {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws InterruptedException {
    AllocationPerformance n = new AllocationPerformance();
    n.testGetScore(80);
  }


   public void testGetScore(int maxEvents) {
    System.out.println("getScore");
    List<Job> allJobs = Manager.getJobCollection().getAll();
    List<Person> allPersons = Manager.getPersonCollection().getAll();
    List<Event> allEvents = Manager.getEventCollection().getAll();
    // Event e = allEvents.get(0);
    int count = 0;
    for (Event e : allEvents) {
      for (Job j : allJobs) {
        for (Person p : allPersons) {
          AllocationRating rating = new AllocationRating(p.getPersonId(), e.getEventId(), j.getJobId());
          rating.getScore();
        }
      }

      count++;

      
    }
          System.out.println("Total of "+count +" events processed");

  }

  
}
