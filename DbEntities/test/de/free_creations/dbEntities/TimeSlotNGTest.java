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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.Locale;
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
public class TimeSlotNGTest {

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

  private TimeSlot firstTimeSlot;
  private TimeSlot lastTimeSlot;

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
    TypedQuery<TimeSlot> q = entityManager.createNamedQuery("TimeSlot.findAll", TimeSlot.class);
    List<TimeSlot> tt = q.getResultList();
    assertNotNull(tt);
    assertFalse(tt.isEmpty());

    // the first item will be used for further testing (we assume that a suitable test-database is used)
    firstTimeSlot = tt.get(0);
    lastTimeSlot = tt.get(tt.size() - 1);
  }

  @AfterMethod
  public void tearDownMethod() throws Exception {
    entityManager.getTransaction().rollback();
  }

  @Test
  public void testIdentity() {
    EntityIdentity expected = new EntityIdentity(firstTimeSlot.getClass(), firstTimeSlot.getTimeSlotId());
    assertEquals(expected, firstTimeSlot.identity());
  }

  @Test
  public void testPropertyChangeCallback() throws Throwable {
    // TimeSlot has no setters
  }

  @Test
  public void testDateTime() throws Throwable {

    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd-MMMM-yyyy", Locale.GERMAN);
    String date1 = DATE_FORMAT.format(firstTimeSlot.getDatum());
    String date2 = DATE_FORMAT.format(lastTimeSlot.getDatum());
    System.out.println("### " + date1);
    System.out.println("### " + date2);

    SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("kk:mm", Locale.GERMAN);
    String time1 = TIME_FORMAT.format(firstTimeSlot.getStartTime());
    String time2 = TIME_FORMAT.format(lastTimeSlot.getEndTime());
    System.out.println("### " + time1);
    System.out.println("### " + time2);
  }

}
