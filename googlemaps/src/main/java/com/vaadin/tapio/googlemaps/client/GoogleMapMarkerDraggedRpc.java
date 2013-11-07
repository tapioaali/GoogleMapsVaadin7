package com.vaadin.tapio.googlemaps.client;

import com.vaadin.shared.communication.ServerRpc;

/**
 * An RPC from client to server that is called when a marker has been dragged in
 * Google Maps.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 * 
 */
public interface GoogleMapMarkerDraggedRpc extends ServerRpc {
    public void markerDragged(long markerId, LatLon newPosition);
}
