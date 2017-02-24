package com.delta.smsandroidproject.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.PublicKeyData;
import com.delta.smsandroidproject.dialog.NetAlertDialog;
import com.delta.smsandroidproject.dialog.SettingsDialog;
import com.delta.smsandroidproject.presenter.LoginPresenter;
import com.delta.smsandroidproject.presenter.PublicKeyPresenter;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.RSAUtil;
import com.delta.smsandroidproject.util.ToastCustom;
import com.delta.smsandroidproject.util.ToolUtil;
import com.delta.smsandroidproject.view.LoginView;
import com.delta.smsandroidproject.view.PublicKeyView;

import java.security.interfaces.RSAPublicKey;

/**
 * login ui
 *
 * @author Jianzao.Zhang
 */
public class LoginActivity extends Activity implements OnClickListener,
        LoginView, OnCheckedChangeListener, PublicKeyView, TextWatcher {

    private static final String TAG = "LoginActivity";
    public static final String SMS_IP_SP = "sms_ip_sp";
    public static final String SMS_IP = "sms_ip";
    public static SharedPreferences spIP;
    public static SharedPreferences.Editor editorIP;
    private LoginActivity mContext;
    private EditText mAccount;
    private EditText mPassword;
    private CheckBox mRemember;
    private Button mLogin;
    private ProgressDialog dialog;
    private LoginPresenter loginPresenter;
    private NetAlertDialog netAlertDialog;
    private Toolbar mToolbar;
    private RadioButton mRadio1;
    private RadioButton mRadio2;
    private RadioButton mRadio3;
    private RadioButton mRadio4;
    private RadioButton mRadio5;
    private RSAPublicKey PUBLIC_KEY; // 公钥
    private PublicKeyPresenter publicKeyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SMSApplication.getInstance().addActivity(this);
        if (!ToolUtil.getRememberMe()) {
            ToolUtil.clearLogin();
        }

        mContext = this;
        loginPresenter = new LoginPresenter(this, this);
        publicKeyPresenter = new PublicKeyPresenter(this, this);
        init();
        listener();
    }

    /**
     * init
     */
    private void init() {
        mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.ip_menu);
        View ipView = mToolbar.findViewById(R.id.ip_setting);
        ipView.setOnClickListener(this);

        mRadio1 = (RadioButton) findViewById(R.id.radio1);
        mRadio2 = (RadioButton) findViewById(R.id.radio2);
        mRadio3 = (RadioButton) findViewById(R.id.radio3);
        mRadio4 = (RadioButton) findViewById(R.id.radio4);
        mRadio5 = (RadioButton) findViewById(R.id.radio5);
        mLogin = (Button) findViewById(R.id.login);
        mAccount = (EditText) findViewById(R.id.account);
        mAccount.addTextChangedListener(this);
        mPassword = (EditText) findViewById(R.id.password);
        mRemember = (CheckBox) findViewById(R.id.remember);
        mRemember.setChecked(ToolUtil.getRememberMe());
        mAccount.setText(ToolUtil.getUid());
        mPassword.setText(ToolUtil.getPw());
        // 解决密码框跟普通框字体不一样的问题
        mPassword.setTypeface(Typeface.DEFAULT);
        mPassword.setTransformationMethod(new PasswordTransformationMethod());
        initIP();
    }

    private void initIP() {
        spIP = this.getPreferences(MODE_PRIVATE);
        String ip = spIP.getString(SMS_IP, null);
        editorIP = spIP.edit();
        if (TextUtils.isEmpty(ip)) {
            ip = mRadio1.getText().toString().trim();
            editorIP.putString(SMS_IP, ip);
            editorIP.commit();
        }
        Comment.setBASE_URL(ip);

        Logg.i("LoginActivity-Comment.LOGIN_URL", "" + Comment.LOGIN_URL);
    }

    /**
     * listener
     */
    private void listener() {
        mLogin.setOnClickListener(this);
        mRemember.setOnCheckedChangeListener(this);
        mRadio1.setOnClickListener(this);
        mRadio2.setOnClickListener(this);
        mRadio3.setOnClickListener(this);
        mRadio4.setOnClickListener(this);
        mRadio5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ip_setting:
                SettingsDialog dialog = new SettingsDialog(this, R.style.radius_dialog);
                dialog.show();
                break;
            case R.id.login:
                publicKeyPresenter.loadData(Comment.PUBLIC_KEY);

                // String path = Comment.BASE_URL.replace("http", "ws") +
                // "WebSocket";
                // Logg.i("MyWebsocket-path:", path);
                // WebSocketRegisterTool.registerWebSocket(getApplicationContext(),
                // path);
                break;
            case R.id.radio1:
                Comment.setBASE_URL(mRadio1.getText().toString().trim());
                Logg.i("LoginActivity-Comment.LOGIN_URL", "" + Comment.LOGIN_URL);
                break;
            case R.id.radio2:
                Comment.setBASE_URL(mRadio2.getText().toString().trim());
                Logg.i("LoginActivity-Comment.LOGIN_URL", "" + Comment.LOGIN_URL);
                break;
            case R.id.radio3:
                Comment.setBASE_URL(mRadio3.getText().toString().trim());
                Logg.i("LoginActivity-Comment.LOGIN_URL", "" + Comment.LOGIN_URL);
                break;
            case R.id.radio4:
                Comment.setBASE_URL(mRadio4.getText().toString().trim());
                Logg.i("LoginActivity-Comment.LOGIN_URL", "" + Comment.LOGIN_URL);
                break;
            case R.id.radio5:
                Comment.setBASE_URL(mRadio5.getText().toString().trim());
                Logg.i(TAG, "-Comment.LOGIN_URL" + Comment.LOGIN_URL);
                break;
            default:
                break;
        }
    }

    /**
     * do login
     *
     * @throws Exception
     */
    private void doLogin() {
        String account = mAccount.getText().toString().trim();
        String pw = mPassword.getText().toString().trim();
        if (account.equals("")) {
            ToastCustom.showToast(mContext,
                    mContext.getResources().getString(R.string.uid_null),
                    ToastCustom.LENGTH_SHORT);
            return;
        }
        if (pw.equals("")) {
            ToastCustom.showToast(mContext,
                    mContext.getResources().getString(R.string.pw_null),
                    ToastCustom.LENGTH_SHORT);
            return;
        }
        // SHAEncrypt shaEncrypt = new SHAEncrypt();
        // String encryptPwd = shaEncrypt.encryptPwd(pw);
        // // loginPresenter.login(account, encryptPwd);

        try {
            String encryptPwd = RSAUtil.encryptByPublicKey(pw, PUBLIC_KEY);
            Logg.i(TAG, "encryptPwd-" + encryptPwd);
            loginPresenter.login(account, encryptPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(getResources().getString(R.string.loading));
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    @Override
    public void intentToMainActivity() {
        ToolUtil.saveLogin(mAccount.getText().toString().trim(), mPassword
                .getText().toString().trim());
        startActivity(new Intent(mContext, SelectNetworkActivity.class));
        finish();
    }

    @Override
    public void failed(String s) {
        ToastCustom.showToast(mContext, s, ToastCustom.LENGTH_SHORT);
        if (PUBLIC_KEY != null) {
            PUBLIC_KEY = null;
        }
    }

    @Override
    public void successed(String s) {
        ToastCustom.showToast(mContext, s, ToastCustom.LENGTH_SHORT);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ToolUtil.setRememberMe(isChecked);
    }

    @Override
    public void showNetAlertDialog() {
        if (netAlertDialog == null) {
            netAlertDialog = new NetAlertDialog(mContext,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            publicKeyPresenter.loadData(Comment.PUBLIC_KEY);
                        }
                    });
        }
        netAlertDialog.show();
    }

    @Override
    public void setPublicKey(PublicKeyData keyData) {
        if (keyData != null) {
            try {
                PUBLIC_KEY = RSAUtil.verify(keyData.getPublicKey());
                Logg.i(TAG, "PUBLIC_KEY-" + PUBLIC_KEY);
                if (PUBLIC_KEY != null) {
                    doLogin();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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

    /*
     * （非 Javadoc）
     *
     * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence,
     * int, int, int)
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    /*
     * （非 Javadoc）
     *
     * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int,
     * int, int)
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (this.mAccount.getText().toString().trim().equals("")) {
            this.mPassword.setText("");
        }
    }

    /*
     * （非 Javadoc）
     *
     * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
     */
    @Override
    public void afterTextChanged(Editable s) {

    }
}
