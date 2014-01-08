/*
 * Copyright 2014 Harald Postner<harald at free-creations.de>.
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

import de.free_creations.dbEntities.JobType;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class JobTypeCollectionTest {

  public JobTypeCollectionTest() {
  }

  @Before
  public void setUp() throws Exception {
    assertTrue("did you start the database-server?", Manager.isOpen());
  }

  @After
  public void tearDown() {
    Manager.close(false);
  }

  /**
   * Test of jobTypeIds method, of class JobTypeCollection.
   */
  @Test
  public void testJobTypeIds() {
  }

  /**
   * Test of jobNames method, of class JobTypeCollection.
   */
  @Test
  public void testJobNames() {
  }

  /**
   * Test of jobIconNames method, of class JobTypeCollection.
   */
  @Test
  public void testJobIconNames() {
  }

  /**
   * Test of getAll method, of class JobTypeCollection.
   */
  @Test
  public void testGetAll() {
    List<JobType> all = Manager.getJobTypeCollection().getAll();
    assertNotNull(all);
    assertFalse("Bad test- database?", all.isEmpty());
  }

  /**
   * Test of findEntity method, of class JobTypeCollection.
   */
  @Test
  public void testFindEntity() throws Exception {
  }

}
