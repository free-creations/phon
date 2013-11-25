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
import java.util.List;
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
  @NamedQuery(name = "Contest.findByName", query = "SELECT c FROM Contest c WHERE c.name = :name")})
public class Contest implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "CONTESTID")
  private Integer contestid;
  @Column(name = "NAME")
  private String name;
  @JoinColumn(name = "PERSON", referencedColumnName = "PERSONID")
  @ManyToOne
  private Person person;
  @JoinColumn(name = "CONTESTTYPE", referencedColumnName = "CONTESTTYPEID")
  @ManyToOne(optional = false)
  private Contesttype contesttype;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "contest")
  private List<Event> eventList;

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

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public Contesttype getContesttype() {
    return contesttype;
  }

  public void setContesttype(Contesttype contesttype) {
    this.contesttype = contesttype;
  }

  @XmlTransient
  public List<Event> getEventList() {
    return eventList;
  }

  public void setEventList(List<Event> eventList) {
    this.eventList = eventList;
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
