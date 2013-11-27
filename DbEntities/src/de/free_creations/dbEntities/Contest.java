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
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Harald <Harald at free-creations.de>
 */
@Entity
@Table(name = "CONTEST")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Contest.findAll", query = "SELECT c FROM Contest c"),
  @NamedQuery(name = "Contest.findByContestId", query = "SELECT c FROM Contest c WHERE c.contestId = :contestId"),
  @NamedQuery(name = "Contest.findByName", query = "SELECT c FROM Contest c WHERE c.name = :name")})
public class Contest implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "CONTESTID")
  private Integer contestId;
  @Column(name = "NAME")
  private String name;
  // @JoinColumn(name = "PERSON", referencedColumnName = "PERSONID")
  // @ManyToOne
  @Transient //<<<<<<<<<<<<<<<<<<<<<<<remove
  private Person person;
  @JoinColumn(name = "CONTESTTYPE", referencedColumnName = "CONTESTTYPEID")
  @ManyToOne(optional = false)
  private ContestType contestType;
  //@OneToMany(cascade = CascadeType.ALL, mappedBy = "contest")
  @Transient //<<<<<<<<<<<<<<<<<<<<<<<remove
  private List<Event> eventList;

  public static final String PROP_NAME = "PROP_NAME";
  public static final String PROP_CONTESTTYPE = "PROP_CONTESTTYPE";

  public Contest() {
  }

  public Contest(Integer contestId) {
    this.contestId = contestId;
  }

  public Integer getContestId() {
    return contestId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    String old = this.name;
    this.name = name;
    if (!Objects.equals(old, name)) {
      firePropertyChange(PROP_NAME, old, name);
    }
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public ContestType getContestType() {
    return contestType;
  }

  public void setContestType(ContestType contestType) {
    ContestType old = this.contestType;
    this.contestType = contestType;
    if (!Objects.equals(old, contestType)) {
      if (old != null) {
        old.removeContest(this);
      }
      if (contestType != null) {
        contestType.addContest(this);
      }
      EntityIdentity newId = (contestType == null) ? null : contestType.identity();
      EntityIdentity oldId = (old == null) ? null : old.identity();
      firePropertyChange(PROP_CONTESTTYPE, oldId, newId);
    }
  }

  @XmlTransient
  public List<Event> getEventList() {
    return eventList;
  }

  public void setEventList(List<Event> eventList) {
    this.eventList = eventList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (contestId != null ? contestId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Contest)) {
      return false;
    }
    Contest other = (Contest) object;
    if ((this.contestId == null && other.contestId != null) || (this.contestId != null && !this.contestId.equals(other.contestId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Contest[ contestId=" + contestId + " ]";
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener(listener, this.contestId);
  }

  public static void addPropertyChangeListener(PropertyChangeListener listener, Integer contestId) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Contest.class, contestId));
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.contestId);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param contestId
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, Integer contestId) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Contest.class, contestId));
  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Contest.class, contestId);
  }
}
