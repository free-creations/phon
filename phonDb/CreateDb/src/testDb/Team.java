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

package testDb;

import java.io.Serializable;
import java.util.Collection;
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
@Table(name = "TEAM")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Team.findAll", query = "SELECT c FROM Team c"),
  @NamedQuery(name = "Team.findByTeam", query = "SELECT c FROM Team c WHERE c.team = :team"),
  @NamedQuery(name = "Team.findByName", query = "SELECT c FROM Team c WHERE c.name = :name")})
public class Team implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "TEAM")
  private Integer team;
  @Column(name = "NAME")
  private String name;
  @OneToMany(mappedBy = "team")
  private Collection<Person> personCollection;

  public Team() {
  }

  public Team(Integer team) {
    this.team = team;
  }

  public Integer getTeam() {
    return team;
  }

  public void setTeam(Integer team) {
    this.team = team;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlTransient
  public Collection<Person> getPersonCollection() {
    return personCollection;
  }

  public void setPersonCollection(Collection<Person> personCollection) {
    this.personCollection = personCollection;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (team != null ? team.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Team)) {
      return false;
    }
    Team other = (Team) object;
    if ((this.team == null && other.team != null) || (this.team != null && !this.team.equals(other.team))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Team[ team=" + team + " ]";
  }
  
}
