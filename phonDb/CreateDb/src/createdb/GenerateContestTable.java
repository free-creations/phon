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
 * @author Harald Postner<harald at free-creations.de>
 */
public class GenerateContestTable {

  private final int timeSlotCount = 15; // must be the same as in "populateCoreTables.sql"
  private final int LocationCount = 19; // must be the same as in "populateExampleData.sql"
  private final int PersonCount = 50; // must be less or equal as in "gereatePersonTable.java"

  public GenerateContestTable() throws FileNotFoundException, IOException {
    File inFile = new File("src/createdb/resources/Contests.csv");

    File outputDir = new File("sql");
    if (!outputDir.exists()) {
      throw new RuntimeException("output directory not found. >" + outputDir.getAbsolutePath() + "<");
    }
    if (!outputDir.isDirectory()) {
      throw new RuntimeException("is not a directory. >" + outputDir.getAbsolutePath() + "<");
    }
    File outFile = new File(outputDir, "_4_populateExampleContests.sql");
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
          processLine(output, line, lineCount);
          lineCount++;
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
    GenerateContestTable generator = new GenerateContestTable();
  }

  private void processLine(PrintWriter output, String line, int lineCount) {
    int contestId = lineCount + 1;
    makeContestRecord(output, line, contestId);
    makeEventRecords(output, contestId);
  }

  private void makeContestRecord(PrintWriter output, String line, int contestId) {
    CsvReader csv = new CsvReader(line);

    String outLine
            = "INSERT INTO \"APP\".\"CONTEST\" VALUES("
            + "1,"
            + "'CONTESTTYPE',"
            + "'NAME',"
            + "'PERSON');";

    output.println(outLine);
  }

  private void makePrologue(PrintWriter output) {
    output.println("/*");
    output.println("File generated with \"GenerateContestTable.java\" " + (new Date()));
    output.println("*/");
    output.println("DELETE FROM \"APP\".\"AVAILABILITY\";");
    output.println("DELETE FROM \"APP\".\"ALLOCATION\";");
    output.println("DELETE FROM \"APP\".\"PERSON\";");
    output.println("DELETE FROM \"APP\".\"TEAM\";");
  }

  private void makeEpilogue(PrintWriter output, int lineCount) {
    output.println("--");
    output.println("ALTER TABLE \"APP\".\"CONTEST\" "
            + "ALTER COLUMN PERSONID RESTART WITH " + (lineCount + 1) + ";");
    output.println("--");
    output.println("/*");
    output.println("" + lineCount + " records written");
    output.println("*/");


  }

  private void makeEventRecords(PrintWriter output, int personId) {
    for (int timeSlot = 1; timeSlot <= timeSlotCount; timeSlot++) {
      String outLine
              = "INSERT INTO \"APP\".\"EVENT\" VALUES('EVENTID','CONTEST','TIMESLOT','LOCATION','SCHEDULED');";
      output.println(outLine);
    }
  }

}
