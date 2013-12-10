package com.vaadin.tapio.googlemaps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.vaadin.tapio.googlemaps.client.GoogleMapControl;
import com.vaadin.tapio.googlemaps.client.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.GoogleMapInfoWindowClosedRpc;
import com.vaadin.tapio.googlemaps.client.GoogleMapKmlLayer;
import com.vaadin.tapio.googlemaps.client.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.GoogleMapMarkerClickedRpc;
import com.vaadin.tapio.googlemaps.client.GoogleMapMarkerDraggedRpc;
import com.vaadin.tapio.googlemaps.client.GoogleMapMovedRpc;
import com.vaadin.tapio.googlemaps.client.GoogleMapPolygon;
import com.vaadin.tapio.googlemaps.client.GoogleMapPolyline;
import com.vaadin.tapio.googlemaps.client.GoogleMapState;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.InfoWindowClosedListener;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;

/**
 * The class representing Google Maps.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 */
public class GoogleMap extends com.vaadin.ui.AbstractComponent {

    /**
     * Base map types supported by Google Maps.
     */
    public enum MapType {
        Hybrid, Roadmap, Satellite, Terrain
    }

    private GoogleMapMarkerClickedRpc markerClickedRpc = new GoogleMapMarkerClickedRpc() {
        @Override
        public void markerClicked(long markerId) {

            GoogleMapMarker marker = getState().markers.get(markerId);
            for (MarkerClickListener listener : markerClickListeners) {
                listener.markerClicked(marker);
            }
        }
    };

    private GoogleMapMarkerDraggedRpc markerDraggedRpc = new GoogleMapMarkerDraggedRpc() {
        @Override
        public void markerDragged(long markerId, LatLon newPosition) {
            GoogleMapMarker marker = getState().markers.get(markerId);
            LatLon oldPosition = marker.getPosition();
            marker.setPosition(newPosition);
            for (MarkerDragListener listener : markerDragListeners) {
                listener.markerDragged(marker, oldPosition);
            }
        }
    };

    private GoogleMapMovedRpc mapMovedRpc = new GoogleMapMovedRpc() {
        @Override
        public void mapMoved(double zoomLevel, LatLon center, LatLon boundsNE,
                LatLon boundsSW) {
            getState().locationFromClient = true;
            getState().zoom = zoomLevel;
            getState().center = center;
            fitToBounds(null, null);

            for (MapMoveListener listener : mapMoveListeners) {
                listener.mapMoved(zoomLevel, center, boundsNE, boundsSW);
            }

        }
    };

    private GoogleMapInfoWindowClosedRpc infoWindowClosedRpc = new GoogleMapInfoWindowClosedRpc() {

        @Override
        public void infoWindowClosed(long windowId) {
            GoogleMapInfoWindow window = getState().infoWindows.get(windowId);
            for (InfoWindowClosedListener listener : infoWindowClosedListeners) {
                listener.infoWindowClosed(window);
            }
            getState().infoWindows.remove(windowId);
        }
    };

    private List<MarkerClickListener> markerClickListeners = new ArrayList<MarkerClickListener>();

    private List<MapMoveListener> mapMoveListeners = new ArrayList<MapMoveListener>();

    private List<MarkerDragListener> markerDragListeners = new ArrayList<MarkerDragListener>();

    private List<InfoWindowClosedListener> infoWindowClosedListeners = new ArrayList<InfoWindowClosedListener>();

    /**
     * Initiates a new GoogleMap object with default settings from the
     * {@link GoogleMapState state object}.
     * 
     * @param apiKeyOrClientId
     *            The Maps API key from Google or the client ID for the
     *            Business API. All client IDs begin with a gme- prefix.
     *            Not required when developing in localhost.
     */
    public GoogleMap(String apiKeyOrClientId) {
    	if (isClientId(apiKeyOrClientId)) {
    		getState().clientId = apiKeyOrClientId;
    	} else {
    		getState().apiKey = apiKeyOrClientId;
    	}
        registerRpc(markerClickedRpc);
        registerRpc(mapMovedRpc);
        registerRpc(markerDraggedRpc);
        registerRpc(infoWindowClosedRpc);
    }

