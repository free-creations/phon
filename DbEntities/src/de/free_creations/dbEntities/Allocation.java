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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Harald <Harald at free-creations.de>
 */
@Entity
@Table(name = "ALLOCATION")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Allocation.findAll", query = "SELECT t FROM Allocation t"),
  @NamedQuery(name = "Allocation.findByZeitid", query = "SELECT t FROM Allocation t WHERE t.zeit = :zeitid"),
  @NamedQuery(name = "Allocation.findByJuryid", query = "SELECT t FROM Allocation t WHERE t.contestid = :juryid"),
  @NamedQuery(name = "Allocation.findByFunktionid", query = "SELECT t FROM Allocation t WHERE t.funktionen = :funktionid"),
  @NamedQuery(name = "Allocation.findByLetzteaenderung", query = "SELECT t FROM Allocation t WHERE t.letzteaenderung = :letzteaenderung"),
  @NamedQuery(name = "Allocation.findByPlaner", query = "SELECT t FROM Allocation t WHERE t.planer = :planer"),
  @NamedQuery(name = "Allocation.findByErklaerung", query = "SELECT t FROM Allocation t WHERE t.erklaerung = :erklaerung"),})
public class Allocation implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ALLOCATIONID")
  private Integer allocationid;
  @Column(name = "LETZTEAENDERUNG")
  @Temporal(TemporalType.TIMESTAMP)
  private Date letzteaenderung;
  @Column(name = "PLANER")
  private String planer;
  @Column(name = "ERKLAERUNG")
  private String erklaerung;
  @JoinColumn(name = "ZEITID", referencedColumnName = "ZEITID")
  @ManyToOne(optional = false)
  private TimeSlot zeit;
  @JoinColumn(name = "PERSONID", referencedColumnName = "PERSONID")
  @ManyToOne(optional = false)
  private Person personid;
  @JoinColumn(name = "FUNKTIONID", referencedColumnName = "FUNKTIONID")
  @ManyToOne(optional = false)
  private Job funktionen;
  @JoinColumn(name = "CONTESTID", referencedColumnName = "CONTESTID")
  @ManyToOne(optional = false)
  private Contest contestid;

//  public final static String PROP_FUNCTION = "function";
//  public final static String PROP_JURY = "jury";
//  public final static String PROP_ZEIT = "zeit";
  public final static String PROP_PERSON = "person";
  public final static String PROP_SELF_REMOVED = "selfRemoved";

  public Allocation() {
  }

  public Allocation(Integer allocationid) {
    this.allocationid = allocationid;
  }

  public Integer getAllocationid() {
    return allocationid;
  }

  public Allocation(TimeSlot z, Contest j, Job f) {
    this();
    setZeit(z);
    setJury(j);
    setFunktionen(f);
  }

  public void prepareRemoval() {
    setZeit(null);
    setJury(null);
    setFunktionen(null);
    setPersonid(null);
    firePropertyChange(PROP_SELF_REMOVED, null, null);
    PropertyChangeManager.instance().removeAllListeners(identity());
  }

  public Date getLetzteaenderung() {
    return letzteaenderung;
  }

  public void setLetzteaenderung(Date letzteaenderung) {
    this.letzteaenderung = letzteaenderung;
  }

  public String getPlaner() {
    return planer;
  }

  public void setPlaner(String planer) {
    this.planer = planer;
  }

  public String getErklaerung() {
    return erklaerung;
  }

  public void setErklaerung(String erklaerung) {
    this.erklaerung = erklaerung;
  }

  public TimeSlot getZeit() {
    return zeit;
  }

  public final void setZeit(TimeSlot zeit) {
    TimeSlot old = this.zeit;
    this.zeit = zeit;
//    EntityIdentity newId = (zeit == null) ? null : zeit.identity();
//    EntityIdentity oldId = (old == null) ? null : old.identity();

    if (!Objects.equals(old, zeit)) {
      //firePropertyChange(PROP_ZEIT, oldId, newId);
      if (old != null) {
        old.removeTeameinteilung(this);
      }
      if (zeit != null) {
        zeit.addTeameinteilung(this);
      }
    }
  }

  public Person getPersonid() {
    return personid;
  }

  public void setPersonid(Person personid) {
    Person old = this.personid;
    this.personid = personid;
    EntityIdentity newId = (personid == null) ? null : personid.identity();
    EntityIdentity oldId = (old == null) ? null : old.identity();

    if (!Objects.equals(old, personid)) {
      firePropertyChange(PROP_PERSON, oldId, newId);
      if (old != null) {
        old.removeTeameinteilung(this);
      }
      if (personid != null) {
        personid.addTeameinteilung(this);
      }
    }
  }

  public Contest getJury() {
    return contestid;
  }

  public final void setJury(Contest jury) {
    Contest old = this.contestid;
    this.contestid = jury;
//    EntityIdentity newId = (jury == null) ? null : jury.identity();
//    EntityIdentity oldId = (old == null) ? null : old.identity();

    if (!Objects.equals(old, jury)) {
      //firePropertyChange(PROP_JURY, oldId, newId);
      if (old != null) {
        old.removeTeameinteilung(this);
      }
      if (jury != null) {
        jury.addTeameinteilung(this);
      }
    }

  }

  public Job getFunktionen() {
    return funktionen;
  }

  public final void setFunktionen(Job funktionen) {
    Job old = this.funktionen;
    this.funktionen = funktionen;
//    EntityIdentity newId = (funktionen == null) ? null : funktionen.identity();
//    EntityIdentity oldId = (old == null) ? null : old.identity();

    if (!Objects.equals(old, funktionen)) {
      //firePropertyChange(PROP_FUNCTION, oldId, newId);
      if (old != null) {
        old.removeTeameinteilung(this);
      }
      if (funktionen != null) {
        funktionen.addTeameinteilung(this);
      }
    }
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (allocationid != null ? allocationid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Allocation)) {
      return false;
    }
    Allocation other = (Allocation) object;
    if ((this.allocationid == null && other.allocationid != null) || (this.allocationid != null && !this.allocationid.equals(other.allocationid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Allocation" + allocationid;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    assert (this.allocationid != null);
    addPropertyChangeListener(listener, this.allocationid);
  }

  /**
   * Add a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param allocationid
   */
  public static void addPropertyChangeListener(PropertyChangeListener listener, Integer allocationid) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Allocation.class, allocationid));
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * instance.
   *
   * @param listener
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.allocationid);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener the listener to be removed.
   * @param allocationid the primary key
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, Integer allocationid) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Allocation.class, allocationid));

  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Allocation.class, allocationid);
  }
}
