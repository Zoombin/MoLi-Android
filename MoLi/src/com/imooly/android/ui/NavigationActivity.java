package com.imooly.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;

import com.imooly.android.R;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.fragment.Navigation1Fragment;
import com.imooly.android.fragment.Navigation2Fragment;
import com.imooly.android.fragment.Navigation3Fragment;
import com.imooly.android.fragment.Navigation4Fragment;
import com.imooly.android.fragment.Navigation5Fragment;

/**
 * 导航页
 * 
 * @author daiye
 * 
 */
public class NavigationActivity extends BaseActivity {

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);

		logActivityName(this);

		mViewPager = (ViewPager) findViewById(R.id.viewflipper);
		TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(adapter);
		mViewPager.setOffscreenPageLimit(5);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			
			private int preposition;
			 
			@Override
			public void onPageSelected(int position) {
				switch (preposition) {
				case 0:
					if (fragment1 != null) {
						((Navigation1Fragment) fragment1).stopAnimation();
					}
					break;
				case 1:
					if (fragment2 != null) {
						((Navigation2Fragment) fragment2).stopAnimation();
					}
					break;
				case 2:
					if (fragment3 != null) {
						((Navigation3Fragment) fragment3).stopAnimation();
					}
					break;
				case 3:
					if (fragment4 != null) {
						((Navigation4Fragment) fragment4).stopAnimation();
					}
					break;
				case 4:
					if (fragment5 != null) {
						((Navigation5Fragment) fragment5).stopAnimation();
					}
					break;
				default:
					
					break;
				}
				
				
				preposition = position;
				switch (position) {
				case 0:
					if (fragment1 != null) {
						((Navigation1Fragment) fragment1).startAnimation();
					}
					break;
				case 1:
					if (fragment2 != null) {
						((Navigation2Fragment) fragment2).startAnimation();
					}
					break;
				case 2:
					if (fragment3 != null) {
						((Navigation3Fragment) fragment3).startAnimation();
					}
					break;
				case 3:
					if (fragment4 != null) {
						((Navigation4Fragment) fragment4).startAnimation();
					}
					break;
				case 4:
					if (fragment5 != null) {
						((Navigation5Fragment) fragment5).startAnimation();
					}
					break;
				default:
					
					break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	static Fragment fragment1 = null;
	static Fragment fragment2 = null;
	static Fragment fragment3 = null;
	static Fragment fragment4 = null;
	static Fragment fragment5 = null;
	
	public static class TabFragmentPagerAdapter extends FragmentPagerAdapter {
		
		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int index) {

			switch (index) {
			case 0:
				if (fragment1 == null) {
					fragment1 = new Navigation1Fragment();
				}
				return fragment1;
			case 1:
				if (fragment2 == null) {
					fragment2 = new Navigation2Fragment();
				}
				return fragment2;
			case 2:
				if (fragment3 == null) {
					fragment3 = new Navigation3Fragment();
				}
				return fragment3;
			case 3:
				if (fragment4 == null) {
					fragment4 = new Navigation4Fragment();
				}
				return fragment4;
			case 4:
				if (fragment5 == null) {
					fragment5 = new Navigation5Fragment();
				}
				return fragment5;
			default:

				break;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 5;
		}
	}
}
