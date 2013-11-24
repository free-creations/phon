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
--  T a b l e  LOCATION
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
DELETE FROM "APP"."LOCATION";
/*
INSERT INTO "APP"."LOCATION" VALUES(1,'NAME','BUILDING','ROOM','STREET','TOWN','GRIDNUMBER');
*/
INSERT INTO "APP"."LOCATION" VALUES(1,'JMS 0.11','Jugendmusikschule','Saal 0.11','Hindenburgstr. 29','71638 Ludwigsburg',NULL);
INSERT INTO "APP"."LOCATION" VALUES(2,'JMS 2.11','Jugendmusikschule','Saal 2.11','Hindenburgstr. 29','71638 Ludwigsburg',NULL);

INSERT INTO "APP"."LOCATION" VALUES(3,'Karlskaserene','Karlskaserene','Reithalle/Kl. Bühne','Hindenburgstr. 29','71638 Ludwigsburg',NULL);

INSERT INTO "APP"."LOCATION" VALUES(4,'Mörike-Gymn.-Aula','Mörike Gymnasium','Aula','Karlstraße 19','71638 Ludwigsburg',NULL);
INSERT INTO "APP"."LOCATION" VALUES(5,'Mörike-Gymn.-Musiksaal','Mörike Gymnasium','Musiksaal 1','Karlstraße 19','71638 Ludwigsburg',NULL);

INSERT INTO "APP"."LOCATION" VALUES(6,'Goethe-Gymn.-Aula','Goethe Gymnasium','Aula','Seestraße 37','71638 Ludwigsburg',NULL);

INSERT INTO "APP"."LOCATION" VALUES(7,'Fr.-Schiller-Gymn.-Mensa','Friedrich-Schiller-Gymnasium','Feuerseemensa','Alleenstraße 16 ','71638 Ludwigsburg',NULL);
INSERT INTO "APP"."LOCATION" VALUES(8,'Fr.-Schiller-Gymn.-Musiksaal','Friedrich-Schiller-Gymnasium','Musiksaal 2','Alleenstraße 16 ','71638 Ludwigsburg',NULL);

INSERT INTO "APP"."LOCATION" VALUES(9,'Ev. Hochschule','Evangelische Hochschule','Raum P5/P6','Paulusweg 6','71638 Ludwigsburg',NULL);

INSERT INTO "APP"."LOCATION" VALUES(10,'E.-H.-Knapp Real.Musiksaal','Elly-Heuss-Knapp Realschule','Musiksaal 2','Karlstr. 33','71638 Ludwigsburg',NULL);
INSERT INTO "APP"."LOCATION" VALUES(11,'E.-H.-Knapp Real.Aufenthaltsraum','Elly-Heuss-Knapp Realschule','Aufenthaltsraum','Karlstr. 33','71638 Ludwigsburg',NULL);

INSERT INTO "APP"."LOCATION" VALUES(12,'Kulturzentrum Kl. Saal','Kulturzentrum','Kleiner Saal','Wilhelmstraße 9/1','71638 Ludwigsburg',NULL);
INSERT INTO "APP"."LOCATION" VALUES(13,'Kulturzentrum Kl. Saal','Kulturzentrum','Raum 303','Wilhelmstraße 9/1','71638 Ludwigsburg',NULL);
INSERT INTO "APP"."LOCATION" VALUES(14,'Kulturzentrum Kl. Saal','Kulturzentrum','Großer Saal','Wilhelmstraße 9/1','71638 Ludwigsburg',NULL);

INSERT INTO "APP"."LOCATION" VALUES(15,'Landratsamt','Landratsamt','Großer Sitzungssaal','Hindenburgstr. 40','71638 Ludwigsburg',NULL);

INSERT INTO "APP"."LOCATION" VALUES(16,'Staatsarchiv','Staatsarchiv','Vortragssaal','Arsenalplatz 3','71638 Ludwigsburg',NULL);

INSERT INTO "APP"."LOCATION" VALUES(17,'Schloss','Schloss','Festinbau, Raum 1','Schlossstraße 30','71634 Ludwigsburg',NULL);

