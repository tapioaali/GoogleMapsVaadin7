package com.vaadin.tapio.googlemaps.extensions;

import com.vaadin.server.AbstractExtension;
import com.vaadin.tapio.googlemaps.GoogleMap;

/**
 * The class representing Google Maps.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 */
public class GoogleMapGeometry extends AbstractExtension {
    public GoogleMapGeometry(GoogleMap map) {
        super.extend(map);
    }
}
