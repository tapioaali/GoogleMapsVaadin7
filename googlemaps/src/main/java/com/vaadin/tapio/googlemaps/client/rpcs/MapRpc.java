package com.vaadin.tapio.googlemaps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.GoogleMapTypeId;

/**
 * A collection of RPCs that keep up the state of the map object.
 */
public interface MapRpc extends ServerRpc {
    /**
     * The user has clicked the map.
     *
     * @param position
     *            the position of the click
     */
    public void mapClicked(LatLon position);

    /**
     * The user has modified the viewport.
     *
     * @param zoomLevel
     *            the amount of zoom
     * @param center
     *            position of the center of the map
     * @param boundsNE
     *            north-east corner of the viewport of the map
     * @param boundsSW
     *            south-west corner of the viewport of the map
     */
    public void mapMoved(int zoomLevel, LatLon center, LatLon boundsNE,
            LatLon boundsSW);

    /**
     * The user has changed the type of map (road map, satellite...)
     *
     * @param newMapTypeId
     *            the new type
     */
    public void mapTypeChanged(GoogleMapTypeId newMapTypeId);
}
