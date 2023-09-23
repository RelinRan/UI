package androidx.ui.app;

/**
 * 正在加载
 */
public interface AppLoading {

    /**
     * 显示正在加载
     */
    void showLoading();

    /**
     * 显示正在加载
     *
     * @param msg 提示文字
     */
    void showLoading(String msg);

    /**
     * 隐藏正在加载
     */
    void dismissLoading();

}
