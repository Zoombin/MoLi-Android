package com.imooly.android.tool;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.imooly.android.animation.RotateFlipCardAnimation;

public class Rotation3D {
	private float start;
	private float end;
	private View view;
	private RotationListener listener;

	public interface RotationListener {
		public void onEndRotation();
	}

	public Rotation3D(float startAngle, float endAngle, View view, RotationListener listener) {
		
		this.start = startAngle;
		this.end = endAngle;
		this.view = view;
		this.listener = listener;
		
		applyRotation(0.0f, 180.0f);
	}

	/***
	 * 翻转动画
	 * 
	 * @param start
	 * @param end
	 */
	private void applyRotation(float start, float end) {
		// 计算中心点
		float centerX = view.getWidth() / 2.0f;
		float centerY = view.getHeight() / 2.0f;

		RotateFlipCardAnimation rotation = new RotateFlipCardAnimation(start, end, centerX, centerY, 0.0f, true);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new LinearInterpolator());
		// 设置监听
		rotation.setAnimationListener(new DisplayNextView());

		view.startAnimation(rotation);
	}

	private class DisplayNextView implements Animation.AnimationListener {
		public void onAnimationStart(Animation animation) {
		}

		// 动画结束
		public void onAnimationEnd(Animation animation) {

			listener.onEndRotation();
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}
}
