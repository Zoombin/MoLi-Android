package com.imooly.android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.widget.Button;

import com.imooly.android.R;

/**
 * 倒计时button
 * @author daiye
 *
 */
public class CountDownButton extends CountDownTimer {
	public static final int TIME_COUNT_FUTURE = 120000;
	public static final int TIME_COUNT_INTERVAL = 1000;
	
	private Context mContext;
	private Button mButton;
	private String mOriginalText;
	private Drawable mOriginalBackground;
	
	public CountDownButton(Context context, Button button) {
		super(TIME_COUNT_FUTURE, TIME_COUNT_INTERVAL);
		this.mContext = context;
		this.mButton = button;
		this.mOriginalText = mButton.getText().toString();
		this.mOriginalBackground = mButton.getBackground();
	}
	
	public CountDownButton(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}

	@Override
	public void onFinish() {
		if (mContext != null && mButton != null) {
			mButton.setText(mOriginalText);
			mButton.setBackgroundDrawable(mOriginalBackground);
			mButton.setClickable(true);
		}
	}

	@Override
	public void onTick(long millisUntilFinished) {
		if (mContext != null && mButton != null) {
			mButton.setClickable(false);
			mButton.setBackgroundResource(R.drawable.btn_round_gray);
			mButton.setText(millisUntilFinished / 1000 + mContext.getResources().getText(R.string.get_verifycode).toString());

		}
	}
}
