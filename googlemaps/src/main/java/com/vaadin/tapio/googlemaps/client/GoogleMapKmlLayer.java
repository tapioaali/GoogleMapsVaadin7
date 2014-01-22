package com.vaadin.tapio.googlemaps.client;

import java.io.Serializable;

public class GoogleMapKmlLayer implements Serializable {
 	private static final long serialVersionUID = 7426132367355158931L;

	private static long idCounter = 0;

    private long id;

	private String url=null;

	private boolean clickable = true;
	
	private boolean preserveViewport = false;
	
	private boolean suppressInfoWindows = false;

	/**
     * Instantiates a new GoogleMapKmlLayer.
     */
    public GoogleMapKmlLayer() {
        id = idCounter;
        idCounter++;
    }

    /**
     * Instantiates a new GoogleMapKmlLayer
     * 
     * @param url
     *            The kmlLayer url.
     */
    public GoogleMapKmlLayer(String url) {
        this();
        this.url = url;
    }

    /**
     * Instantiates a new GoogleMapKmlLayer
     * 
     * @param url
     *            The kmlLayer url.
     * @param clickable
     *            Defines if kmlLayer is clickable.
     */
    public GoogleMapKmlLayer(String url, boolean clickable) {
        this(url);
        this.clickable = clickable;
    }
    
    /**
     * Instantiates a new GoogleMapKmlLayer
     * 
     * @param url
     *            The kmlLayer url.
     * @param clickable
     *            Defines if kmlLayer is clickable.
     * @param preserveViewport
     *            Specifies if the map should be adjusted to the bounds of the KmlLayer's contents 
     *            when showing the layer.
     */
    public GoogleMapKmlLayer(String url, boolean clickable, boolean preserveViewport) {
        this(url, clickable);
        this.preserveViewport = preserveViewport;
    }

    /**
     * Instantiates a new GoogleMapKmlLayer
     * 
     * @param url
     *            The kmlLayer url.
     * @param clickable
     *            Defines if kmlLayer is clickable.
     * @param preserveViewport
     *            Specifies if the map should be adjusted to the bounds of the KmlLayer's contents 
     *            when showing the layer.
     * @param suppressInfoWindows
     *            Indicates if clickable features within the KmlLayer should trigger the display of 
     *            InfoWindow objects.
     */
    public GoogleMapKmlLayer(String url, 
    		                  boolean clickable, 
    		                  boolean preserveViewport, boolean suppressInfoWindows) {
        this(url, clickable, preserveViewport);
        this.suppressInfoWindows = suppressInfoWindows;
    }
    
    public String getUrl(){
		return url;
	}
	public void setUrl(String url){
		this.url=url;		
	}
	
	public boolean isClickable() {
		return clickable;
	}

	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}

	public boolean isPreserveViewport() {
		return preserveViewport;
	}

	public void setPreserveViewport(boolean preserveViewport) {
		this.preserveViewport = preserveViewport;
	}

	public boolean isSuppressInfoWindows() {
		return suppressInfoWindows;
	}

	public void setSuppressInfoWindows(boolean suppressInfoWindows) {
		this.suppressInfoWindows = suppressInfoWindows;
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
