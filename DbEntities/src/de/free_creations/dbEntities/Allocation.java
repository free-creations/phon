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
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Harald <Harald at free-creations.de>
 */
@Entity
@Table(name = "ALLOCATION")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Allocation.findByPersonAndTimeslot", query
          = "SELECT a "
          + "FROM Allocation a "
          + "JOIN a.event e "
          + "WHERE e.timeSlot = :timeSlot "
          + "AND a.person = :person"),
  @NamedQuery(name = "Allocation.findAll", query = "SELECT a FROM Allocation a"),
  @NamedQuery(name = "Allocation.findByAllocationId", query = "SELECT a FROM Allocation a WHERE a.allocationId = :allocationId"),
  @NamedQuery(name = "Allocation.findByLastchange", query = "SELECT a FROM Allocation a WHERE a.lastchange = :lastchange"),
  @NamedQuery(name = "Allocation.findByPlanner", query = "SELECT a FROM Allocation a WHERE a.planner = :planner"),
  @NamedQuery(name = "Allocation.findByNote", query = "SELECT a FROM Allocation a WHERE a.note = :note")})
public class Allocation implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ALLOCATIONID")
  private Integer allocationId;
  @Column(name = "LASTCHANGE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastchange;
  @Column(name = "PLANNER")
  private String planner;
  @Column(name = "NOTE")
  private String note;
  @JoinColumn(name = "PERSON", referencedColumnName = "PERSONID")
  @ManyToOne(optional = false)
  private Person person;
  @JoinColumn(name = "JOB", referencedColumnName = "JOBID")
  @ManyToOne
  private Job job;
  @JoinColumn(name = "EVENT", referencedColumnName = "EVENTID")
  @ManyToOne(optional = false)
  private Event event;
  public static final String PROP_NOTE = "PROP_NOTE";
  public static final String PROP_PLANNER = "PROP_PLANNER";
  public static final String PROP_JOB = "PROP_JOB";
  public static final String PROP_EVENT = "PROP_EVENT";
  public static final String PROP_PERSON = "PROP_PERSON";

  protected Allocation() {
  }

  protected Allocation(Integer allocationId) {
    this.allocationId = allocationId;
  }

  public static Allocation newAllocation(EntityManager entityManager, Person person, Event event, Job job) {
    assert (entityManager != null);
    assert (person != null);
    assert (event != null);
    assert (job != null);
    assert (entityManager.contains(person));
    assert (entityManager.contains(event));

    Allocation newAllocation = new Allocation();

    newAllocation.setPerson(person);
    newAllocation.setEvent(event);
    newAllocation.setJob(job);

    entityManager.persist(newAllocation);
    entityManager.flush();

    return newAllocation;
  }

  public void remove(EntityManager entityManager) {
    setPerson(null);
    setEvent(null);
    setJob(null);
    entityManager.remove(this);
  }

  public Integer getAllocationId() {
    return allocationId;
  }

  public Date getLastchange() {
    return lastchange;
  }

  public void setLastchange(Date lastchange) {
    this.lastchange = lastchange;
  }

  public String getPlanner() {
    return planner;
  }

  public void setPlanner(String value) {
    String old = this.planner;
    this.planner = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_PLANNER, old, value);
    }
  }

  public String getNote() {
    return note;
  }

  public void setNote(String value) {
    String old = this.note;
    this.note = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_NOTE, old, value);
    }
  }

  public Person getPerson() {
    return person;
  }

  public Job getJob() {
    return job;
  }

  protected void setJob(Job newValue) {
    Job old = this.job;
    this.job = newValue;
    if (!Objects.equals(old, newValue)) {
      if (old != null) {
        old.removeAllocation(this);
      }
      if (newValue != null) {
        newValue.addAllocation(this);
      }
      EntityIdentity newId = (newValue == null) ? null : newValue.identity();
      EntityIdentity oldId = (old == null) ? null : old.identity();
      firePropertyChange(PROP_JOB, oldId, newId);
    }
  }

  protected void setPerson(Person newValue) {
    Person old = this.person;
    this.person = newValue;
    if (!Objects.equals(old, newValue)) {
      if (old != null) {
        old.removeAllocation(this);
      }
      if (newValue != null) {
        newValue.addAllocation(this);
      }
      EntityIdentity newId = (newValue == null) ? null : newValue.identity();
      EntityIdentity oldId = (old == null) ? null : old.identity();
      firePropertyChange(PROP_PERSON, oldId, newId);
    }
  }

  protected void setEvent(Event newValue) {
    Event old = this.event;
    this.event = newValue;
    if (!Objects.equals(old, newValue)) {
      if (old != null) {
        old.removeAllocation(this);
      }
      if (newValue != null) {
        newValue.addAllocation(this);
      }
      EntityIdentity newId = (newValue == null) ? null : newValue.identity();
      EntityIdentity oldId = (old == null) ? null : old.identity();
      firePropertyChange(PROP_EVENT, oldId, newId);
    }
  }

  public Event getEvent() {
    return event;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (allocationId != null ? allocationId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Allocation)) {
      return false;
    }
    Allocation other = (Allocation) object;
    if ((this.allocationId == null && other.allocationId != null) || (this.allocationId != null && !this.allocationId.equals(other.allocationId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Allocation" + allocationId;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    assert (this.allocationId != null);
    addPropertyChangeListener(listener, this.allocationId);
  }

  /**
   * Add a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param allocationid
   */
  public static void addPropertyChangeListener(PropertyChangeListener listener, Integer allocationid) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Allocation.class, allocationid));
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * instance.
   *
   * @param listener
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.allocationId);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener the listener to be removed.
   * @param allocationId
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, Integer allocationId) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Allocation.class, allocationId));

  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Allocation.class, allocationId);
  }
}
