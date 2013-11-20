/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.free_creations.dbEntities;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Harald <Harald at free-creations.de>
 */
public class PersonenTest {

  static final String PersistenceUnitName = "DbEntitiesPU";
  private static EntityManager entityManager = null;

  @BeforeClass
  public static void setUpClass() {
    try {
      EntityManagerFactory factory = Persistence.createEntityManagerFactory(PersistenceUnitName);
      entityManager = factory.createEntityManager();
    } catch (javax.persistence.PersistenceException ex) {
      fail("+++ Please start the database server and try again.");

    }
  }

  @AfterClass
  public static void tearDownClass() {
    if (entityManager != null) {
      entityManager.close();
    }
  }
  private Personen testPerson;
  private Personen testPerson_2;
  private Zeit testZeit;
  private Job testFunction;

  @Before
  public void setUp() {
    entityManager.getTransaction().begin();

    TypedQuery<Personen> ppq = entityManager.createNamedQuery("Personen.findAll", Personen.class);
    List<Personen> pp = ppq.getResultList();
    assertNotNull(pp);
    assertTrue(pp.size() > 2);
    testPerson = pp.get(0);
    testPerson_2 = pp.get(1);

    TypedQuery<Zeit> zzq = entityManager.createNamedQuery("Zeit.findAll", Zeit.class);
    List<Zeit> zz = zzq.getResultList();
    assertNotNull(zz);
    assertTrue(zz.size() > 1);
    testZeit = zz.get(0);

    TypedQuery<Job> ffq = entityManager.createNamedQuery("Job.findAll", Job.class);
    List<Job> ff = ffq.getResultList();
    assertNotNull(ff);
    assertTrue(ff.size() > 1);
    testFunction = ff.get(0);
  }

  @After
  public void tearDown() {
    entityManager.getTransaction().rollback();

    testPerson = null;
    testZeit = null;
  }

  /**
   */
  @Test
  public void testAddVerfuegbarkeit() {
    Availability v1 = new Availability();
    entityManager.persist(v1);
    v1.setZeitid(testZeit);

    v1.setPersonid(testPerson);


    entityManager.flush(); //

    assertTrue(testPerson.getVerfuegbarkeitList().contains(v1));
    assertEquals(testPerson, v1.getPersonid());
  }

  @Test
  public void testSetGewuensteFunktion() {
    testPerson.setGewuenschtefunktion(testFunction);
    assertTrue(testFunction.getPersonenList().contains(testPerson));

    testPerson.setGewuenschtefunktion(null);
    assertFalse(testFunction.getPersonenList().contains(testPerson));
  }

  @Test
  public void testSetGewuenschterkollege() {
    testPerson.setGewuenschterkollege(testPerson_2);
    assertTrue(testPerson_2.getGroupList().contains(testPerson));

    testPerson.setGewuenschterkollege(null);
    assertFalse(testPerson_2.getGroupList().contains(testPerson));
  }

  /**
   */
  @Test
  @Ignore("Problems with AWT thread")
  public void testPropertyChangeCallback() throws InterruptedException, InvocationTargetException {
    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        TestListener testListener = new TestListener();
        testPerson.addPropertyChangeListener(testListener);

        // change something in the test item.
        testPerson.setFamilienname("ABC");

        // verify that the test listener got called.
        assertEquals(1, testListener.called);
        assertNotNull(testListener.lastEvent);
        assertEquals(Personen.PROP_FAMILIENNAME, testListener.lastEvent.getPropertyName());
        assertEquals("ABC", testListener.lastEvent.getNewValue());

        // remove the test listener 
        testPerson.removePropertyChangeListener(testListener);
        // change again something in the test item.
        testPerson.setFamilienname("XYZ");
        // verify that the test listener was not called again.
        assertEquals(1, testListener.called);
      }
    });

  }

  private class TestListener implements PropertyChangeListener {

    public int called = 0;
    public PropertyChangeEvent lastEvent = null;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      called++;
      lastEvent = evt;
    }
  }
}