package com.vaadin.tapio.googlemaps.client;

import java.io.Serializable;

/**
 * The class representing a marker of the Google Map.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 */
public class GoogleMapMarker implements Serializable {
    private static final long serialVersionUID = 612346543243L;

    private LatLon position = new LatLon(0, 0);

    private String caption = "";

    private boolean draggable = false;

    /**
     * Instantiates a new GoogleMapMarker.
     */
    public GoogleMapMarker() {
    }

    /**
     * Instantiates a new GoogleMapMarker
     * 
     * @param caption
     *            The caption to use.
     * @param position
     *            The position of the marker
     * @param draggable
     *            Can marker be dragged?
     */
    public GoogleMapMarker(String caption, LatLon position, boolean draggable) {
        this.caption = caption;
        this.position = position;
        this.draggable = draggable;
    }

    /**
     * Returns the position of the marker.
     * 
     * @return The position of the marker.
     */
    public LatLon getPosition() {
        return position;
    }

    /**
     * Sets the position of the marker.
     * 
     * @param position
     *            The new position of the marker.
     */
    public void setPosition(LatLon position) {
        this.position = position;
    }

    /**
     * Gets the caption of the marker.
     * 
     * @return The caption of the marker.
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the caption of the marker.
     * 
     * @param caption
     *            The new caption of the marker.
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Checks if the marker is draggable.
     * 
     * @return true, if it is draggable
     */
    public boolean isDraggable() {
        return draggable;
    }

    /**
     * Enables/disables dragging of the marker.
     * 
     * @param draggable
     *            Set to true to enable dragging.
     */
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }
}
