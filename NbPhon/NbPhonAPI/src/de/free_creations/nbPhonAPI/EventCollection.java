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
import de.free_creations.dbEntities.TimeSlot;
import java.awt.EventQueue;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Collections;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class EventCollection implements MutableEntityCollection<Event, Integer> {

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
