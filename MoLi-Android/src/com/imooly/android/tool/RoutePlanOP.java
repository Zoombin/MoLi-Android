package com.imooly.android.tool;

import com.baidu.mapapi.model.LatLng;

public class RoutePlanOP {
	static RoutePlanOP mRoutePlanOP;

	private String routeType;
	private Object routeLine;
	private LatLng markLatLng;// 路径计算的终点

	// 路线规划暂存
	synchronized public static RoutePlanOP instance() {
		if (mRoutePlanOP == null) {
			mRoutePlanOP = new RoutePlanOP();
		}
		return mRoutePlanOP;
	}

	private RoutePlanOP() {
	}

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	public Object getRouteLine() {
		return routeLine;
	}

	public void setRouteLine(Object routeLine) {
		this.routeLine = routeLine;
	}

	public LatLng getMarkLatLng() {
		return markLatLng;
	}

	public void setMarkLatLng(LatLng markLatLng) {
		this.markLatLng = markLatLng;
	}

}
