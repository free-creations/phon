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
--  T a b l e  JOBTYPE
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
DELETE FROM "APP"."JOBTYPE";
INSERT INTO "APP"."JOBTYPE" VALUES('LEHRER', 'Lehrkraft','teacher.png');
INSERT INTO "APP"."JOBTYPE" VALUES('HELFER', 'Helfer','helper.png');

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
--  T a b l e  JOB
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
DELETE FROM "APP"."JOB";
INSERT INTO "APP"."JOB" VALUES('LEHRER', 'LEHRER', 1, 'Lehrkraft');
INSERT INTO "APP"."JOB" VALUES('HELFER1','HELFER', 2, 'Helfer 1');
INSERT INTO "APP"."JOB" VALUES('HELFER2','HELFER', 3, 'Helfer 2');
INSERT INTO "APP"."JOB" VALUES('HELFER3','HELFER', 4, 'Helfer 3');

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
--  T a b l e  CONTESTTYPE
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
DELETE FROM "APP"."CONTESTTYPE";
INSERT INTO "APP"."CONTESTTYPE" VALUES('KLAVIER', 'Klavier', 'piano.png');
INSERT INTO "APP"."CONTESTTYPE" VALUES('GESANG', 'Gesang', 'sing.png');
INSERT INTO "APP"."CONTESTTYPE" VALUES('HARFE', 'Harfe', 'harp.png');
INSERT INTO "APP"."CONTESTTYPE" VALUES('ENSEMBLE', 'Ensemble', 'ensemble.png');
INSERT INTO "APP"."CONTESTTYPE" VALUES('POP', 'Popularmusik', 'pop.png');
INSERT INTO "APP"."CONTESTTYPE" VALUES('NEUEMUSIK', 'Neue Musik', 'newmusic.png');

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
--  T a b l e  TIMESLOT
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
DELETE FROM "APP"."TIMESLOT";
INSERT INTO "APP"."TIMESLOT" VALUES(1,	1,	1,'2014-04-02','08:00','11:00','Mittwoch','Mi_Vo','Vormittag');
INSERT INTO "APP"."TIMESLOT" VALUES(2,	1,	2,'2014-04-02','11:00','15:00','Mittwoch','Mi_Mi','Mittag');
INSERT INTO "APP"."TIMESLOT" VALUES(3,	1,	3,'2014-04-02','15:00','18:00','Mittwoch','Mi_Na','Nachmittag');

INSERT INTO "APP"."TIMESLOT" VALUES(4,	2,	1,'2014-04-03','08:00','11:00','Donnerstag','Do_Vo','Vormittag');
INSERT INTO "APP"."TIMESLOT" VALUES(5,	2,	2,'2014-04-03','11:00','15:00','Donnerstag','Do_Mi','Mittag');
INSERT INTO "APP"."TIMESLOT" VALUES(6,	2,	3,'2014-04-03','15:00','18:00','Donnerstag','Do_Na','Nachmittag');

INSERT INTO "APP"."TIMESLOT" VALUES(7,	3,	1,'2014-04-04','08:00','11:00','Freitag','Fr_Vo','Vormittag');
INSERT INTO "APP"."TIMESLOT" VALUES(8,	3,	2,'2014-04-04','11:00','15:00','Freitag','Fr_Mi','Mittag');
INSERT INTO "APP"."TIMESLOT" VALUES(9,	3,	3,'2014-04-04','15:00','18:00','Freitag','Fr_Na','Nachmittag');

INSERT INTO "APP"."TIMESLOT" VALUES(10,	4,	1,'2014-04-05','08:00','11:00','Samstag','Sa_Vo','Vormittag');
INSERT INTO "APP"."TIMESLOT" VALUES(11,	4,	2,'2014-04-05','11:00','15:00','Samstag','Sa_Mi','Mittag');
INSERT INTO "APP"."TIMESLOT" VALUES(12,	4,	3,'2014-04-05','15:00','18:00','Samstag','Sa_Na','Nachmittag');

INSERT INTO "APP"."TIMESLOT" VALUES(13,	5,	1,'2014-04-06','08:00','11:00','Sonntag','So_Vo','Vormittag');
INSERT INTO "APP"."TIMESLOT" VALUES(14,	5,	2,'2014-04-06','11:00','15:00','Sonntag','So_Mi','Mittag');
INSERT INTO "APP"."TIMESLOT" VALUES(15,	5,	3,'2014-04-06','15:00','18:00','Sonntag','So_Na','Nachmittag');


