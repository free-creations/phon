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
package testDb;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class AsyncronousExperimentNGTest {

  public AsyncronousExperimentNGTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @BeforeMethod
  public void setUpMethod() throws Exception {
  }

  @AfterMethod
  public void tearDownMethod() throws Exception {
  }

  /**
   * Test of callLater method, of class AsyncronousExperiment.
   */
  @Test
  public void testCallLater() throws InterruptedException, InvocationTargetException {
    System.out.println("callLater");
    TestListner listener = new TestListner();
    AsyncronousExperiment instance = new AsyncronousExperiment();
    instance.callLater(listener);
    checkLater(listener);
  }

  @Test
  public void testBadCallLater() throws InterruptedException, InvocationTargetException {
    System.out.println("callLater");
    TestListner listener = new TestListner();
    AsyncronousExperiment instance = new AsyncronousExperiment();
    //instance.callLater(listener);
    checkLater(listener);
  }

  private void checkLater(final TestListner listener) throws InterruptedException, InvocationTargetException {
    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        assertTrue(listener.wasCalled);
      }
    });
  }

  private class TestListner implements PropertyChangeListener {

    public boolean wasCalled = false;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      wasCalled = true;
      System.out.println("#### was called");
    }

  }

}
