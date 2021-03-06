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

import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Person;
import de.free_creations.nbPhonAPI.AllocationCollection;
import de.free_creations.nbPhonAPI.Manager;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AllocationRatingNGTest {

  public AllocationRatingNGTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    if (!Manager.isOpen()) {
      fail("+++ Please start the database server and try again.");
    }

  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    Manager.close(false);
  }

  /**
   * Test of getScore method, of class AllocationRating.
   *
   * Here we test whether the "getScore" method is sufficiently fast to be
   * executed over all persons all jobs and all events.
   */
  @Test
  public void testGetScore() {
    System.out.println("getScore");
    List<Job> allJobs = Manager.getJobCollection().getAll();
    List<Person> allPersons = Manager.getPersonCollection().getAll();
    List<Event> allEvents = Manager.getEventCollection().getAll();
    for (Event e : allEvents) {
      for (Job j : allJobs) {
        for (Person p : allPersons) {
          AllocationRating rating = new AllocationRating(p.getPersonId(), e.getEventId(), j.getJobId());
          rating.getScore();

        }
      }
    }

  }

  /**
   * Test of isRealisable method, of class AllocationRating.
   */
  @Test
  public void testIsRealisable() {
    System.out.println("isRealisable");
  }

  /**
   * Test of isAvailable method, of class AllocationRating.
   */
  @Test
  public void testIsAvailable() {
    System.out.println("isAvailable");

  }

  /**
   * Test of isClashing method, of class AllocationRating.
   */
  @Test
  public void testIsClashing() {
    System.out.println("isClashing");

  }

  /**
   * Test of isVacant method, of class AllocationRating.
   */
  @Test
  public void testIsVacant() {
    System.out.println("isVacant");
    List<Allocation> aa = Manager.getAllocationCollection().getAll();
    assertFalse(aa.isEmpty(), "Bad Test Data?");
    Allocation a = aa.get(0);

    AllocationRating rating = new AllocationRating(a.getPerson(), a.getEvent(), a.getJob());
    assertFalse(rating.isVacant());

  }

  /**
   * Test of isRedundant method, of class AllocationRating.
   */
  @Test
  public void testIsRedundant() {
    System.out.println("isRedundant");

  }

}
