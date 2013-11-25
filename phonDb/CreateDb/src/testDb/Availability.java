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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
@Entity
@Table(name = "AVAILABILITY")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Availability.findAll", query = "SELECT a FROM Availability a"),
  @NamedQuery(name = "Availability.findByAvailabilityid", query = "SELECT a FROM Availability a WHERE a.availabilityid = :availabilityid"),
  @NamedQuery(name = "Availability.findByAvailable", query = "SELECT a FROM Availability a WHERE a.available = :available"),
  @NamedQuery(name = "Availability.findByLastchange", query = "SELECT a FROM Availability a WHERE a.lastchange = :lastchange")})
public class Availability implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "AVAILABILITYID")
  private Integer availabilityid;
  @Column(name = "AVAILABLE")
  private Integer available;
  @Column(name = "LASTCHANGE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastchange;
  @JoinColumn(name = "TIMESLOT", referencedColumnName = "TIMESLOTID")
  @ManyToOne(optional = false)
  private Timeslot timeslot;
  @JoinColumn(name = "PERSON", referencedColumnName = "PERSONID")
  @ManyToOne(optional = false)
  private Person person;

  public Availability() {
  }

  public Availability(Integer availabilityid) {
    this.availabilityid = availabilityid;
  }

  public Integer getAvailabilityid() {
    return availabilityid;
  }

  public void setAvailabilityid(Integer availabilityid) {
    this.availabilityid = availabilityid;
  }

  public Integer getAvailable() {
    return available;
  }

  public void setAvailable(Integer available) {
    this.available = available;
  }

  public Date getLastchange() {
    return lastchange;
  }

  public void setLastchange(Date lastchange) {
    this.lastchange = lastchange;
  }

  public Timeslot getTimeslot() {
    return timeslot;
  }

  public void setTimeslot(Timeslot timeslot) {
    this.timeslot = timeslot;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (availabilityid != null ? availabilityid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Availability)) {
      return false;
    }
    Availability other = (Availability) object;
    if ((this.availabilityid == null && other.availabilityid != null) || (this.availabilityid != null && !this.availabilityid.equals(other.availabilityid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Availability[ availabilityid=" + availabilityid + " ]";
  }
  
}
