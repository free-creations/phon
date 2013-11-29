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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Harald <Harald at free-creations.de>
 */
@Entity
@Table(name = "AVAILABILITY")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Availability.findAll", query = "SELECT a FROM Availability a"),
  @NamedQuery(name = "Availability.findByAvailabilityId", query = "SELECT a FROM Availability a WHERE a.availabilityId = :availabilityId"),
  @NamedQuery(name = "Availability.findByAvailable", query = "SELECT a FROM Availability a WHERE a.available = :available"),
  @NamedQuery(name = "Availability.findByLastchange", query = "SELECT a FROM Availability a WHERE a.lastchange = :lastchange")})
public class Availability implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "AVAILABILITYID")
  private Integer availabilityId;
  @Column(name = "AVAILABLE")
  private Integer available;
  @Column(name = "LASTCHANGE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastchange;
  @JoinColumn(name = "TIMESLOT", referencedColumnName = "TIMESLOTID")
  @ManyToOne(optional = false)
  private TimeSlot timeSlot;
  @JoinColumn(name = "PERSON", referencedColumnName = "PERSONID")
  @ManyToOne(optional = false)
  private Person person;
  public static final String PROP_AVAILABLE = "PROP_AVAILABLE";
  public static final String PROP_PERSON = "PROP_PERSON";
  public static final String PROP_TIMESLOT = "PROP_TIMESLOT";

  public Availability() {
  }

  public Availability(Integer availabilityId) {
    this.availabilityId = availabilityId;
  }

  public Integer getAvailabilityId() {
    return availabilityId;
  }

  public boolean isAvailable() {
    if (available == null) {
      return false;
    } else {
      return !available.equals(0);
    }

  }

  public void setAvailable(boolean value) {
    if (isAvailable() != value) {
      boolean old = isAvailable();
      this.available = value ? 1 : 0;
      firePropertyChange(PROP_AVAILABLE, old, value);
    }
  }

  public Date getLastchange() {
    return lastchange;
  }

  public void setLastchange(Date lastchange) {
    this.lastchange = lastchange;
  }

  public TimeSlot getTimeSlot() {
    return timeSlot;
  }

 public final void setTimeSlot(TimeSlot newValue) {
    TimeSlot old = this.timeSlot;
    this.timeSlot = newValue;
    if (!Objects.equals(old, newValue)) {
      if (old != null) {
        old.removeAvailability(this);
      }
      if (newValue != null) {
        newValue.addAvailability(this);
      }
      EntityIdentity newId = (newValue == null) ? null : newValue.identity();
      EntityIdentity oldId = (old == null) ? null : old.identity();
      firePropertyChange(PROP_TIMESLOT, oldId, newId);
    }
  }

  public Person getPerson() {
    return person;
  }

  public final void setPerson(Person newValue) {
    Person old = this.person;
    this.person = newValue;
    if (!Objects.equals(old, newValue)) {
      if (old != null) {
        old.removeAvailability(this);
      }
      if (newValue != null) {
        newValue.addAvailability(this);
      }
      EntityIdentity newId = (newValue == null) ? null : newValue.identity();
      EntityIdentity oldId = (old == null) ? null : old.identity();
      firePropertyChange(PROP_PERSON, oldId, newId);
    }
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (availabilityId != null ? availabilityId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Availability)) {
      return false;
    }
    Availability other = (Availability) object;
    if ((this.availabilityId == null && other.availabilityId != null) || (this.availabilityId != null && !this.availabilityId.equals(other.availabilityId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Availability[ availabilityId=" + availabilityId + " ]";
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener(listener, this.availabilityId);
  }

  public static void addPropertyChangeListener(PropertyChangeListener listener, Integer availabilityId) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Availability.class, availabilityId));
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.availabilityId);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param availabilityId
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, Integer availabilityId) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Availability.class, availabilityId));
  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Availability.class, availabilityId);
  }
}
