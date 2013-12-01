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

import de.free_creations.dbEntities.Location;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Harald Postner <Harald at free-lreations.de>
 */
public class LocationRootNode extends AbstractNode {

  private static class Locations extends Children.Array {

    private final MutableEntityCollection<Location, Integer> locationManager;

    private Locations(MutableEntityCollection<Location, Integer> locationManager) {
      this.locationManager = locationManager;
    }

    @Override
    protected Collection<Node> initCollection() {
      List<Location> ll = locationManager.getAll();
      ArrayList<Node> result = new ArrayList<>();
      for (Location l : ll) {
        LocationNode ln = new LocationNode(l.getLocationId(), locationManager);
        result.add(ln);
      }
      return result;
    }

  };

  public LocationRootNode(MutableEntityCollection<Location, Integer> locationManager) {
    super(new Locations(locationManager));
  }

  @Override
  public String getName() {
    return String.format("Location Root");
  }

}
