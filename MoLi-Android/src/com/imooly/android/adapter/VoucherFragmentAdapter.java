package com.imooly.android.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class VoucherFragmentAdapter extends FragmentPagerAdapter {
	
	private List<Fragment> list;
	
	public VoucherFragmentAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
