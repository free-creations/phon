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
package de.free_creations.nbPhon4Netbeans;

import de.free_creations.dbEntities.Personen;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class PersonCompareTest {

  private class CollectionMock implements MutableEntityCollection<Personen, Integer> {

    public Personen p1 = null;
    public Personen p2 = null;

    @Override
    public List<Personen> getAll() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Personen findEntity(Integer key) throws DataBaseNotReadyException {
      switch (key) {
        case 1:
          return p1;
        case 2:
          return p2;
      }
      return null;

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    @Override
    public Personen newEntity() throws DataBaseNotReadyException {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeEntity(Integer key) throws DataBaseNotReadyException {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  };
  private CollectionMock personCollectionMock = new CollectionMock();

  /**
   * Test of PersonCompare.byName
   */
  @Test
  public void testByNameNullRecords() {
    PersonCompare.PersonComparator testComparator = PersonCompare.byName(personCollectionMock);
    PersonNode np1 = new PersonNode(1, personCollectionMock);
    PersonNode np2 = new PersonNode(2, personCollectionMock);


    // must accept null records
    int result = testComparator.compare(null, null);
    assertEquals(0, result);

    result = testComparator.compare(np1, null);
    assertTrue(result > 0);

    result = testComparator.compare(null, np2);
    assertTrue(result < 0);

    // must accept null person-records
    // all names null
    result = testComparator.compare(np1, np2);
    assertEquals(0, result);

    // one family name not null
    Personen p1 = new Personen();
    personCollectionMock.p1 = p1;
    p1.setFamilienname("A");
    result = testComparator.compare(np1, np2);
    assertTrue(result > 0);

    result = testComparator.compare(np2, np1);
    assertTrue(result < 0);

    // both family names not null
    Personen p2 = new Personen();
    personCollectionMock.p2 = p2;
    p2.setFamilienname("A");
    result = testComparator.compare(np1, np2);
    assertEquals(0, result);

    // one first name not null
    p1.setVorname("A");
    result = testComparator.compare(np1, np2);
    assertTrue(result > 0);

    result = testComparator.compare(np2, np1);
    assertTrue(result < 0);

    // both first names not null
    p2.setVorname("A");
    result = testComparator.compare(np1, np2);
    assertEquals(0, result);

    p1.setVorname("B");
    result = testComparator.compare(np1, np2);
    assertTrue(result > 0);
    result = testComparator.compare(np2, np1);
    assertTrue(result < 0);

    p2.setFamilienname("B");
    result = testComparator.compare(np1, np2);
    assertTrue(result < 0);

    result = testComparator.compare(np2, np1);
    assertTrue(result > 0);
  }
}