package com.delta.smsandroidproject.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.adapter.SettingSpinnerAdapter;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.NetworkData;
import com.delta.smsandroidproject.bean.UseData.Function;
import com.delta.smsandroidproject.dialog.NetAlertDialog;
import com.delta.smsandroidproject.presenter.NetworkListPresenter;
import com.delta.smsandroidproject.presenter.UserPresenter;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.ToolUtil;
import com.delta.smsandroidproject.view.NetworkListView;
import com.delta.smsandroidproject.view.UseView;

/**
 * select a network
 *
 * @author Jianzao.Zhang
 */
public class SelectNetworkActivity extends Activity implements NetworkListView,
        OnClickListener, OnItemSelectedListener, UseView {

    private static final String TAG = "SelectNetworkActivity";
    private SelectNetworkActivity mContext;
    private NetworkListPresenter presenter;
    private List<NetworkData> datas = new ArrayList<NetworkData>();
    private ProgressDialog mDialog;
    private SettingSpinnerAdapter mAdpater;
    private Button mBtnNext;
    private Spinner mSpinner;
    private NetAlertDialog netAlertDialog;
    private TextView mTxProm;
    private int PageNo = 1;
    private int PerPage = 255;
    private UserPresenter userPresenter;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_network);
        mContext = this;
        SMSApplication.getInstance().addActivity(this);
        initView();
        presenter = new NetworkListPresenter(this, this);
        presenter.loadData(ToolUtil.getUid(), PageNo, PerPage);
        userPresenter = new UserPresenter(this, this);
        listener();
    }

    private void initView() {
        mTxProm = (TextView) findViewById(R.id.txProm);
        mBtnNext = (Button) findViewById(R.id.btnNext);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mAdpater = new SettingSpinnerAdapter(mContext);
        mSpinner.setAdapter(mAdpater);

    }

    private void listener() {
        mBtnNext.setOnClickListener(this);
        mSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void setNetworkDatas(List<NetworkData> datas) {
        this.datas = datas;
        notifyGetNetworkName(datas);
    }

    private void notifyGetNetworkName(List<NetworkData> datas) {
        List<String> strings = new ArrayList<String>();
        if (datas != null) {
            if (datas.size() == 0) {
                mSpinner.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            Toast.makeText(mContext, R.string.no_available_network,
                                    Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                return;
            }
            if (datas.size() == 1) {// 当networkid只有一个，就直接设定此networkid，否则第一次安装apk就为空
                strings.add("");
                for (NetworkData data : datas) {
                    strings.add(data.getName());
                }
                mAdpater.setList(strings);
                mSpinner.setSelection(1, true);
                return;
            }
            if (datas.size() > 0 && strings.size() == 0) {
                strings.add("");
                for (NetworkData data : datas) {
                    strings.add(data.getName());
                }
                mAdpater.setList(strings);
                mSpinner.setSelection(ToolUtil.findDefaultNetworkNameIndex(strings),
                        true);
                if (findNetWorkName(strings)) {
                    // mTxProm.setVisibility(View.GONE);
                    mBtnNext.setEnabled(true);
                } else {
                    mTxProm.setVisibility(View.VISIBLE);
                    mBtnNext.setEnabled(false);
                }
            }
        }
    }

    /**
     * 查找存储的network是否与当前的networklist里
     *
     * @param strings
     * @return
     */
    private boolean findNetWorkName(List<String> strings) {
        if (strings != null) {
            for (int i = 0; i < strings.size(); i++) {
                if (ToolUtil.getNetworkName().equals(strings.get(i))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public List<NetworkData> getNetworkDatas() {
        return datas;
    }

    @Override
    public void showDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage(mContext.getResources().getString(
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
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                // userPresenter.loadData("http://172.22.35.161:7001/EMEA/user/info/publickey").;
                String string = mSpinner.getSelectedItem().toString().trim();
                if (datas != null && datas.size() > 0) {
                    int itemPosition = mSpinner.getSelectedItemPosition() - 1;
                    Logg.i(TAG, "itemPosition-" + itemPosition);
                    NetworkData networkData = datas.get(itemPosition);
                    if (networkData != null) {
                        String networkId = networkData.getId();
                        ToolUtil.saveNetworkId(networkId, string);
                        ToolUtil.saveDefaultNetwork(networkId, string);
                        ToolUtil.setIsSelectNetwork(networkId, string);
                        userPresenter.loadData(Comment.USER_ROLE + "UserId="
                                + ToolUtil.getUid() + "&NetworkId=" + networkId);
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        if (!mSpinner.getSelectedItem().toString().trim().isEmpty()) {
            mBtnNext.setEnabled(true);
            mTxProm.setVisibility(View.GONE);
        } else {
            mTxProm.setVisibility(View.VISIBLE);
            mBtnNext.setEnabled(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void showNetAlertDialog() {
        if (netAlertDialog == null) {
            netAlertDialog = new NetAlertDialog(mContext,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.loadData(ToolUtil.getUid(), PageNo,
                                    PerPage);
                        }
                    });
        }
        netAlertDialog.show();
    }

    @Override
    public void setUseRole(Function function) {
        if (function != null) {
            Log.i("Function", function.toString());
            String groupID = function.getGroupID();
            if (groupID != null) {
                ToolUtil.saveIsSevice(groupID);

                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                showAlertDialog();
            }
        }
    }

    /**
     * 没有service或者support用户角色
     */
    public void showAlertDialog() {
        if (mAlertDialog == null) {
            Builder builder = new Builder(mContext);
            builder.setMessage(mContext.getResources().getString(
                    R.string.not_authorized));
            builder.setCancelable(true);
            mAlertDialog = builder.create();
            mAlertDialog.setButton(Dialog.BUTTON_POSITIVE, mContext
                            .getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                        }
                    });
        }
        mAlertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                SMSApplication.getInstance().clearSession();
                break;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
