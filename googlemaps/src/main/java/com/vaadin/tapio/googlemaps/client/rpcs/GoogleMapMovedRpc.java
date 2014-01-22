package com.vaadin.tapio.googlemaps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.tapio.googlemaps.client.LatLon;

/**
 * An RPC from client to server that is called when the user has moved the map.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 * 
 */
public interface GoogleMapMovedRpc extends ServerRpc {
    public void mapMoved(double zoomLevel, LatLon center, LatLon boundsNE,
            LatLon boundsSW);
}
