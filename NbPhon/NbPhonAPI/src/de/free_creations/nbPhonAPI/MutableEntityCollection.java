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

import java.beans.PropertyChangeListener;

/**
 * Classes implementing this interface manage the access 
 * a specific table of the database.
 * 
 * @author Harald Postner <Harald at free-creations.de>
 * @param <EntiyClass>
 * @param <KeyClass>
 */
public interface MutableEntityCollection<EntiyClass, KeyClass>
        extends EntityCollection<EntiyClass, KeyClass> {

  /**
   * Event indicating that an item has been added to the list. 
   * 
   * The variable  evt.oldValue()
   * will give the key of the removed value, evt.newValue() is set to null.
   *
   * Note: PROP_ITEM_REMOVED, PROP_ITEM_ADDED and PROP_ITEM_LIST_CHANGED are
   * mutually exclusive. Only one of these events will be fired at the same
   * time.
   */
  public static final String PROP_ITEM_REMOVED = "ITEM_REMOVED";
  /**
   * Event indicating that an item has been removed from the list. 
   * 
   * The variable evt.newValue()
   * will give the key of the new value, evt.oldValue() is set to null.
   *
   * Note: PROP_ITEM_REMOVED, PROP_ITEM_ADDED and PROP_ITEM_LIST_CHANGED are
   * mutually exclusive. Only one of these events will be fired at the same
   * time.
   */
  public static final String PROP_ITEM_ADDED = "ITEM_ADDED";
  /**
   * Event indicating that the list of items has substantially changed.
   * Listeners must refresh their representation. evt.newValue() and
   * evt.oldValue() are set to null.
   *
   * Note: PROP_ITEM_REMOVED, PROP_ITEM_ADDED and PROP_ITEM_LIST_CHANGED are
   * mutually exclusive. Only one of these events will be fired at the same
   * time.
   */
  public static final String PROP_ITEM_LIST_CHANGED = "ITEM_LIST_CHANGED";

  /**
   * Creates a new record in the table .
   *
   * @return an entity representing a new record in the table . Note, the
   * returned entity is attached to the current persistence context and a
   * corresponding record is flushed (but not committed).
   */
  public EntiyClass newEntity() throws DataBaseNotReadyException;

  public void removeEntity(KeyClass key) throws DataBaseNotReadyException;

  /**
   * Add PropertyChangeListener.
   *
   * @param listener
   */
  public void addPropertyChangeListener(PropertyChangeListener listener);

  /**
   * Remove PropertyChangeListener.
   *
   * @param listener
   */
  public void removePropertyChangeListener(PropertyChangeListener listener);
}
