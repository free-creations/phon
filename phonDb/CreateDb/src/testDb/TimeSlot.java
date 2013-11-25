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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
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
public class TimeSlot implements Serializable {
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

  public TimeSlot() {
  }

  public TimeSlot(Integer timeSlotId) {
    this.timeSlotId = timeSlotId;
  }

  public Integer getTimeSlotId() {
    return timeSlotId;
  }

  public void setTimeSlotId(Integer timeSlotId) {
    this.timeSlotId = timeSlotId;
  }

  public Integer getDayIdx() {
    return dayIdx;
  }

  public void setDayIdx(Integer dayIdx) {
    this.dayIdx = dayIdx;
  }

  public Integer getTimeOfDayIdx() {
    return timeOfDayIdx;
  }

  public void setTimeOfDayIdx(Integer timeOfDayIdx) {
    this.timeOfDayIdx = timeOfDayIdx;
  }

  public Date getDatum() {
    return datum;
  }

  public void setDatum(Date datum) {
    this.datum = datum;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndtime() {
    return endTime;
  }

  public void setEndtime(Date endTime) {
    this.endTime = endTime;
  }

  public String getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(String dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getTimeOfDayPrint() {
    return timeOfDayPrint;
  }

  public void setTimeOfDayPrint(String timeOfDayPrint) {
    this.timeOfDayPrint = timeOfDayPrint;
  }

  @XmlTransient
  public List<Availability> getAvailabilityList() {
    return availabilityList;
  }

  public void setAvailabilityList(List<Availability> availabilityList) {
    this.availabilityList = availabilityList;
  }

  @XmlTransient
  public List<Event> getEventList() {
    return eventList;
  }

  public void setEventList(List<Event> eventList) {
    this.eventList = eventList;
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
  
}
