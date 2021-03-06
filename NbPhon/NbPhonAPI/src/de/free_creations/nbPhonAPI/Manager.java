/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.free_creations.nbPhonAPI;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author Harald <Harald at free-creations.de>
 */
public class Manager {

  static final String PersistenceUnitName = "DbEntitiesPU";
  private static EntityManager entityManager = null;
  private static final PersonCollection personCollection = new PersonCollection();
  private static TimeSlotCollection timeSlotCollection = null;
  private static JobCollection jobCollection = null;
  private static final TeamCollection teamCollection = new TeamCollection();
  private static boolean problemHasBeenReported = false;
  public static final Object databaseAccessLock = new Object();
  private static final Object timeSlotLock = new Object();
  private static final Object jobColLock = new Object();
  private static final Object contestLock = new Object();
  private static ContestCollection contestCollection = null;

  protected static EntityManager getEntityManager() throws DataBaseNotReadyException {
    synchronized (databaseAccessLock) {
      if (entityManager == null) {
        try {
          EntityManagerFactory factory = Persistence.createEntityManagerFactory(PersistenceUnitName);
          entityManager = factory.createEntityManager();
          entityManager.getTransaction().begin();
          personCollection.fireDataBaseStatusChange();
        } catch (Throwable ex) {
          throw new DataBaseNotReadyException(ex);
        }
      }
      return entityManager;
    }
  }

  /**
   * Verifies whether the database can be accessed.
   *
   * @return true if the database can be accessed, false otherwise.
   */
  @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
  public static boolean isOpen() {
    synchronized (databaseAccessLock) {
      try {
        getEntityManager();
        return true;
      } catch (Throwable ex) {
        return false;
      }
    }
  }

