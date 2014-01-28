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
package createdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 *
 *
 * @see http://www.fakenamegenerator.com
 * @author Harald Postner<harald at free-creations.de>
 */
public class GeneratePersonTable {

  private final int maxCount = 300; // must be less than there are records in FakeNames.csv (500)
  private final int timeSlotCount = 15; // must be the same as in "populateCoreTables.sql"

  private final double availabilty = 0.3;
  private final double teacherRatio = 0.05; // note: "Generate conetst table will add some 30 teachers
  private final double childrenRatio = 0.2;
  private final double teenagerRatio = 0.4; // note: adult ratio is (1.0-teenagerRatio-childrenRatio)
  private final double meanTeamSize = 10;
  private final double wantsToBeInTeamRatio = 0.5;
  private final double teamButNoContestRatio = 0.3; // the number of persons in a team that have no contest preference
  private final double teamButOtherContestRatio = 0.1; // the number of persons in a team that have an other contest preference

  private final int teamCount;
  private final String[] teamNames;

  public GeneratePersonTable() throws FileNotFoundException, IOException {
    teamCount = (int) ((maxCount * wantsToBeInTeamRatio) / meanTeamSize);
    teamNames = new String[teamCount+1];

    for (int i = 0; i <= teamCount; i++) {
      teamNames[i] = String.format("Team[%s]", i + 1);
    }

    File inFile = new File("src/createdb/resources/FakeNames.csv");

    File outputDir = new File("sql");
    if (!outputDir.exists()) {
      throw new RuntimeException("output directory not found. >" + outputDir.getAbsolutePath() + "<");
    }
    if (!outputDir.isDirectory()) {
      throw new RuntimeException("is not a directory. >" + outputDir.getAbsolutePath() + "<");
    }
    File outFile = new File(outputDir, "_3_populateExamplePersons.sql");
    PrintWriter output;
    output = new PrintWriter(outFile);
    makePrologue(output);
    int lineCount = 0;
    try (BufferedReader input = new BufferedReader(new FileReader(inFile))) {

      input.readLine(); // skip first line
      boolean eof = false;
      while (!eof) {
        String line = input.readLine();
        if (line == null) {
          eof = true;
        } else {
          if (lineCount >= maxCount) {
            eof = true;
          } else {
            processLine(output, line, lineCount);
            lineCount++;
          }
        }
      }
      input.close();
    }
    makeEpilogue(output, lineCount);
    output.close();
  }

  /**
   * @param args the command line arguments
   * @throws java.io.FileNotFoundException
   */
  public static void main(String[] args) throws FileNotFoundException, IOException {
    GeneratePersonTable generator = new GeneratePersonTable();
  }

  private void processLine(PrintWriter output, String line, int lineCount) {
    int personId = lineCount + 1;
    makePersonRecord(output, line, personId);
    makeAvailabilityRecords(output, personId);
  }

  private void makePersonRecord(PrintWriter output, String line, int personId) {
    CsvReader csv = new CsvReader(line);
    String age = randomAge();
    String jobType = "HELFER";
    boolean teacher = isTeacher();
    if (teacher) {
      age = "ERWACHSEN";
      jobType = "LEHRER";
    }
    int teamNumber = teamNumber();
    setTeamName(String.format("Team[%s]", csv.item(3)), teamNumber);

    String outLine
            = "INSERT INTO \"APP\".\"PERSON\" VALUES ("
            + personId + "," +//  "PERSONID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
            "'" + csv.item(3) + "'," +//  "SURNAME" VARCHAR(50), 
            "'" + csv.item(1) + "'," +//  "GIVENNAME" VARCHAR(50), 
            gender(csv.item(0)) + "," +//  "GENDER" VARCHAR(50), 
            "'" + csv.item(7) + "'," +//  "ZIPCODE" VARCHAR(50), 
            "'" + csv.item(5) + "'," +//  "CITY" VARCHAR(50), 
            "'" + csv.item(4) + "'," +//  "STREET" VARCHAR(50), 
            "'" + csv.item(9) + "'," +//  "TELEFONE" VARCHAR(50), 
            "'" + randomMobile() + "'," +//  "MOBILE" VARCHAR(50), 
            "'" + csv.item(8) + "'," +//  "EMAIL" VARCHAR(50), 
            "'" + age + "'," +//  "AGEGRUPPE" VARCHAR(50), 
            "NULL," +//  "NOTICE" VARCHAR(255), 
            teamNumberString(teamNumber) + "," +//"TEAM" INTEGER, 
            "'" + jobType + "'," +//  "JOBTYPE" VARCHAR(50), 
            randomContestType(teamNumber) + "," +//  "CONTESTYPE" INTEGER, 
            "CURRENT_TIMESTAMP" +//  "LETZTEAENDERUNG" TIMESTAMP
            ");";

    output.println(outLine);
  }

