package androidx.ui.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.DimenRes;

/**
 * 尺度单位<br/>
 * 获取资源尺寸、对应类型尺寸。<br/>
 */
public class Dimension {

    /**
     * 检索特定资源 ID 的维度。 单元转换基于当前关联的 {@link DisplayMetrics}与资源。
     *
     * @param context 上下文
     * @param id      资源ID
     * @return
     */
    public static float getDimension(Context context, @DimenRes int id) {
        return context.getResources().getDimension(id);
    }

    /**
     * 检索特定资源 ID 的维度以供使用作为原始像素的大小。
     * 这与{@link #getDimension}，除了返回值转换为用作大小的整数像素。
     * 尺寸转换涉及四舍五入基值，并确保非零基值至少是一个像素大小。
     *
     * @param context 上下文
     * @param id      资源ID
     * @return
     */
    public static int getDimensionPixelSize(Context context, @DimenRes int id) {
        return context.getResources().getDimensionPixelSize(id);
    }

    /**
     * 检索特定资源 ID 的维度以供使用作为原始像素的偏移量。 这与{@link #getDimension}，除了返回值转换为为您提供整数像素。
     * 偏移量转换只涉及将基值截断为整数。
     *
     * @param context 上下文
     * @param id      资源ID
     * @return
     */
    public static int getDimensionPixelOffset(Context context, @DimenRes int id) {
        return context.getResources().getDimensionPixelOffset(id);
    }

    /**
     * 复杂单位：值是原始像素
     *
     * @param context
     * @param value
     * @return
     */
    public static float px(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value, context.getResources().getDisplayMetrics());
    }

    /**
     * 复杂单位：值与设备无关
     *
     * @param context
     * @param value
     * @return
     */
    public static float dip(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    /**
     * 复杂单位：值是一个缩放像素。
     *
     * @param context
     * @param value
     * @return
     */
    public static float sp(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics());
    }

    /**
     * 复杂单位：值以点为单位。
     *
     * @param context
     * @param value
     * @return
     */
    public static float points(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value, context.getResources().getDisplayMetrics());
    }

    /**
     * 复杂单位：值以英寸为单位。
     *
     * @param context
     * @param value
     * @return
     */
    public static float inches(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, value, context.getResources().getDisplayMetrics());
    }

    /**
     * 复杂单位：值以毫米为单位。
     *
     * @param context
     * @param value
     * @return
     */
    public static float millimeters(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, value, context.getResources().getDisplayMetrics());
    }

}
