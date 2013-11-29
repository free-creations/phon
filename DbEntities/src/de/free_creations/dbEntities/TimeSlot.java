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
import java.util.Calendar;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Harald <Harald at free-creations.de>
 */
@Entity
@Table(name = "TIMESLOT")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "TimeSlot.findAll", query = "SELECT t FROM TimeSlot t"),
  @NamedQuery(name = "TimeSlot.findByTimeSlotId", query = "SELECT t FROM TimeSlot t WHERE t.timeSlotId = :timeSlotId"),
  @NamedQuery(name = "TimeSlot.findByDayIdx", query = "SELECT t FROM TimeSlot t WHERE t.dayIdx = :dayIdx"),
  @NamedQuery(name = "TimeSlot.findByTimeOfDayIdx", query = "SELECT t FROM TimeSlot t WHERE t.timeOfDayIdx = :timeOfDayIdx"),
  @NamedQuery(name = "TimeSlot.findByDatum", query = "SELECT t FROM TimeSlot t WHERE t.datum = :datum"),
  @NamedQuery(name = "TimeSlot.findByStartTime", query = "SELECT t FROM TimeSlot t WHERE t.startTime = :startTime"),
  @NamedQuery(name = "TimeSlot.findByEndtime", query = "SELECT t FROM TimeSlot t WHERE t.endTime = :endTime"),
  @NamedQuery(name = "TimeSlot.findByDayOfWeek", query = "SELECT t FROM TimeSlot t WHERE t.dayOfWeek = :dayOfWeek"),
  @NamedQuery(name = "TimeSlot.findByLabel", query = "SELECT t FROM TimeSlot t WHERE t.label = :label"),
  @NamedQuery(name = "TimeSlot.findByTimeOfDayPrint", query = "SELECT t FROM TimeSlot t WHERE t.timeOfDayPrint = :timeOfDayPrint")})
public class TimeSlot implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "TIMESLOTID")
  private Integer timeSlotId;
  @Column(name = "DAYIDX")
  private Integer dayIdx;
  @Column(name = "TIMEOFDAYIDX")
  private Integer timeOfDayIdx;
  @Column(name = "DATUM")
  @Temporal(TemporalType.DATE)
  private Date datum;
  @Column(name = "STARTTIME")
  @Temporal(TemporalType.TIME)
  private Date startTime;
  @Column(name = "ENDTIME")
  @Temporal(TemporalType.TIME)
  private Date endTime;
  @Column(name = "DAYOFWEEK")
  private String dayOfWeek;
  @Column(name = "LABEL")
  private String label;
  @Column(name = "TIMEOFDAYPRINT")
  private String timeOfDayPrint;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "timeSlot")
  private List<Availability> availabilityList;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "timeSlot")
  private List<Event> eventList;
  public static final String PROP_EVENTREMOVED = "PROP_EVENTREMOVED";
  public static final String PROP_EVENTADDED = "PROP_EVENTADDED";
  public static final String PROP_AVAILABILITYREMOVED = "PROP_AVAILABILITYREMOVED";
  public static final String PROP_AVAILABILITYADDED = "PROP_AVAILABILITYADDED";

  public TimeSlot() {
  }

  public TimeSlot(Integer timeSlotId) {
    this.timeSlotId = timeSlotId;
  }

  public Integer getTimeSlotId() {
    return timeSlotId;
  }

  public Integer getDayIdx() {
    return dayIdx;
  }

  public Integer getTimeOfDayIdx() {
    return timeOfDayIdx;
  }

  public Date getDatum() {
    return datum;
  }

  public Date getStartTime() {
    return startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public String getDayOfWeek() {
    return dayOfWeek;
  }

  public String getLabel() {
    return label;
  }

  public String getTimeOfDayPrint() {
    return timeOfDayPrint;
  }

  @XmlTransient
  public List<Availability> getAvailabilityList() {
    return availabilityList;
  }

  @XmlTransient
  public List<Event> getEventList() {
    return eventList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (timeSlotId != null ? timeSlotId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof TimeSlot)) {
      return false;
    }
    TimeSlot other = (TimeSlot) object;
    if ((this.timeSlotId == null && other.timeSlotId != null) || (this.timeSlotId != null && !this.timeSlotId.equals(other.timeSlotId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.TimeSlot[ timeSlotId=" + timeSlotId + " ]";
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener(listener, this.timeSlotId);
  }

  public static void addPropertyChangeListener(PropertyChangeListener listener, Integer timeSlotId) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(TimeSlot.class, timeSlotId));
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.timeSlotId);
  }

  public static void removePropertyChangeListener(PropertyChangeListener listener, Integer timeSlotId) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(TimeSlot.class, timeSlotId));

  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(TimeSlot.class, timeSlotId);
  }

  protected void removeEvent(Event e) {
    if (eventList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (!eventList.contains(e)) {
      return;
    }
    eventList.remove(e);
    firePropertyChange(PROP_EVENTREMOVED, e.identity(), null);
  }

  protected void addEvent(Event e) {
    assert (e != null);
    if (eventList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (eventList.contains(e)) {
      return;
    }
    if (this != e.getTimeSlot()) {
      throw new RuntimeException("Entity missmatch.");
    }
    eventList.add(e);
    firePropertyChange(PROP_EVENTADDED, null, e.identity());
  }

  void removeAvailability(Availability a) {
    if (availabilityList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (!availabilityList.contains(a)) {
      return;
    }
    availabilityList.remove(a);
    firePropertyChange(PROP_AVAILABILITYREMOVED, a.identity(), null);
  }

  void addAvailability(Availability a) {
    assert (a != null);
    if (availabilityList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (availabilityList.contains(a)) {
      return;
    }
    if (this != a.getTimeSlot()) {
      throw new RuntimeException("Entity missmatch.");
    }
    availabilityList.add(a);
    firePropertyChange(PROP_AVAILABILITYADDED, null, a.identity());
  }
}
