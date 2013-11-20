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

import de.free_creations.dbEntities.Personen;
import de.free_creations.dbEntities.Verfuegbarkeit;
import de.free_creations.dbEntities.Zeit;
import java.awt.EventQueue;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Manages the access to the table "PERSONEN".
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class PersonCollection implements MutableEntityCollection<Personen, Integer> {

  private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
  /**
   * Indicates that a new item has been added.
   */
  
  /**
   * Indicates that a new item has been added.
   */
  public static final String PROP_DATABASESTATUS = "databaseStatus";

  /**
   * The constructor is protected, in order to ensure the singleton pattern.
   *
   * To get access to this instance use the getPersonCollection() method of the
   * Manager.
   */
  protected PersonCollection() {
  }

  /**
   * Returns the list of all entries in table PERSONEN.
   *
   * If the connection to the database cannot be established, an empty list will
   * be returned.
   *
   * @return the list of all entries in table PERSONEN
   */
  @Override
  public List<Personen> getAll() {
    synchronized (Manager.databaseAccessLock) {
      try {
        EntityManager entityManager = Manager.getEntityManager();
        TypedQuery<Personen> query = entityManager.createNamedQuery("Personen.findAll", Personen.class);
        return query.getResultList();
      } catch (DataBaseNotReadyException ignored) {
        return Collections.emptyList();
      }
    }
  }

  @Override
  public Personen findEntity(Integer key) throws DataBaseNotReadyException {
    if (key == null) {
      return null;
    }
    synchronized (Manager.databaseAccessLock) {
      return Manager.getEntityManager().find(Personen.class, key);
    }
  }

  /**
   * Creates a new record in the table PERSONEN.
   *
   * @return an entity representing a new record in the table PERSONEN. Note,
   * the returned entity is attached to the current persistence context and a
   * corresponding record is flushed (but not committed).
   */
  @Override
  public Personen newEntity() throws DataBaseNotReadyException {
    Personen newPerson;
    synchronized (Manager.databaseAccessLock) {
      EntityManager entityManager = Manager.getEntityManager();
      newPerson = new Personen();
      entityManager.persist(newPerson);
      try {
        entityManager.flush();
      } catch (Throwable ex) {
        throw new DataBaseNotReadyException(ex);
      }

      TypedQuery<Zeit> allTimesQuery = entityManager.createNamedQuery("Zeit.findAll", Zeit.class);
      List<Zeit> allTimeslots = allTimesQuery.getResultList();

      for (Zeit t : allTimeslots) {
        Verfuegbarkeit v = new Verfuegbarkeit();
        v.setVerfuegbar(false);
        entityManager.persist(v);
        v.setZeitid(t);
        v.setPersonid(newPerson);
        entityManager.flush();
      }
    }
    firePropertyChange(PROP_ITEM_ADDED, null, newPerson.identity());

    return newPerson;
  }


  /**
   * Add PropertyChangeListener.
   *
   * @param listener
   */
  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * Remove PropertyChangeListener.
   *
   * @param listener
   */
  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  protected void fireDataBaseStatusChange() {
    firePropertyChange(PROP_DATABASESTATUS, null, null);
  }

  /**
   * Reports a bound property update to listeners that have been registered to
   * track updates of all properties or a property with the specified name.
   *
   * Note: the callback is guaranteed to execute in the AWT thread.
   *
   * @param propertyName
   * @param oldValue
   * @param newValue
   */
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
  public void removeEntity(Integer key) throws DataBaseNotReadyException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
