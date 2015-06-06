package com.vaadin.tapio.googlemaps.client.overlays;

import java.io.Serializable;

import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.Point;

/**
 * The class representing a marker of the Google Map.
 */
public class GoogleMapMarker implements Serializable {
    private static final long serialVersionUID = 612346543243L;

    private static long idCounter = 0;

    private long id;

    private LatLon position = new LatLon(0, 0);

    private String caption = "";

    private boolean draggable = false;

    private String iconUrl = null;

    private boolean animationEnabled = true;

    private boolean optimized = true;

    private Integer zIndex = 0;

    private Point anchor = null;
    /**
     * Instantiates a new GoogleMapMarker.
     */
    public GoogleMapMarker() {
        id = idCounter;
        idCounter++;
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
        this();
        this.caption = caption;
        this.position = position;
        this.draggable = draggable;
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
    public GoogleMapMarker(String caption, LatLon position, boolean draggable,
            String iconUrl) {
        this(caption, position, draggable);
        this.iconUrl = iconUrl;
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

    /**
     * Returns the url of the icon of the marker.
     * 
     * @return the url of the icon, default null.
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * Sets the url of the icon of the marker.
     * 
     * @param iconUrl
     *            The new url of the icon.
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     * Checks if marker animation is enabled.
     * 
     * @return true, if enabled
     */
    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    /**
     * Enables/disables marker animation.
     * 
     * @param animationEnabled
     *            Set true to enable (default true).
     */
    public void setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
    }

    /**
     * Checks if optimization is enabled.
     * 
     * @return true, if enabled
     */
    public boolean isOptimized() {
        return optimized;
    }

    /**
     * Enables/disables marker optimization. If enabled, many markers are
     * rendered as a single static element. Disable if you want to use animated
     * GIFs or PNGs.
     * 
     * @param optimized
     *            Set true to enable (default true).
     */
    public void setOptimized(boolean optimized) {
        this.optimized = optimized;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    /**
     * All markers are displayed on the map in order of their zIndex, with higher values displaying in front of markers
     * with lower values. By default, markers are displayed according to their vertical position on screen, with lower
     * markers appearing in front of markers further up the screen.
     * @return Z Index of the marker
     */
    public Integer getZIndex() {
        return zIndex;
    }

    /**
     * All markers are displayed on the map in order of their zIndex, with higher values displaying in front of markers
     * with lower values. By default, markers are displayed according to their vertical position on screen, with lower
     * markers appearing in front of markers further up the screen.
     * @param zIndex Z index of the marker
     */
    public void setZIndex(Integer zIndex) {
        this.zIndex = zIndex;
    }


    /**
     * Sets the position at which to anchor an image in correspondence to the location of the marker on the map. By default,
     * the anchor is located along the center point of the bottom of the image.
     * @return the anchor point of the custom marker image, in pixels
     */
    public Point getAnchor() {
        return anchor;
    }

    /**
     * Sets the position at which to anchor an image in correspondence to the location of the marker on the map. By default,
     * the anchor is located along the center point of the bottom of the image.
     * @param anchor the point to anchor on the custom marker image, in pixels.
     */
    public void setAnchor(Point anchor) {
        this.anchor = anchor;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GoogleMapMarker other = (GoogleMapMarker) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    public boolean hasSameFieldValues(GoogleMapMarker other) {

        if (position != null ? !position.equals(other.position) : other.position != null) return false;
        if (caption != null ? !caption.equals(other.caption) : other.caption != null) return false;
        if (iconUrl != null ? !iconUrl.equals(other.iconUrl) : other.iconUrl != null) return false;
        if (zIndex != null ? !zIndex.equals(other.zIndex) : other.zIndex != null) return false;
        if (anchor != null ? !anchor.equals(other.anchor) : other.anchor != null) return false;
        if (draggable != other.draggable) return false;
        if (animationEnabled != other.animationEnabled) return false;
        if (optimized != other.optimized) return false;

        return true;
    }

}
