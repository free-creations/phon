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
public class ContestCollection implements MutableEntityCollection<Contest, Integer> {

  private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  protected ContestCollection() {
  }

  /**
   * @deprecated  use ContestTypeCollection().contestTypeIds()
   * @return 
   */
  @Deprecated
  public List<String> contestTypes() {
    return Manager.getContestTypeCollection().contestTypeIds();

  }

  @Override
  public List<Contest> getAll() {
    synchronized (Manager.databaseAccessLock) {
      try {
        Manager.ping();
        EntityManager entityManager = Manager.getEntityManager();
        TypedQuery<Contest> query = entityManager.createNamedQuery("Contest.findAll", Contest.class);
        List<Contest> cc = query.getResultList();
        return cc;
      } catch (DataBaseNotReadyException | ConnectionLostException ignored) {
        return Collections.emptyList();
      }
    }
  }

  @Override
  public Contest findEntity(Integer key) throws DataBaseNotReadyException {
    if (key == null) {
      return null;
    }
    synchronized (Manager.databaseAccessLock) {
      return Manager.getEntityManager().find(Contest.class, key);
    }
  }

  @Override
  public Contest newEntity() throws DataBaseNotReadyException {
    Contest newContest = null;
    synchronized (Manager.databaseAccessLock) {
      EntityManager entityManager = Manager.getEntityManager();
      newContest = new Contest();
      entityManager.persist(newContest);
      try {
        entityManager.flush();
      } catch (Throwable ex) {
        throw new DataBaseNotReadyException(ex);
      }
      try {
        entityManager.flush();
        firePropertyChange(PROP_ITEM_ADDED, null, newContest.identity());
      } catch (Throwable ex) {
        throw new DataBaseNotReadyException(ex);
      }
      
      return newContest;
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
}
