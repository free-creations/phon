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

import de.free_creations.actions.event.AllocatePersonForEvent;
import de.free_creations.actions.rating.AllocationRating;
import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AutomaticAllocationExecutor {

  static final private Logger logger = Logger.getLogger(AutomaticAllocationExecutor.class.getName());
  private String processQuality = "";

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

  /**
   * Combination of a job and the event when the job should done.
   */
  protected class Task implements Comparable<Task> {

    public final Job job;
    public final Event event;

    public Task(Job job, Event event) {
      this.job = job;
      this.event = event;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 13 * hash + Objects.hashCode(this.job);
      hash = 13 * hash + Objects.hashCode(this.event);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Task other = (Task) obj;
      if (!Objects.equals(this.job, other.job)) {
        return false;
      }
      if (!Objects.equals(this.event, other.event)) {
        return false;
      }
      return true;
    }

    /**
     * The sort order determines in which order the free tasks are allocated.
     *
     * @param other
     * @return
     */
    @Override
    public int compareTo(Task other) {

      // sort on contest priority
      Contest thisContest = event.getContest();
      Contest otherContest = other.event.getContest();
      int thisContestPrio = (thisContest.getPriority() == null) ? 0 : thisContest.getPriority();
      int otherContestPrio = (otherContest.getPriority() == null) ? 0 : otherContest.getPriority();
      int contestCompare = Integer.compare(thisContestPrio, otherContestPrio);
      if (contestCompare != 0) {
        return contestCompare;
      }

      // sort on job priority
      int jobCompare = Integer.compare(job.getPriority(), other.job.getPriority());
      if (jobCompare != 0) {
        return jobCompare;
      }

      // sort on ascending time
      int timeCompare = Integer.compare(getTimeSlotId(), other.getTimeSlotId());
      if (timeCompare != 0) {
        return timeCompare;
      }
      // finally sort on hash value 
      return Integer.compare(hashCode(), other.hashCode());
    }

    public int getTimeSlotId() {
      if (event != null) {
        TimeSlot timeSlot = event.getTimeSlot();
        if (timeSlot != null) {
          return timeSlot.getTimeSlotId();
        }
      }
      return -1;
    }

    public boolean isTeacher() {
      return "LEHRER".equals(job.getJobId());
    }

  }

  private enum State {

    RemovingAllAllocations,
    RemovingBadAllocations,
    CollectingTasks,
    AllocatingTasks,
    Finished
  }
  private State currentState;
  private ProgressIndicator currentProgress;
  private Iterator<Task> openJobsIterator;
  // these variables help to update the progress indicator
  private double currentProgressRatio = 0D;
  private double progressDelta = 1D;
  private int stepsToDo = 0;
  private int stepsDone = 0;
  private final Object ProgressIndicatorLock = new Object();

  public AutomaticAllocationExecutor(boolean fullReAllocation) {
    if (fullReAllocation) {
      currentState = State.RemovingAllAllocations;
      currentProgress = new ProgressIndicator(0, "Removing all allocations...");
    } else {
      currentState = State.RemovingBadAllocations;
      currentProgress = new ProgressIndicator(0, "Removing bad allocations...");
    }
  }

  /**
   * Execute the next step.
   *
   * @return true if there is more work to be done, false otherwise.
   * @throws Exception
   */
  public boolean doNext() throws Exception {
    switch (currentState) {
      case RemovingAllAllocations:
        removeAllAllocations();
        return true;
      case RemovingBadAllocations:
        removeBadAllocations();
        return true;
      case CollectingTasks:
        collectTasks();
        return true;
      case AllocatingTasks:
        allocateTasks();
        return true;
      case Finished:
        return false;
    }
    return false;
  }

  private void removeAllAllocations() throws DataBaseNotReadyException {
    ArrayList<Allocation> aa = new ArrayList<>(Manager.getAllocationCollection().getAll());
    int allocCount = aa.size();
    Manager.getAllocationCollection().removeAll(aa);
    // prepare next state
    currentState = State.CollectingTasks;
    setProgress(new ProgressIndicator(10, String.format("%s Allocations removed. Collecting Tasks...",allocCount)));
  }

  private void removeBadAllocations() throws DataBaseNotReadyException {
    ArrayList<Allocation> aa = new ArrayList<>(Manager.getAllocationCollection().getAll());
    int allocCount = aa.size();
    Manager.getAllocationCollection().removeAll(aa);
    // prepare next state
    currentState = State.CollectingTasks;
    setProgress(new ProgressIndicator(10, String.format("%s Allocations removed. Collecting Tasks...",allocCount)));
  }

  private void collectTasks() {
    SortedSet<Task> openJobs = collectOpenJobs();
    openJobsIterator = openJobs.iterator();

    // prepare the next state
    stepsToDo = openJobs.size();
    stepsDone = 0;
    currentProgressRatio += 0.1D;
    progressDelta = (1D - currentProgressRatio) / stepsToDo;
    currentState = State.AllocatingTasks;
    setProgress(new ProgressIndicator(
            (int) (currentProgressRatio * 100D),
            String.format("Step %s of %s", stepsDone + 1, stepsToDo)));

  }

  private void allocateTasks() throws DataBaseNotReadyException {
    assert (openJobsIterator != null);
    if (!openJobsIterator.hasNext()) {
      // iterator empty we are finished
      processQuality = "Improved by";
      currentState = State.Finished;
      setProgress(new ProgressIndicator(100, "Finished."));
      return;
    }
    Task next = openJobsIterator.next();
    Person person = findBestMatch(next);
    if (person != null) {
      Integer eventId = next.event.getEventId();
      String jobId = next.job.getJobId();
      Integer personId = person.getPersonId();
      AllocatePersonForEvent alloc = new AllocatePersonForEvent(false, eventId, personId, jobId, Allocation.PLANNER_AUTOMAT);
      try {
        alloc.apply();
      } catch (AllocatePersonForEvent.AllocationException ex) {
        logger.log(Level.SEVERE, "Allocation failed.", ex);
      }
    } else {
      logger.log(Level.INFO, "no person found for {0} and {1}", new Object[]{next.event, next.job});
    }

    // prepare the next step
    stepsDone++;
    currentProgressRatio += progressDelta;
    currentState = State.AllocatingTasks;
    setProgress(new ProgressIndicator(
            (int) (currentProgressRatio * 100D),
            String.format("Step %s of %s ...", stepsDone + 1, stepsToDo)));

  }

  public ProgressIndicator getProgress() {
    synchronized (ProgressIndicatorLock) {
      return currentProgress;
    }
  }

  public void setProgress(ProgressIndicator pi) {
    synchronized (ProgressIndicatorLock) {
      currentProgress = pi;
    }
  }

  public String getProcessQuality() {
    synchronized (ProgressIndicatorLock) {
      return processQuality;
    }
  }

  public void setProgress(String processQuality) {
    synchronized (ProgressIndicatorLock) {
      this.processQuality = processQuality;
    }
  }

  public SortedSet<Task> collectOpenJobs() {
    TreeSet<Task> result = new TreeSet<>();
    List<Event> allEvents = Manager.getEventCollection().getAll();
    List<Job> allJobs = Manager.getJobCollection().getAll();
    for (Event e : allEvents) {
      if (e.isScheduled()) {
        ArrayList<Job> candidateJobs = new ArrayList<>(allJobs);
        List<Allocation> eventsAlloctations = e.getAllocationList();
        for (Allocation a : eventsAlloctations) {
          candidateJobs.remove(a.getJob());
        }
        for (Job openJob : candidateJobs) {
          Task newOpenJob = new Task(openJob, e);
          result.add(newOpenJob);
        }
      }
    }
    return result;
  }

  public Person findBestMatch(Task openJob) {
    Person winner = null;
    int bestScore = Integer.MIN_VALUE;
    Event event = openJob.event;
    Job job = openJob.job;
    List<Person> pp = Manager.getPersonCollection().getAll();
    for (Person p : pp) {
      AllocationRating eval = new AllocationRating(p, event, job);
      if (eval.isRealisable()) {
        int score = eval.getScore();
        if (score > bestScore) {
          bestScore = score;
          winner = p;
        }
      }
    }

    return winner;
  }
}
