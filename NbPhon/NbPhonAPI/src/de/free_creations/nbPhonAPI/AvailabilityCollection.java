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

import de.free_creations.dbEntities.Availability;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.TimeSlot;
import java.awt.EventQueue;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Harald Postner <Harald at fraa-creations.de>
 */
public class AvailabilityCollection implements MutableEntityCollection<Availability, Integer> {

  private static final Logger logger = Logger.getLogger(AvailabilityCollection.class.getName());

  private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  protected AvailabilityCollection() {
  }

  @Override
  public List<Availability> getAll() {
    synchronized (Manager.databaseAccessLock) {
      try {
        Manager.ping();
        EntityManager entityManager = Manager.getEntityManager();
        TypedQuery<Availability> query = entityManager.createNamedQuery("Availability.findAll", Availability.class);
        List<Availability> aa = query.getResultList();
        return aa;
      } catch (DataBaseNotReadyException | ConnectionLostException ignored) {
        return Collections.emptyList();
      }
    }
  }

  @Override
  public Availability findEntity(Integer key) throws DataBaseNotReadyException {
    if (key == null) {
      return null;
    }
    synchronized (Manager.databaseAccessLock) {
      return Manager.getEntityManager().find(Availability.class, key);
    }
  }

  /**
   * Searches the availability-record for a given person and a given time-slot. If
   * (by error) more than one such records exists, the first one is returned and
   * a warning is logged.
   *
   * @param p the location
   * @param t the time-slot
   * @return the searched record or null if no such record exists.
   * @throws DataBaseNotReadyException
   */
  public Availability findEntity(Person p, TimeSlot t) throws DataBaseNotReadyException {
    List<Availability> resultList = findAll(p, t);
    assert (resultList != null);
    if (resultList.isEmpty()) {
      return null;
    }
    if (resultList.size() > 1) {
      logger.log(Level.WARNING, "More than one Availability for {0} at time {1}.", new Object[]{p, t});
    }
    return resultList.get(0);
  }

  /**
   * Searches all availability-records for a given person and a given time-slot.
   *
   * If fact, in a given person can only have one availability for a given time;
   * but the database structure does not prohibit such miss-allocations,
   * therefore the whole list is returned.
   *
   * @param p the person
   * @param t the time-slot
   * @return a list of all records fulfilling the search criteria.
   * @throws DataBaseNotReadyException
   */
  public List<Availability> findAll(Person p, TimeSlot t) throws DataBaseNotReadyException {
    synchronized (Manager.databaseAccessLock) {
      TypedQuery<Availability> query = Manager.getEntityManager().createNamedQuery("Availability.findByPersonAndTimeslot", Availability.class);
      query.setParameter("timeSlot", t);
      query.setParameter("person", p);
      List<Availability> resultList = query.getResultList();
      assert (resultList != null);
      return resultList;
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
  public Availability newEntity() throws DataBaseNotReadyException {
    // new availabilty records are automatically generated when a new person is created.
    throw new UnsupportedOperationException("Not supported. Use PersonCollection.newEntity()."); //To change body of generated methods, choose Tools | Templates.
  }
}
