package com.vaadin.tapio.googlemaps.client.events;

import java.io.Serializable;

import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolygon;

/**
 * Interface for listening polygon click events.
 * 
 */
public interface PolygonClickListener extends Serializable {
    /**
     * Handle a PolygonClickEvent.
     * 
     * @param clickedPolygon
     *            The polygon that was clicked.
     */
    public void polygonClicked(GoogleMapPolygon clickedPolygon, LatLon position);
}