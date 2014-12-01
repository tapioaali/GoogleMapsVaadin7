package com.vaadin.tapio.googlemaps.client.events;

import com.vaadin.tapio.googlemaps.client.LatLon;

/**
 * Interface for listening map double click events.
 * 
 */
public interface MapDbClickListener {
	/**
     * Handle a MapDbClickEvent.
     * 
     * @param mapDbClicked
     *            The map that was double clicked.
     */
    public void mapDbClicked(LatLon position);
}
