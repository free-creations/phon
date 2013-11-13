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

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class CompareUtils {

  public final static int bothValid = 0xFF;

  /**
   * A string compare allowing one or both strings to be null.
   *
   * Null is considered to be the smallest string possible.
   *
   * @param s1
   * @param s2
   * @return
   */
  public static int stringCompareNull(String s1, String s2) {
    int c = typeCheckCompare(s1, s2, String.class);
    if (c != bothValid) {
      return c;
    } else {
      return s1.compareTo(s2);
    }
  }

  public static int integerCompareNull(Integer i1, Integer i2) {
    int c = typeCheckCompare(i1, i2, Integer.class);
    if (c != bothValid) {
      return c;
    } else {
      return i1.compareTo(i2);
    }
  }

  /**
   * This function can be used in comparators to filter those cases
   * where the two arguments are not of the expected class.
   * 
   * @param o1
   * @param o2
   * @param clazz
   * @return bothValid if both arguments are of the expected class. Returns
   * the comparator result if one or both arguments are not valid.
   * Returns a negative integer if the first is invalid and the second is valid. 
   * Returns a positive integer if the first is valid and the second is invalid. 
   * Returns 0 if the first is invalid and the second is invalid. 
   */
   @SuppressWarnings("rawtypes")
  public static int typeCheckCompare(Object o1, Object o2, Class clazz) {
    if (isInstanceOf(o1, clazz)) {
      if (isInstanceOf(o2, clazz)) {
        return bothValid;
      } else {
        return 1; //first valid, second invalid
      }
    } else {
      if (isInstanceOf(o2, clazz)) {
        return -1; //first invalid, second valid
      } else {
        return 0; //first invalid, second invalid
      }
    }


  }

  @SuppressWarnings("rawtypes")
  public static boolean isInstanceOf(Object o, Class clazz) {
    return clazz.isInstance(o);
  }
}
