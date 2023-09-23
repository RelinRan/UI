package androidx.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.ui.R;

/**
 * 卡片图片
 */
public class CardImage extends CardView {

    private ImageView imageView;
    private static final ImageView.ScaleType[] sScaleTypeArray = {
            ImageView.ScaleType.MATRIX,
            ImageView.ScaleType.FIT_XY,
            ImageView.ScaleType.FIT_START,
            ImageView.ScaleType.FIT_CENTER,
            ImageView.ScaleType.FIT_END,
            ImageView.ScaleType.CENTER,
            ImageView.ScaleType.CENTER_CROP,
            ImageView.ScaleType.CENTER_INSIDE
    };

    public CardImage(@NonNull Context context) {
        super(context);
        initAttributeSet(context, null);
    }

    public CardImage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(context, attrs);
    }

    public CardImage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
    }

    /**
     * 初始化属性
     *
     * @param context 上下文
     * @param attrs   属性
     */
    protected void initAttributeSet(Context context, AttributeSet attrs) {
        imageView = new ImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CardImage);
            int index = array.getInt(R.styleable.CardImage_android_scaleType, -1);
            boolean adjustViewBounds = array.getBoolean(R.styleable.CardImage_android_adjustViewBounds, false);
            if (index >= 0) {
                imageView.setScaleType(sScaleTypeArray[index]);
            }
            imageView.setAdjustViewBounds(adjustViewBounds);
            Drawable d = array.getDrawable(R.styleable.CardImage_android_src);
            if (d != null) {
                imageView.setImageDrawable(d);
            }
            array.recycle();
        }
        addView(imageView);
    }

    /**
     * @return 图片
     */
    public ImageView getImageView() {
        return imageView;
    }

}
