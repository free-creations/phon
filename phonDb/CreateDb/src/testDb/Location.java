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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "LOCATION")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l"),
  @NamedQuery(name = "Location.findByLocationid", query = "SELECT l FROM Location l WHERE l.locationid = :locationid"),
  @NamedQuery(name = "Location.findByName", query = "SELECT l FROM Location l WHERE l.name = :name"),
  @NamedQuery(name = "Location.findByBuilding", query = "SELECT l FROM Location l WHERE l.building = :building"),
  @NamedQuery(name = "Location.findByRoom", query = "SELECT l FROM Location l WHERE l.room = :room"),
  @NamedQuery(name = "Location.findByStreet", query = "SELECT l FROM Location l WHERE l.street = :street"),
  @NamedQuery(name = "Location.findByTown", query = "SELECT l FROM Location l WHERE l.town = :town"),
  @NamedQuery(name = "Location.findByGridnumber", query = "SELECT l FROM Location l WHERE l.gridnumber = :gridnumber")})
public class Location implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "LOCATIONID")
  private Integer locationid;
  @Column(name = "NAME")
  private String name;
  @Column(name = "BUILDING")
  private String building;
  @Column(name = "ROOM")
  private String room;
  @Column(name = "STREET")
  private String street;
  @Column(name = "TOWN")
  private String town;
  @Column(name = "GRIDNUMBER")
  private String gridnumber;
  @OneToMany(mappedBy = "location")
  private List<Event> eventList;

  public Location() {
  }

  public Location(Integer locationid) {
    this.locationid = locationid;
  }

  public Integer getLocationid() {
    return locationid;
  }

  public void setLocationid(Integer locationid) {
    this.locationid = locationid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBuilding() {
    return building;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getTown() {
    return town;
  }

  public void setTown(String town) {
    this.town = town;
  }

  public String getGridnumber() {
    return gridnumber;
  }

  public void setGridnumber(String gridnumber) {
    this.gridnumber = gridnumber;
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
    hash += (locationid != null ? locationid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Location)) {
      return false;
    }
    Location other = (Location) object;
    if ((this.locationid == null && other.locationid != null) || (this.locationid != null && !this.locationid.equals(other.locationid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Location[ locationid=" + locationid + " ]";
  }
  
}
