package com.vaadin.tapio.googlemaps.client.overlays;

import java.io.Serializable;

import com.vaadin.tapio.googlemaps.client.LatLon;

/**
 * A class representing a pop-up window with HTML contents. They are often
 * anchored to
 * {@link com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker markers}.
 */
public class GoogleMapInfoWindow implements Serializable {

    private static final long serialVersionUID = 646386543641L;

    private static long idCounter = 0;

    private long id;

    private String content = null;
    private boolean autoPanDisabled = false;
    private Integer maxWidth = null;
    private Integer pixelOffsetWidth = null;
    private Integer pixelOffsetHeight = null;
    private Integer zIndex = null;
    private LatLon position = null;
    private GoogleMapMarker anchorMarker = null;
    private String width = null;
    private String height = null;

    /**
     * Instantiates a new info window.
     */
    public GoogleMapInfoWindow() {
        id = idCounter;
        idCounter++;
    }

    /**
     * Instantiates a new info window with the given content.
     * 
     * @param content
     *            The content in HTML.
     */
    public GoogleMapInfoWindow(String content) {
        this();
        this.content = content;
    }

    /**
     * Instantiates a new info window with given content and anchored to a
     * marker.
     * 
     * @param content
     *            The content in HTML.
     * @param anchorMarker
     *            The marker in which the window will be anchored.
     */
    public GoogleMapInfoWindow(String content, GoogleMapMarker anchorMarker) {
        this(content);
        this.anchorMarker = anchorMarker;
    }

    /**
     * Returns the content of the window.
     * 
     * @return The content of the window.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the window.
     * 
     * @param content
     *            The new content in HTML.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Checks if auto pan is disabled.
     * 
     * @return true, if auto pan is disabled
     */
    public boolean isAutoPanDisabled() {
        return autoPanDisabled;
    }

    /**
     * Enables/disables auto pan.
     * 
     * @param autoPanDisabled
     *            Set true to disable auto pan.
     */
    public void setAutoPanDisabled(boolean autoPanDisabled) {
        this.autoPanDisabled = autoPanDisabled;
    }

    /**
     * Returns the maximum width of the window.
     * 
     * @return The maximum width of the window.
     */
    public Integer getMaxWidth() {
        return maxWidth;
    }

    /**
     * Sets the maximum width of the window.
     * 
     * @param maxWidth
     *            The new maximum width in pixels.
     */
    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * Returns width of the pixel offset of the window.
     * 
     * @return The width of the pixel offset.
     */
    public Integer getPixelOffsetWidth() {
        return pixelOffsetWidth;
    }

    /**
     * Sets width of the pixel offset of the window.
     * 
     * @param pixelOffsetWidth
     *            The new width of the pixel offset.
     */
    public void setPixelOffsetWidth(Integer pixelOffsetWidth) {
        this.pixelOffsetWidth = pixelOffsetWidth;
    }

    /**
     * Returns the height of the pixel offset of the window.
     * 
     * @return The height of the pixel offset.
     */
    public Integer getPixelOffsetHeight() {
        return pixelOffsetHeight;
    }

    /**
     * Sets the height of the pixel offset of the window.
     * 
     * @param pixelOffsetHeight
     *            The new height of the pixel offset.
     */
    public void setPixelOffsetHeight(Integer pixelOffsetHeight) {
        this.pixelOffsetHeight = pixelOffsetHeight;
    }

    /**
     * Returns the z index of the window.
     * 
     * @return The z index.
     */
    public Integer getzIndex() {
        return zIndex;
    }

    /**
     * Sets the z index of the window.
     * 
     * @param zIndex
     *            The new z index.
     */
    public void setzIndex(Integer zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * Returns the position of the window.
     * 
     * @return The position of the window.
     */
    public LatLon getPosition() {
        return position;
    }

    /**
     * Sets the position of the window.
     * 
     * @param position
     *            The new position.
     */
    public void setPosition(LatLon position) {
        this.position = position;
    }

    /**
     * Returns the marker in which the window has been anchored.
     * 
     * @return The anchor marker or null if the window has not been anchored
     */
    public GoogleMapMarker getAnchorMarker() {
        return anchorMarker;
    }

    /**
     * Sets the marker in which the window should be anchored.
     * 
     * @param anchorMarker
     *            The new anchor marker.
     */
    public void setAnchorMarker(GoogleMapMarker anchorMarker) {
        this.anchorMarker = anchorMarker;
    }

    /**
     * Returns the current width for the contens of the info window.
     * 
     * @return The width as a CSS string or null if the width should be
     *         calculated automatically.
     */
    public String getWidth() {
        return width;
    }

    /**
     * Sets the width of the contents of the info window.
     * 
     * @param width
     *            The wanted width as CSS string. Set to null to if the width
     *            should be calculated automatically (default null).
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * Returns the current height for the contens of the info window.
     * 
     * @return The height as a CSS string or null if the width should be
     *         calculated automatically.
     */
    public String getHeight() {
        return height;
    }

    /**
     * Sets the height of the contents of the info window.
     * 
     * @param height
     *            The wanted height as CSS string. Set to null to if the height
     *            should be calculated automatically (default null).
     */
    public void setHeight(String height) {
        this.height = height;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        GoogleMapInfoWindow other = (GoogleMapInfoWindow) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

}
