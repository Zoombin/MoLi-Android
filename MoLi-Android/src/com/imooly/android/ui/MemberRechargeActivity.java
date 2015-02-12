package com.imooly.android.ui;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspMemberFeeinfo;
import com.imooly.android.entity.RspMemberFeeinfo.FeeinfoEty;
import com.imooly.android.entity.RspMemberOrder;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.Toast;

public class MemberRechargeActivity extends BaseActivity implements OnClickListener, android.widget.RadioGroup.OnCheckedChangeListener, OnItemClickListener {

	private RelativeLayout rl_title;

	private Button button_pay;
	private TextView tv_title;
	private RadioGroup radioGroup;
	private RadioButton radioButton1, radioButton2;
	private TextView text_time, text_money, text_aggreement;
	private int payments[] = { 0, 0, 0, 0, 0 };
	private PopupWindow popupWindow = null;
	private String[] adapterData = new String[] { "一年", "二年", "三年", "四年 ", "五年" };
	private ListView listView;
	private LinearLayout ll_time;

	private boolean isPayForYear = true;

	private int text_time_width = 0;

	int try1Month = 0;
	int price1Year = 0;
	int mYearSelected = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_recharge);

		logActivityName(this);

		initView();
		initPopWindow();
		initData();

	}

	private void initView() {

		// title
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);
		findViewById(R.id.iv_back).setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);

		// 试用时间
		text_time = (TextView) findViewById(R.id.text_time);
		text_time.setOnClickListener(this);
		text_time_width = text_time.getLayoutParams().width;

		// 会员充值
		button_pay = (Button) findViewById(R.id.button_pay);
		button_pay.setOnClickListener(this);

		// 应付金额
		text_money = (TextView) findViewById(R.id.text_money);

		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		radioButton1 = (RadioButton) findViewById(R.id.radiobutton_1);
		radioButton2 = (RadioButton) findViewById(R.id.radiobutton_2);
		radioGroup.setOnCheckedChangeListener(this);

		ll_time = (LinearLayout) findViewById(R.id.ll_time);

		// 会员协议
		text_aggreement = (TextView) findViewById(R.id.txt_agreement);
		text_aggreement.setOnClickListener(this);
	}

	private void initData() {
		Api.memberfeeinfo(self, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspMemberFeeinfo rsp = (RspMemberFeeinfo) rspData;
				RspMemberFeeinfo.Data data = rsp.data;
				List<FeeinfoEty> lists = data.getFeelist();
				FeeinfoEty year = lists.get(0);
				FeeinfoEty tryFee = lists.get(2);
				try1Month = tryFee.fee;
				price1Year = year.fee;

				Log.e("xxx", "try1Month = " + try1Month);
				Log.e("xxx", "price1Year = " + price1Year);

				for (int i = 0; i < payments.length; i++) {
					payments[i] = (i + 1) * price1Year;
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub

			}
		}, RspMemberFeeinfo.class);

	}

	private void initPopWindow() {
		popupWindow = new PopupWindow(self);
		View popView = getLayoutInflater().inflate(R.layout.view_popwindow, null, false);
		listView = (ListView) popView.findViewById(R.id.list);
		listView.setOnItemClickListener(this);
		listView.setAdapter(new ArrayAdapter<String>(self, R.layout.item_list_pop_years, adapterData));
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_bg_address));
		popupWindow.setWidth(text_time_width);
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setContentView(popView);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		if (checkedId == R.id.radiobutton_1) {

			radioButton1.setTextColor(getResources().getColor(R.color.main_color));
			radioButton2.setTextColor(getResources().getColor(R.color.app_text_gray));

			Drawable rightDrawable = getResources().getDrawable(R.drawable.ic_address_arrow_down);
			rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
			text_time.setCompoundDrawables(null, null, rightDrawable, null);

			text_time.setText("一年");
			text_money.setText("￥" + payments[0]);

			isPayForYear = true;

		} else if (checkedId == R.id.radiobutton_2) {
			radioButton2.setTextColor(getResources().getColor(R.color.main_color));
			radioButton1.setTextColor(getResources().getColor(R.color.app_text_gray));

			text_time.setCompoundDrawables(null, null, null, null);
			text_time.setText("一月");
			text_money.setText("￥" + try1Month);

			isPayForYear = false;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	// 获取订单号，支付单号，支付金额跳转到支付界面
	private void goToPay(boolean isPayforYear) {
		String type = "";
		int times = 0;

		if (isPayforYear == false) {
			type = "try";
			times = 1;
		} else {
			type = "year";
			times = mYearSelected + 1;
		}

		Api.membeCharge(self, type, mYearSelected, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspMemberOrder rsp = (RspMemberOrder) rspData;
				RspMemberOrder.Data data = rsp.data;
				Intent intent = new Intent(self, PaymentActivity.class);
				intent.putExtra(PaymentActivity.GOOD_NAME, data.getPaysubject());
				intent.putExtra(PaymentActivity.GOOD_INFO, data.getPaybody());
				intent.putExtra(PaymentActivity.PAYNO, data.getPayno());
				intent.putExtra(PaymentActivity.PAY_MONEY, data.getPayamount());
				startActivity(intent);
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}
			
		}, RspMemberOrder.class);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.iv_back:

			finish();
			break;

		case R.id.button_pay:
			goToPay(isPayForYear);
			break;

		case R.id.text_time:

			if (isPayForYear == true) {
				popupWindow.showAsDropDown(text_time);
			}

			break;
		case R.id.txt_agreement:
			Intent intent2 = new Intent(self, AgreementActivity.class);
			intent2.putExtra(AgreementActivity.EXTRA, AgreementActivity.EXTRA_VIP);
			self.startActivity(intent2);
			break;

		default:
			break;

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mYearSelected = position;
		text_time.setText(adapterData[position]);
		text_money.setText("￥" + payments[position]);
		popupWindow.dismiss();
	}

}
