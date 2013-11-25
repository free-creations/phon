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
 * @author Harald Postner<harald at free-creations.de>
 */
@Entity
@Table(name = "JOBTYPE")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Jobtype.findAll", query = "SELECT j FROM Jobtype j"),
  @NamedQuery(name = "Jobtype.findByJobtypeid", query = "SELECT j FROM Jobtype j WHERE j.jobtypeid = :jobtypeid"),
  @NamedQuery(name = "Jobtype.findByName", query = "SELECT j FROM Jobtype j WHERE j.name = :name"),
  @NamedQuery(name = "Jobtype.findByIcon", query = "SELECT j FROM Jobtype j WHERE j.icon = :icon")})
public class Jobtype implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "JOBTYPEID")
  private String jobtypeid;
  @Column(name = "NAME")
  private String name;
  @Column(name = "ICON")
  private String icon;
  @OneToMany(mappedBy = "jobtype")
  private List<Person> personList;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobtype")
  private List<Job> jobList;

  public Jobtype() {
  }

  public Jobtype(String jobtypeid) {
    this.jobtypeid = jobtypeid;
  }

  public String getJobtypeid() {
    return jobtypeid;
  }

  public void setJobtypeid(String jobtypeid) {
    this.jobtypeid = jobtypeid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  @XmlTransient
  public List<Person> getPersonList() {
    return personList;
  }

  public void setPersonList(List<Person> personList) {
    this.personList = personList;
  }

  @XmlTransient
  public List<Job> getJobList() {
    return jobList;
  }

  public void setJobList(List<Job> jobList) {
    this.jobList = jobList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (jobtypeid != null ? jobtypeid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Jobtype)) {
      return false;
    }
    Jobtype other = (Jobtype) object;
    if ((this.jobtypeid == null && other.jobtypeid != null) || (this.jobtypeid != null && !this.jobtypeid.equals(other.jobtypeid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Jobtype[ jobtypeid=" + jobtypeid + " ]";
  }
  
}
