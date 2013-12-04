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
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
--                      D r o p (o l d)    T a b l e s
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------

ALTER TABLE "APP"."AVAILABILITY" DROP CONSTRAINT "AVAILABILITY_FK1";
ALTER TABLE "APP"."AVAILABILITY" DROP CONSTRAINT "AVAILABILITY_FK2";
ALTER TABLE "APP"."AVAILABILITY" DROP CONSTRAINT "AVAILABILITY_UNIQUE";
ALTER TABLE "APP"."PERSON" DROP CONSTRAINT PERSON_FK1;
ALTER TABLE "APP"."PERSON" DROP CONSTRAINT PERSON_FK2;
ALTER TABLE "APP"."PERSON" DROP CONSTRAINT PERSON_FK3;
ALTER TABLE "APP"."CONTEST" DROP CONSTRAINT CONTEST_FK1;
ALTER TABLE "APP"."CONTEST" DROP CONSTRAINT CONTEST_FK2;
ALTER TABLE "APP"."ALLOCATION" DROP CONSTRAINT ALLOCATION_FK1;
ALTER TABLE "APP"."ALLOCATION" DROP CONSTRAINT ALLOCATION_FK2;
ALTER TABLE "APP"."ALLOCATION" DROP CONSTRAINT ALLOCATION_FK3;
ALTER TABLE "APP"."ALLOCATION" DROP CONSTRAINT ALLOCATION_UNIQUE;
ALTER TABLE "APP"."JOB" DROP CONSTRAINT JOB_FK1;
ALTER TABLE "APP"."EVENT" DROP CONSTRAINT EVENT_FK1;
ALTER TABLE "APP"."EVENT" DROP CONSTRAINT EVENT_FK2;
ALTER TABLE "APP"."EVENT" DROP CONSTRAINT EVENT_FK3;
ALTER TABLE "APP"."EVENT" DROP CONSTRAINT EVENT_UNIQUE;

DROP TABLE "APP"."PERSON";
DROP TABLE "APP"."CONTEST";
DROP TABLE "APP"."AVAILABILITY";
DROP TABLE "APP"."ALLOCATION";
DROP TABLE "APP"."JOB";
DROP TABLE "APP"."TIMESLOT";
DROP TABLE "APP"."TEAM";
DROP TABLE "APP"."JOBTYPE";
DROP TABLE "APP"."CONTESTTYPE";
DROP TABLE "APP"."LOCATION";
DROP TABLE "APP"."EVENT";

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
--                      C r e a t e    T a b l e s
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- Table TIMESLOT
-- Describes the basic calendar.
-- Each record describes an elementary block of time.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."TIMESLOT" (
  "TIMESLOTID" INTEGER NOT NULL, 
  "DAYIDX" INTEGER, -- index for the day (starting by 1) e.g. (1= first day of the events, 2= second day etc.)
  "TIMEOFDAYIDX" INTEGER, -- index for the time of the day (starting by 1) e.g. (1= morning, 2=noon, 3=evening)
  "DATUM" DATE, -- the date of this timeslot e.g. 2014-04-02
  "STARTTIME" TIME, -- start of this time-slot 09:30
  "ENDTIME" TIME, -- end of this time-slot 12:00
  "DAYOFWEEK" VARCHAR(50), -- day of the week e.g. "Mittwoch" ("Wednesday")
  "LABEL" VARCHAR(50), -- a short label for printout e.g. Mi_Vo
  "TIMEOFDAYPRINT" VARCHAR(50) -- printout for the time-of-day e.g. "Vormittag" ("Morning")
);

-- -----------------------------------------------------------------------------
-- Table PERSON
-- Describes all persons by their addresses and their preferences.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."PERSON" (
  "PERSONID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  "SURNAME" VARCHAR(50), -- e.g. "Seiler"
  "GIVENNAME" VARCHAR(50), -- e.g "Brigitte"
  "GENDER" VARCHAR(50), -- male = "Hr." , female = "Fr."
  "ZIPCODE" VARCHAR(50), -- e.g. "81829"
  "CITY" VARCHAR(50), -- e.g. "München"
  "STREET" VARCHAR(50), -- e.g. "Billstedt"
  "TELEPHONE" VARCHAR(50), -- e.g. "089 93 37 07"
  "MOBILE" VARCHAR(50), -- e.g. "0172 23 45 178"
  "EMAIL" VARCHAR(50), -- e.g. "BrigitteSeiler@dayrep.com"
  "AGEGROUP" VARCHAR(50), -- "KIND", "JUGENDLICH", "ERWACSEN"
  "NOTICE" VARCHAR(255), -- 
  "TEAM" INTEGER, -- the team in which this person wants to be integrated
  "JOBTYPE" VARCHAR(50), -- the type of job this person wants to take over
  "CONTESTTYPE" VARCHAR(50), -- the type of contest this person wants to be allocated
  "LASTCHANGE" TIMESTAMP
);
-- -----------------------------------------------------------------------------
-- Table CONTEST
-- Describes the details of the contests.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."CONTEST" (
  "CONTESTID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  "CONTESTTYPE" VARCHAR(50), -- the type of contest
  "NAME" VARCHAR(50), -- a name chosen by the user (preferably not longer than 8 chars)
  "DESCRIPTION" VARCHAR(50), -- a description
  "PERSON" INTEGER -- the person responsible for the organization of this contest
);

