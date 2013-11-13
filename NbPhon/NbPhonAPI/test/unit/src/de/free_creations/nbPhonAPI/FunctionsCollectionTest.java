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

import de.free_creations.dbEntities.Funktionen;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class FunctionsCollectionTest {

  public FunctionsCollectionTest() {
  }

  @Before
  public void setUp() {
  }

  /**
   * Test of functionNames method, of class FunctionsCollection.
   */
  @Test
  public void testFunctionNames() {
    System.out.println("testFunctionNames");
    assertTrue("did you start the database-server?", Manager.isOpen());
    FunctionsCollection functionsCollection = Manager.getFunctionsCollection();
    String[] functionNames = functionsCollection.functionNames();
    assertNotNull(functionNames);
    assertTrue("Problem in test-database, not enough functions defined.", functionNames.length > 1);
    for (String s : functionNames) {
      System.out.println("..." + s);
    }
  }

  /**
   * Test of getAll method, of class FunctionsCollection.
   */
  @Test
  public void testGetAll() {
  }

  /**
   * Test of functionKeys method, of class FunctionsCollection.
   */
  @Test
  public void testFunctionKeys() {
  }

  /**
   * Test of findEntity method, of class FunctionsCollection.
   */
  @Test
  public void testFindEntity_String() throws Exception {
  }

  /**
   * Test of findEntity method, of class FunctionsCollection.
   */
  @Test
  public void testFindEntity_int() throws Exception {
    System.out.println("testFindEntity_int");
    assertTrue("did you start the database-server?", Manager.isOpen());
    FunctionsCollection functionsCollection = Manager.getFunctionsCollection();
    Funktionen f0 = functionsCollection.findEntity(0);
    assertNotNull(f0);
    Funktionen f1 = functionsCollection.findEntity(1);
    assertNotNull(f1);

    assertTrue(f1.getSortvalue() >= f0.getSortvalue());
  }
}