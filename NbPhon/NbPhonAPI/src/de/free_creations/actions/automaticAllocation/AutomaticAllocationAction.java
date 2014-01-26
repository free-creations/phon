/*
 * Copyright 2014 harald.
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
package de.free_creations.actions.automaticAllocation;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

@ActionID(
        category = "Edit",
        id = "de.free_creations.actions.automaticAllocation.AutomaticAllocationAction"
)
@ActionRegistration(
        iconBase = "de/free_creations/actions/automaticAllocation/allocate24.png",
        displayName = "#CTL_AutomaticAllocationAction"
)
@ActionReferences({
  @ActionReference(path = "Menu/Edit", position = -100),
  @ActionReference(path = "Toolbars/File", position = 1000)
})
@Messages("CTL_AutomaticAllocationAction=Allocate")
public final class AutomaticAllocationAction implements ActionListener {

  @Override
  public void actionPerformed(ActionEvent e) {
    Frame mainWindow = WindowManager.getDefault().getMainWindow();
    AutomaticAllocationDialog dialog = new AutomaticAllocationDialog(mainWindow, true);
    dialog.setVisible(true);
  }
}
