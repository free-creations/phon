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
--                      T a b l e s
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- Table ALLOCATION
-- Each record of this table allocates a given person for a time slot 
-- to a task (identified by the function).
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."ALLOCATION" (
  "PERSONID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
  "ZEITID" INTEGER NOT NULL, 
  "JURYID" VARCHAR(50) NOT NULL, 
  "FUNKTIONID" VARCHAR(50) NOT NULL, 
  "PERSONID" INTEGER NOT NULL, 
  "LETZTEAENDERUNG" TIMESTAMP, 
  "PLANER" VARCHAR(50)
);

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
-- Table FUNCTION
-- Describes in details the task of a person.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."FUNCTION" (
  "FUNKTIONID" VARCHAR(50) NOT NULL, 
  "FUNKTIONNAME" VARCHAR(50), 
  "SORTVALUE" INTEGER
);

-- -----------------------------------------------------------------------------
-- Table CONTEST
-- Describes in details of the task of a person.
-- -----------------------------------------------------------------------------
CREATE TABLE "APP"."CONTEST" (
  "JURYID" VARCHAR(50) NOT NULL, 
  "WERTUNGSTYP" VARCHAR(50)
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

CREATE TABLE "APP"."VERFUEGBARKEIT" ("VERFUEGID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "PERSONID" INTEGER NOT NULL, "ZEITID" INTEGER NOT NULL, "VERFUEGBAR" INTEGER, "LETZTEAENDERUNG" TIMESTAMP);

-- ----------------------------------------------
-- DDL-Anweisungen f�r Schl�ssel
-- ----------------------------------------------

-- prim�r/eindeutig
ALTER TABLE "APP"."PERSONEN" ADD CONSTRAINT "PERSONID" PRIMARY KEY ("PERSONID");

ALTER TABLE "APP"."FUNKTIONEN" ADD CONSTRAINT "SQL090310125909320" PRIMARY KEY ("FUNKTIONID");

ALTER TABLE "APP"."ZEIT" ADD CONSTRAINT "SQL090310125909820" PRIMARY KEY ("ZEITID");

ALTER TABLE "APP"."VERFUEGBARKEIT" ADD CONSTRAINT "VERFUEGID" PRIMARY KEY ("VERFUEGID");

ALTER TABLE "APP"."TEAMEINTEILUNG" ADD CONSTRAINT "TEAMEINTEILUNG_PK" PRIMARY KEY ("ZEITID", "JURYID", "FUNKTIONID");

ALTER TABLE "APP"."JURY" ADD CONSTRAINT "SQL090310125909460" PRIMARY KEY ("JURYID");

-- fremd
ALTER TABLE "APP"."PERSONEN" ADD CONSTRAINT "PERSONEN_FK1" FOREIGN KEY ("GEWUENSCHTEFUNKTION") REFERENCES "APP"."FUNKTIONEN" ("FUNKTIONID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "APP"."PERSONEN" ADD CONSTRAINT "PERSONEN_FK2" FOREIGN KEY ("GEWUENSCHTERKOLLEGE") REFERENCES "APP"."PERSONEN" ("PERSONID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "APP"."VERFUEGBARKEIT" ADD CONSTRAINT "VERFUEGBARKEIT_FK1" FOREIGN KEY ("PERSONID") REFERENCES "APP"."PERSONEN" ("PERSONID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "APP"."VERFUEGBARKEIT" ADD CONSTRAINT "VERFUEGBARKEIT_FK2" FOREIGN KEY ("ZEITID") REFERENCES "APP"."ZEIT" ("ZEITID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "APP"."TEAMEINTEILUNG" ADD CONSTRAINT "TEAMEINTEILUNG_FK1" FOREIGN KEY ("ZEITID") REFERENCES "APP"."ZEIT" ("ZEITID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "APP"."TEAMEINTEILUNG" ADD CONSTRAINT "TEAMEINTEILUNG_FK2" FOREIGN KEY ("PERSONID") REFERENCES "APP"."PERSONEN" ("PERSONID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "APP"."TEAMEINTEILUNG" ADD CONSTRAINT "TEAMEINTEILUNG_FK3" FOREIGN KEY ("JURYID") REFERENCES "APP"."JURY" ("JURYID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "APP"."TEAMEINTEILUNG" ADD CONSTRAINT "TEAMEINTEILUNG_FK4" FOREIGN KEY ("FUNKTIONID") REFERENCES "APP"."FUNKTIONEN" ("FUNKTIONID") ON DELETE NO ACTION ON UPDATE NO ACTION;

