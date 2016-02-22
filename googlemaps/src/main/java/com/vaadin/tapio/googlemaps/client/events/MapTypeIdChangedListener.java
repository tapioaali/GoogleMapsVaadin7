package com.vaadin.tapio.googlemaps.client.events;

import java.io.Serializable;

import com.vaadin.tapio.googlemaps.client.GoogleMapTypeId;

/**
 * Interface for listening map type changes.
 */
public interface MapTypeIdChangedListener extends Serializable {
    public void mapTypeIdChanged(GoogleMapTypeId newMapTypeId);
}
