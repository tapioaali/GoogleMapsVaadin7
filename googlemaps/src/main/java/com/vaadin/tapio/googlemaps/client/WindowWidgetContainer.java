package com.vaadin.tapio.googlemaps.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;

public class WindowWidgetContainer {

    private static Map<CustomInfoWindow, Widget> windowWidgetMap = new HashMap<>();

    public static void addWindowWithWidget(CustomInfoWindow window,
        Widget widget) {
        windowWidgetMap.put(window, widget);
    }

    public static void detachWidget(CustomInfoWindow window) {
        assert (window != null);
        Widget widget = windowWidgetMap.remove(window);
        if (widget != null) {
            if (widget.getParent() != null) {
                widget.removeFromParent();
            }
        }
        window.setContent("");
    }
}
