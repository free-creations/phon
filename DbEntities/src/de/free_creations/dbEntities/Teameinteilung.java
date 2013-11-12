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
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "TEAMEINTEILUNG")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Teameinteilung.findAll", query = "SELECT t FROM Teameinteilung t"),
  @NamedQuery(name = "Teameinteilung.findByZeitid", query = "SELECT t FROM Teameinteilung t WHERE t.teameinteilungPK.zeitid = :zeitid"),
  @NamedQuery(name = "Teameinteilung.findByJuryid", query = "SELECT t FROM Teameinteilung t WHERE t.teameinteilungPK.juryid = :juryid"),
  @NamedQuery(name = "Teameinteilung.findByFunktionid", query = "SELECT t FROM Teameinteilung t WHERE t.teameinteilungPK.funktionid = :funktionid"),
  @NamedQuery(name = "Teameinteilung.findByLetzteaenderung", query = "SELECT t FROM Teameinteilung t WHERE t.letzteaenderung = :letzteaenderung"),
  @NamedQuery(name = "Teameinteilung.findByPlaner", query = "SELECT t FROM Teameinteilung t WHERE t.planer = :planer"),
  @NamedQuery(name = "Teameinteilung.findByErklaerung", query = "SELECT t FROM Teameinteilung t WHERE t.erklaerung = :erklaerung")})
public class Teameinteilung implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @EmbeddedId
  protected TeameinteilungPK teameinteilungPK;
  @Column(name = "LETZTEAENDERUNG")
  @Temporal(TemporalType.TIMESTAMP)
  private Date letzteaenderung;
  @Column(name = "PLANER")
  private String planer;
  @Column(name = "ERKLAERUNG")
  private String erklaerung;
  @JoinColumn(name = "ZEITID", referencedColumnName = "ZEITID", insertable = false, updatable = false)
  @ManyToOne(optional = false)
  private Zeit zeit;
  @JoinColumn(name = "PERSONID", referencedColumnName = "PERSONID")
  @ManyToOne(optional = false)
  private Personen personid;
  @JoinColumn(name = "JURYID", referencedColumnName = "JURYID", insertable = false, updatable = false)
  @ManyToOne(optional = false)
  private Jury jury;
  @JoinColumn(name = "FUNKTIONID", referencedColumnName = "FUNKTIONID", insertable = false, updatable = false)
  @ManyToOne(optional = false)
  private Funktionen funktionen;
//  public final static String PROP_FUNCTION = "function";
//  public final static String PROP_JURY = "jury";
//  public final static String PROP_ZEIT = "zeit";
  public final static String PROP_PERSON = "person";
  public final static String PROP_SELF_REMOVED = "selfRemoved";

  public Teameinteilung() {
  }

  private Teameinteilung(TeameinteilungPK teameinteilungPK) {
    setTeameinteilungPK(teameinteilungPK);
  }

  private Teameinteilung(int zeitid, String juryid, String funktionid) {
    this(new TeameinteilungPK(zeitid, juryid, funktionid));
  }

  public TeameinteilungPK getTeameinteilungPK() {
    return teameinteilungPK;
  }

  private void setTeameinteilungPK(TeameinteilungPK teameinteilungPK) {
    this.teameinteilungPK = teameinteilungPK;
  }

  public Teameinteilung(Zeit z, Jury j, Funktionen f) {
    this(z.getZeitid(), j.getJuryid(), f.getFunktionid());
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

  public Zeit getZeit() {
    return zeit;
  }

  private void setZeit(Zeit zeit) {
    Zeit old = this.zeit;
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

  public Personen getPersonid() {
    return personid;
  }

  public void setPersonid(Personen personid) {
    Personen old = this.personid;
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

  public Jury getJury() {
    return jury;
  }

  private void setJury(Jury jury) {
    Jury old = this.jury;
    this.jury = jury;
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

  public Funktionen getFunktionen() {
    return funktionen;
  }

  private void setFunktionen(Funktionen funktionen) {
    Funktionen old = this.funktionen;
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
    hash += (teameinteilungPK != null ? teameinteilungPK.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Teameinteilung)) {
      return false;
    }
    Teameinteilung other = (Teameinteilung) object;
    if ((this.teameinteilungPK == null && other.teameinteilungPK != null) || (this.teameinteilungPK != null && !this.teameinteilungPK.equals(other.teameinteilungPK))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Teameinteilung" + teameinteilungPK;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    assert (this.teameinteilungPK != null);
    addPropertyChangeListener(listener, this.teameinteilungPK);
  }

  /**
   * Add a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param teameinteilungId
   */
  public static void addPropertyChangeListener(PropertyChangeListener listener, TeameinteilungPK teameinteilungId) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Teameinteilung.class, teameinteilungId));
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * instance.
   *
   * @param listener
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.teameinteilungPK);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener the listener to be removed.
   * @param teameinteilungId the primary key
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, TeameinteilungPK teameinteilungId) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Teameinteilung.class, teameinteilungId));

  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Teameinteilung.class, teameinteilungPK);
  }
}
