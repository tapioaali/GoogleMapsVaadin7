package com.vaadin.tapio.googlemaps.client;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The shared state of the Google Maps. Contains also the default
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 */
public class GoogleMapState extends com.vaadin.shared.AbstractComponentState {
    public String apiKey = null;

    public String mapTypeId = "Roadmap";
    public LatLon center = new LatLon(51.477811, -0.001475);
    public double zoom = 8.0;
    public boolean draggable = true;
    public boolean keyboardShortcutsEnabled = true;
    public boolean scrollWheelEnabled = true;

    public Set<GoogleMapControl> controls = new HashSet<GoogleMapControl>(
            Arrays.asList(GoogleMapControl.MapType, GoogleMapControl.Pan,
                    GoogleMapControl.Rotate, GoogleMapControl.Scale,
                    GoogleMapControl.StreetView, GoogleMapControl.Zoom));
    public boolean locationFromClient = false;

    public boolean limitCenterBounds = false;
    public LatLon centerSWLimit = new LatLon(0.0, 0.0);
    public LatLon centerNELimit = new LatLon(0.0, 0.0);
    public Set<GoogleMapPolygon> polygons = new HashSet<GoogleMapPolygon>();
    public Set<GoogleMapPolyline> polylines = new HashSet<GoogleMapPolyline>();
    public Set<GoogleMapMarker> markers = new HashSet<GoogleMapMarker>();
}