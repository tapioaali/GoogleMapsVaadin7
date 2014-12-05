package com.vaadin.tapio.googlemaps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.tapio.googlemaps.client.LatLon;

/**
 * An RPC from client to server that is called when a marker has been clicked in
 * Google Maps.
 */
public interface MapClickedRpc extends ServerRpc {
    public void mapClicked(LatLon position);
}
