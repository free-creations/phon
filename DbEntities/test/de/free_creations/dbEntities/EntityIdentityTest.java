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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class EntityIdentityTest {

  public EntityIdentityTest() {
  }

  /**
   * Test of hashCode method, of class EntityIdentity.
   */
  @Test
  public void testHashCode() {
    System.out.println("hashCode");

    EntityIdentity p1 = new EntityIdentity(Personen.class, new Integer(123));
    EntityIdentity p2 = new EntityIdentity(Personen.class, new Integer(123));
    EntityIdentity p3 = new EntityIdentity(Personen.class, new Integer(456));
    EntityIdentity z1 = new EntityIdentity(Zeit.class, new Integer(123));
    
    assertEquals(p1.hashCode(), p2.hashCode());
    assertTrue(p1.hashCode() != p3.hashCode());
    assertTrue(p1.hashCode() != z1.hashCode());


  }

  /**
   * Test of equals method, of class EntityIdentity.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
    Integer key1 = new Integer(123);    
    Integer key2 = new Integer(123);
    assertNotSame(key1, key2);
    
    EntityIdentity p1 = new EntityIdentity(Personen.class, key1);
    EntityIdentity p2 = new EntityIdentity(Personen.class, key2);
    EntityIdentity p3 = new EntityIdentity(Personen.class, new Integer(456));
    EntityIdentity z1 = new EntityIdentity(Zeit.class, new Integer(123));

    assertTrue(p1.equals(p2));
    assertTrue(p2.equals(p1));

    assertFalse(p1.equals(p3));
    assertFalse(p3.equals(p1));

    assertFalse(p1.equals(z1));


  }
}