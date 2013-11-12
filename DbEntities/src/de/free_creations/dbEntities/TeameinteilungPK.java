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

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Harald <Harald at free-creations.de>
 */
@Embeddable
public class TeameinteilungPK implements Serializable {
  @Basic(optional = false)
  @Column(name = "ZEITID")
  private int zeitid;
  @Basic(optional = false)
  @Column(name = "JURYID")
  private String juryid;
  @Basic(optional = false)
  @Column(name = "FUNKTIONID")
  private String funktionid;

  public TeameinteilungPK() {
  }

  public TeameinteilungPK(int zeitid, String juryid, String funktionid) {
    this.zeitid = zeitid;
    this.juryid = juryid;
    this.funktionid = funktionid;
  }

  public int getZeitid() {
    return zeitid;
  }

  public void setZeitid(int zeitid) {
    this.zeitid = zeitid;
  }

  public String getJuryid() {
    return juryid;
  }

  public void setJuryid(String juryid) {
    this.juryid = juryid;
  }

  public String getFunktionid() {
    return funktionid;
  }

  public void setFunktionid(String funktionid) {
    this.funktionid = funktionid;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (int) zeitid;
    hash += (juryid != null ? juryid.hashCode() : 0);
    hash += (funktionid != null ? funktionid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof TeameinteilungPK)) {
      return false;
    }
    TeameinteilungPK other = (TeameinteilungPK) object;
    if (this.zeitid != other.zeitid) {
      return false;
    }
    if ((this.juryid == null && other.juryid != null) || (this.juryid != null && !this.juryid.equals(other.juryid))) {
      return false;
    }
    if ((this.funktionid == null && other.funktionid != null) || (this.funktionid != null && !this.funktionid.equals(other.funktionid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "[ zeitid=" + zeitid + ", juryid=" + juryid + ", funktionid=" + funktionid + " ]";
  }
  
}
