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

import de.free_creations.dbEntities.JobType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class JobTypeCollection implements EntityCollection<JobType, String> {

  private final List<String> jobTypeIds;
  private final List<String> jobNames;
  private final List<String> jobIconNames;

  protected JobTypeCollection() {
    jobTypeIds = new ArrayList<>();
    jobNames = new ArrayList<>();
    jobIconNames = new ArrayList<>();
    for (JobType jt : getAll()) {
      jobTypeIds.add(jt.getJobTypeId());
      jobNames.add(jt.getName());
      jobIconNames.add(jt.getIcon());
    }

  }

  public List<String> jobTypeIds() {
    return jobTypeIds;
  }

  public List<String> jobNames() {
    return jobNames;
  }

  public List<String> jobIconNames() {
    return jobIconNames;
  }

  /**
   * Returns the list of all entries in table JOBTYPE.
   *
   * If the connection to the database cannot be established, an empty list will
   * be returned.
   *
   * @return the list of all entries in table JOBTYPE
   */
  @Override
  public final List<JobType> getAll() {
    synchronized (Manager.databaseAccessLock) {
      try {
        EntityManager entityManager = Manager.getEntityManager();
        TypedQuery<JobType> query = entityManager.createNamedQuery("Job.findAll", JobType.class);
        List<JobType> ff = query.getResultList();
        return ff;
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
  public JobType findEntity(String key) throws DataBaseNotReadyException {
    if (key == null) {
      return null;
    }
    synchronized (Manager.databaseAccessLock) {
      return Manager.getEntityManager().find(JobType.class, key);
    }
  }

}
