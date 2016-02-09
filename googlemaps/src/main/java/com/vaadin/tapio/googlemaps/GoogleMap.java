package com.vaadin.tapio.googlemaps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.vaadin.tapio.googlemaps.client.GoogleMapControl;
import com.vaadin.tapio.googlemaps.client.GoogleMapState;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.InfoWindowClosedListener;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;
import com.vaadin.tapio.googlemaps.client.layers.GoogleMapKmlLayer;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolygon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.tapio.googlemaps.client.rpcs.InfoWindowClosedRpc;
import com.vaadin.tapio.googlemaps.client.rpcs.MapClickedRpc;
import com.vaadin.tapio.googlemaps.client.rpcs.MapMovedRpc;
import com.vaadin.tapio.googlemaps.client.rpcs.MarkerClickedRpc;
import com.vaadin.tapio.googlemaps.client.rpcs.MarkerDraggedRpc;
import com.vaadin.ui.AbstractComponent;

/**
 * The class representing Google Maps.
 */
public class GoogleMap extends AbstractComponent {

    /**
     * Base map types supported by Google Maps.
     */
    public enum MapType {
        Hybrid, Roadmap, Satellite, Terrain
    }

    private final MarkerClickedRpc markerClickedRpc = new MarkerClickedRpc() {
        @Override
        public void markerClicked(long markerId) {

            GoogleMapMarker marker = getState().markers.get(markerId);
            for (MarkerClickListener listener : markerClickListeners) {
                listener.markerClicked(marker);
            }
        }
    };

