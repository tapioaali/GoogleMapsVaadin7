package com.vaadin.tapio.googlemaps.client.overlays;

import java.io.Serializable;

import com.vaadin.tapio.googlemaps.client.LatLon;

/**
 * A class representing a circle overlay of Google Maps.
 * 
 * @author Daniel Rampanelli <hello@neuralquery.com>
 * 
 */
public class GoogleMapCircle implements Serializable {

	private static final long serialVersionUID = -265127681935439373L;

	private static long idCounter = 0;

    private long id;

    private LatLon center = new LatLon(0, 0);
    
    private int radius = 0;

    private String fillColor = "#ffffff";

    private double fillOpacity = 1.0;

    private String strokeColor = "#000000";

    private double strokeOpacity = 1.0;

    private int strokeWeight = 1;

    private int zIndex = 0;

    /**
     * Instantiates a new circle overlay using default values.
     */
    public GoogleMapCircle() {
        id = idCounter;
        idCounter++;
    }

    /**
     * Instantiates a new circle overlay with the given center coordinate and radius, using
     * defaults for other values.
     * 
     * @param center
     *            The center coordinate of the circle.
     * @param radius
     *            The radius of the circle (in meters).
     */
    public GoogleMapCircle(LatLon center, Integer radius) {
        this();
        this.center = center;
        this.radius = radius;
    }

    /**
     * Instantiates a new circle overlay using the given values.
     * 
     * @param center
     *            The center coordinate of the circle.
     * @param radius
     *            The radius of the circle (in meters).
     * @param fillColor
     *            The fill color of the circle.
     * @param fillOpacity
     *            The fill opacity of the circle.
     * @param strokeColor
     *            The stroke color of the circle.
     * @param strokeOpacity
     *            The stroke opacity of the circle.
     * @param strokeWeight
     *            The stroke weight of the circle.
     */
    public GoogleMapCircle(LatLon center, Integer radius, String fillColor,
            double fillOpacity, String strokeColor, double strokeOpacity,
            int strokeWeight) {
        this(center, radius);
        this.fillColor = fillColor;
        this.fillOpacity = fillOpacity;
        this.strokeColor = strokeColor;
        this.strokeOpacity = strokeOpacity;
        this.strokeWeight = strokeWeight;
    }

    /**
     * Returns the center coordinate of the circle.
     * 
     * @return the circle's center coordinate
     */
    public LatLon getCenter() {
		return center;
	}
    
    /**
     * Sets the circle's center coordinate.
     * 
     * @param center
     *            The new center coordinate.
     */
    public void setCenter(LatLon center) {
		this.center = center;
	}
    
    /**
     * Returns the radius of the circle.
     * 
     * @return the radius
     */
    public int getRadius() {
		return radius;
	}
    
    /**
     * Sets the radius (in meters) of the circle.
     * 
     * @param radius
     *            The new radius.
     */
    public void setRadius(int radius) {
		this.radius = radius;
	}

    /**
     * Returns the fill color of the circle.
     * 
     * @return the fill color
     */
    public String getFillColor() {
        return fillColor;
    }

    /**
     * Sets the fill color of the circle.
     * 
     * @param fillColor
     *            The new fill color.
     */
    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Returns the fill opacity of the circle.
     * 
     * @return the fill opacity
     */
    public double getFillOpacity() {
        return fillOpacity;
    }

    /**
     * Sets the fill opacity of the circle.
     * 
     * @param fillOpacity
     *            The new fill opacity.
     */
    public void setFillOpacity(double fillOpacity) {
        this.fillOpacity = fillOpacity;
    }

    /**
     * Returns the stroke color of the circle.
     * 
     * @return the stroke color
     */
    public String getStrokeColor() {
        return strokeColor;
    }

    /**
     * Sets the stroke color of the circle.
     * 
     * @param strokeColor
     *            The new stroke color.
     */
    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    /**
     * Returns the stroke opacity of the circle.
     * 
     * @return the stroke opacity
     */
    public double getStrokeOpacity() {
        return strokeOpacity;
    }

    /**
     * Sets the stroke opacity of the circle.
     * 
     * @param strokeOpacity
     *            The new stroke opacity.
     */
    public void setStrokeOpacity(double strokeOpacity) {
        this.strokeOpacity = strokeOpacity;
    }

    /**
     * Returns the stroke weight of the circle.
     * 
     * @return the stroke weight
     */
    public int getStrokeWeight() {
        return strokeWeight;
    }

    /**
     * Sets the stroke weight of the circle.
     * 
     * @param strokeWeight
     *            The new stroke weight.
     */
    public void setStrokeWeight(int strokeWeight) {
        this.strokeWeight = strokeWeight;
    }

    /**
     * Returns the z index compared to other circles.
     * 
     * @return the z index
     */
    public int getzIndex() {
        return zIndex;
    }

    /**
     * Sets the z index compared to other circles.
     * 
     * @param zIndex
     *            the new z index
     */
    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
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
        GoogleMapCircle other = (GoogleMapCircle) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}
