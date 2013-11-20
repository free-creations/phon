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

import de.free_creations.nbPhonAPI.Manager;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import javax.swing.Icon;
import org.netbeans.spi.actions.AbstractSavable;



/**
 * This class interacts with the org.netbeans.spi.actions utilities managing the
 * commitment of pending changes.
 *
 * @see <a href="https://platform.netbeans.org/tutorials/nbm-crud.html">NetBeans
 Platform CRUD Application Tutorial</a>
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class Committer {

  static private final HashSet<CommittableNode> pending = new HashSet<>();
  static private final Object pendingLock = new Object();
 
  static public class SaveRequester extends AbstractSavable implements Icon {
     private final Icon icon;

    public SaveRequester(){
      BufferedImage image = IconManager.iconManager().iconStar;
      icon = IconManager.iconManager().image2icon(image);
    }

    @Override
    protected String findDisplayName() {
      return String.format("%d changed records.", pending.size());
    }

    @Override
    protected void handleSave() throws IOException {
      if (!EventQueue.isDispatchThread()) {
        throw new IOException("Must be called from AWT thread.");
      }

      synchronized (pendingLock) {
        try {
          Manager.commit();
          final HashSet<CommittableNode> committed = new HashSet<>(pending);
          pending.clear();
          done(committed);
        } catch (Throwable ex) {
          throw new IOException(ex);
        }
      }

    }

    private void done(HashSet<CommittableNode> committed) {
      for (CommittableNode n : committed) {
        n.notifyCommittment();
      }
    }

    /**
     * all save requests are considered equal.
     *
     * So a new save request will replace its predecessor, only the last save
     * request will be executed.
     *
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
      return other instanceof SaveRequester;
    }

    /**
     * all save requests are considered equal.
     *
     * They all have the same hash code.
     *
     * @return
     */
    @Override
    public int hashCode() {
      return 0;
    }

    public void requestCommit() {
      register();
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      icon.paintIcon(c, g, x, y);
    }

    @Override
    public int getIconWidth() {
      return icon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
      return icon.getIconHeight();
    }
  }

  public static void requestCommit(CommittableNode n) {
    synchronized (pendingLock) {
      pending.add(n);
    }
    SaveRequester r = new SaveRequester();
    r.requestCommit();
  }
}
