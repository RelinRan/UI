package androidx.ui.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.ui.util.Size;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppPackage {

    public static final String TAG = AppPackage.class.getSimpleName();
    /**
     * 安装Apk请求代码
     */
    public static int REQUEST_CODE_INSTALL = -1;
    /**
     * 安装ApK授权
     */
    public static String AUTHORITY;
    /**
     * 安装Apk文件
     */
    public static File APK;

    /**
     * @param context 是否是桌面
     * @return 当前界面是否是桌面
     */
    public static boolean isDesktop(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        List<String> list = getDesktopPackages(context);
        if (Size.of(list) != 0 && Size.of(rti) != 0) {
            return list.contains(rti.get(0).topActivity.getPackageName());
        } else {
            return false;
        }
    }

    /**
     * @param context 是否是桌面
     * @return 属于桌面的应用的应用包名称
     */
    public static List<String> getDesktopPackages(Context context) {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : resolveInfo) {
            names.add(info.activityInfo.packageName);
            Log.i(TAG, "getDesktopPackages packageName =" + info.activityInfo.packageName);
        }
        return names;
    }

    /**
     * 启动App
     *
     * @param context     上下文
     * @param packageName 包名
     * @param bundle      参数
     */
    public static void startBackgroundActivity(Context context, String packageName, Bundle bundle) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName appTopActivity = null;
        //获取当前正在运行的应用列表
        List<ActivityManager.RunningTaskInfo> infos = manager.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : infos) {
            //判断原app是否还在运行
            if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                appTopActivity = info.topActivity;
            }
        }
        if (isBackground(context, packageName)) {
            Intent intent = new Intent();
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            //在receiver或者service里新建activity都要添加这个属性
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(appTopActivity);
            //使用addFlags,而不是setFlags,掉Task栈需要显示Activity之上的其他activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //加上这个才不会新建立一个Activity，而是显示旧的
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        }
    }

    /**
     * 启动新实例APP
     *
     * @param context       上下文
     * @param componentName 包名 + 类名{@link ComponentName#createRelative(String, String)}
     * @param param         需要传递的参数
     */
    private void startNewInstanceActivity(Context context, ComponentName componentName, Bundle param) {
        if (componentName != null) {
            try {
                context.getPackageManager().getPackageInfo(componentName.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(context, "您手机上还未安装该APP!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            try {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(componentName);
                if (param != null) {
                    intent.putExtras(param);
                }
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "APP启动异常!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * @param context     上下文
     * @param packageName 程序包名
     * @return App是否在前端或后台运行运行
     */
    public static boolean isForeground(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RecentTaskInfo> appTask = activityManager.getRecentTasks(Integer.MAX_VALUE, 1);
        if (appTask == null) {
            return false;
        }
        if (appTask.get(0).baseIntent.toString().contains(packageName)) {
            return true;
        }
        return false;
    }

    /**
     * @param context        上下文
     * @param appPackageName 应用的包名
     * @return PP是否在后台运行
     */
    public static boolean isBackground(Context context, String appPackageName) {
        boolean isInBackground = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName appTopActivity = null;
        //如果是Android 5.0以上
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            //如果是Android 5.0以下,取当前正在运行的应用列表
            List<ActivityManager.RunningTaskInfo> rti = manager.getRunningTasks(100);
            for (ActivityManager.RunningTaskInfo info : rti) {
                if (info.topActivity.getPackageName().equals(appPackageName) && info.baseActivity.getPackageName().equals(appPackageName)) {
                    appTopActivity = info.topActivity;
                }
            }
            if (appTopActivity != null) {
                isInBackground = true;
            } else {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    /**
     * @param context 上下文对象
     * @return 版本号
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return versionName;
    }

    /**
     * 获取版本
     *
     * @param context 上下文对象
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return versionCode;
    }

    /**
     * @param localVersion 本地版本，例如：1.0.0
     * @param apiVersion   接口版本，例如：1.1.0
     * @return 是否新版本
     */
    public static boolean isNewVersion(String localVersion, String apiVersion) {
        String[] localItems = localVersion.split("\\.");
        String[] apiItems = apiVersion.split("\\.");
        int[] localIntItems = toIntArray(localItems);
        int[] apiIntItems = toIntArray(apiItems);
        if (localIntItems.length >= apiIntItems.length) {
            for (int i = 0; i < apiIntItems.length; i++) {
                if (localIntItems[i] < apiIntItems[i]) {
                    return true;
                }
                if (localIntItems[i] > apiIntItems[i]) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < localIntItems.length; i++) {
                if (localIntItems[i] < apiIntItems[i]) {
                    return true;
                }
                if (localIntItems[i] > apiIntItems[i]) {
                    return false;
                }
            }
            int lastIndex = localIntItems.length - 1;
            if (localIntItems[lastIndex] < apiIntItems[lastIndex]) {
                return true;
            }
            return localIntItems[lastIndex] == apiIntItems[lastIndex];
        }
        return false;
    }

    /**
     * @param array String数组
     * @return Int数组
     */
    private static int[] toIntArray(String[] array) {
        int[] items = new int[array.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = Integer.parseInt(array[i]);
        }
        return items;
    }

    /**
     * 安装APK
     *
     * @param context   上下文对象
     * @param authority 权限
     * @param file      apk文件
     */
    public static void installPackage(Context context, String authority, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(context, authority, file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * @param context 上下文
     * @return 是否能安装apk
     */
    public static boolean canRequestPackageInstalls(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return context.getApplicationContext().getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    /**
     * 管理未知来源
     *
     * @param activity    页面
     * @param requestCode 请求码
     */
    public static void manageUnknownAppSources(Activity activity, int requestCode) {
        Uri packageUri = Uri.parse("package:" + activity.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param requestCode 请求代码
     * @param resultCode  结果代码
     * @return 是否未知来源apk请求
     */
    public static boolean isUnknownAppSourcesInstallRequest(int requestCode, int resultCode) {
        return requestCode == REQUEST_CODE_INSTALL && resultCode == Activity.RESULT_OK;
    }

    /**
     * 处理结果
     *
     * @param activity    页面
     * @param requestCode 请求代码
     * @param resultCode  结果代码
     * @param data        数据
     */
    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (isUnknownAppSourcesInstallRequest(requestCode, resultCode) && canRequestPackageInstalls(activity)&&APK!=null) {
            installPackage(activity, AUTHORITY, APK);
        }
    }

    /**
     * 请求安装apk
     * 注意：声明权限 REQUEST_INSTALL_PACKAGES
     *
     * @param activity  页面
     * @param authority 权限
     * @param file      apk文件
     */
    public static boolean installApk(Activity activity, String authority, File file) {
        AUTHORITY = authority;
        APK = file;
        Context appContext = activity.getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (canRequestPackageInstalls(appContext)) {
                installPackage(appContext, authority, file);
                return true;
            } else {
                manageUnknownAppSources(activity, REQUEST_CODE_INSTALL);
                return false;
            }
        } else {
            installPackage(appContext, authority, file);
            return true;
        }
    }

    /**
     * 返回主页
     *
     * @param context 上下文对象
     */
    public static void startHomeActivity(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * @param context 上下文
     * @return 应用包名
     */
    public static String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param context 上下文对象
     * @return 启动类名
     */
    public static String getLauncherClassName(Context context) {
        String launcherClassName = "";
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            if (context != null && context.getPackageName().equalsIgnoreCase(resolveInfo.activityInfo.applicationInfo.packageName)) {
                launcherClassName = resolveInfo.activityInfo.name;
                break;
            }
        }
        Log.i(TAG, "launcherClassName:" + launcherClassName);
        return launcherClassName;
    }

}
