package androidx.ui.app;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Stack;

/**
 * 应用碎片管理<br/>
 * 主要在Activity中替换、添加碎片。<br/>
 */
public class AppFragmentManager {

    /**
     * 碎片 - 添加
     */
    public final static int ADD = 1;
    /**
     * 碎片 - 替换
     */
    public final static int REPLACE = 2;

    /**
     * 应用页面布局
     */
    private AppLayout appLayout;
    /**
     * 碎片集合
     */
    private Stack<Fragment> stack;

    private Fragment currentFragment;

    /**
     * 应用碎片管理
     *
     * @param appLayout 应用页面布局类
     */
    public AppFragmentManager(AppLayout appLayout) {
        this.appLayout = appLayout;
        stack = new Stack<>();
    }

    /**
     * 获取容器资源id
     *
     * @return 容器资源id
     */
    public int getContainerViewResId() {
        return appLayout.getContainerViewResId();
    }

    /**
     * 获取碎片管理器
     *
     * @return
     */
    public FragmentManager getFragmentManager() {
        return appLayout.getAppFragmentManager();
    }

    /**
     * 添加碎片
     *
     * @param fragment 碎片
     */
    public void add(Fragment fragment) {
        transaction(ADD, fragment, false);
    }

    /**
     * 添加碎片
     *
     * @param fragment       碎片
     * @param addToBackStack 是否添加到回退栈
     */
    public void add(Fragment fragment, boolean addToBackStack) {
        transaction(ADD, fragment, addToBackStack);
    }

    /**
     * 找到Stack里面的Fragment
     *
     * @param clazz Fragment的类
     * @return
     */
    public Fragment findStackFragment(Class<? extends Fragment> clazz) {
        for (int i = 0; i < stack.size(); i++) {
            if (clazz == stack.get(i).getClass()) {
                return stack.get(i);
            }
        }
        return null;
    }

    /**
     * 添加Fragment
     *
     * @param clazz 碎片
     */
    public void add(Class<? extends Fragment> clazz) {
        add(clazz, null);
    }

    /**
     * 添加Fragment
     *
     * @param clazz   碎片
     * @param options 参数
     */
    public void add(Class<? extends Fragment> clazz, Bundle options) {
        try {
            Fragment fragment = findStackFragment(clazz);
            if (fragment == null) {
                fragment = clazz.newInstance();
                stack.push(fragment);
            }
            if (options != null) {
                fragment.setArguments(options);
            }
            add(fragment);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换碎片
     *
     * @param fragment 碎片
     */
    public void replace(Fragment fragment) {
        transaction(REPLACE, fragment, false);
    }

    /**
     * 替换碎片
     *
     * @param fragment       碎片
     * @param addToBackStack 是否添加到回退栈
     */
    public void replace(Fragment fragment, boolean addToBackStack) {
        transaction(REPLACE, fragment, addToBackStack);
    }

    /**
     * 替换Fragment
     *
     * @param clazz   碎片
     * @param options 参数
     */
    public void replace(Class<? extends Fragment> clazz, Bundle options) {
        try {
            Fragment fragment = findStackFragment(clazz);
            if (fragment == null) {
                fragment = clazz.newInstance();
                stack.push(fragment);
            }
            if (options != null) {
                fragment.setArguments(options);
            }
            replace(fragment);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fragment事务处理
     *
     * @param type           类型{@link #ADD} OR {@link #REPLACE}
     * @param fragment       碎片
     * @param addToBackStack 是否添加到回退栈
     */
    public void transaction(int type, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        String tag = String.valueOf(System.currentTimeMillis());
        if (type == ADD) {
            if (fragment.isAdded()) {
                for (Fragment item : stack) {
                    if (fragment.getTag().equals(item.getTag())) {
                        Log.i("RRL", "======1=======>");
                        item.onResume();
                        transaction.show(item);
                        currentFragment = item;
                    } else {
                        Log.i("RRL", "======2=======>");
                        transaction.hide(item);
                    }
                }
            } else {
                Log.i("RRL", "======3=======>");
                stack.push(fragment);
                currentFragment = fragment;
                transaction.add(getContainerViewResId(), fragment, tag);
            }
        }
        if (type == REPLACE) {
            transaction.replace(getContainerViewResId(), fragment, tag);
        }
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * @return 当前操作Fragment
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    /**
     * 弹出栈
     */
    public void popBackStack() {
        if (getFragmentManager() == null) {
            new RuntimeException("popTop failed , activity or fragment is null.").printStackTrace();
            return;
        }
        FragmentManager manager = getFragmentManager();
        manager.popBackStackImmediate();
    }

    /**
     * 弹出栈
     *
     * @param position 剩余位置
     */
    public void popBackStack(int position) {
        if (getFragmentManager() == null) {
            new RuntimeException("pop failed , activity or fragment is null.").printStackTrace();
            return;
        }
        FragmentManager manager = getFragmentManager();
        while (manager.getBackStackEntryCount() > position + 1) {
            manager.popBackStackImmediate();
        }
        getFragmentManager().executePendingTransactions();
    }

}
