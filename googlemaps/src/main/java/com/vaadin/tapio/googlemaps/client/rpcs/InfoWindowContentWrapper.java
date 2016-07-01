package com.vaadin.tapio.googlemaps.client.rpcs;

import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class InfoWindowContentWrapper extends FlowPanel {

    private final FlowPanel rootPanel;

    public InfoWindowContentWrapper(FlowPanel rootPanel) {
        super();
        this.rootPanel = rootPanel;
        setVisible(false);
        RootPanel.detachOnWindowClose(this);
    }

    @Override
    public void onAttach() {
        super.onAttach();
    }

    @Override
    public void onDetach() {
        removeFromParent();
        if(getWrappedWidget() != null) {
            getWrappedWidget().removeFromParent();
        }
        super.onDetach();
    }

    public void setContent(Widget widget) {
        add(widget);
    }

    @Override
    public void add(Widget child) {
        if(getWrappedWidget() != null) {
            super.remove(getWrappedWidget());
        }
        if(child.getParent() != null) {
            child.removeFromParent();
        }
        getChildren().add(child);
        DOM.appendChild(rootPanel.getElement(), child.getElement());
        adopt(child);
    }

    private Widget getWrappedWidget() {
        return getChildren().get(0);
    }

    public void setWindow(InfoWindow window) {
        if(window != null) {
            setVisible(true);
            window.setContent(this);
        } else {
            setVisible(false);
            rootPanel.add(this);
        }
    }
}
