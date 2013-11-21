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
package de.free_creations.dbEntities;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
@Entity
@Table(name = "CREW")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Crew.findAll", query = "SELECT c FROM Crew c"),
  @NamedQuery(name = "Crew.findByCrew", query = "SELECT c FROM Crew c WHERE c.crewId = :crew"),
  @NamedQuery(name = "Crew.findByName", query = "SELECT c FROM Crew c WHERE c.name = :name")})
public class Crew implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "CREW")
  private Integer crewId;
  @Column(name = "NAME")
  private String name;
  @OneToMany(mappedBy = "crew")
  private List<Person> personList;
  public static final String PROP_ADD_PERSON = "addPerson";
  public static final String PROP_REMOVE_PERSON = "removePerson";

  public Crew() {
  }

  public Crew(Integer crew) {
    this.crewId = crew;
  }

  public Integer getCrewId() {
    return crewId;
  }

  protected void setCrewId(Integer crewId) {
    this.crewId = crewId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlTransient
  public List<Person> getPersonList() {
    return personList;
  }

  protected void setPersonList(List<Person> personList) {
    this.personList = personList;
  }

  /**
   * Adds a person who wants to be in this crew.
   *
   * @param p
   */
  protected void addPerson(Person p) {
    assert (p != null);
    if (personList == null) {
      throw new RuntimeException("Cannot add a Person to this Crew. Record must be persited");
    }
    if (personList.contains(p)) {
      return;
    }
    if (p.getCrew() != this) {
      throw new RuntimeException("Cannot add Person whishing an other Crew.");
    }
    personList.add(p);
    firePropertyChange(PROP_ADD_PERSON, null, p.identity());
  }
  
    /**
   * Removes a person from the list of persons who want to be assigned to this
   * function.
   *
   * @param p
   */
  protected void removePerson(Person p) {
    if (personList == null) {
      throw new RuntimeException("Cannot remove Person from Function. Record must be persited");
    }
    if (!personList.contains(p)) {
      return;
    }
    personList.remove(p);
    assert (p != null);
    firePropertyChange(PROP_REMOVE_PERSON, p.identity(), null);
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (crewId != null ? crewId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Crew)) {
      return false;
    }
    Crew other = (Crew) object;
    if ((this.crewId == null && other.crewId != null) || (this.crewId != null && !this.crewId.equals(other.crewId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    if (name != null) {
      return String.format("Crew \"%s\"", name);
    } else {
      return String.format("Crew [%s]", crewId);
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener(listener, this.crewId);
  }

  public static void addPropertyChangeListener(PropertyChangeListener listener, Integer crewId) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Crew.class, crewId));
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.crewId);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param crewid
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, Integer crewid) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Crew.class, crewid));
  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Crew.class, crewId);
  }
}
