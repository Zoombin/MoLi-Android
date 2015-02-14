package com.imooly.android.entity;

import java.io.Serializable;

import com.baidu.mapapi.search.route.TransitRouteLine;

public class RouteLineEntity implements Serializable {
	String type;
	TransitRouteLine transitLine;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TransitRouteLine getTransitLine() {
		return transitLine;
	}

	public void setTransitLine(TransitRouteLine transitLine) {
		this.transitLine = transitLine;
	}

}
