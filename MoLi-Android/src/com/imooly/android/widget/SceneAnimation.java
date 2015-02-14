package com.imooly.android.widget;

import android.widget.ImageView;

/**
 * 帧动画类
 * 
 * @author daiye
 * 
 */
public class SceneAnimation {
	private ImageView mImageView;
	private int[] mFrameRess;
	private int mDuration;
	private int mLastFrameNo;

	private boolean loop = true;
	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	public SceneAnimation(ImageView pImageView, int[] pFrameRess, int pDuration) {
		mImageView = pImageView;
		mFrameRess = pFrameRess;
		mDuration = pDuration;
		mLastFrameNo = pFrameRess.length - 1;

		playConstant(0);
	}

	private void playConstant(final int pFrameNo) {
		mImageView.postDelayed(new Runnable() {
			public void run() {
				mImageView.setBackgroundResource(mFrameRess[pFrameNo]);

				if (pFrameNo == mLastFrameNo) {
					if (loop) {
						playConstant(0);
					}
				} else {
					playConstant(pFrameNo + 1);
				}
			}
		}, mDuration);
	}
};
