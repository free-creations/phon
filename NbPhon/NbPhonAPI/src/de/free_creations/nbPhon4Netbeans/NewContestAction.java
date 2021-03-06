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

import de.free_creations.dbEntities.Contest;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.ContestCollection;
import de.free_creations.nbPhonAPI.Manager;
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
        id = "de.free_creations.jmNetbeans.NewJuryAction")
@ActionRegistration(
        iconBase = "de/free_creations/nbPhon4Netbeans/resources/newJury.png",
        displayName = "#CTL_newJury")
@ActionReferences({
  @ActionReference(path = "Menu/Edit", position = 0),
  @ActionReference(path = "Toolbars/File", position = 350),
  //@ActionReference(path = "Shortcuts", name = "D-ENTER")
})
@Messages("CTL_newJury=new Contest")
public final class NewContestAction extends AbstractAction {

  private static final Logger logger = Logger.getLogger(NewContestAction.class.getName());

  public NewContestAction() {
    super("new Contest");
  }

  @Override
  @SuppressWarnings("UseSpecificCatch")
  public void actionPerformed(ActionEvent e) {
    try {
      ContestCollection personCollection = Manager.getContestCollection();
      Contest newJury = personCollection.newEntity();
      ContestEditorProvider provider =
              Lookup.getDefault().lookup(
              ContestEditorProvider.class);
      if (provider != null) {
        provider.getEditor(true, newJury.getContestId());
      } else {
        throw new RuntimeException("No Editor provider found.");
      }
    } catch (DataBaseNotReadyException ex) {
      NotifyDescriptor.Message message = new NotifyDescriptor.Message(
              "Cannot access the database.\n Try to re-start the server.",
              NotifyDescriptor.ERROR_MESSAGE);
      DialogDisplayer.getDefault().notify(message);
      logger.log(Level.INFO, "Could not create new person record.", ex);
    } catch (Throwable ex) {
      logger.log(Level.SEVERE, "Could not create new person record.", ex);
    }
  }
}
