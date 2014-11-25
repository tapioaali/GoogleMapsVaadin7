package com.vaadin.tapio.googlemaps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.tapio.googlemaps.client.LatLon;

public interface CircleClickedRpc extends ServerRpc 
{
	public void circleClicked(long circleId, LatLon position);
}

