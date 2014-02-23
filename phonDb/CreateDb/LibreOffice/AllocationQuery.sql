/**/
SELECT 
  "ALLOC"."PERSON" ,
  "ALLOC"."JOB" ,
  "CONTEST"."CONTESTID",
  "CONTEST"."DESCRIPTION" AS "ContestDescription",
  "CONTEST"."NAME"  AS "ContestName",
  "PERSON"."SURNAME", 
  "PERSON"."GIVENNAME" ,
  "Responsible"."SURNAME" AS "RespSurname", 
  "Responsible"."GIVENNAME" AS "RespGivenname", 
  "PERSON"."TELEPHONE", 
  "PERSON"."MOBILE", 
  "PERSON"."EMAIL", 
  "TIMESLOT"."TIMESLOTID", 
  "TIMESLOT"."DAYOFWEEK", 
  "TIMESLOT"."DAYIDX",
  "TIMESLOT"."DATUM", 
  "TIMESLOT"."TIMEOFDAYPRINT", 
  "TIMESLOT"."TIMEOFDAYIDX",
  "TIMESLOT"."LABEL" AS "TimeLabel",
  "LOCATION"."NAME" AS "LocationName",
  "LOCATION"."BUILDING",
  "LOCATION"."ROOM",
  "LOCATION"."STREET"
/**/
FROM 
  "APP"."ALLOCATION" AS "ALLOC"

   LEFT JOIN "APP"."PERSON" AS "PERSON"
   ON "ALLOC"."PERSON" = "PERSON"."PERSONID" 

   LEFT JOIN   "APP"."EVENT" AS "EVENT"
   ON "ALLOC"."EVENT" = "EVENT"."EVENTID"

   LEFT JOIN "APP"."TIMESLOT" AS "TIMESLOT"
   ON "EVENT"."TIMESLOT" = "TIMESLOT"."TIMESLOTID" 

   LEFT JOIN  "APP"."CONTEST" AS "CONTEST"
   ON  "EVENT"."CONTEST" = "CONTEST"."CONTESTID"

   LEFT JOIN  "APP"."LOCATION" AS "LOCATION"
   ON  "EVENT"."LOCATION" = "LOCATION"."LOCATIONID"

   LEFT JOIN "APP"."PERSON" AS "Responsible"
   ON "CONTEST"."PERSON" = "PERSON"."PERSONID" 
/**/

