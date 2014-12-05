package com.vaadin.tapio.googlemaps.client.events;

import java.io.Serializable;

import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

/**
 * Interface for listening marker drag events.
 */
public interface MarkerDragListener extends Serializable {
    /**
     * Handle a MarkerDragEvent.
     * 
     * @param draggedMarker
     *            The marker that was dragged with position updated.
     * @param oldPosition
     *            The old position of the marker.
     */
    public void markerDragged(GoogleMapMarker draggedMarker, LatLon oldPosition);
}
