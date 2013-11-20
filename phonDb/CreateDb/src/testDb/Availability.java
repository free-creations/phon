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
  @NamedQuery(name = "Availability.findByVerfuegid", query = "SELECT a FROM Availability a WHERE a.verfuegid = :verfuegid"),
  @NamedQuery(name = "Availability.findByVerfuegbar", query = "SELECT a FROM Availability a WHERE a.verfuegbar = :verfuegbar"),
  @NamedQuery(name = "Availability.findByLetzteaenderung", query = "SELECT a FROM Availability a WHERE a.letzteaenderung = :letzteaenderung")})
public class Availability implements Serializable {
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
  @JoinColumn(name = "ZEITID", referencedColumnName = "ZEITID")
  @ManyToOne(optional = false)
  private Timeslot zeitid;
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

  public void setVerfuegbar(Integer verfuegbar) {
    this.verfuegbar = verfuegbar;
  }

  public Date getLetzteaenderung() {
    return letzteaenderung;
  }

  public void setLetzteaenderung(Date letzteaenderung) {
    this.letzteaenderung = letzteaenderung;
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
    return "testDb.Availability[ verfuegid=" + verfuegid + " ]";
  }
  
}
