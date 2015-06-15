package com.imooly.android.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.imooly.android.R;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.utils.ImageLoaderUtil;
import com.imooly.android.utils.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MemberRightActivity extends BaseActivity {

	private ImageView iv_back;
	private ImageView image_one;
	private ImageView image_two;
	private ImageView image_three;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_right);

		logActivityName(this);

		initView();
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		

//		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//		.cacheInMemory(false)
//		.bitmapConfig(Bitmap.Config.RGB_565)  // 图片压缩质量
//		.build();
//		
//		image_one = (ImageView) findViewById(R.id.image_one);
//		image_two = (ImageView) findViewById(R.id.image_two);
//		image_three = (ImageView) findViewById(R.id.image_three);
//		ImageLoader.getInstance().displayImage(
//				ImageLoaderUtil.Drawable_Path + R.drawable.bg_member_right_one,
//				image_one, defaultOptions);
//		ImageLoader.getInstance().displayImage(
//				ImageLoaderUtil.Drawable_Path + R.drawable.bg_member_right_two,
//				image_two, defaultOptions);
//		ImageLoader.getInstance().displayImage(
//				ImageLoaderUtil.Drawable_Path
//						+ R.drawable.bg_member_right_three, image_three, defaultOptions);
//		
//		ImageUtil.recycleImageVIew(image_one);
//		ImageUtil.recycleImageVIew(image_two);
//		ImageUtil.recycleImageVIew(image_three);
	}
}
