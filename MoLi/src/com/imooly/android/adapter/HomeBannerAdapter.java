package com.imooly.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imooly.android.R;
import com.imooly.android.entity.RspAdvertiseIndexads.Info;
import com.imooly.android.ui.ProductDetailActivity;
import com.imooly.android.ui.StoreDetailActivity;
import com.imooly.android.ui.StoreProActivity;
import com.imooly.android.widget.autoscrollviewpager.RecyclingPagerAdapter;
import com.imooly.android.widget.viewpage.CirclePageIndicator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 首页banner适配器
 * @author daiye
 *
 */
public class HomeBannerAdapter extends RecyclingPagerAdapter {

    private Context       context;
    List<Info> infos;
    private int           size;
    private boolean       isInfiniteLoop;
	// 商品详情
	String BN01 = "BN01";
	// 店铺
	String SH01 = "SH01";
	// 实体店详情
	String PH01 = "PH01";
	private DisplayImageOptions defaultOptions;
	
    public HomeBannerAdapter(Context context, List<Info> infos) {
        this.context = context;
        this.infos = infos;
        this.size = getSize(infos);
        isInfiniteLoop = true;
		defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_loading_640_160)
		    .showImageForEmptyUri(R.drawable.ic_error_640_160)  // empty URI时显示的图片  
		    .showImageOnFail(R.drawable.ic_error_640_160)      // 不是图片文件 显示图片  
			.cacheInMemory(true)
			.bitmapConfig(Bitmap.Config.RGB_565)  // 图片压缩质量
			.cacheOnDisc(true)			
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			.build();
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? getSize(infos) * CirclePageIndicator.fornum : getSize(infos);
    }

    public <V> int getSize(List<V> sourceList) {
        return sourceList == null ? 0 : sourceList.size();
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
    public View getView(int position, View view, ViewGroup container) {
    	final Info info = infos.get(getPosition(position));
    	
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(context);
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        
        final String apppagecode = info.getApppagecode();
        ImageLoader.getInstance().displayImage(info.getImagepath(), holder.imageView, defaultOptions);
        holder.imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				boolean skip = false;
				if (apppagecode.equals(BN01)) {
					// 商品详情
					skip = true;
					intent.putExtra(ProductDetailActivity.EXTRA_GOODSID, info.getParamid());
					intent.setClass(context, ProductDetailActivity.class);
				} else if (apppagecode.equals(SH01)) {
					// 店铺
					skip = true;
					intent.putExtra(StoreProActivity.EXTRA_BUSINESSID, info.getParamid());
					intent.setClass(context, StoreProActivity.class);
				} else if (apppagecode.equals(PH01)) {
					// 实体店详情
					skip = true;
					intent.putExtra(StoreDetailActivity.EXTRA_BUSNESSID, info.getParamid());
					intent.setClass(context, StoreDetailActivity.class);
				}
				if(skip){
					context.startActivity(intent);
				}
			}
		});
        
        return view;
    }

    private static class ViewHolder {
        ImageView imageView;
    }
}
