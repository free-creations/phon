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

import de.free_creations.dbEntities.Team;
import de.free_creations.dbEntities.Person;
import java.awt.EventQueue;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class TeamCollection implements MutableEntityCollection<Team, Integer> {

  private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  /**
   * The constructor is protected, in order to ensure the singleton pattern.
   *
   * To get access to this instance use the getTeamCollection() method of the
   * Manager.
   */
  protected TeamCollection() {
  }

  /**
   * Returns the list of all entries in table TEAM.
   *
   * If the connection to the database cannot be established, an empty list will
   * be returned.
   *
   * @return the list of all entries in table TEAM
   */
  @Override
  public final List<Team> getAll() {
    synchronized (Manager.databaseAccessLock) {
      try {
        EntityManager entityManager = Manager.getEntityManager();
        TypedQuery<Team> query = entityManager.createNamedQuery("Team.findAll", Team.class);
        List<Team> cc = query.getResultList();
        return cc;
      } catch (DataBaseNotReadyException ignored) {
        return Collections.emptyList();
      }
    }
  }

  /**
   * Returns an entity from the current persistency context.
   *
   * @param key the primary key.
   * @return the returned entity is guaranteed to belong to the current
   * persistency context.
   * @throws de.free_creations.nbPhonAPI.DataBaseNotReadyException
   */
  @Override
  public Team findEntity(Integer key) throws DataBaseNotReadyException {
    if (key == null) {
      return null;
    }
    synchronized (Manager.databaseAccessLock) {
      return Manager.getEntityManager().find(Team.class, key);
    }
  }

  /**
   * Creates a new record in the table TEAM.
   *
   * @return an entity representing a new record in the table TEAM. Note, the
   * returned entity is attached to the current persistence context and a
   * corresponding record is flushed (but not committed).
   * @throws de.free_creations.nbPhonAPI.DataBaseNotReadyException
   */
  @Override
  public Team newEntity() throws DataBaseNotReadyException {

    synchronized (Manager.databaseAccessLock) {
      EntityManager entityManager = Manager.getEntityManager();
      Team newTeam = new Team();
      entityManager.persist(newTeam);
      try {
        entityManager.flush();
        firePropertyChange(PROP_ITEM_ADDED, null, newTeam.identity());
        return newTeam;
      } catch (Throwable ex) {
        throw new DataBaseNotReadyException(ex);
      }
    }
  }

  /**
   * Removes a TEAM-record from the database.
   *
   * Note: all persons inscribed in the given team are removed from the team
   * before deleting the record.
   *
   * @param key
   * @throws DataBaseNotReadyException
   */
  @Override
  public void removeEntity(Integer key) throws DataBaseNotReadyException {
    Team toRemove = findEntity(key);
    if (toRemove == null) {
      return; // should we throw an exception here?
    }
    assert (toRemove.getPersonList() != null);
    ArrayList<Person> pp = new ArrayList<>(toRemove.getPersonList());
    for (Person p : pp) {
      p.setTeam(null);
    }
    synchronized (Manager.databaseAccessLock) {
      EntityManager entityManager = Manager.getEntityManager();
      entityManager.remove(toRemove);
      try {
        entityManager.flush();
        firePropertyChange(PROP_ITEM_REMOVED, toRemove.identity(), null);
      } catch (Throwable ex) {
        throw new DataBaseNotReadyException(ex);
      }
    }
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
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
}
