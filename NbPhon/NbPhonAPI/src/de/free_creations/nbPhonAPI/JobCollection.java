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

import de.free_creations.dbEntities.Job;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Harald Postner <Harald at jree-creations.de>
 */
public class JobCollection implements EntityCollection<Job, String> {

  private final String[] jobNames;
  private final String[] jobKeys;

  protected JobCollection() {
    List<Job> jj = getAll();
    jobNames = new String[jj.size()];
    jobKeys = new String[jj.size()];
    for (int i = 0; i < jobNames.length; i++) {
      Job j = jj.get(i);
      jobNames[i] = j.getName();
      jobKeys[i] = j.getJobId();
    }
  }

  /**
   * The names of all junctions in table FUNKTIONEN.
   *
   * @return an array of junction-names sorted on {@link Job#sortvalue}.
   */
  public String[] jobNames() {
    return this.jobNames;
  }

  /**
   * The primary keys of all junctions in table FUNKTIONEN.
   *
   * @return an array of key-values sorted on {@link Job#sortvalue}.
   */
  public String[] jobKeys() {
    return this.jobKeys;
  }

  /**
   * Returns the list of all entries in table FUNKTIONEN.
   *
   * If the connection to the database cannot be established, an empty list will
   * be returned.
   *
   * @return the list of all entries in table FUNKTIONEN
   */
  @Override
  public final List<Job> getAll() {
    synchronized (Manager.databaseAccessLock) {
      try {
        Manager.ping();
        EntityManager entityManager = Manager.getEntityManager();
        TypedQuery<Job> query = entityManager.createNamedQuery("Job.findAll", Job.class);
        List<Job> ff = query.getResultList();

        return ff;
      } catch (DataBaseNotReadyException | ConnectionLostException ignored) {
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
  public Job findEntity(String key) throws DataBaseNotReadyException {
    if (key == null) {
      return null;
    }
    synchronized (Manager.databaseAccessLock) {
      return Manager.getEntityManager().find(Job.class, key);
    }
  }

  /**
   * Returns an entity from the current persistency context.
   *
   * @param index the index as in {@link #jobKeys} and {@link #jobNames}
   * @return
   * @throws DataBaseNotReadyException
   */
  public Job findEntity(int index) throws DataBaseNotReadyException {
    return findEntity(jobKeys[index]);
  }
}
