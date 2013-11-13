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

import de.free_creations.nbPhonAPI.Manager;
import static de.free_creations.jmNetbeans.IconManager.iconManager;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class GroupNode extends AbstractNode {

  private final String groupId;

  private static class LChildren extends Children.Array {

    private final Collection<Node> ch;

    public LChildren(Collection<Node> ch) {
      super();
      assert (ch != null);
      this.ch = ch;
    }

    @Override
    protected Collection<Node> initCollection() {
      return ch;
    }
  };

  private GroupNode(Children ch, String groupId) {
    super(ch);
    this.groupId = groupId;
  }

  public GroupNode(String groupId) {
    this(makeChildren(), groupId);
  }

  private static Children makeChildren() {
    Node[] nodes = new Node[]{
      new PersonNode(7, Manager.getPersonCollection()),
      new PersonNode(12, Manager.getPersonCollection()),
      new PersonNode(83, Manager.getPersonCollection()),
      new PersonNode(18, Manager.getPersonCollection()),
      new PersonNode(137, Manager.getPersonCollection()),
      new PersonNode(6, Manager.getPersonCollection()),
      new PersonNode(111, Manager.getPersonCollection()),};


    ArrayList aNodes = new ArrayList();
    aNodes.addAll(Arrays.asList(nodes));


    LChildren children = new LChildren(aNodes);


    return children;
  }

  @Override
  public String getName() {
    return String.format("%s", groupId);
  }

  @Override
  public Image getIcon(int type) {
    BufferedImage result = iconManager().iconGroup;
    return result;
  }
}
