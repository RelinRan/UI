package androidx.ui.app;

import androidx.annotation.DrawableRes;

public interface AppPlaceholder {

    /**
     * 占位 - 空数据
     */
    int PLACEHOLDER_EMPTY = 110;
    /**
     * 占位 - 错误
     */
    int PLACEHOLDER_ERROR = 111;
    /**
     * 占位 - 网络
     */
    int PLACEHOLDER_NET = 112;
    /**
     * 占位 - 消息
     */
    int PLACEHOLDER_MSG = 113;

    /**
     * 设置占位参数
     *
     * @param type  类型{@link #PLACEHOLDER_ERROR}等
     * @param resId 图片资源
     * @param text  文字
     */
    void setPlaceholder(int type, @DrawableRes int resId, String text);

    /**
     * 设置占位参数
     *
     * @param type  类型{@link #PLACEHOLDER_ERROR}等
     * @param resId 图片资源
     */
    void setPlaceholder(int type, @DrawableRes int resId);

    /**
     * 设置占位参数
     *
     * @param type 类型{@link #PLACEHOLDER_ERROR}等
     * @param text 文字
     */
    void setPlaceholder(int type, String text);

    /**
     * 显示占位图
     *
     * @param type 占位类型{@link #PLACEHOLDER_EMPTY} OR {@link #PLACEHOLDER_ERROR}
     */
    void showPlaceholder(int type);

    /**
     * 隐藏占位图
     */
    void hidePlaceholder();


}
