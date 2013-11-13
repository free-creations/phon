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

import de.free_creations.dbEntities.Jury;
import java.awt.EventQueue;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class JuryCollection implements MutableEntityCollection<Jury, String> {

  private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  protected JuryCollection() {
  }

  public Set<String> juryTypes() {
    List<Jury> jj = getAll();
    TreeSet<String> result = new TreeSet<>();
    for (Jury j : jj) {
      if (j.getWertungstyp() != null) {
        result.add(j.getWertungstyp());
      }
    }
    return result;
  }

  @Override
  public List<Jury> getAll() {
    synchronized (Manager.databaseAccessLock) {
      try {
        EntityManager entityManager = Manager.getEntityManager();
        TypedQuery<Jury> query = entityManager.createNamedQuery("Jury.findAll", Jury.class);
        List<Jury> jj = query.getResultList();
        return jj;
      } catch (DataBaseNotReadyException ignored) {
        return Collections.emptyList();
      }
    }
  }

  @Override
  public Jury findEntity(String key) throws DataBaseNotReadyException {
    if (key == null) {
      return null;
    }
    synchronized (Manager.databaseAccessLock) {
      return Manager.getEntityManager().find(Jury.class, key);
    }
  }

  @Override
  public Jury newEntity() throws DataBaseNotReadyException {
    Jury newJury = null;
    synchronized (Manager.databaseAccessLock) {
      EntityManager entityManager = Manager.getEntityManager();
      newJury = new Jury(makeNewKey());
      entityManager.persist(newJury);
      try {
        entityManager.flush();
        firePropertyChange(PROP_ITEM_ADDED, null, newJury.identity());
      } catch (Throwable ex) {
        throw new DataBaseNotReadyException(ex);
      }
      
      return newJury;
    }
  }

  /**
   * A very simplistic way to generate a new primary key.
   * @return
   * @throws DataBaseNotReadyException 
   */
  private String makeNewKey() throws DataBaseNotReadyException {
    for (int i = 1; i < Integer.MAX_VALUE; i++) {
      String newKey = String.format("Jury%d", i);
      if (findEntity(newKey) == null) {
        return newKey;
      }
    }
    throw new RuntimeException("Key range exhausted.");
  }

  @Override
  public void removeEntity(String key) throws DataBaseNotReadyException {
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
