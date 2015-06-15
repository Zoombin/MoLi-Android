package com.imooly.android.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class ImageUtil {
	private Activity mActivity;

	private int mPicWidth;
	private int mPicHeight;

	private File mPicFile;
	private File mPicFileTemp;

	public static final int CAPTURE_PIC = 0;
	public static final int HANDLE_PIC = 1;
	public static final int PICK_PIC = 2;
	public static final int CAMERA_PIC = 3;

	public static final String DIR_NAME = "Moli/Image";

	public ImageUtil(Activity activity, String picName, String picNameTemp) {
		setActivity(activity);
		setPicFile(createImageFile(DIR_NAME, picName));
		setPicFileTemp(createImageFile(DIR_NAME, picNameTemp));
	}

	public ImageUtil(Activity activity, int picWidth, int picHeight, String picName, String picNameTemp) {
		setActivity(activity);
		setPicWidth(picWidth);
		setPicHeight(picHeight);
		setPicFile(createImageFile(DIR_NAME, picName));
		setPicFileTemp(createImageFile(DIR_NAME, picNameTemp));
	}

	public static void recycleImageVIew(ImageView iv) {
		Drawable d = iv.getDrawable();
		if (d != null && d instanceof BitmapDrawable) {
			Bitmap bmp = ((BitmapDrawable) d).getBitmap();
			bmp.recycle();
			bmp = null;
		}
		iv.setImageBitmap(null);
		if (d != null) {
			d.setCallback(null);
		}
	}
	
	public Activity getActivity() {
		return mActivity;
	}

	public void setActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}

	public int getPicWidth() {
		return mPicWidth;
	}

	public void setPicWidth(int mPicWidth) {
		this.mPicWidth = mPicWidth;
	}

	public int getPicHeight() {
		return mPicHeight;
	}

	public void setPicHeight(int mPicHeight) {
		this.mPicHeight = mPicHeight;
	}

	public File getPicFile() {
		return mPicFile;
	}

	public void setPicFile(File mPicFile) {
		this.mPicFile = mPicFile;
	}

	public File getPicFileTemp() {
		return mPicFileTemp;
	}

	public void setPicFileTemp(File mPicFileTemp) {
		this.mPicFileTemp = mPicFileTemp;
	}

	public void deleteFile() {
		if (mPicFile.exists()) {
			mPicFile.delete();
		}
	}

	private File createImageFile(String path, String fileName) {
		File file = new File(Environment.getExternalStorageDirectory().getPath(), path);

		if (!file.exists()) {
			file.mkdirs();
		}

		Log.d("ImageUtil", file.getAbsolutePath());
		File f = new File(file, fileName);
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", mPicWidth);
		intent.putExtra("outputY", mPicHeight);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false); //false：回调方法data.getExtras().getParcelable("data")返回数据为空
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPicFile));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // 取消人脸识别功能
		mActivity.startActivityForResult(intent, HANDLE_PIC);
	}

	/**
	 * 直接从图库裁剪图片 
	 */
	public void getAndCropPhoto() {

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", mPicWidth);
		intent.putExtra("outputY", mPicHeight);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPicFile));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", false); // no face detection
		mActivity.startActivityForResult(intent, HANDLE_PIC);
	}

	/**
	 * 拍照
	 */
	public void takeCameraPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		// 下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPicFile));
		mActivity.startActivityForResult(intent, ImageUtil.CAMERA_PIC);
	}

	/**
	 * 直接选取图片不剪裁
	 */
	public void getEexistPhoto() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.putExtra("return-data", true);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		mActivity.startActivityForResult(intent, PICK_PIC);
	}

	/**
	 * 获取拍照图片
	 */
	public Uri getCameraPhoto(Context context) {
		File file = getPicFileTemp();
		if (file == null) {
			return null;
		}
		try {
			String uriString = MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), null, null);
			Uri uri = Uri.parse(uriString);
			return uri;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 设置图片数据
	 * 
	 * @param picdata
	 */
	public void setPicToView(Context context, ImageView view, Uri uri) {
		Bitmap photo = decodeUriAsBitmap(context, uri);

		if (null != photo) {
			view.setImageBitmap(photo);
		}
	}

	public Bitmap decodeUriAsBitmap(Context context, Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 生成缩略图
	 * 
	 * @param uri
	 *            （路径）
	 * @return
	 */
	public Bitmap generatePhoto(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);
		/* 计算得到图片的高度 */
		/* 这里需要主意，如果你需要更高的精度来保证图片不变形的话，需要自己进行一下数学运算 */
		int height = options.outHeight * 200 / options.outWidth;
		options.outWidth = 200;
		options.outHeight = height;
		/* 这样才能真正的返回一个Bitmap给你 */
		options.inJustDecodeBounds = false;
		int inSampleSize = options.outWidth / 200;
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		// 默认是Bitmap.Config.ARGB_88880
		/* 下面两个字段需要组合使用 */
		options.inPurgeable = true;
		options.inInputShareable = true;
		Bitmap bmpReturn = BitmapFactory.decodeFile(path, options);
		return bmpReturn;
	}
}
