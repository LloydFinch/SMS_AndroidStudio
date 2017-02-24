package com.delta.smsandroidproject.view.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delta.common.utils.GsonTools;
import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.adapter.ChargerListAdapter;
import com.delta.smsandroidproject.adapter.ChargerListAdapter.ChargerUploadImg;
import com.delta.smsandroidproject.adapter.WrappingLinearLayoutManager;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.ChargerInfoData;
import com.delta.smsandroidproject.bean.ChargerListData;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.bean.InfoByPush;
import com.delta.smsandroidproject.bean.InfoByPush.LocationPush;
import com.delta.smsandroidproject.bean.LocationInfoData;
import com.delta.smsandroidproject.presenter.GetChargerInfoPresenter;
import com.delta.smsandroidproject.presenter.GetChargerInfoPresenter.IGetChargerInfo;
import com.delta.smsandroidproject.presenter.GetChargerListPresenter;
import com.delta.smsandroidproject.presenter.GetChargerListPresenter.IGetChargerList;
import com.delta.smsandroidproject.presenter.GetLocationInfoPresenter;
import com.delta.smsandroidproject.presenter.GetLocationInfoPresenter.getLocationInfo;
import com.delta.smsandroidproject.presenter.PutUploadPresenter;
import com.delta.smsandroidproject.presenter.PutUploadPresenter.UpLoadImg;
import com.delta.smsandroidproject.presenter.ipresenter.IPresenter;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.GoogleUtil;
import com.delta.smsandroidproject.util.ToastCustom;
import com.delta.smsandroidproject.util.ToolUtil;
import com.delta.smsandroidproject.view.activity.CameraActivity;
import com.delta.smsandroidproject.view.activity.MainActivity;
import com.delta.smsandroidproject.view.iview.ILoadView;
import com.delta.smsandroidproject.webrequest.RequestItem;
import com.squareup.picasso.Picasso;

