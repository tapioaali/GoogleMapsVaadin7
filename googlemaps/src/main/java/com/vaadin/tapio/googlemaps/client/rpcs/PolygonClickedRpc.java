package com.vaadin.tapio.googlemaps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.tapio.googlemaps.client.LatLon;

public interface PolygonClickedRpc extends ServerRpc 
{
	public void polygonClicked(long polygonId, LatLon position);
}
