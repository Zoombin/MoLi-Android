package com.imooly.android.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.adapter.StoreCircleSelectGridAdapter;
import com.imooly.android.entity.RspBusinessCirclelist.Circle;
import com.imooly.android.entity.RspStoreCityList.City;
import com.imooly.android.fragment.StoreFragment;
import com.imooly.android.ui.StoreCityChangeActivity;

/***
 * 
 * 实体店 - 城市选择
 * 
 * @author LSD
 * 
 */
public class StoreAddressSelectDialog {
	private Context context;
	private List<Circle> circleList;
	private City curCity;
	private PopupWindow popWin;
	private AddressSelectCallBack callBack;
	private GridView listView;
	// private StoreAddressSelectGridAdapter adapter;
	private StoreCircleSelectGridAdapter adapter;
	private TextView tv_null, tv_curcity;
	private View shadow_view;

	private LinearLayout ll_curcity;

	public StoreAddressSelectDialog(Context context, AddressSelectCallBack callBack) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.callBack = callBack;
		initPopWin(context);
	}

	public interface AddressSelectCallBack {
		void onSelect(Circle mode, int index);
	}

	public void setShadow_view(View shadow_view) {
		this.shadow_view = shadow_view;
	}

	/**
	 * 初始化背景颜色弹窗
	 * 
	 * @param context
	 */
	private void initPopWin(final Context context) {
		View popView = LayoutInflater.from(context).inflate(R.layout.view_store_address_select_dialog, null);
		if (popWin == null) {
			popWin = new PopupWindow(popView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		}
		popWin.setAnimationStyle(R.style.selectCityDropDownAnim);
		// 需要设置一下此参数，点击外边可消失
		popWin.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		popWin.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		popWin.setFocusable(true);
		popWin.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				shadow_view.setVisibility(View.GONE);
				shadow_view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alphahide));
			}
		});
		popView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismissPopWin();
			}
		});

		tv_null = (TextView) popView.findViewById(R.id.tv_null);
		tv_curcity = (TextView) popView.findViewById(R.id.tv_curcity);
		ll_curcity = (LinearLayout) popView.findViewById(R.id.ll_curcity);
		ll_curcity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Activity) context).startActivityForResult(new Intent(context, StoreCityChangeActivity.class), StoreFragment.SELECT_CITY);
			}
		});

		listView = (GridView) popWin.getContentView().findViewById(R.id.comm_glist);
		listView.setAdapter(adapter = new StoreCircleSelectGridAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				adapter.changeSelect(position);
				if (callBack != null) {
					callBack.onSelect((Circle) adapter.getItem(position), position);
				}
				dismissPopWin();
			}
		});
	}

	public void showPopWin(View parent, final List<Circle> circleList, final City curCity, int index) {
		this.circleList = circleList;
		this.curCity = curCity;
		if (popWin != null && !popWin.isShowing()) {
			popWin.showAsDropDown(parent);

			shadow_view.setVisibility(View.VISIBLE);
			shadow_view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alphashow));
		}

		adapter.setData(index, circleList);

		if (circleList != null && circleList.size() > 0) {
			listView.setVisibility(View.VISIBLE);
			tv_null.setVisibility(View.GONE);
		} else {
			listView.setVisibility(View.GONE);
			tv_null.setVisibility(View.VISIBLE);
		}

		tv_curcity.setText("当前城市：" + curCity.getName());
	}

	public void dismissPopWin() {
		if (popWin != null) {
			popWin.dismiss();
		}
		shadow_view.setVisibility(View.GONE);
		shadow_view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alphahide));
	}

	public void refrashView(List<Circle> circleList, final City curCity,int index) {
		this.curCity = curCity;
		tv_curcity.setText("当前城市：" + curCity.getName());
		if (circleList != null) {
			adapter.setData(index, circleList);
		}
	}
}
