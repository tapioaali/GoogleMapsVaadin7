package com.vaadin.tapio.googlemaps.client;

import java.io.Serializable;

/**
 * A class representing a pop-up window with HTML contents. They are often
 * anchored to {@link GoogleMapMarker markers}.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 * 
 */
public class GoogleMapInfoWindow implements Serializable {

    private static final long serialVersionUID = 646386543641L;

    private String content = null;
    private boolean autoPanDisabled = false;
    private Integer maxWidth = null;
    private Integer pixelOffsetWidth = null;
    private Integer pixelOffsetHeight = null;
    private Integer zIndex = null;
    private LatLon position = null;
    private GoogleMapMarker anchorMarker = null;

    /**
     * Instantiates a new info window.
     */
    public GoogleMapInfoWindow() {

    }

    /**
     * Instantiates a new info window with the given content.
     * 
     * @param content
     *            The content in HTML.
     */
    public GoogleMapInfoWindow(String content) {
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
     * @return The anchor marker or null if the window has not been anchored.
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((anchorMarker == null) ? 0 : anchorMarker.hashCode());
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result
                + ((position == null) ? 0 : position.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        if (anchorMarker == null) {
            if (other.anchorMarker != null) {
                return false;
            }
        } else if (!anchorMarker.equals(other.anchorMarker)) {
            return false;
        }
        if (content == null) {
            if (other.content != null) {
                return false;
            }
        } else if (!content.equals(other.content)) {
            return false;
        }
        if (position == null) {
            if (other.position != null) {
                return false;
            }
        } else if (!position.equals(other.position)) {
            return false;
        }
        return true;
    }

}
