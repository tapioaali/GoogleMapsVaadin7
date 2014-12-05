package com.vaadin.tapio.googlemaps.client.events;

import java.io.Serializable;

import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;

/**
 * Interface for listening info window close events initiated by the user.
 */
public interface InfoWindowClosedListener extends Serializable {

    /**
     * Handle an info window close event
     * 
     * @param window
     *            the info window that was closed.
     */
    public void infoWindowClosed(GoogleMapInfoWindow window);
}
