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

import static de.free_creations.dbEntities.TimeSlotTest.PersistenceUnitName;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
public class EntityManagerTest {

  public EntityManagerTest() {
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

  @Test
  @SuppressWarnings("CallToThreadDumpStack")
  public void createEntityManager() {
    try{
      EntityManagerFactory factory = Persistence.createEntityManagerFactory(PersistenceUnitName);
      EntityManager entityManager = factory.createEntityManager();
      entityManager.close();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail("+++ Please start the database server and try again.");

    }
  }
}
