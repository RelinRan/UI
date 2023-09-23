package androidx.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.ui.R;

/**
 * 形状布局
 */
public class ShapeLayout extends RelativeLayout {

    /**
     * 填充颜色
     */
    private int solid = getResources().getColor(R.color.shape_solid_color);
    /**
     * 线条宽度
     */
    private int strokeWidth = 0;
    /**
     * 线条颜色
     */
    private int strokeColor = getResources().getColor(R.color.shape_stroke_color);
    /**
     * 图形
     */
    private int shape = 0;
    /**
     * 圆角
     */
    private float radius = 0;
    /**
     * 左上圆角
     */
    private float topLeftRadius = 0;
    /**
     * 右上圆角
     */
    private float topRightRadius = 0;
    /**
     * 左下圆角
     */
    private float bottomLeftRadius = 0;
    /**
     * 右下圆角
     */
    private float bottomRightRadius = 0;

    public ShapeLayout(Context context) {
        super(context);
        initAttributeSet(context, null);
    }

    public ShapeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(context, attrs);
    }

    public ShapeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
    }

    /**
     * 初始化属性参数
     *
     * @param context 上下文
     * @param attrs   属性
     */
    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShapeLayout);
            solid = array.getColor(R.styleable.ShapeLayout_solidColor, solid);
            strokeWidth = (int) array.getDimension(R.styleable.ShapeLayout_strokeWidth, strokeWidth);
            strokeColor = array.getColor(R.styleable.ShapeLayout_strokeColor, strokeColor);
            if (array.getString(R.styleable.ShapeLayout_shape) != null) {
                shape = Integer.parseInt(array.getString(R.styleable.ShapeLayout_shape));
            }
            radius = array.getDimension(R.styleable.ShapeLayout_radius, 0);
            topLeftRadius = array.getDimension(R.styleable.ShapeLayout_topLeftRadius, 0);
            topRightRadius = array.getDimension(R.styleable.ShapeLayout_topRightRadius, 0);
            bottomLeftRadius = array.getDimension(R.styleable.ShapeLayout_bottomLeftRadius, 0);
            bottomRightRadius = array.getDimension(R.styleable.ShapeLayout_bottomRightRadius, 0);
            array.recycle();
        }
        drawDrawable();
    }

    /**
     * 绘制背景
     */
    protected void drawDrawable() {
        Drawable normalDrawable = createShape(shape, strokeWidth, strokeColor, solid, radius, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
        int[][] states = new int[2][];
        states[0] = new int[]{-android.R.attr.state_pressed};
        states[1] = new int[]{-android.R.attr.state_pressed};
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(states[0], normalDrawable);
        stateListDrawable.addState(states[1], normalDrawable);
        Drawable wrapDrawable = DrawableCompat.wrap(stateListDrawable);
        setDrawable(wrapDrawable);
    }

    /**
     * 适用于所有android api
     * 设置各种背景。
     *
     * @param drawable
     */
    private void setDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    /**
     * 创建Shape
     * 这个方法是为了创建一个Shape来替代xml创建Shape.
     *
     * @param shape             类型 GradientDrawable.RECTANGLE  GradientDrawable.OVAL
     * @param strokeWidth       外线宽度 button stroke width
     * @param strokeColor       外线颜色 button stroke color
     * @param solidColor        填充颜色 button background color
     * @param cornerRadius      圆角大小 all corner is the same as is the radius
     * @param topLeftRadius     左上圆角 top left corner radius
     * @param topRightRadius    右上圆角 top right corner radius
     * @param bottomLeftRadius  底左圆角  bottom left corner radius
     * @param bottomRightRadius 底右圆角 bottom right corner radius
     * @return
     */
    public Drawable createShape(int shape, int strokeWidth,
                                int strokeColor, int solidColor, float cornerRadius,
                                float topLeftRadius, float topRightRadius,
                                float bottomLeftRadius, float bottomRightRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(shape);
        drawable.setSize(10, 10);
        drawable.setStroke(strokeWidth, strokeColor);
        drawable.setColor(solidColor);
        if (cornerRadius != 0) {
            drawable.setCornerRadius(cornerRadius);
        } else {
            drawable.setCornerRadii(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius});
        }
        return drawable;
    }

}
