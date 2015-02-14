package com.imooly.android.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.imooly.android.widget.viewpage.CirclePageIndicator;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PagerviewImgAdapter extends PagerAdapter {

	private Context context;
	private List<String> goodsimage;
	private int size;
	private boolean isInfiniteLoop;

	public PagerviewImgAdapter(Context context, List<String> goodsimage,boolean isInfiniteLoop) {
		this.context = context;
		this.goodsimage = goodsimage;
		this.size = getSize(goodsimage);
		this.isInfiniteLoop = isInfiniteLoop;
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? getSize(goodsimage) * CirclePageIndicator.fornum
				: getSize(goodsimage);
	}

	public <V> int getSize(List<V> sourceList) {
		return sourceList == null ? 0 : sourceList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == (View) obj;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		ImageView photoView = new ImageView(context);
		photoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		// photoView
		// .setBackgroundResource(R.drawable.product_list_grid_item_icon_bg);
		// photoView.setScaleType(ScaleType.FIT_XY);

		ImageLoader.getInstance().displayImage(goodsimage.get(getPosition(position)),
				photoView);

		try {
			((ViewPager) container).addView(photoView, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return photoView;
	}

    /**
     * get really position
     * 
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }
    
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}