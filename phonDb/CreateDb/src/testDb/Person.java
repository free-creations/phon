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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "PERSON")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
  @NamedQuery(name = "Person.findByPersonid", query = "SELECT p FROM Person p WHERE p.personid = :personid"),
  @NamedQuery(name = "Person.findByFamilienname", query = "SELECT p FROM Person p WHERE p.familienname = :familienname"),
  @NamedQuery(name = "Person.findByVorname", query = "SELECT p FROM Person p WHERE p.vorname = :vorname"),
  @NamedQuery(name = "Person.findByHerrfrau", query = "SELECT p FROM Person p WHERE p.herrfrau = :herrfrau"),
  @NamedQuery(name = "Person.findByPlz", query = "SELECT p FROM Person p WHERE p.plz = :plz"),
  @NamedQuery(name = "Person.findByOrt", query = "SELECT p FROM Person p WHERE p.ort = :ort"),
  @NamedQuery(name = "Person.findByStrasse", query = "SELECT p FROM Person p WHERE p.strasse = :strasse"),
  @NamedQuery(name = "Person.findByTelnr", query = "SELECT p FROM Person p WHERE p.telnr = :telnr"),
  @NamedQuery(name = "Person.findByHandy", query = "SELECT p FROM Person p WHERE p.handy = :handy"),
  @NamedQuery(name = "Person.findByEmail", query = "SELECT p FROM Person p WHERE p.email = :email"),
  @NamedQuery(name = "Person.findByAltersgruppe", query = "SELECT p FROM Person p WHERE p.altersgruppe = :altersgruppe"),
  @NamedQuery(name = "Person.findByNotiz", query = "SELECT p FROM Person p WHERE p.notiz = :notiz"),
  @NamedQuery(name = "Person.findByGewuenschtewertung", query = "SELECT p FROM Person p WHERE p.gewuenschtewertung = :gewuenschtewertung"),
  @NamedQuery(name = "Person.findByGewuenschterkollege", query = "SELECT p FROM Person p WHERE p.gewuenschterkollege = :gewuenschterkollege"),
  @NamedQuery(name = "Person.findByLetzteaenderung", query = "SELECT p FROM Person p WHERE p.letzteaenderung = :letzteaenderung")})
public class Person implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "PERSONID")
  private Integer personid;
  @Column(name = "FAMILIENNAME")
  private String familienname;
  @Column(name = "VORNAME")
  private String vorname;
  @Column(name = "HERRFRAU")
  private String herrfrau;
  @Column(name = "PLZ")
  private String plz;
  @Column(name = "ORT")
  private String ort;
  @Column(name = "STRASSE")
  private String strasse;
  @Column(name = "TELNR")
  private String telnr;
  @Column(name = "HANDY")
  private String handy;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "ALTERSGRUPPE")
  private String altersgruppe;
  @Column(name = "NOTIZ")
  private String notiz;
  @Column(name = "GEWUENSCHTEWERTUNG")
  private String gewuenschtewertung;
  @Column(name = "GEWUENSCHTERKOLLEGE")
  private Integer gewuenschterkollege;
  @Column(name = "LETZTEAENDERUNG")
  @Temporal(TemporalType.TIMESTAMP)
  private Date letzteaenderung;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "personid")
  private Collection<Availability> availabilityCollection;
  @OneToMany(mappedBy = "verantwortlich")
  private Collection<Contest> contestCollection;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "personid")
  private Collection<Allocation> allocationCollection;
  @JoinColumn(name = "GEWUENSCHTEFUNKTION", referencedColumnName = "FUNKTIONID")
  @ManyToOne
  private Job gewuenschtefunktion;

  public Person() {
  }

  public Person(Integer personid) {
    this.personid = personid;
  }

  public Integer getPersonid() {
    return personid;
  }

  public void setPersonid(Integer personid) {
    this.personid = personid;
  }

  public String getFamilienname() {
    return familienname;
  }

  public void setFamilienname(String familienname) {
    this.familienname = familienname;
  }

  public String getVorname() {
    return vorname;
  }

  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  public String getHerrfrau() {
    return herrfrau;
  }

  public void setHerrfrau(String herrfrau) {
    this.herrfrau = herrfrau;
  }

  public String getPlz() {
    return plz;
  }

  public void setPlz(String plz) {
    this.plz = plz;
  }

  public String getOrt() {
    return ort;
  }

  public void setOrt(String ort) {
    this.ort = ort;
  }

  public String getStrasse() {
    return strasse;
  }

  public void setStrasse(String strasse) {
    this.strasse = strasse;
  }

  public String getTelnr() {
    return telnr;
  }

  public void setTelnr(String telnr) {
    this.telnr = telnr;
  }

  public String getHandy() {
    return handy;
  }

  public void setHandy(String handy) {
    this.handy = handy;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAltersgruppe() {
    return altersgruppe;
  }

  public void setAltersgruppe(String altersgruppe) {
    this.altersgruppe = altersgruppe;
  }

  public String getNotiz() {
    return notiz;
  }

  public void setNotiz(String notiz) {
    this.notiz = notiz;
  }

  public String getGewuenschtewertung() {
    return gewuenschtewertung;
  }

  public void setGewuenschtewertung(String gewuenschtewertung) {
    this.gewuenschtewertung = gewuenschtewertung;
  }

  public Integer getGewuenschterkollege() {
    return gewuenschterkollege;
  }

  public void setGewuenschterkollege(Integer gewuenschterkollege) {
    this.gewuenschterkollege = gewuenschterkollege;
  }

  public Date getLetzteaenderung() {
    return letzteaenderung;
  }

  public void setLetzteaenderung(Date letzteaenderung) {
    this.letzteaenderung = letzteaenderung;
  }

  @XmlTransient
  public Collection<Availability> getAvailabilityCollection() {
    return availabilityCollection;
  }

  public void setAvailabilityCollection(Collection<Availability> availabilityCollection) {
    this.availabilityCollection = availabilityCollection;
  }

  @XmlTransient
  public Collection<Contest> getContestCollection() {
    return contestCollection;
  }

  public void setContestCollection(Collection<Contest> contestCollection) {
    this.contestCollection = contestCollection;
  }

  @XmlTransient
  public Collection<Allocation> getAllocationCollection() {
    return allocationCollection;
  }

  public void setAllocationCollection(Collection<Allocation> allocationCollection) {
    this.allocationCollection = allocationCollection;
  }

  public Job getGewuenschtefunktion() {
    return gewuenschtefunktion;
  }

  public void setGewuenschtefunktion(Job gewuenschtefunktion) {
    this.gewuenschtefunktion = gewuenschtefunktion;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (personid != null ? personid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Person)) {
      return false;
    }
    Person other = (Person) object;
    if ((this.personid == null && other.personid != null) || (this.personid != null && !this.personid.equals(other.personid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Person[ personid=" + personid + " ]";
  }
  
}
