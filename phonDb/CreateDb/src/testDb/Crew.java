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
@Table(name = "CREW")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Crew.findAll", query = "SELECT c FROM Crew c"),
  @NamedQuery(name = "Crew.findByCrew", query = "SELECT c FROM Crew c WHERE c.crew = :crew"),
  @NamedQuery(name = "Crew.findByName", query = "SELECT c FROM Crew c WHERE c.name = :name")})
public class Crew implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "CREW")
  private Integer crew;
  @Column(name = "NAME")
  private String name;
  @OneToMany(mappedBy = "crew")
  private Collection<Person> personCollection;

  public Crew() {
  }

  public Crew(Integer crew) {
    this.crew = crew;
  }

  public Integer getCrew() {
    return crew;
  }

  public void setCrew(Integer crew) {
    this.crew = crew;
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
    hash += (crew != null ? crew.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Crew)) {
      return false;
    }
    Crew other = (Crew) object;
    if ((this.crew == null && other.crew != null) || (this.crew != null && !this.crew.equals(other.crew))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Crew[ crew=" + crew + " ]";
  }
  
}
