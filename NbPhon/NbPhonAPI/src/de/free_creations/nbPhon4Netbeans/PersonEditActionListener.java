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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.cookies.EditCookie;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Edit",
        id = "de.free_creations.jmNetbeans.PersonEditActionListener")
@ActionRegistration(
        displayName = "#CTL_PersonEditActionListener")
@ActionReference(path = "Menu/Edit", position = 100)
@Messages("CTL_PersonEditActionListener=Edit Person Record")
public final class PersonEditActionListener implements ActionListener {

  private final EditCookie context;

  public PersonEditActionListener(EditCookie context) {
    this.context = context;
  }

  @Override
  public void actionPerformed(ActionEvent ev) {
    context.edit();
  }
}
