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
@Table(name = "CONTEST")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Contest.findAll", query = "SELECT c FROM Contest c"),
  @NamedQuery(name = "Contest.findByContestid", query = "SELECT c FROM Contest c WHERE c.contestid = :contestid"),
  @NamedQuery(name = "Contest.findByName", query = "SELECT c FROM Contest c WHERE c.name = :name"),
  @NamedQuery(name = "Contest.findByWertungstyp", query = "SELECT c FROM Contest c WHERE c.wertungstyp = :wertungstyp"),
  @NamedQuery(name = "Contest.findByWertung", query = "SELECT c FROM Contest c WHERE c.wertung = :wertung"),
  @NamedQuery(name = "Contest.findByWertungsraum", query = "SELECT c FROM Contest c WHERE c.wertungsraum = :wertungsraum"),
  @NamedQuery(name = "Contest.findByAustraggungsortplannr", query = "SELECT c FROM Contest c WHERE c.austraggungsortplannr = :austraggungsortplannr"),
  @NamedQuery(name = "Contest.findByAustraggungsort", query = "SELECT c FROM Contest c WHERE c.austraggungsort = :austraggungsort"),
  @NamedQuery(name = "Contest.findByZeitfreitag", query = "SELECT c FROM Contest c WHERE c.zeitfreitag = :zeitfreitag"),
  @NamedQuery(name = "Contest.findByZeitsamstag", query = "SELECT c FROM Contest c WHERE c.zeitsamstag = :zeitsamstag"),
  @NamedQuery(name = "Contest.findByZeitsonntag", query = "SELECT c FROM Contest c WHERE c.zeitsonntag = :zeitsonntag")})
public class Contest implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "CONTESTID")
  private Integer contestid;
  @Column(name = "NAME")
  private String name;
  @Column(name = "WERTUNGSTYP")
  private String wertungstyp;
  @Column(name = "WERTUNG")
  private String wertung;
  @Column(name = "WERTUNGSRAUM")
  private String wertungsraum;
  @Column(name = "AUSTRAGGUNGSORTPLANNR")
  private String austraggungsortplannr;
  @Column(name = "AUSTRAGGUNGSORT")
  private String austraggungsort;
  @Column(name = "ZEITFREITAG")
  private String zeitfreitag;
  @Column(name = "ZEITSAMSTAG")
  private String zeitsamstag;
  @Column(name = "ZEITSONNTAG")
  private String zeitsonntag;
  @JoinColumn(name = "VERANTWORTLICH", referencedColumnName = "PERSONID")
  @ManyToOne
  private Person verantwortlich;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "contestid")
  private Collection<Allocation> allocationCollection;

  public Contest() {
  }

  public Contest(Integer contestid) {
    this.contestid = contestid;
  }

  public Integer getContestid() {
    return contestid;
  }

  public void setContestid(Integer contestid) {
    this.contestid = contestid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getWertungstyp() {
    return wertungstyp;
  }

  public void setWertungstyp(String wertungstyp) {
    this.wertungstyp = wertungstyp;
  }

  public String getWertung() {
    return wertung;
  }

  public void setWertung(String wertung) {
    this.wertung = wertung;
  }

  public String getWertungsraum() {
    return wertungsraum;
  }

  public void setWertungsraum(String wertungsraum) {
    this.wertungsraum = wertungsraum;
  }

  public String getAustraggungsortplannr() {
    return austraggungsortplannr;
  }

  public void setAustraggungsortplannr(String austraggungsortplannr) {
    this.austraggungsortplannr = austraggungsortplannr;
  }

  public String getAustraggungsort() {
    return austraggungsort;
  }

  public void setAustraggungsort(String austraggungsort) {
    this.austraggungsort = austraggungsort;
  }

  public String getZeitfreitag() {
    return zeitfreitag;
  }

  public void setZeitfreitag(String zeitfreitag) {
    this.zeitfreitag = zeitfreitag;
  }

  public String getZeitsamstag() {
    return zeitsamstag;
  }

  public void setZeitsamstag(String zeitsamstag) {
    this.zeitsamstag = zeitsamstag;
  }

  public String getZeitsonntag() {
    return zeitsonntag;
  }

  public void setZeitsonntag(String zeitsonntag) {
    this.zeitsonntag = zeitsonntag;
  }

  public Person getVerantwortlich() {
    return verantwortlich;
  }

  public void setVerantwortlich(Person verantwortlich) {
    this.verantwortlich = verantwortlich;
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
    hash += (contestid != null ? contestid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Contest)) {
      return false;
    }
    Contest other = (Contest) object;
    if ((this.contestid == null && other.contestid != null) || (this.contestid != null && !this.contestid.equals(other.contestid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Contest[ contestid=" + contestid + " ]";
  }
  
}
