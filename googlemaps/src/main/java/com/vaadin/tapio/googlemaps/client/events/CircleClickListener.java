package com.vaadin.tapio.googlemaps.client.events;

import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapCircle;

/**
 * Interface for listening circle click events.
 * 
 */
public interface CircleClickListener {
	/**
     * Handle a CircleClickEvent.
     * 
     * @param clickedCircle, position
     *            The circle that was clicked.
     */
    public void circleClicked(GoogleMapCircle clickedCircle, LatLon position);
}
