package com.imooly.android.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OrderServiceStatusFragmentAdapter extends FragmentPagerAdapter {

	/***
	 * 售后状态Fragment 适配器
	 */
	List<Fragment> list;

	public void setData(List<Fragment> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public OrderServiceStatusFragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(index);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

}
