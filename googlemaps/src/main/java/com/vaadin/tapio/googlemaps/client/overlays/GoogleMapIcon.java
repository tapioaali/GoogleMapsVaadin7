package com.vaadin.tapio.googlemaps.client.overlays;

import com.vaadin.tapio.googlemaps.client.Point;
import com.vaadin.tapio.googlemaps.client.Size;

import java.io.Serializable;

/**
 * Created by andyphillips404 on 6/8/15.
 */
public class GoogleMapIcon implements Serializable {
    private static final long serialVersionUID = 812554543243L;


    private Point origin;
    private Size size;
    private Size scaledSize;
    private String url;
    private Point anchor;

    public GoogleMapIcon(Point origin, Size size, Size scaledSize, String url, Point anchor) {
        this.origin = origin;
        this.size = size;
        this.scaledSize = scaledSize;
        this.url = url;
        this.anchor = anchor;
    }

    public GoogleMapIcon(String url) {
        this.url = url;
    }

    public GoogleMapIcon(String url, Point anchor) {
        this.url = url;
        this.anchor = anchor;
    }

    /**
     * Gets the position of the image within a sprite, if any. By default, the origin is located at the top left
     * corner of the image (0, 0).
     * @return the images's origin
     */
    public Point getOrigin() {
        return origin;
    }

    /**
     * Sets the position of the image within a sprite, if any. By default, the origin is located at the top left
     * corner of the image (0, 0).
     * @param origin the images's origin
     */
    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    /**
     * The display size of the sprite or image. When using sprites, you must specify the sprite size.
     * If the size is not provided, it will be set when the image loads.
     * @return the images's size
     */
    public Size getSize() {
        return size;
    }

    /**
     * The display size of the sprite or image. When using sprites, you must specify the sprite size.
     * If the size is not provided, it will be set when the image loads.
     * @param size the image's size
     */
    public void setSize(Size size) {
        this.size = size;
    }

    /**
     * The size of the entire image after scaling, if any. Use this property to stretch/shrink an image or a sprite.
     * @return the image's scaled size
     */
    public Size getScaledSize() {
        return scaledSize;
    }

    /**
     * The size of the entire image after scaling, if any. Use this property to stretch/shrink an image or a sprite.
     * @param scaledSize the image's scaled size
     */
    public void setScaledSize(Size scaledSize) {
        this.scaledSize = scaledSize;
    }

    /**
     * The URL of the image or sprite sheet.
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * The URL of the image or sprite sheet.
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the position at which to anchor an image in correspondence to the location of the marker on the map. By default,
     * the anchor is located along the center point of the bottom of the image.
     * @return the markers anchor position
     */
    public Point getAnchor() {
        return anchor;
    }

    /**
     * Sets the position at which to anchor an image in correspondence to the location of the marker on the map. By default,
     * the anchor is located along the center point of the bottom of the image.
     * @param anchor Anchor point
     */
    public void setAnchor(Point anchor) {
        this.anchor = anchor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoogleMapIcon that = (GoogleMapIcon) o;

        if (origin != null ? !origin.equals(that.origin) : that.origin != null) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;
        if (scaledSize != null ? !scaledSize.equals(that.scaledSize) : that.scaledSize != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        return !(anchor != null ? !anchor.equals(that.anchor) : that.anchor != null);

    }

    @Override
    public int hashCode() {
        int result = origin != null ? origin.hashCode() : 0;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (scaledSize != null ? scaledSize.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (anchor != null ? anchor.hashCode() : 0);
        return result;
    }
}
