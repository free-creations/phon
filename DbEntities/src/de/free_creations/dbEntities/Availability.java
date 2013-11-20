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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Harald <Harald at free-creations.de>
 */
@Entity
@Table(name = "AVAILABILITY")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Availability.findAll", query = "SELECT v FROM Availability v"),
  @NamedQuery(name = "Availability.findByVerfuegid", query = "SELECT v FROM Availability v WHERE v.verfuegid = :verfuegid"),
  @NamedQuery(name = "Availability.findByVerfuegbar", query = "SELECT v FROM Availability v WHERE v.verfuegbar = :verfuegbar"),
  @NamedQuery(name = "Availability.findByLetzteaenderung", query = "SELECT v FROM Availability v WHERE v.letzteaenderung = :letzteaenderung")})
public class Availability implements Serializable , DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "VERFUEGID")
  private Integer verfuegid;
  @Column(name = "VERFUEGBAR")
  private Integer verfuegbar;
  @Column(name = "LETZTEAENDERUNG")
  @Temporal(TemporalType.TIMESTAMP)
  private Date letzteaenderung;
  private final static String PROP_ZEIT = "zeit";
  @JoinColumn(name = "ZEITID", referencedColumnName = "ZEITID")
  @ManyToOne(optional = false)
  private TimeSlot zeitid;
  final private static String PROP_PERSON = "person";
  @JoinColumn(name = "PERSONID", referencedColumnName = "PERSONID")
  @ManyToOne(optional = false)
  private Person personid;

  public Availability() {
  }

  public Availability(Integer verfuegid) {
    this.verfuegid = verfuegid;
  }

  public Integer getVerfuegid() {
    return verfuegid;
  }

  public void setVerfuegid(Integer verfuegid) {
    this.verfuegid = verfuegid;
  }

  public Integer getVerfuegbar() {
    return verfuegbar;
  }

  /**
   * 
   * @return true if the person is available for the given time
   */
  public boolean isVerfuegbar() {
    return (verfuegbar == null) ? false : verfuegbar != 0;
  }

  public void setVerfuegbar(Integer verfuegbar) {
    Integer old = this.verfuegbar;
    this.verfuegbar = verfuegbar;
    if (!Objects.equals(old, verfuegbar)) {
      firePropertyChangeOnSelf(Person.PROP_VERFUEGBARKEIT, old, verfuegbar);
      if (personid != null) {
        firePropertyChangeOnPerson(personid.getPersonid(), Person.PROP_VERFUEGBARKEIT, old, verfuegbar);
      }
    }
  }

  public void setVerfuegbar(boolean isVerfuegbar) {
    if (isVerfuegbar) {
      setVerfuegbar(1);
    } else {
      setVerfuegbar(0);
    }
  }

  public Date getLetzteaenderung() {
    return letzteaenderung;
  }

  public void setLetzteaenderung(Date letzteaenderung) {
    this.letzteaenderung = letzteaenderung;
  }

  public TimeSlot getZeitid() {
    return zeitid;
  }

  /**
   * Set the time-slot for which this disponibility record holds.
   * @param zeitid 
   */
  public void setZeitid(TimeSlot zeitid) {
    TimeSlot old = this.zeitid;
    this.zeitid = zeitid;
    EntityIdentity newId = (zeitid == null) ? null : zeitid.identity();
    EntityIdentity oldId = (old == null) ? null : old.identity();

    if (!Objects.equals(old, zeitid)) {
      firePropertyChangeOnSelf(Availability.PROP_ZEIT, oldId, newId);
      if (old != null) {
        old.removeVerfuegbarkeit(this);
      }
      if (zeitid != null) {
        zeitid.addVerfuegbarkeit(this);
      }
    }
  }

  public Person getPersonid() {
    return personid;
  }

  /**
   * Note: before deleting an existing "VERFUEGBARKEIT" record, set the foreign
   * key "person" to null. This will ensure that the Person entity is informed
   * about the property change.
   *
   * @param person
   */
  public void setPersonid(Person person) {
    Person old = this.personid;
    this.personid = person;
    EntityIdentity newId = (person == null) ? null : person.identity();
    EntityIdentity oldId = (old == null) ? null : old.identity();

    if (!Objects.equals(old, person)) {
      firePropertyChangeOnSelf(Availability.PROP_PERSON, oldId, newId);
      if (old != null) {
        old.removeVerfuegbarkeit(this);
      }
      if (person != null) {
        person.addVerfuegbarkeit(this);
      }
    }
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (verfuegid != null ? verfuegid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Availability)) {
      return false;
    }
    Availability other = (Availability) object;
    if ((this.verfuegid == null && other.verfuegid != null) || (this.verfuegid != null && !this.verfuegid.equals(other.verfuegid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.free_creations.dbEntities.Availability[ verfuegid=" + verfuegid + " ]";
  }

  /**
   * Add PropertyChangeListener.
   *
   * @param listener property change listener
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(getClass(), verfuegid));
  }

  /**
   * Remove PropertyChangeListener.
   *
   * @param listener property change listener
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(getClass(), verfuegid));

  }

  private void firePropertyChangeOnSelf(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            new EntityIdentity(getClass(), verfuegid),
            propertyName, oldValue, newValue);
  }

  private void firePropertyChangeOnPerson(Integer personenId, String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            new EntityIdentity(Person.class, personenId),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Availability.class, verfuegid);
  }
}
