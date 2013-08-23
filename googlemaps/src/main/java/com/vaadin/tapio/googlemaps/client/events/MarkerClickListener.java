package com.vaadin.tapio.googlemaps.client.events;

import com.vaadin.tapio.googlemaps.client.GoogleMapMarker;

/**
 * Interface for listening marker click events.
 * 
 */
public interface MarkerClickListener {
    /**
     * Handle a MarkerClickEvent.
     * 
     * @param clickedMarker
     *            The marker that was clicked.
     */
    public void markerClicked(GoogleMapMarker clickedMarker);
}
