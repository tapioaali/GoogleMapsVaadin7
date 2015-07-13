package com.vaadin.tapio.googlemaps.client;

import com.google.gwt.maps.client.MapWidget;

/**
 * A listener which instances are called when the Google Maps API has been
 * loaded and the map object is ready to be used.
 */
public interface GoogleMapInitListener {
    public void mapWidgetInitiated(MapWidget map);

    public void mapsApiLoaded();
}
