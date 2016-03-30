package com.vaadin.tapio.googlemaps.client.events;

import java.io.Serializable;

import com.google.gwt.maps.client.MapTypeId;

public interface MapTypeChangeListener extends Serializable {
    /**
     * Handle a MapTypeIdChange.
     *
     * @param mapTypeId The id of the new map type.
     */
    void mapTypeChanged(MapTypeId mapTypeId);
}
