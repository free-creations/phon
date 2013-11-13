/*
 * Copyright 2013 harald.
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
package de.free_creations.explorers;

import de.free_creations.nbPhon4Netbeans.JuryRootNode;
import de.free_creations.nbPhonAPI.JuryCollection;
import de.free_creations.nbPhonAPI.Manager;
import java.awt.Cursor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * A window which displays the list of all juries registered in the database.
 */
@ConvertAsProperties(
        dtd = "-//de.free_creations.explorers//Jury//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "JuryListTopComponent",
        iconBase = "de/free_creations/explorers/resources/chairs.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true,position = 200)
@ActionID(category = "Window", id = "de.free_creations.explorers.JuryListTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_JuryListAction",
        preferredID = "JuryListTopComponent")
@Messages({
  "CTL_JuryListAction=Show all Juries",
  "CTL_JuryListTopComponent=Juries",
  "HINT_JuryListTopComponent=All juries registered in the database"
})
public final class JuryListTopComponent extends TopComponent
        implements ExplorerManager.Provider {

  private static final Logger logger = Logger.getLogger(JuryListTopComponent.class.getName());
  private static ExplorerManager explorerManager = new ExplorerManager();
  private DatabaseActivationTask databaseActivationTask = null;

  private class DatabaseActivationTask extends SwingWorker<Void, Void> {

    private final ProgressHandle progressHandle;
    JuryRootNode juryRootNode = null;

    public DatabaseActivationTask() {
      super();
      progressHandle = ProgressHandleFactory.createHandle("Connecting to Database.");
    }

    @Override
    protected Void doInBackground() throws Exception {
      try {
        progressHandle.start();
        JuryCollection jj = Manager.getJuryCollection();
        juryRootNode = new JuryRootNode(jj);
      } catch (Throwable ex) {
        logger.log(Level.SEVERE, "Could not access the database.", ex);
      }
      return null;
    }

    @Override
    protected void done() {
      Manager.assertOpen();
      if (juryRootNode != null) {
        explorerManager.setRootContext(juryRootNode);
      }
      scrollPane.setCursor(null);
      progressHandle.finish();
    }
  }

  public JuryListTopComponent() {
    initComponents();
    setName(Bundle.CTL_JuryListTopComponent());
    //setToolTipText(Bundle.HINT_JuryListTopComponent());
    putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
    //ListView listView = (ListView) scrollPane;

    BeanTreeView beanTreeView = (BeanTreeView) scrollPane;
    beanTreeView.setRootVisible(false);
    associateLookup(ExplorerUtils.createLookup(explorerManager, getActionMap()));
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    scrollPane = new org.openide.explorer.view.BeanTreeView();

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
    );
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane scrollPane;
  // End of variables declaration//GEN-END:variables
  private final Object databaseActivationTaskLock = new Object();

  @Override
  public void componentOpened() {
    super.componentOpened();
    synchronized (databaseActivationTaskLock) {
      if (databaseActivationTask == null) {
        scrollPane.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        databaseActivationTask = new DatabaseActivationTask();
        databaseActivationTask.execute();
      }
    }
  }

  @Override
  public void componentClosed() {
    // TODO add custom code on component closing
  }

  void writeProperties(java.util.Properties p) {
    // better to version settings since initial version as advocated at
    // http://wiki.apidesign.org/wiki/PropertyFiles
    p.setProperty("version", "1.0");
    // TODO store your settings
  }

  void readProperties(java.util.Properties p) {
    String version = p.getProperty("version");
    // TODO read your settings according to their version
  }

  @Override
  public ExplorerManager getExplorerManager() {
    return explorerManager;
  }
}
