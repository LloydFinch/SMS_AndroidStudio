package com.delta.smsandroidproject.view.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.adapter.SettingSpinnerAdapter;
import com.delta.smsandroidproject.bean.NetworkData;
import com.delta.smsandroidproject.dialog.NetAlertDialog;
import com.delta.smsandroidproject.presenter.NetworkListPresenter;
import com.delta.smsandroidproject.util.LanguageUtil;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.ToolUtil;
import com.delta.smsandroidproject.view.NetworkListView;
import com.delta.smsandroidproject.view.activity.MainActivity;

/**
 * settings
 *
 * @author Jianzao.Zhang
 */
public class SettingFragment extends BaseFragment implements NetworkListView, OnItemSelectedListener {
    public static final String TAG = "SettingFragment";
    private NetworkData data;
    private ProgressDialog mDialog;
    private NetworkListPresenter presenter;

    private SettingSpinnerAdapter mAdapterNW;
    private SettingSpinnerAdapter mAdapterL;
    private List<NetworkData> datas;
    private Spinner mSpinnerNW;
    private Spinner mSpinnerL;
    private Context mContext;
    private View inflate;
    private NetAlertDialog netAlertDialog;
    private int PageNo = 1;
    private int PerPage = 10;

    private boolean changeLocale = false;

    public SettingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
//		LanguageUtil.setLanguage(mContext, ToolUtil.getLanguageId());
        //SMSApplication.setLanguage();
        inflate = inflater.inflate(R.layout.fragment_setting, container,
                false);
        initView(inflate);
        listener();
        presenter = new NetworkListPresenter(this, getActivity());
        presenter.loadData(ToolUtil.getUid(), PageNo, PerPage);
        return inflate;
    }

    private void listener() {
        mSpinnerNW.setOnItemSelectedListener(this);
        mSpinnerL.setOnItemSelectedListener(this);
    }

    private void initView(View inflate) {
        mSpinnerNW = (Spinner) inflate
                .findViewById(R.id.spinnerNetWork);
        mSpinnerL = (Spinner) inflate
                .findViewById(R.id.spinnerLanguage);
        mAdapterL = new SettingSpinnerAdapter(getActivity());
        mSpinnerL.setAdapter(mAdapterL);
        mAdapterNW = new SettingSpinnerAdapter(getActivity());
        mSpinnerNW.setAdapter(mAdapterNW);
        notifyGetLanguage();
    }

    @Override
    public void onResume() {
        super.onResume();
        changeLocale = true;
        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setActionBarTitle(getActivity()
                .getResources().getString(R.string.title_settings));
    }

    private void notifyGetNetworkName(List<NetworkData> datas) {
        List<String> stringNWs = new ArrayList<String>();
        ;
        if (datas != null && datas.size() > 0) {
//			stringNWs.add("");
            for (NetworkData data : datas) {
                stringNWs.add(data.getName());
            }
            mAdapterNW.setList(stringNWs);
            mSpinnerNW.setSelection(ToolUtil.findDefaultNetworkNameIndex(stringNWs), true);
        }
    }

    private List<String> notifyGetLanguage() {
        List<String> stringLs = new ArrayList<String>();
        String[] strings = mContext.getResources().getStringArray(R.array.language);
        for (String string : strings) {
            stringLs.add(string);
        }
        mAdapterL.setList(stringLs);
        mSpinnerL.setSelection(ToolUtil.findLanguageIndex(stringLs), true);
        return stringLs;
    }

    @Override
    public boolean onBackPress() {
        return false;
    }

    @Override
    public void showDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage(getActivity().getResources().getString(
                    R.string.loading));
        }
        mDialog.show();

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
    public void setNetworkDatas(List<NetworkData> datas) {
        this.datas = datas;
        notifyGetNetworkName(datas);
    }

    @Override
    public List<NetworkData> getNetworkDatas() {
        return datas;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch (parent.getId()) {
            case R.id.spinnerNetWork:
                String networkId = datas.get(position).getId();
                String name = datas.get(position).getName();
//			ToolUtil.setIsSelectNetwork(networkId, name);
                ToolUtil.saveDefaultNetwork(networkId, name);
                break;
            case R.id.spinnerLanguage:
                Log.i("setLocale", "bool:" + changeLocale);
                if (changeLocale) {
                    setLanguage(position);
                }
                changeLocale = true;
                break;
            default:
                break;
        }
    }

    private void setLanguage(int position) {
        String s = mSpinnerL.getSelectedItem().toString();
        Logg.i(TAG, "language-" + s);
        LanguageUtil.setLanguage(mContext, position);
        ToolUtil.saveLanguage(s, position);
//			getFragmentManager().popBackStack();
        getActivity().startActivity(new Intent(getContext(), MainActivity.class));//刷新
        getActivity().finish();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Logg.i(TAG, "onNothingSelected-");
    }

    @Override
    public void showNetAlertDialog() {
        if (netAlertDialog == null) {
            netAlertDialog = new NetAlertDialog(mContext, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (presenter != null) {
                        presenter.loadData(ToolUtil.getUid(), PageNo, PerPage);
                    }
                }
            });
        }
        netAlertDialog.show();
    }
}
