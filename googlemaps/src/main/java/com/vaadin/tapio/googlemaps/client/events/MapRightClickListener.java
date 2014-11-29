package com.vaadin.tapio.googlemaps.client.events;

import com.vaadin.tapio.googlemaps.client.LatLon;

public interface MapRightClickListener {
	/**
     * Handle a MapRightClickListener.
     * 
     * @param mapRightClicked
     *            The map that was right clicked.
     */
    public void mapRightClicked(LatLon position);
}
