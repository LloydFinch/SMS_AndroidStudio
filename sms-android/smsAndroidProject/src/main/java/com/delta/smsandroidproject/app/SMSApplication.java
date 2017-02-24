package com.delta.smsandroidproject.app;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.delta.smsandroidproject.util.CrashHandler;
import com.delta.smsandroidproject.util.LanguageUtil;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.ToolUtil;

//@ReportsCrashes(httpMethod = HttpSender.Method.PUT, reportType = HttpSender.Type.JSON, formUri = "http://10.120.129.34:5984/acra-sms/_design/acra-storage/_update/report", formUriBasicAuthLogin = "reporter", formUriBasicAuthPassword = "drc@1234", formKey = "")
public class SMSApplication extends Application {

    private static RequestQueue queue;
    private static SMSApplication smsApplication;

    private HashMap<String, WeakReference<Activity>> activityMap = new HashMap<String, WeakReference<Activity>>();

    private static Location myLocation;

    private static final String LOGIN_RECORD = "login_sp";
    private static final String LOGIN = "login_sp";
    private SharedPreferences loginSp;

    private static final String SESSION_COOKIE = "session_cookie";
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String COOKIE = "Cookie";
    // private static final String SESSION = "sessionid";
    private static final String SESSION = "JSESSIONID";
    private SharedPreferences cookiePreferences;
    private SharedPreferences.Editor prefEditor;

    public static SMSApplication getInstance() {
        return smsApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        smsApplication = this;
        loginSp = getSharedPreferences(LOGIN_RECORD, Context.MODE_PRIVATE);
        initCookieShare();
        setLanguage();
        queue = Volley.newRequestQueue(getApplicationContext());
        CrashHandler.getInstance().init(smsApplication);// 捕捉异常信息

        //ACRA.init(this);
    }

    public void recordLogin() {
        loginSp.edit().putBoolean(LOGIN, true).commit();
    }

    public void removeLogin() {
        loginSp.edit().clear();
    }

    public boolean isLogin() {
        return loginSp.getBoolean(LOGIN, false);
    }

    public static RequestQueue getRequestQueue() {
        return queue;
    }

    public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        queue.add(request);
    }

    public static void cancelAll(Object tag) {
        queue.cancelAll(tag);
    }

    public void addActivity(Activity activity) {
        if (activity != null) {
            if (activityMap == null) {
                activityMap = new HashMap<String, WeakReference<Activity>>();
            }
            activityMap.put(activity.getClass().getName(),
                    new WeakReference<Activity>(activity));
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            if (activityMap != null) {
                activityMap.remove(activityMap.get(activity.getClass()
                        .getName()));
            }
        }
    }

    public void clearAllActivity() {
        for (String key : activityMap.keySet()) {
            WeakReference<Activity> weakReference = activityMap.get(key);
            Activity activity = weakReference.get();
            if (weakReference != null && activity != null) {
                activity.finish();
            }
        }
    }

    public void exit() {
        clearAllActivity();
        clearSession();
        System.exit(0);
    }

    public static void setLanguage() {
        LanguageUtil.setLanguage(smsApplication.getApplicationContext(),
                ToolUtil.getLanguageId());
    }

    public static Location getMyLocation() {
        return myLocation;
    }

    public static void setMyLocation(Location location) {
        if (location != null) {
            myLocation = location;
        }
    }

    private void initCookieShare() {
        cookiePreferences = getSharedPreferences(SESSION_COOKIE,
                Context.MODE_PRIVATE);
        prefEditor = cookiePreferences.edit();
    }

    public final void saveSession(Map<String, String> headers) {
        if (headers == null) {
            return;
        }

        if (headers.containsKey(SET_COOKIE)) {
            String cookie = headers.get(SET_COOKIE);
            if (cookie.startsWith(SESSION)) {
                if (cookie.length() > 0) {
                    String[] splitCookie = cookie.split(";");
                    String[] splitSessionId = splitCookie[0].split("=");
                    cookie = splitSessionId[1];
                    Logg.i("SESSION", "sessionID:" + splitSessionId[0]);
                    Logg.i("SESSION", "sessionValue:" + cookie);
                    if (prefEditor != null) {
                        prefEditor.putString(SESSION, cookie).commit();
                    }
                }
            }
        }
        Logg.i("SESSION", "saveSession:" + headers.toString());
    }

    public final void addSession(Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        String cookie = cookiePreferences.getString(SESSION, "");
        if (cookie.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION);
            builder.append("=");
            builder.append(cookie);
            if (headers.containsKey(COOKIE)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE));
            }
            headers.put(COOKIE, builder.toString());
        }
        Logg.i("SESSION", "addSession:" + headers.toString());
    }

    public final void clearSession() {
        if (prefEditor != null) {
            prefEditor.clear().commit();
        }
        Logg.i("SESSION", "clearSession:" + cookiePreferences.getString(SESSION, ""));

    }
}
