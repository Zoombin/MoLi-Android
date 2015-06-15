package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_User;
import com.imooly.android.entity.RspDiscountstore;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 实体店 评价
 * 
 * @author lsd
 * 
 */
public class StoreCommentActivity extends BaseActivity implements OnClickListener {
	public static String MODE = "mode";
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;

	private ImageView comm_top_pic;
	private TextView comm_top_name;
	private TextView comm_content_detail;
	private EditText comment_content;
	private RatingBar comment_ratingBar;

	private Button comment_commit;
	String businessid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_comment_commit);

		logActivityName(this);
		
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		comm_top_pic = (ImageView) findViewById(R.id.comm_top_pic);
		comm_top_name = (TextView) findViewById(R.id.comm_top_name);
		comm_content_detail = (TextView) findViewById(R.id.comm_content_detail);
		comment_content = (EditText) findViewById(R.id.comment_content);
		comment_ratingBar = (RatingBar) findViewById(R.id.comment_ratingBar);
		comment_commit = (Button) findViewById(R.id.comment_commit);
		comment_commit.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		
		RspDiscountstore model = (RspDiscountstore) intent.getSerializableExtra(MODE);
		if(model != null){
			businessid = model.data.getBusinessid();
			String icon  = model.data.getBusinessicon();
			ImageLoader.getInstance().displayImage(icon, comm_top_pic);
			comm_top_name.setText( model.data.getBusinessname());
			comm_content_detail.setText(model.data.getIndustry());
		}
		
	}

	private void comment(String contentTxt, float rating) {
		// TODO Auto-generated method stub
		DB_User db_User = new DB_User(self);
		Api.storeComment(self, businessid, db_User.getUserid(), rating + "", contentTxt,
				new NetCallBack<ServiceResult>() {
					@Override
					public void success(ServiceResult rspData) {
						// TODO Auto-generated method stub
						RspSuccessCommon rsp = (RspSuccessCommon) rspData;
						if (rsp.data != null && rsp.data.getSuccess() == 1) {
							Toast.show(self, "评论成功");
							setResult(RESULT_OK);
							finish();
						}else{
							String msg = rsp.data.getMessage();
							if(!TextUtils.isEmpty(msg)){
								Toast.show(self, msg);
							}
						}
					}

					@Override
					public void failed(String msg) {
						// TODO Auto-generated method stub
						if(!TextUtils.isEmpty(msg)){
							Toast.show(self, msg);
						}
					}
				}, RspSuccessCommon.class);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.comment_commit:
			if(!MoLiApplication.getInstance().isLogin()){
				Toast.show(self, "您还未登录");
				startActivity(new Intent(self,LoginActivity.class));
				return;
			}
			String contentTxt = comment_content.getText().toString();
			float rating = comment_ratingBar.getRating();
			if (TextUtils.isEmpty(contentTxt)) {
				Toast.show(self, "请输入评价内容");
				return;
			}
			comment(contentTxt, rating);
			break;
		default:
			break;
		}
	}
}
