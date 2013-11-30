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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
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
public class JobType implements Serializable, DbEntity {

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
  public static final String PROP_REMOVE_PERSON = "removePerson";
  public static final String PROP_ADD_PERSON = "addPerson";

  public JobType() {
  }

  public JobType(String jobTypeId) {
    this.jobTypeId = jobTypeId;
  }

  public String getJobTypeId() {
    return jobTypeId;
  }

  public String getName() {
    return name;
  }

  public String getIcon() {
    return icon;
  }

  /**
   * The list of all persons who would like to be allocated to a job of this
   * kind.
   *
   * @return
   */
  @XmlTransient
  public List<Person> getPersonList() {
    return personList;
  }

  /**
   * The list of all concrete jobs that are of this type.
   *
   * @return
   */
  @XmlTransient
  public List<Job> getJobList() {
    return jobList;
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

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener(listener, this.jobTypeId);
  }

  public static void addPropertyChangeListener(PropertyChangeListener listener, String jobTypeId) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(JobType.class, jobTypeId));
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.jobTypeId);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param jobTypeId
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, String jobTypeId) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(JobType.class, jobTypeId));
  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(this.getClass(), jobTypeId);
  }

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

  protected void addPerson(Person p) {
    assert (p != null);
    if (personList == null) {
      throw new RuntimeException("Cannot add a Person to this Team. Record must be persited");
    }
    if (personList.contains(p)) {
      return;
    }
    if (p.getJobType() != this) {
      throw new RuntimeException("Cannot add Person whishing an other Team.");
    }
    personList.add(p);
    firePropertyChange(PROP_ADD_PERSON, null, p.identity());
  }
}
