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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
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
 * @author Harald <Harald at free-creations.de>
 */
@Entity
@Table(name = "PERSON")
@Access(AccessType.FIELD)
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Personen.findAll", query = "SELECT p FROM Personen p"),
  @NamedQuery(name = "Personen.findByPersonid", query = "SELECT p FROM Personen p WHERE p.personid = :personid"),
  @NamedQuery(name = "Personen.findByFamilienname", query = "SELECT p FROM Personen p WHERE p.familienname = :familienname"),
  @NamedQuery(name = "Personen.findByVorname", query = "SELECT p FROM Personen p WHERE p.vorname = :vorname"),
  @NamedQuery(name = "Personen.findByHerrfrau", query = "SELECT p FROM Personen p WHERE p.herrfrau = :herrfrau"),
  @NamedQuery(name = "Personen.findByPlz", query = "SELECT p FROM Personen p WHERE p.plz = :plz"),
  @NamedQuery(name = "Personen.findByOrt", query = "SELECT p FROM Personen p WHERE p.ort = :ort"),
  @NamedQuery(name = "Personen.findByStrasse", query = "SELECT p FROM Personen p WHERE p.strasse = :strasse"),
  @NamedQuery(name = "Personen.findByTelnr", query = "SELECT p FROM Personen p WHERE p.telnr = :telnr"),
  @NamedQuery(name = "Personen.findByHandy", query = "SELECT p FROM Personen p WHERE p.handy = :handy"),
  @NamedQuery(name = "Personen.findByEmail", query = "SELECT p FROM Personen p WHERE p.email = :email"),
  @NamedQuery(name = "Personen.findByAltersgruppe", query = "SELECT p FROM Personen p WHERE p.altersgruppe = :altersgruppe"),
  @NamedQuery(name = "Personen.findByNotiz", query = "SELECT p FROM Personen p WHERE p.notiz = :notiz"),
  @NamedQuery(name = "Personen.findByGewuenschtewertung", query = "SELECT p FROM Personen p WHERE p.gewuenschtewertung = :gewuenschtewertung"),
  @NamedQuery(name = "Personen.findByLetzteaenderung", query = "SELECT p FROM Personen p WHERE p.letzteaenderung = :letzteaenderung")})
