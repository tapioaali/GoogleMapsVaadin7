package com.vaadin.tapio.googlemaps.client.events;

import com.vaadin.tapio.googlemaps.client.LatLon;

/**
 * Interface for listening map move and zoom events.
 * 
 * @author Henri Muurimaa
 */
public interface MapMoveListener {
    /**
     * Handle a MapMoveEvent.
     * 
     * @param zoomLevel
     *            New zoom level
     * @param center
     *            New center
     * @param boundsNE
     *            Position of the north-east corner of the map
     * @param boundsSW
     *            Position of the south-west corner of the map
     */
    public void mapMoved(double zoomLevel, LatLon center, LatLon boundsNE,
            LatLon boundsSW);
}
