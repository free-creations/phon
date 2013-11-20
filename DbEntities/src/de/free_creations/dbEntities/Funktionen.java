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
package de.free_creations.dbEntities;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Harald <Harald at free-creations.de>
 */
@Entity
@Table(name = "JOB")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Funktionen.findAll", query = "SELECT f FROM Funktionen f"),
  @NamedQuery(name = "Funktionen.findByFunktionid", query = "SELECT f FROM Funktionen f WHERE f.funktionid = :funktionid"),
  @NamedQuery(name = "Funktionen.findByFunktionname", query = "SELECT f FROM Funktionen f WHERE f.funktionname = :funktionname"),
  @NamedQuery(name = "Funktionen.findBySortvalue", query = "SELECT f FROM Funktionen f WHERE f.sortvalue = :sortvalue")})
public class Funktionen implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "FUNKTIONID")
  private String funktionid;
  @Column(name = "FUNKTIONNAME")
  private String funktionname;
  @Column(name = "SORTVALUE")
  private Integer sortvalue;
  public final static String PROP_ADD_PREFERING_PERSON = "add_prefering_person";
  public final static String PROP_REMOVE_PREFERING_PERSON = "remove_prefering_person";
  @OneToMany(mappedBy = "gewuenschtefunktion")
  private List<Personen> personenList;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "funktionen")
  private List<Teameinteilung> teameinteilungList;
  public final static String PROP_REMOVE_TEAMEINTEILUNG = "removeTeameinteilung";
  public final static String  PROP_ADD_TEAMEINTEILUNG  = "addTeameinteilung";

  public Funktionen() {
  }

  public Funktionen(String funktionid, String funktionname, Integer sortvalue) {
    this.funktionid = funktionid;
    this.funktionname = funktionname;
    this.sortvalue = sortvalue;
  }

  public Funktionen(String funktionid) {
    this.funktionid = funktionid;
  }

  public String getFunktionid() {
    return funktionid;
  }

  public String getFunktionname() {
    return funktionname;
  }

  public Integer getSortvalue() {
    return sortvalue;
  }

  /**
   * The list of all persons who prefer to be assigned to this function.
   *
   * @return the list of all persons who prefer to be assigned to this function.
   */
  @XmlTransient
  public List<Personen> getPersonenList() {
    return personenList;
  }

  /**
   * Adds a person who prefers to be assigned to this function.
   *
   * @param p
   */
  protected void addPreferingPerson(Personen p) {
    assert (p != null);
    if (personenList == null) {
      throw new RuntimeException("Cannot add a Person to this Function. Record must be persited");
    }
    if (personenList.contains(p)) {
      return;
    }
    if (p.getGewuenschtefunktion() != this) {
      throw new RuntimeException("Cannot add Person whishing an other function.");
    }
    personenList.add(p);
    firePropertyChange(PROP_ADD_PREFERING_PERSON, null, p.identity());
  }

  /**
   * Removes a person from the list of persons who want to be assigned to this
   * function.
   *
   * @param p
   */
  protected void removePreferingPerson(Personen p) {
    if (personenList == null) {
      throw new RuntimeException("Cannot remove Person from Function. Record must be persited");
    }
    if (!personenList.contains(p)) {
      return;
    }
    personenList.remove(p);
    assert (p != null);
    firePropertyChange(PROP_REMOVE_PREFERING_PERSON, p.identity(), null);
  }

  private void setPersonenList(List<Personen> personenList) {
    this.personenList = personenList;
  }

  @XmlTransient
  public List<Teameinteilung> getTeameinteilungList() {
    return teameinteilungList;
  }

  /**
   * Use this function only within package dbEntities and use it only for test.
   *
   * @param teameinteilungList
   */
  protected void setTeameinteilungList(List<Teameinteilung> teameinteilungList) {
    this.teameinteilungList = teameinteilungList;
  }

  void removeTeameinteilung(Teameinteilung t) {
    if (teameinteilungList == null) {
      throw new RuntimeException("Cannot remove Teameinteilung from Function. Record must be persited");
    }
    if (!teameinteilungList.contains(t)) {
      return;
    }
    teameinteilungList.remove(t);
    assert (t != null);
    firePropertyChange(PROP_REMOVE_TEAMEINTEILUNG, t.identity(), null);
  }

  void addTeameinteilung(Teameinteilung t) {
    assert (t != null);
    if (teameinteilungList == null) {
      throw new RuntimeException("Cannot add a Teameinteilung to this Function. Record must be persited");
    }
    if (teameinteilungList.contains(t)) {
      return;
    }
    if (t.getFunktionen() != this) {
      throw new RuntimeException("Cannot add Teameinteilung for an other function.");
    }
    teameinteilungList.add(t);
    firePropertyChange(PROP_ADD_TEAMEINTEILUNG, null, t.identity());
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (funktionid != null ? funktionid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Funktionen)) {
      return false;
    }
    Funktionen other = (Funktionen) object;
    if ((this.funktionid == null && other.funktionid != null) || (this.funktionid != null && !this.funktionid.equals(other.funktionid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return getFunktionname();
  }

  /**
   * Add a PropertyChangeListener for the entity represented by this instance.
   *
   * @param listener
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener(listener, this.funktionid);
  }

  /**
   * Add a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param funktionid
   */
  public static void addPropertyChangeListener(PropertyChangeListener listener, String funktionid) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Funktionen.class, funktionid));
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * instance.
   *
   * @param listener
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.funktionid);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener the listener to be removed.
   * @param funktionid the primary key
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, String funktionid) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Funktionen.class, funktionid));

  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Funktionen.class, funktionid);
  }
}
