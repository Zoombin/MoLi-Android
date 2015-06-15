package com.imooly.android.ui;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.fragment.CategoryFragment;
import com.imooly.android.fragment.HomeFragment;
import com.imooly.android.fragment.MyCenterFragment;
import com.imooly.android.fragment.ShopCartFragment;
import com.imooly.android.fragment.StoreFragment;
import com.imooly.android.service.MoLiService;
import com.imooly.android.tool.Config;
import com.imooly.android.tool.Logger;
import com.imooly.android.widget.Toast;

/**
 * 主画面
 * 
 * @author daiye
 * 
 */
public class MainActivity extends BaseActivity {
	public static String ACTION = "com.imooly.android.main";
	// /** 再按一次退出程序 */
	private long exitTime = 0;
	private int currentTabIndex;
	private Fragment[] fragments;
	private int index;
	private Button[] mTabs;
	private HomeFragment homefragment;
	private CategoryFragment categoryfragment;
	private StoreFragment storefragment;
	private ShopCartFragment shopcartfragment;
	private MyCenterFragment mycenterfragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		logActivityName(this);
		
		registerReceiver();

		homefragment = new HomeFragment();
		categoryfragment = new CategoryFragment();
		storefragment = new StoreFragment();
		shopcartfragment = new ShopCartFragment();
		mycenterfragment = new MyCenterFragment();
		Fragment[] arrayOfFragment = new Fragment[5];
		arrayOfFragment[0] = homefragment;
		arrayOfFragment[1] = categoryfragment;
		arrayOfFragment[2] = storefragment;
		arrayOfFragment[3] = shopcartfragment;
		arrayOfFragment[4] = mycenterfragment;
		fragments = arrayOfFragment;
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, homefragment).show(homefragment)
				.commit();
	}

	private void initView() {
		this.mTabs = new Button[5];
		this.mTabs[0] = ((Button) findViewById(R.id.button_home));
		this.mTabs[1] = ((Button) findViewById(R.id.button_sort));
		this.mTabs[2] = ((Button) findViewById(R.id.button_store));
		this.mTabs[3] = ((Button) findViewById(R.id.button_shopcart));
		this.mTabs[4] = ((Button) findViewById(R.id.button_mycenter));
		this.mTabs[0].setSelected(true);
	}

	public void onTabClicked(View v) {
		tabSelect(v.getId());
	}

	public void tabSelect(int viewid) {
		switch (viewid) {
		case R.id.button_home:
			index = 0;
			selectTab();
			break;
		case R.id.button_sort:
			index = 1;
			selectTab();
			break;
		case R.id.button_store:
			index = 2;
			selectTab();
			break;
		case R.id.button_shopcart:
			index = 3;
			selectTab();
			if(shopcartfragment != null){
				shopcartfragment.refrashData();
			}
			break;
		case R.id.button_mycenter:
			index = 4;
			selectTab();
			break;
		default:
			break;
		}
	}
	
	private void selectTab() {
		hideCategoryListView();
		if (currentTabIndex != index) {
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				transaction.add(R.id.fragment_container, this.fragments[index]);
			}
			transaction.show(fragments[index]).commitAllowingStateLoss();
		}
		mTabs[currentTabIndex].setSelected(false);
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}
	
	private void hideCategoryListView() {
		if (currentTabIndex == 1) {
			categoryfragment.hideListView();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("Tag", "Main -- onResume");
		if (3 == currentTabIndex) {
			//重新打开购物车  刷新数据
			Config.curShopCarNum = 0;
			shopcartfragment.refrashData();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.show(this, "再按一次退出程序");
				exitTime = System.currentTimeMillis();
			} else {
				MoLiApplication.getInstance().setLogin(false);

				// 停止刷新tiket
				Intent intent = new Intent(this, MoLiService.class);
				stopService(intent);

				// finish();
				activityManager.popAllActivity();
				MoLiApplication.getInstance().onTerminate();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
//		if (2 == currentTabIndex) {
//			//切换城市返回  实体店  刷新数据
//			storefragment.refrashData();
//		}
	}
	
	@Override
	protected void onDestroy() {
		unRegisterReceiver();
		super.onDestroy();
	}

	/**
	 * 回调接口
	 * 
	 * @author
	 * 
	 */
	public interface MyTouchListener {
		public void onTouchEvent(MotionEvent event);
	}

	/**
	 * 保存MyTouchListener接口的列表
	 */
	private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MainActivity.MyTouchListener>();

	/**
	 * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
	 * 
	 * @param listener
	 */
	public void registerMyTouchListener(MyTouchListener listener) {
		myTouchListeners.add(listener);
	}

	/**
	 * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
	 * 
	 * @param listener
	 */
	public void unRegisterMyTouchListener(MyTouchListener listener) {
		myTouchListeners.remove(listener);
	}

	/**
	 * 分发触摸事件给所有注册了MyTouchListener的接口
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		for (MyTouchListener listener : myTouchListeners) {
			listener.onTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	/***
	 * 注册广播
	 */
	private void registerReceiver() {
		IntentFilter intent = new IntentFilter(ACTION);
		registerReceiver(broadcastReceiver, intent);
	}

	/***
	 * 取消注册
	 */
	private void unRegisterReceiver() {
		unregisterReceiver(broadcastReceiver);
	}

	/***
	 * 首页广播接收
	 * 
	 */
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && intent.getAction() != null) {
				// 接收到广播
				String action = intent.getAction();
				if (ACTION.equals(action)) {
					activityManager.popTopActivity();

					if (intent.hasExtra("viewid")) {
						int tag = intent.getIntExtra("viewid", R.id.button_home);
						tabSelect(tag);
					}
				}
			}
		}
	};
}