-- -----------------------------------------------------------------------------
-- Table AVAILABILITY
-- Each record describes the availability of a given person for a specific
-- time slot. When a new person record is created, for each timeslot a corresponding
-- availability record mmust be inserted.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."AVAILABILITY" (
  "AVAILABILITYID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  "PERSON" INTEGER NOT NULL, -- foreign key into PERSON table
  "TIMESLOT" INTEGER NOT NULL, -- foreign key into TIMESLOT table
  "AVAILABLE" INTEGER, -- 1= person is available for this time-slot, 0= person is not available.
  "LASTCHANGE" TIMESTAMP
);
-- -----------------------------------------------------------------------------
-- Table ALLOCATION
-- Each record of this table allocates a given person for a time slot 
-- to a task (identified by the function).
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."ALLOCATION" (
  "ALLOCATIONID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  "PERSON" INTEGER NOT NULL,  -- who is allocated, foreign key into PERSON table
  "EVENT" INTEGER NOT NULL, -- to which event is she/he allocated, foreign key into EVENT table
  "JOB" VARCHAR(50), -- to which task is she/he allocated, , foreign key into JOB table
  "LASTCHANGE" TIMESTAMP, -- when was this record last changed
  "PLANNER" VARCHAR(50), -- who did change this record the last time
  "NOTE" VARCHAR(32000) -- some further notes
);

-- -----------------------------------------------------------------------------
-- Table CONTESTTYPE
-- Describes in general a contest.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."CONTESTTYPE" (
  "CONTESTTYPEID" VARCHAR(50) NOT NULL, 
  "NAME" VARCHAR(50), -- name of this type of contest
  "ICON" VARCHAR(50) -- an icon shown on the userinterface e.g. "piano.png"

);
-- -----------------------------------------------------------------------------
-- Table JOBTYPE
-- Describes in general the task of a person.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."JOBTYPE" (
  "JOBTYPEID" VARCHAR(50) NOT NULL, 
  "NAME" VARCHAR(50), -- name of this type of contest
  "ICON" VARCHAR(50) -- an icon shown on the userinterface e.g. "teacher.png"

);
-- -----------------------------------------------------------------------------
-- Table JOB
-- Describes in detail the task of a person.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."JOB" (
  "JOBID" VARCHAR(50) NOT NULL, -- primary key
  "JOBTYPE" VARCHAR(50) NOT NULL, -- the type of job, foreign key into JOBTYPE table
  "NAME" VARCHAR(50) -- the name shown on printouts
);
-- -----------------------------------------------------------------------------
-- Table T E A M
-- The participants might be joined into teams.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."TEAM" (
  "TEAMID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  "NAME" VARCHAR(50) -- the name shown on printouts
);

-- -----------------------------------------------------------------------------
-- Table LOCATION
-- The participants might be joined into teams.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."LOCATION" (
  "LOCATIONID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  "NAME" VARCHAR(50), -- a short mnemonic name chosen by the user
  "BUILDING" VARCHAR(50), -- name of the building e.g. Karlskaserne
  "ROOM" VARCHAR(50), -- room appellation e.g. Kleine Buehne
  "STREET" VARCHAR(50), -- Streetname + number e.g. Hindenburgstrasse 29
  "TOWN" VARCHAR(50), -- Postal code + town e.g. 71638 Ludwigsburg
  "GRIDNUMBER" VARCHAR(50) -- coordinates on map e.g. A4
);

-- -----------------------------------------------------------------------------
-- Table EVENT
-- Describes in detail the events of a contest
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."EVENT" (
  "EVENTID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  "CONTEST" INTEGER NOT NULL, -- the contest of which this event is part of, foreign key into CONTEST table
  "TIMESLOT" INTEGER NOT NULL, -- when does this event take place, foreign key into TIMESLOT table
  "LOCATION" INTEGER, -- where does this event take place, foreign key into LOCATION table
  "SCHEDULED" INTEGER DEFAULT 1 -- if zero, the event has been dropped
);
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
--                      P r i m a r y    K e y s
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
ALTER TABLE "APP"."JOB" 
  ADD CONSTRAINT "JobPrimKey" PRIMARY KEY ("JOBID");
ALTER TABLE "APP"."CONTEST" 
  ADD CONSTRAINT "ContestPrimKey" PRIMARY KEY ("CONTESTID");
ALTER TABLE "APP"."ALLOCATION" 
  ADD CONSTRAINT "AllocationPrimKey" PRIMARY KEY ("ALLOCATIONID");
ALTER TABLE "APP"."TIMESLOT" 
  ADD CONSTRAINT "TimeslotPrimKey" PRIMARY KEY ("TIMESLOTID");
