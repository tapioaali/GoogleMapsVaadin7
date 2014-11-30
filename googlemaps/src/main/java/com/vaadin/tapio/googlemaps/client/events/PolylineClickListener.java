package com.vaadin.tapio.googlemaps.client.events;

import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;

/**
 * Interface for listening polyline click events.
 * 
 */
public interface PolylineClickListener {
	/**
     * Handle a PolylineClickEvent.
     * 
     * @param clickedPolyline
     *            The polyline that was clicked.
     */
    public void polylineClicked(GoogleMapPolyline clickedPolyline, LatLon position);
}
