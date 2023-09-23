package androidx.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.ui.R;

/**
 * 形状文字
 */
public class ShapeText extends AppCompatTextView {

    /**
     * 位置
     */
    protected int gravity = Gravity.CENTER;
    /**
     * 填充颜色
     */
    protected int solid = getResources().getColor(R.color.shape_solid_color);
    /**
     * 线条宽度
     */
    protected int strokeWidth = 0;
    /**
     * 线条颜色
     */
    protected int strokeColor = getResources().getColor(R.color.shape_stroke_color);
    /**
     * 图形
     */
    protected int shape = 0;
    /**
     * 圆角
     */
    protected float radius = 0;
    /**
     * 左上圆角
     */
    protected float topLeftRadius = 0;
    /**
     * 右上圆角
     */
    protected float topRightRadius = 0;
    /**
     * 左下圆角
     */
    protected float bottomLeftRadius = 0;
    /**
     * 右下圆角
     */
    protected float bottomRightRadius = 0;
    /**
     * 饱和度
     */
    protected float saturation = 0.25f;

    public ShapeText(@NonNull Context context) {
        super(context);
        initAttributeSet(context, null);
    }

    public ShapeText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(context, attrs);
    }

    public ShapeText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
    }

    /**
     * 初始化属性参数
     *
     * @param context 上下文
     * @param attrs   属性
     */
    protected void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeText);
            gravity = typedArray.getInt(R.styleable.ShapeText_android_gravity, gravity);
            solid = typedArray.getColor(R.styleable.ShapeText_solidColor, solid);
            strokeWidth = (int) typedArray.getDimension(R.styleable.ShapeText_strokeWidth, strokeWidth);
            strokeColor = typedArray.getColor(R.styleable.ShapeText_strokeColor, strokeColor);
            if (typedArray.getString(R.styleable.ShapeText_shape) != null) {
                shape = Integer.parseInt(typedArray.getString(R.styleable.ShapeText_shape));
            }
            radius = typedArray.getDimension(R.styleable.ShapeText_radius, 0);
            topLeftRadius = typedArray.getDimension(R.styleable.ShapeText_topLeftRadius, 0);
            topRightRadius = typedArray.getDimension(R.styleable.ShapeText_topRightRadius, 0);
            bottomLeftRadius = typedArray.getDimension(R.styleable.ShapeText_bottomLeftRadius, 0);
            bottomRightRadius = typedArray.getDimension(R.styleable.ShapeText_bottomRightRadius, 0);
            saturation = typedArray.getFloat(R.styleable.ShapeText_saturation, 0.90f);
            typedArray.recycle();
            initStyledAttributes(context, attrs);
        }
        onDrawDrawable();
        setGravity(gravity);
    }

    /**
     * 初始样式属性
     *
     * @param context 上下文
     * @param attrs   属性值
     */
    protected void initStyledAttributes(Context context, AttributeSet attrs) {

    }

    /**
     * 绘制背景
     */
    protected void onDrawDrawable() {
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
    protected void setDrawable(Drawable drawable) {
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
    protected Drawable createShape(int shape, int strokeWidth,
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

    /**
     * 获取填充颜色
     *
     * @return
     */
    public int getSolid() {
        return solid;
    }

    /**
     * 设置填充颜色
     *
     * @param solid
     */
    public void setSolid(int solid) {
        this.solid = solid;
        onDrawDrawable();
        invalidate();
    }

    /**
     * 获取线宽
     *
     * @return
     */
    public int getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * 设置线宽
     *
     * @param strokeWidth
     */
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        onDrawDrawable();
        invalidate();
    }

    /**
     * 获取线颜色
     *
     * @return
     */
    public int getStrokeColor() {
        return strokeColor;
    }

    /**
     * 设置线颜色
     *
     * @param strokeColor
     */
    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        onDrawDrawable();
        invalidate();
    }

    /**
     * 获取图形
     *
     * @return
     */
    public int getShape() {
        return shape;
    }

    /**
     * 设置图形
     *
     * @param shape {@link GradientDrawable#RECTANGLE}  or {@link GradientDrawable#OVAL}
     */
    public void setShape(int shape) {
        this.shape = shape;
        onDrawDrawable();
        invalidate();
    }

    /**
     * 获取半径
     *
     * @return
     */
    public float getRadius() {
        return radius;
    }

    /**
     * 设置四个方向圆角
     *
     * @param radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
        onDrawDrawable();
        invalidate();
    }

    /**
     * 获取顶部左边圆角
     *
     * @return
     */
    public float getTopLeftRadius() {
        return topLeftRadius;
    }

    /**
     * 设置顶部左边圆角
     *
     * @param topLeftRadius
     */
    public void setTopLeftRadius(float topLeftRadius) {
        this.topLeftRadius = topLeftRadius;
        onDrawDrawable();
        invalidate();
    }

    /**
     * 获取顶部右边圆角
     *
     * @return
     */
    public float getTopRightRadius() {
        return topRightRadius;
    }

    /**
     * 设置顶部右边圆角
     *
     * @param topRightRadius
     */
    public void setTopRightRadius(float topRightRadius) {
        this.topRightRadius = topRightRadius;
        onDrawDrawable();
        invalidate();
    }

    /**
     * 获取底部左边圆角
     *
     * @return
     */
    public float getBottomLeftRadius() {
        return bottomLeftRadius;
    }

    /**
     * 设置底部左边圆角
     *
     * @param bottomLeftRadius
     */
    public void setBottomLeftRadius(float bottomLeftRadius) {
        this.bottomLeftRadius = bottomLeftRadius;
        onDrawDrawable();
        invalidate();
    }

    /**
     * 获取底部左边圆角
     *
     * @return
     */
    public float getBottomRightRadius() {
        return bottomRightRadius;
    }

    /**
     * 设置底部右边圆角
     *
     * @param bottomRightRadius
     */
    public void setBottomRightRadius(float bottomRightRadius) {
        this.bottomRightRadius = bottomRightRadius;
        onDrawDrawable();
        invalidate();
    }

    /**
     * 获取饱和度
     *
     * @return
     */
    public float getSaturation() {
        return saturation;
    }

    /**
     * 设置饱和度
     *
     * @param saturation
     */
    public void setSaturation(float saturation) {
        this.saturation = saturation;
        onDrawDrawable();
        invalidate();
    }


}
