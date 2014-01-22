package com.vaadin.tapio.googlemaps.client;

import java.io.Serializable;

/**
 * The class representing a marker of the Google Map.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
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
    
    private GoogleMapInfoWindow window=null;

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

    public GoogleMapInfoWindow infoWindow() {
		return window;
	}

	public void infoWindow(GoogleMapInfoWindow window) {
		if (this == window.getAnchorMarker()){  // method should be called only by GoogleMapInfoWindow
		    this.window = window;               // constructor
		}
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
        if ((other.getCaption() != null || getCaption() != null)
                && !other.getCaption().equals(getCaption())) {
            return false;
        }
        if ((other.getIconUrl() != null || getIconUrl() != null)
                && !other.getIconUrl().equals(getIconUrl())) {
            return false;
        }
        if (!other.getPosition().equals(getPosition())) {
            return false;
        }
        if (other.isAnimationEnabled() != isAnimationEnabled()) {
            return false;
        }
        if (other.isDraggable() != isDraggable()) {
            return false;
        }
        if (other.isOptimized() != isOptimized()) {
            return false;
        }
        return true;
    }

}
