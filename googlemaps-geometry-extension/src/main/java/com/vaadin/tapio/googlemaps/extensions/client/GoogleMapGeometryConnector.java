package com.vaadin.tapio.googlemaps.extensions.client;

import com.google.gwt.core.client.GWT;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.tapio.googlemaps.client.GoogleMapConnector;
import com.vaadin.tapio.googlemaps.extensions.GoogleMapGeometry;

/**
 * The connector for the Google Maps JavaScript API v3.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 */
@Connect(GoogleMapGeometry.class)
public class GoogleMapGeometryConnector extends AbstractExtensionConnector {

    private static final long serialVersionUID = 646346521143L;

    private GoogleMapConnector connector;
    private GoogleMapGeometryWidget widget;

    @Override
    protected void extend(ServerConnector target) {
        GoogleMapConnector connector = (GoogleMapConnector) target;
        connector.getWidget().getMap();
    }

    private GoogleMapGeometryWidget createWidget() {
        return GWT.create(GoogleMapGeometryWidget.class);
    }

    private GoogleMapGeometryWidget getWidget() {
        if (widget == null) {
            widget = createWidget();
        }
        return widget;
    }

}
