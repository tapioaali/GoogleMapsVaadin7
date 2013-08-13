# Google Maps Add-on for Vaadin 7

**Remember to get an API key from [https://code.google.com/apis/console/](https://code.google.com/apis/console/). It is normally not required when developing in localhost.**

Check out demo @ [http://tapio.virtuallypreinstalled.com/googlemaps/](http://tapio.virtuallypreinstalled.com/googlemaps/)

## Supported features ##

- Setting center & zoom
- Setting visible map controls
- Adding & dragging makers
- Adding polygon overlays
- Adding polyline overlays
- Limiting center bounds
- Enabling/disabling map dragging, keyboard shortcuts and scroll wheel zoom

## Supported events ##
- Map move
- Marker click
- Marker drag

## Development notice ##

If you want to use the add-on without Maven, grab **Mapsv3 bindings for GWT 3.8.0** from [https://code.google.com/p/gwt-google-apis/downloads/detail?name=gwt-maps-3.8.0-pre1.zip](https://code.google.com/p/gwt-google-apis/downloads/detail?name=gwt-maps-3.8.0-pre1.zip). 

## Maven notice ##

The add-on uses `com.github.rwl:gwt-maps` artifact. In order to use it, you'll have to exclude `com.google.gwt:gwt-user`. Either do it directly with `gwt-maps`

	<dependency>
	    <groupId>com.github.rwl</groupId>
	    <artifactId>gwt-maps</artifactId>
	    <version>3.8.0-pre1</version>
	    <exclusions>
			<exclusion>
	        	<groupId>com.google.gwt</groupId>
	        	<artifactId>gwt-user</artifactId>
	    	</exclusion>
		</exclusions>
	</dependency>

or with the widget

	<dependency>
	    <groupId>com.vaadin.tapio</groupId>
	    <artifactId>googlemaps</artifactId>
	    <version>0.2</version>
	    <exclusions>
		    <exclusion>
	            <groupId>com.google.gwt</groupId>
	       	    <artifactId>gwt-user</artifactId>
	  	    </exclusion>
	    </exclusions>
	</dependency>