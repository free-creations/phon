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

import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhonAPI.util.StringArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * The TimeSlotCollection provides access to the "TimeSlot" records.
 *
 * The procedures shall help to show the time-slots in tables ordered by days
 * and time of day.
 *
 * We assume that the time-slot records do not change, therefore these records
 * can be analyzed at startup.
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class TimeSlotCollection implements EntityCollection<TimeSlot, Integer> {

  private final String[] timeOfDayNames;
  private final String[] dayNames;

  /**
   * The constructor is protected, in order to ensure the singleton pattern.
   *
   * To get access to this instance use the getTimeSlotCollection() method of
   * the Manager.
   */
  protected TimeSlotCollection() {

    // analize the time-slot and extract the names of the days 
    StringArrayList _timeOfDayNames = new StringArrayList();
    StringArrayList _dayNames = new StringArrayList();

    List<TimeSlot> tt = getAll();
    for (TimeSlot t : tt) {
      if (t.getTimeOfDayIdx()!= null) {
        _timeOfDayNames.put(t.getTimeOfDayIdx() - 1, t.getTimeOfDayPrint());
      }
      if (t.getDayIdx()!= null) {
        _dayNames.put(t.getDayIdx()- 1, t.getDayOfWeek());
      }
    }
    timeOfDayNames = _timeOfDayNames.toArray();
    dayNames = _dayNames.toArray();
  }

  /**
   * Returns the list of all entries in table ZEIT.
   *
   * If the connection to the database cannot be established, an empty list will
   * be returned.
   *
   * @return the list of all entries in table ZEIT
   */
  @Override
  public final List<TimeSlot> getAll() {
    synchronized (Manager.databaseAccessLock) {
      try {
        EntityManager entityManager = Manager.getEntityManager();
        TypedQuery<TimeSlot> query = entityManager.createNamedQuery("TimeSlot.findAll", TimeSlot.class);
        return query.getResultList();
      } catch (DataBaseNotReadyException ignored) {
        return Collections.emptyList();
      }
    }
  }

  public String[] timeOfDayNames() {
    return this.timeOfDayNames;
  }

  public String[] dayNames() {
    return this.dayNames;
  }

  /**
   *
   * @param day the zero-based day-index
   * @param timeOfDay the zero-based timeOfDay-index 
   * @return the time-slot where day = t.getTag()-1 and timeOfDay =
   * t.getTageszeit()-1 if no such entry can be found the function returns null.
   * @throws DataBaseNotReadyException
   */
  public TimeSlot findEntity(int day, int timeOfDay) throws DataBaseNotReadyException {
    final String qlString =
            "SELECT t FROM TimeSlot t "
            + "WHERE t.dayIdx = :dayIdx "
            + "AND t.timeOfDayIdx = :timeOfDayIdx";  
    
    synchronized (Manager.databaseAccessLock) {
      EntityManager entityManager = Manager.getEntityManager();
      TypedQuery<TimeSlot> query = entityManager.createQuery(qlString, TimeSlot.class);
      query.setParameter("dayIdx", day + 1);
      query.setParameter("timeOfDayIdx", timeOfDay + 1);
      try {
        return query.getSingleResult();
      } catch (Throwable th) {
        return null;
      }
    }
  }

  @Override
  public TimeSlot findEntity(Integer key) throws DataBaseNotReadyException {
    if (key == null) {
      return null;
    }
    synchronized (Manager.databaseAccessLock) {
      return Manager.getEntityManager().find(TimeSlot.class, key);
    }
  }


}
