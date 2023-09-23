package androidx.ui.app;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

/**
 * 页面布局
 */
public interface AppLayout {

    /**
     * 获取页面上下文
     *
     * @return
     */
    Context getAppContext();

    /**
     * 获取页面内容布局
     *
     * @return
     */
    int getContentViewLayoutResId();

    /**
     * 获取Fragment内容容器
     *
     * @return
     */
    int getContainerViewResId();

    /**
     * 获取Fragment管理器
     *
     * @return
     */
    FragmentManager getAppFragmentManager();

}
