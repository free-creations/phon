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

import de.free_creations.dbEntities.Funktionen;
import static de.free_creations.nbPhonAPI.util.CompareUtils.bothValid;
import static de.free_creations.nbPhonAPI.util.CompareUtils.typeCheckCompare;
import static de.free_creations.nbPhonAPI.util.CompareUtils.integerCompareNull;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class JobCollection implements EntityCollection<Funktionen, String> {

  private final String[] functionNames;
  private final String[] functionKeys;
  private final Comparator<Funktionen> compareOnSortOrder = new Comparator<Funktionen>() {
    @Override
    public int compare(Funktionen f1, Funktionen f2) {
      int c = typeCheckCompare(f1, f2, Funktionen.class);
      if (c != bothValid) {
        return c;
      }
      return integerCompareNull(f1.getSortvalue(), f2.getSortvalue());
    }
  };

  protected JobCollection() {
    List<Funktionen> ff = getAll();
    functionNames = new String[ff.size()];
    functionKeys = new String[ff.size()];
    for (int i = 0; i < functionNames.length; i++) {
      Funktionen f = ff.get(i);
      functionNames[i] = f.getFunktionname();
      functionKeys[i] = f.getFunktionid();
    }
  }

  /**
   * The names of all functions in table FUNKTIONEN.
   *
   * @return an array of function-names sorted on {@link Funktionen#sortvalue}.
   */
  public String[] functionNames() {
    return this.functionNames;
  }

  /**
   * The primary keys of all functions in table FUNKTIONEN.
   *
   * @return an array of key-values sorted on {@link Funktionen#sortvalue}.
   */
  public String[] functionKeys() {
    return this.functionKeys;
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
  public final List<Funktionen> getAll() {
    synchronized (Manager.databaseAccessLock) {
      try {
        EntityManager entityManager = Manager.getEntityManager();
        TypedQuery<Funktionen> query = entityManager.createNamedQuery("Funktionen.findAll", Funktionen.class);
        List<Funktionen> ff = query.getResultList();
        Collections.sort(ff, compareOnSortOrder);
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
   */
  @Override
  public Funktionen findEntity(String key) throws DataBaseNotReadyException {
    if (key == null) {
      return null;
    }
    synchronized (Manager.databaseAccessLock) {
      return Manager.getEntityManager().find(Funktionen.class, key);
    }
  }

  /**
   * Returns an entity from the current persistency context.
   * @param index the index as in {@link #functionKeys} and {@link #functionNames}
   * @return
   * @throws DataBaseNotReadyException 
   */
  public Funktionen findEntity(int index) throws DataBaseNotReadyException {
    return findEntity(functionKeys[index]);
  }
}
