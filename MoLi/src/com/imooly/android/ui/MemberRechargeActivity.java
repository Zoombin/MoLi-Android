package com.imooly.android.ui;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspMemberFeeinfo;
import com.imooly.android.entity.RspMemberFeeinfo.FeeinfoEty;
import com.imooly.android.entity.RspMemberOrder;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.ClickUtil;
import com.imooly.android.utils.DensityUtil;
import com.imooly.android.widget.Toast;

public class MemberRechargeActivity extends BaseActivity implements OnClickListener, android.widget.RadioGroup.OnCheckedChangeListener,
		OnItemClickListener {

	private RelativeLayout rl_title;

	private Button button_pay;
	private TextView tv_title;
	private RadioGroup radioGroup;
	private RadioButton radioButton1, radioButton2;
	private TextView text_time, text_money, text_aggreement;
	private PopupWindow popupWindow = null;
	private String[] adapterDataYear = new String[] { "一年", "二年", "三年", "四年 ", "五年" };
	private String[] adapterDataMonth = new String[] { "一月", "二月", "三月", "四月 ", "五月", "六月", "七月", "八月", "九月 ", "十月", "十一月" };
	private ListView listView;
	private LinearLayout ll_time;
	private CheckBox check_box;

	private boolean mIsPayForYear = true; // 默认选中的是按年付费
	private boolean mIsPayForMonth = false;

	private int text_time_width = 0;

	float mPrice1Month = 0.0f;
	float mPrice1Year = 0.0f;
	int mSelected = 1;

	DecimalFormat fnum = new DecimalFormat("##0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_recharge);

		logActivityName(this);

		initView();

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
		check_box = (CheckBox) findViewById(R.id.chk_agree);
	}

	private void initData() {
		Api.memberfeeinfo(self, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspMemberFeeinfo rsp = (RspMemberFeeinfo) rspData;
				RspMemberFeeinfo.Data data = rsp.data;
				List<FeeinfoEty> lists = data.getFeelist();

				for (int i = 0; i < lists.size(); i++) {
					
					// 按年
					if (TextUtils.equals(lists.get(i).getType(), "year")) {
						mPrice1Year = lists.get(i).getFee();
						if (lists.get(i).getStatus() == 0) { // 隐藏按月付费的按钮
							radioButton1.setVisibility(View.GONE);
						}else {
							radioButton1.setVisibility(View.VISIBLE);
						}
					}

					// 按月
					if (TextUtils.equals(lists.get(i).getType(), "month")) {
						mPrice1Month = lists.get(i).getFee();

						if (lists.get(i).getStatus() == 0) { // 隐藏按月付费的按钮
							radioButton2.setVisibility(View.GONE);
						}else {
							radioButton2.setVisibility(View.VISIBLE);
						}
					}
				}

				text_money.setText("￥" + fnum.format(mPrice1Year));

			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}
		}, RspMemberFeeinfo.class);

	}

	private void initPopWindow(String[] adapterDataYear) {
		popupWindow = new PopupWindow(self);
		View popView = getLayoutInflater().inflate(R.layout.view_popwindow, null, false);
		listView = (ListView) popView.findViewById(R.id.list);
		listView.setOnItemClickListener(this);
		listView.setAdapter(new ArrayAdapter<String>(self, R.layout.item_list_pop_years, adapterDataYear));
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_bg_address));
		// popupWindow.setWidth(text_time_width);
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);

		popupWindow.setWidth((int) (((width - DensityUtil.dip2px(this, 20)) * 3.5 / 4.5)));

		popupWindow.setContentView(popView);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		Drawable rightDrawable = getResources().getDrawable(R.drawable.ic_address_arrow_down);
		rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());

		if (checkedId == R.id.radiobutton_1) {

			radioButton1.setTextColor(getResources().getColor(R.color.main_color));
			radioButton2.setTextColor(getResources().getColor(R.color.app_text_gray));

			text_time.setCompoundDrawables(null, null, rightDrawable, null);

			text_time.setText("一年");
			text_money.setText("￥" + fnum.format(mPrice1Year));

			mIsPayForYear = true;
			mIsPayForMonth = false;

		} else if (checkedId == R.id.radiobutton_2) {
			radioButton2.setTextColor(getResources().getColor(R.color.main_color));
			radioButton1.setTextColor(getResources().getColor(R.color.app_text_gray));

			text_time.setCompoundDrawables(null, null, rightDrawable, null);
			text_time.setText("一月");
			text_money.setText("￥" + fnum.format(mPrice1Month));

			mIsPayForYear = false;
			mIsPayForMonth = true;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	// 获取订单号，支付单号，支付金额跳转到支付界面
	private void goToPay(boolean isPayforYear) {

		if (!check_box.isChecked()) {
			Toast.show(self, "请同意魔力网服务条款！");
			return;
		}
		String type = "";
		int times = 0;

		if (isPayforYear) {
			type = "year";
			times = mSelected;

		} else {
			times = mSelected;
			type = "month";
		}

		Log.d("xxx", "times = " + times);

		Api.membeCharge(self, type, times, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspMemberOrder rsp = (RspMemberOrder) rspData;
				RspMemberOrder.Data data = rsp.data;

				((MoLiApplication) getApplication()).setMemeberPay(true);

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

		if (ClickUtil.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {

		case R.id.iv_back:

			finish();
			break;

		case R.id.button_pay:
			goToPay(mIsPayForYear);
			break;

		case R.id.text_time:
			if (mIsPayForYear == true) {
				initPopWindow(adapterDataYear);
				popupWindow.showAsDropDown(text_time);
			}
			if (mIsPayForMonth == true) {
				initPopWindow(adapterDataMonth);
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

		if (mIsPayForYear) { // 那年

			mSelected = position + 1;
			text_time.setText(adapterDataYear[position]);
			text_money.setText("￥" + fnum.format(mSelected * mPrice1Year));
			popupWindow.dismiss();

		} else { // 按月

			mSelected = position + 1;
			text_time.setText(adapterDataMonth[position]);
			text_money.setText("￥" + fnum.format(mSelected * mPrice1Month));
			popupWindow.dismiss();

		}

	}

}
