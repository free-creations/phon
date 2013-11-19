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
--  T a b l e  F U N C T I O N
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
INSERT INTO "APP"."FUNCTION" VALUES('LEHRER', 'Lehrkraft',1);
INSERT INTO "APP"."FUNCTION" VALUES('HELFER1', 'Helfer 1',2);
INSERT INTO "APP"."FUNCTION" VALUES('HELFER2', 'Helfer 2',3);
INSERT INTO "APP"."FUNCTION" VALUES('HELFER3', 'Helfer 3',4);


INSERT INTO "APP"."TIMESLOT" VALUES(1,	1,	1,'2014-04-02','09:00','13:00','Mittwoch','Mi_Vo','Vormittag');
INSERT INTO "APP"."TIMESLOT" VALUES(2,	1,	2,'2014-04-02','13:00','17:00','Mittwoch','Mi_Vo','Nachmittag');
INSERT INTO "APP"."TIMESLOT" VALUES(3,	1,	3,'2014-04-02','17:00','21:00','Mittwoch','Mi_Ab','Abend');

INSERT INTO "APP"."TIMESLOT" VALUES(4,	2,	1,'2014-04-03','09:00','13:00','Donnerstag','Do_Vo','Vormittag');
INSERT INTO "APP"."TIMESLOT" VALUES(5,	2,	2,'2014-04-03','13:00','17:00','Donnerstag','Do_Vo','Nachmittag');
INSERT INTO "APP"."TIMESLOT" VALUES(6,	2,	3,'2014-04-03','17:00','21:00','Donnerstag','Do_Ab','Abend');

INSERT INTO "APP"."TIMESLOT" VALUES(7,	3,	1,'2014-04-04','09:00','13:00','Freitag','Fr_Vo','Vormittag');
INSERT INTO "APP"."TIMESLOT" VALUES(8,	3,	2,'2014-04-04','13:00','17:00','Freitag','Fr_Vo','Nachmittag');
INSERT INTO "APP"."TIMESLOT" VALUES(9,	3,	3,'2014-04-04','17:00','21:00','Freitag','Fr_Ab','Abend');

INSERT INTO "APP"."TIMESLOT" VALUES(10,	4,	1,'2014-04-05','09:00','13:00','Samstag','Sa_Vo','Vormittag');
INSERT INTO "APP"."TIMESLOT" VALUES(11,	4,	2,'2014-04-05','13:00','17:00','Samstag','Sa_Vo','Nachmittag');
INSERT INTO "APP"."TIMESLOT" VALUES(12,	4,	3,'2014-04-05','17:00','21:00','Samstag','Sa_Ab','Abend');

INSERT INTO "APP"."TIMESLOT" VALUES(13,	5,	1,'2014-04-06','09:00','13:00','Sonntag','Fr_Vo','Vormittag');
INSERT INTO "APP"."TIMESLOT" VALUES(14,	5,	2,'2014-04-06','13:00','17:00','Sonntag','Fr_Vo','Nachmittag');
INSERT INTO "APP"."TIMESLOT" VALUES(15,	5,	3,'2014-04-06','17:00','21:00','Sonntag','Fr_Ab','Abend');