public class LocationFragment extends BaseFragment implements IGetChargerInfo,
		IGetChargerList, getLocationInfo, ILoadView, OnClickListener,
		UpLoadImg, ChargerUploadImg, OnRefreshListener {
	public static final String TAG = "LocationFragment";
	private static final String EXTRA_IMAGE = "com.antonioleiva.materializeyourapp.extraImage";
	private static final String EXTRA_TITLE = "com.antonioleiva.materializeyourapp.extraTitle";
	public static final int NULL = 9001;
	public static final int REQUEST_PHOTO = 1001;
	public static final int PASS_CAMERA = 1002;
	public static final int PASS_PHOTO = 1003;
	public static final int SEND_PHOTO = 1004;

	public static final int CAMERA = 3001;
	public static final int CROP = 3002;
	public static final int SET = 3003;
	public static final int REQUEST_PHOTO_CH = 1011;
	public static final int PASS_CAMERA_CH = 1012;
	public static final int PASS_PHOTO_CH = 1013;
	public static final int SEND_PHOTO_CH = 1014;

	public static final int CAMERA_CH = 3011;
	public static final int CROP_CH = 3012;
	public static final int SET_CH = 3013;
	private Bitmap ticketPhoto;
	public static String path = "/sdcard/ticket_photo/";
	public static int count = 0;
	public static List<Bitmap> photos = new ArrayList<>();
	private static Map<Bitmap, String> imagePaths = new HashMap<>();
	// public static String TAG = "LocationFragment";
	private CollapsingToolbarLayout collapsingToolbarLayout;
	private RecyclerView Charger_info;
	private Toolbar toolbar, maintoolbar, toolbar1;
	private IPresenter infoPresenter, listPresenter, locationInfoPresenter,
			locationRePre;
	private ArrayList<ChargerInfoData> infoData;
	private ArrayList<ChargerInfoData> infoSum;
	private int removePosition = 0;
	private ArrayList<ChargerListData> chargerListDatas;
	private ChargerListAdapter adapter;
	private TextView station_name, address, phone_number, descripton,
			coordinates, lat, mail, mobile;
	private Map<String, String> locInfMap, chIfMap;
	private ImageView image, ivCamera;
	private LinearLayout lin_mail, lin_lat, lin_coordinates, lin_note,
			lin_phone, lin_adress, lin_chargername, lin_mobile;
	private View view;
	private Bitmap bitmap;
	private PutUploadPresenter upLoadPresenter;
	private ChargerLocationData chargerLocationdata;
	private List<RequestItem> requestList, ch_requestList;
	private ProgressDialog mDialog;
	private boolean loc_finish = false;
	private boolean ch_finish = false;
	private RelativeLayout coo;
	private SwipeRefreshLayout swipe_refresh_widget;
	private AppBarLayout appBar;
	public static LocationFragment instance;

	// 163 同一个
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = inflater.inflate(R.layout.location_info, container, false);
		coo = (RelativeLayout) getActivity().findViewById(R.id.container);
		initView(view);
		this.view = view;
		initPresenter();

		if (Integer.valueOf(android.os.Build.VERSION.SDK) < 22) {

		} else {// R.id.container
			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) coo
					.getLayoutParams();
			linearParams.height = coo.getMeasuredHeight()
					+ getStatusBarHeight(getActivity())
					+ maintoolbar.getMeasuredHeight();
			coo.setLayoutParams(linearParams);
			coo.scrollBy(0, 0 - getStatusBarHeight(getActivity()));
		}
		instance = this;
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		// instance = null;
		super.onDestroy();
	}

	private void initPresenter() {
		chIfMap = new HashMap<String, String>();
		locInfMap = new HashMap<String, String>();
		upLoadPresenter = new PutUploadPresenter(this, this,getActivity());
		infoPresenter = new GetChargerInfoPresenter(this, this,getActivity());
		listPresenter = new GetChargerListPresenter(this, this,getActivity());
		locationInfoPresenter = new GetLocationInfoPresenter(this,getActivity());
		// ChargerLocationData data = (ChargerLocationData)
		// this.getArguments().getSerializable("ChargerLocationData");
		loadData();

	}

	private void loadData() {
		Bundle bundle = this.getArguments();
		if (bundle == null) {
			locInfMap.put("key", "2");
			locationInfoPresenter.loadData(locInfMap);
		} else {

			chargerLocationdata = (ChargerLocationData) bundle
					.getSerializable("ChargerLocationData");
			locInfMap.put("key", chargerLocationdata.getId());
			locationInfoPresenter.loadData(locInfMap);
			requestList = new ArrayList<RequestItem>();
			requestList.add(new RequestItem("Name", chargerLocationdata
					.getName()));
			requestList.add(new RequestItem("Id", chargerLocationdata.getId()));
			requestList.add(new RequestItem("NetworkId", ToolUtil
					.getNetworkId()));
		}
		infoPresenter.loadData(locInfMap);

	}

	public void SocketUpdata(String jsonData) {
		// instance.isVisible();
		Log.e("显示推送", jsonData);
		if (instance != null && instance.isVisible()
				&& !(TextUtils.isEmpty(jsonData))) {
			try {
				InfoByPush pushInfo = GsonTools.changeGsonToBean(jsonData,
						InfoByPush.class);

				LocationPush location = pushInfo.getLocation();
				String LocationID = location.getLocationId();
				Log.e("显示推送消息", LocationID + " " + location.getLocationId());
				if (LocationID.equals(chargerLocationdata.getId())) {
					locInfMap.clear();
					locInfMap.put("key", LocationID);
					if (chargerListDatas!=null) {
						chargerListDatas.clear();
					}
					locationInfoPresenter.loadData(locInfMap);
					infoPresenter.loadData(locInfMap);
				}
			} catch (Exception e) {
			}
		}
	}

	private void loadLocation() {
		locationInfoPresenter.loadData(locInfMap);
	}

	private void initLocationInfo() {
		loc_finish = false;
		ch_finish = false;
		locationInfoPresenter.loadData(locInfMap);
		infoPresenter.loadData(locInfMap);
	}

	@Override
	public void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();

	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void initView(View view) {
		loc_finish = false;
		ch_finish = false;
		ivCamera = (ImageView) view.findViewById(R.id.camera);
		if (ToolUtil.notService()) {
			ivCamera.setVisibility(View.GONE);
		} else {
			ivCamera.setVisibility(View.VISIBLE);
		}
		ivCamera.setOnClickListener(this);
		lin_mail = (LinearLayout) view.findViewById(R.id.lin_mail);
		lin_coordinates = (LinearLayout) view
				.findViewById(R.id.lin_coordinates);
		lin_coordinates.setOnClickListener(this);
		lin_note = (LinearLayout) view.findViewById(R.id.lin_note);

		lin_phone = (LinearLayout) view.findViewById(R.id.lin_phone);
		lin_phone.setOnClickListener(this);
		lin_adress = (LinearLayout) view.findViewById(R.id.lin_adress);
		lin_adress.setOnClickListener(this);
		lin_chargername = (LinearLayout) view
				.findViewById(R.id.lin_chargername);
		lin_mobile = (LinearLayout) view.findViewById(R.id.lin_mobile);
		lin_mobile.setOnClickListener(this);
		chargerListDatas = new ArrayList<>();
		infoData = new ArrayList<ChargerInfoData>();
		adapter = new ChargerListAdapter(getActivity(), this);
		appBar = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
		ViewCompat.setTransitionName(appBar, EXTRA_IMAGE);
		getActivity().supportPostponeEnterTransition();
		toolbar = (Toolbar) view.findViewById(R.id.toolbar1);
		toolbar1 = (Toolbar) view.findViewById(R.id.toolbar12);
		maintoolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
		maintoolbar.setVisibility(View.GONE);
		// maintoolbar.setVisibility(View.GONE);1555255-1443705-25*200 = 106550
		// ((MainActivity) getActivity()).getActionBar().hide();
		Charger_info = (RecyclerView) view.findViewById(R.id.li_recyclerview);
		// StaggeredGridLayoutManager layoutManager = new
		// StaggeredGridLayoutManager(1,LinearLayoutManager.VERTICAL);
		Charger_info.setNestedScrollingEnabled(false);
		Charger_info.setLayoutManager(new WrappingLinearLayoutManager(
				getActivity()));
		Charger_info.setAdapter(adapter);

		toolbar.setNavigationIcon(R.drawable.ic_back);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				MainActivity.mFragmentManager.popBackStack();

			}
		});

		collapsingToolbarLayout = (CollapsingToolbarLayout) view
				.findViewById(R.id.collapsing_toolbar);

		collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(
				android.R.color.transparent));
		collapsingToolbarLayout.setContentScrimColor(getResources().getColor(
				R.color.theme_primary));
		image = (ImageView) view.findViewById(R.id.image);

		// Picasso.with(getActivity()).load("http://i.imgur.com/DvpvklR.png")
		// .placeholder(R.drawable.dvpvklr).error(R.drawable.dvpvklr)
		// .into(image);

		TextView title = (TextView) view.findViewById(R.id.title);
		title.setVisibility(View.GONE);
		station_name = (TextView) view.findViewById(R.id.charger_text1);
		address = (TextView) view.findViewById(R.id.address_text1);
		phone_number = (TextView) view.findViewById(R.id.phone_text1);
		descripton = (TextView) view.findViewById(R.id.descripion_text1);
		coordinates = (TextView) view.findViewById(R.id.cs_coordinates);
		mail = (TextView) view.findViewById(R.id.cs_mail);
		mobile = (TextView) view.findViewById(R.id.mobile_text1);

		swipe_refresh_widget = (SwipeRefreshLayout) view
				.findViewById(R.id.swipe_refresh_widget);
		swipe_refresh_widget.setOnRefreshListener(this);
		appBar.addOnOffsetChangedListener(new OnOffsetChangedListener() {

			@Override
			public void onOffsetChanged(AppBarLayout arg0, int verticalOffset) {
				if (verticalOffset >= 0) {
					swipe_refresh_widget.setEnabled(true);
				} else {
					swipe_refresh_widget.setEnabled(false);
				}
			}
		});
		// swipe_refresh_widget.setRefreshing(true);
	}

	@Override
	public boolean onBackPress() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void onDestroyView() {
		// TODO 自动生成的方法存根

		if (Integer.valueOf(android.os.Build.VERSION.SDK) < 22) {
		} else {
			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) coo
					.getLayoutParams();
			linearParams.height = coo.getMeasuredHeight()
					- getStatusBarHeight(getActivity())
					- maintoolbar.getMeasuredHeight();
			coo.setLayoutParams(linearParams);
			// coo.scrollBy(0, 0 - getStatusBarHeight(getActivity()));
			coo.scrollBy(0, getStatusBarHeight(getActivity()));
		}
		super.onDestroyView();
		toolbar = null;
		maintoolbar.setVisibility(View.VISIBLE);
	}

	@Override
	public synchronized void getChargerListSuccess(ChargerListData datas) {
		// TODO 自动生成的方法存根
		Log.e("显示evse内容", datas.toString());
		chargerListDatas.add(datas);
		if (infoSum.size()>0) {
			infoSum.remove(0);
		}
		
		if (!(infoSum.size() < 1)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("key", infoSum.get(0).getId());
			listPresenter.loadData(map);
		} else {
			ch_finish = true;

			Charger_info.setAdapter(adapter);
			adapter.setData(infoData, chargerListDatas);
			dissLoading();
			return;
		}
	}

	@Override
	public void getChargerListFailed() {
		// TODO 自动生成的方法存根
		ch_finish = true;
	}

	@Override
	public synchronized void getChargerInfoSuccess(ArrayList<ChargerInfoData> datas) {
		// TODO 自动生成的方法存根
		// infoData = datas;
		if (datas.size() > 0) {
			infoSum = datas;
			infoData.clear();
			infoData.addAll(datas);
			Map<String, String> map = new HashMap<String, String>();
			map.put("key", infoSum.get(0).getId());
			listPresenter.loadData(map);
		} else {
			// Toast.makeText(getActivity(), "返回数据为空",
			// Toast.LENGTH_SHORT).show();
			ch_finish = true;
			dissLoading();
		}

	}

	@Override
	public void getChargerInfoFailed() {
		// TODO 自动生成的方法存根
		ch_finish = true;
		dissLoading();
	}

	@Override
	public void getLocationInfoSuccess(LocationInfoData datas) {
		// TODO 自动生成的方法存根
		swipe_refresh_widget.setRefreshing(false);
		loc_finish = true;
		StringBuffer sb = new StringBuffer();
		String s = " ";
		String data_coordinates = SMSApplication.getInstance().getResources().getString(
				R.string.longitude)
				+ "%s"
				+ SMSApplication.getInstance().getResources().getString(R.string.latitude)
				+ "%s";
		;

		if (TextUtils.isEmpty(datas.getName())) {
			lin_chargername.setVisibility(View.GONE);
		} else {
			station_name.setText(datas.getName());
		}
		if (TextUtils.isEmpty(datas.getName())) {
			lin_chargername.setVisibility(View.GONE);
		}
		// 地址

		if (!TextUtils.isEmpty(datas.getAddress1())) {
			sb.append(datas.getAddress1());
		}
		if (!TextUtils.isEmpty(datas.getAddress2())) {
			sb.append(s);
			sb.append(datas.getAddress2());
		}
		if (!TextUtils.isEmpty(datas.getCity())) {
			sb.append(s);
			sb.append(datas.getCity());
		}
		if (!TextUtils.isEmpty(datas.getState())) {
			sb.append(s);
			sb.append(datas.getState());
		}
		if (!TextUtils.isEmpty(datas.getCountry())) {
			sb.append(s);
			sb.append(datas.getCountry());
		}
		if (!TextUtils.isEmpty(sb.toString())) {
			address.setText(sb.toString());
		} else {
			lin_adress.setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(datas.getPhone())) {
			lin_phone.setVisibility(View.GONE);
		} else {
			phone_number.setText(datas.getPhone());
		}
		if (TextUtils.isEmpty(datas.getNote())) {
			lin_note.setVisibility(View.GONE);
		} else {
			descripton.setText(datas.getNote());
		}
		if (TextUtils.isEmpty(datas.getLon())) {
			lin_coordinates.setVisibility(View.GONE);
		} else {
			if (TextUtils.isEmpty(datas.getLat())) {
				lin_coordinates.setVisibility(View.GONE);
			} else {
				coordinates.setText((String.format(data_coordinates,
						datas.getLon(), datas.getLat())));
			}
		}

		if (TextUtils.isEmpty(datas.getMail())) {
			lin_mail.setVisibility(View.GONE);
		} else {
			mail.setText(datas.getMail());
		}
		if (TextUtils.isEmpty(datas.getMobile())) {
			lin_mobile.setVisibility(View.GONE);
		} else {

			mobile.setText(datas.getMobile());
		}
		ViewGroup.LayoutParams lp = collapsingToolbarLayout.getLayoutParams();
		if (TextUtils.isEmpty(datas.getImage())) {
			lp.height = ivCamera.getMeasuredHeight()
					+ toolbar.getMeasuredHeight() + toolbar.getMeasuredHeight();
			collapsingToolbarLayout.setLayoutParams(lp);
		} else {
			lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
			Picasso.with(getActivity()).load(datas.getImage())
					.placeholder(R.drawable.zxc).error(R.drawable.zxc)
					.into(image);
		}
	}

	@Override
	public void getLocationInfoFailed() {
		// TODO 自动生成的方法存根
		loc_finish = true;
	}

	public int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	@Override
	public void showLoading() {
		// TODO 自动生成的方法存根
		if (mDialog == null) {
			mDialog = new ProgressDialog(getActivity());
			mDialog.setMessage(getActivity().getResources().getString(
					R.string.loading));
		}
		if (!mDialog.isShowing()) {
			mDialog.show();
		}

	}

	@Override
	public void dissLoading() {
		// TODO 自动生成的方法存根
		if (mDialog != null) {
			if (ch_finish && loc_finish) {
				mDialog.dismiss();
				swipe_refresh_widget.setRefreshing(false);
			}

		}
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.camera:
			startActivityForResult(new Intent(getActivity(),
					CameraActivity.class), REQUEST_PHOTO);
			break;
		case R.id.lin_adress:
			intentToChargerMapFragment();
			break;
		case R.id.lin_phone:
			toCall();
			break;
		case R.id.lin_mobile:
			toCall();
			break;
		case R.id.lin_coordinates:
			intentToChargerMapFragment();
			break;

		default:
			break;
		}
	}

	private void toCall() {
		if (ToolUtil.simIsExist()) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ phone_number.getText().toString().trim()));
			startActivity(intent);
		} else {
			Toast.makeText(getActivity(), R.string.no_sim, Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		switch (requestCode) {

		case REQUEST_PHOTO:

			switch (resultCode) {

			case NULL:
				Toast.makeText(getActivity(),
						"You can't add more than three pictures..",
						Toast.LENGTH_LONG).show();
				break;

			case PASS_CAMERA:

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA);
				break;

			case PASS_PHOTO:

				Intent intent2 = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent2, CROP);
				break;

			default:
				break;
			}
			break;

		case CAMERA:
			if (resultCode == Activity.RESULT_OK) {

				if (data != null) {
					handlePicture(data);
					showDialot(requestList, Comment.UPDATA_LOCATION_INFO);
				}
			}
			break;

		case CROP:
			if (resultCode == Activity.RESULT_OK) {

				Uri selectedImage = data.getData();
				String[] filePathColumns = { MediaStore.Images.Media.DATA };
				Cursor c = getActivity().getContentResolver().query(
						selectedImage, filePathColumns, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePathColumns[0]);
				String picturePath = c.getString(columnIndex);
				handlePicture(picturePath);
				showDialot(requestList, Comment.UPDATA_LOCATION_INFO);
			}

			break;
		case REQUEST_PHOTO_CH:

			switch (resultCode) {

			case NULL:
				Toast.makeText(getActivity(),
						"You can't add more than three pictures..",
						Toast.LENGTH_LONG).show();
				break;

			case PASS_CAMERA:

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA_CH);
				break;

			case PASS_PHOTO:

				Intent intent2 = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent2, CROP_CH);
				break;

			default:
				break;
			}
			break;

		case CAMERA_CH:
			if (resultCode == Activity.RESULT_OK) {

				if (data != null) {
					handlePicture(data);
					showDialot(ch_requestList, Comment.UPDATA_CHARGER_INFO);
				}
			}
			break;

		case CROP_CH:
			if (resultCode == Activity.RESULT_OK) {

				Uri selectedImage = data.getData();
				String[] filePathColumns = { MediaStore.Images.Media.DATA };
				Cursor c = getActivity().getContentResolver().query(
						selectedImage, filePathColumns, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePathColumns[0]);
				String picturePath = c.getString(columnIndex);
				handlePicture(picturePath);
				showDialot(ch_requestList, Comment.UPDATA_CHARGER_INFO);
			}

			break;

		case SET:
			handlePicture(data);
			break;

		default:
			break;
		}
	}

	private void handlePicture(String path) {
		if (path != null) {
			ticketPhoto = BitmapFactory.decodeFile(path);
			if (ticketPhoto != null) {
				addPicToView(ticketPhoto, false);
			}
		}
	}

	private void handlePicture(Intent data) {
		if (data != null) {
			Bundle extras = data.getExtras();
			ticketPhoto = extras.getParcelable("data");
			if (ticketPhoto != null) {
				addPicToView(ticketPhoto, false);
			}
		}
	}

	private void addPicToView(final Bitmap bitmap, boolean isInit) {
		this.bitmap = bitmap;

	}

	private void showDialot(final List<RequestItem> requestList,
			final String url) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.Prompt);
		builder.setMessage(R.string.Sure_to_upload_photo);
		// builder.setIcon(R.drawable.ic_launcher);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.Sure,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Bitmap bitmap = ticketPhoto;
						upLoadPresenter.UpLoadImg(bitmap, requestList, url);
					}

				});
		builder.setNegativeButton(R.string.Cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根

					}

				});
		builder.create().show();
	}

	@Override
	public void UpLoadSuccess() {
		// TODO 自动生成的方法存根
		chargerListDatas.clear();
		infoData.clear();
		initLocationInfo();
	}

	@Override
	public void UpLoadFailed() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void ChargerUpload(ChargerListData listData, String Id) {
		// TODO 自动生成的方法存根
		if (ch_requestList == null) {

			ch_requestList = new ArrayList<RequestItem>();
		}
		ch_requestList.add(new RequestItem("Id", Id));
		ch_requestList.add(new RequestItem("Name", listData.getName()));
		startActivityForResult(new Intent(getActivity(), CameraActivity.class),
				REQUEST_PHOTO_CH);
	}

	private void intentToChargerMapFragment() {
		if (GoogleUtil.isSupportGoogMap()) {

			GoogleUtil.navigationAddress(getActivity(), ToolUtil.getCurLocLat()
					+ "", ToolUtil.getCurLocLon() + "",
					chargerLocationdata.getLat() + "",
					chargerLocationdata.getLon() + "");
		} else {
			ToastCustom.showToast(getActivity(), getActivity().getResources()
					.getString(R.string.goolge_map_no_support),
					ToastCustom.LENGTH_SHORT);
		}
	}

	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		swipe_refresh_widget.setRefreshing(true);
		loadLocation();
	}

}
