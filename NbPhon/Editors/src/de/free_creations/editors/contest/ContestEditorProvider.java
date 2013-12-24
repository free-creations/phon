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
package de.free_creations.editors.contest;

import org.openide.windows.CloneableTopComponent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
@org.openide.util.lookup.ServiceProvider(
        service=de.free_creations.nbPhon4Netbeans.ContestEditorProvider.class,
        position=100)
public class ContestEditorProvider implements de.free_creations.nbPhon4Netbeans.ContestEditorProvider {



  @Override
  public CloneableTopComponent getEditor(boolean newWindow, Integer key) {
    WindowManager windowManager = WindowManager.getDefault();
    ContestTopComponent editor = null;
    if(!newWindow){
      TopComponent foundTC = windowManager.findTopComponent("ContestTopComponent");
      if(foundTC instanceof ContestTopComponent){
        editor = (ContestTopComponent)foundTC;
      }
    }
    if(editor == null){
     editor = new ContestTopComponent(key);
    }
    editor.viewContestRecord(key);
    editor.open();
    editor.requestActive();
    return editor;  }
  
}
