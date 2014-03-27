/*
Lists all personnes which have no allocation.

*/
SELECT 

  "PERSON"."SURNAME" AS "PersonSurname", 
  "PERSON"."GIVENNAME" AS "PersonGivenName", 
  (COALESCE("PERSON"."SURNAME",'N') || ', ' || COALESCE("PERSON"."GIVENNAME",'N'))AS "PersonFullName",
 
  "PERSON"."TELEPHONE",   
  "PERSON"."ZIPCODE",   
  "PERSON"."CITY",   
  "PERSON"."STREET", 
  "PERSON"."MOBILE", 
  "PERSON"."EMAIL"

/**/
FROM 
  "APP"."PERSON" AS "PERSON"

   LEFT JOIN "APP"."ALLOCATION" AS "ALLOC"
   ON "ALLOC"."PERSON" = "PERSON"."PERSONID" 

WHERE
  "ALLOC"."ALLOCATIONID" IS NULL



