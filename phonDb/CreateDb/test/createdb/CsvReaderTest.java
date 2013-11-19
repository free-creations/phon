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
package createdb;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class CsvReaderTest {

  public CsvReaderTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of head method, of class CsvReader.
   */
  @Test
  public void testHead1() {
    CsvReader instance = new CsvReader(null);
    String expResult = "xyz";
    String result = instance.head(" xyz ");
    assertEquals(expResult, result);
  }

  @Test
  public void testHead2() {
    CsvReader instance = new CsvReader(null);
    String expResult = "";
    String result = instance.head(null);
    assertEquals(expResult, result);
  }

  @Test
  public void testHead3() {
    CsvReader instance = new CsvReader(null);
    String expResult = "xyz";
    String result = instance.head(" xyz , abc");
    assertEquals(expResult, result);
  }

  @Test
  public void testHead4() {
    CsvReader instance = new CsvReader(null);
    String expResult = "";
    String result = instance.head("    ");
    assertEquals(expResult, result);
  }

  @Test
  public void testHead5() {
    CsvReader instance = new CsvReader(null);
    String expResult = "";
    String result = instance.head(" ,   ");
    assertEquals(expResult, result);
  }

  /**
   * Test of tail method, of class CsvReader.
   */
  @Test
  public void testTail1() {
    CsvReader instance = new CsvReader(null);
    String result = instance.tail(" xyz ");
    assertNull(result);
  }

  @Test
  public void testTail2() {
    CsvReader instance = new CsvReader(null);
    String result = instance.tail(null);
    assertNull(result);
  }

  @Test
  public void testTail3() {
    CsvReader instance = new CsvReader(null);
    String result = instance.tail(" xyz , abc");
    String expResult = " abc";
    assertEquals(expResult, result);
  }

  @Test
  public void testTail4() {
    CsvReader instance = new CsvReader(null);
    String result = instance.tail("    ");
    assertNull(result);
  }

  @Test
  public void testTail5() {
    CsvReader instance = new CsvReader(null);
    String result = instance.tail("  ,");
    assertNull(result);
  }

  @Test
  public void testTail6() {
    CsvReader instance = new CsvReader(null);
    String result = instance.tail("  ,,  ");
    String expResult = ",  ";
    assertEquals(expResult, result);
  }

  /**
   * Test of head method, of class CsvReader.
   */
  @Test
  public void testHead() {

  }

  /**
   * Test of tail method, of class CsvReader.
   */
  @Test
  public void testTail() {

  }

  /**
   * Test of part method, of class CsvReader.
   */
  @Test
  public void testPart1() {
    CsvReader instance = new CsvReader(null);
    String expResult = "0,1,2,3";
    String result = instance.part(0, "0,1,2,3");
    assertEquals(expResult, result);
  }

  @Test
  public void testPart2() {
    CsvReader instance = new CsvReader(null);
    String expResult = "3";
    String result = instance.part(3, "0,1,2,3");
    assertEquals(expResult, result);
  }

  @Test
  public void testPart3() {
    CsvReader instance = new CsvReader(null);
    String expResult = "";
    String result = instance.part(500, "0,1,2,3");
    assertEquals(expResult, result);
  }

  /**
   * Test of part method, of class CsvReader.
   */
  @Test
  public void testPart() {

  }

  /**
   * Test of item method, of class CsvReader.
   */
  @Test
  public void testItem() {

  }

  @Test
  public void testItem1() {
    int index = 0;
    CsvReader instance = new CsvReader("0 , 1 , 2 , 3 ");
    String expResult = "0";
    String result = instance.item(index);
    assertEquals(expResult, result);
  }

  @Test
  public void testItem2() {
    int index = 3;
    CsvReader instance = new CsvReader("0 , 1 , 2 , 3 ");
    String expResult = "3";
    String result = instance.item(index);
    assertEquals(expResult, result);
  }

  @Test
  public void testItem3() {
    int index = 3;
    CsvReader instance = new CsvReader("0 , 1 , 2 , 3, ");
    String expResult = "3";
    String result = instance.item(index);
    assertEquals(expResult, result);
  }

  @Test
  public void testItem4() {
    int index = 4;
    CsvReader instance = new CsvReader("0 , 1 , 2 , 3, ");
    String expResult = "";
    String result = instance.item(index);
    assertEquals(expResult, result);
  }
}
