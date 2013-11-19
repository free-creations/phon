/*
 * Copyright 2013 Harald Postner<harald at free-creations.de>.
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
package createdb;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class CsvReader {

  private final String line;

  public CsvReader(String line) {
    this.line = line;
  }
  
  public String item(int index){
    return head(part(index, line));
  }

  protected String head(String s) {
    if (s == null) {
      return "";
    }
    int commaPos = s.indexOf(',');
    if (commaPos > 0) {
      return s.substring(0, commaPos).trim();
    } else {
      return s.trim();
    }
  }

  protected String tail(String s) {
    if (s == null) {
      return null;
    }
    int commaPos = s.indexOf(',');
    if (commaPos > 0) {
      String tail = s.substring(commaPos + 1);
      if (tail.trim().length() == 0) {
        return null;
      } else {
        return tail;
      }
    } else {
      return null;
    }
  }

  protected String part(int i, String s) {
    if (s == null) {
      return "";
    }
    if (i <= 0) {
      return s;
    }
    return part(i - 1, tail(s));
  }
}
