package com.vaadin.tapio.googlemaps.client.util;

import java.util.Collection;

import com.google.gwt.thirdparty.guava.common.collect.BiMap;
import com.google.maps.gwt.client.Animation;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

public class MarkerUtil {
    public static void updateMarkerMap(
            final BiMap<Marker, GoogleMapMarker> markerMap,
            final Collection<GoogleMapMarker> markers,
            final MarkerClickListener markerClickListener,
            final MarkerDragListener markerDragListener, GoogleMap map) {
        // clear removed markers
        for (Marker marker : markerMap.keySet()) {
            GoogleMapMarker gMapMarker = markerMap.get(marker);
            if (!markers.contains(gMapMarker)) {
                marker.setMap((GoogleMap) null);
                markerMap.remove(marker);
            }
        }

        for (GoogleMapMarker googleMapMarker : markers) {
            if (!markerMap.containsValue(googleMapMarker)) {
                // new marker
                final Marker marker = MarkerUtil.createMarker(googleMapMarker,
                        map);
                markerMap.forcePut(marker, googleMapMarker);

                marker.addClickListener(new Marker.ClickHandler() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (markerClickListener != null) {
                            markerClickListener.markerClicked(markerMap
                                    .get(marker));
                        }
                    }
                });
                marker.addDragEndListener(new Marker.DragEndHandler() {
                    @Override
                    public void handle(MouseEvent event) {
                        GoogleMapMarker gMarker = markerMap.get(marker);
                        LatLon oldPosition = gMarker.getPosition();
                        gMarker.setPosition(new LatLon(marker.getPosition()
                                .lat(), marker.getPosition().lng()));
                        if (markerDragListener != null) {
                            markerDragListener.markerDragged(gMarker,
                                    oldPosition);
                        }
                    }
                });
            } else {
                // marker exists, create new
                Marker marker = markerMap.inverse().get(googleMapMarker);
                GoogleMapMarker oldGmMarker = markerMap.get(marker);
                updateMarker(marker, oldGmMarker, googleMapMarker);
                markerMap.forcePut(marker, googleMapMarker);
            }
        }
    }

    public static Marker createMarker(GoogleMapMarker googleMapMarker,
            GoogleMap map) {
        MarkerOptions options = createMarkerOptions(googleMapMarker);
        final Marker marker = Marker.create(options);
        marker.setMap(map);
        return marker;
    }

    public static void updateMarker(Marker marker, GoogleMapMarker oldGmMarker,
            GoogleMapMarker newGmMarker) {
        if (!oldGmMarker.hasSameFieldValues(newGmMarker)) {
            MarkerOptions options = createMarkerOptions(newGmMarker);
            marker.setOptions(options);
        }
    }

    private static MarkerOptions createMarkerOptions(
            GoogleMapMarker googleMapMarker) {
        LatLng center = LatLng.create(googleMapMarker.getPosition().getLat(),
                googleMapMarker.getPosition().getLon());
        MarkerOptions options = MarkerOptions.create();
        options.setPosition(center);
        options.setTitle(googleMapMarker.getCaption());
        options.setDraggable(googleMapMarker.isDraggable());
        options.setOptimized(googleMapMarker.isOptimized());

        if (googleMapMarker.isAnimationEnabled()) {
            options.setAnimation(Animation.DROP);
        }

        if (googleMapMarker.getIconUrl() != null) {
            options.setIcon(googleMapMarker.getIconUrl());
        }
        return options;
    }

}
