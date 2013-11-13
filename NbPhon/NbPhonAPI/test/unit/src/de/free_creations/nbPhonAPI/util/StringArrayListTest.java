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
package de.free_creations.nbPhonAPI.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**

 * @author Harald Postner <Harald at free-creations.de>
 */
public class StringArrayListTest {

  /**
   * Test of put method, of class StringArrayList.
   */
  @Test
  public void testPut() {
    StringArrayList testItem = new StringArrayList();
    testItem.put(2, "abc");
    assertEquals("abc", testItem.get(2));
    assertEquals("", testItem.get(1));
    assertEquals("", testItem.get(0));
    testItem.put(2, "def");
    assertEquals("def", testItem.get(2));
    assertEquals(3, testItem.size());
  }

  /**
   * Test of toArray method, of class StringArrayList.
   */
  @Test
  public void testToArray() {
    StringArrayList testItem = new StringArrayList();
    String[] expectedEmpty = testItem.toArray();
    assertNotNull(expectedEmpty);
    assertEquals(0, expectedEmpty.length);

    testItem.put(2, "abc");
    String[] expectedNotEmpty = testItem.toArray();
    assertNotNull(expectedNotEmpty);
    assertEquals(3, expectedNotEmpty.length);
  }
}