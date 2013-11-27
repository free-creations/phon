/*
 * Copyright 2013 Harald Postner<harald at free-creations.de>.
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class LocationNGTest {

  private class TestListener implements PropertyChangeListener {

    public int called = 0;
    public PropertyChangeEvent lastEvent = null;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      called++;
      lastEvent = evt;
    }
  }

  static final String PersistenceUnitName = "DbEntitiesPU";
  private static EntityManager entityManager = null;

  private Location testLocation;

  @BeforeClass
  public static void setUpClass() throws Exception {
    try {
      EntityManagerFactory factory = Persistence.createEntityManagerFactory(PersistenceUnitName);
      entityManager = factory.createEntityManager();
    } catch (javax.persistence.PersistenceException ex) {
      fail("+++ Please start the database server and try again.");

    }
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    if (entityManager != null) {
      entityManager.close();
    }
  }

  @BeforeMethod
  public void setUpMethod() throws Exception {
    entityManager.getTransaction().begin();
    // verify that we can retrieve the enties from the database (we assume that a suitable test-database is used)
    TypedQuery<Location> q = entityManager.createNamedQuery("Location.findAll", Location.class);
    List<Location> ll = q.getResultList();
    assertNotNull(ll);
    assertFalse(ll.isEmpty());

    // the first item will be used for further testing (we assume that a suitable test-database is used)
    testLocation = ll.get(0);
  }

  @AfterMethod
  public void tearDownMethod() throws Exception {
    entityManager.getTransaction().rollback();
  }

  @Test
  public void testIdentity() {
    EntityIdentity expected = new EntityIdentity(testLocation.getClass(), testLocation.getLocationId());
    assertEquals(expected, testLocation.identity());
  }

  @Test
  public void testPropertyChangeCallback() throws Throwable  {

    Location testItem = new Location(Integer.MAX_VALUE);
    final TestListener testListener = new TestListener();
    testItem.addPropertyChangeListener(testListener);

    testItem.setName("Abc"); // 1
    testItem.setBuilding("Town hall"); // 2
    testItem.setTown("AbcCity"); // 3
    testItem.setStreet("Abc Lane"); // 4
    testItem.setRoom("Room 123"); // 5
    testItem.setGridnumber("X1"); // 6
    final int expectedCallbackCount = 6;

    try {
      EventQueue.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          assertEquals(testListener.called, expectedCallbackCount );
        }
      });
    } catch (InvocationTargetException ex) {
      throw ex.getCause();
    }

  }

}
