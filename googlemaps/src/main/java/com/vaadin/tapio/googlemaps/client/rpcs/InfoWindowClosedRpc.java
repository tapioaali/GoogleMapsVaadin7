package com.vaadin.tapio.googlemaps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;

/**
 * An RPC from the client to the server that is called when an info window is
 * closed by the user on the map.
 */
public interface InfoWindowClosedRpc extends ServerRpc {
    public void infoWindowClosed(long windowId);
}
