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

import java.util.List;

/**
 * Manages the access to a table of the database.
 *
 * @author Harald Postner <Harald at free-creations.de>
 * @param <EntiyClass>
 * @param <KeyClass>
 */
public interface EntityCollection<EntiyClass, KeyClass> {

  /**
   * Returns the list of all entries in table corresponding to the entity.
   *
   * If the connection to the database cannot be established, an empty list will
   * be returned.
   *
   * @return the list of all entries in the table
   */
  public List<EntiyClass> getAll();

  /**
   * Returns an entity from the current persistency context.
   *
   * @param key the primary key.
   * @return the returned entity is guaranteed to belong to the current
   * persistency context.
   * @throws de.free_creations.nbPhonAPI.DataBaseNotReadyException
   */
  public EntiyClass findEntity(KeyClass key) throws DataBaseNotReadyException;
}
