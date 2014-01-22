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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "LOCATION")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l"),
  @NamedQuery(name = "Location.findByLocationId", query = "SELECT l FROM Location l WHERE l.locationId = :locationId"),
  @NamedQuery(name = "Location.findByName", query = "SELECT l FROM Location l WHERE l.name = :name"),
  @NamedQuery(name = "Location.findByBuilding", query = "SELECT l FROM Location l WHERE l.building = :building"),
  @NamedQuery(name = "Location.findByRoom", query = "SELECT l FROM Location l WHERE l.room = :room"),
  @NamedQuery(name = "Location.findByStreet", query = "SELECT l FROM Location l WHERE l.street = :street"),
  @NamedQuery(name = "Location.findByTown", query = "SELECT l FROM Location l WHERE l.town = :town"),
  @NamedQuery(name = "Location.findByGridnumber", query = "SELECT l FROM Location l WHERE l.gridnumber = :gridnumber")})
public class Location implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "LOCATIONID")
  private Integer locationId;
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
  public static final String PROP_NAME = "locPROP_NAME";
  public static final String PROP_BUILDING = "locPROP_BUILDING";
  public static final String PROP_STREET = "locPROP_STREET";
  public static final String PROP_TOWN = "locPROP_TOWN";
  public static final String PROP_GRIDNUMBER = "locPROP_GRIDNUMBER";
  public static final String PROP_ROOM = "locPROP_ROOM";
  public static final String PROP_EVENTREMOVED = "locPROP_EVENTREMOVED";
  public static final String PROP_EVENTADDED = "locPROP_EVENTADDED";

  public Location() {
  }

  public Location(Integer locationid) {
    this.locationId = locationid;
  }

  public Integer getLocationId() {
    return locationId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    String old = this.name;
    this.name = name;
    if (!Objects.equals(old, name)) {
      firePropertyChange(PROP_NAME, old, name);
    }
  }

  public String getBuilding() {
    return building;
  }

  public void setBuilding(String building) {
    String old = this.building;
    this.building = building;
    if (!Objects.equals(old, building)) {
      firePropertyChange(PROP_BUILDING, old, building);
    }
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    String old = this.room;
    this.room = room;
    if (!Objects.equals(old, room)) {
      firePropertyChange(PROP_ROOM, old, room);
    }
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    String old = this.street;
    this.street = street;
    if (!Objects.equals(old, street)) {
      firePropertyChange(PROP_STREET, old, street);
    }
  }

  public String getTown() {
    return town;
  }

  public void setTown(String town) {
    String old = this.town;
    this.town = town;
    if (!Objects.equals(old, town)) {
      firePropertyChange(PROP_TOWN, old, town);
    }
  }

  public String getGridnumber() {
    return gridnumber;
  }

  public void setGridnumber(String gridnumber) {
    String old = this.gridnumber;
    this.gridnumber = gridnumber;
    if (!Objects.equals(old, gridnumber)) {
      firePropertyChange(PROP_GRIDNUMBER, old, gridnumber);
    }
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
    hash += (locationId != null ? locationId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Location)) {
      return false;
    }
    Location other = (Location) object;
    if ((this.locationId == null && other.locationId != null) || (this.locationId != null && !this.locationId.equals(other.locationId))) {
      return false;
    }
    return true;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener(listener, this.locationId);
  }

  public static void addPropertyChangeListener(PropertyChangeListener listener, Integer locationId) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Location.class, locationId));
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.locationId);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param locationId
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, Integer locationId) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Location.class, locationId));
  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Location.class, locationId);
  }

  @Override
  public String toString() {
    return "Location[" + locationId + "]";
  }

  void removeEvent(Event e) {
    if (eventList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (!eventList.contains(e)) {
      return;
    }
    eventList.remove(e);
    firePropertyChange(PROP_EVENTREMOVED, e.identity(), null);
  }

  void addEvent(Event e) {
    assert (e != null);
    if (eventList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (eventList.contains(e)) {
      return;
    }
    if (this != e.getLocation()) {
      throw new RuntimeException("Entity missmatch.");
    }
    eventList.add(e);
    firePropertyChange(PROP_EVENTADDED, null, e.identity());
  }

}
