package androidx.ui.flow;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 水平布局子元素，直到行满，然后移动到下一行。 称呼
 * {@link FlowLayout#setSingleLine(boolean)} 禁用回流并将所有子级排成一行。
 */
public class FlowLayout extends ViewGroup {

    private int lineSpacing;
    private int itemSpacing;
    private boolean singleLine;
    private int rowCount;

    public FlowLayout(@NonNull Context context) {
        this(context, null);
    }

    public FlowLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        singleLine = false;
        loadFromAttributes(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlowLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        singleLine = false;
        loadFromAttributes(context, attrs);
    }

    private void loadFromAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        lineSpacing = array.getDimensionPixelSize(R.styleable.FlowLayout_android_verticalSpacing, 0);
        itemSpacing = array.getDimensionPixelSize(R.styleable.FlowLayout_android_horizontalSpacing, 0);
        array.recycle();
    }

    /**
     * @return 行间距
     */
    public int getLineSpacing() {
        return lineSpacing;
    }

    /**
     * 设置行间距
     *
     * @param lineSpacing 行间距
     */
    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
        invalidate();
    }

    /**
     * @return item间距
     */
    public int getItemSpacing() {
        return itemSpacing;
    }

    /**
     * 设置item间距
     *
     * @param itemSpacing item间距
     */
    public void setItemSpacing(int itemSpacing) {
        this.itemSpacing = itemSpacing;
        invalidate();
    }

    /**
     * 返回此芯片组是单行还是回流多行。
     */
    public boolean isSingleLine() {
        return singleLine;
    }

    /**
     * 设置此芯片组是单行还是回流多行。
     */
    public void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int maxWidth = widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY ? width : Integer.MAX_VALUE;
        int childLeft = getPaddingLeft();
        int childTop = getPaddingTop();
        int childBottom = childTop;
        int childRight = childLeft;
        int maxChildRight = 0;
        final int maxRight = maxWidth - getPaddingRight();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            LayoutParams lp = child.getLayoutParams();
            int leftMargin = 0;
            int rightMargin = 0;
            if (lp instanceof MarginLayoutParams) {
                MarginLayoutParams marginLp = (MarginLayoutParams) lp;
                leftMargin += marginLp.leftMargin;
                rightMargin += marginLp.rightMargin;
            }
            childRight = childLeft + leftMargin + child.getMeasuredWidth();
            //如果当前孩子的右边界超过Flowlayout的最大右边界并且flowlayout是
            //不局限于一行，将此子元素移动到下一行并将其左边界重置为
            //flowlayout 的左边界。
            if (childRight > maxRight && !isSingleLine()) {
                childLeft = getPaddingLeft();
                childTop = childBottom + lineSpacing;
            }
            childRight = childLeft + leftMargin + child.getMeasuredWidth();
            childBottom = childTop + child.getMeasuredHeight();
            //如果当前孩子的右边界超过它，则更新 Flowlayout 的最大右边界。
            if (childRight > maxChildRight) {
                maxChildRight = childRight;
            }
            childLeft += (leftMargin + rightMargin + child.getMeasuredWidth()) + itemSpacing;
            // 对于所有前面的孩子，下一个考虑孩子的右边距
            // 孩子的左边界 (childLeft)。 然而， childLeft 在最后一个孩子之后被忽略，所以
            // 最后一个孩子的右边距需要显式添加到 Flowlayout 的最大右边界。
            if (i == (getChildCount() - 1)) {
                maxChildRight += rightMargin;
            }
        }
        maxChildRight += getPaddingRight();
        childBottom += getPaddingBottom();
        int finalWidth = getMeasuredDimension(width, widthMode, maxChildRight);
        int finalHeight = getMeasuredDimension(height, heightMode, childBottom);
        setMeasuredDimension(finalWidth, finalHeight);
    }

    private static int getMeasuredDimension(int size, int mode, int childrenEdge) {
        switch (mode) {
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.AT_MOST:
                return Math.min(childrenEdge, size);
            default:
                return childrenEdge;
        }
    }

    @Override
    protected void onLayout(boolean sizeChanged, int left, int top, int right, int bottom) {
        if (getChildCount() == 0) {
            //没有孩子时不要重新布局。
            rowCount = 0;
            return;
        }
        rowCount = 1;
        boolean isRtl = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
        int paddingStart = isRtl ? getPaddingRight() : getPaddingLeft();
        int paddingEnd = isRtl ? getPaddingLeft() : getPaddingRight();
        int childStart = paddingStart;
        int childTop = getPaddingTop();
        int childBottom = childTop;
        int childEnd;
        final int maxChildEnd = right - left - paddingEnd;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                child.setTag(R.id.row_index_key, -1);
                continue;
            }
            LayoutParams lp = child.getLayoutParams();
            int startMargin = 0;
            int endMargin = 0;
            if (lp instanceof MarginLayoutParams) {
                MarginLayoutParams marginLp = (MarginLayoutParams) lp;
                startMargin = MarginLayoutParamsCompat.getMarginStart(marginLp);
                endMargin = MarginLayoutParamsCompat.getMarginEnd(marginLp);
            }
            childEnd = childStart + startMargin + child.getMeasuredWidth();
            if (!singleLine && (childEnd > maxChildEnd)) {
                childStart = paddingStart;
                childTop = childBottom + lineSpacing;
                rowCount++;
            }
            child.setTag(R.id.row_index_key, rowCount - 1);
            childEnd = childStart + startMargin + child.getMeasuredWidth();
            childBottom = childTop + child.getMeasuredHeight();
            if (isRtl) {
                child.layout(maxChildEnd - childEnd, childTop, maxChildEnd - childStart - startMargin, childBottom);
            } else {
                child.layout(childStart + startMargin, childTop, childEnd, childBottom);
            }
            childStart += (startMargin + endMargin + child.getMeasuredWidth()) + itemSpacing;
        }
    }

    /**
     * @return 行数
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * @param rowIndex 行下标
     * @return 当前行所有View
     */
    public View[] getRowViews(int rowIndex) {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (rowIndex == getRowIndex(child)) {
                views.add(child);
            }
        }
        return views.toArray(new View[views.size()]);
    }

    /**
     * @param rowIndex 行下标
     * @return 当前行第一个View
     */
    public View getRowFirst(int rowIndex) {
        View[] views = getRowViews(rowIndex);
        if (views.length > 0) {
            return views[0];
        }
        return null;
    }

    /**
     * @param rowIndex 行下标
     * @return 当前行最后一个View
     */
    public View getRowLast(int rowIndex) {
        View[] views = getRowViews(rowIndex);
        if (views.length > 0) {
            return views[views.length - 1];
        }
        return null;
    }

    /**
     * 获取子项的行索引，主要用于可访问性。
     */
    public int getRowIndex(@NonNull View child) {
        Object index = child.getTag(R.id.row_index_key);
        if (!(index instanceof Integer)) {
            return -1;
        }
        return (int) index;
    }

}
