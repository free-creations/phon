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
 * @author Harald <Harald at free-creations.de>
 */
@Entity
@Table(name = "TIMESLOT")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "TimeSlot.findAll", query = "SELECT z FROM TimeSlot z"),
  @NamedQuery(name = "TimeSlot.findByZeitid", query = "SELECT z FROM TimeSlot z WHERE z.zeitid = :zeitid"),
  @NamedQuery(name = "TimeSlot.findByTag", query = "SELECT z FROM TimeSlot z WHERE z.tag = :tag"),
  @NamedQuery(name = "TimeSlot.findByTageszeit", query = "SELECT z FROM TimeSlot z WHERE z.tageszeit = :tageszeit"),
  @NamedQuery(name = "TimeSlot.findByDatum", query = "SELECT z FROM TimeSlot z WHERE z.datum = :datum"),
  @NamedQuery(name = "TimeSlot.findByStartzeit", query = "SELECT z FROM TimeSlot z WHERE z.startzeit = :startzeit"),
  @NamedQuery(name = "TimeSlot.findByEndezeit", query = "SELECT z FROM TimeSlot z WHERE z.endezeit = :endezeit"),
  @NamedQuery(name = "TimeSlot.findByWochentag", query = "SELECT z FROM TimeSlot z WHERE z.wochentag = :wochentag"),
  @NamedQuery(name = "TimeSlot.findByLabel", query = "SELECT z FROM TimeSlot z WHERE z.label = :label"),
  @NamedQuery(name = "TimeSlot.findByTageszeitprint", query = "SELECT z FROM TimeSlot z WHERE z.tageszeitprint = :tageszeitprint")})
public class TimeSlot implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "ZEITID")
  private Integer zeitid;
  @Column(name = "TAG")
  private Integer tag;
  @Column(name = "TAGESZEIT")
  private Integer tageszeit;
  @Column(name = "DATUM")
  @Temporal(TemporalType.DATE)
  private Date datum;
  @Column(name = "STARTZEIT")
  @Temporal(TemporalType.TIME)
  private Date startzeit;
  @Column(name = "ENDEZEIT")
  @Temporal(TemporalType.TIME)
  private Date endezeit;
  @Column(name = "WOCHENTAG")
  private String wochentag;
  @Column(name = "LABEL")
  private String label;
  @Column(name = "TAGESZEITPRINT")
  private String tageszeitprint;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "zeit")
  private List<Allocation> teameinteilungList;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "zeitid")
  private List<Availability> verfuegbarkeitList;
  public static final String PROP_VERFUEGBARKEIT = "verfuegbarkeit";
  public static final String PROP_ADD_TEAMEINTEILUNG = "addTeameinteilung";
  public static final String PROP_REMOVE_TEAMEINTEILUNG = "removeTeameinteilung";

  public TimeSlot() {
  }

  /**
   * Can be used in tests to create an initialized item.
   *
   * @param tag
   * @param tageszeit
   * @param wochentag
   * @param label
   * @param tageszeitprint
   * @deprecated use only for tests.
   */
  public TimeSlot(Integer zeitid, Integer tag, Integer tageszeit, String wochentag, String label, String tageszeitprint) {
    this.zeitid = zeitid;
    this.tag = tag;
    this.tageszeit = tageszeit;
    this.wochentag = wochentag;
    this.label = label;
    this.tageszeitprint = tageszeitprint;
  }

  public TimeSlot(Integer zeitid) {
    this.zeitid = zeitid;
  }

  public Integer getZeitid() {
    return zeitid;
  }

  protected void setZeitid(Integer zeitid) {
    this.zeitid = zeitid;
  }

  public Integer getTag() {
    return tag;
  }

  public Integer getTageszeit() {
    return tageszeit;
  }

  public Date getDatum() {
    return datum;
  }

  public Date getStartzeit() {
    return startzeit;
  }

  public Date getEndezeit() {
    return endezeit;
  }

  public String getWochentag() {
    return wochentag;
  }

  public String getLabel() {
    return label;
  }

  public String getTageszeitprint() {
    return tageszeitprint;
  }

  @XmlTransient
  public List<Allocation> getTeameinteilungList() {
    return teameinteilungList;
  }

  public void setTeameinteilungList(List<Allocation> teameinteilungList) {
    this.teameinteilungList = teameinteilungList;
  }

  void addTeameinteilung(Allocation t) {
    assert (t != null);
    if (teameinteilungList == null) {
      throw new RuntimeException("Cannot add a Allocation to this Time-Slot. Record must be persited");
    }
    if (teameinteilungList.contains(t)) {
      return;
    }
    if (t.getZeit() != this) {
      throw new RuntimeException("Cannot add Allocation for an other Time-Slot.");
    }
    teameinteilungList.add(t);
    firePropertyChange(PROP_ADD_TEAMEINTEILUNG, null, t.identity());
  }

  void removeTeameinteilung(Allocation t) {
    if (teameinteilungList == null) {
      throw new RuntimeException("Cannot add a Allocation to this Time-Slot. Record must be persited");
    }
    if (!teameinteilungList.contains(t)) {
      return;
    }
    teameinteilungList.remove(t);
    assert (t != null);
    firePropertyChange(PROP_REMOVE_TEAMEINTEILUNG, t.identity(), null);
  }

  @XmlTransient
  public List<Availability> getVerfuegbarkeitList() {
    return verfuegbarkeitList;
  }

  /**
   * Adds a VERFUEGBARKEIT to this time-slot.
   *
   * @param v a new VERFUEGBARKEIT record. It is assumed that this record is not
   * assigned to an other time-slot and that the this entity has been persisted.
   */
  protected void addVerfuegbarkeit(Availability v) {
    assert (v != null);
    if (verfuegbarkeitList == null) {
      throw new RuntimeException("Cannot add Verfügbarkeit. Record must be persited");
    }
    if (verfuegbarkeitList.contains(v)) {
      return;
    }
    if (v.getZeitid() != this) {
      throw new RuntimeException("Cannot add Verfügbarkeit used for other time-slot.");
    }

    verfuegbarkeitList.add(v);
    firePropertyChange(PROP_VERFUEGBARKEIT, null, v);
  }

  void removeVerfuegbarkeit(Availability v) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (zeitid != null ? zeitid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof TimeSlot)) {
      return false;
    }
    TimeSlot other = (TimeSlot) object;
    if ((this.zeitid == null && other.zeitid != null) || (this.zeitid != null && !this.zeitid.equals(other.zeitid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.free_creations.dbEntities.TimeSlot[ zeitid=" + zeitid + " ]";
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.zeitid);
  }

  public static void removePropertyChangeListener(PropertyChangeListener listener, Integer zeitid) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Person.class, zeitid));

  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(TimeSlot.class, zeitid);
  }
}