  private void setTeamName(String newName, int teamNumber) {
    if (teamNumber > teamCount) {
      return;
    }
    if (teamNumber < 0) {
      return;
    }
    teamNames[teamNumber] = newName;
  }

  private String teamNumberString(int teamNumber) {
    if (teamNumber < 1) {
      return "NULL";
    } else {
      return Integer.toString(teamNumber);
    }
  }

  /**
   * Assigns a random team number.
   *
   * @return The team number or -1 if the person does not want to be allocated
   * to a team.
   */
  private int teamNumber() {
    if (hasTeam()) {
      double random = Math.random();
      int team = ((int) ((teamCount) * random)) + 1;
      return team;
    } else {
      return -1;
    }
  }

  private boolean hasTeam() {
    double random = Math.random();
    return (random < wantsToBeInTeamRatio);
  }

  private String gender(String s) {
    switch (s) {
      case "female":
        return "'Fr.'";
      case "male":
        return "'Hr.'";
    }
    return "NULL";
  }

  private void makePrologue(PrintWriter output) {
    output.println("/*");
    output.println("File generated with \"GeneratePersonTable.java\" " + (new Date()));
    output.println("*/");
    output.println("DELETE FROM \"APP\".\"AVAILABILITY\";");
    output.println("DELETE FROM \"APP\".\"ALLOCATION\";");
    output.println("DELETE FROM \"APP\".\"PERSON\";");
    output.println("DELETE FROM \"APP\".\"TEAM\";");

    makeTeamRecords(output);

  }

  private void makeTeamRecords(PrintWriter output) {
    for (int i = 0; i < teamCount; i++) {
      output.println("INSERT INTO \"APP\".\"TEAM\" VALUES("
              + String.format("%s,", i + 1) +//  "TEAMID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
              String.format("'Team[%s]'", i + 1) + "," + //"NAME" VARCHAR(50)
              "NULL" +//"PERSON" INTEGER
              ");");
    }
    output.println("ALTER TABLE \"APP\".\"TEAM\" "
            + "ALTER COLUMN \"TEAMID\" RESTART WITH " + (teamCount + 1) + ";");
    output.println("");
    output.println("");
  }

  private void updateTeamNames(PrintWriter output) {
    for (int i = 1; i <= teamCount; i++) {
      output.println("UPDATE APP.TEAM SET \"NAME\" = "
              + "'"+teamNames[i]+"' "
              + "WHERE TEAMID = "
              + teamNumberString(i)
              + ";");
    }
  }

  private void makeEpilogue(PrintWriter output, int lineCount) {
    output.println("--");
    updateTeamNames(output);

    output.println("--");
    output.println("ALTER TABLE \"APP\".\"PERSON\" "
            + "ALTER COLUMN PERSONID RESTART WITH " + (lineCount + 1) + ";");
    output.println("--");
    output.println("/*");
    output.println("" + lineCount + " persons-records written");
    output.println("*/");

  }

  private void makeAvailabilityRecords(PrintWriter output, int personId) {
    for (int timeSlot = 1; timeSlot <= timeSlotCount; timeSlot++) {
      String outLine
              = "INSERT INTO \"APP\".\"AVAILABILITY\" VALUES ("
              + "DEFAULT," +//  "VERFUEGID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
              personId + "," +//  "PERSONID" INTEGER NOT NULL, 
              timeSlot + "," +//  "ZEITID" INTEGER NOT NULL, 
              isAvailable() + "," +//  "VERFUEGBAR" INTEGER, 
              "CURRENT_TIMESTAMP" +//  "LETZTEAENDERUNG" TIMESTAMP
              ");";
      output.println(outLine);
    }
  }

  private int isAvailable() {
    double random = Math.random();
    if (random < availabilty) {
      return 1;
    } else {
      return 0;
    }
  }

  private boolean isTeacher() {
    double random = Math.random();
    return (random < teacherRatio);
  }

  private String randomAge() {
    double random = Math.random();
    if (random < childrenRatio) {
      return "KIND";
    } else if (random < teenagerRatio) {
      return "JUGENDLICH";
    }
    return "ERWACHSEN";
  }

  private String randomMobile() {
    double random = Math.random();
    int n = (int) (9999999 * random);
    return "0152 " + n;

  }

  private String randomContestType(int teamNumber) {
    double random = Math.random();
    int contest = (int) ((6 + 2) * random);
    if (teamNumber > 0) {
      if (Math.random() > teamButOtherContestRatio) {
        if (Math.random() > teamButNoContestRatio) {
          contest = (teamNumber - 1) % 6;
        } else {
          contest = -1;
        }
      }
    }
    switch (contest) {
      case 0:
        return "'KLAVIER'";
      case 1:
        return "'GESANG'";
      case 2:
        return "'HARFE'";
      case 3:
        return "'ENSEMBLE'";
      case 4:
        return "'POP'";
      case 5:
        return "'NEUEMUSIK'";
      default:
        return "NULL";
    }
  }

}
