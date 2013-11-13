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
package de.free_creations.nbPhon4Netbeans;

import de.free_creations.dbEntities.Personen;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.util.Comparator;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 * Provides comparators to sort the PERSONEN records according to divers
 * criteria.
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class PersonCompare {

  public static abstract class PersonComparator implements Comparator<Node> {

    private final MutableEntityCollection<Personen, Integer> personCollection;

    public PersonComparator(MutableEntityCollection<Personen, Integer> personCollection) {
      this.personCollection = personCollection;
    }

    @Override
    final public int compare(Node n1, Node n2) {
      return compare(personCollection, n1, n2);
    }

    protected abstract int compare(MutableEntityCollection<Personen, Integer> personCollection, Node n1, Node n2);
  };

  public static PersonComparator byPriority(MutableEntityCollection<Personen, Integer> personCollection) {
    return new PersonComparator(personCollection) {
      @Override
      public int compare(MutableEntityCollection<Personen, Integer> personCollection, Node n1, Node n2) {
        assert (personCollection != null);
        int checkNull = checkValidPersonNodes(n1, n2);
        if (checkNull != bothValid) {
          return checkNull;
        }
        PersonNode pn1 = ((PersonNode) n1);
        PersonNode pn2 = ((PersonNode) n2);
        if (pn1.getKey() == PersonNode.nullKey) {
          if (pn2.getKey() == PersonNode.nullKey) {
            return 0;
          } else {
            return 1;
          }
        }
        try {
          Personen p1 = personCollection.findEntity(((PersonNode) n1).getKey());
          Personen p2 = personCollection.findEntity(((PersonNode) n2).getKey());
          int checkNull2 = checkNotNull(p1, p2);
          if (checkNull2 != bothValid) {
            return checkNull2;
          }
          /**
           * @Todo this is just a test, implement real priority comparison
           */
          int prio1 = nonNull(p1.getPersonid());
          int prio2 = nonNull(p2.getPersonid());

          return Integer.compare(prio1, prio2);

        } catch (DataBaseNotReadyException ex) {
          Exceptions.printStackTrace(ex);
        }
        return 0;
      }
    };
  }

  public static PersonComparator byName(MutableEntityCollection<Personen, Integer> personCollection) {
    return new PersonComparator(personCollection) {
      @Override
      public int compare(MutableEntityCollection<Personen, Integer> personCollection, Node n1, Node n2) {
        assert (personCollection != null);
        int checkNull = checkValidPersonNodes(n1, n2);
        if (checkNull != bothValid) {
          return checkNull;
        }
        PersonNode pn1 = ((PersonNode) n1);
        PersonNode pn2 = ((PersonNode) n2);
        if (pn1.getKey() == PersonNode.nullKey) {
          if (pn2.getKey() == PersonNode.nullKey) {
            return 0;
          } else {
            return 1;
          }
        }
        try {
          Personen p1 = personCollection.findEntity(pn1.getKey());
          Personen p2 = personCollection.findEntity(pn2.getKey());
          int checkNull2 = checkNotNull(p1, p2);
          if (checkNull2 != bothValid) {
            return checkNull2;
          }

          String f1 = nonNull(p1.getFamilienname());
          String f2 = nonNull(p2.getFamilienname());
          if (f1.equals(f2)) {
            String v1 = nonNull(p1.getVorname());
            String v2 = nonNull(p2.getVorname());
            return v1.compareToIgnoreCase(v2);
          } else {
            return f1.compareToIgnoreCase(f2);
          }

        } catch (DataBaseNotReadyException ex) {
          Exceptions.printStackTrace(ex);
        }
        return 0;
      }
    };
  }
  private final static int bothValid = 0xFF;

  private static int checkValidPersonNodes(Object o1, Object o2) {
    o1 = typeCheck(o1);
    o2 = typeCheck(o2);
    return checkNotNull(o1, o2);

  }

  private static int checkNotNull(Object o1, Object o2) {
    if (o1 == null) {
      return (o2 == null) ? 0 : -1;
    }
    if (o2 == null) {
      return +1;
    }
    return bothValid;
  }

  /**
   * nullify any object that is not an instance of PersonNode
   *
   * @param o the object to test
   * @return the given object if it is a subtype of PersonNode, null otherwise.
   */
  private static Object typeCheck(Object o) {
    if (o != null) {
      if (o instanceof PersonNode) {
        return o;
      } else {
        return null;
      }
    } else {
      return o;
    }
  }

  private static String nonNull(String s) {
    if (s == null) {
      return "";
    } else {
      return s;
    }
  }

  private static int nonNull(Integer i) {
    if (i == null) {
      return Integer.MIN_VALUE;
    } else {
      return i;
    }
  }
}
