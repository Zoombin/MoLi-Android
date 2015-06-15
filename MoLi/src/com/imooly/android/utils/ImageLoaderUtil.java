package com.imooly.android.utils;

import android.graphics.Bitmap;

import com.imooly.android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageLoaderUtil {

	public static final String Drawable_Path = "drawable://";
	
	public static DisplayImageOptions getMemberCardImageOption() {
		
		
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.bitmapConfig(Bitmap.Config.ARGB_8888)  // 图片压缩质量	
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.build();
		
		return defaultOptions;
	}

}