  /**
   * Show an error dialog if the database cannot be accessed, this dialog is
   * only shown the first time that this procedure is called, thus avoiding to
   * nag the user with several times the same error report at startup.
   *
   * @return
   */
  public static boolean assertOpen() {
    boolean open = isOpen();
    if (!open) {
      if (!problemHasBeenReported) {
        NotifyDescriptor.Message message = new NotifyDescriptor.Message(
                "Cannot access the database.\n Has the server been started?",
                NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(message);
        problemHasBeenReported = true;
      }
    }
    return open;
  }

  /**
   * A rapid check, to verify whether the database is still alive.
   *
   * This check is recommended before doing complex queries (like "getAll"). The
   * execution of such queries on a dead database might take several minutes
   * before ending on a time out.
   *
   * @throws ConnectionLostException
   */
  @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "unchecked", "rawtypes"})
  public static void ping() throws ConnectionLostException {
    if (!assertOpen()) {
      return; // too early to report any problem
    }
    synchronized (databaseAccessLock) {
      try {
        long currentTimeMillis = System.currentTimeMillis();
        if ((currentTimeMillis - lastGoodPingMillis) < 500) {
          // database was alive half a second ago, we assume it is still so.
          return;
        }
        Query q = getEntityManager().createNamedQuery("ping");
        List resultList = q.getResultList();
        if (resultList != null) {
          lastGoodPingMillis = System.currentTimeMillis();
          return; // normal exit
        }
      } catch (Throwable ex) {
        // ignore all exceptions. ConnectionLostException will be thrown anyway.
      }
      throw new ConnectionLostException();
    }
  }
  private static long lastGoodPingMillis = Long.MIN_VALUE;

  /**
   * Commits the current transaction and starts a new one.
   */
  public synchronized static void commit() {
    synchronized (databaseAccessLock) {
      entityManager.getTransaction().commit();
      entityManager.getTransaction().begin();
    }
  }

  /**
   * Rolls back the current transaction and starts a new one.
   */
  public synchronized static void rollback() {
    synchronized (databaseAccessLock) {
      entityManager.getTransaction().rollback();
      entityManager.getTransaction().begin();
    }
  }

  /**
   * Closes the connection to the database.
   *
   * @param commit
   */
  public synchronized static void close(boolean commit) {
    synchronized (databaseAccessLock) {
      if (entityManager != null) {
        if (entityManager.getTransaction().isActive()) {
          if (commit) {
            entityManager.getTransaction().commit();
          } else {
            entityManager.getTransaction().rollback();
          }
        }
        entityManager.close();
      }

      entityManager = null;
    }
  }

  /**
   * Returns the collection of all PERSON records.
   *
   * @return the collection of all PERSON records.
   */
  public static PersonCollection getPersonCollection() {
    return personCollection;
  }

  /**
   * Returns the collection of all TEAM records.
   *
   * @return the collection of all TEAM records.
   */
  public static TeamCollection getTeamCollection() {
    return teamCollection;
  }

  /**
   * Returns the collection of all ZEIT records.
   *
   * @return the collection of all ZEIT records.
   */
  public static TimeSlotCollection getTimeSlotCollection() {
    synchronized (timeSlotLock) {
      if (timeSlotCollection == null) {
        timeSlotCollection = new TimeSlotCollection();
      }
      return timeSlotCollection;
    }
  }

  /**
   * Indicates whether the time-slot-collection has been initialized.
   *
   * @return true if the has been initialized.
   */
  protected static boolean hasTimeSlotCollection() {
    synchronized (timeSlotLock) {
      return (timeSlotCollection != null);
    }
  }

  /**
   * Returns the collection of all FUNCTIONS records.
   *
   * @return the collection of all FUNCTIONS records.
   */
  public static JobCollection getJobCollection() {
    synchronized (jobColLock) {
      if (jobCollection == null) {
        jobCollection = new JobCollection();
      }
      return jobCollection;
    }
  }

  /**
   * Returns the collection of all CONTEST records.
   *
   * @return the collection of all CONTEST records.
   */
  public static ContestCollection getContestCollection() {
    synchronized (contestLock) {
      if (contestCollection == null) {
        contestCollection = new ContestCollection();
      }
      return contestCollection;
    }
  }

  private static final Object contestTypeLock = new Object();
  private static ContestTypeCollection contestTypeCollection = null;

  public static ContestTypeCollection getContestTypeCollection() {
    synchronized (contestTypeLock) {
      if (contestTypeCollection == null) {
        contestTypeCollection = new ContestTypeCollection();
      }
      return contestTypeCollection;
    }
  }

  private static final Object jobTypeLock = new Object();
  private static JobTypeCollection jobTypeCollection = null;

  public static JobTypeCollection getJobTypeCollection() {
    synchronized (jobTypeLock) {
      if (jobTypeCollection == null) {
        jobTypeCollection = new JobTypeCollection();
      }
      return jobTypeCollection;
    }
  }

  private static final Object locationLock = new Object();
  private static LocationCollection locationCollection = null;

  public static LocationCollection getLocationCollection() {
    synchronized (locationLock) {
      if (locationCollection == null) {
        locationCollection = new LocationCollection();
      }
      return locationCollection;
    }
  }

  private static final Object eventLock = new Object();
  private static EventCollection eventCollection = null;

  public static EventCollection getEventCollection() {
    synchronized (eventLock) {
      if (eventCollection == null) {
        eventCollection = new EventCollection();
      }
      return eventCollection;
    }
  }

  private static final Object allocationLock = new Object();
  private static AllocationCollection allocationCollection = null;

  public static AllocationCollection getAllocationCollection() {
    synchronized (allocationLock) {
      if (allocationCollection == null) {
        allocationCollection = new AllocationCollection();
      }
      return allocationCollection;
    }
  }

  private static final Object availabilityLock = new Object();
  private static AvailabilityCollection availabilityCollection = null;

  public static AvailabilityCollection getAvailabilityCollection() {
    synchronized (availabilityLock) {
      if (availabilityCollection == null) {
        availabilityCollection = new AvailabilityCollection();
      }
      return availabilityCollection;
    }
  }
}
