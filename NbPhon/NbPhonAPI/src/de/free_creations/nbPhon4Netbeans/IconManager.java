/*
 * Copyright 2013 Harald Postner <Harald at free-creations.de>.
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

import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.RescaleOp;
import java.io.IOException;

import java.util.HashMap;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.openide.util.Exceptions;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class IconManager {

  public final BufferedImage iconStar;
  //---- Team Node
  public final BufferedImage iconTeam;
  public final BufferedImage iconTeamOpened;
  //---- Location Node
  public final BufferedImage iconLocation;
  public final BufferedImage iconNullLocation;
  //---- ContestNode
  public final BufferedImage iconContest;
  public final BufferedImage iconNullContest;
  //---- ContestTypes
  public final BufferedImage iconContestPiano;
  public final BufferedImage iconContestSinging;
  public final BufferedImage iconContestHarp;
  public final BufferedImage iconContestEnsemble;
  public final BufferedImage iconContestPop;
  public final BufferedImage iconNewMusic;
  //---- PersonNode
  public final BufferedImage iconEmpty24x16;
  public final BufferedImage iconEmpty32x16;
  public final BufferedImage iconChildFemale;
  public final BufferedImage iconChildMale;
  public final BufferedImage iconTeacher;
  public final BufferedImage iconMan;
  public final BufferedImage iconWoman;
  public final BufferedImage iconNobody;
  public final BufferedImage iconGroupleader;
  //---- Instruments
  public final BufferedImage iconClarinet;
  public final BufferedImage iconGuitar;
  public final BufferedImage iconHorn;
  public final BufferedImage iconPiano;
  public final BufferedImage iconOboe;
  public final BufferedImage iconPianoViolin;
  public final BufferedImage iconRecorder;
  public final BufferedImage iconSax;
  public final BufferedImage iconSinger;
  public final BufferedImage iconTrombone;
  public final BufferedImage iconTrumpet;
  public final BufferedImage iconTuba;
  public final BufferedImage iconTwoPianos;
  public final BufferedImage iconTwoSingers;
  public final BufferedImage iconViolin;
  public final BufferedImage iconNote;

  private IconManager() throws IOException {
    //---- Location Node
    iconTeam = ImageIO.read(IconManager.class.getResource("resources/group.png"));
    iconTeamOpened = ImageIO.read(IconManager.class.getResource("resources/groupOpened.png"));
    //---- Location Node
    iconLocation = ImageIO.read(IconManager.class.getResource("resources/house.png"));
    iconNullLocation = ImageIO.read(IconManager.class.getResource("resources/noHouse16x16.png"));
    //---- ContestNode
    iconContest = ImageIO.read(IconManager.class.getResource("resources/chairs16x16.png"));
    iconNullContest = ImageIO.read(IconManager.class.getResource("resources/noChairs16x16.png"));
    //---- ContestTypes
    iconContestPiano = ImageIO.read(IconManager.class.getResource("resources/pianoKeys.png"));
    iconContestSinging = ImageIO.read(IconManager.class.getResource("resources/twoSingers.png"));
    iconContestHarp = ImageIO.read(IconManager.class.getResource("resources/harp.png"));
    iconContestEnsemble = ImageIO.read(IconManager.class.getResource("resources/pianoViolin.png"));
    iconContestPop = ImageIO.read(IconManager.class.getResource("resources/sax.png"));
    iconNewMusic = ImageIO.read(IconManager.class.getResource("resources/newMusic.png"));

    //---- PersonNode
    iconEmpty24x16 = ImageIO.read(IconManager.class.getResource("resources/empty24x16.png"));
    iconEmpty32x16 = ImageIO.read(IconManager.class.getResource("resources/empty32x16.png"));
    iconChildFemale = ImageIO.read(IconManager.class.getResource("resources/childFemale16.png"));
    iconChildMale = ImageIO.read(IconManager.class.getResource("resources/childMale16.png"));
    iconTeacher = ImageIO.read(IconManager.class.getResource("resources/teacher16.png"));
    iconMan = ImageIO.read(IconManager.class.getResource("resources/man16.png"));
    iconWoman = ImageIO.read(IconManager.class.getResource("resources/woman16.png"));
    iconStar = ImageIO.read(IconManager.class.getResource("resources/star16.png"));
    iconNobody = ImageIO.read(IconManager.class.getResource("resources/nobody16.png"));
    iconGroupleader = ImageIO.read(IconManager.class.getResource("resources/groupLeader16.png"));
    //---- Instruments
    iconClarinet = ImageIO.read(IconManager.class.getResource("resources/clarinet.png"));
    iconGuitar = ImageIO.read(IconManager.class.getResource("resources/guitar.png"));
    iconHorn = ImageIO.read(IconManager.class.getResource("resources/horn.png"));
    iconPiano = ImageIO.read(IconManager.class.getResource("resources/piano.png"));
    iconOboe = ImageIO.read(IconManager.class.getResource("resources/oboe.png"));
    iconPianoViolin = ImageIO.read(IconManager.class.getResource("resources/pianoViolin.png"));
    iconRecorder = ImageIO.read(IconManager.class.getResource("resources/recorder.png"));
    iconSax = ImageIO.read(IconManager.class.getResource("resources/sax.png"));
    iconSinger = ImageIO.read(IconManager.class.getResource("resources/singer.png"));
    iconTrombone = ImageIO.read(IconManager.class.getResource("resources/trombone.png"));
    iconTrumpet = ImageIO.read(IconManager.class.getResource("resources/trumpet.png"));
    iconTuba = ImageIO.read(IconManager.class.getResource("resources/tuba.png"));
    iconTwoPianos = ImageIO.read(IconManager.class.getResource("resources/twoPianos.png"));
    iconTwoSingers = ImageIO.read(IconManager.class.getResource("resources/twoSingers.png"));
    iconViolin = ImageIO.read(IconManager.class.getResource("resources/violin.png"));
    iconNote = ImageIO.read(IconManager.class.getResource("resources/note.png"));
  }
  private static IconManager instance = null;
  private static final Object instanceLock = new Object();

  public static IconManager iconManager() {
    synchronized (instanceLock) {
      if (instance == null) {
        try {
          instance = new IconManager();
        } catch (IOException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
      return instance;
    }
  }

  /**
   * BLOCKFLOETE x GITARRE x HORN x KLARINETTE x KLAVIER-DUO x KLAVIER-STREICH x
   * KUNSTLIED MUSICAL OBOE x ORGEL POP POSAUNE trombone x QUERFLOETE SAX x
   * SEL-BESETZ TROMPETE x TUBA x
   *
   * @param contestType can be an icon from the resources or the ContestType Id
   * string
   * @return
   */
  public BufferedImage getContestTypeImage(String contestType) {
    if (contestType == null) {
      return null;
    }
    contestType = contestType.toLowerCase();
    if (!contestType.endsWith(".png")) {
      contestType = contestType + ".png";
    }

    switch (contestType) {
      case "klavier.png":
        return iconContestPiano;
      case "piano.png":
        return iconContestPiano;

      case "gesang.png":
        return iconContestSinging;
      case "sing.png":
        return iconContestSinging;

      case "harfe.png":
        return iconContestHarp;
      case "harp.png":
        return iconContestHarp;

      case "ensemble.png":
        return iconContestEnsemble;

      case "pop.png":
        return iconContestPop;

      case "neuemusik.png":
        return iconNewMusic;
      case "newmusic.png":
        return iconNewMusic;
    }
    return null;
  }

  BufferedImage getJobtypeImage(String jobType) {
    if (jobType == null) {
      return iconMan;
    }
    jobType = jobType.toLowerCase();
    if (!jobType.endsWith(".png")) {
      jobType = jobType + ".png";
    }
    switch (jobType) {
      case "teacher.png":
        return iconTeacher;
      case "helper.png":
        return iconMan;
    }
    return iconMan;
  }

  public ImageIcon image2icon(BufferedImage image) {
    synchronized (iconCacheLock) {
      if (image == null) {
        return null;
      }
      if (!iconCache.containsKey(image)) {
        ImageIcon newIcon = new ImageIcon(image);
        iconCache.put(image, newIcon);
      }
      return iconCache.get(image);
    }
  }
  private final HashMap<BufferedImage, ImageIcon> iconCache = new HashMap<>();
  private final Object iconCacheLock = new Object();

  /**
   * Overlays a star onto the given image and caches it for further use.
   *
   * @param originalImage
   * @return
   */
  public BufferedImage getStaredImage(BufferedImage originalImage) {
    synchronized (staredImageCacheLock) {
      if (originalImage == null) {
        return null;
      }
      if (!staredImageCache.containsKey(originalImage)) {
        BufferedImage staredImage = mergeImages(originalImage, iconStar, 0);
        staredImageCache.put(originalImage, staredImage);
      }
      return staredImageCache.get(originalImage);
    }
  }
  private final HashMap<BufferedImage, BufferedImage> staredImageCache = new HashMap<>();
  private final Object staredImageCacheLock = new Object();

  /**
   * Renders the image in gray and caches it for further use.
   *
   * @param originalImage
   * @return
   */
  public BufferedImage getDisabledImage(BufferedImage originalImage) {
    synchronized (disabledImageCacheLock) {
      if (originalImage == null) {
        return null;
      }
      if (!disabledImageCache.containsKey(originalImage)) {
        BufferedImage disabledImage = createDisabledImage(originalImage);
        disabledImageCache.put(originalImage, disabledImage);
      }
      return disabledImageCache.get(originalImage);
    }
  }
  private final HashMap<BufferedImage, BufferedImage> disabledImageCache = new HashMap<>();
  private final Object disabledImageCacheLock = new Object();

  /**
   * Overlays a star onto the given image and caches it for further use.
   *
   * @param baseImage
   * @param contestType
   * @return
   */
  public BufferedImage underLayContestTypeImage(BufferedImage baseImage, String contestType) {
    synchronized (contestTypeImageCacheLock) {
      if (baseImage == null) {
        return getContestTypeImage(contestType);
      }
      BufferedImage contestTypeImage = getContestTypeImage(contestType);
      if (contestTypeImage == null) {
        return baseImage;
      }
      ImageKey imageKey = new ImageKey(baseImage, contestType);
      if (!contestTypeImageCache.containsKey(imageKey)) {
        BufferedImage instrumentedImage = mergeImages(contestTypeImage, baseImage, 8, 0);
        contestTypeImageCache.put(imageKey, instrumentedImage);
      }
      return contestTypeImageCache.get(imageKey);
    }
  }
  private final HashMap<ImageKey, BufferedImage> contestTypeImageCache = new HashMap<>();
  private final Object contestTypeImageCacheLock = new Object();

  private class ImageKey {

    private final BufferedImage baseImage;
    private final String contestType;
    private final int hash;

    public ImageKey(BufferedImage baseImage, String contestType) {
      this.baseImage = baseImage;
      this.contestType = contestType;
      int h = 7;
      h = 53 * h + Objects.hashCode(this.baseImage);
      h = 53 * h + Objects.hashCode(this.contestType);
      hash = h;
    }

    @Override
    public int hashCode() {
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final ImageKey other = (ImageKey) obj;
      if (!Objects.equals(this.baseImage, other.baseImage)) {
        return false;
      }
      return Objects.equals(this.contestType, other.contestType);
    }
  }

  public BufferedImage mergeImages(BufferedImage image1, BufferedImage image2, int x) {
    return mergeImages(image1, image2, 0, x);
  }

  public BufferedImage mergeImages(BufferedImage image1, BufferedImage image2, int x1, int x2) {
    int width = 24;
    int height = 16;
    BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = combined.createGraphics();

    graphics.drawImage(image1, null, x1, 0);
    graphics.drawImage(image2, null, x2, 0);
    graphics.dispose();
    return combined;
  }

  private BufferedImage createDisabledImage(BufferedImage image1) {
    int width = 24;
    int height = 16;
    Graphics2D graphics;

    final BufferedImage greyImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    graphics = greyImage.createGraphics();
    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
    ColorConvertOp op = new ColorConvertOp(cs, null);
    graphics.drawImage(image1, op, 0, 0);
    graphics.dispose();

    float[] scales = {1f, 1f, 1f, 0.5f};
    float[] offsets = {0x00, 0x31, 0x4e, 0};
    RescaleOp rop = new RescaleOp(scales, offsets, null);
    final BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    graphics = scaledImage.createGraphics();
    graphics.drawImage(greyImage, rop, 0, 0);
    graphics.dispose();

    return scaledImage;
  }
}
