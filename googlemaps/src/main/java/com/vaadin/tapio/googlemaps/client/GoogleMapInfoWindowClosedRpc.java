package com.vaadin.tapio.googlemaps.client;

import com.vaadin.shared.communication.ServerRpc;

/**
 * An RPC from the client to the server that is called when an info window is
 * closed by the user on the map.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 * 
 */
public interface GoogleMapInfoWindowClosedRpc extends ServerRpc {
    public void infoWindowClosed(GoogleMapInfoWindow window);
}
