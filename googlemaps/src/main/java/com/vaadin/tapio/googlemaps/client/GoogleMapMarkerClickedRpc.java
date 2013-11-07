package com.vaadin.tapio.googlemaps.client;

import com.vaadin.shared.communication.ServerRpc;

/**
 * An RPC from client to server that is called when a marker has been clicked in
 * Google Maps.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 * 
 */
public interface GoogleMapMarkerClickedRpc extends ServerRpc {
    public void markerClicked(long markerId);
}
