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
package de.free_creations.editors.contest;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.windows.WindowManager;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
class ProposeAllocationAction extends AbstractAction {

  public final Integer eventId;
  public final String jobId;

  public ProposeAllocationAction(Integer eventId, String jobId) {
    super("Propose (other) Person");
    this.eventId = eventId;
    this.jobId = jobId;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Frame mainWindow = WindowManager.getDefault().getMainWindow();
    ProposeAllocationDialog dialog = new ProposeAllocationDialog(mainWindow, eventId, jobId);
    dialog.setVisible(true);
  }

}
