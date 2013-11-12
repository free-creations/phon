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
package de.free_creations.dbEntities;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A utility class that is used by DbEntities to support bound properties.
 *
 * It manages a list of listeners and dispatches PropertyChangeEvents to them.
 *
 * Note: storing permanently a reference to the listeners introduces a memory
 * leak. A better solution would be to hold a weak reference.
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
class PropertyChangeManager {

  private static PropertyChangeManager self = new PropertyChangeManager();
  // private final Object lock = new Object();
  private final Map<EntityIdentity, Set<PropertyChangeListener>> listeners = new HashMap<>();

  private PropertyChangeManager() {
  }

  protected static PropertyChangeManager instance() {
    return self;
  }

  /**
   * Add PropertyChangeListener.
   *
   * Note: the execution of this function is inserted at the end of the
   * AWT-queue. Thus avoiding concurrent access to the "listeners" map.
   *
   * @param listener property change listener
   * @param entityClass entity class
   * @param primaryKey primary key
   */
  protected void addPropertyChangeListener(final PropertyChangeListener listener, final EntityIdentity entityIdentity) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        excuteAddListener(listener, entityIdentity);
      }
    });
  }

  private void excuteAddListener(PropertyChangeListener listener, EntityIdentity entityIdentity) {
    // synchronized (lock) {
    Set<PropertyChangeListener> listenerSet = listeners.get(entityIdentity);
    if (listenerSet == null) {
      listenerSet = new HashSet<>();
      listeners.put(entityIdentity, listenerSet);
    }
    listenerSet.add(listener);
    //}
  }

  protected void removePropertyChangeListener(final PropertyChangeListener listener, final EntityIdentity entityIdentity) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        excuteRemoveListener(listener, entityIdentity);
      }
    });
  }

  /**
   * Remove PropertyChangeListener.
   *
   * @param listener property change listener
   * @param entityClass entity class
   * @param primaryKey primary key
   */
  private void excuteRemoveListener(PropertyChangeListener listener, EntityIdentity entityIdentity) {
    //  synchronized (lock) {
    Set<PropertyChangeListener> listenerSet = listeners.get(entityIdentity);
    if (listenerSet == null) {
      return;
    }
    listenerSet.remove(listener);
    // }
  }

  /**
   * Reports a bound property update that occurred in an entity with the given
   * class and the given key.
   *
   * Note: the callback is guaranteed to execute in the AWT thread.
   *
   * @param entityClass
   * @param primaryKey
   * @param propertyName
   * @param oldValue
   * @param newValue
   */
  protected void firePropertyChange(final EntityIdentity entityIdentity, final String propertyName, final Object oldValue, final Object newValue) {
//    if (EventQueue.isDispatchThread()) {
//      exceutePropertyChange(entityIdentity, propertyName, oldValue, newValue);
//    } else {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        exceutePropertyChange(entityIdentity, propertyName, oldValue, newValue);
      }
    });
    // }
  }

  /**
   * This procedure shall be executed in the AWT thread.
   *
   * @param entityIdentity
   * @param propertyName
   * @param oldValue
   * @param newValue
   */
  private void exceutePropertyChange(EntityIdentity entityIdentity, String propertyName, Object oldValue, Object newValue) {
    // synchronized (lock) {
    Set<PropertyChangeListener> listenerSet = listeners.get(entityIdentity);
    if (listenerSet == null) {
      return;
    }
    PropertyChangeEvent event = new PropertyChangeEvent(entityIdentity, propertyName, oldValue, newValue);
    for (PropertyChangeListener listener : listenerSet) {
      listener.propertyChange(event);
    }
    //}
  }

  void removeAllListeners(final EntityIdentity identity) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        executeRemoveAllListeners(identity);
      }
    });
  }

  private void executeRemoveAllListeners(EntityIdentity entityIdentity) {
    listeners.remove(entityIdentity);
  }
}
