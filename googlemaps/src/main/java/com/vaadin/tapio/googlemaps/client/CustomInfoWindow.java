package com.vaadin.tapio.googlemaps.client;

/*
 * #%L
 * GWT Maps API V3 - Core API
 * %%
 * Copyright (C) 2011 - 2012 GWT Maps API V3
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.maps.client.MapImpl;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.MapEventType;
import com.google.gwt.maps.client.events.MapHandlerRegistration;
import com.google.gwt.maps.client.events.closeclick.CloseClickEventFormatter;
import com.google.gwt.maps.client.events.closeclick.CloseClickMapHandler;
import com.google.gwt.maps.client.events.content.ContentChangeEventFormatter;
import com.google.gwt.maps.client.events.content.ContentChangeMapHandler;
import com.google.gwt.maps.client.events.domready.DomReadyEventFormatter;
import com.google.gwt.maps.client.events.domready.DomReadyMapHandler;
import com.google.gwt.maps.client.events.position.PositionChangeEventFormatter;
import com.google.gwt.maps.client.events.position.PositionChangeMapHandler;
import com.google.gwt.maps.client.events.zindex.ZindexChangeEventFormatter;
import com.google.gwt.maps.client.events.zindex.ZindexChangeMapHandler;
import com.google.gwt.maps.client.mvc.MVCObject;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.streetview.StreetViewPanoramaImpl;
import com.google.gwt.maps.client.streetview.StreetViewPanoramaWidget;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class CustomInfoWindow extends Panel {

    private InfoWindow window;
    private Widget contents;
    private CloseClickMapHandler closeHandler;
    private HandlerRegistration closeHandlerReg;

    CustomInfoWindow() {
        window = null;
        contents = null;
        closeHandler = null;
        closeHandlerReg = null;
        setElement(Document.get().createDivElement());
    }

    public void open(InfoWindowOptions opts, MapWidget map) {
        open(opts,map,null);
    }

    public void open(InfoWindowOptions opts, MapWidget map, Marker marker) {
        window = InfoWindow.newInstance(opts);
        window.setContent(this.getElement());
        if(closeHandler != null) {
            closeHandlerReg = window.addCloseClickHandler(closeHandler);
        }
        if(marker != null) {
            window.open(map, marker);
        } else {
            window.open(map);
        }
    }

    public void close() {
        if(window != null) {
            window.close();
            closeHandlerReg.removeHandler();
            window = null;
        }
    }

    public void addCloseClickListener(CloseClickMapHandler handler) {
        closeHandler = handler;
        if(window != null) {
            closeHandlerReg = window.addCloseClickHandler(handler);
        }
    }

    public InfoWindow getWrapped() {
        return window;
    }

    @Override
    public Iterator<Widget> iterator() {
        return Arrays.asList(contents).iterator();
    }

    @Override
    public void add(Widget child) {
        if(contents != null) {
            remove(contents);
        }
        contents = child;
        getElement().appendChild(child.getElement());
        adopt(child);
    }

    @Override
    public boolean remove(Widget child) {
        if(child == contents) {
            orphan(child);
            child.getElement().removeFromParent();
            contents = null;
            return true;
        }
        return false;
    }

    public void setContent(String s) {
        setContent(new Label(s));
    }

    public void setContent(Element elem) {
        FlowPanel p = new FlowPanel();
        p.getElement().appendChild(elem);
        add(p);
    }

    public void setContent(Widget widget) {
        add(widget);
    }

    public Widget getContents() {
        return contents;
    }

}
