/*
 * Copyright 2014 Harald Postner<harald at free-creations.de>.
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
package de.free_creations.actions.analysis;

import de.free_creations.dbEntities.Availability;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.JobType;
import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhonAPI.Manager;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Harald Postner<harald at free-creations.de>
 */
public class RequirementPlanningExecutor {

  private String[] timeSlotNames;
  private int[] highPrioAvailable;
  private int[] highPrioRequired;

  private int[] lowPrioAvailable;
  private int[] lowPrioRequired;
  private int timeSlotCount;
  private String prioJobName;

  public void apply() {
    // find the high prio job.
    List<Job> jj = Manager.getJobCollection().getAll();
    Job prioJob = null;
    for (Job j : jj) {
      if (Objects.equals(j.getPriority(), 1)) {
        prioJob = j;
        break;
      }
    }
    assert (prioJob != null);
    JobType prioJobType = prioJob.getJobType();
    prioJobName = prioJobType.getName();
    assert (prioJobType != null);
    int lowPrioJobCount = jj.size() - 1;
    assert (lowPrioJobCount > 0);

    List<TimeSlot> tt = Manager.getTimeSlotCollection().getAll();
    timeSlotCount = tt.size();
    timeSlotNames = new String[timeSlotCount];
    highPrioAvailable = new int[timeSlotCount];
    highPrioRequired = new int[timeSlotCount];
    lowPrioAvailable = new int[timeSlotCount];
    lowPrioRequired = new int[timeSlotCount];

    for (TimeSlot t : tt) {
      int idx = t.getTimeSlotId() - 1;
      timeSlotNames[idx] = t.getLabel();
    }
    // assess the number of Required
    List<Event> ee = Manager.getEventCollection().getAll();

    for (Event e : ee) {
      if (e.isScheduled()) {
        TimeSlot timeSlot = e.getTimeSlot();
        assert (timeSlot != null);
        int idx = timeSlot.getTimeSlotId() - 1;
        highPrioRequired[idx]++;
        lowPrioRequired[idx] += lowPrioJobCount;
      }
    }
    // assess the number of Available
    List<Availability> aa = Manager.getAvailabilityCollection().getAll();
    for (Availability a : aa) {
      if (a.isAvailable()) {
        TimeSlot timeSlot = a.getTimeSlot();
        assert (timeSlot != null);
        int idx = timeSlot.getTimeSlotId() - 1;
        Person person = a.getPerson();
        assert (person != null);
        if (Objects.equals(person.getJobType(), prioJobType)) {
          highPrioAvailable[idx]++;
        } else {
          lowPrioAvailable[idx]++;
        }
      }
    }
    // if there are superfluous high-prio we can use them for low-prio
    for (int idx=0;idx<timeSlotCount;idx++){
      int superFlous = highPrioAvailable[idx] - highPrioRequired[idx];
      if(superFlous >0){
        lowPrioAvailable[idx] += superFlous;
      }
    }

  }

  public String getHtmlResult() {
    StringBuilder result = new StringBuilder();
    result.append("<html>\n");

    result.append(String.format("<b>For \"%s\" Jobs:</b><br><br>\n", prioJobName));

    result.append("<table border=\"1\">");
    result.append("<th>Time</th><th>required/available</th><th>missing</th>\n");
    for (int i = 0; i < timeSlotCount; i++) {
      result.append("<tr>");
      int avail = highPrioAvailable[i];
      int requir = highPrioRequired[i];
      if (avail < requir) {
        result.append(String.format("<td>%s</td><td> %s / %s </td><td> %s</td>\n",
                timeSlotNames[i],
                requir,
                avail,
                requir - avail));
      } else {
        result.append(String.format("<td>%s</td><td> %s / %s </td><td></td>\n",
                timeSlotNames[i],
                requir,
                avail));
      }
      result.append("</tr>");
    }
    result.append("</table>");
    result.append("<br><br>\n");

    result.append("<b>For \"Helfer\" Jobs:</b><br><br>");

    result.append("<table border=\"1\">");
    result.append("<th>Time</th><th>required/available</th><th>missing</th>\n");
    for (int i = 0; i < timeSlotCount; i++) {
      result.append("<tr>");
      int avail = lowPrioAvailable[i];
      int requir = lowPrioRequired[i];
      if (avail < requir) {
        result.append(String.format("<td>%s</td><td> %s / %s </td><td> %s</td>\n",
                timeSlotNames[i],
                requir,
                avail,
                requir - avail));
      } else {
        result.append(String.format("<td>%s</td><td> %s / %s </td><td></td>\n",
                timeSlotNames[i],
                requir,
                avail));
      }
      result.append("</tr>");
    }
    result.append("</table>");
    result.append("</html>");
    return result.toString();
  }

}
