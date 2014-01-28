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
import java.util.Objects;
import javax.persistence.Basic;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
@Entity
@Table(name = "TEAM")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Team.findAll", query = "SELECT t FROM Team t"),
  @NamedQuery(name = "Team.findByTeamId", query = "SELECT t FROM Team t WHERE t.teamId = :teamId"),
  @NamedQuery(name = "Team.findByName", query = "SELECT t FROM Team t WHERE t.name = :name")})
public class Team implements Serializable, DbEntity {

  @JoinColumn(name = "PERSON", referencedColumnName = "PERSONID")
  @ManyToOne
  private Person person;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "TEAMID")
  private Integer teamId;
  @Column(name = "NAME")
  private String name;
  @OneToMany(mappedBy = "team")
  private List<Person> personList;
  public static final String PROP_ADD_PERSON = "TeamAddPerson";
  public static final String PROP_REMOVE_PERSON = "TeamRemovePerson";
  public static final String PROP_NAME = "TeamPROP_NAME";
  /**
   * The null team is a virtual team (there is no Team entity) that groups all
   * persons which are NOT members of a team.
   */
  public static final int NULL_TEAM_ID = Integer.MIN_VALUE;

  public Team() {
  }

  public Team(Integer team) {
    this.teamId = team;
  }

  public Integer getTeamId() {
    return teamId;
  }

  protected void setTeamId(Integer teamId) {
    this.teamId = teamId;
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

  @XmlTransient
  public List<Person> getPersonList() {
    return personList;
  }

  protected void setPersonList(List<Person> personList) {
    this.personList = personList;
  }

  /**
   * Adds a person who wants to be in this team.
   *
   * @param p
   */
  protected void addPerson(Person p) {
    assert (p != null);
    if (personList == null) {
      throw new RuntimeException("Cannot add a Person to this Team. Record must be persited");
    }
    if (personList.contains(p)) {
      return;
    }
    if (p.getTeam() != this) {
      throw new RuntimeException("Entity missmatch.");
    }
    personList.add(p);
    firePropertyChange(PROP_ADD_PERSON, null, p.identity());
  }

  /**
   * Removes a person from the list of persons who want to be assigned to this
   * team.
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
    hash += (teamId != null ? teamId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Team)) {
      return false;
    }
    Team other = (Team) object;
    if ((this.teamId == null && other.teamId != null) || (this.teamId != null && !this.teamId.equals(other.teamId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    if (name != null) {
      return String.format("Team \"%s\"", name);
    } else {
      return String.format("Team [%s]", teamId);
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener(listener, this.teamId);
  }

  public static void addPropertyChangeListener(PropertyChangeListener listener, Integer teamId) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Team.class, teamId));
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.teamId);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param teamid
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, Integer teamid) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Team.class, teamid));
  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Team.class, teamId);
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    //this.person = person;
  }
}
