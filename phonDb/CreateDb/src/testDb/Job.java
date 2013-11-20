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
 * @author Harald Postner<harald at free-creations.de>
 */
@Entity
@Table(name = "JOB")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Job.findAll", query = "SELECT j FROM Job j"),
  @NamedQuery(name = "Job.findByFunktionid", query = "SELECT j FROM Job j WHERE j.funktionid = :funktionid"),
  @NamedQuery(name = "Job.findByFunktionname", query = "SELECT j FROM Job j WHERE j.funktionname = :funktionname"),
  @NamedQuery(name = "Job.findBySortvalue", query = "SELECT j FROM Job j WHERE j.sortvalue = :sortvalue")})
public class Job implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "FUNKTIONID")
  private String funktionid;
  @Column(name = "FUNKTIONNAME")
  private String funktionname;
  @Column(name = "SORTVALUE")
  private Integer sortvalue;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "funktionid")
  private Collection<Allocation> allocationCollection;
  @OneToMany(mappedBy = "gewuenschtefunktion")
  private Collection<Person> personCollection;

  public Job() {
  }

  public Job(String funktionid) {
    this.funktionid = funktionid;
  }

  public String getFunktionid() {
    return funktionid;
  }

  public void setFunktionid(String funktionid) {
    this.funktionid = funktionid;
  }

  public String getFunktionname() {
    return funktionname;
  }

  public void setFunktionname(String funktionname) {
    this.funktionname = funktionname;
  }

  public Integer getSortvalue() {
    return sortvalue;
  }

  public void setSortvalue(Integer sortvalue) {
    this.sortvalue = sortvalue;
  }

  @XmlTransient
  public Collection<Allocation> getAllocationCollection() {
    return allocationCollection;
  }

  public void setAllocationCollection(Collection<Allocation> allocationCollection) {
    this.allocationCollection = allocationCollection;
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
    hash += (funktionid != null ? funktionid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Job)) {
      return false;
    }
    Job other = (Job) object;
    if ((this.funktionid == null && other.funktionid != null) || (this.funktionid != null && !this.funktionid.equals(other.funktionid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Job[ funktionid=" + funktionid + " ]";
  }
  
}
