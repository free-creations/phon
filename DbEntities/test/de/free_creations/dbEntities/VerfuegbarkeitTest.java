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
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class VerfuegbarkeitTest {

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
  private Query query;
  private Personen testPerson;
  private Verfuegbarkeit testVerfuegbarkeit;
  private Zeit zeit;

  @Before
  public void setUp() {
    entityManager.getTransaction().begin();
    query = entityManager.createNamedQuery("Personen.findAll");
    List<Personen> result = query.getResultList();
    assertNotNull(result);
    assertTrue(result.size() > 1);
    testPerson = result.get(0);

    List<Verfuegbarkeit> verfuegbarkeitList = testPerson.getVerfuegbarkeitList();
    assertNotNull(verfuegbarkeitList);
    assertTrue(verfuegbarkeitList.size() > 1);
    testVerfuegbarkeit = verfuegbarkeitList.get(0);

    query = entityManager.createNamedQuery("Zeit.findAll");

    List<Zeit> zeitList = query.getResultList();
    assertNotNull(zeitList);
    assertTrue(zeitList.size() > 1);
    zeit = zeitList.get(0);


  }

  @After
  public void tearDown() {
    entityManager.getTransaction().rollback();
    query = null;
    testPerson = null;
    testVerfuegbarkeit = null;
  }

  @Test
  public void testMustBeFalseOnCreation() {
    Verfuegbarkeit newItem = new Verfuegbarkeit();
    newItem.setPersonid(testPerson);
    newItem.setZeitid(zeit);
    entityManager.persist(newItem);
    entityManager.flush();

    assertFalse(newItem.isVerfuegbar());

  }

  @Test
  @Ignore("Problems with AWT thread")
  public void testFirePropertyChangeOnSelf() throws InterruptedException, InvocationTargetException {
    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        TestListener testListener = new TestListener();
        testVerfuegbarkeit.addPropertyChangeListener(testListener);

        // change something in the test item.
        boolean newVal = !testVerfuegbarkeit.isVerfuegbar();
        testVerfuegbarkeit.setVerfuegbar(newVal);

        // verify that the test listener got called.
        assertEquals(1, testListener.called);
        assertNotNull(testListener.lastEvent);
        assertEquals(Personen.PROP_VERFUEGBARKEIT, testListener.lastEvent.getPropertyName());


        // remove the test listener 
        testVerfuegbarkeit.removePropertyChangeListener(testListener);
        // change again something in the test item.
        testVerfuegbarkeit.setVerfuegbar(!newVal);
        // verify that the test listener was not called again.
        assertEquals(1, testListener.called);
      }
    });
  }

  @Test
  @Ignore("Problems with AWT thread")
  public void testFirePropertyChangeOnPerson() throws InterruptedException, InvocationTargetException {
    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        TestListener testListener = new TestListener();
        testPerson.addPropertyChangeListener(testListener);

        // change something in the test "Verfugbarkeit".
        boolean newVal = !testVerfuegbarkeit.isVerfuegbar();
        testVerfuegbarkeit.setVerfuegbar(newVal);

        // verify that the test listener got called.
        assertEquals(1, testListener.called);
        assertNotNull(testListener.lastEvent);
        assertEquals(Personen.PROP_VERFUEGBARKEIT, testListener.lastEvent.getPropertyName());


        // remove the test listener 
        testPerson.removePropertyChangeListener(testListener);
        // change again something in the test item.
        testVerfuegbarkeit.setVerfuegbar(!newVal);
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