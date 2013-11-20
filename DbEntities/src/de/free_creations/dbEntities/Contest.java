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
import java.util.List;
import java.util.Objects;
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
 * @author Harald <Harald at free-creations.de>
 */
@Entity
@Table(name = "CONTEST")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Contest.findAll", query = "SELECT j FROM Contest j"),
  @NamedQuery(name = "Contest.findByJuryid", query = "SELECT j FROM Contest j WHERE j.contestid = :juryid"),
  @NamedQuery(name = "Contest.findByWertungstyp", query = "SELECT j FROM Contest j WHERE j.wertungstyp = :wertungstyp"),
  @NamedQuery(name = "Contest.findByWertung", query = "SELECT j FROM Contest j WHERE j.wertung = :wertung"),
  @NamedQuery(name = "Contest.findByWertungsraum", query = "SELECT j FROM Contest j WHERE j.wertungsraum = :wertungsraum"),
  @NamedQuery(name = "Contest.findByAustraggungsortplannr", query = "SELECT j FROM Contest j WHERE j.austraggungsortplannr = :austraggungsortplannr"),
  @NamedQuery(name = "Contest.findByAustraggungsort", query = "SELECT j FROM Contest j WHERE j.austraggungsort = :austraggungsort"),
  @NamedQuery(name = "Contest.findByZeitfreitag", query = "SELECT j FROM Contest j WHERE j.zeitfreitag = :zeitfreitag"),
  @NamedQuery(name = "Contest.findByZeitsamstag", query = "SELECT j FROM Contest j WHERE j.zeitsamstag = :zeitsamstag"),
  @NamedQuery(name = "Contest.findByZeitsonntag", query = "SELECT j FROM Contest j WHERE j.zeitsonntag = :zeitsonntag")})
public class Contest implements Serializable, DbEntity {

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
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "contestid")
  private List<Teameinteilung> teameinteilungList;
  @JoinColumn(name = "VERANTWORTLICH", referencedColumnName = "PERSONID")
  @ManyToOne
  private Personen verantwortlich;
  public static final String PROP_WERUNGSTYP = "wertungstyp";
  public static final String PROP_WERUNG = "wertung";
  public static final String PROP_WERUNGSRAUM = "wertungsraum";
  public static final String PROP_AUSTRAGUNGSORTPLANNR = "austraggungsortplannr";
  public static final String PROP_AUSTRAGUNGSORT = "austraggungsort";
  public static final String PROP_VERATWORTLICH = "verantwortlich";
  public static final String PROP_ADD_TEAMEINTEILUNG = "addTeameinteilung";
  public static final String PROP_REMOVE_TEAMEINTEILUNG = "removeTeameinteilung";

  public Contest() {
  }

  public Contest(int contestid) {
    this.contestid = contestid;
  }

  public int getJuryid() {
    return contestid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

//  public void setJuryid(String juryid) {
//    this.juryid = juryid;
//  }
  public String getWertungstyp() {
    return wertungstyp;
  }

  public void setWertungstyp(String wertungstyp) {
    String old = this.wertungstyp;
    this.wertungstyp = wertungstyp;
    if (!Objects.equals(old, wertungstyp)) {
      firePropertyChange(PROP_WERUNGSTYP, old, wertungstyp);
    }
  }

  public String getWertung() {
    return wertung;
  }

  public void setWertung(String wertung) {
    String old = this.wertung;
    this.wertung = wertung;
    if (!Objects.equals(old, wertung)) {
      firePropertyChange(PROP_WERUNG, old, wertung);
    }
  }

  public String getWertungsraum() {
    return wertungsraum;
  }

  public void setWertungsraum(String wertungsraum) {
    String old = this.wertungsraum;
    this.wertungsraum = wertungsraum;
    if (!Objects.equals(old, wertungsraum)) {
      firePropertyChange(PROP_WERUNGSRAUM, old, wertungsraum);
    }
  }

  public String getAustraggungsortplannr() {
    return austraggungsortplannr;

  }

  public void setAustraggungsortplannr(String austraggungsortplannr) {
    String old = this.austraggungsortplannr;
    this.austraggungsortplannr = austraggungsortplannr;
    if (!Objects.equals(old, austraggungsortplannr)) {
      firePropertyChange(PROP_AUSTRAGUNGSORTPLANNR, old, austraggungsortplannr);
    }
  }

  public String getAustraggungsort() {
    return austraggungsort;
  }

  public void setAustraggungsort(String austraggungsort) {
    String old = this.austraggungsort;
    this.austraggungsort = austraggungsort;
    if (!Objects.equals(old, austraggungsort)) {
      firePropertyChange(PROP_AUSTRAGUNGSORT, old, austraggungsort);
    }
  }

  public String getZeitfreitag() {
    return zeitfreitag;
  }

  public void setZeitfreitag(String zeitfreitag) {
    /**
     * @ToDo redesign
     */
    this.zeitfreitag = zeitfreitag;
  }

  public String getZeitsamstag() {
    /**
     * @ToDo redesign
     */
    return zeitsamstag;
  }

  public void setZeitsamstag(String zeitsamstag) {
    /**
     * @ToDo redesign
     */
    this.zeitsamstag = zeitsamstag;
  }

  public String getZeitsonntag() {
    /**
     * @ToDo redesign
     */
    return zeitsonntag;
  }

  public void setZeitsonntag(String zeitsonntag) {
    /**
     * @ToDo redesign
     */
    this.zeitsonntag = zeitsonntag;
  }

  @XmlTransient
  public List<Teameinteilung> getTeameinteilungList() {
    return teameinteilungList;
  }

  protected void addTeameinteilung(Teameinteilung t) {
    assert (t != null);
    if (teameinteilungList == null) {
      throw new RuntimeException("Cannot add a Teameinteilung to this Contest. Record must be persited");
    }
    if (teameinteilungList.contains(t)) {
      return;
    }
    if (t.getJury() != this) {
      throw new RuntimeException("Cannot add Teameinteilung for an other jury.");
    }
    teameinteilungList.add(t);
    firePropertyChange(PROP_ADD_TEAMEINTEILUNG, null, t.identity());
  }

  protected void removeTeameinteilung(Teameinteilung t) {
    if (teameinteilungList == null) {
      throw new RuntimeException("Cannot remove Teameinteilung from Contest. Record must be persited");
    }
    if (!teameinteilungList.contains(t)) {
      return;
    }
    teameinteilungList.remove(t);
    assert (t != null);
    firePropertyChange(PROP_REMOVE_TEAMEINTEILUNG, t.identity(), null);
  }

  protected void setTeameinteilungList(List<Teameinteilung> teameinteilungList) {
    this.teameinteilungList = teameinteilungList;
  }

  public Personen getVerantwortlich() {
    return verantwortlich;
  }

  public void setVerantwortlich(Personen p) {
    Personen old = this.verantwortlich;
    this.verantwortlich = p;

    EntityIdentity newId = (p == null) ? null : p.identity();
    EntityIdentity oldId = (old == null) ? null : old.identity();

    if (!Objects.equals(oldId, newId)) {
      firePropertyChange(PROP_VERATWORTLICH, oldId, newId);
      if (old != null) {
        old.removeJuryResponsability(this);
      }
      if (p != null) {
        p.addJuryResponsability(this);
      }
    }
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
    return "Contest[ juryid=" + contestid + " ]";
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener(listener, this.contestid);
  }

  public static void addPropertyChangeListener(PropertyChangeListener listener, Integer juryid) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Contest.class, juryid));
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.contestid);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param juryid
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, Integer juryid) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Contest.class, juryid));
  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Contest.class, contestid);
  }
}
