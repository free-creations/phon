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
  @NamedQuery(name = "Timeslot.findAll", query = "SELECT t FROM Timeslot t"),
  @NamedQuery(name = "Timeslot.findByTimeslotid", query = "SELECT t FROM Timeslot t WHERE t.timeslotid = :timeslotid"),
  @NamedQuery(name = "Timeslot.findByDay", query = "SELECT t FROM Timeslot t WHERE t.day = :day"),
  @NamedQuery(name = "Timeslot.findByTimeofday", query = "SELECT t FROM Timeslot t WHERE t.timeofday = :timeofday"),
  @NamedQuery(name = "Timeslot.findByDatum", query = "SELECT t FROM Timeslot t WHERE t.datum = :datum"),
  @NamedQuery(name = "Timeslot.findByStart", query = "SELECT t FROM Timeslot t WHERE t.start = :start"),
  @NamedQuery(name = "Timeslot.findByEnd", query = "SELECT t FROM Timeslot t WHERE t.end = :end"),
  @NamedQuery(name = "Timeslot.findByWochentag", query = "SELECT t FROM Timeslot t WHERE t.wochentag = :wochentag"),
  @NamedQuery(name = "Timeslot.findByLabel", query = "SELECT t FROM Timeslot t WHERE t.label = :label"),
  @NamedQuery(name = "Timeslot.findByTimeofdayprint", query = "SELECT t FROM Timeslot t WHERE t.timeofdayprint = :timeofdayprint")})
public class Timeslot implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "TIMESLOTID")
  private Integer timeslotid;
  @Column(name = "DAY")
  private Integer day;
  @Column(name = "TIMEOFDAY")
  private Integer timeofday;
  @Column(name = "DATUM")
  @Temporal(TemporalType.DATE)
  private Date datum;
  @Column(name = "START")
  @Temporal(TemporalType.TIME)
  private Date start;
  @Column(name = "END")
  @Temporal(TemporalType.TIME)
  private Date end;
  @Column(name = "WOCHENTAG")
  private String wochentag;
  @Column(name = "LABEL")
  private String label;
  @Column(name = "TIMEOFDAYPRINT")
  private String timeofdayprint;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "timeslot")
  private List<Availability> availabilityList;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "timeslot")
  private List<Event> eventList;

  public Timeslot() {
  }

  public Timeslot(Integer timeslotid) {
    this.timeslotid = timeslotid;
  }

  public Integer getTimeslotid() {
    return timeslotid;
  }

  public void setTimeslotid(Integer timeslotid) {
    this.timeslotid = timeslotid;
  }

  public Integer getDay() {
    return day;
  }

  public void setDay(Integer day) {
    this.day = day;
  }

  public Integer getTimeofday() {
    return timeofday;
  }

  public void setTimeofday(Integer timeofday) {
    this.timeofday = timeofday;
  }

  public Date getDatum() {
    return datum;
  }

  public void setDatum(Date datum) {
    this.datum = datum;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public String getWochentag() {
    return wochentag;
  }

  public void setWochentag(String wochentag) {
    this.wochentag = wochentag;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getTimeofdayprint() {
    return timeofdayprint;
  }

  public void setTimeofdayprint(String timeofdayprint) {
    this.timeofdayprint = timeofdayprint;
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
    hash += (timeslotid != null ? timeslotid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Timeslot)) {
      return false;
    }
    Timeslot other = (Timeslot) object;
    if ((this.timeslotid == null && other.timeslotid != null) || (this.timeslotid != null && !this.timeslotid.equals(other.timeslotid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Timeslot[ timeslotid=" + timeslotid + " ]";
  }
  
}
