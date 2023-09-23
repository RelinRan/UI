package androidx.ui.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

/**
 * 应用程序事务处理接口<br/>
 * 主要处理Activity和Fragment公共逻辑，例如Fragment维护、登录状态、网路令牌。<br/>
 */
public interface AppTransaction {

    /**
     * 准备 - 启动页面
     */
    int PENDING_START = 201;
    /**
     * 准备 - 结束页面
     */
    int PENDING_FINISH = 202;

    /**
     * 添加Fragment
     *
     * @param fragment 碎片View
     */
    void addFragment(Fragment fragment);

    /**
     * 添加Fragment
     *
     * @param fragment 碎片View
     * @param options  参数
     */
    void addFragment(Fragment fragment, Bundle options);

    /**
     * 添加Fragment
     *
     * @param cls 碎片类
     */
    void addFragment(Class<? extends Fragment> cls);

    /**
     * 添加Fragment
     *
     * @param cls     碎片类
     * @param options 参数
     */
    void addFragment(Class<? extends Fragment> cls, Bundle options);

    /**
     * @return 当前操作的Fragment
     */
    Fragment getCurrentFragment();

    /**
     * 是否登录
     *
     * @return
     */
    boolean isLogin();

    /**
     * 设置登录
     *
     * @param login
     */
    void setLogin(boolean login);

    /**
     * 设置令牌
     *
     * @param token
     */
    void setToken(String token);

    /**
     * 获取令牌
     *
     * @return
     */
    String getToken();

    /**
     * 设置用户信息
     *
     * @param json json数据
     */
    void setUserInfo(String json);

    /**
     * 获取用户信息
     *
     * @return
     */
    String getUserInfo();

    /**
     * 重写准备动画
     *
     * @param pending 准备类型{@link #PENDING_START} OR {@link #PENDING_FINISH}
     */
    void overrideAppPendingTransition(int pending);

    /**
     * 跳转活动页面
     *
     * @param cls 活动页面类
     */
    void startActivity(Class<?> cls);

    /**
     * 跳转活动页面
     *
     * @param cls     活动页面类
     * @param options 参数
     */
    void startActivity(Class<?> cls, Bundle options);

    /**
     * 跳转活动页面
     *
     * @param cls         活动页面类
     * @param requestCode 请求代码
     */
    void startActivityForResult(Class<?> cls, int requestCode);

    /**
     * 跳转活动页面
     *
     * @param cls         活动页面类
     * @param requestCode 请求代码
     * @param options     参数
     */
    void startActivityForResult(Class<?> cls, int requestCode, Bundle options);

    /**
     * 处理页面结果
     *
     * @param activity    页面
     * @param requestCode 请求代码
     * @param resultCode  结果代码
     * @param data        数据
     */
    void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);

}
