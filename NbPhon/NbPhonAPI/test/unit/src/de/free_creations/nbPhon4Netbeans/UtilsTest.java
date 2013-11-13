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

import org.junit.Test;
import static de.free_creations.nbPhon4Netbeans.Utils.isInstanceOf;
import static de.free_creations.nbPhon4Netbeans.Utils.integerCompareNull;
import static de.free_creations.nbPhon4Netbeans.Utils.stringCompareNull;
import static de.free_creations.nbPhon4Netbeans.Utils.typeCheckCompare;
import static de.free_creations.nbPhon4Netbeans.Utils.bothValid;
import static org.junit.Assert.*;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class UtilsTest {

  private class TestClass {
  }

  private class OtherClass {
  }

  private class OtherClass2 {
  }

  private class TestSubClass extends TestClass {
  }

  /**
   * Test of isInstanceOf method, of class Utils.
   */
  @Test
  public void testIsInstanceOf() {
    TestClass testClass = new TestClass();
    OtherClass otherClass = new OtherClass();
    TestSubClass testSubClass = new TestSubClass();

    assertFalse(isInstanceOf(null, TestClass.class));
    assertTrue(isInstanceOf(testClass, TestClass.class));
    assertFalse(isInstanceOf(otherClass, TestClass.class));
    assertTrue(isInstanceOf(testSubClass, TestClass.class));
  }

  /**
   * Test of stringCompareNull method, of class Utils.
   */
  @Test
  public void testStringCompareNull() {
    String s1 = "A";
    String s2 = "B";

    assertEquals(0, stringCompareNull(s1, s1));
    assertEquals(0, stringCompareNull(null, null));

    assertEquals(-1, stringCompareNull(s1, s2));
    assertEquals(1, stringCompareNull(s2, s1));

    assertEquals(-1, stringCompareNull(null, s1));
    assertEquals(1, stringCompareNull(s1, null));
  }

  /**
   * Test of integerCompareNull method, of class Utils.
   */
  @Test
  public void testIntegerCompareNull() {
    assertEquals(0, integerCompareNull(123, 123));
    assertEquals(0, integerCompareNull(null, null));

    assertEquals(-1, integerCompareNull(-2, 5));
    assertEquals(1, integerCompareNull(25, 5));

    assertEquals(-1, integerCompareNull(null, 5));
    assertEquals(1, integerCompareNull(25, null));
  }

  /**
   * Test of typeCheckCompare method, of class Utils.
   */
  @Test
  public void testTypeCheckCompare() {
    TestClass testClass = new TestClass();
    OtherClass otherClass = new OtherClass();
    OtherClass2 otherClass2 = new OtherClass2();
    TestSubClass testSubClass = new TestSubClass();

    assertEquals(bothValid, typeCheckCompare(testClass, testSubClass, TestClass.class));

    assertEquals(0, typeCheckCompare(otherClass, otherClass2, TestClass.class));
    assertEquals(0, typeCheckCompare(otherClass, null, TestClass.class));
    assertEquals(0, typeCheckCompare(null, otherClass, TestClass.class));
    assertEquals(0, typeCheckCompare(null, null, TestClass.class));

    assertEquals(1, typeCheckCompare(testClass, null, TestClass.class));
    assertEquals(-1, typeCheckCompare(null, testClass, TestClass.class));



  }
}