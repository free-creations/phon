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
import java.util.Collection;
import java.util.Date;
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
  @NamedQuery(name = "Timeslot.findByZeitid", query = "SELECT t FROM Timeslot t WHERE t.zeitid = :zeitid"),
  @NamedQuery(name = "Timeslot.findByTag", query = "SELECT t FROM Timeslot t WHERE t.tag = :tag"),
  @NamedQuery(name = "Timeslot.findByTageszeit", query = "SELECT t FROM Timeslot t WHERE t.tageszeit = :tageszeit"),
  @NamedQuery(name = "Timeslot.findByDatum", query = "SELECT t FROM Timeslot t WHERE t.datum = :datum"),
  @NamedQuery(name = "Timeslot.findByStartzeit", query = "SELECT t FROM Timeslot t WHERE t.startzeit = :startzeit"),
  @NamedQuery(name = "Timeslot.findByEndezeit", query = "SELECT t FROM Timeslot t WHERE t.endezeit = :endezeit"),
  @NamedQuery(name = "Timeslot.findByWochentag", query = "SELECT t FROM Timeslot t WHERE t.wochentag = :wochentag"),
  @NamedQuery(name = "Timeslot.findByLabel", query = "SELECT t FROM Timeslot t WHERE t.label = :label"),
  @NamedQuery(name = "Timeslot.findByTageszeitprint", query = "SELECT t FROM Timeslot t WHERE t.tageszeitprint = :tageszeitprint")})
public class Timeslot implements Serializable {
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
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "zeitid")
  private Collection<Availability> availabilityCollection;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "zeitid")
  private Collection<Allocation> allocationCollection;

  public Timeslot() {
  }

  public Timeslot(Integer zeitid) {
    this.zeitid = zeitid;
  }

  public Integer getZeitid() {
    return zeitid;
  }

  public void setZeitid(Integer zeitid) {
    this.zeitid = zeitid;
  }

  public Integer getTag() {
    return tag;
  }

  public void setTag(Integer tag) {
    this.tag = tag;
  }

  public Integer getTageszeit() {
    return tageszeit;
  }

  public void setTageszeit(Integer tageszeit) {
    this.tageszeit = tageszeit;
  }

  public Date getDatum() {
    return datum;
  }

  public void setDatum(Date datum) {
    this.datum = datum;
  }

  public Date getStartzeit() {
    return startzeit;
  }

  public void setStartzeit(Date startzeit) {
    this.startzeit = startzeit;
  }

  public Date getEndezeit() {
    return endezeit;
  }

  public void setEndezeit(Date endezeit) {
    this.endezeit = endezeit;
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

  public String getTageszeitprint() {
    return tageszeitprint;
  }

  public void setTageszeitprint(String tageszeitprint) {
    this.tageszeitprint = tageszeitprint;
  }

  @XmlTransient
  public Collection<Availability> getAvailabilityCollection() {
    return availabilityCollection;
  }

  public void setAvailabilityCollection(Collection<Availability> availabilityCollection) {
    this.availabilityCollection = availabilityCollection;
  }

  @XmlTransient
  public Collection<Allocation> getAllocationCollection() {
    return allocationCollection;
  }

  public void setAllocationCollection(Collection<Allocation> allocationCollection) {
    this.allocationCollection = allocationCollection;
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
    if (!(object instanceof Timeslot)) {
      return false;
    }
    Timeslot other = (Timeslot) object;
    if ((this.zeitid == null && other.zeitid != null) || (this.zeitid != null && !this.zeitid.equals(other.zeitid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Timeslot[ zeitid=" + zeitid + " ]";
  }
  
}
