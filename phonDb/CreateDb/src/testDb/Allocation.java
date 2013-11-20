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
@Table(name = "ALLOCATION")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Allocation.findAll", query = "SELECT a FROM Allocation a"),
  @NamedQuery(name = "Allocation.findByAllocationid", query = "SELECT a FROM Allocation a WHERE a.allocationid = :allocationid"),
  @NamedQuery(name = "Allocation.findByLetzteaenderung", query = "SELECT a FROM Allocation a WHERE a.letzteaenderung = :letzteaenderung"),
  @NamedQuery(name = "Allocation.findByPlaner", query = "SELECT a FROM Allocation a WHERE a.planer = :planer"),
  @NamedQuery(name = "Allocation.findByErklaerung", query = "SELECT a FROM Allocation a WHERE a.erklaerung = :erklaerung")})
public class Allocation implements Serializable {
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
  private Timeslot zeitid;
  @JoinColumn(name = "PERSONID", referencedColumnName = "PERSONID")
  @ManyToOne(optional = false)
  private Person personid;
  @JoinColumn(name = "FUNKTIONID", referencedColumnName = "FUNKTIONID")
  @ManyToOne(optional = false)
  private Job funktionid;
  @JoinColumn(name = "CONTESTID", referencedColumnName = "CONTESTID")
  @ManyToOne(optional = false)
  private Contest contestid;

  public Allocation() {
  }

  public Allocation(Integer allocationid) {
    this.allocationid = allocationid;
  }

  public Integer getAllocationid() {
    return allocationid;
  }

  public void setAllocationid(Integer allocationid) {
    this.allocationid = allocationid;
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

  public Timeslot getZeitid() {
    return zeitid;
  }

  public void setZeitid(Timeslot zeitid) {
    this.zeitid = zeitid;
  }

  public Person getPersonid() {
    return personid;
  }

  public void setPersonid(Person personid) {
    this.personid = personid;
  }

  public Job getFunktionid() {
    return funktionid;
  }

  public void setFunktionid(Job funktionid) {
    this.funktionid = funktionid;
  }

  public Contest getContestid() {
    return contestid;
  }

  public void setContestid(Contest contestid) {
    this.contestid = contestid;
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
    return "testDb.Allocation[ allocationid=" + allocationid + " ]";
  }
  
}
