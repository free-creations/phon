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
import java.util.Date;
import java.util.List;
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
public class PersonNGTest {

  private class TestListener implements PropertyChangeListener {

    public int called = 0;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      called++;
    }
  }

  static final String PersistenceUnitName = "DbEntitiesPU";
  private static EntityManager entityManager = null;

  private Person testPerson;

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
    TypedQuery<Person> q = entityManager.createNamedQuery("Person.findAll", Person.class);
    List<Person> pp = q.getResultList();
    assertNotNull(pp);
    assertFalse(pp.isEmpty());

    // the first item will be used for further testing (we assume that a suitable test-database is used)
    testPerson = pp.get(0);
  }

  @AfterMethod
  public void tearDownMethod() throws Exception {
    entityManager.getTransaction().rollback();
  }

  @Test
  public void testIdentity() {
    EntityIdentity expected = new EntityIdentity(testPerson.getClass(), testPerson.getPersonId());
    assertEquals(expected, testPerson.identity());
  }

  @Test
  public void testPropertyChangeCallback() throws Throwable  {

    Person testItem = new Person(Integer.MAX_VALUE);
    final TestListener testListener = new TestListener();
    testItem.addPropertyChangeListener(testListener);

    testItem.setAgegroup("QuiteOld");//1    
    testItem.setCity("North Pole");//2
    testItem.setEmail("SantaClaus@Huskymail.ca");//3
    testItem.setGender("male");//4
    testItem.setGivenname("Claus");//5
    testItem.setLastchange(new Date());//6
    testItem.setMobile("0173 000001");//7
    testItem.setNotice("Ho ho ho");//8
    testItem.setStreet("1 Reindeer Lane ");//9
    testItem.setSurname("Santa");// 10 
    testItem.setTelephone("000 0000 001");// 11
    testItem.setZipcode("H0H 0H0 ");// 12
   
    
    final int expectedCallbackCount = 12;

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
