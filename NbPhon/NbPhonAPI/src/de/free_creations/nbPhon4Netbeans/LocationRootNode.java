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

import de.free_creations.dbEntities.EntityIdentity;
import de.free_creations.dbEntities.Location;
import de.free_creations.nbPhonAPI.MutableEntityCollection;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.Action;
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
    private final PropertyChangeListener locationCollectionListener = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (MutableEntityCollection.PROP_ITEM_ADDED.equals(evt.getPropertyName())) {
        // a new location has been added to the to the  locationCollection,
          // this location must also be added to the internal "nodes" list.
          Object o = evt.getNewValue();
          if (o instanceof EntityIdentity) {
            EntityIdentity newLocation = (EntityIdentity) o;
            Integer locationid = (Integer) newLocation.primaryKey;
            //create a node for this new location
            LocationNode newNode = new LocationNode(locationid, locationManager);
            newNode.notifyPendingChanges();
            if (nodes instanceof ArrayList) {
            // if the nodes- list is an ArrayList (as created in initCollection)
              // we can insert on top of the list.
              ((ArrayList<Node>) nodes).add(0, newNode);
            } else {
              // otherways, we just insert at the end.
              nodes.add(newNode);
            }
            refresh();
          }
        }
        if (MutableEntityCollection.PROP_ITEM_REMOVED.equals(evt.getPropertyName())) {
          throw new UnsupportedOperationException("Not supported yet.");
        }
        if (MutableEntityCollection.PROP_ITEM_LIST_CHANGED.equals(evt.getPropertyName())) {
          nodes = initCollection();
          refresh();
        }
      }
    };

    private Locations(MutableEntityCollection<Location, Integer> locationManager) {
      this.locationManager = locationManager;
      this.locationManager.addPropertyChangeListener(locationCollectionListener);
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

  private final Action newItemAction = new NewLocationAction();
  private final Action[] allActions = new Action[]{newItemAction};

  public LocationRootNode(MutableEntityCollection<Location, Integer> locationManager) {
    super(new Locations(locationManager));
  }

  @Override
  public String getName() {
    return String.format("Location Root");
  }

  @Override
  public Action[] getActions(boolean context) {
    return allActions;
  }

}
