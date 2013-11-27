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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "EVENT")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
  @NamedQuery(name = "Event.findByEventId", query = "SELECT e FROM Event e WHERE e.eventId = :eventId"),
  @NamedQuery(name = "Event.findByConfirmed", query = "SELECT e FROM Event e WHERE e.confirmed = :confirmed")})
public class Event implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "EVENTID")
  private Integer eventId;
  @Column(name = "CONFIRMED")
  private Integer confirmed;
//  @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
  @Transient //<<<<<<<<<<<<<<<<<<<<<<<remove
  private List<Allocation> allocationList;
//  @JoinColumn(name = "TIMESLOT", referencedColumnName = "TIMESLOTID")
//  @ManyToOne(optional = false)
  @Transient //<<<<<<<<<<<<<<<<<<<<<<<remove
  private TimeSlot timeSlot;
//  @JoinColumn(name = "LOCATION", referencedColumnName = "LOCATIONID")
//  @ManyToOne
  @Transient //<<<<<<<<<<<<<<<<<<<<<<<remove
  private Location location;
//  @JoinColumn(name = "CONTEST", referencedColumnName = "CONTESTID")
//  @ManyToOne(optional = false)
  @Transient //<<<<<<<<<<<<<<<<<<<<<<<remove
  private Contest contest;
  public static final String PROP_CONFIRMED = "PROP_CONFIRMED";

  public Event() {
  }

  public Event(Integer eventId) {
    this.eventId = eventId;
  }

  public Integer getEventId() {
    return eventId;
  }

  public boolean isConfirmed() {
    if (confirmed == null) {
      return false;
    } else {
      return !confirmed.equals(0);
    }
  }

  public void setConfirmed(boolean value) {
    if (isConfirmed() != value) {
      boolean old = isConfirmed();
      this.confirmed = value ? 1 : 0;
      firePropertyChange(PROP_CONFIRMED, old, value);
    }
  }

  @XmlTransient
  public List<Allocation> getAllocationList() {
    return allocationList;
  }

  public void setAllocationList(List<Allocation> allocationList) {
    this.allocationList = allocationList;
  }

  public TimeSlot getTimeSlot() {
    return timeSlot;
  }

  public void setTimeSlot(TimeSlot timeSlot) {
    this.timeSlot = timeSlot;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Contest getContest() {
    return contest;
  }

  public void setContest(Contest contest) {
    this.contest = contest;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (eventId != null ? eventId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Event)) {
      return false;
    }
    Event other = (Event) object;
    if ((this.eventId == null && other.eventId != null) || (this.eventId != null && !this.eventId.equals(other.eventId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Event[ eventId=" + eventId + " ]";
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener(listener, this.eventId);
  }

  public static void addPropertyChangeListener(PropertyChangeListener listener, Integer eventId) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Event.class, eventId));
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.eventId);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param eventId
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, Integer eventId) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Event.class, eventId));
  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Event.class, eventId);
  }
}