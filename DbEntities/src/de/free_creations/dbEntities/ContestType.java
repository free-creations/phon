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
package de.free_creations.dbEntities;

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
import javax.persistence.Transient;
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
  @NamedQuery(name = "ContestType.findAll", query = "SELECT c FROM ContestType c"),
  @NamedQuery(name = "ContestType.findByContestTypeId", query = "SELECT c FROM ContestType c WHERE c.contestTypeId = :contestTypeId"),
  @NamedQuery(name = "ContestType.findByName", query = "SELECT c FROM ContestType c WHERE c.name = :name"),
  @NamedQuery(name = "ContestType.findByIcon", query = "SELECT c FROM ContestType c WHERE c.icon = :icon")})
public class ContestType implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "CONTESTTYPEID")
  private String contestTypeId;
  @Column(name = "NAME")
  private String name;
  @Column(name = "ICON")
  private String icon;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "contestType")
  private List<Contest> contestList;
  //@OneToMany(mappedBy = "contestType")
  @Transient
  private List<Person> personList;

  public ContestType() {
  }

  public ContestType(String contestTypeId) {
    this.contestTypeId = contestTypeId;
  }

  public String getContestTypeId() {
    return contestTypeId;
  }

  public String getName() {
    return name;
  }

  public String getIcon() {
    return icon;
  }

  @XmlTransient
  public List<Contest> getContestList() {
    return contestList;
  }

  protected void addContest(Contest c) {
    assert (c != null);
    if (contestList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (contestList.contains(c)) {
      return;
    }
    if (this != c.getContestType()) {
      throw new RuntimeException("Entity missmatch.");
    }
    contestList.add(c);
  }

  protected void removeContest(Contest c) {
    if (contestList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (!contestList.contains(c)) {
      return;
    }
    contestList.remove(c);
  }

  /**
   * The list of all persons who would like to be allocated to a contest of this
   * kind.
   *
   * @return
   */
  @XmlTransient
  public List<Person> getPersonList() {
    return personList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (contestTypeId != null ? contestTypeId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof ContestType)) {
      return false;
    }
    ContestType other = (ContestType) object;
    if ((this.contestTypeId == null && other.contestTypeId != null) || (this.contestTypeId != null && !this.contestTypeId.equals(other.contestTypeId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.ContestType[ contestTypeId=" + contestTypeId + " ]";
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(this.getClass(), contestTypeId);
  }
}
