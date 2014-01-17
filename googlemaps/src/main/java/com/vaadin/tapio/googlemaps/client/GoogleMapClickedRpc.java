package com.vaadin.tapio.googlemaps.client;

import com.vaadin.shared.communication.ServerRpc;

/**
 * An RPC from client to server that is called when a marker has been clicked in
 * Google Maps.
 * 
 * @author Ivano Selvaggi <ivoselva@gmail.com>
 * 
 */
public interface GoogleMapClickedRpc extends ServerRpc {
    public void mapClicked(LatLon position);
}
