package com.imooly.android.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.view.View;
import android.view.View.OnClickListener;

public abstract class OnSingleClickListener implements OnClickListener {
    public boolean mEnable = true;
    public static final int mDelay = 1000;

    @Override
    public void onClick(View v) {
        if (mEnable) {
            mEnable = false;
            doClick(v);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mEnable = true;
                }
            }, mDelay);
        }
    }

    public abstract void doClick(View v);
}
