package com.imooly.android.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.ui.PaymentActivity;
import com.imooly.android.ui.PaymentFailed;
import com.imooly.android.ui.PaymentSuccess;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/*
 * 微信支付成功和失败的借口
 * 必须是这个名字，这个activity被设置为透明全屏
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "WXPayEntryActivity";
	
	public static final String APP_ID = "wx501bd7cea77cc83a";
	
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);
        
        
//        Log.d(TAG, "onCreate............................");
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
        
//        Log.d(TAG, "onNewIntent............................");
	}

	@Override
	public void onReq(BaseReq req) {
		
//		Log.d(TAG, "onReq............................");
	}

	@Override
	public void onResp(BaseResp resp) {
		

//		Log.d(TAG, "onResp............................");
//		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		
		
		
		if (resp.errCode == 0) {  // 支付成功
			
			MoLiApplication app = (MoLiApplication) getApplication();
			String payno = app.getmPayNo();
			float price  = app.getmPrice();
			Intent intent = new Intent(this, PaymentSuccess.class);
			intent.putExtra(PaymentActivity.PAYNO, payno);
			intent.putExtra(PaymentActivity.PAY_MONEY, price);
			intent.putExtra(PaymentActivity.PAY_TYPE, "微信支付");
			this.startActivity(intent);
			this.finish();
			
		}else {
			this.startActivity(new Intent(this, PaymentFailed.class));
			this.finish();
		}

//		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("提示");
//			builder.setMessage("微信支付结果：%s" + resp.errCode);
//			builder.show();
//		}
	}
}