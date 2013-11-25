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
  @NamedQuery(name = "Event.findByEventid", query = "SELECT e FROM Event e WHERE e.eventid = :eventid"),
  @NamedQuery(name = "Event.findByConfirmed", query = "SELECT e FROM Event e WHERE e.confirmed = :confirmed")})
public class Event implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "EVENTID")
  private Integer eventid;
  @Column(name = "CONFIRMED")
  private Integer confirmed;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
  private List<Allocation> allocationList;
  @JoinColumn(name = "TIMESLOT", referencedColumnName = "TIMESLOTID")
  @ManyToOne(optional = false)
  private Timeslot timeslot;
  @JoinColumn(name = "LOCATION", referencedColumnName = "LOCATIONID")
  @ManyToOne
  private Location location;
  @JoinColumn(name = "CONTEST", referencedColumnName = "CONTESTID")
  @ManyToOne(optional = false)
  private Contest contest;

  public Event() {
  }

  public Event(Integer eventid) {
    this.eventid = eventid;
  }

  public Integer getEventid() {
    return eventid;
  }

  public void setEventid(Integer eventid) {
    this.eventid = eventid;
  }

  public Integer getConfirmed() {
    return confirmed;
  }

  public void setConfirmed(Integer confirmed) {
    this.confirmed = confirmed;
  }

  @XmlTransient
  public List<Allocation> getAllocationList() {
    return allocationList;
  }

  public void setAllocationList(List<Allocation> allocationList) {
    this.allocationList = allocationList;
  }

  public Timeslot getTimeslot() {
    return timeslot;
  }

  public void setTimeslot(Timeslot timeslot) {
    this.timeslot = timeslot;
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
    hash += (eventid != null ? eventid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Event)) {
      return false;
    }
    Event other = (Event) object;
    if ((this.eventid == null && other.eventid != null) || (this.eventid != null && !this.eventid.equals(other.eventid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Event[ eventid=" + eventid + " ]";
  }
  
}
