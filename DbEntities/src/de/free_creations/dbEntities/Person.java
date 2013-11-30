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

import static de.free_creations.dbEntities.Location.PROP_STREET;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
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
import javax.persistence.Transient;
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
  @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
  @NamedQuery(name = "Person.findByPersonId", query = "SELECT p FROM Person p WHERE p.personId = :personId"),
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
public class Person implements Serializable, DbEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "PERSONID")
  private Integer personId;
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
  private JobType jobType;
  @JoinColumn(name = "CONTESTTYPE", referencedColumnName = "CONTESTTYPEID")
  @ManyToOne
  private ContestType contestType;
  public static final String PROP_SURNAME = "PROP_SURNAME";
  public static final String PROP_GENDER = "PROP_GENDER";
  public static final String PROP_ZIPCODE = "PROP_ZIPCODE";
  public static final String PROP_CITY = "PROP_CITY";
  public static final String PROP_TELEPHONE = "PROP_TELEPHONE";
  public static final String PROP_MOBILE = "PROP_MOBILE";
  public static final String PROP_EMAIL = "PROP_EMAIL";
  public static final String PROP_AGEGROUP = "PROP_AGEGROUP";
  public static final String PROP_NOTICE = "PROP_NOTICE";
  public static final String PROP_LASTCHANGE = "PROP_LASTCHANGE";
  public static final String PROP_CONTESTREMOVED = "PROP_CONTESTREMOVED";
  public static final String PROP_CONTESTADDED = "PROP_CONTESTADDED";
  public static final String PROP_AVAILABILITYREMOVED = "PROP_AVAILABILITYREMOVED";
  public static final String PROP_AVAILABILITYADDED = "PROP_AVAILABILITYADDED";
  public static final String PROP_ALLOCATIONREMOVED = "PROP_ALLOCATIONREMOVED";
  public static final String PROP_ALLOCATIONADDED = "PROP_ALLOCATIONADDED";
  public static final String PROP_TEAM = "PROP_TEAM";
  public static final String PROP_GIVENNAME = "PROP_GIVENNAME";
  public static final String PROP_JOBTYPE = "PROP_JOBTYPE";
  public static final String PROP_CONTESTTYPE = "PROP_CONTESTTYPE";
  public static final String PROP_AVAILABILITY = "PROP_AVAILABILITY";

  public Person() {
  }

  public Person(Integer personId) {
    this.personId = personId;
  }

  public Integer getPersonId() {
    return personId;
  }

  public String getSurname() {
    return surname;

  }

  public void setSurname(String value) {
    String old = this.surname;
    this.surname = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_SURNAME, old, value);
    }
  }

  public String getGivenname() {
    return givenname;
  }

  public void setGivenname(String value) {
    String old = this.givenname;
    this.givenname = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_GIVENNAME, old, value);
    }
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String value) {
    String old = this.gender;
    this.gender = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_GENDER, old, value);
    }
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String value) {
    String old = this.zipcode;
    this.zipcode = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_ZIPCODE, old, value);
    }
  }

  public String getCity() {
    return city;
  }

  public void setCity(String value) {
    String old = this.city;
    this.city = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_CITY, old, value);
    }
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String value) {
    String old = this.street;
    this.street = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_STREET, old, value);
    }
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String value) {
    String old = this.telephone;
    this.telephone = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_TELEPHONE, old, value);
    }
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String value) {
    String old = this.mobile;
    this.mobile = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_MOBILE, old, value);
    }
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String value) {
    String old = this.email;
    this.email = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_EMAIL, old, value);
    }
  }

  public String getAgegroup() {
    return agegroup;
  }

  public void setAgegroup(String value) {
    String old = this.agegroup;
    this.agegroup = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_AGEGROUP, old, value);
    }
  }

  public String getNotice() {
    return notice;
  }

  public void setNotice(String value) {
    String old = this.notice;
    this.notice = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_NOTICE, old, value);
    }
  }

  public Date getLastchange() {
    return lastchange;
  }

  public void setLastchange(Date value) {
    Date old = this.lastchange;
    this.lastchange = value;
    if (!Objects.equals(old, value)) {
      firePropertyChange(PROP_LASTCHANGE, old, value);
    }
  }

  @XmlTransient
  public List<Availability> getAvailabilityList() {
    return availabilityList;
  }

  public void setAvailabilityList(List<Availability> availabilityList) {
    this.availabilityList = availabilityList;
  }

  /**
   * Get the list of contests for which this person is responsible.
   *
   * @return
   */
  @XmlTransient
  public List<Contest> getContestList() {
    return contestList;
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

  public void setTeam(Team newValue) {
    Team old = this.team;
    this.team = newValue;
    if (!Objects.equals(old, newValue)) {
      if (old != null) {
        old.removePerson(this);
      }
      if (newValue != null) {
        newValue.addPerson(this);
      }
      EntityIdentity newId = (newValue == null) ? null : newValue.identity();
      EntityIdentity oldId = (old == null) ? null : old.identity();
      firePropertyChange(PROP_TEAM, oldId, newId);
    }
  }

  public JobType getJobType() {
    return jobType;
  }

  public void setJobType(JobType newValue) {
    JobType old = this.jobType;
    this.jobType = newValue;
    if (!Objects.equals(old, newValue)) {
      if (old != null) {
        old.removePerson(this);
      }
      if (newValue != null) {
        newValue.addPerson(this);
      }
      EntityIdentity newId = (newValue == null) ? null : newValue.identity();
      EntityIdentity oldId = (old == null) ? null : old.identity();
      firePropertyChange(PROP_JOBTYPE, oldId, newId);
    }
  }

  public ContestType getContestType() {
    return contestType;
  }

  public void setContestType(ContestType newValue) {
    ContestType old = this.contestType;
    this.contestType = newValue;
    if (!Objects.equals(old, newValue)) {
      if (old != null) {
        old.removePerson(this);
      }
      if (newValue != null) {
        newValue.addPerson(this);
      }
      EntityIdentity newId = (newValue == null) ? null : newValue.identity();
      EntityIdentity oldId = (old == null) ? null : old.identity();
      firePropertyChange(PROP_CONTESTTYPE, oldId, newId);
    }
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (personId != null ? personId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Person)) {
      return false;
    }
    Person other = (Person) object;
    if ((this.personId == null && other.personId != null) || (this.personId != null && !this.personId.equals(other.personId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "testDb.Person[ personId=" + personId + " ]";
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    addPropertyChangeListener(listener, this.personId);
  }

  public static void addPropertyChangeListener(PropertyChangeListener listener, Integer personId) {
    PropertyChangeManager.instance().addPropertyChangeListener(listener,
            new EntityIdentity(Person.class, personId));
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    removePropertyChangeListener(listener, this.personId);
  }

  /**
   * Remove a PropertyChangeListener for the entity represented by this
   * <em>Class</em>
   * and the given primary key.
   *
   * @param listener
   * @param personId
   */
  public static void removePropertyChangeListener(PropertyChangeListener listener, Integer personId) {
    PropertyChangeManager.instance().removePropertyChangeListener(listener,
            new EntityIdentity(Person.class, personId));
  }

  private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    PropertyChangeManager.instance().firePropertyChange(
            identity(),
            propertyName, oldValue, newValue);
  }

  @Override
  public EntityIdentity identity() {
    return new EntityIdentity(Person.class, personId);
  }

  /**
   * Remove a contest for which this person is responsible.
   *
   * @param c
   */
  protected void removeContest(Contest c) {
    if (contestList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (!contestList.contains(c)) {
      return;
    }
    contestList.remove(c);
    firePropertyChange(PROP_CONTESTREMOVED, c.identity(), null);
  }

  /**
   * Add a contest for which this person is responsible.
   *
   * @param c
   */
  protected void addContest(Contest c) {
    assert (c != null);
    if (contestList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (contestList.contains(c)) {
      return;
    }
    if (this != c.getPerson()) {
      throw new RuntimeException("Entity missmatch.");
    }
    contestList.add(c);
    firePropertyChange(PROP_CONTESTADDED, null, c.identity());
  }

  protected void removeAvailability(Availability a) {
    if (availabilityList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (!availabilityList.contains(a)) {
      return;
    }
    availabilityList.remove(a);
    firePropertyChange(PROP_AVAILABILITYREMOVED, a.identity(), null);
  }

  protected void addAvailability(Availability a) {
    assert (a != null);
    if (availabilityList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (availabilityList.contains(a)) {
      return;
    }
    if (this != a.getPerson()) {
      throw new RuntimeException("Entity missmatch.");
    }
    availabilityList.add(a);
    firePropertyChange(PROP_AVAILABILITYADDED, null, a.identity());
  }

  public boolean isAvailable() {
    if (availabilityList == null) {
      return false;
    }
    for (Availability a : availabilityList) {
      if (a.isAvailable()) {
        return true;
      }
    }
    return false;
  }

  void removeAllocation(Allocation a) {
    if (allocationList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (!allocationList.contains(a)) {
      return;
    }
    allocationList.remove(a);
    firePropertyChange(PROP_ALLOCATIONREMOVED, a.identity(), null);
  }

  void addAllocation(Allocation a) {
    assert (a != null);
    if (allocationList == null) {
      throw new RuntimeException("Cannot perform this operation. Record must be persited");
    }
    if (allocationList.contains(a)) {
      return;
    }
    if (this != a.getPerson()) {
      throw new RuntimeException("Entity missmatch.");
    }
    allocationList.add(a);
    firePropertyChange(PROP_ALLOCATIONADDED, null, a.identity());
  }
}
