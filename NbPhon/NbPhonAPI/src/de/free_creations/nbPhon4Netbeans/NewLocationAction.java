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
package de.free_creations.nbPhon4Netbeans;

import de.free_creations.dbEntities.Location;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.LocationCollection;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Edit",
        id = "de.free_creations.jmNetbeans.NewLocationAction")
@ActionRegistration(
        iconBase = "de/free_creations/nbPhon4Netbeans/resources/newLocation.png",
        displayName = "#CTL_newLocation")
@ActionReferences({
  @ActionReference(path = "Menu/Edit", position = 0),
  @ActionReference(path = "Toolbars/File", position = 400)

})
@Messages("CTL_newLocation=new Location")
public final class NewLocationAction extends AbstractAction {

  private static final Logger logger = Logger.getLogger(NewLocationAction.class.getName());

  public NewLocationAction() {
    super("new Location");
  }

  @Override
  @SuppressWarnings("UseSpecificCatch")
  public void actionPerformed(ActionEvent e) {
    try {
      LocationCollection locationCollection = Manager.getLocationCollection();
      Location newLocation = locationCollection.newEntity();
      LocationEditorProvider provider =
              Lookup.getDefault().lookup(
              LocationEditorProvider.class);
      if (provider != null) {
        provider.getEditor(true, newLocation.getLocationId());
      } else {
        throw new RuntimeException("No Editor provider found.");
      }
    } catch (DataBaseNotReadyException ex) {
      NotifyDescriptor.Message message = new NotifyDescriptor.Message(
              "Cannot access the database.\n Try to re-start the server.",
              NotifyDescriptor.ERROR_MESSAGE);
      DialogDisplayer.getDefault().notify(message);
      logger.log(Level.INFO, "Could not create new location record.", ex);
    } catch (Throwable ex) {
      logger.log(Level.SEVERE, "Could not create new location record.", ex);
    }
  }
}
