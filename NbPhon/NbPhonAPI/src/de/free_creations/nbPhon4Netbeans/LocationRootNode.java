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
import java.util.Objects;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Harald Postner <Harald at free-lreations.de>
 */
public class LocationRootNode extends AbstractNode {

  private static class LocationsNodesArray extends Children.Array {

    private final MutableEntityCollection<Location, Integer> locationManager;
    private final boolean withNullItem;

    /**/
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

    private LocationsNodesArray(MutableEntityCollection<Location, Integer> locationManager, boolean withNullItem) {
      this.withNullItem = withNullItem;
      this.locationManager = locationManager;
      this.locationManager.addPropertyChangeListener(locationCollectionListener);
    }

    @Override
    protected Collection<Node> initCollection() {
      List<Location> ll = locationManager.getAll();
      ArrayList<Node> result = new ArrayList<>();
      if (withNullItem) {
        result.add(new LocationNode(null, null));
      }
      for (Location l : ll) {
        LocationNode ln = new LocationNode(l.getLocationId(), locationManager);
        result.add(ln);
      }

      return result;
    }

    /**
     * Find the current position of a given node.
     *
     * @param key the LocationId that is searched for.
     * @return returns the index of the current position. Returns -1 if no
     * LocationNode with the searched key could be found.
     */
    int findIndexForNode(Integer key) {
      int nodesCount = getNodesCount();
      for (int i = 0; i < nodesCount; i++) {
        Node n = getNodeAt(i);
        if (n instanceof LocationNode) {
          LocationNode pn = (LocationNode) n;
          if (Objects.equals(pn.getLocationId(), key)) {
            return i;
          }
        }
      }
      return -1;
    }

  };

  private final Action newItemAction = new NewLocationAction();
  private final Action[] allActions = new Action[]{newItemAction};
  private final LocationsNodesArray children;

  public LocationRootNode(MutableEntityCollection<Location, Integer> locationManager) {
    this(locationManager, false);
  }

  public LocationRootNode(MutableEntityCollection<Location, Integer> locationManager,
          boolean withNullItem) {
    this(new LocationsNodesArray(locationManager, withNullItem));
  }

  private LocationRootNode(LocationsNodesArray children) {
    super(children);
    this.children = children;
  }

  @Override
  public String getName() {
    return String.format("Location Root");
  }

  @Override
  public Action[] getActions(boolean context) {
    return allActions;
  }

  /**
   * Getter for a child at a given position.
   *
   * Note: the position of a child node depends on the currently chosen sort
   * order.
   *
   * If a child with such index does not exists it returns null.
   *
   * @param index
   * @return the LocationNode that is currently at the position given by index
   * or null if the index is invalid.
   */
  public LocationNode getNodeAt(int index) {
    if (index < 0) {
      return null;
    }
    if (index >= children.getNodesCount()) {
      return null;
    }
    Node result = children.getNodeAt(index);
    if (result instanceof LocationNode) {
      return (LocationNode) result;
    } else {
      return null;
    }
  }

  /**
   * Returns the key for the node currently located at the given position.
   *
   * Note: the position of a child node depends on the currently chosen sort
   * order.
   *
   * If a child with such index does not exists it returns null.
   *
   *
   * @param index
   * @return the LocationNode that is currently at the position given by index.
   * Returns null if the index is invalid.
   */
  public Integer getNodeKeyAt(int index) {
    LocationNode node = getNodeAt(index);
    if (node != null) {
      return node.getLocationId();
    } else {
      return null;
    }
  }

  /**
   * Find the position of a Location node with a given key.
   *
   * @param key
   * @return return the current position. If there is no LocationNode with the
   * given key the function will return -1.
   */
  public int findIndexForNode(Integer key) {
    return children.findIndexForNode(key);
  }
}
