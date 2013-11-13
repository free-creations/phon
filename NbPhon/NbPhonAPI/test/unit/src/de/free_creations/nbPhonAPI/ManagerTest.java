/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.free_creations.nbPhonAPI;

import javax.persistence.EntityManager;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Make sure to start the database-server before running the tests.
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class ManagerTest {

  public ManagerTest() {
  }

  /**
   * Test of getEntityManager method, of class Manager.
   */
  @Test
  public void testOpenCloseDatabaseConnection() {

    EntityManager testManager = null;
    try {
      testManager = Manager.getEntityManager();
    } catch (Exception ex) {
      fail("Did you start the database???");
    }
    assertNotNull(testManager);
    
    Manager.close(false);

  }

  /**
   * Test of getEntityManager method, of class Manager.
   */
  @Test
  public void testGetPersonCollection() {

    EntityManager testManager = null;
    try {
      testManager = Manager.getEntityManager();
    } catch (Exception ex) {
      fail("Did you start the database???");
    }
    PersonCollection personCollection = Manager.getPersonCollection();
    assertNotNull(personCollection);
    
    Manager.close(false);

  }
}