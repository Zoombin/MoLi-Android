package com.imooly.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.MyMessageAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_Message;
import com.imooly.android.entity.RspNewmsg;
import com.imooly.android.entity.RspNewmsg.Msg;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;
import com.imooly.android.widget.Toast;

/***
 * 我的消息
 * 
 * @author
 * 
 */
public class MyMessageActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout ll_title, rl_none_message;
	private ImageView iv_back;
	private TextView tv_title;
	private ListView listView;

	private MyMessageAdapter adapter;
	DB_Message db_Message;

	int curPage = 1;
	
	int localMsgSize = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mymessage);

		logActivityName(this);

		initView();
		initData();
		getData(curPage);
		

	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_message_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		listView = (ListView) findViewById(R.id.mymessage_list);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Msg message = (Msg) adapter.getItem(position);
				adapter.setReaded(position); // 设置已读
								
				TextView textView = (TextView) view.findViewById(R.id.mymessage_item_info);
				textView.setTextColor(self.getResources().getColor(R.color.app_text_gray));
				
				Intent intent = new Intent(MyMessageActivity.this, MyMessageDetailActivity.class);
				intent.putExtra("messageid", message.getMessageid());
				intent.putExtra("time", message.getSenddate());
				
				MyMessageActivity.this.startActivity(intent);
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				showDeleteMsgDialog(position);
				
				return false;
			}
		});
		
		rl_none_message = (RelativeLayout) findViewById(R.id.rl_none_message);
	}

	private void initData() {
		// TODO Auto-generated method stub
		if (adapter == null) {
			adapter = new MyMessageAdapter(this);
		}
		
		listView.setAdapter(adapter);

		// 获取本地消息
		db_Message = new DB_Message(self);
		List<Msg> list = new ArrayList<Msg>();		
		List<Msg> old = db_Message.getOldMessage();
		
		localMsgSize = list.size();
		
		adapter.setData(old);
		

	}

	private void getData(int pager) {
		// TODO Auto-generated method stub
		String lastpulltime = db_Message.getLastPullTime();
		//String lastpulltime = "0";

		// 获取消息
		Api.getNewMsgList(self, lastpulltime, pager, 1000, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspNewmsg rsp = (RspNewmsg) rspData;
				adapter.addData(rsp.data.getMsglist());
				db_Message.setLastPullTime();		
				
				// 处理没有数据的显示
				if (adapter.getCount() == 0) {
					rl_none_message.setVisibility(View.VISIBLE);
				}else {
					rl_none_message.setVisibility(View.GONE);
				}
			}

			@Override
			public void failed(String msg) {
			}
			
		}, RspNewmsg.class);
	}

	private void showDeleteMsgDialog(final int position) {
		final View view = LayoutInflater.from(self).inflate(R.layout.view_delete_message_dialog, null);
		final CustomDialog dialog = CustomDialog.create(self);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();
		// dialog no 按钮
		view.findViewById(R.id.no).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		// dialog yes 按钮
		view.findViewById(R.id.yes).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				Api.deleteMsg(self, ((Msg)adapter.getItem(position)).getMessageid(), new NetCallBack<ServiceResult>() {

					@Override
					public void success(ServiceResult rspData) {
						adapter.deleteData(position); // 删除一条消息
						
						// 没有内容的显示
						if (adapter.getCount() == 0) {
							rl_none_message.setVisibility(View.VISIBLE);
						}else {
							rl_none_message.setVisibility(View.GONE);
						}
					}

					@Override
					public void failed(String msg) {
						Toast.show(self, msg);
						
					}
				}, RspSuccessCommon.class);
				

				
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_message_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		List<Msg> oldList = adapter.getMsgList();
		if (oldList != null && oldList.size() > 0) {
			db_Message.setOldMessage(oldList);
		}
		super.onDestroy();
	}
}
