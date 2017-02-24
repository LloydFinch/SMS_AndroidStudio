package com.delta.smsandroidproject.view.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.delta.common.utils.LogUtils;
import com.delta.smsandroidproject.BuildConfig;
import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.model.IbackInterface;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.GoogleUtil;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.ToastCustom;
import com.delta.smsandroidproject.util.ToolUtil;
import com.delta.smsandroidproject.util.WebSocketRegisterTool;
import com.delta.smsandroidproject.view.fragment.AboutFragment;
import com.delta.smsandroidproject.view.fragment.BaseFragment;
import com.delta.smsandroidproject.view.fragment.ChargerMapFragment;
import com.delta.smsandroidproject.view.fragment.ChargingFunctionFragment;
import com.delta.smsandroidproject.view.fragment.DashboardFragment;
import com.delta.smsandroidproject.view.fragment.RoutePlannerFragment;
import com.delta.smsandroidproject.view.fragment.SelectNetWorkFragment;
import com.delta.smsandroidproject.view.fragment.ServiceRoutePlannerFragment;
import com.delta.smsandroidproject.view.fragment.SettingFragment;

/**
 * Moterial desgin framework
 * 
 * @author Jianzao.Zhang
 * 
 */
public class MainActivity extends AppCompatActivity implements
		NavigationView.OnNavigationItemSelectedListener, IbackInterface {

	private static final boolean DEBUG = BuildConfig.DEBUG;
	private static final String LOG_TAG = MainActivity.class.getSimpleName();
	private static final long DRAWER_CLOSE_DELAY_MS = 250;

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	private final Handler mDrawerActionHandler = new Handler();
	public static FragmentManager mFragmentManager;
	private AppBarLayout mAppBarLayout;
	private int mNavItemId;
	public DrawerLayout mDrawerLayout;

	public static CoordinatorLayout SnackBarContainer;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private CharSequence mSubtitle;
	private TabLayout mTabLayout;
	private BaseFragment baseFragment;
	private MainActivity mContext;
	private boolean isFrist = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		SMSApplication.getInstance().addActivity(this);

		SnackBarContainer = (CoordinatorLayout) findViewById(R.id.snack_container);
		// Set up fragment manager
		mFragmentManager = getSupportFragmentManager();
		mFragmentManager
				.beginTransaction()
				.replace(R.id.container, DashboardFragment.getInstance(),
						DashboardFragment.TAG).commit();

		// Set up the Toolbar & TabLayout
		setUpToolbar();

		// Set up the NavigationView
		setUpNavigationView(savedInstanceState);

		// direct the user to the target fragment
		navigate();

		// 注册WebSocket进行监听
		String path = Comment.BASE_URL.replace("http", "ws") + "WebSocket";
		Logg.i("Websocket-path:", path);
		WebSocketRegisterTool.registerWebSocket(getApplicationContext(), path);
	}

	public FragmentManager getFragmentManagers() {
		return mFragmentManager;
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mNavItemId);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Log.e("查看ItemID", " " + item.getItemId());
		// if (isFrist) {
		// mDrawerLayout.openDrawer(GravityCompat.START);
		// Fragment fragment = mFragmentManager
		// .findFragmentByTag(AboutFragment.TAG);
		// Log.i("fragment111111", "" + fragment);
		// Log.i("fragmentsize",
		// "" + mFragmentManager.getBackStackEntryCount());
		// if (fragment != null
		// && mFragmentManager.getBackStackEntryCount() > 0) {
		// mDrawerLayout.closeDrawer(GravityCompat.START);
		// mFragmentManager.popBackStack();
		// }
		//
		// for (Fragment f : mFragmentManager.getFragments()) {
		// Log.i("fragment--", "" + f);
		// }
		//
		// return true;
		// }
		switch (item.getItemId()) {
		case android.R.id.home:
			mDrawerLayout.openDrawer(GravityCompat.START);
			Fragment fragment = mFragmentManager
					.findFragmentByTag(DashboardFragment.TAG);
			Fragment aboutfragment = mFragmentManager
					.findFragmentByTag(AboutFragment.TAG);
			Log.i("fragment222222", "" + fragment);
			Log.i("fragmentsize",
					"" + mFragmentManager.getBackStackEntryCount());
			if (fragment != null
					&& mFragmentManager.getBackStackEntryCount() > 0
					|| (aboutfragment != null && mFragmentManager
							.getBackStackEntryCount() > 0)) {
				mDrawerLayout.closeDrawer(GravityCompat.START);
				mFragmentManager.popBackStack();
			}

			for (Fragment f : mFragmentManager.getFragments()) {
				Log.i("fragment--", "" + f);
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void setActionBarTitle(String title) {
		setActionBarTitle(title, null);
	}

	public void setActionBarTitle(String title, String subtitle) {
		mTitle = title;
		mSubtitle = subtitle;
		getSupportActionBar().setTitle(title);
		getSupportActionBar().setSubtitle(subtitle);
	}

	public AppBarLayout getAppBarLayout() {
		return mAppBarLayout;
	}

	public TabLayout getTabLayout() {
		return mTabLayout;
	}

	private void setUpToolbar() {
		mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);

		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
		mTabLayout.setVisibility(View.GONE);
	}

	private void setUpNavigationView(Bundle savedInstanceState) {
		NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation);
		MenuItem menu = mNavigationView.getMenu().findItem(
				R.id.item_route_planner);
		if (ToolUtil.notService()) {
			menu.setVisible(false);
		} else {
			menu.setVisible(true);
		}
		mNavigationView.setNavigationItemSelectedListener(this);

		// load saved navigation state if present
		if (null == savedInstanceState) {
			mNavItemId = R.id.item_dash_board;
		} else {
			mNavItemId = savedInstanceState.getInt(STATE_SELECTED_POSITION);
		}
		mNavigationView.getMenu().findItem(mNavItemId).setChecked(true);
		mNavigationView.setItemIconTintList(null);// 解决侧滑菜单图标黑色问题
		// Set up the DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		View drawerHeader = mNavigationView.getHeaderView(0);
		TextView userName = (TextView) drawerHeader
				.findViewById(R.id.user_name_text);
		userName.setText(ToolUtil.getUid());
		TextView userMail = (TextView) drawerHeader
				.findViewById(R.id.user_email_text);
		userMail.setText("");
	}

	/**
	 * intent to other ui
	 */
	private void navigate() {
		// clear the back stack
		mFragmentManager.popBackStackImmediate(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
		isFrist = false;
		// mFragmentManager
		// .beginTransaction()
		// .replace(R.id.container, DashboardFragment.getInstance(),
		// DashboardFragment.TAG).addToBackStack(null).commit();

		// perform the action according to mNavItemId
		switch (mNavItemId) {

		case R.id.item_dash_board:
			mTitle = getString(R.string.title_dashborad);
			// mFragmentManager
			// .beginTransaction()
			// .replace(R.id.container, DashboardFragment.getInstance(),
			// DashboardFragment.TAG).addToBackStack(null)
			// .commit();
			// mFragmentManager.popBackStackImmediate(null,
			// FragmentManager.POP_BACK_STACK_INCLUSIVE);
			mFragmentManager
					.beginTransaction()
					.replace(R.id.container, DashboardFragment.getInstance(),
							DashboardFragment.TAG).commit();

			break;

		case R.id.item_charging_functions:
			mTitle = getString(R.string.title_charging_fuctions);
			mFragmentManager
					.beginTransaction()
					.replace(R.id.container, new ChargingFunctionFragment(),
							ChargingFunctionFragment.TAG).addToBackStack(null)
					.commit();
			break;
		case R.id.item_map:
			if (GoogleUtil.isSupportGoogMap()) {
				mTitle = getString(R.string.chargig_location_map);
				mFragmentManager
						.beginTransaction()
						.replace(R.id.container, new ChargerMapFragment(),
								ChargerMapFragment.TAG).addToBackStack(null)
						.commit();
			} else {
				ToastCustom.showToast(mContext, mContext.getResources()
						.getString(R.string.goolge_map_no_support),
						ToastCustom.LENGTH_SHORT);
			}
			break;

		case R.id.item_route_planner:
			if (!ToolUtil.notService()) {
				mTitle = getString(R.string.title_route_planner);
				mFragmentManager
						.beginTransaction()
						.replace(R.id.container,
								ServiceRoutePlannerFragment.getInstance())
						.addToBackStack(RoutePlannerFragment.TAG).commit();
			}

			break;

		case R.id.item_network_setting:
			mTitle = getString(R.string.title_network_setting);
			mFragmentManager
					.beginTransaction()
					.replace(R.id.container, new SelectNetWorkFragment(),
							SelectNetWorkFragment.TAG).addToBackStack(null)
					.commit();
			break;

		case R.id.item_logout:
			mTitle = getString(R.string.title_logout);
			startActivity(new Intent(MainActivity.this, LoginActivity.class));
			finish();
			break;

		case R.id.item_settings:
			mTitle = getString(R.string.title_settings);
			mFragmentManager
					.beginTransaction()
					.replace(R.id.container, new SettingFragment(),
							SettingFragment.TAG).addToBackStack(null).commit();
			break;
		case R.id.item_about:
			isFrist = true;
			mTitle = getString(R.string.title_about);
			mFragmentManager
					.beginTransaction()
					.replace(R.id.container, new AboutFragment(),
							AboutFragment.TAG).commit();
			break;

		default:
			break;
		}
		setActionBarTitle((String) mTitle);

	}

	@Override
	public boolean onNavigationItemSelected(final MenuItem menuItem) {
		if (DEBUG) {
			Log.d(LOG_TAG, "onNavigationItemSelected(): menuItem = " + menuItem);
		}

		// update highlighted item in the navigation menu
		menuItem.setChecked(true);
		mNavItemId = menuItem.getItemId();

		// allow some time after closing the drawer before performing real
		// navigation
		// so the user can see what is happening
		mDrawerLayout.closeDrawer(GravityCompat.START);
		mDrawerActionHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				navigate();
			}
		}, DRAWER_CLOSE_DELAY_MS);
		return false;
	}

	private static Boolean isExit = false;

	@Override
	public void onBackPressed() {
		if (baseFragment == null || !baseFragment.onBackPress()) {
			isExit = false;
			if (mFragmentManager.getBackStackEntryCount() != 0) {
				mFragmentManager.popBackStack();
			} else {
				LogUtils.i("onBackPressed", "exit? :" + isExit);
			}
		} else {
			exitBy2Click();
		}
	}

	private void exitBy2Click() {
		Timer tExit = null;

		if (isExit == false) {
			isExit = true;
			Toast.makeText(this, this.getString(R.string.back_toast),
					Toast.LENGTH_SHORT).show();
			tExit = new Timer();

			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);

		} else {
			moveTaskToBack(true);
		}
	}

	@Override
	public void setSelectedFragment(BaseFragment baseFragment) {
		this.baseFragment = baseFragment;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSApplication.getInstance().removeActivity(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtils.i("GoogleApiClient", "REQUEST_CHECK_SETTINGS_mainActivity");
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void setIsFrist(boolean isFirst) {
		this.isFrist = isFirst;
	}
}
