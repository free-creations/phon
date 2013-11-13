/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.free_creations.nbPhonAPI;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
  private static FunctionsCollection functionsCollection = null;
  private static boolean problemHasBeenReported = false;
  public static final Object databaseAccessLock = new Object();
  private static final Object timeSlotLock = new Object();
  private static final Object functionsLock = new Object();
  private static final Object juryLock = new Object();
  private static JuryCollection juryCollection = null;

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
   * Returns the collection of all PERSONEN records.
   *
   * @return the collection of all PERSONEN records.
   */
  public static PersonCollection getPersonCollection() {
    return personCollection;
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
  public static FunctionsCollection getFunctionsCollection() {
    synchronized (functionsLock) {
      if (functionsCollection == null) {
        functionsCollection = new FunctionsCollection();
      }
      return functionsCollection;
    }
  }

  /**
   * Returns the collection of all JURY records.
   *
   * @return the collection of all JURY records.
   */
  public static JuryCollection getJuryCollection() {
    synchronized (juryLock) {
      if (juryCollection == null) {
        juryCollection = new JuryCollection();
      }
      return juryCollection;
    }
  }
}
