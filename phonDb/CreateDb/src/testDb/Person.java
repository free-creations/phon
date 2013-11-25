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
  @NamedQuery(name = "Person.findBySurname", query = "SELECT p FROM Person p WHERE p.surname = :surname"),
  @NamedQuery(name = "Person.findByGivenname", query = "SELECT p FROM Person p WHERE p.givenname = :givenname"),
  @NamedQuery(name = "Person.findByGender", query = "SELECT p FROM Person p WHERE p.gender = :gender"),
  @NamedQuery(name = "Person.findByZipcode", query = "SELECT p FROM Person p WHERE p.zipcode = :zipcode"),
  @NamedQuery(name = "Person.findByCity", query = "SELECT p FROM Person p WHERE p.city = :city"),
  @NamedQuery(name = "Person.findByStreet", query = "SELECT p FROM Person p WHERE p.street = :street"),
  @NamedQuery(name = "Person.findByTelephone", query = "SELECT p FROM Person p WHERE p.telephone = :telephone"),
  @NamedQuery(name = "Person.findByMobile", query = "SELECT p FROM Person p WHERE p.mobile = :mobile"),
  @NamedQuery(name = "Person.findByEmail", query = "SELECT p FROM Person p WHERE p.email = :email"),
  @NamedQuery(name = "Person.findByAgegroup", query = "SELECT p FROM Person p WHERE p.agegroup = :agegroup"),
  @NamedQuery(name = "Person.findByNotice", query = "SELECT p FROM Person p WHERE p.notice = :notice"),
  @NamedQuery(name = "Person.findByLastchange", query = "SELECT p FROM Person p WHERE p.lastchange = :lastchange")})
public class Person implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "PERSONID")
  private Integer personid;
  @Column(name = "SURNAME")
  private String surname;
  @Column(name = "GIVENNAME")
  private String givenname;
  @Column(name = "GENDER")
  private String gender;
  @Column(name = "ZIPCODE")
  private String zipcode;
  @Column(name = "CITY")
  private String city;
  @Column(name = "STREET")
  private String street;
  @Column(name = "TELEPHONE")
  private String telephone;
  @Column(name = "MOBILE")
  private String mobile;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "AGEGROUP")
  private String agegroup;
  @Column(name = "NOTICE")
  private String notice;
  @Column(name = "LASTCHANGE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastchange;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
  private List<Availability> availabilityList;
  @OneToMany(mappedBy = "person")
  private List<Contest> contestList;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
  private List<Allocation> allocationList;
  @JoinColumn(name = "TEAM", referencedColumnName = "TEAMID")
  @ManyToOne
  private Team team;
  @JoinColumn(name = "JOBTYPE", referencedColumnName = "JOBTYPEID")
  @ManyToOne
  private Jobtype jobtype;
  @JoinColumn(name = "CONTESTTYPE", referencedColumnName = "CONTESTTYPEID")
  @ManyToOne
  private Contesttype contesttype;

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

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getGivenname() {
    return givenname;
  }

  public void setGivenname(String givenname) {
    this.givenname = givenname;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAgegroup() {
    return agegroup;
  }

  public void setAgegroup(String agegroup) {
    this.agegroup = agegroup;
  }

  public String getNotice() {
    return notice;
  }

  public void setNotice(String notice) {
    this.notice = notice;
  }

  public Date getLastchange() {
    return lastchange;
  }

  public void setLastchange(Date lastchange) {
    this.lastchange = lastchange;
  }

  @XmlTransient
  public List<Availability> getAvailabilityList() {
    return availabilityList;
  }

  public void setAvailabilityList(List<Availability> availabilityList) {
    this.availabilityList = availabilityList;
  }

  @XmlTransient
  public List<Contest> getContestList() {
    return contestList;
  }

  public void setContestList(List<Contest> contestList) {
    this.contestList = contestList;
  }

  @XmlTransient
  public List<Allocation> getAllocationList() {
    return allocationList;
  }

  public void setAllocationList(List<Allocation> allocationList) {
    this.allocationList = allocationList;
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public Jobtype getJobtype() {
    return jobtype;
  }

  public void setJobtype(Jobtype jobtype) {
    this.jobtype = jobtype;
  }

  public Contesttype getContesttype() {
    return contesttype;
  }

  public void setContesttype(Contesttype contesttype) {
    this.contesttype = contesttype;
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
