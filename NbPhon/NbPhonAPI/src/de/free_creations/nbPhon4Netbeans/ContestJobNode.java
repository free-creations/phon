/*
 * Copyright 2014 Harald Postner <Harald at free-creations.de>.
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
package de.free_creations.nbPhon4Netbeans;

import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.ContestType;
import de.free_creations.dbEntities.Job;
import de.free_creations.dbEntities.JobType;

import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.datatransfer.ExTransferable;
import static de.free_creations.nbPhon4Netbeans.IconManager.*;
import de.free_creations.nbPhonAPI.Manager;

/**
 * Visualizes a job within a contest.
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class ContestJobNode extends AbstractNode {

  /**
   * The data flavor for a Drag and Drop action.
   */
  public static class ContestJobNodeFlavor extends DataFlavor {

    public ContestJobNodeFlavor() {
      super(ContestJobNode.class, "ContestJob");
    }
  }
  
  public static class TransferData{
    public final Integer contestId;
    public final String jobId;

    public TransferData(Integer contestId, String jobId) {
      this.contestId = contestId;
      this.jobId = jobId;
    }

    @Override
    public String toString() {
      return "TransferData{" + "contestId=" + contestId + ", jobId=" + jobId + '}';
    }
    
  }

  /**
   * The transferable that is transfered in a Drag and Drop action.
   */
  public static class ContestJobNodeTransferable extends ExTransferable.Single {

    public final Integer transferContestId;
    public final String transferJobId;

    public ContestJobNodeTransferable(Integer contestId, String jobId) {
      super(CONTESTJOB_NODE_FLAVOR);
      this.transferContestId = contestId;
      this.transferJobId = jobId;
    }

    /**
     * The contest node transfers the primary key of the record it represents.
     *
     * @return
     */
    @Override
    protected TransferData getData() {
      return new TransferData(transferContestId, transferJobId);
    }
  }
  public static final DataFlavor CONTESTJOB_NODE_FLAVOR = new ContestJobNodeFlavor();

  private final Integer contestId;
  private final String jobId;

  public ContestJobNode(Integer contestId, String jobId) {
    super(Children.LEAF);
    this.contestId = contestId;
    this.jobId = jobId;

  }

  public Integer getContestId() {
    return contestId;
  }

  public String getJobId() {
    return jobId;
  }

  @Override
  public boolean canCopy() {
    return true;
  }

  @Override
  public boolean canCut() {
    return false;
  }

  @Override
  public boolean canRename() {
    return false;
  }

  @Override
  public String getName() {
    return String.format("%s in contest %s", jobId, contestId);
  }

  @Override
  public String getDisplayName() {
    return getDisplayName(jobId);
  }

  /**
   * A static version that can be used without creating a Node.
   *
   * @param jobId
   * @return
   */
  public static String getDisplayName(String jobId) {
    String result = jobId;
    try {
      Job j = Manager.getJobCollection().findEntity(jobId);
      if (j != null) {
        String name = j.getName();
        if (name != null) {
          result = name;
        }
      }
    } catch (DataBaseNotReadyException ex) {
    }
    return result;
  }

  /**
   * Formats an html string of two lines, on the first line the contest description,
   * on the second line the job description.
   * @param contestId
   * @param jobId
   * @return 
   */
  public static String getLongHtmlDescription(Integer contestId, String jobId) {
    String contestDesc = String.format("contest[%s]", contestId); // a default
    try {
      Contest c = Manager.getContestCollection().findEntity(contestId);
      if(c != null){
        String name = c.getName();
        if(name != null){
          contestDesc = name;
        }
      }
    } catch (DataBaseNotReadyException ex) {
    }
    return String.format("<html>%s<br>%s</html>",contestDesc,getDisplayName(jobId));
  }

  @Override
  public Image getIcon(int type) {
    return getIcon(contestId, jobId);
  }

  /**
   * A static version that can be used without creating a Node.
   *
   * @param contestId
   * @param jobId
   * @return
   */
  public static Image getIcon(Integer contestId, String jobId) {
    BufferedImage result = iconManager().getJobtypeImage(null);
    try {
      Job j = Manager.getJobCollection().findEntity(jobId);
      if (j != null) {
        JobType jobType = j.getJobType();
        if (jobType != null) {
          result = iconManager().getJobtypeImage(jobType.getIcon());
        }
      }
      Contest c = Manager.getContestCollection().findEntity(contestId);
      if (c != null) {
        ContestType contestType = c.getContestType();
        if (contestType != null) {
          result = iconManager().underLayContestTypeImage(result, contestType.getIcon());
        }
      }
    } catch (DataBaseNotReadyException ex) {
    }
    return result;
  }

  @Override
  public Image getOpenedIcon(int type) {
    return getIcon(type);
  }

  /**
   * The transferable in a Drag and Drop is the default Netbeans node
   * transferable plus the specific ContestNodeTransferable.
   *
   * @return the interface for classes that can be used to provide data for a
   * transfer operation.
   * @throws IOException
   */
  @Override
  public Transferable clipboardCopy() throws IOException {
    Transferable nbDefault = super.clipboardCopy();
    ExTransferable added = ExTransferable.create(nbDefault);
    added.put(new ContestJobNodeTransferable(getContestId(), getJobId()));
    return added;
  }
}
