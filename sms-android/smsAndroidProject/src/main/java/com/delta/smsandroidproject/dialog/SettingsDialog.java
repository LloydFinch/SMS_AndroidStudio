package com.delta.smsandroidproject.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.view.BindViewTool;
import com.delta.smsandroidproject.view.ViewInitHelper;
import com.delta.smsandroidproject.view.activity.LoginActivity;

import java.security.PrivilegedAction;

public class SettingsDialog extends Dialog implements
        View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @BindViewTool(id = R.id.radioGroup_ip)
    private RadioGroup mRadioGroup;
    @BindViewTool(id = R.id.button_ok, clickable = true)
    private TextView tvOK;

    @BindViewTool(id = R.id.radio131, clickable = true)
    private RadioButton mRadio131;
    @BindViewTool(id = R.id.radio163, clickable = true)
    private RadioButton mRadio163;
    @BindViewTool(id = R.id.radio140, clickable = true)
    private RadioButton mRadio140;
    @BindViewTool(id = R.id.radio146, clickable = true)
    private RadioButton mRadio146;
    @BindViewTool(id = R.id.radio155, clickable = true)
    private RadioButton mRadio155;

    private Context mContext;
    private View contentView;
    private String url;

    public SettingsDialog(Context context, int style) {
        super(context, style);
        this.mContext = context;
        init();
    }

    @SuppressLint("InflateParams")
    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        contentView = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_settings, null);
        setContentView(contentView);

        initWindow();
        initView(contentView);
    }

    private void initWindow() {
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 260, mContext.getResources()
                        .getDisplayMetrics());
        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                300, mContext.getResources().getDisplayMetrics());
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    private void initView(View view) {
        ViewInitHelper.initView(this, view);
        mRadioGroup.setOnCheckedChangeListener(this);
        initIP();
    }

    private void initIP() {
        url = LoginActivity.spIP.getString(LoginActivity.SMS_IP, null);
        if (TextUtils.isEmpty(url)) {
            url = mRadio131.getText().toString().trim();
        }

        if (url.equals(mRadio131.getText().toString().trim())) {
            mRadioGroup.check(R.id.radio131);
        } else if (url.equals(mRadio163.getText().toString().trim())) {
            mRadioGroup.check(R.id.radio163);
        } else if (url.equals(mRadio140.getText().toString().trim())) {
            mRadioGroup.check(R.id.radio140);
        } else if (url.equals(mRadio146.getText().toString().trim())) {
            mRadioGroup.check(R.id.radio146);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
                setIP();
                this.dismiss();
                break;
            default:
                break;
        }
    }

    private void setIP() {
        LoginActivity.editorIP.putString(LoginActivity.SMS_IP, url).commit();
        Comment.setBASE_URL(url);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio131:
                url = mRadio131.getText().toString().trim();
                break;
            case R.id.radio163:
                url = mRadio163.getText().toString().trim();
                break;
            case R.id.radio140:
                url = mRadio140.getText().toString().trim();
                break;
            case R.id.radio146:
                url = mRadio146.getText().toString().trim();
                break;
            case R.id.radio155:
                url = mRadio155.getText().toString().trim();
                break;
        }
    }
}
