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
  @NamedQuery(name = "JobType.findAll", query = "SELECT j FROM JobType j"),
  @NamedQuery(name = "JobType.findByJobTypeId", query = "SELECT j FROM JobType j WHERE j.jobTypeId = :jobTypeId"),
  @NamedQuery(name = "JobType.findByName", query = "SELECT j FROM JobType j WHERE j.name = :name"),
  @NamedQuery(name = "JobType.findByIcon", query = "SELECT j FROM JobType j WHERE j.icon = :icon")})
public class JobType implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "JOBTYPEID")
  private String jobTypeId;
  @Column(name = "NAME")
  private String name;
  @Column(name = "ICON")
  private String icon;
  @OneToMany(mappedBy = "jobType")
  private List<Person> personList;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobType")
  private List<Job> jobList;

  public JobType() {
  }

  public JobType(String jobTypeId) {
    this.jobTypeId = jobTypeId;
  }

  public String getJobTypeId() {
    return jobTypeId;
  }

  public void setJobTypeId(String jobTypeId) {
    this.jobTypeId = jobTypeId;
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
    hash += (jobTypeId != null ? jobTypeId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof JobType)) {
      return false;
    }
    JobType other = (JobType) object;
    if ((this.jobTypeId == null && other.jobTypeId != null) || (this.jobTypeId != null && !this.jobTypeId.equals(other.jobTypeId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.JobType[ jobTypeId=" + jobTypeId + " ]";
  }
  
}
