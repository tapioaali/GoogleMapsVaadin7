package com.vaadin.tapio.googlemaps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.tapio.googlemaps.client.LatLon;

/**
 * Collection of marker-related events that are most likely caused by the user.
 */
public interface MarkerRpc extends ServerRpc {
    /**
     * A marker has been clicked.
     * 
     * @param markerId
     *            the id of the clicked marker
     */
    public void markerClicked(long markerId);

    /**
     * A marker has been dragged
     * 
     * @param markerId
     *            the id of the dragged marker
     * @param newPosition
     *            the new position of the marker
     */
    public void markerDragged(long markerId, LatLon newPosition);
}
