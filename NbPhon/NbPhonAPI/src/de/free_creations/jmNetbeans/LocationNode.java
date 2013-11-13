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
package de.free_creations.jmNetbeans;

import static de.free_creations.jmNetbeans.IconManager.iconManager;
import java.awt.Image;
import java.awt.image.BufferedImage;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class LocationNode extends AbstractNode {

  private final String locationId;

  public LocationNode(String locationId) {
    super(Children.LEAF);
    this.locationId = locationId;
  }

  @Override
  public String getName() {
    return String.format("%s", locationId);
  }

  @Override
  public Image getIcon(int type) {
    BufferedImage result = iconManager().iconLocation;
    return result;
  }
}
