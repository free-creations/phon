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

DROP TABLE "APP"."PERSON";
DROP TABLE "APP"."CONTEST";
DROP TABLE "APP"."AVAILABILITY";
DROP TABLE "APP"."ALLOCATION";
DROP TABLE "APP"."FUNCTION";
DROP TABLE "APP"."TIMESLOT";


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
  "ZEITID" INTEGER NOT NULL, 
  "TAG" INTEGER, 
  "TAGESZEIT" INTEGER, 
  "DATUM" DATE, 
  "STARTZEIT" TIME, 
  "ENDEZEIT" TIME, 
  "WOCHENTAG" VARCHAR(50), 
  "LABEL" VARCHAR(50), 
  "TAGESZEITPRINT" VARCHAR(50)
);

-- -----------------------------------------------------------------------------
-- Table PERSON
-- Describes all persons by their addresses and their preferences.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."PERSON" (
  "PERSONID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
  "FAMILIENNAME" VARCHAR(50), 
  "VORNAME" VARCHAR(50), 
  "HERRFRAU" VARCHAR(50), 
  "PLZ" VARCHAR(50), 
  "ORT" VARCHAR(50), 
  "STRASSE" VARCHAR(50), 
  "TELNR" VARCHAR(50), 
  "HANDY" VARCHAR(50), 
  "EMAIL" VARCHAR(50), 
  "ALTERSGRUPPE" VARCHAR(50), 
  "NOTIZ" VARCHAR(255), 
  "GEWUENSCHTEWERTUNG" VARCHAR(50), 
  "GEWUENSCHTEFUNKTION" VARCHAR(50), 
  "GEWUENSCHTERKOLLEGE" INTEGER, 
  "LETZTEAENDERUNG" TIMESTAMP
);
-- -----------------------------------------------------------------------------
-- Table CONTEST
-- Describes the details of the contests.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."CONTEST" (
  "CONTESTID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
  "WERTUNGSTYP" VARCHAR(50), 
  "VERANTWORTLICH" INTEGER, 
  "WERTUNG" VARCHAR(50), 
  "WERTUNGSRAUM" VARCHAR(50), 
  "AUSTRAGGUNGSORTPLANNR" VARCHAR(50), 
  "AUSTRAGGUNGSORT" VARCHAR(50), 
  "ZEITFREITAG" VARCHAR(50), 
  "ZEITSAMSTAG" VARCHAR(50), 
  "ZEITSONNTAG" VARCHAR(50)
);

-- -----------------------------------------------------------------------------
-- Table AVAILABILITY
-- Each record describes the availability of a given person for a specific
-- time slot. When a new person record is created, for each timeslot a corresponding
-- availability record mmust be inserted.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."AVAILABILITY" (
  "VERFUEGID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
  "PERSONID" INTEGER NOT NULL, 
  "ZEITID" INTEGER NOT NULL, 
  "VERFUEGBAR" INTEGER, 
  "LETZTEAENDERUNG" TIMESTAMP
);
-- -----------------------------------------------------------------------------
-- Table ALLOCATION
-- Each record of this table allocates a given person for a time slot 
-- to a task (identified by the function).
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."ALLOCATION" (
  "ALLOCATIONID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
  "ZEITID" INTEGER NOT NULL, 
  "JURYID" VARCHAR(50) NOT NULL, 
  "FUNKTIONID" VARCHAR(50) NOT NULL, 
  "PERSONID" INTEGER NOT NULL, 
  "LETZTEAENDERUNG" TIMESTAMP, 
  "PLANER" VARCHAR(50), 
  "ERKLAERUNG" VARCHAR(32000)
);

-- -----------------------------------------------------------------------------
-- Table FUNCTION
-- Describes in detail the task of a person.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."FUNCTION" (
  "FUNKTIONID" VARCHAR(50) NOT NULL, 
  "FUNKTIONNAME" VARCHAR(50), 
  "SORTVALUE" INTEGER
);

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
--                      P r i m a r y    K e y s
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
ALTER TABLE "APP"."FUNCTION" 
  ADD CONSTRAINT "FunctionPrimKey" PRIMARY KEY ("FUNKTIONID");
ALTER TABLE "APP"."CONTEST" 
  ADD CONSTRAINT "ContestPrimKey" PRIMARY KEY ("CONTESTID");
ALTER TABLE "APP"."ALLOCATION" 
  ADD CONSTRAINT "AllocationPrimKey" PRIMARY KEY ("ALLOCATIONID");
ALTER TABLE "APP"."TIMESLOT" 
  ADD CONSTRAINT "TimeslotPrimKey" PRIMARY KEY ("ZEITID");
ALTER TABLE "APP"."AVAILABILITY" 
  ADD CONSTRAINT "AvailabilityPrimKey" PRIMARY KEY ("VERFUEGID");
ALTER TABLE "APP"."PERSON" 
  ADD CONSTRAINT "PersonPrimKey" PRIMARY KEY ("PERSONID");

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
--                      F o r e i g n    K e y s
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
