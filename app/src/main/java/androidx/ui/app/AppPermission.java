package androidx.ui.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用权限
 */
public class AppPermission {

    private static final String TAG = AppPermission.class.getSimpleName();
    /**
     * 请求代码
     */
    public static final int REQUEST_PERMISSION_CODE = 1;
    /**
     * 请求文件相关权限代码
     */
    public static final int REQUEST_STORAGE_CODE = 2;
    /**
     * 文件读写权限 Android 10及其以下
     */
    public final static String GROUP_STORAGE[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    /**
     * 文件管理权限 Android 11以上
     */
    public final static String MANAGE_EXTERNAL_STORAGE[] = {
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
    };

    public final static String GROUP_MEDIA[] = {
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES,
    };
    /**
     * 照相机权限
     */
    public final static String GROUP_CAMERA[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    /**
     * 视频录制权限
     */
    public final static String GROUP_VIDEO[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };
    /**
     * 定位权限
     */
    public final static String GROUP_LOCATION[] = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    /**
     * 页面
     */
    private Activity activity;
    /**
     * 碎片
     */
    private Fragment fragment;
    /**
     * 权限请求监听
     */
    private OnRequestPermissionsListener listener;
    private String[] permissions;
    private int requestCode;
    private boolean startForResult;

    /**
     * 权限构造函数
     *
     * @param activity 页面
     * @param listener 权限请求监听
     */
    public AppPermission(Activity activity, OnRequestPermissionsListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    /**
     * 权限构造函数
     *
     * @param fragment 页面
     * @param listener 权限请求监听
     */
    public AppPermission(Fragment fragment, OnRequestPermissionsListener listener) {
        this.fragment = fragment;
        this.listener = listener;
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public Context getContext() {
        if (fragment != null) {
            return fragment.getContext();
        }
        return activity;
    }

    /**
     * 是否显示权限理由
     *
     * @param permission
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean shouldShowRequestPermissionRationale(String permission) {
        if (activity != null) {
            return activity.shouldShowRequestPermissionRationale(permission);
        }
        return fragment.shouldShowRequestPermissionRationale(permission);
    }

    /**
     * 检查被拒绝的权限
     *
     * @param permissions 权限
     * @return 授权失败的权限
     */
    public List<String> checkDeniedPermissions(String[] permissions) {
        List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(getContext(), permissions[i]) == PackageManager.PERMISSION_DENIED) {
                denied.add(permissions[i]);
            }
        }
        return denied;
    }

    /**
     * 检查显示请求理由的权限
     *
     * @param permissions 权限
     * @return
     */
    public List<String> checkShowRequestRationalePermissions(String[] permissions) {
        List<String> disabled = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(permissions[i])) {
                    disabled.add(permissions[i]);
                }
            }
        }
        return disabled;
    }

    /**
     * 集合转数组
     *
     * @param permissions 权限
     * @return
     */
    public String[] toArray(List<String> permissions) {
        int size = permissions == null ? 0 : permissions.size();
        String[] array = new String[size];
        for (int i = 0; i < size; i++) {
            array[i] = permissions.get(i);
        }
        return array;
    }

    /**
     * 数组转集合
     *
     * @param permissions 权限
     * @return
     */
    public List<String> toList(String[] permissions) {
        int size = permissions.length;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(permissions[i]);
        }
        return list;
    }

    /**
     * 请求文件存储权限
     */
    public void requestExternalStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                Log.i(TAG, "onRequestPermissionsGranted");
                if (listener != null) {
                    listener.onRequestPermissionsGranted(REQUEST_STORAGE_CODE, GROUP_STORAGE);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    //Android 13
                    requestPermissions(GROUP_MEDIA, REQUEST_STORAGE_CODE);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    //Android 11 + Android 12
                    requestPermissions(MANAGE_EXTERNAL_STORAGE, REQUEST_STORAGE_CODE);
                }
            }
        } else {
            requestPermissions(GROUP_STORAGE, REQUEST_STORAGE_CODE);
        }
    }

    /**
     * 获取请求代码
     * @return
     */
    public int getRequestCode() {
        return requestCode;
    }

    /**
     * 获取请求的权限
     * @return
     */
    public String[] getPermissions() {
        return permissions;
    }

    /**
     * 请求权限
     *
     * @param permissions 权限数组
     * @param requestCode 请求代码
     */
    public void requestPermissions(String[] permissions, int requestCode) {
        this.permissions = permissions;
        this.requestCode = requestCode;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "requestPermissions permissions: " + toList(permissions).toString() + ", requestCode: " + requestCode);
            List<String> rationalePermissions = checkShowRequestRationalePermissions(permissions);
            if (rationalePermissions.size() > 0) {
                Log.i(TAG, "rationalePermissions: " + rationalePermissions);
                if (listener != null) {
                    listener.onRequestPermissionRationale(requestCode, permissions);
                }
            } else {
                List<String> deniedPermissions = checkDeniedPermissions(permissions);
                Log.i(TAG, "deniedPermissions: " + deniedPermissions);
                if (deniedPermissions.size() > 0) {
                    if (activity != null) {
                        activity.requestPermissions(toArray(deniedPermissions), requestCode);
                    }
                    if (fragment != null) {
                        fragment.requestPermissions(toArray(deniedPermissions), requestCode);
                    }
                } else {
                    Log.i(TAG, "onRequestPermissionsGranted");
                    if (listener != null) {
                        listener.onRequestPermissionsGranted(requestCode, permissions);
                    }
                }
            }
        } else {
            Log.i(TAG, "onRequestPermissionsGranted");
            if (listener != null) {
                listener.onRequestPermissionsGranted(requestCode, permissions);
            }
        }
    }

    /**
     * 重启当前页面
     */
    public void recreate() {
        if (activity != null) {
            activity.recreate();
        }
        if (fragment != null) {
            fragment.getActivity().recreate();
        }
    }

    /**
     * 页面请求返回
     *
     * @param requestCode 请求代码
     * @param resultCode  结果代码
     * @param data        数据
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult requestCode：" + requestCode + ", resultCode: " + resultCode);
        boolean isResultCode = resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED;
        if (startForResult && isResultCode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                Log.i(TAG, "onRequestPermissionsGranted");
                if (listener != null) {
                    listener.onRequestPermissionsGranted(getRequestCode(), permissions);
                }
            }
        }
        startForResult = false;
    }


    /**
     * 处理页面的权限请求
     *
     * @param requestCode  请求代码
     * @param permissions  权限
     * @param grantResults 获取的权限
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        int permissionsSize = permissions.length, grantResultSize = grantResults.length;
        List<String> rationalePermissions = checkShowRequestRationalePermissions(permissions);
        Log.i(TAG, "onRequestPermissionsResult requestCode: " + requestCode + ", permissionsSize: " + permissionsSize + ",grantResultSize: " + grantResultSize);
        if (rationalePermissions.size() > 0) {
            Log.i(TAG, "rationalePermissions: " + rationalePermissions);
            if (listener != null) {
                listener.onRequestPermissionRationale(requestCode, toArray(rationalePermissions));
            }
        } else {
            List<String> deniedPermissions = checkDeniedPermissions(permissions);
            Log.i(TAG, "deniedPermissions: " + deniedPermissions.toString());
            if (deniedPermissions.size() > 0) {
                Log.i(TAG, "onRequestPermissionsDenied");
                if (listener != null) {
                    listener.onRequestPermissionsDenied(requestCode, toArray(deniedPermissions));
                }
            } else {
                Log.i(TAG, "onRequestPermissionsGranted");
                if (listener != null) {
                    listener.onRequestPermissionsGranted(requestCode, permissions);
                }
            }
        }
    }

    /**
     * 启动页面获取结果
     *
     * @param intent      意图
     * @param requestCode 请求代码
     */
    public void startActivityForResult(Intent intent, int requestCode) {
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
        }
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        }
        startForResult = true;
    }

    /**
     * 跳转当前应用的文件访问权限确认页面
     */
    public void startAppAllFilesAccessPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", getContext().getApplicationContext().getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_STORAGE_CODE);
        }
    }

    /**
     * 跳转所有应用的文件访问权限确认页面
     */
    public void startAllFilesAccessPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivityForResult(intent, REQUEST_STORAGE_CODE);
        }
    }

    /**
     * 跳转应用详细设置页面
     */
    public void startApplicationDetailSettings() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getContext().getApplicationContext().getPackageName(), null);
            intent.setData(uri);
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getContext().getApplicationContext().getPackageName());
        }
        startActivityForResult(intent, REQUEST_STORAGE_CODE);
    }


    /**
     * 权限操作监听
     */
    public interface OnRequestPermissionsListener {

        /**
         * 已全通过权限
         *
         * @param permissions
         */
        void onRequestPermissionsGranted(int requestCode, String[] permissions);

        /**
         * 已拒绝权限
         *
         * @param permissions
         */
        void onRequestPermissionsDenied(int requestCode, String[] permissions);

        /**
         * 已禁止权限
         *
         * @param permissions
         */
        void onRequestPermissionRationale(int requestCode, String[] permissions);

    }

}
