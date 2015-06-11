package com.vaadin.tapio.googlemaps.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.shared.ui.Connect;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.events.InfoWindowClosedListener;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.rpcs.InfoWindowClosedRpc;
import com.vaadin.tapio.googlemaps.client.rpcs.MapClickedRpc;
import com.vaadin.tapio.googlemaps.client.rpcs.MapMovedRpc;
import com.vaadin.tapio.googlemaps.client.rpcs.MarkerClickedRpc;
import com.vaadin.tapio.googlemaps.client.rpcs.MarkerDraggedRpc;

/**
 * The connector for the Google Maps JavaScript API v3.
 */
@Connect(GoogleMap.class)
public class GoogleMapConnector extends AbstractComponentConnector implements
        MarkerClickListener, MapMoveListener, MapClickListener,
        MarkerDragListener, InfoWindowClosedListener {

    private static final long serialVersionUID = 646346521643L;

    protected static boolean apiLoaded = false;
    protected static boolean mapInitiated = false;

    private boolean deferred = false;
    private MarkerClickedRpc markerClickedRpc = RpcProxy.create(
            MarkerClickedRpc.class, this);
    private MapMovedRpc mapMovedRpc = RpcProxy.create(MapMovedRpc.class, this);
    private MapClickedRpc mapClickRpc = RpcProxy.create(MapClickedRpc.class,
            this);
    private MarkerDraggedRpc markerDraggedRpc = RpcProxy.create(
            MarkerDraggedRpc.class, this);
    private InfoWindowClosedRpc infoWindowClosedRpc = RpcProxy.create(
            InfoWindowClosedRpc.class, this);

    public GoogleMapConnector() {
    }

    private void initMap() {
        getWidget().setVisualRefreshEnabled(getState().visualRefreshEnabled);
        getWidget().initMap(getState().center, getState().zoom,
                getState().mapTypeId);
        getWidget().setMarkerClickListener(this);
        getWidget().setMapMoveListener(this);
        getWidget().setMapClickListener(this);
        getWidget().setMarkerDragListener(this);
        getWidget().setInfoWindowClosedListener(this);
        if (deferred) {
            loadDeferred();
            deferred = false;
        }
        getLayoutManager().addElementResizeListener(getWidget().getElement(),
                new ElementResizeListener() {
                    @Override
                    public void onElementResize(ElementResizeEvent e) {
                        getWidget().triggerResize();
                    }
                });
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(GoogleMapWidget.class);
    }

    @Override
    public GoogleMapWidget getWidget() {
        return (GoogleMapWidget) super.getWidget();
    }

    @Override
    public GoogleMapState getState() {
        return (GoogleMapState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        // settings that can be set without API being loaded/map initiated
        if (getState().limitCenterBounds) {
            getWidget().setCenterBoundLimits(getState().centerNELimit,
                    getState().centerSWLimit);
        } else {
            getWidget().clearCenterBoundLimits();
        }

        if (getState().limitVisibleAreaBounds) {
            getWidget().setVisibleAreaBoundLimits(
                    getState().visibleAreaNELimit,
                    getState().visibleAreaSWLimit);
        } else {
            getWidget().clearVisibleAreaBoundLimits();
        }

        // load API/init map
        if (!apiLoaded) {
            deferred = true;
            loadMapApi();
            apiLoaded = true;
            return;
        } else if (!getWidget().isMapInitiated()) {
            deferred = true;
            initMap();
            return;
        }

        // settings that require initiated map
        boolean initial = stateChangeEvent.isInitialStateChange();
        // do not set zoom/center again if the change originated from client
        if (!getState().locationFromClient || initial) {
            if (getState().center.getLat() != getWidget().getLatitude()
                    || getState().center.getLon() != getWidget().getLongitude()) {
                getWidget().setCenter(getState().center);
            }
            if (getState().zoom != getWidget().getZoom()) {
                getWidget().setZoom(getState().zoom);
            }
        }

        if (stateChangeEvent.hasPropertyChanged("markers") || initial) {
            getWidget().setMarkers(getState().markers.values());
        }

        if (stateChangeEvent.hasPropertyChanged("circles") || initial) {
        	getWidget().setCircleOverlays(getState().circles);
        }
        if (stateChangeEvent.hasPropertyChanged("polygons") || initial) {
            getWidget().setPolygonOverlays(getState().polygons);
        }
        if (stateChangeEvent.hasPropertyChanged("polylines") || initial) {
            getWidget().setPolylineOverlays(getState().polylines);
        }
        if (stateChangeEvent.hasPropertyChanged("kmlLayers") || initial) {
            getWidget().setKmlLayers(getState().kmlLayers);
        }
        if (stateChangeEvent.hasPropertyChanged("mapTypeId") || initial) {
            getWidget().setMapType(getState().mapTypeId);
        }

        if (stateChangeEvent.hasPropertyChanged("controls") || initial) {
            getWidget().setControls(getState().controls);
        }

        if (stateChangeEvent.hasPropertyChanged("draggable") || initial) {
            getWidget().setDraggable(getState().draggable);
        }
        if (stateChangeEvent.hasPropertyChanged("keyboardShortcutsEnabled")
                || initial) {
            getWidget().setKeyboardShortcutsEnabled(
                    getState().keyboardShortcutsEnabled);
        }
        if (stateChangeEvent.hasPropertyChanged("scrollWheelEnabled")
                || initial) {
            getWidget().setScrollWheelEnabled(getState().scrollWheelEnabled);
        }
        if (stateChangeEvent.hasPropertyChanged("minZoom") || initial) {
            getWidget().setMinZoom(getState().minZoom);
        }
        if (stateChangeEvent.hasPropertyChanged("maxZoom") || initial) {
            getWidget().setMaxZoom(getState().maxZoom);
        }

        if (stateChangeEvent.hasPropertyChanged("infoWindows") || initial) {
            getWidget().setInfoWindows(getState().infoWindows.values());
        }

        if (stateChangeEvent.hasPropertyChanged("visualRefreshEnabled")
                || initial) {
            getWidget()
                    .setVisualRefreshEnabled(getState().visualRefreshEnabled);
        }

        if (stateChangeEvent.hasPropertyChanged("fitToBoundsNE")
                || stateChangeEvent.hasPropertyChanged("fitToBoundsSW")
                || initial) {
            if (getState().fitToBoundsNE != null
                    && getState().fitToBoundsSW != null) {
                getWidget().fitToBounds(getState().fitToBoundsNE,
                        getState().fitToBoundsSW);
            }
        }

        if (initial) {
            getWidget().triggerResize();
        }

    }

    private void loadMapApi() {
        ArrayList<LoadApi.LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();
        Runnable onLoad = new Runnable() {
            @Override
            public void run() {
                initMap();
            }
        };

        LoadApi.Language language = null;
        if (getState().language != null) {
            language = LoadApi.Language.fromValue(getState().language);
        }

        String params = null;
        if (getState().clientId != null) {
            params = "client=" + getState().clientId;
        } else if (getState().apiKey != null) {
            params = "APIKEY=" + getState().apiKey;
        }

        LoadApi.go(onLoad, loadLibraries, false, language, params);
    }

    private void loadDeferred() {
        getWidget().setMarkers(getState().markers.values());
        getWidget().setCircleOverlays(getState().circles);
        getWidget().setPolygonOverlays(getState().polygons);
        getWidget().setPolylineOverlays(getState().polylines);
        getWidget().setKmlLayers(getState().kmlLayers);
        getWidget().setInfoWindows(getState().infoWindows.values());
        getWidget().setMapType(getState().mapTypeId);
        getWidget().setControls(getState().controls);
        getWidget().setDraggable(getState().draggable);
        getWidget().setKeyboardShortcutsEnabled(
                getState().keyboardShortcutsEnabled);
        getWidget().setScrollWheelEnabled(getState().scrollWheelEnabled);
        getWidget().setMinZoom(getState().minZoom);
        getWidget().setMaxZoom(getState().maxZoom);
        if (getState().fitToBoundsNE != null
                && getState().fitToBoundsSW != null) {
            getWidget().fitToBounds(getState().fitToBoundsNE,
                    getState().fitToBoundsSW);
        }
    }

    @Override
    public void markerClicked(GoogleMapMarker clickedMarker) {
        markerClickedRpc.markerClicked(clickedMarker.getId());
    }

    @Override
    public void mapMoved(int zoomLevel, LatLon center, LatLon boundsNE,
            LatLon boundsSW) {
        mapMovedRpc.mapMoved(zoomLevel, center, boundsNE, boundsSW);
    }

    @Override
    public void markerDragged(GoogleMapMarker draggedMarker, LatLon oldPosition) {
        markerDraggedRpc.markerDragged(draggedMarker.getId(),
                draggedMarker.getPosition());
    }

    @Override
    public void infoWindowClosed(GoogleMapInfoWindow window) {
        infoWindowClosedRpc.infoWindowClosed(window.getId());
    }

    @Override
    public void mapClicked(LatLon position) {
        mapClickRpc.mapClicked(position);
    }
}
