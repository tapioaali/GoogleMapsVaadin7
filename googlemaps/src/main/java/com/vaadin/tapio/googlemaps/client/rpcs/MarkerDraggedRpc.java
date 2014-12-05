package com.vaadin.tapio.googlemaps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.tapio.googlemaps.client.LatLon;

/**
 * An RPC from client to server that is called when a marker has been dragged in
 * Google Maps.
 */
public interface MarkerDraggedRpc extends ServerRpc {
    public void markerDragged(long markerId, LatLon newPosition);
}