ALTER TABLE "APP"."AVAILABILITY" 
  ADD CONSTRAINT "AvailabilityPrimKey" PRIMARY KEY ("AVAILABILITYID");
ALTER TABLE "APP"."PERSON" 
  ADD CONSTRAINT "PersonPrimKey" PRIMARY KEY ("PERSONID");
ALTER TABLE "APP"."TEAM" 
  ADD CONSTRAINT "TeamPrimKey" PRIMARY KEY ("TEAMID");
ALTER TABLE "APP"."JOBTYPE" 
  ADD CONSTRAINT "JobtypePrimKey" PRIMARY KEY ("JOBTYPEID");
ALTER TABLE "APP"."CONTESTTYPE" 
  ADD CONSTRAINT "ContestTypePrimKey" PRIMARY KEY ("CONTESTTYPEID");
ALTER TABLE "APP"."LOCATION" 
  ADD CONSTRAINT "LocationPrimKey" PRIMARY KEY ("LOCATIONID");
ALTER TABLE "APP"."EVENT" 
  ADD CONSTRAINT "EventPrimKey" PRIMARY KEY ("EVENTID");

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
--                      F o r e i g n    K e y s
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------

-- Table CONTEST
ALTER TABLE "APP"."CONTEST" 
  ADD CONSTRAINT "CONTEST_FK1" FOREIGN KEY ("CONTESTTYPE") 
  REFERENCES "APP"."CONTESTTYPE" ("CONTESTTYPEID") 
  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "APP"."CONTEST" 
  ADD CONSTRAINT "CONTEST_FK2" FOREIGN KEY ("PERSON") 
  REFERENCES "APP"."PERSON" ("PERSONID") 
  ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Table ALLOCATION
ALTER TABLE "APP"."ALLOCATION" 
  ADD CONSTRAINT "ALLOCATION_FK1" FOREIGN KEY ("PERSON") 
  REFERENCES "APP"."PERSON" ("PERSONID") 
  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "APP"."ALLOCATION" 
  ADD CONSTRAINT "ALLOCATION_FK2" FOREIGN KEY ("JOB") 
  REFERENCES "APP"."JOB" ("JOBID") 
  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "APP"."ALLOCATION" 
  ADD CONSTRAINT "ALLOCATION_FK3" FOREIGN KEY ("EVENT") 
  REFERENCES "APP"."EVENT" ("EVENTID") 
  ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "APP"."ALLOCATION" 
  ADD CONSTRAINT "ALLOCATION_UNIQUE" UNIQUE ("PERSON","EVENT" ); 

-- Table PERSON
ALTER TABLE "APP"."PERSON" 
  ADD CONSTRAINT "PERSON_FK1" FOREIGN KEY ("JOBTYPE") 
  REFERENCES "APP"."JOBTYPE" ("JOBTYPEID") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "APP"."PERSON" 
  ADD CONSTRAINT "PERSON_FK2" FOREIGN KEY ("TEAM") 
  REFERENCES "APP"."TEAM" ("TEAMID") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "APP"."PERSON" 
  ADD CONSTRAINT "PERSON_FK3" FOREIGN KEY ("CONTESTTYPE") 
  REFERENCES "APP"."CONTESTTYPE" ("CONTESTTYPEID") ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Table AVAILABILITY
ALTER TABLE "APP"."AVAILABILITY"  
  ADD CONSTRAINT "AVAILABILITY_FK1" FOREIGN KEY ("PERSON") 
  REFERENCES "APP"."PERSON" ("PERSONID") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "APP"."AVAILABILITY"  
  ADD CONSTRAINT "AVAILABILITY_FK2" FOREIGN KEY ("TIMESLOT") 
  REFERENCES "APP"."TIMESLOT" ("TIMESLOTID") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "APP"."AVAILABILITY" 
  ADD CONSTRAINT "AVAILABILITY_UNIQUE" UNIQUE ("PERSON","TIMESLOT" ); 

-- Table JOB
ALTER TABLE "APP"."JOB" 
  ADD CONSTRAINT "JOB_FK1" FOREIGN KEY ("JOBTYPE") 
  REFERENCES "APP"."JOBTYPE" ("JOBTYPEID") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Table EVENT
ALTER TABLE "APP"."EVENT" 
  ADD CONSTRAINT "EVENT_FK1" FOREIGN KEY ("CONTEST") 
  REFERENCES "APP"."CONTEST" ("CONTESTID") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "APP"."EVENT" 
  ADD CONSTRAINT "EVENT_FK2" FOREIGN KEY ("LOCATION") 
  REFERENCES "APP"."LOCATION" ("LOCATIONID") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "APP"."EVENT" 
  ADD CONSTRAINT "EVENT_FK3" FOREIGN KEY ("TIMESLOT") 
  REFERENCES "APP"."TIMESLOT" ("TIMESLOTID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "APP"."EVENT" 
  ADD CONSTRAINT "EVENT_UNIQUE" UNIQUE ("CONTEST","TIMESLOT" ); 