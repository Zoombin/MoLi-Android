package com.imooly.android.utils;

import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 把bitmap转换成byte数组
 */
public class BitmapUtils {

	/**
	 * 把bitmap转换成byte数组
	 */
	public static byte[] bitmap2ByteArray(Bitmap bitmap) {

		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();

		return bytes;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		// 保证是方形，并且从中心画
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int w;
		int deltaX = 0;
		int deltaY = 0;
		if (width <= height) {
			w = width;
			deltaY = height - w;
		} else {
			w = height;
			deltaX = width - w;
		}
		
		final Rect rect = new Rect(deltaX, deltaY, w, w);
		final RectF rectF = new RectF(rect);

		// 抗锯齿
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		
		canvas.drawARGB(0, 0, 0, 0);
		
		// 圆形，所有只用一个
		int radius = (int) (Math.sqrt(w * w * 2.0d) / 2);
		canvas.drawRoundRect(rectF, radius, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
	
	/** 
	 * 将图片纠正到正确方向 
	 *  
	 * @param degree ： 图片被系统旋转的角度 
	 * @param bitmap ： 需纠正方向的图片 
	 * @return 纠向后的图片 
	 */  
	public static Bitmap rotateBitmap(int degree, Bitmap bitmap) {  
	    Matrix matrix = new Matrix();  
	    matrix.postRotate(degree);  
	  
	    Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),  
	            bitmap.getHeight(), matrix, true);  
	    return bm;  
	}  
}
