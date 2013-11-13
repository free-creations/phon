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
package de.free_creations.nbPhonAPI.util;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An extension of Java's ArrayList.
 * 
 * Permits to put a value at a given index. if the new value does not fit
 * into the existing array the array automatically grows to accommodate the
 * value at the given index.
 * 
 * @author Harald Postner <Harald at free-creations.de>
 */
public class StringArrayList extends ArrayList<String> {
  private static final Logger logger = Logger.getLogger(StringArrayList.class.getName());
  private static final String emptyString = "";
  /**
   * Put the given value at the position indicated by the index.
   * 
   * If the array is currently too small to hold the value a number
   * of empty strings is inserted accordingly.
   * @param index
   * @param value 
   */
  public void put(int index, String value){
    if(index<0){
      logger.log(Level.WARNING, "got negative index");
      return;
    }
    grow(index);
    set(index, value);
  }
  
  private void grow(int index){
    int requestedSize = index +1;
    while(size()<requestedSize){
      add(emptyString);
    }
  }
  
  @Override
  public String[] toArray(){
    String[] result = new String[size()];
    return super.toArray(result);    
  }
  
}
