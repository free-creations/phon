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
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
  @NamedQuery(name = "Event.findByContestAndTimeslot", query
          = "SELECT e "
          + "FROM Event e "
          + "WHERE e.contest = :contest "
          + "AND e.timeSlot = :timeSlot"),
  @NamedQuery(name = "Event.findByLocationAndTimeslot", query
          = "SELECT e "
          + "FROM Event e "
          + "WHERE e.location = :location "
          + "AND e.timeSlot = :timeSlot"),
  @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
  @NamedQuery(name = "Event.findByEventId", query = "SELECT e FROM Event e WHERE e.eventId = :eventId"),
  @NamedQuery(name = "Event.findByScheduled", query = "SELECT e FROM Event e WHERE e.scheduled = :scheduled")})
public class Event implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "EVENTID")
  private Integer eventId;
  @Column(name = "SCHEDULED")
  private Integer scheduled;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
  private List<Allocation> allocationList;
  @JoinColumn(name = "TIMESLOT", referencedColumnName = "TIMESLOTID")
  @ManyToOne(optional = false)
  private TimeSlot timeSlot;
  @JoinColumn(name = "LOCATION", referencedColumnName = "LOCATIONID")
  @ManyToOne
  private Location location;
  @JoinColumn(name = "CONTEST", referencedColumnName = "CONTESTID")
  @ManyToOne(optional = false)
  private Contest contest;
  public static final String PROP_SCHEDULED = "eventPROP_CONFIRMED";
  public static final String PROP_LOCATION = "eventPROP_LOCATION";
  public static final String PROP_CONTEST = "eventPROP_CONTEST";
  public static final String PROP_ALLOCATIONREMOVED = "eventPROP_ALLOCATIONREMOVED";
  public static final String PROP_ALLOCATIONADDED = "eventPROP_ALLOCATIONADDED";

  protected Event() {
  }

  protected Event(Integer eventId, Contest contest, TimeSlot timeSlot) {
    this(contest, timeSlot);
    this.eventId = eventId;
  }

  protected Event(Contest contest, TimeSlot timeSlot) {
    setContest(contest);
    setTimeSlot(timeSlot);
  }

  public static Event newEvent(EntityManager entityManager, Contest contest, TimeSlot timeSlot) {
    assert (entityManager != null);
    assert (contest != null);
    assert (timeSlot != null);
    assert (entityManager.contains(contest));
    assert (entityManager.contains(timeSlot));

    Event newEvent = new Event(contest, timeSlot);
    entityManager.persist(newEvent);
    entityManager.flush();
    return newEvent;
  }

  public Integer getEventId() {
    return eventId;
  }

  public boolean isScheduled() {
    if (scheduled == null) {
      return false;
    } else {
      return !scheduled.equals(0);
    }
  }

  public void setScheduled(boolean value) {
    if (isScheduled() != value) {
      boolean old = isScheduled();
      this.scheduled = value ? 1 : 0;
      firePropertyChange(PROP_SCHEDULED, old, value);
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

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location newValue) {
    Location old = this.location;
    this.location = newValue;
    if (!Objects.equals(old, newValue)) {
      if (old != null) {
        old.removeEvent(this);
      }
      if (newValue != null) {
        newValue.addEvent(this);
      }
      EntityIdentity newId = (newValue == null) ? null : newValue.identity();
      EntityIdentity oldId = (old == null) ? null : old.identity();
      firePropertyChange(PROP_LOCATION, oldId, newId);
    }
  }

  protected final void setContest(Contest newValue) {
    Contest old = this.contest;
    this.contest = newValue;
    if (!Objects.equals(old, newValue)) {
      if (old != null) {
        old.removeEvent(this);
      }
      if (newValue != null) {
        newValue.addEvent(this);
      }
      EntityIdentity newId = (newValue == null) ? null : newValue.identity();
      EntityIdentity oldId = (old == null) ? null : old.identity();
      firePropertyChange(PROP_CONTEST, oldId, newId);
    }
  }

  protected final void setTimeSlot(TimeSlot newValue) {
    TimeSlot old = this.timeSlot;
    this.timeSlot = newValue;
    if (!Objects.equals(old, newValue)) {
      if (old != null) {
        old.removeEvent(this);
      }
      if (newValue != null) {
        newValue.addEvent(this);
      }
      EntityIdentity newId = (newValue == null) ? null : newValue.identity();
      EntityIdentity oldId = (old == null) ? null : old.identity();
      firePropertyChange(PROP_CONTEST, oldId, newId);
    }
  }

  public Contest getContest() {
    return contest;
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
    return "Event[" + eventId + "]";
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

  protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Event.class, eventId);
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
    if (this != a.getEvent()) {
      throw new RuntimeException("Entity missmatch.");
    }
    allocationList.add(a);
    firePropertyChange(PROP_ALLOCATIONADDED, null, a.identity());
  }
}
