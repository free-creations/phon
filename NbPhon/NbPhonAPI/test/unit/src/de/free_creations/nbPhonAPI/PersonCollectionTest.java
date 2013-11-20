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

import de.free_creations.dbEntities.EntityIdentity;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.Availability;
import de.free_creations.dbEntities.TimeSlot;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class PersonCollectionTest {

  @After
  public void tearDown() {
    Manager.close(false);
  }

  /**
   * When applying the method getAll() against a non empty PERSONEN table
   * (that's assumed in our test database), the returned list must not be empty.
   */
  @Test
  public void getAll_mustNotBeEmpty() {
    System.out.println("getAll_mustNotBeEmpty");
    PersonCollection testItem = Manager.getPersonCollection();
    List<Person> allPersons = testItem.getAll();
    assertNotNull(allPersons);
    assertFalse(allPersons.isEmpty());
  }

  /**
   * When applying the method find() the returned references must always be the
   * same (within one transaction)
   */
  @Test
  public void find_mustAlwaysReturnTheSameEntity() throws DataBaseNotReadyException {
    PersonCollection cc = Manager.getPersonCollection();
    List<Person> allPersons = cc.getAll();

    Person testPerson = allPersons.get(0);
    int key = testPerson.getPersonid();

    Person fromFind = cc.findEntity(key);
    assertTrue(testPerson == fromFind);

    testPerson.setFamilienname("Test");
    Person fromFind2 = cc.findEntity(key);
    assertEquals("Test", fromFind2.getFamilienname());
    ///
    /// BUT NOTE
    ///
    Manager.rollback();
    Person fromFind3 = cc.findEntity(key);
    assertTrue(testPerson != fromFind3); //!!!!!!!

  }

  /**
   * When applying the method getAll() several times the returned elements must
   * be identic i.e reference the same objects.
   */
  @Test
  public void getAll_mustReturnTheSameElemets() {
    System.out.println("getAll_mustReturnTheSameElemets");
    PersonCollection testItem = Manager.getPersonCollection();
    List<Person> allPersons_1 = testItem.getAll();
    List<Person> allPersons_2 = testItem.getAll();
    // check that the first entry refernces the same record
    assertEquals(allPersons_1.get(0).getPersonid(), allPersons_2.get(0).getPersonid());
    // check pointer identity
    assertTrue(allPersons_1.get(0) == allPersons_2.get(0));
  }

  /**
   * When applying the method getAll() the returned elements must all be
   * attached to the current persistency context.
   */
  @Test
  public void getAll_mustReturnObjectsFromPersitencyContext() throws DataBaseNotReadyException {
    System.out.println("getAll_mustReturnObjectsFromPersitencyContext");
    PersonCollection testItem = Manager.getPersonCollection();
    List<Person> allPersons = testItem.getAll();
    Person personFromList = allPersons.get(0);

    Integer key = personFromList.getPersonid();
    Person personFromContext = Manager.getEntityManager().find(Person.class, key);

    assertTrue(personFromContext == personFromList);
  }

  /**
   * When applying the method newPerson() the returned "Person"- entity must
   * be attached to the current persistency context.
   */
  @Test
  public void newPerson_mustReturnObjectsFromPersitencyContext() throws DataBaseNotReadyException {
    System.out.println("newPerson_mustReturnObjectsFromPersitencyContext");
    PersonCollection testItem = Manager.getPersonCollection();

    Person newPerson = testItem.newEntity();

    assertTrue(Manager.getEntityManager().contains(newPerson));

    //uncomment the next line to see the changes in the database.
    //Manager.close();
  }

  /**
   * When applying the method newPerson() the returned "Person"- entity must
   * have a valid key.
   */
  @Test
  public void newPerson_mustHaveValidKey() throws DataBaseNotReadyException {
    System.out.println("newPerson_mustHaveValidKey");
    PersonCollection testItem = Manager.getPersonCollection();

    Person newPerson = testItem.newEntity();
    newPerson.setFamilienname("UNIT TEST: newPerson_mustHaveValidKey");
    int personid = newPerson.getPersonid();

    System.out.println("...newPersonId=" + personid);

    assertTrue(personid > 1); // we assume there was at least one record in the dB
  }

  /**
   * When applying the method newPerson() the returned "Person"- entity must
   * have all disponibility (Availability) records attached.
   */
  @Test
  public void newPerson_mustHaveTimeSlots() throws DataBaseNotReadyException {
    System.out.println("newPerson_mustHaveTimeSlots");
    PersonCollection testItem = Manager.getPersonCollection();

    Person newPerson = testItem.newEntity();
    List<Availability> verfuegbarkeitList = newPerson.getVerfuegbarkeitList();

    // verify that the verfuegbarkeitList exists and is not empty
    assertNotNull(verfuegbarkeitList);
    assertFalse(verfuegbarkeitList.isEmpty());

    // verify that all the dependencies are correctly linked
    for (Availability v : verfuegbarkeitList) {
      assertEquals(v.getPersonid(), newPerson);
      TimeSlot z = v.getZeitid();
      assertTrue(z.getVerfuegbarkeitList().contains(v));
    }
  }

  /**
   * When applying the method newPerson() the list returned by getAll() must
   * grow in size... and the new item must be an element of the list.
   */
  @Test
  public void newPerson_ListMustGrow() throws DataBaseNotReadyException {
    System.out.println("newPerson_ListMustGrow");
    PersonCollection testItem = Manager.getPersonCollection();
    List<Person> listBefore = testItem.getAll();
    int sizeBefore = listBefore.size();

    Person newPerson = testItem.newEntity();

    List<Person> listAfter = testItem.getAll();
    int sizeAfter = listAfter.size();

    assertEquals(sizeBefore + 1, sizeAfter);

    boolean found = false;
    for (Person p : listAfter) {
      if (p == newPerson) {
        found = true;
      }
    }
    assertTrue(found);

  }
  private int listnerCalledCount = 0; // used in newPerson_MustInformListeners()
  private     Object newValue = null;

  /**
   * When the method newPerson() has executed all listeners must be informed.
   */
  @Test
  public void newPerson_MustInformListeners() throws DataBaseNotReadyException {
    System.out.println("newPerson_MustInformListeners");

    listnerCalledCount = 0;

    PropertyChangeListener listener = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (PersonCollection.PROP_ITEM_ADDED.equals(evt.getPropertyName())) {
          listnerCalledCount++;
          newValue = evt.getNewValue();
        }
      }
    };
    PersonCollection testItem = Manager.getPersonCollection();
    testItem.addPropertyChangeListener(listener);
    
    final EntityIdentity identity = testItem.newEntity().identity();
    // the verification whether the listener has been called must execute on the
    // AWT thread.
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        assertTrue(listnerCalledCount == 1);
        assertTrue(identity.equals(newValue));

      }
    });


  }
}