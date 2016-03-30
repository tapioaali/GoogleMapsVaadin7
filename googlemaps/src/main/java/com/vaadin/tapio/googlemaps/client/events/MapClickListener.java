package com.vaadin.tapio.googlemaps.client.events;

import java.io.Serializable;

import com.vaadin.tapio.googlemaps.client.LatLon;

/**
 * Interface for listening map click events.
 */
public interface MapClickListener extends Serializable {
    /**
     * Handle a MapClickListener.
     * 
     * @param position
     *            The position that was clicked.
     */
    void mapClicked(LatLon position);
}
