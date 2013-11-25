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
import javax.persistence.Id;
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
@Table(name = "CONTESTTYPE")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Contesttype.findAll", query = "SELECT c FROM Contesttype c"),
  @NamedQuery(name = "Contesttype.findByContesttypeid", query = "SELECT c FROM Contesttype c WHERE c.contesttypeid = :contesttypeid"),
  @NamedQuery(name = "Contesttype.findByName", query = "SELECT c FROM Contesttype c WHERE c.name = :name"),
  @NamedQuery(name = "Contesttype.findByIcon", query = "SELECT c FROM Contesttype c WHERE c.icon = :icon")})
public class Contesttype implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "CONTESTTYPEID")
  private String contesttypeid;
  @Column(name = "NAME")
  private String name;
  @Column(name = "ICON")
  private String icon;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "contesttype")
  private List<Contest> contestList;
  @OneToMany(mappedBy = "contesttype")
  private List<Person> personList;

  public Contesttype() {
  }

  public Contesttype(String contesttypeid) {
    this.contesttypeid = contesttypeid;
  }

  public String getContesttypeid() {
    return contesttypeid;
  }

  public void setContesttypeid(String contesttypeid) {
    this.contesttypeid = contesttypeid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  @XmlTransient
  public List<Contest> getContestList() {
    return contestList;
  }

  public void setContestList(List<Contest> contestList) {
    this.contestList = contestList;
  }

  @XmlTransient
  public List<Person> getPersonList() {
    return personList;
  }

  public void setPersonList(List<Person> personList) {
    this.personList = personList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (contesttypeid != null ? contesttypeid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Contesttype)) {
      return false;
    }
    Contesttype other = (Contesttype) object;
    if ((this.contesttypeid == null && other.contesttypeid != null) || (this.contesttypeid != null && !this.contesttypeid.equals(other.contesttypeid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Contesttype[ contesttypeid=" + contesttypeid + " ]";
  }
  
}
