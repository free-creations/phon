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

import de.free_creations.actions.automaticAllocation.AutomaticAllocationExecutor.Task;
import de.free_creations.actions.rating.AllocationRating;
import de.free_creations.dbEntities.Person;
import de.free_creations.nbPhonAPI.Manager;
import java.util.SortedSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AutomaticAllocationExecutorTest {

  public AutomaticAllocationExecutorTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
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
   * Test of doNext method, of class AutomaticAllocationExecutor.
   */
  @Test
  public void testDoNext() throws Exception {

  }

  /**
   * Test of getProgress method, of class AutomaticAllocationExecutor.
   */
  @Test
  public void testGetProgress() {

  }

  /**
   * Test of setProgress method, of class AutomaticAllocationExecutor.
   */
  @Test
  public void testSetProgress() {

  }

  /**
   * Test of collectOpenJobs method, of class AutomaticAllocationExecutor.
   */
  @Test
  public void testCollectOpenJobs() {
    System.out.println("testCollectOpenJobs ");
    AutomaticAllocationExecutor exec = new AutomaticAllocationExecutor(false);
    // the statement we want to test...
    SortedSet<Task> openTasks = exec.collectOpenJobs();
    // if somebody has run the automatic allocation and has commited threr wont be any open tasks
    assertNotNull("Bad test data?",openTasks);
    int openJobCount = openTasks.size();

    // let's verify whether openJobCount is plausible
    int allocCount = Manager.getAllocationCollection().getAll().size();
    int jobCount = Manager.getJobCollection().getAll().size();
    int eventCount = Manager.getEventCollection().getAll().size();

    // the maximum possible of open jobs
    int openJobMax = (eventCount * jobCount) - allocCount;
    assertTrue(openJobCount <= openJobMax);

    // we assume that the testdata have more than half of the events scheduled.
    int openJobMin = openJobMax / 2;
    assertTrue("Bad test data?", openJobCount > openJobMin);

    // we assume that there is at least one teacher-job in the testdata
    int teacherCount = 0;
    for (Task oj : openTasks) {
      if (oj.isTeacher()) {
        teacherCount++;
      }
    }
    assertTrue("Bad test data?", teacherCount > 0);

    // teachers-jobs must be first in the set
    assertTrue("Bad test data?", openTasks.first().isTeacher());

    // all teachers must be in a sequence
    boolean expectingTeacher = true;
    for (Task oj : openTasks) {
      if (oj.isTeacher()) {
        assertTrue(expectingTeacher);
      } else {
        expectingTeacher = false;
      }
    }
  }

  /**
   * Test of findBestMatch method, of class AutomaticAllocationExecutor.
   */
  @Test
  public void testFindBestMatch() {
    System.out.println("findBestMatch");

    AutomaticAllocationExecutor exec = new AutomaticAllocationExecutor(false);
    SortedSet<Task> openJobs = exec.collectOpenJobs();
    assertFalse(openJobs.isEmpty());
    Task openJob = openJobs.first();

    // the statement we want to test...
    Person bestMatch = exec.findBestMatch(openJob);

    assertNotNull("Bad test data?", bestMatch);

    Integer personId = bestMatch.getPersonId();
    Integer eventId = openJob.event.getEventId();
    String jobId = openJob.job.getJobId();

    AllocationRating eval = new AllocationRating(personId, eventId, jobId);
    int score = eval.getScore();
    System.out.println("... score = " + score);

  }

}
