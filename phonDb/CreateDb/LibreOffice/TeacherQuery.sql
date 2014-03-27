/**/
SELECT 
  "ALLOC"."ALLOCATIONID",
  "ALLOC"."PERSON" AS "PersonId",
  "ALLOC"."JOB" AS "JobId",
  "CONTEST"."CONTESTID",
  "CONTEST"."DESCRIPTION" AS "ContestDescription",
  "CONTEST"."NAME"  AS "ContestName",
  "PERSON"."SURNAME" AS "PersonSurname", 
  "PERSON"."GIVENNAME" AS "PersonGivenName", 
  (COALESCE("PERSON"."SURNAME",'N') || ', ' || COALESCE("PERSON"."GIVENNAME",'N'))AS "PersonFullName",
  "Responsible"."SURNAME" AS "RespSurname", 
  "Responsible"."GIVENNAME" AS "RespGivenname", 
  (COALESCE("Responsible"."SURNAME",'N') || ', ' || COALESCE("Responsible"."GIVENNAME",'N'))AS "ResponsibleFullName",
  "Teacher"."SURNAME" AS "TeacherSurname", 
  "Teacher"."GIVENNAME" AS "TeacherGivenname", 
  (COALESCE("Teacher"."SURNAME",'N') || ', ' || COALESCE("Teacher"."GIVENNAME",'N'))AS "TeacherFullName",
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
  "LOCATION"."STREET",
  "JOBTYPE"."JOBTYPEID",
  "JOBTYPE"."NAME" AS "JobTypeName",
  "AllocTeacher"."PERSON" AS "TeacherId"
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
   ON "CONTEST"."PERSON" = "Responsible"."PERSONID" 

   LEFT JOIN "APP"."JOB" AS "JOB"
   ON "ALLOC"."JOB" = "JOB"."JOBID"

   LEFT JOIN "APP"."JOBTYPE" AS "JOBTYPE"
   ON "JOB"."JOBTYPE" = "JOBTYPE"."JOBTYPEID"

   LEFT JOIN "APP"."ALLOCATION" AS "AllocTeacher"
   ON "AllocTeacher"."EVENT" = "EVENT"."EVENTID"

   LEFT JOIN "APP"."PERSON" AS "Teacher"
   ON "AllocTeacher"."PERSON" = "Teacher"."PERSONID" 
WHERE
  "AllocTeacher"."JOB" = 'LEHRER'
   AND "ALLOC"."JOB" <> 'LEHRER'



