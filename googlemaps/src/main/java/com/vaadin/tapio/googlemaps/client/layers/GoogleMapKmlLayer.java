package com.vaadin.tapio.googlemaps.client.layers;

import java.io.Serializable;

public class GoogleMapKmlLayer implements Serializable {
    private static final long serialVersionUID = 7426132367355158931L;

    private static long idCounter = 0;

    private long id;

    private String url = null;

    private boolean clickable = true;

    private boolean viewportPreserved = false;

    private boolean infoWindowRenderingDisabled = false;

    /**
     * Instantiates a new GoogleMapKmlLayer.
     */
    public GoogleMapKmlLayer() {
        id = idCounter;
        idCounter++;
    }

    /**
     * Instantiates a new GoogleMapKmlLayer with given url.
     * 
     * @param url
     *            The URL of the KML file being displayed.
     */
    public GoogleMapKmlLayer(String url) {
        this();
        this.url = url;
    }

    /**
     * Instantiates a new GoogleMapKmlLayer with given URL and other settings.
     * 
     * @param url
     *            The URL of the KML file being displayed.
     * @param clickable
     *            Defines if the KML Layer is clickable (default true).
     * @param viewportPreserved
     *            Specifies if the map should be adjusted to the bounds of the
     *            KML layer when the layer is added to the map (default false).
     * @param infoWindowRenderingDisabled
     *            Enables/disables rendering of info windows when layer features
     *            are clicked.
     */
    public GoogleMapKmlLayer(String url, boolean clickable,
            boolean viewportPreserved, boolean infoWindowRenderingDisabled) {
        this(url);
        this.clickable = clickable;
        this.viewportPreserved = viewportPreserved;
        this.infoWindowRenderingDisabled = infoWindowRenderingDisabled;
    }

    /**
     * Returns the URL of the KML file being displayed.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL of the KML file being displayed.
     * 
     * @param url
     *            The URL to display.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns whether the layer is clickable or not.
     * 
     * @return true, if clickable
     */
    public boolean isClickable() {
        return clickable;
    }

    /**
     * Sets whether the layer should be clickable or not.
     * 
     * @param clickable
     *            Set to false to disable clickability (default true).
     */
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    /**
     * Checks if the viewport is adjusted to the bounds of the layer when added
     * to the map.
     * 
     * @return true, if the viewport is not adjusted
     */
    public boolean isViewportPreserved() {
        return viewportPreserved;
    }

    /**
     * Enables/disables adjusting of the viewport to the bounds of the layer
     * when added to the map.
     * 
     * @param viewportPreserved
     *            Set to true to disable adjusting of the viewport (default
     *            false).
     */
    public void setViewportPreserved(boolean viewportPreserved) {
        this.viewportPreserved = viewportPreserved;
    }

    /**
     * Checks if rendering of info windows is suppressed when layer features are
     * clicked.
     * 
     * @return true, if disabled
     */
    public boolean isInfoWindowRenderingDisabled() {
        return infoWindowRenderingDisabled;
    }

    /**
     * Enables/disables rendering of info windows when layer features are
     * clicked.
     * 
     * @param infoWindowRenderingDisabled
     *            Set to true to disable rendering (default false).
     */
    public void setInfoWindowRenderingDisabled(
            boolean infoWindowRenderingDisabled) {
        this.infoWindowRenderingDisabled = infoWindowRenderingDisabled;
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
        GoogleMapKmlLayer other = (GoogleMapKmlLayer) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}