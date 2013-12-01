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
package de.free_creations.editors.person;


import de.free_creations.nbPhonAPI.Manager;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class JuryComboBox extends JComboBox<String> {

  public JuryComboBox() {
    super();
    JuryComboBoxModel model = new JuryComboBoxModel();
    setModel(model);
    setEditable(true);
  }

  public class JuryComboBoxModel extends DefaultComboBoxModel<String> {

    public JuryComboBoxModel() {
      super();
      if (java.beans.Beans.isDesignTime()) {

        addElement("KLAVIER-STREICH");
        JuryComboBoxModel.this.setSelectedItem("KLAVIER-STREICH");

      } else {
        List<String> juryTypes = Manager.getContestCollection().contestTypes();
        for (String jt : juryTypes) {
          addElement(jt);
        }
        addElement(null);
      }
    }
    /**
     * 
     * @return null if the selected item was blanked out.
     */
    @Override
    public Object getSelectedItem(){
      Object selectedItem = super.getSelectedItem();
      if(selectedItem instanceof String){
        String s = (String)selectedItem;
        s = s.trim();
        if(s.isEmpty()){
          return null;
        }        
      }
      return selectedItem;
      
    }
  }
}
