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
 * The Job Table is pre-populated, therfore no public constructor and
 * no public setters.
 * 
 * @author Harald <Harald at free-creations.de>
 */
@Entity
@Table(name = "JOB")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Job.findAll", query = "SELECT j FROM Job j"),
  @NamedQuery(name = "Job.findByJobId", query = "SELECT j FROM Job j WHERE j.jobId = :jobId"),
  @NamedQuery(name = "Job.findByName", query = "SELECT j FROM Job j WHERE j.name = :name")})
public class Job implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "JOBID")
  private String jobId;
  @Column(name = "NAME")
  private String name;
  @OneToMany(mappedBy = "job")
  private List<Allocation> allocationList;
  @JoinColumn(name = "JOBTYPE", referencedColumnName = "JOBTYPEID")
  @ManyToOne(optional = false)
  private JobType jobType;
  public static final String PROP_ALLOCATIONREMOVED = "PROP_ALLOCATIONREMOVED";
  public static final String PROP_ALLOCATIONADDED = "PROP_ALLOCATIONADDED";

  protected Job() {
  }

  /**
   * Only for test within this package.
   * @param jobId
   * @param jobType 
   */
  protected Job(String jobId, JobType jobType) {
    this.jobId = jobId;
    this.jobType = jobType;
  }

  public String getJobId() {
    return jobId;
  }

  public String getName() {
    return name;
  }

  @XmlTransient
  public List<Allocation> getAllocationList() {
    return allocationList;
  }

  public JobType getJobType() {
    return jobType;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (jobId != null ? jobId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Job)) {
      return false;
    }
    Job other = (Job) object;
    if ((this.jobId == null && other.jobId != null) || (this.jobId != null && !this.jobId.equals(other.jobId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Job[" + jobId + "]";
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Job.class, jobId);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener(listener, this.jobId);
  }

  public static void addPropertyChangeListener(PropertyChangeListener listener, String jobId) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Job.class, jobId));
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.jobId);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param jobId
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, String jobId) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Job.class, jobId));
  }

  protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  void removeAllocation(Allocation a) {
    if (allocationList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (!allocationList.contains(a)) {
      return;
    }
    allocationList.remove(a);
    firePropertyChange(PROP_ALLOCATIONREMOVED, a.identity(), null);
  }

  void addAllocation(Allocation a) {
    assert (a != null);
    if (allocationList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (allocationList.contains(a)) {
      return;
    }
    if (this != a.getJob()) {
      throw new RuntimeException("Entity missmatch.");
    }
    allocationList.add(a);
    firePropertyChange(PROP_ALLOCATIONADDED, null, a.identity());
  }
}
