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
package de.free_creations.nbPhonAPI;

import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.TimeSlot;
import java.awt.EventQueue;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class AllocationCollection implements MutableEntityCollection<Allocation, Long> {

  private static final Logger logger = Logger.getLogger(AllocationCollection.class.getName());

  private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  protected AllocationCollection() {
  }

  @Override
  public List<Allocation> getAll() {
    synchronized (Manager.databaseAccessLock) {
      try {
        Manager.ping();
        EntityManager entityManager = Manager.getEntityManager();
        TypedQuery<Allocation> query = entityManager.createNamedQuery("Allocation.findAll", Allocation.class);
        List<Allocation> ll = query.getResultList();
        return ll;
      } catch (DataBaseNotReadyException | ConnectionLostException ignored) {
        return Collections.emptyList();
      }
    }
  }

  /**
   * Returns a list of all automatically generated allocations.
   * @return 
   */
  public List<Allocation> getAllAutomatic() {
    synchronized (Manager.databaseAccessLock) {
      try {
        Manager.ping();
        EntityManager entityManager = Manager.getEntityManager();
        TypedQuery<Allocation> query = entityManager.createNamedQuery("Allocation.findAll", Allocation.class);
        List<Allocation> ll = query.getResultList();
        
        ArrayList<Allocation> result = new ArrayList<>();
        for(Allocation a : ll){
          if(Allocation.PLANNER_AUTOMAT.equals(a.getPlanner())){
            result.add(a);
          }
        }
        
        return result;
      } catch (DataBaseNotReadyException | ConnectionLostException ignored) {
        return Collections.emptyList();
      }
    }
  }

  @Override
  public Allocation findEntity(Long key) throws DataBaseNotReadyException {
    if (key == null) {
      return null;
    }
    synchronized (Manager.databaseAccessLock) {
      return Manager.getEntityManager().find(Allocation.class, key);
    }
  }

  /**
   * Searches the allocation-record for a given person and a given time-slot. If
   * (by error) more than one such records exists, the first one is returned and
   * a warning is logged.
   *
   * @param p the person
   * @param t the time-slot
   * @return the searched record or null if no such record exists.
   * @throws DataBaseNotReadyException
   */
  public Allocation findEntity(Person p, TimeSlot t) throws DataBaseNotReadyException {

    List<Allocation> resultList = findAll(p, t);
    assert (resultList != null);
    if (resultList.isEmpty()) {
      return null;
    }
    if (resultList.size() > 1) {
      logger.log(Level.WARNING, "More than one allocation for {0} at time {1}.", new Object[]{p, t});
    }
    return resultList.get(0);

  }

  /**
   * Searches all allocation-records for a given person and a given time-slot.
   *
   * If fact, one person can only have zero or one allocations at the same time;
   * but the database structure does not prohibit such miss-allocations,
   * therefore the whole list is returned.
   *
   * @param p the person
   * @param t the time-slot
   * @return a list of all records fulfilling the search criteria.
   * @throws DataBaseNotReadyException
   */
  public List<Allocation> findAll(Person p, TimeSlot t) throws DataBaseNotReadyException {
    if (p == null) {
      return Collections.emptyList();
    }
    if (t == null) {
      return Collections.emptyList();
    }
    synchronized (Manager.databaseAccessLock) {
      ArrayList<Allocation> result = new ArrayList<>();
      List<Allocation> personsAllocs = p.getAllocationList();
      for (Allocation a : personsAllocs) {
        Event event = a.getEvent();
        assert (event != null);
        TimeSlot timeSlot = event.getTimeSlot();
        if (Objects.equals(timeSlot, t)) {
          result.add(a);
        }
      }
      return result;
    }
  }

  public List<Allocation> findAllSlow(Person p, TimeSlot t) throws DataBaseNotReadyException {
    synchronized (Manager.databaseAccessLock) {
      TypedQuery<Allocation> query = Manager.getEntityManager().createNamedQuery("Allocation.findByPersonAndTimeslot", Allocation.class);
      query.setParameter("timeSlot", t);
      query.setParameter("person", p);
      List<Allocation> resultList = query.getResultList();
      assert (resultList != null);
      return resultList;
    }
  }

  /**
   * Searches the allocation-record for a given event and a given job. If (by
   * error) more than one such records exists, the first one is returned and a
   * warning is logged.
   *
   * @param e the event
   * @param j the job
   * @return the searched record or null if no such record exists.
   * @throws DataBaseNotReadyException
   */
  public Allocation findEntity(Event e, Job j) throws DataBaseNotReadyException {

    List<Allocation> resultList = findAll(e, j);
    assert (resultList != null);
    if (resultList.isEmpty()) {
      return null;
    }
    if (resultList.size() > 1) {
      logger.log(Level.WARNING, "More than one allocation for event {0} and job {1}.", new Object[]{e, j});
    }
    return resultList.get(0);

  }

  /**
   * Searches all allocation-records for a given event and a given job.
   *
   * If fact, for a given event and a given job there should be at maximum one
   * allocation; but the database structure does not prohibit such
   * miss-allocations, therefore the whole list is returned.
   *
   * @param e the event
   * @param j the job
   * @return a list of all records fulfilling the search criteria.
   * @throws DataBaseNotReadyException
   */
  public List<Allocation> findAll(Event e, Job j) throws DataBaseNotReadyException {
    if (e == null) {
      return Collections.emptyList();
    }
    if (j == null) {
      return Collections.emptyList();
    }
    synchronized (Manager.databaseAccessLock) {
      ArrayList<Allocation> result = new ArrayList<>();
      List<Allocation> eventsAllocs = e.getAllocationList();
      for (Allocation a : eventsAllocs) {
        Job job = a.getJob();
        assert (job != null);
        if (job.equals(j)) {
          result.add(a);
        }
      }

      return result;
    }
  }

  public List<Allocation> findAllSlow(Event e, Job j) throws DataBaseNotReadyException {
    synchronized (Manager.databaseAccessLock) {
      TypedQuery<Allocation> query = Manager.getEntityManager().createNamedQuery("Allocation.findByEventAndJob", Allocation.class);
      query.setParameter("event", e);
      query.setParameter("job", j);
      List<Allocation> resultList = query.getResultList();
      assert (resultList != null);
      return resultList;
    }
  }

  @Override
  @Deprecated
  public Allocation newEntity() throws DataBaseNotReadyException {
    /**
     * @ToDo remove this signature from MutableEntityCollection *
     */
    throw new UnsupportedOperationException("Not supported. Use newEntity(Person, Event, Job)"); //ToDo: remove 

  }

  public Allocation newEntity(Person person, Event event, Job job, String planner) throws DataBaseNotReadyException {
    Allocation newAllocation;
    synchronized (Manager.databaseAccessLock) {
      EntityManager entityManager = Manager.getEntityManager();
      newAllocation = Allocation.newAllocation(entityManager, person, event, job, planner);

    }
    firePropertyChange(PROP_ITEM_ADDED, null, newAllocation.identity());
    return newAllocation;
  }

  @Override
  public void removeEntity(Long key) throws DataBaseNotReadyException {
    removeEntity(findEntity(key));
  }

  /**
   * Remove the given allocation from the database.
   *
   * The database will be flushed after each removal.
   *
   * @param allocation
   * @throws DataBaseNotReadyException
   */
  public void removeEntity(Allocation allocation) throws DataBaseNotReadyException {
    if (allocation == null) {
      return;
    }
    synchronized (Manager.databaseAccessLock) {
      EntityManager entityManager = Manager.getEntityManager();
      allocation.remove(entityManager);
    }
    firePropertyChange(PROP_ITEM_REMOVED, allocation.identity(), null);
  }

  /**
   * Remove a list of allocations from the database.
   *
   * The database will be flushed only when the whole list has been deleted.
   *
   * @param allocactions
   * @throws DataBaseNotReadyException
   */
  public void removeAll(List<Allocation> allocactions) throws DataBaseNotReadyException {
    synchronized (Manager.databaseAccessLock) {
      EntityManager entityManager = Manager.getEntityManager();
      for (Allocation a : allocactions) {
        a.remove(entityManager, true);
      }
      entityManager.flush();
    }
    firePropertyChange(PROP_ITEM_LIST_CHANGED, null, null);
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  private void firePropertyChange(final String propertyName,
          final Object oldValue,
          final Object newValue) {
    if (EventQueue.isDispatchThread()) {
      propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    } else {
      EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
          propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
      });
    }
  }

}
