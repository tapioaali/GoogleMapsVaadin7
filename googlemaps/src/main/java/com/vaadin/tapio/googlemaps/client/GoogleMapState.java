package com.vaadin.tapio.googlemaps.client;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.shared.AbstractComponentState;

/**
 * The shared state of the Google Maps. Contains also the default
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 */
public class GoogleMapState extends AbstractComponentState {
    private static final long serialVersionUID = 646346522643L;

    public String apiKey = null;

    // defaults to the language setting of the browser
    public String language = null;
    public String mapTypeId = "Roadmap";
    public LatLon center = new LatLon(51.477811, -0.001475);
    public double zoom = 8.0;
    public double maxZoom = 21.0;
    public double minZoom = 0.0;

    public boolean draggable = true;
    public boolean keyboardShortcutsEnabled = true;
    public boolean scrollWheelEnabled = true;

    public boolean visualRefreshEnabled = false;

    public Set<GoogleMapControl> controls = new HashSet<GoogleMapControl>(
            Arrays.asList(GoogleMapControl.MapType, GoogleMapControl.Pan,
                    GoogleMapControl.Rotate, GoogleMapControl.Scale,
                    GoogleMapControl.StreetView, GoogleMapControl.Zoom));
    public boolean locationFromClient = false;

    public boolean limitCenterBounds = false;
    public LatLon centerSWLimit = new LatLon(0.0, 0.0);
    public LatLon centerNELimit = new LatLon(0.0, 0.0);

    public boolean limitVisibleAreaBounds = false;
    public LatLon visibleAreaSWLimit = new LatLon(0.0, 0.0);
    public LatLon visibleAreaNELimit = new LatLon(0.0, 0.0);

    public Set<GoogleMapPolygon> polygons = new HashSet<GoogleMapPolygon>();
    public Set<GoogleMapPolyline> polylines = new HashSet<GoogleMapPolyline>();
    public Set<GoogleMapMarker> markers = new HashSet<GoogleMapMarker>();

    public Set<GoogleMapInfoWindow> infoWindows = new HashSet<GoogleMapInfoWindow>();
}