public class Personen implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "PERSONID")
  private Integer personid;
  @Column(name = "FAMILIENNAME")
  private String familienname;
  public static final String PROP_FAMILIENNAME = "FAMILIENNAME";
  @Column(name = "VORNAME")
  private String vorname;
  public static final String PROP_VORNAME = "VORNAME";
  //
  @Column(name = "HERRFRAU")
  private String herrfrau;
  public static final String PROP_HERRFRAU = "HERRFRAU";
  //
  @Column(name = "PLZ")
  private String plz;
  public static final String PROP_PLZ = "PLZ";
  //
  @Column(name = "ORT")
  private String ort;
  public static final String PROP_ORT = "ORT";
  //
  @Column(name = "STRASSE")
  private String strasse;
  public static final String PROP_STRASSE = "STRASSE";
  //
  @Column(name = "TELNR")
  private String telnr;
  public static final String PROP_TELNR = "TELNR";
  //
  @Column(name = "HANDY")
  private String handy;
  public static final String PROP_HANDY = "HANDY";
  //
  @Column(name = "EMAIL")
  private String email;
  public static final String PROP_EMAIL = "EMAIL";
  //
  @Column(name = "ALTERSGRUPPE")
  private String altersgruppe;
  public static final String PROP_ALTERSGRUPPE = "ALTERSGRUPPE";
  //
  @Column(name = "NOTIZ")
  private String notiz;
  public static final String PROP_NOTIZ = "NOTIZ";
  //
  @Column(name = "GEWUENSCHTEWERTUNG")
  private String gewuenschtewertung;
  public static final String PROP_GEWUENSCHTEWERTUNG = "GEWUENSCHTEWERTUNG";
  //
  @Column(name = "LETZTEAENDERUNG")
  @Temporal(TemporalType.TIMESTAMP)
  private Date letzteaenderung;
  public static final String PROP_LETZTEAENDERUNG = "LETZTEAENDERUNG";
  //
  @OneToMany(mappedBy = "gewuenschterkollege")
  private List<Personen> personenList;
  @JoinColumn(name = "GEWUENSCHTERKOLLEGE", referencedColumnName = "PERSONID")
  @ManyToOne
  private Personen gewuenschterkollege;
  public static final String PROP_FAVOREDGROUPLEADER = "FAVOREDGROUPLEADER";
  public static final String PROP_REMOVE_GROUPMEMBER = "REMOVE_GROUPMEMBER";
  public static final String PROP_ADD_GROUPMEMBER = "PROP_ADD_GROUPMEMBER";
  //
  @JoinColumn(name = "GEWUENSCHTEFUNKTION", referencedColumnName = "FUNKTIONID")
  @ManyToOne
  private Funktionen gewuenschtefunktion;
  public static final String PROP_GEWUENSCHTEFUNKTION = "GEWUENSCHTEFUNKTION";
  //
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "personid")
  private List<Allocation> teameinteilungList;
  //
  @OneToMany(mappedBy = "verantwortlich")
  private List<Contest> juryList;
  //
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "personid")
  private List<Availability> verfuegbarkeitList;
  public static final String PROP_VERFUEGBARKEIT = "VERFUEGBARKEIT";
  public static final String PROP_ADD_TEAMEINTEILUNG = "addTeameinteilung";
  public static final String PROP_REMOVE_TEAMEINTEILUNG = "removeTeameinteilung";
  public static final String PROP_REMOVE_JURY = "removeJury";
  public static final String PROP_ADD_JURY = "addJury";

  public Personen() {
  }

  /**
   * A constructor that permits to setup a Personen object with some fields
   * initialized as the entity manager would do. This constructor is used
   * exclusively in tests.
   *
   * @param personid
   */
  public Personen(Integer personid) {
    this.personid = personid;
    this.personenList = new ArrayList<>();
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
    String old = this.familienname;
    this.familienname = familienname;
    if (!Objects.equals(old, familienname)) {
      firePropertyChange(PROP_FAMILIENNAME, old, familienname);
    }
  }

  public String getVorname() {
    return vorname;
  }

  public void setVorname(String vorname) {
    String old = this.vorname;
    this.vorname = vorname;
    if (!Objects.equals(old, vorname)) {
      firePropertyChange(PROP_VORNAME, old, vorname);
    }
  }

  public String getHerrfrau() {
    return herrfrau;
  }

  public void setHerrfrau(String herrfrau) {
    String old = this.herrfrau;
    this.herrfrau = herrfrau;
    if (!Objects.equals(old, herrfrau)) {
      firePropertyChange(PROP_HERRFRAU, old, herrfrau);
    }
  }

  public String getPlz() {
    return plz;
  }

  public void setPlz(String plz) {
    String old = this.plz;
    this.plz = plz;
    if (!Objects.equals(old, plz)) {
      firePropertyChange(PROP_PLZ, old, plz);
    }
  }

  public String getOrt() {
    return ort;
  }

  public void setOrt(String ort) {
    String old = this.ort;
    this.ort = ort;
    if (!Objects.equals(old, ort)) {
      firePropertyChange(PROP_ORT, old, ort);
    }
  }

  public String getStrasse() {
    return strasse;
  }

  public void setStrasse(String strasse) {
    String old = this.strasse;
    this.strasse = strasse;
    if (!Objects.equals(old, strasse)) {
      firePropertyChange(PROP_STRASSE, old, strasse);
    }
  }

  public String getTelnr() {
    return telnr;
  }

  public void setTelnr(String telnr) {
    String old = this.telnr;
    this.telnr = telnr;
    if (!Objects.equals(old, telnr)) {
      firePropertyChange(PROP_TELNR, old, telnr);
    }
  }

  public String getHandy() {
    return handy;
  }

  public void setHandy(String handy) {
    String old = this.handy;
    this.handy = handy;
    if (!Objects.equals(old, handy)) {
      firePropertyChange(PROP_HANDY, old, handy);
    }
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    String old = this.email;
    this.email = email;
    if (!Objects.equals(old, email)) {
      firePropertyChange(PROP_EMAIL, old, email);
    }
  }

  public String getAltersgruppe() {
    return altersgruppe;
  }

  public void setAltersgruppe(String altersgruppe) {
    String old = this.altersgruppe;
    this.altersgruppe = altersgruppe;
    if (!Objects.equals(old, altersgruppe)) {
      firePropertyChange(PROP_ALTERSGRUPPE, old, altersgruppe);
    }
  }

  public String getNotiz() {
    return notiz;
  }

  public void setNotiz(String notiz) {
    String old = this.notiz;
    this.notiz = notiz;
    if (!Objects.equals(old, notiz)) {
      firePropertyChange(PROP_NOTIZ, old, notiz);
    }
  }

  public String getGewuenschtewertung() {
    return gewuenschtewertung;
  }

  public void setGewuenschtewertung(String gewuenschtewertung) {
    String old = this.gewuenschtewertung;
    this.gewuenschtewertung = gewuenschtewertung;
    if (!Objects.equals(old, gewuenschtewertung)) {
      firePropertyChange(PROP_GEWUENSCHTEWERTUNG, old, gewuenschtewertung);
    }
  }

  public Date getLetzteaenderung() {
    return letzteaenderung;
  }

  public void setLetzteaenderung(Date letzteaenderung) {
    this.letzteaenderung = letzteaenderung;
  }

  /**
   * Get the list of all persons who favor this person as group-leader
   * (=Gewuenschterkollege).
   *
   * @return the list of all persons who favor this person as group-leader.
   */
  @XmlTransient
  public List<Personen> getGroupList() {
    return personenList;
  }

  private void setPersonenList(List<Personen> personenList) {
    this.personenList = personenList;
  }

  public Personen getGewuenschterkollege() {
    return gewuenschterkollege;
  }

  public void setGewuenschterkollege(Personen p) {
    Personen old = this.gewuenschterkollege;
    this.gewuenschterkollege = p;

    EntityIdentity newId = (p == null) ? null : p.identity();
    EntityIdentity oldId = (old == null) ? null : old.identity();

    if (!Objects.equals(oldId, newId)) {
      firePropertyChange(PROP_FAVOREDGROUPLEADER, oldId, newId);
      if (old != null) {
        old.removeGroupmember(this);
      }
      if (p != null) {
        p.addGroupmember(this);
      }
    }

  }

  public Funktionen getGewuenschtefunktion() {
    return gewuenschtefunktion;
  }

  public void setGewuenschtefunktion(Funktionen gewuenschtefunktion) {
    Funktionen old = this.gewuenschtefunktion;
    this.gewuenschtefunktion = gewuenschtefunktion;

    EntityIdentity newId = (gewuenschtefunktion == null) ? null : gewuenschtefunktion.identity();
    EntityIdentity oldId = (old == null) ? null : old.identity();

    if (!Objects.equals(oldId, newId)) {
      firePropertyChange(PROP_GEWUENSCHTEFUNKTION, oldId, newId);
      if (old != null) {
        old.removePreferingPerson(this);
      }
      if (gewuenschtefunktion != null) {
        gewuenschtefunktion.addPreferingPerson(this);
      }
    }
  }

  @XmlTransient
  public List<Allocation> getTeameinteilungList() {
    return teameinteilungList;
  }

  public void setTeameinteilungList(List<Allocation> teameinteilungList) {
    this.teameinteilungList = teameinteilungList;
  }

  void removeTeameinteilung(Allocation t) {
    if (teameinteilungList == null) {
      throw new RuntimeException("Cannot remove Allocation from Personen. Record must be persited");
    }
    if (!teameinteilungList.contains(t)) {
      return;
    }
    teameinteilungList.remove(t);
    assert (t != null);
    firePropertyChange(PROP_REMOVE_TEAMEINTEILUNG, t.identity(), null);
  }

  void addTeameinteilung(Allocation t) {
    assert (t != null);
    if (teameinteilungList == null) {
      throw new RuntimeException("Cannot add Allocation. Record must be persited");
    }
    if (teameinteilungList.contains(t)) {
      return;
    }
    if (t.getPersonid() != this) {
      throw new RuntimeException("Cannot add Allocation used for other person.");
    }
    teameinteilungList.add(t);
    firePropertyChange(PROP_ADD_TEAMEINTEILUNG, null, t.identity());
  }

  @XmlTransient
  public List<Contest> getJuryList() {
    return juryList;
  }

  void removeJuryResponsability(Contest j) {
    if (juryList == null) {
      throw new RuntimeException("Cannot remove Contest from Personen. Record must be persited");
    }
    if (!juryList.contains(j)) {
      return;
    }
    juryList.remove(j);
    assert (j != null);
    firePropertyChange(PROP_REMOVE_JURY, j.identity(), null);
  }

  void addJuryResponsability(Contest j) {
    assert (j != null);
    if (juryList == null) {
      throw new RuntimeException("Cannot add Contest to Personen. Record must be persited");
    }
    if (juryList.contains(j)) {
      return;
    }
    if (j.getVerantwortlich() != this) {
      throw new RuntimeException("Cannot add Contest used for other person.");
    }
    juryList.add(j);
    firePropertyChange(PROP_ADD_JURY, null, j.identity());
  }

  public void setJuryList(List<Contest> juryList) {
    this.juryList = juryList;
  }

  /**
   *
   * @return true if this person is available for at least one time-slot.
   */
  public boolean isAvailable() {
    List<Availability> vv = getVerfuegbarkeitList();
    if (vv == null) {
      return false;
    }
    for (Availability v : vv) {
      if (v.isVerfuegbar()) {
        return true;
      }
    }
    return false;
  }

  @XmlTransient
  public List<Availability> getVerfuegbarkeitList() {
    return verfuegbarkeitList;
  }

  private void setVerfuegbarkeitList(List<Availability> verfuegbarkeitList) {
    this.verfuegbarkeitList = verfuegbarkeitList;
  }

  /**
   * Adds a Availability to this person.
   *
   * This method is protected, use {@link Availability#setPersonid() }
   *
   * @param v a new Availability record. It is assumed that this record is not
 assigned to an other person.
   */
  protected void addVerfuegbarkeit(Availability v) {
    assert (v != null);
    if (verfuegbarkeitList == null) {
      throw new RuntimeException("Cannot add Verfügbarkeit. Record must be persited");
    }
    if (verfuegbarkeitList.contains(v)) {
      return;
    }
    if (v.getPersonid() != this) {
      throw new RuntimeException("Cannot add Verfügbarkeit used for other person.");
    }
    verfuegbarkeitList.add(v);
    firePropertyChange(PROP_VERFUEGBARKEIT, null, v.identity());
  }

  protected void removeVerfuegbarkeit(Availability v) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    if (!(object instanceof Personen)) {
      return false;
    }
    Personen other = (Personen) object;
    if ((this.personid == null && other.personid != null) || (this.personid != null && !this.personid.equals(other.personid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    if (getFamilienname() != null) {
      return String.format("%s, %s", getFamilienname(), getVorname());
    } else {
      return String.format("Person[%s]", getPersonid());
    }
  }

  /**
   * Add a PropertyChangeListener for the entity represented by this instance.
   *
   * @param listener
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener(listener, this.personid);
  }

  /**
   * Add a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param personid
   */
  public static void addPropertyChangeListener(PropertyChangeListener listener, Integer personid) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Personen.class, personid));
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * instance.
   *
   * @param listener
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.personid);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param personid
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, Integer personid) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Personen.class, personid));

  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            new EntityIdentity(getClass(), personid),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Personen.class, personid);
  }

  private void removeGroupmember(Personen p) {
    assert (p != null);
    if (personenList == null) {
      throw new RuntimeException("Cannot remove Person from Group-list. Record must be persited");
    }
    if (!personenList.contains(p)) {
      return;
    }
    personenList.remove(p);

    firePropertyChange(PROP_REMOVE_GROUPMEMBER, p.identity(), null);
  }

  private void addGroupmember(Personen p) {
    assert (p != null);
    if (personenList == null) {
      throw new RuntimeException("Cannot add Person to Group-list. Record must be persited");
    }
    if (personenList.contains(p)) {
      return;
    }
    if (p.getGewuenschterkollege() != this) {
      throw new RuntimeException("Cannot add Person to Group-list favoring an other group-list.");
    }
    personenList.add(p);
    firePropertyChange(PROP_ADD_GROUPMEMBER, null, p.identity());
  }
}
