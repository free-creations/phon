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
 * With this class we try to find out how to unit-test call-backs that happen
 * in the AWT thread.
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
    checkLater(listener, "This test should not fail");
  }

  /**
   * Here we demonstrate how a test failure manifests itself in the test runner.
   * @throws InterruptedException
   * @throws InvocationTargetException 
   */
  @Test
  public void testBadCallLater() throws InterruptedException, InvocationTargetException {
    System.out.println("callLater");
    TestListner listener = new TestListner();
    AsyncronousExperiment instance = new AsyncronousExperiment();
    //no call to : instance.callLater(listener); <== this is the error we want to demonstrate
    checkLater(listener, "This test fails on purpose!");
  }

  private void checkLater(final TestListner listener, final String comment) throws InterruptedException, InvocationTargetException {
    EventQueue.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        assertTrue(listener.wasCalled, comment);
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
