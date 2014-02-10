/*
 * Copyright 2014 Harald Postner<harald at free-creations.de>.
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
package de.free_creations.editors.person;

import de.free_creations.editors.contest.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.windows.WindowManager;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
class ProposeAssignmentAction extends AbstractAction {

  public final Integer personId;
  public final Integer timeSlotId;

  public ProposeAssignmentAction(Integer personId, Integer timeSlotId) {
    super("Propose (other) Task");
    this.personId = personId;
    this.timeSlotId = timeSlotId;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Frame mainWindow = WindowManager.getDefault().getMainWindow();
    ProposeAssignmentDialog dialog = new ProposeAssignmentDialog(mainWindow, personId, timeSlotId);
    dialog.setVisible(true);
  }

}
