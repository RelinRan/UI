package androidx.ui.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * 应用程序 - Fragment
 */
public class AppFragment extends Fragment implements AppTransaction, AppLayout, AppView, AppPlaceholder, AppPermission.OnRequestPermissionsListener, AppLoading, View.OnClickListener {

    private View contentView;
    private AppTransaction transaction;
    private AppPlaceholder placeholder;
    private AppPermission permission;
    private AppLoading loading;

    /**
     * 获取内容View
     *
     * @return
     */
    public View getContentView() {
        return contentView;
    }

    /**
     * 设置加载器
     *
     * @param loading
     */
    public void setLoading(AppLoading loading) {
        this.loading = loading;
    }

    /**
     * 获取加载器对象
     *
     * @return
     */
    public AppLoading getLoading() {
        return loading;
    }


    /**
     * 设置权限对象
     *
     * @param permission
     */
    public void setPermission(AppPermission permission) {
        this.permission = permission;
    }

    /**
     * 获取权限对象
     *
     * @return
     */
    public AppPermission getPermission() {
        return permission;
    }

    /**
     * 设置事务处理实现类
     *
     * @param transaction 事务处理实现类
     */
    public void setAppTransactionImpl(AppTransaction transaction) {
        this.transaction = transaction;
    }

    /**
     * 设置占位实现类
     *
     * @param placeholder 占位实现类
     */
    public void setPlaceholderImpl(AppPlaceholder placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * 获取占位类
     *
     * @return
     */
    public <T extends AppPlaceholderImpl> T getPlaceholder() {
        return (T) placeholder;
    }

    /**
     * 找到View
     *
     * @param id view id
     * @return
     */
    public <T extends View> T findViewById(@IdRes int id) {
        return contentView.findViewById(id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewLayoutResId() != 0) {
            setPlaceholderImpl(new AppPlaceholderImpl(this));
        }
        return getPlaceholder() == null ? container : getPlaceholder().getParent();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentView = view;
        onInitViews();
        setPermission(new AppPermission(this, this));
        setAppTransactionImpl(new AppTransactionImpl(this));
        setLoading(new AppLoadingImpl(view.getContext()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overrideAppPendingTransition(PENDING_START);
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        super.startActivity(intent, options);
        overrideAppPendingTransition(PENDING_START);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overrideAppPendingTransition(PENDING_START);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overrideAppPendingTransition(PENDING_START);
    }

    @Override
    public Context getAppContext() {
        return getContext();
    }

    @Override
    public int getContentViewLayoutResId() {
        return 0;
    }

    @Override
    public int getContainerViewResId() {
        return 0;
    }

    @Override
    public FragmentManager getAppFragmentManager() {
        return getChildFragmentManager();
    }

    @Override
    public void setPlaceholder(int type, int resId, String text) {
        placeholder.setPlaceholder(type, resId, text);
    }

    @Override
    public void setPlaceholder(int type, int resId) {
        placeholder.setPlaceholder(type, resId);
    }

    @Override
    public void setPlaceholder(int type, String text) {
        placeholder.setPlaceholder(type, text);
    }

    @Override
    public void showPlaceholder(int type) {
        placeholder.showPlaceholder(type);
    }

    @Override
    public void hidePlaceholder() {
        placeholder.hidePlaceholder();
    }

    @Override
    public void addFragment(Fragment fragment) {
        transaction.addFragment(fragment);
    }

    @Override
    public void addFragment(Fragment fragment, Bundle options) {
        transaction.addFragment(fragment, options);
    }

    @Override
    public void addFragment(Class<? extends Fragment> cls) {
        transaction.addFragment(cls);
    }

    @Override
    public void addFragment(Class<? extends Fragment> cls, Bundle options) {
        transaction.addFragment(cls, options);
    }

    @Override
    public Fragment getCurrentFragment() {
        return transaction.getCurrentFragment();
    }

    @Override
    public boolean isLogin() {
        return transaction.isLogin();
    }

    @Override
    public void setLogin(boolean login) {
        transaction.setLogin(login);
    }

    @Override
    public void setToken(String token) {
        transaction.setToken(token);
    }

    @Override
    public String getToken() {
        return transaction.getToken();
    }

    @Override
    public void setUserInfo(String json) {
        transaction.setUserInfo(json);
    }

    @Override
    public String getUserInfo() {
        return transaction.getUserInfo();
    }

    @Override
    public void overrideAppPendingTransition(int pending) {
        transaction.overrideAppPendingTransition(pending);
    }

    @Override
    public void startActivity(Class<?> cls) {
        transaction.startActivity(cls);
    }

    @Override
    public void startActivity(Class<?> cls, Bundle options) {
        transaction.startActivity(cls);
    }

    @Override
    public void startActivityForResult(Class<?> cls, int requestCode) {
        transaction.startActivityForResult(cls, requestCode);
    }

    @Override
    public void startActivityForResult(Class<?> cls, int requestCode, Bundle options) {
        transaction.startActivityForResult(cls, requestCode, options);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onActivityResult(getActivity(), requestCode, resultCode, data);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        transaction.onActivityResult(activity, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onRequestPermissionsGranted(int requestCode, String[] permissions) {

    }

    @Override
    public void onRequestPermissionsDenied(int requestCode, String[] permissions) {

    }

    @Override
    public void onRequestPermissionRationale(int requestCode, String[] permissions) {

    }

    @Override
    public void showLoading() {
        getLoading().showLoading();
    }

    @Override
    public void showLoading(String msg) {
        getLoading().showLoading(msg);
    }

    @Override
    public void dismissLoading() {
        getLoading().dismissLoading();
    }

    /**
     * 显示提示
     *
     * @param msg 内容
     */
    public void showToast(String msg) {
        AppToast.show(getContext(), msg);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onInitViews() {

    }

    @Override
    public void addClick(int... ids) {
        for (int i = 0; i < ids.length; i++) {
            findViewById(ids[i]).setOnClickListener(this);
        }
    }

    @Override
    public void addClick(View... views) {
        for (int i = 0; i < views.length; i++) {
            views[i].setOnClickListener(this);
        }
    }

    @Override
    public <T extends View> T find(Class<T> clazz, int id) {
        return findViewById(id);
    }

}
