package com.delta.smsandroidproject.view.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.adapter.NetSettingSpinnerAdapter;
import com.delta.smsandroidproject.bean.NetworkData;
import com.delta.smsandroidproject.bean.NetwrkInfoData;
import com.delta.smsandroidproject.dialog.NetAlertDialog;
import com.delta.smsandroidproject.presenter.NetworkListPresenter;
import com.delta.smsandroidproject.presenter.NetworkSettingPresenter;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.SetTextUtils;
import com.delta.smsandroidproject.util.ToolUtil;
import com.delta.smsandroidproject.view.NetworkListView;
import com.delta.smsandroidproject.view.NetworkSettingView;
import com.delta.smsandroidproject.view.activity.MainActivity;

/**
 * select a network
 *
 * @author Jianzao.Zhang
 *
 */
public class SelectNetWorkFragment extends BaseFragment implements
		NetworkSettingView, NetworkListView, OnItemSelectedListener {

	private static final int MAX_IMAGE_WIDTH = 200;
	private static final int MAX_IMAGE_HEIGHT = 180;

	public static final String TAG = "SelectNetWorkFragment";
	private List<NetworkData> datas = new ArrayList<NetworkData>();
	private ProgressDialog mDialog;
	private NetworkSettingPresenter presenter;
	private NetwrkInfoData data;
	private Spinner mSpinner;
	private TextView mCompany;
	private TextView mContact;
	private TextView mAddress;
	private TextView mAddress2;
	private TextView mCountry;
	private TextView mState;
	private TextView mMobile;
	private TextView mMail;
	private TextView mPhone;
	private ImageView netIcon;
	private LinearLayout mCompanyLayout;
	private LinearLayout mContactLayout;
	private LinearLayout mAddressLayout;
	private LinearLayout mAddressLayout2;
	private LinearLayout mAddressLayout3;
	private LinearLayout mStateLayout;
	private LinearLayout mPhoneLayout;
	private LinearLayout mMobileLayout;
	private LinearLayout mMailLayout;
	private NetSettingSpinnerAdapter mAdapter;
	private NetworkListPresenter networkListPresenter;
	private Context mContext;
	private NetAlertDialog netAlertDialog;
	private int PageNo = 1;
	private int PerPage = 10;

	public SelectNetWorkFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_select_net_work,
				container, false);
		mContext = getActivity();
		networkListPresenter = new NetworkListPresenter(this,getActivity());
		networkListPresenter.loadData(ToolUtil.getUid(), PageNo, PerPage);
		presenter = new NetworkSettingPresenter(this,getActivity());
		initView(inflate);
		listener();
		return inflate;
	}

	private void initView(View inflate) {
		mCompanyLayout = (LinearLayout) inflate
				.findViewById(R.id.companyLayout);
		mContactLayout = (LinearLayout) inflate
				.findViewById(R.id.contactLayout);
		mAddressLayout = (LinearLayout) inflate
				.findViewById(R.id.addressLayout);
		mAddressLayout2 = (LinearLayout) inflate
				.findViewById(R.id.addressLayout2);
		mAddressLayout3 = (LinearLayout) inflate
				.findViewById(R.id.addressLayout3);
		mPhoneLayout = (LinearLayout) inflate.findViewById(R.id.phoneLayout);
		mStateLayout = (LinearLayout) inflate.findViewById(R.id.stateLayout);
		mPhoneLayout = (LinearLayout) inflate.findViewById(R.id.phoneLayout);
		mMobileLayout = (LinearLayout) inflate.findViewById(R.id.mobileLayout);
		mMailLayout = (LinearLayout) inflate.findViewById(R.id.mailLayout);

		netIcon = (ImageView) inflate.findViewById(R.id.iv_net_icon);
		mCompany = (TextView) inflate.findViewById(R.id.company);
		mContact = (TextView) inflate.findViewById(R.id.contact);
		mAddress = (TextView) inflate.findViewById(R.id.address);
		mAddress2 = (TextView) inflate.findViewById(R.id.address2);
		mCountry = (TextView) inflate.findViewById(R.id.country);
		mState = (TextView) inflate.findViewById(R.id.state);
		mMobile = (TextView) inflate.findViewById(R.id.mobile);
		mMail = (TextView) inflate.findViewById(R.id.mail);
		mPhone = (TextView) inflate.findViewById(R.id.phone);
		mSpinner = (Spinner) inflate.findViewById(R.id.spinnerNetWork);
		mAdapter = new NetSettingSpinnerAdapter(getActivity());
		mSpinner.setAdapter(mAdapter);
	}

	private void listener() {
		mSpinner.setOnItemSelectedListener(this);
	}

	private void notifyGetNetworkName(List<NetworkData> datas) {
		List<String> strings = new ArrayList<String>();
		if (datas != null && datas.size() > 0) {
			// strings.add("");
			for (NetworkData data : datas) {
				strings.add(data.getName());
			}
			mAdapter.setList(strings);
			Logg.i("strings.size()", "" + strings.size());
			mSpinner.setSelection(ToolUtil.findNetworkNameIndex(strings), true);
			presenter.loadData(ToolUtil.getNetworkId());
		}

	}

	private void setInfomatio() {
		if (data != null) {

			// set information
			SetTextUtils.setText(mCompany, data.getCompany(), mCompanyLayout);
			SetTextUtils.setText(mContact, data.getContact(), mContactLayout);
			SetTextUtils.setText(mAddress, data.getAddress1(), mAddressLayout);
			String address2 = data.getAddress2();
			String city = data.getCity();
			if (!TextUtils.isEmpty(address2)) {
				SetTextUtils.setText(mAddress2, address2 + " " + city,
						mAddressLayout2);
			} else {
				SetTextUtils.setText(mAddress2, city, mAddressLayout2);
			}
			SetTextUtils.setText(mState, data.getState(), mStateLayout);
			SetTextUtils.setText(mCountry, data.getCountry(), mAddressLayout3);
			SetTextUtils.setText(mMobile, data.getMobile(), mMobileLayout);
			SetTextUtils.setText(mMail, data.getMail(), mMailLayout);
			SetTextUtils.setText(mPhone, data.getPhone(), mPhoneLayout);

			// load image
			final String imagePath = data.getImage();
			if (imagePath != null) {
				RequestQueue mQueue = Volley.newRequestQueue(mContext);
				ImageLoader imageLoader = new ImageLoader(mQueue,
						new BitmapCache());
				ImageListener listener = ImageLoader.getImageListener(netIcon,
						R.drawable.ic_empty, R.drawable.ic_empty);
				String path = Comment.BASE_URL.replace("/EMEA/", "")
						+ imagePath.replace(" ", "%20");
				Logg.i("image", "path--" + path);
				Logg.i("data", "data--" + data.toString());
				try {
					imageLoader.get(path, listener, MAX_IMAGE_WIDTH,
							MAX_IMAGE_HEIGHT);
				} catch (Exception e) {
					if (isAdded()) {
						netIcon.setImageBitmap(BitmapFactory.decodeResource(
								getResources(), R.drawable.logo));
					}
				}
			} else {
				if (isAdded()) {
					netIcon.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.ic_empty));
				}
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
		((MainActivity) getActivity()).setActionBarTitle(getActivity()
				.getResources().getString(R.string.dashboard_network_setting));
	}

	@Override
	public void showDialog() {
		if (mDialog == null) {
			mDialog = new ProgressDialog(getActivity());
			mDialog.setMessage(getActivity().getResources().getString(
					R.string.loading));
		}
		if (!mDialog.isShowing()) {
			mDialog.show();
		} else {
			mDialog.dismiss();
		}
	}

	@Override
	public void dimiss() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		if (netAlertDialog != null) {
			netAlertDialog.dismiss();
		}
	}

	@Override
	public boolean onBackPress() {
		return false;
	}

	@Override
	public void setNetwrkInfoData(NetwrkInfoData data) {
		this.data = data;
		setInfomatio();
	}

	@Override
	public NetwrkInfoData getNetwrkInfoData() {
		return data;
	}

	@Override
	public void setNetworkDatas(List<NetworkData> datas) {
		this.datas = datas;
		if (datas.size() > 0) {
			Logg.i("datas.size()", "" + datas.size());
			notifyGetNetworkName(datas);
		}
	}

	@Override
	public List<NetworkData> getNetworkDatas() {
		return datas;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		int selectedPosition = position;
		if (selectedPosition >= 0) {
			Logg.i(TAG, "position-"+position);
			String networkId = datas.get(selectedPosition).getId();
			String name = datas.get(selectedPosition).getName();
			presenter.loadData(networkId);
			ToolUtil.setIsSelectNetwork(networkId, name);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		presenter.loadData(ToolUtil.getNetworkId());
	}

	@Override
	public void showNetAlertDialog() {
		if (netAlertDialog == null) {
			netAlertDialog = new NetAlertDialog(mContext,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (presenter != null) {
								networkListPresenter.loadData(
										ToolUtil.getUid(), PageNo, PerPage);
								presenter.loadData(ToolUtil.getNetworkId());
							}
						}
					});
		}
		netAlertDialog.show();
	}

	public class BitmapCache implements ImageCache {
		private LruCache<String, Bitmap> cache;

		public BitmapCache() {
			cache = new LruCache<String, Bitmap>(8 * 1024 * 1024) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return bitmap.getRowBytes() * bitmap.getHeight();
				}
			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			return cache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			cache.put(url, bitmap);
		}
	}
}