    /**
     * Creates a new GoogleMap object with the given center. Other settings will
     * be {@link GoogleMapState defaults of the state object}.
     * 
     * @param center
     *            Coordinates of the center.
     * @param apiKeyOrClientId
     *            The Maps API key from Google or the client ID for the
     *            Business API. All client IDs begin with a gme- prefix.
     *            Not required when developing in localhost.
     */
    public GoogleMap(LatLon center, String apiKeyOrClientId) {
        this(apiKeyOrClientId);
        getState().center = center;
    }

    /**
     * Creates a new GoogleMap object with the given center and zoom. Other
     * settings will be {@link GoogleMapState defaults of the state object}.
     * 
     * @param center
     *            Coordinates of the center.
     * @param zoom
     *            Amount of zoom.
     * @param apiKeyOrClientId
     *            The Maps API key from Google or the client ID for the
     *            Business API. All client IDs begin with a gme- prefix.
     *            Not required when developing in localhost.
     */
    public GoogleMap(LatLon center, double zoom, String apiKeyOrClientId) {
        this(apiKeyOrClientId);
        getState().zoom = zoom;
        getState().center = center;
    }

    /**
     * Creates a new GoogleMap object with the given center, zoom and language.
     * Other settings will be {@link GoogleMapState defaults of the state
     * object}.
     * 
     * @param center
     *            Coordinates of the center.
     * @param zoom
     *            Amount of zoom.
     * @param apiKeyOrClientId
     *            The Maps API key from Google or the client ID for the
     *            Business API. All client IDs begin with a gme- prefix.
     *            Not required when developing in localhost.
     * @param language
     *            The language to use with maps. See
     *            https://developers.google.com/maps/faq#languagesupport for the
     *            list of the supported languages.
     */
    public GoogleMap(LatLon center, double zoom, String apiKeyOrClientId, String language) {
        this(apiKeyOrClientId);
        getState().zoom = zoom;
        getState().center = center;
        getState().language = language;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.AbstractComponent#getState()
     */
    @Override
    protected GoogleMapState getState() {
        return (GoogleMapState) super.getState();
    }

    /**
     * Sets the center of the map to the given coordinates.
     * 
     * @param center
     *            The new coordinates of the center.
     */
    public void setCenter(LatLon center) {
        getState().locationFromClient = false;
        getState().center = center;
    }

    /**
     * Returns the current position of the center of the map.
     * 
     * @return Coordinates of the center.
     */
    public LatLon getCenter() {
        return getState().center;
    }

    /**
     * Zooms the map to the given value.
     * 
     * @param zoom
     *            New amount of the zoom.
     */
    public void setZoom(double zoom) {
        getState().locationFromClient = false;
        getState().zoom = zoom;
    }

    /**
     * Returns the current zoom of the map.
     * 
     * @return Current value of the zoom.
     */
    public double getZoom() {
        return getState().zoom;
    }

    /**
     * Adds a new marker to the map.
     * 
     * @param caption
     *            Caption of the marker shown when the marker is hovered.
     * @param position
     *            Coordinates of the marker on the map.
     * @param draggable
     *            Set true to enable dragging of the marker.
     * @param iconUrl
     *            The url of the icon of the marker.
     * @return GoogleMapMarker object created with the given settings.
     */
    public GoogleMapMarker addMarker(String caption, LatLon position,
            boolean draggable, String iconUrl) {
        GoogleMapMarker marker = new GoogleMapMarker(caption, position,
                draggable, iconUrl);
        getState().markers.put(marker.getId(), marker);
        return marker;
    }

    /**
     * Adds a marker to the map.
     * 
     * @param marker
     *            The marker to add.
     */
    public void addMarker(GoogleMapMarker marker) {
        getState().markers.put(marker.getId(), marker);
    }

    /**
     * Removes a marker from the map.
     * 
     * @param marker
     *            The marker to remove.
     */
    public void removeMarker(GoogleMapMarker marker) {
        getState().markers.remove(marker.getId());
    }

    /**
     * Removes all the markers from the map.
     */
    public void clearMarkers() {
        getState().markers.clear();
    }

    /**
     * Checks if a marker has been added to the map.
     * 
     * @param marker
     *            The marker to check.
     * @return true, if the marker has been added to the map.
     */
    public boolean hasMarker(GoogleMapMarker marker) {
        return getState().markers.containsKey(marker.getId());
    }

    /**
     * Returns the markers that have been added to he map.
     * 
     * @return Set of the markers.
     */
    public Collection<GoogleMapMarker> getMarkers() {
        return getState().markers.values();
    }

    /**
     * Adds a MarkerClickListener to the map.
     * 
     * @param listener
     *            The listener to add.
     */
    public void addMarkerClickListener(MarkerClickListener listener) {
        markerClickListeners.add(listener);
    }

    /**
     * Removes a MarkerClickListener from the map.
     * 
     * @param listener
     *            The listener to remove.
     */
    public void removeMarkerClickListener(MarkerClickListener listener) {
        markerClickListeners.remove(listener);
    }

    /**
     * Adds a MarkerDragListener to the map.
     * 
     * @param listener
     *            The listener to add.
     */
    public void addMarkerDragListener(MarkerDragListener listener) {
        markerDragListeners.add(listener);
    }

    /**
     * Removes a MarkerDragListenr from the map.
     * 
     * @param listener
     *            The listener to remove.
     */
    public void removeMarkerDragListener(MarkerDragListener listener) {
        markerDragListeners.remove(listener);
    }

    /**
     * Adds a MapMoveListener to the map.
     * 
     * @param listener
     *            The listener to add.
     */
    public void addMapMoveListener(MapMoveListener listener) {
        mapMoveListeners.add(listener);
    }

    /**
     * Removes a MapMoveListener from the map.
     * 
     * @param listener
     *            The listener to add.
     */
    public void removeMapMoveListener(MapMoveListener listener) {
        mapMoveListeners.remove(listener);
    }

    /**
     * Adds an InfoWindowClosedListener to the map.
     * 
     * @param listener
     *            The listener to add.
     */
    public void addInfoWindowClosedListener(InfoWindowClosedListener listener) {
        infoWindowClosedListeners.add(listener);
    }

    /**
     * Removes an InfoWindowClosedListener from the map.
     * 
     * @param listener
     *            The listener to remove.
     */
    public void removeInfoWindowClosedListener(InfoWindowClosedListener listener) {
        infoWindowClosedListeners.remove(listener);
    }

    /**
     * Checks if limiting of the center bounds is enabled.
     * 
     * @return true, if enabled
     */
    public boolean isCenterBoundLimitsEnabled() {
        return getState().limitCenterBounds;
    }

    /**
     * Enables/disables limiting of the center bounds.
     * 
     * @param enable
     *            Set true to enable the limiting.
     */
    public void setCenterBoundLimitsEnabled(boolean enable) {
        getState().limitCenterBounds = enable;
    }

    /**
     * Sets the limits of the bounds of the center to given values.
     * 
     * @param limitNE
     *            The coordinates of the northeast limit.
     * @param limitSW
     *            The coordinates of the southwest limit.
     */
    public void setCenterBoundLimits(LatLon limitNE, LatLon limitSW) {
        getState().centerNELimit = limitNE;
        getState().centerSWLimit = limitSW;
        getState().limitCenterBounds = true;
    }

    /**
     * Adds a polygon overlay to the map.
     * 
     * @param polygon
     *            The GoogleMapPolygon to add.
     */
    public void addPolygonOverlay(GoogleMapPolygon polygon) {
        getState().polygons.add(polygon);
    }

    /**
     * Removes a polygon overlay from the map.
     * 
     * @param polygon
     *            The GoogleMapPolygon to remove.
     */
    public void removePolygonOverlay(GoogleMapPolygon polygon) {
        getState().polygons.remove(polygon);
    }

    /**
     * Adds a polyline to the map.
     * 
     * @param polyline
     *            The GoogleMapPolyline to add.
     */
    public void addPolyline(GoogleMapPolyline polyline) {
        getState().polylines.add(polyline);
    }

    /**
     * Removes a polyline from the map.
     * 
     * @param polyline
     *            The GoogleMapPolyline to add.
     */
    public void removePolyline(GoogleMapPolyline polyline) {
        getState().polylines.remove(polyline);
    }
    /**
     * Adds a kmlLayer to the map.
     * 
     * @param kmlLayer
     *            The GoogleMapKmlLayer to add.
     */
    public void addKmlLayer(GoogleMapKmlLayer kmlLayer) {
        getState().kmlLayers.add(kmlLayer);
    }

    /**
     * Adds a kmlLayer to the map.
     * 
     * @param kmlLayerUrl
     *            Url of the kmlLayer to add.
     */
    public void addKmlLayer(String kmlLayerUrl) {
    	GoogleMapKmlLayer kmlLayer = new GoogleMapKmlLayer(kmlLayerUrl);
        getState().kmlLayers.add(kmlLayer);
    }
    
    /**
     * Removes a kmlLayer from the map.
     * 
     * @param kmlLayer
     *            The GoogleMapKmlLayer to remove.
     */
    public void removeKmlLayer(GoogleMapKmlLayer kmlLayer) {
        getState().kmlLayers.remove(kmlLayer);
    }
 
    /**
     * Sets the type of the base map.
     * 
     * @param type
     *            The new MapType to use.
     */
    public void setMapType(MapType type) {
        getState().mapTypeId = type.name();
    }

    /**
     * Returns the current type of the base map.
     * 
     * @return The current MapType.
     */
    public MapType getMapType() {
        return MapType.valueOf(getState().mapTypeId);
    }

    /**
     * Checks if the map is currently draggable.
     * 
     * @return true, if the map draggable.
     */
    public boolean isDraggable() {
        return getState().draggable;
    }

    /**
     * Enables/disables dragging of the map.
     * 
     * @param draggable
     *            Set to true to enable dragging.
     */
    public void setDraggable(boolean draggable) {
        getState().draggable = draggable;
    }

    /**
     * Checks if the keyboard shortcuts are enabled.
     * 
     * @return true, if the shortcuts are enabled.
     */
    public boolean areKeyboardShortcutsEnabled() {
        return getState().keyboardShortcutsEnabled;
    }

    /**
     * Enables/disables the keyboard shortcuts.
     * 
     * @param enabled
     *            Set true to enable keyboard shortcuts.
     */
    public void setKeyboardShortcutsEnabled(boolean enabled) {
        getState().keyboardShortcutsEnabled = enabled;
    }

    /**
     * Checks if the scroll wheel is enabled.
     * 
     * @return true, if the scroll wheel is enabled
     */
    public boolean isScrollWheelEnabled() {
        return getState().scrollWheelEnabled;
    }

    /**
     * Enables/disables the scroll wheel.
     * 
     * @param enabled
     *            Set true to enable scroll wheel.
     */
    public void setScrollWheelEnabled(boolean enabled) {
        getState().scrollWheelEnabled = enabled;
    }

    /**
     * Returns the currently enabled map controls.
     * 
     * @return Currently enabled map controls.
     */
    public Set<GoogleMapControl> getControls() {
        return getState().controls;
    }

    /**
     * Sets the controls of the map.
     * 
     * @param controls
     *            The new controls to use.
     */
    public void setControls(Set<GoogleMapControl> controls) {
        getState().controls = controls;
    }

    /**
     * Enables the given control on the map. Does nothing if the control is
     * already enabled.
     * 
     * @param control
     *            The control to enable.
     */
    public void addControl(GoogleMapControl control) {
        getState().controls.add(control);
    }

    /**
     * Removes the control from the map. Does nothing if the control isn't
     * enabled.
     * 
     * @param control
     *            The control to remove.
     */
    public void removeControl(GoogleMapControl control) {
        getState().controls.remove(control);
    }

    /**
     * Enables/disables limiting of the bounds of the visible area.
     * 
     * @param enabled
     *            Set true to enable the limiting.
     */
    public void setVisibleAreaBoundLimitsEnabled(boolean enabled) {
        getState().limitVisibleAreaBounds = enabled;

    }

    /**
     * Checks if limiting of the bounds of the visible area is enabled.
     * 
     * @return true if enabled
     */
    public boolean isVisibleAreaBoundLimitsEnabled() {
        return getState().limitVisibleAreaBounds;
    }

    /**
     * Sets the limits of the bounds of the visible area to the given values.
     * NOTE: Using the feature does not affect zooming, consider using
     * {@link #setMinZoom(double)} too.
     * 
     * @param limitNE
     *            The coordinates of the northeast limit.
     * @param limitSW
     *            The coordinates of the southwest limit.
     */
    public void setVisibleAreaBoundLimits(LatLon limitNE, LatLon limitSW) {
        getState().visibleAreaNELimit = limitNE;
        getState().visibleAreaSWLimit = limitSW;
        getState().limitVisibleAreaBounds = true;
    }

    /**
     * Sets the maximum allowed amount of zoom (default 21.0).
     * 
     * @param maxZoom
     *            The maximum amount for zoom.
     */
    public void setMaxZoom(double maxZoom) {
        getState().maxZoom = maxZoom;
    }

    /**
     * Returns the current maximum amount of zoom.
     * 
     * @return maximum amount of zoom
     */
    public double getMaxZoom() {
        return getState().maxZoom;
    }

    /**
     * Sets the minimum allowed amount of zoom (default 0.0).
     * 
     * @param minZoom
     *            The minimum amount for zoom.
     */
    public void setMinZoom(double minZoom) {
        getState().minZoom = minZoom;
    }

    /**
     * Returns the current minimum amount of zoom.
     * 
     * @return minimum amount of zoom
     */
    public double getMinZoom() {
        return getState().minZoom;
    }

    /**
     * Opens an info window.
     * 
     * @param infoWindow
     *            The window to open.
     */
    public void openInfoWindow(GoogleMapInfoWindow infoWindow) {
        getState().infoWindows.put(infoWindow.getId(), infoWindow);
    }

    /**
     * Closes an info window.
     * 
     * @param infoWindow
     *            The window to close.
     */
    public void closeInfoWindow(GoogleMapInfoWindow infoWindow) {
        getState().infoWindows.remove(infoWindow.getId());
    }

    /**
     * Checks if an info window is open.
     * 
     * @param infoWindow
     *            The window to check.
     * @return true, if the window is open.
     */
    public boolean isInfoWindowOpen(GoogleMapInfoWindow infoWindow) {
        return getState().infoWindows.containsKey(infoWindow.getId());
    }

    /**
     * Enables/disables new visual style of the map. NOTICE: this must be set
     * before rendering the map.
     * 
     * @param enabled
     *            Set true to enable (defaul false).
     */
    public void setVisualRefreshEnabled(boolean enabled) {
        getState().visualRefreshEnabled = enabled;
    }

    /**
     * Checks if the new visual style is enabled.
     * 
     * @return true, if visual refresh is enabled
     */
    public boolean isVisualRefreshEnabled() {
        return getState().visualRefreshEnabled;
    }

    /**
     * Tries to fit the visible area of the map inside given boundaries by
     * modifying zoom and/or center.
     * 
     * @param boundsNE
     *            The northeast boundaries.
     * @param boundsSW
     *            The southwest boundaries.
     */
    public void fitToBounds(LatLon boundsNE, LatLon boundsSW) {
        getState().fitToBoundsNE = boundsNE;
        getState().fitToBoundsSW = boundsSW;
    }
    
    private boolean isClientId(String apiKeyOrClientId) {
    	return apiKeyOrClientId != null && apiKeyOrClientId.startsWith("gme-");
    }
}
