package com.imooly.android.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine.TransitStep;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine.WalkingStep;
import com.imooly.android.R;

/***
 * 水平ListView适配器
 * 
 * @author lsd
 * 
 */
public class StoreRoutePlanAdapter extends BaseAdapter {
	List<?> list;

	public void setData(List<?> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return list == null ? 0 : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Tag tag;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_routeplan_list_item, null);
			tag.route_title = (TextView) convertView.findViewById(R.id.route_title);
			tag.route_content = (TextView) convertView.findViewById(R.id.route_content);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		Object object = getItem(position);
		if (object != null) {
			String title = "";
			String content = "";
			if (object instanceof TransitRouteLine) {
				TransitRouteLine line = (TransitRouteLine) object;
				List<TransitStep> steps = line.getAllStep();
				StringBuffer sBuffer = new StringBuffer();
				String strDir = " → ";
				for (TransitStep step : steps) {
					if (step.getVehicleInfo() != null) {
						String vehicleInfo = step.getVehicleInfo().getTitle();
						sBuffer.append(vehicleInfo);
						sBuffer.append(strDir);
					}
				}
				title = sBuffer.toString().substring(0, sBuffer.toString().lastIndexOf(strDir));

				int duration = line.getDuration();
				int hour = duration / (60 * 60);
				int min = (duration - (hour * 60 * 60)) / 60;
				int distance = line.getDistance();
				content = String.format("约%s小时%s分  |  %s公里", hour, min, new DecimalFormat("#.0").format(distance / 1000.0f));
			}
			if(object instanceof DrivingRouteLine){
				DrivingRouteLine line = (DrivingRouteLine) object;
				List<DrivingStep> steps = line.getAllStep();
				title = "驾车路线";
				
				int duration = line.getDuration();
				int hour = duration / (60 * 60);
				int min = (duration - (hour * 60 * 60)) / 60;
				int distance = line.getDistance();
				content = String.format("约%s小时%s分  |  %s公里", hour, min, new DecimalFormat("#.0").format(distance / 1000.0f));
			}
			if(object instanceof WalkingRouteLine){
				WalkingRouteLine line = (WalkingRouteLine) object;
				List<WalkingStep> steps = line.getAllStep();
				title = "步行路线";
				
				int duration = line.getDuration();
				int hour = duration / (60 * 60);
				int min = (duration - (hour * 60 * 60)) / 60;
				int distance = line.getDistance();
				content = String.format("约%s小时%s分  |  %s公里", hour, min, new DecimalFormat("#.0").format(distance / 1000.0f));
			}
			tag.route_title.setText(title);
			tag.route_content.setText(content);
		}

		return convertView;
	}

	class Tag {
		TextView route_title;
		TextView route_content;
	}
}
