package com.imooly.android.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseFragment extends Fragment {

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	protected BaseActivity mActivity;
	protected int width, height;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (BaseActivity) getActivity();

		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		imageLoader.clearMemoryCache();
		System.gc();
	}
}
