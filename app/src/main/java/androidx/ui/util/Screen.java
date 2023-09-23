package androidx.ui.util;

import android.content.res.Resources;

/**
 * 屏幕工具
 */
public class Screen {

    /**
     * Get the screen of density
     *
     * @return
     */
    public static float density() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * The screen of width.
     *
     * @return
     */
    public static int width() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * 宽度百分比
     *
     * @param percent
     * @return
     */
    public static int width(float percent) {
        return (int) (width() * percent);
    }

    /**
     * The screen of height.
     *
     * @return
     */
    public static int height() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * 高度百分比
     *
     * @param percent
     * @return
     */
    public static int height(float percent) {
        return (int) (height() * percent);
    }


    /**
     * Px to dp
     *
     * @param px
     * @return
     */
    public static float px2Dp(float px) {
        return px / density();
    }

    /**
     * dp to px
     *
     * @param dp
     * @return
     */
    public static float dp2Px(float dp) {
        return dp * density();
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public static int sp2Px(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int px2Sp(float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

}
