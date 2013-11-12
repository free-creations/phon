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

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class PropertyChangeManagerTest {

  private class TestListener implements PropertyChangeListener {

    public int called = 0;
    public PropertyChangeEvent lastEvent = null;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      called++;
      lastEvent = evt;
    }
  }

  /**
   * Test of firePropertyChange method, of class PropertyChangeManager.
   */
  @Test
  public void testAddFireRemove() {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        System.out.println("testAddFireRemove");
        TestListener testListener = new TestListener();
        assertEquals(0, testListener.called);

        EntityIdentity i = new EntityIdentity(Personen.class, 123);

        PropertyChangeManager.instance().addPropertyChangeListener(testListener, i);
        PropertyChangeManager.instance().firePropertyChange(i, "TESTPROP", null, null);

        assertEquals(1, testListener.called);
        assertEquals("TESTPROP", testListener.lastEvent.getPropertyName());

        PropertyChangeManager.instance().removePropertyChangeListener(testListener, i);
        PropertyChangeManager.instance().firePropertyChange(i, "TESTPROP", null, null);
        assertEquals(1, testListener.called);
      }
    });

  }
}