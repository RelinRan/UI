package androidx.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * 形状按钮
 */
public class ShapeButton extends ShapeText {

    public ShapeButton(@NonNull Context context) {
        super(context);
    }

    public ShapeButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShapeButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDrawDrawable() {
        Drawable normalDrawable = createShape(shape, strokeWidth, strokeColor, solid, radius, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
        Drawable pressedDrawable = createShape(shape, strokeWidth, createPressedColor(strokeColor), createPressedColor(solid), radius, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
        int[][] states = new int[4][];
        states[0] = new int[]{-android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{-android.R.attr.state_focused};
        states[3] = new int[]{android.R.attr.state_focused};
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(states[0], normalDrawable);
        stateListDrawable.addState(states[1], pressedDrawable);
        stateListDrawable.addState(states[2], normalDrawable);
        stateListDrawable.addState(states[3], pressedDrawable);
        Drawable wrapDrawable = DrawableCompat.wrap(stateListDrawable);
        setDrawable(wrapDrawable);
    }

    /**
     * 使用HSV创建按下状态颜色
     * hsv[2]:值是大的-是深的或亮的
     *
     * @param color
     * @return
     */
    protected int createPressedColor(int color) {
        int alpha = Color.alpha(color);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= saturation;
        return Color.HSVToColor(alpha, hsv);
    }
    
}
