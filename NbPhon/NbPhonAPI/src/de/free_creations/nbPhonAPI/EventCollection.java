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

import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Location;
import de.free_creations.dbEntities.TimeSlot;
import java.awt.EventQueue;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class EventCollection implements MutableEntityCollection<Event, Integer> {

  private static final Logger logger = Logger.getLogger(EventCollection.class.getName());

  private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  protected EventCollection() {
  }

  @Override
  public List<Event> getAll() {
    synchronized (Manager.databaseAccessLock) {
      try {
        Manager.ping();
        EntityManager entityManager = Manager.getEntityManager();
        TypedQuery<Event> query = entityManager.createNamedQuery("Event.findAll", Event.class);
        List<Event> ee = query.getResultList();
        return ee;
      } catch (DataBaseNotReadyException | ConnectionLostException ignored) {
        return Collections.emptyList();
      }
    }
  }

  @Override
  public Event findEntity(Integer key) throws DataBaseNotReadyException {
    if (key == null) {
      return null;
    }
    synchronized (Manager.databaseAccessLock) {
      return Manager.getEntityManager().find(Event.class, key);
    }
  }

  /**
   * Searches the event-record for a given location and a given time-slot. If
   * (by error) more than one such records exists, the first one is returned and
   * a warning is logged.
   *
   * @param l the location
   * @param t the time-slot
   * @return the searched record or null if no such record exists.
   * @throws DataBaseNotReadyException
   */
  public Event findEntity(Location l, TimeSlot t) throws DataBaseNotReadyException {
    List<Event> resultList = findAll(l, t);
    assert (resultList != null);
    if (resultList.isEmpty()) {
      return null;
    }
    if (resultList.size() > 1) {
      logger.log(Level.WARNING, "More than one event for {0} at time {1}.", new Object[]{l, t});
    }
    return resultList.get(0);
  }

  /**
   * Searches all event-records for a given location and a given time-slot.
   *
   * If fact, in a given location only one event can happen at the same time;
   * but the database structure does not prohibit such miss-allocations,
   * therefore the whole list is returned.
   *
   * @param l the location
   * @param t the time-slot
   * @return a list of all records fulfilling the search criteria.
   * @throws DataBaseNotReadyException
   */
  public List<Event> findAll(Location l, TimeSlot t) throws DataBaseNotReadyException {
    if (l == null) {
      return Collections.emptyList();
    }
    if (t == null) {
      return Collections.emptyList();
    }
    synchronized (Manager.databaseAccessLock) {
      ArrayList<Event> result = new ArrayList<>();
      List<Event> locationsEvents = l.getEventList();
      for (Event e : locationsEvents) {
        TimeSlot timeSlot = e.getTimeSlot();
        assert (timeSlot != null);
        if (timeSlot.equals(t)) {
          result.add(e);
        }
      }

      return result;
    }

  }

  public List<Event> findAllSlow(Location l, TimeSlot t) throws DataBaseNotReadyException {
    synchronized (Manager.databaseAccessLock) {
      TypedQuery<Event> query = Manager.getEntityManager().createNamedQuery("Event.findByLocationAndTimeslot", Event.class);
      query.setParameter("timeSlot", t);
      query.setParameter("location", l);
      List<Event> resultList = query.getResultList();
      assert (resultList != null);
      return resultList;
    }
  }

  /**
   * Find the event for a given contest that happen at a given time.
   *
   * @param c
   * @param t
   * @return
   * @throws DataBaseNotReadyException
   */
  public Event findEntity(Contest c, TimeSlot t) throws DataBaseNotReadyException {
    if (c == null) {
      return null;
    }
    if (t == null) {
      return null;
    }
    synchronized (Manager.databaseAccessLock) {
      List<Event> contestsEvents = c.getEventList();
      for(Event e: contestsEvents){
        TimeSlot timeSlot = e.getTimeSlot();
        assert(timeSlot != null);
        if(timeSlot.equals(t)){
          return e;
        }
      }
      return null;
    }
  }

  public Event findEntitySlow(Contest c, TimeSlot t) throws DataBaseNotReadyException {
    synchronized (Manager.databaseAccessLock) {
      TypedQuery<Event> query = Manager.getEntityManager().createNamedQuery("Event.findByContestAndTimeslot", Event.class);
      query.setParameter("timeSlot", t);
      query.setParameter("contest", c);

      List<Event> resultList = query.getResultList();
      assert (resultList != null);
      if (resultList.isEmpty()) {
        return null;
      }
      if (resultList.size() > 1) {
        logger.log(Level.WARNING, "More than one event for {0} at time {1}.", new Object[]{c, t});
      }
      return resultList.get(0);
    }
  }

  public Event newEntity(Contest contest, TimeSlot timeSlot) throws DataBaseNotReadyException {

    synchronized (Manager.databaseAccessLock) {
      EntityManager entityManager = Manager.getEntityManager();
      Event newEvent = Event.newEvent(entityManager, contest, timeSlot);
      firePropertyChange(PROP_ITEM_ADDED, null, newEvent.identity());
      return newEvent;
    }
  }

  @Override
  public void removeEntity(Integer key) throws DataBaseNotReadyException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

  @Override
  public Event newEntity() throws DataBaseNotReadyException {
    throw new UnsupportedOperationException("Not supported. Use newEntity(Contest, TimeSlot)"); //To change body of generated methods, choose Tools | Templates.
  }
}
