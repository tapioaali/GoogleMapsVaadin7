package com.vaadin.tapio.googlemaps.client;

import java.io.Serializable;

public class GoogleMapKmlLayer implements Serializable {
 	private static final long serialVersionUID = 7426132367355158931L;

	private static long idCounter = 0;

    private long id;

	private String url=null;

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
	
	
	public String getUrl(){
		return url;
	}
	public void setUrl(String url){
		this.url=url;		
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