INSERT INTO "APP"."LOCATION" VALUES(18,'ev. GH Stadtmitte','?','Saal','?','? Ludwigsburg',NULL);

INSERT INTO "APP"."LOCATION" VALUES(19,'Spaarkasse','Kreissparkasse Ludwigsburg','Louis-Bührer-Saal','Uhlandstraße 10','71638 Ludwigsburg',NULL);

ALTER TABLE "APP"."LOCATION" ALTER COLUMN LOCATIONID RESTART WITH 20;
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
--  T a b l e  C O N T E S T
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
/*
INSERT INTO "APP"."CONTEST" VALUES(1,'CONTESTTYPE','NAME','PERSON');
*/
DELETE FROM "APP"."CONTEST";

INSERT INTO "APP"."CONTEST" VALUES(1,'KLAVIER','Klavier AG II',NULL);
INSERT INTO "APP"."CONTEST" VALUES(2,'KLAVIER','Klavier AG III',NULL);
INSERT INTO "APP"."CONTEST" VALUES(3,'KLAVIER','Klavier AG IV',NULL);
INSERT INTO "APP"."CONTEST" VALUES(4,'KLAVIER','Klavier AG V / VI',NULL);

INSERT INTO "APP"."CONTEST" VALUES(5,'HARFE','Harfe',NULL);

INSERT INTO "APP"."CONTEST" VALUES(6,'POP','Gitarre',NULL);

INSERT INTO "APP"."CONTEST" VALUES(7,'NEUEMUSIK','Neue Musik',NULL);

INSERT INTO "APP"."CONTEST" VALUES(8,'ENSEMBLE','Holzbläserensemble',NULL);

ALTER TABLE "APP"."CONTEST" ALTER COLUMN CONTESTID RESTART WITH 9;

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
--  T a b l e  EVENT
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
/*
INSERT INTO "APP"."EVENT" VALUES('EVENTID','CONTEST','TIMESLOT','LOCATION','CONFIRMED');
*/

DELETE FROM "APP"."EVENT";
-- 'KLAVIER','Klavier AG II' in 'JMS 0.11'
INSERT INTO "APP"."EVENT" VALUES(1,1,1,1,1);
INSERT INTO "APP"."EVENT" VALUES(2,1,2,1,1);
INSERT INTO "APP"."EVENT" VALUES(3,1,3,1,1);
INSERT INTO "APP"."EVENT" VALUES(4,1,4,1,1);
INSERT INTO "APP"."EVENT" VALUES(5,1,5,1,1);
INSERT INTO "APP"."EVENT" VALUES(6,1,6,1,1);
INSERT INTO "APP"."EVENT" VALUES(7,1,7,1,1);
INSERT INTO "APP"."EVENT" VALUES(8,1,8,1,1);
INSERT INTO "APP"."EVENT" VALUES(9,1,9,1,1);
INSERT INTO "APP"."EVENT" VALUES(10,1,10,1,1);
INSERT INTO "APP"."EVENT" VALUES(11,1,11,1,1);
INSERT INTO "APP"."EVENT" VALUES(12,1,12,1,1);
INSERT INTO "APP"."EVENT" VALUES(13,1,13,1,1);
INSERT INTO "APP"."EVENT" VALUES(14,1,14,1,1);
INSERT INTO "APP"."EVENT" VALUES(15,1,15,1,1);


-- 'KLAVIER','KLAVIER','Klavier AG III' in unknown location
INSERT INTO "APP"."EVENT" VALUES(16,2,1,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(17,2,2,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(18,2,3,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(19,2,4,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(20,2,5,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(21,2,6,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(22,2,7,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(23,2,8,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(24,2,9,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(25,2,10,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(26,2,11,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(27,2,12,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(28,2,13,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(29,2,14,NULL,1);
INSERT INTO "APP"."EVENT" VALUES(30,2,15,NULL,1);

ALTER TABLE "APP"."EVENT" ALTER COLUMN EVENTID RESTART WITH 31;



