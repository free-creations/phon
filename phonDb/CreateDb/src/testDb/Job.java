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
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "JOB")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Job.findAll", query = "SELECT j FROM Job j"),
  @NamedQuery(name = "Job.findByJobid", query = "SELECT j FROM Job j WHERE j.jobid = :jobid"),
  @NamedQuery(name = "Job.findByName", query = "SELECT j FROM Job j WHERE j.name = :name")})
public class Job implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "JOBID")
  private String jobid;
  @Column(name = "NAME")
  private String name;
  @OneToMany(mappedBy = "job")
  private List<Allocation> allocationList;
  @JoinColumn(name = "JOBTYPE", referencedColumnName = "JOBTYPEID")
  @ManyToOne(optional = false)
  private Jobtype jobtype;

  public Job() {
  }

  public Job(String jobid) {
    this.jobid = jobid;
  }

  public String getJobid() {
    return jobid;
  }

  public void setJobid(String jobid) {
    this.jobid = jobid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlTransient
  public List<Allocation> getAllocationList() {
    return allocationList;
  }

  public void setAllocationList(List<Allocation> allocationList) {
    this.allocationList = allocationList;
  }

  public Jobtype getJobtype() {
    return jobtype;
  }

  public void setJobtype(Jobtype jobtype) {
    this.jobtype = jobtype;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (jobid != null ? jobid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Job)) {
      return false;
    }
    Job other = (Job) object;
    if ((this.jobid == null && other.jobid != null) || (this.jobid != null && !this.jobid.equals(other.jobid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Job[ jobid=" + jobid + " ]";
  }
  
}