    private final MarkerDraggedRpc markerDraggedRpc = new MarkerDraggedRpc() {
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

    private final MapMovedRpc mapMovedRpc = new MapMovedRpc() {
        @Override
        public void mapMoved(int zoomLevel, LatLon center, LatLon boundsNE,
                             LatLon boundsSW) {
            getState().zoom = zoomLevel;
            getState().center = center;
            fitToBounds(null, null);

            for (MapMoveListener listener : mapMoveListeners) {
                listener.mapMoved(zoomLevel, center, boundsNE, boundsSW);
            }

        }
    };

    private final MapClickedRpc mapClickedRpc = new MapClickedRpc() {
        @Override
        public void mapClicked(LatLon position) {
            for (MapClickListener listener : mapClickListeners) {
                listener.mapClicked(position);
            }
        }
    };

    private final InfoWindowClosedRpc infoWindowClosedRpc = new InfoWindowClosedRpc() {

        @Override
        public void infoWindowClosed(long windowId) {
            GoogleMapInfoWindow window = getState().infoWindows.get(windowId);
            for (InfoWindowClosedListener listener : infoWindowClosedListeners) {
                listener.infoWindowClosed(window);
            }
            getState().infoWindows.remove(windowId);
        }
    };

    private final List<MarkerClickListener> markerClickListeners = new ArrayList<MarkerClickListener>();

    private final List<MapMoveListener> mapMoveListeners = new ArrayList<MapMoveListener>();

    private final List<MapClickListener> mapClickListeners = new ArrayList<MapClickListener>();

    private final List<MarkerDragListener> markerDragListeners = new ArrayList<MarkerDragListener>();

    private final List<InfoWindowClosedListener> infoWindowClosedListeners = new ArrayList<InfoWindowClosedListener>();

    /**
     * Initiates a new GoogleMap object with default settings from the
     * {@link GoogleMapState state object}.
     *
     * @param apiKey   The Maps API key from Google. Not required when developing in
     *                 localhost or when using a client id. Use null or empty string
     *                 to disable.
     * @param clientId Google Maps API for Work client ID. Use this instead of API
     *                 key if available. Use null or empty string to disable.
     * @param language The language to use with maps. See
     *                 https://developers.google.com/maps/faq#languagesupport for the
     *                 list of the supported languages. Use null or empty string to
     *                 disable.
     */
    public GoogleMap(String apiKey, String clientId, String language) {
        if (apiKey != null && !apiKey.isEmpty()) {
            getState().apiKey = apiKey;
        }
        if (clientId != null && !clientId.isEmpty()) {
            getState().clientId = clientId;
        }

        if (language != null && !language.isEmpty()) {
            getState().language = language;
        }

        registerRpc(markerClickedRpc);
        registerRpc(mapMovedRpc);
        registerRpc(mapClickedRpc);
        registerRpc(markerDraggedRpc);
        registerRpc(infoWindowClosedRpc);
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
     * @param center The new coordinates of the center.
     */
    public void setCenter(LatLon center) {
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
     * @param zoom New amount of the zoom.
     */
    public void setZoom(int zoom) {
        getState().zoom = zoom;
    }

    /**
     * Returns the current zoom of the map.
     *
     * @return Current value of the zoom.
     */
    public int getZoom() {
        return getState().zoom;
    }

    /**
     * Adds a new marker to the map.
     *
     * @param caption   Caption of the marker shown when the marker is hovered.
     * @param position  Coordinates of the marker on the map.
     * @param draggable Set true to enable dragging of the marker.
     * @param iconUrl   The url of the icon of the marker.
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
     * @param marker The marker to add.
     */
    public void addMarker(GoogleMapMarker marker) {
        getState().markers.put(marker.getId(), marker);
    }

    /**
     * Removes a marker from the map.
     *
     * @param marker The marker to remove.
     */
    public void removeMarker(GoogleMapMarker marker) {
        getState().markers.remove(marker.getId());
    }

    /**
     * Removes all the markers from the map.
     */
    public void clearMarkers() {
        getState().markers = new HashMap<Long, GoogleMapMarker>();
    }

    /**
     * Checks if a marker has been added to the map.
     *
     * @param marker The marker to check.
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
     * @param listener The listener to add.
     */
    public void addMarkerClickListener(MarkerClickListener listener) {
        markerClickListeners.add(listener);
    }

    /**
     * Removes a MarkerClickListener from the map.
     *
     * @param listener The listener to remove.
     */
    public void removeMarkerClickListener(MarkerClickListener listener) {
        markerClickListeners.remove(listener);
    }

    /**
     * Adds a MarkerDragListener to the map.
     *
     * @param listener The listener to add.
     */
    public void addMarkerDragListener(MarkerDragListener listener) {
        markerDragListeners.add(listener);
    }

    /**
     * Removes a MarkerDragListenr from the map.
     *
     * @param listener The listener to remove.
     */
    public void removeMarkerDragListener(MarkerDragListener listener) {
        markerDragListeners.remove(listener);
    }

    /**
     * Adds a MapMoveListener to the map.
     *
     * @param listener The listener to add.
     */
    public void addMapMoveListener(MapMoveListener listener) {
        mapMoveListeners.add(listener);
    }

    /**
     * Removes a MapMoveListener from the map.
     *
     * @param listener The listener to add.
     */
    public void removeMapMoveListener(MapMoveListener listener) {
        mapMoveListeners.remove(listener);
    }

    /**
     * Adds a MapClickListener to the map.
     *
     * @param listener The listener to add.
     */
    public void addMapClickListener(MapClickListener listener) {
        mapClickListeners.add(listener);
    }

    /**
     * Removes a MapClickListener from the map.
     *
     * @param listener The listener to add.
     */
    public void removeMapClickListener(MapClickListener listener) {
        mapClickListeners.remove(listener);
    }

    /**
     * Adds an InfoWindowClosedListener to the map.
     *
     * @param listener The listener to add.
     */
    public void addInfoWindowClosedListener(InfoWindowClosedListener listener) {
        infoWindowClosedListeners.add(listener);
    }

    /**
     * Removes an InfoWindowClosedListener from the map.
     *
     * @param listener The listener to remove.
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
     * @param enable Set true to enable the limiting.
     */
    public void setCenterBoundLimitsEnabled(boolean enable) {
        getState().limitCenterBounds = enable;
    }

    /**
     * Sets the limits of the bounds of the center to given values.
     *
     * @param limitNE The coordinates of the northeast limit.
     * @param limitSW The coordinates of the southwest limit.
     */
    public void setCenterBoundLimits(LatLon limitNE, LatLon limitSW) {
        getState().centerNELimit = limitNE;
        getState().centerSWLimit = limitSW;
        getState().limitCenterBounds = true;
    }

    /**
     * Adds a polygon overlay to the map.
     *
     * @param polygon The GoogleMapPolygon to add.
     */
    public void addPolygonOverlay(GoogleMapPolygon polygon) {
        getState().polygons.add(polygon);
    }

    /**
     * Removes a polygon overlay from the map.
     *
     * @param polygon The GoogleMapPolygon to remove.
     */
    public void removePolygonOverlay(GoogleMapPolygon polygon) {
        getState().polygons.remove(polygon);
    }

    /**
     * Adds a polyline to the map.
     *
     * @param polyline The GoogleMapPolyline to add.
     */
    public void addPolyline(GoogleMapPolyline polyline) {
        getState().polylines.add(polyline);
    }

    /**
     * Removes a polyline from the map.
     *
     * @param polyline The GoogleMapPolyline to add.
     */
    public void removePolyline(GoogleMapPolyline polyline) {
        getState().polylines.remove(polyline);
    }

    /**
     * Adds a KML layer to the map.
     *
     * @param kmlLayer The KML layer to add.
     */
    public void addKmlLayer(GoogleMapKmlLayer kmlLayer) {
        getState().kmlLayers.add(kmlLayer);
    }

    /**
     * Removes a KML layer from the map.
     *
     * @param kmlLayer The KML layer to remove.
     */
    public void removeKmlLayer(GoogleMapKmlLayer kmlLayer) {
        getState().kmlLayers.remove(kmlLayer);
    }

    /**
     * Sets the type of the base map.
     *
     * @param type The new MapType to use.
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
     * @param draggable Set to true to enable dragging.
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
     * @param enabled Set true to enable keyboard shortcuts.
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
     * @param enabled Set true to enable scroll wheel.
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
     * @param controls The new controls to use.
     */
    public void setControls(Set<GoogleMapControl> controls) {
        getState().controls = controls;
    }

    /**
     * Enables the given control on the map. Does nothing if the control is
     * already enabled.
     *
     * @param control The control to enable.
     */
    public void addControl(GoogleMapControl control) {
        getState().controls.add(control);
    }

    /**
     * Removes the control from the map. Does nothing if the control isn't
     * enabled.
     *
     * @param control The control to remove.
     */
    public void removeControl(GoogleMapControl control) {
        getState().controls.remove(control);
    }

    /**
     * Enables/disables limiting of the bounds of the visible area.
     *
     * @param enabled Set true to enable the limiting.
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
     * {@link #setMinZoom(int)} too.
     *
     * @param limitNE The coordinates of the northeast limit.
     * @param limitSW The coordinates of the southwest limit.
     */
    public void setVisibleAreaBoundLimits(LatLon limitNE, LatLon limitSW) {
        getState().visibleAreaNELimit = limitNE;
        getState().visibleAreaSWLimit = limitSW;
        getState().limitVisibleAreaBounds = true;
    }

    /**
     * Sets the maximum allowed amount of zoom (default 21.0).
     *
     * @param maxZoom The maximum amount for zoom.
     */
    public void setMaxZoom(int maxZoom) {
        getState().maxZoom = maxZoom;
    }

    /**
     * Returns the current maximum amount of zoom.
     *
     * @return maximum amount of zoom
     */
    public int getMaxZoom() {
        return getState().maxZoom;
    }

    /**
     * Sets the minimum allowed amount of zoom (default 0.0).
     *
     * @param minZoom The minimum amount for zoom.
     */
    public void setMinZoom(int minZoom) {
        getState().minZoom = minZoom;
    }

    /**
     * Returns the current minimum amount of zoom.
     *
     * @return minimum amount of zoom
     */
    public int getMinZoom() {
        return getState().minZoom;
    }

    /**
     * Opens an info window.
     *
     * @param infoWindow The window to open.
     */
    public void openInfoWindow(GoogleMapInfoWindow infoWindow) {
        getState().infoWindows.put(infoWindow.getId(), infoWindow);
    }

    /**
     * Closes an info window.
     *
     * @param infoWindow The window to close.
     */
    public void closeInfoWindow(GoogleMapInfoWindow infoWindow) {
        getState().infoWindows.remove(infoWindow.getId());
    }

    /**
     * Checks if an info window is open.
     *
     * @param infoWindow The window to check.
     * @return true, if the window is open.
     */
    public boolean isInfoWindowOpen(GoogleMapInfoWindow infoWindow) {
        return getState().infoWindows.containsKey(infoWindow.getId());
    }

    /**
     * Tries to fit the visible area of the map inside given boundaries by
     * modifying zoom and/or center.
     *
     * @param boundsNE The northeast boundaries.
     * @param boundsSW The southwest boundaries.
     */
    public void fitToBounds(LatLon boundsNE, LatLon boundsSW) {
        getState().fitToBoundsNE = boundsNE;
        getState().fitToBoundsSW = boundsSW;
    }
    
    /**
     * Check if a traffic layer is visible 
     * @return true, if traffic layer is visible
     */
    public boolean isTrafficLayerVisible() {
    	return getState().trafficLayerVisible;
    }

    /**
     * Set a traffic layer visibility 
     * @param visible
     */
    public void setTrafficLayerVisible(boolean visible) {
    	getState().trafficLayerVisible = visible;
    }
}
