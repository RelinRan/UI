package androidx.ui.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import androidx.ui.content.ShareData;
import androidx.ui.R;

/**
 * 应用事务实现类<br/>
 * 实现AppTransaction接口，处理Activity和Fragment公共逻辑，例如Fragment维护、登录状态、网路令牌。<br/>
 */
public class AppTransactionImpl implements AppTransaction {

    /**
     * 登录KEY
     */
    public static final String LOGIN = "ui_LOGIN";
    /**
     * 令牌KEY
     */
    public static final String TOKEN = "ui_TOKEN";
    /**
     * 用户信息KEY
     */
    public static final String USER_INFO = "ui_USER_INFO";

    /**
     * 活动页面
     */
    private AppActivity appActivity;
    /**
     * 碎片页面
     */
    private AppFragment appFragment;
    /**
     * 碎片管理器
     */
    private AppFragmentManager fragmentManager;

    /**
     * 应用事务实现
     *
     * @param appActivity 活动页面
     */
    public AppTransactionImpl(AppActivity appActivity) {
        fragmentManager = new AppFragmentManager(appActivity);
        this.appActivity = appActivity;
    }

    /**
     * 应用事务实现
     *
     * @param appFragment 碎片
     */
    public AppTransactionImpl(AppFragment appFragment) {
        fragmentManager = new AppFragmentManager(appFragment);
        this.appFragment = appFragment;
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public Context getContext() {
        if (appActivity != null) {
            return appActivity;
        } else {
            return appFragment.getContext();
        }
    }

    /**
     * 获取应用活动页面
     *
     * @return
     */
    public AppActivity getAppActivity() {
        return appActivity;
    }

    /**
     * 获取应用碎片页面
     *
     * @return
     */
    public AppFragment getAppFragment() {
        return appFragment;
    }

    @Override
    public void addFragment(Fragment fragment) {
        addFragment(fragment, null);
    }

    @Override
    public void addFragment(Fragment fragment, Bundle options) {
        if (options != null) {
            fragment.setArguments(options);
        }
        fragmentManager.add(fragment);
    }

    @Override
    public void addFragment(Class<? extends Fragment> clazz) {
        addFragment(clazz, null);
    }

    @Override
    public void addFragment(Class<? extends Fragment> clazz, Bundle options) {
        fragmentManager.add(clazz, options);
    }

    @Override
    public Fragment getCurrentFragment() {
        return fragmentManager.getCurrentFragment();
    }

    @Override
    public boolean isLogin() {
        return ShareData.getBoolean(getContext(), LOGIN, false);
    }

    @Override
    public void setLogin(boolean login) {
        ShareData.put(getContext(), LOGIN, login);
    }

    @Override
    public void setToken(String token) {
        ShareData.put(getContext(), TOKEN, token);
    }

    @Override
    public String getToken() {
        return ShareData.getString(getContext(), TOKEN, "");
    }

    @Override
    public void setUserInfo(String json) {
        ShareData.put(getContext(), USER_INFO, json);
    }

    @Override
    public String getUserInfo() {
        return ShareData.getString(getContext(), USER_INFO, "");
    }

    @Override
    public void overrideAppPendingTransition(int pending) {
        Activity activity = getAppActivity();
        if (activity == null && getAppFragment() != null) {
            activity = getAppFragment().getActivity();
        }
        if (activity == null) {
            return;
        }
        if (pending == PENDING_START) {
            activity.overridePendingTransition(R.anim.ui_pending_right_enter, R.anim.ui_pending_left_exit);
        }
        if (pending == PENDING_FINISH) {
            activity.overridePendingTransition(R.anim.ui_pending_left_enter, R.anim.ui_pending_right_exit);
        }
    }

    @Override
    public void startActivity(Class<?> activityClass) {
        startActivity(activityClass, null);
    }

    @Override
    public void startActivity(Class<?> cls, Bundle options) {
        Intent intent = new Intent(getContext(), cls);
        if (options != null) {
            intent.putExtras(options);
        }
        if (getAppActivity() != null) {
            getAppActivity().startActivity(intent);
        }
        if (getAppFragment() != null) {
            getAppFragment().startActivity(intent);
        }
    }

    @Override
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, requestCode, null);
    }

    @Override
    public void startActivityForResult(Class<?> cls, int requestCode, Bundle options) {
        Intent intent = new Intent(getContext(), cls);
        if (options != null) {
            intent.putExtras(options);
        }
        if (getAppActivity() != null) {
            getAppActivity().startActivityForResult(intent, requestCode);
        }
        if (getAppFragment() != null) {
            getAppFragment().startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Log.i(AppTransactionImpl.class.getSimpleName(),"requestCode = "+requestCode+",resultCode = "+resultCode);
        AppPackage.onActivityResult(activity, requestCode, requestCode, data);
    }

}
