package androidx.ui.wheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.ui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 滚轮
 */
public class WheelView extends View {

    protected boolean mCyclic;
    protected int mItemCount;
    protected int mItemWidth;
    protected int mItemHeight;
    private int textSize;
    private int textColor;
    private int selectedTextColor;
    private int dividerColor;
    private int highlightColor;
    private int dividerHeight;
    private CharSequence[] entries;
    protected Rect mClipRectTop;
    protected RectF mClipRectMiddle;
    protected Rect mClipRectBottom;
    protected TextPaint mTextPaint;
    protected TextPaint mSelectedTextPaint;
    protected Paint mDividerPaint;
    protected Paint mHighlightPaint;
    protected float mHighlightRadius = 0;
    protected float mHighlightLeftTopRadius = 0;
    protected float mHighlightLeftBottomRadius = 0;
    protected float mHighlightRightTopRadius = 0;
    protected float mHighlightRightBottomRadius = 0;
    protected float textMarginLeft = 0;
    protected float textMarginRight = 0;
    protected WheelScroller mScroller;
    protected final List<CharSequence> dataSource = new ArrayList<>();

    public WheelView(Context context) {
        super(context);
        initAttributeSet(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(context, attrs);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
    }

    /**
     * 初始化属性
     *
     * @param context 上下文
     * @param attrs   属性
     */
    private void initAttributeSet(Context context, AttributeSet attrs) {
        mCyclic = true;
        mItemCount = 9;
        mItemWidth = $dp(R.dimen.wheel_item_width);
        mItemHeight = $dp(R.dimen.wheel_item_height);
        textSize = $sp(R.dimen.wheel_text_size);
        textColor = $color(R.color.wheel_text_color);
        selectedTextColor = $color(R.color.wheel_selected_text_color);
        dividerColor = $color(R.color.wheel_divider_color);
        highlightColor = $color(R.color.wheel_highlight_color);
        dividerHeight = $dp(R.dimen.wheel_divider_height);
        mHighlightRadius = 0;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelView);
            mCyclic = a.getBoolean(R.styleable.WheelView_wheelCyclic, mCyclic);
            mItemCount = a.getInt(R.styleable.WheelView_wheelItemCount, mItemCount);
            mItemWidth = a.getDimensionPixelOffset(R.styleable.WheelView_wheelItemWidth, mItemWidth);
            mItemHeight = a.getDimensionPixelOffset(R.styleable.WheelView_wheelItemHeight, mItemHeight);
            textSize = a.getDimensionPixelSize(R.styleable.WheelView_wheelTextSize, textSize);
            textColor = a.getColor(R.styleable.WheelView_wheelTextColor, textColor);
            selectedTextColor = a.getColor(R.styleable.WheelView_wheelSelectedTextColor, selectedTextColor);
            dividerColor = a.getColor(R.styleable.WheelView_wheelDividerColor, dividerColor);
            dividerHeight = a.getDimensionPixelOffset(R.styleable.WheelView_wheelDividerHeight, dividerHeight);
            mHighlightRadius = a.getDimension(R.styleable.WheelView_wheelHighlightRadius, mHighlightRadius);
            mHighlightLeftTopRadius = a.getDimension(R.styleable.WheelView_wheelHighlightLeftTopRadius, mHighlightLeftTopRadius);
            mHighlightLeftBottomRadius = a.getDimension(R.styleable.WheelView_wheelHighlightLeftBottomRadius, mHighlightLeftBottomRadius);
            mHighlightRightTopRadius = a.getDimension(R.styleable.WheelView_wheelHighlightRightTopRadius, mHighlightRightTopRadius);
            mHighlightRightBottomRadius = a.getDimension(R.styleable.WheelView_wheelHighlightRightBottomRadius, mHighlightRightBottomRadius);
            textMarginLeft = a.getDimension(R.styleable.WheelView_wheelTextLeftMargin, textMarginLeft);
            textMarginRight = a.getDimension(R.styleable.WheelView_wheelTextRightMargin, textMarginRight);
            highlightColor = a.getColor(R.styleable.WheelView_wheelHighlightColor, highlightColor);
            entries = a.getTextArray(R.styleable.WheelView_wheelEntries);
            a.recycle();
        }

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);

        mSelectedTextPaint = new TextPaint();
        mSelectedTextPaint.setAntiAlias(true);
        mSelectedTextPaint.setTextAlign(Paint.Align.CENTER);
        mSelectedTextPaint.setTextSize(textSize);
        mSelectedTextPaint.setColor(selectedTextColor);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStrokeWidth(dividerHeight);
        mDividerPaint.setColor(dividerColor);

        mHighlightPaint = new Paint();
        mHighlightPaint.setAntiAlias(true);
        mHighlightPaint.setStyle(Paint.Style.FILL);
        mHighlightPaint.setColor(highlightColor);

        if (entries != null && entries.length > 0) {
            dataSource.addAll(Arrays.asList(entries));
        }

        mScroller = new WheelScroller(context, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        } else if (widthSpecMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSpecSize, getPrefHeight());
        } else if (heightSpecMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(getPrefWidth(), heightSpecSize);
        } else {
            setMeasuredDimension(getPrefWidth(), getPrefHeight());
        }
        updateClipRect();
    }

    private void updateClipRect() {
        int clipLeft = getPaddingLeft();
        int clipRight = getMeasuredWidth() - getPaddingRight();
        int clipTop = getPaddingTop();
        int clipBottom = getMeasuredHeight() - getPaddingBottom();
        int clipVMiddle = (clipTop + clipBottom) / 2;

        mClipRectMiddle = new RectF();
        mClipRectMiddle.left = clipLeft;
        mClipRectMiddle.right = clipRight;
        mClipRectMiddle.top = clipVMiddle - mItemHeight / 2;
        mClipRectMiddle.bottom = clipVMiddle + mItemHeight / 2;

        mClipRectTop = new Rect();
        mClipRectTop.left = clipLeft;
        mClipRectTop.right = clipRight;
        mClipRectTop.top = clipTop;
        mClipRectTop.bottom = clipVMiddle - mItemHeight / 2;

        mClipRectBottom = new Rect();
        mClipRectBottom.left = clipLeft;
        mClipRectBottom.right = clipRight;
        mClipRectBottom.top = clipVMiddle + mItemHeight / 2;
        mClipRectBottom.bottom = clipBottom;
    }

    protected int $dp(int resId) {
        return getResources().getDimensionPixelOffset(resId);
    }

    protected int $sp(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    protected int $color(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * @return 控件的预算宽度
     */
    public int getPrefWidth() {
        int paddingHorizontal = getPaddingLeft() + getPaddingRight();
        return paddingHorizontal + mItemWidth;
    }

    /**
     * @return 控件的预算高度
     */
    public int getPrefHeight() {
        int paddingVertical = getPaddingTop() + getPaddingBottom();
        return paddingVertical + mItemHeight * mItemCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawHighlight(canvas);
        drawItems(canvas);
        drawDivider(canvas);
    }

    /**
     * 绘制Item
     *
     * @param canvas 画布
     */
    private void drawItems(Canvas canvas) {
        final int index = mScroller.getItemIndex();
        final int offset = mScroller.getItemOffset();
        final int hf = (mItemCount + 1) / 2;
        final int minIdx, maxIdx;
        if (offset < 0) {
            minIdx = index - hf - 1;
            maxIdx = index + hf;
        } else if (offset > 0) {
            minIdx = index - hf;
            maxIdx = index + hf + 1;
        } else {
            minIdx = index - hf;
            maxIdx = index + hf;
        }
        for (int i = minIdx; i < maxIdx; i++) {
            drawItem(canvas, i, offset);
        }
    }

    /**
     * 绘制Item
     *
     * @param canvas 画布
     * @param index  下标
     * @param offset 位移
     */
    protected void drawItem(Canvas canvas, int index, int offset) {
        CharSequence text = getCharSequence(index);
        if (text == null) {
            return;
        }
        final float centerX = mClipRectMiddle.centerX() + textMarginLeft - textMarginRight;
        final float centerY = mClipRectMiddle.centerY();
        // 和中间选项的距离
        final int range = (index - mScroller.getItemIndex()) * mItemHeight - offset;

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        int baseline = (int) ((fontMetrics.top + fontMetrics.bottom) / 2);

        // 绘制与下分界线相交的文字
        if (range > 0 && range < mItemHeight) {
            canvas.save();
            canvas.clipRect(mClipRectMiddle);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline, mSelectedTextPaint);
            canvas.restore();

            canvas.save();
            canvas.clipRect(mClipRectBottom);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline, mTextPaint);
            canvas.restore();
        }
        // 绘制下分界线下方的文字
        else if (range >= mItemHeight) {
            canvas.save();
            canvas.clipRect(mClipRectBottom);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline, mTextPaint);
            canvas.restore();
        }
        // 绘制与上分界线相交的文字
        else if (range < 0 && range > -mItemHeight) {
            canvas.save();
            canvas.clipRect(mClipRectMiddle);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline, mSelectedTextPaint);
            canvas.restore();

            canvas.save();
            canvas.clipRect(mClipRectTop);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline, mTextPaint);
            canvas.restore();
        }
        // 绘制上分界线上方的文字
        else if (range <= -mItemHeight) {
            canvas.save();
            canvas.clipRect(mClipRectTop);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline, mTextPaint);
            canvas.restore();
        }
        // 绘制两条分界线之间的文字
        else {
            canvas.save();
            canvas.clipRect(mClipRectMiddle);
            canvas.drawText(text, 0, text.length(), centerX, centerY + range - baseline, mSelectedTextPaint);
            canvas.restore();
        }
    }

    /**
     * 获取文字
     *
     * @param index 下标
     * @return
     */
    protected CharSequence getCharSequence(int index) {
        int size = dataSource.size();
        if (size == 0) return null;
        CharSequence text = null;
        if (isCyclic()) {
            int i = index % size;
            if (i < 0) {
                i += size;
            }
            text = dataSource.get(i);
        } else {
            if (index >= 0 && index < size) {
                text = dataSource.get(index);
            }
        }
        return text;
    }

    /**
     * 绘制高亮
     *
     * @param canvas
     */
    private void drawHighlight(Canvas canvas) {
        Path path = new Path();
        if (mHighlightRadius != 0) {
            mHighlightLeftTopRadius = mHighlightLeftBottomRadius = mHighlightRightTopRadius = mHighlightRightBottomRadius = mHighlightRightTopRadius;
        }
        float[] radii = {mHighlightLeftTopRadius, mHighlightLeftTopRadius, mHighlightRightTopRadius, mHighlightRightTopRadius, mHighlightRightBottomRadius, mHighlightRightBottomRadius, mHighlightLeftBottomRadius, mHighlightLeftBottomRadius};
        path.addRoundRect(mClipRectMiddle, radii, Path.Direction.CW);
        canvas.drawPath(path, mHighlightPaint);
//        canvas.drawRoundRect(mClipRectMiddle, mHighlightRadius, mHighlightRadius, mHighlightPaint);
    }

    /**
     * 绘制分割线
     *
     * @param canvas
     */
    private void drawDivider(Canvas canvas) {
        // 绘制上层分割线
        canvas.drawLine(mClipRectMiddle.left, mClipRectMiddle.top, mClipRectMiddle.right, mClipRectMiddle.top, mDividerPaint);
        // 绘制下层分割线
        canvas.drawLine(mClipRectMiddle.left, mClipRectMiddle.bottom, mClipRectMiddle.right, mClipRectMiddle.bottom, mDividerPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScroller.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        mScroller.computeScroll();
    }

    /**
     * @return 是否循环
     */
    public boolean isCyclic() {
        return mCyclic;
    }

    /**
     * 设置是否循环
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        mCyclic = cyclic;
        mScroller.reset();
        invalidate();
    }

    /**
     * @return 文字大小
     */
    public float getTextSize() {
        return mTextPaint.getTextSize();
    }

    /**
     * 设置文字大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        mTextPaint.setTextSize(textSize);
        mSelectedTextPaint.setTextSize(textSize);
        invalidate();
    }

    /**
     * @return 文字颜色
     */
    public int getTextColor() {
        return mTextPaint.getColor();
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    /**
     * @return 选择文字颜色
     */
    public int getSelectedTextColor() {
        return mSelectedTextPaint.getColor();
    }

    /**
     * 设置选择文字颜色
     *
     * @param color 颜色
     */
    public void setSelectedTextColor(int color) {
        mSelectedTextPaint.setColor(color);
        invalidate();
    }

    /**
     * @return item个数
     */
    public int getItemSize() {
        return dataSource == null ? 0 : dataSource.size();
    }

    /**
     * @param index 下标
     * @return 获取Item
     */
    public CharSequence getItem(int index) {
        if (index < 0 && index >= dataSource.size()) {
            return null;
        }
        return dataSource.get(index);
    }

    /**
     * @return 获取当前item
     */
    public CharSequence getCurrentItem() {
        return getItem(getCurrentIndex());
    }

    /**
     * @return 当前下标
     */
    public int getCurrentIndex() {
        return mScroller.getCurrentIndex();
    }

    /**
     * 设置当前下标
     *
     * @param index 位置
     */
    public void setCurrentIndex(int index) {
        setCurrentIndex(index, false);
    }

    /**
     * 设置当前下标
     *
     * @param index    位置
     * @param animated 是否动画
     */
    public void setCurrentIndex(int index, boolean animated) {
        mScroller.setCurrentIndex(index, animated);
    }

    /**
     * @return 分割线颜色
     */
    public int getDividerColor() {
        return dividerColor;
    }

    /**
     * 设置分割线颜色
     *
     * @param dividerColor 颜色
     */
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        mDividerPaint.setColor(dividerColor);
        invalidate();
    }

    /**
     * @return 分割线高度
     */
    public int getDividerHeight() {
        return dividerHeight;
    }

    /**
     * 设置分割线高度
     *
     * @param dividerHeight 分割线高度
     */
    public void setDividerHeight(int dividerHeight) {
        this.dividerHeight = dividerHeight;
        mDividerPaint.setStrokeWidth(dividerHeight);
        invalidate();
    }

    /**
     * @return 中间背景颜色
     */
    public int getHighlightColor() {
        return highlightColor;
    }

    /**
     * 设置中间背景颜色
     *
     * @param highlightColor 背景颜色
     */
    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
        mHighlightPaint.setColor(highlightColor);
        invalidate();
    }

    /**
     * @return item高度
     */
    public int getItemHeight() {
        return mItemHeight;
    }

    /**
     * 设置item高度
     *
     * @param height item高度
     */
    public void setItemHeight(int height) {
        this.mItemHeight = height;
        updateClipRect();
        invalidate();
    }

    /**
     * @return item宽度
     */
    public int getItemWidth() {
        return mItemWidth;
    }

    /**
     * 设置item宽度
     *
     * @param width item宽度
     */
    public void setItemWidth(int width) {
        this.mItemWidth = width;
        updateClipRect();
        invalidate();
    }

    /**
     * 设置数据源
     *
     * @param charSequences 数据源
     */
    public void setDataSource(CharSequence... charSequences) {
        dataSource.clear();
        if (charSequences != null && charSequences.length > 0) {
            Collections.addAll(dataSource, charSequences);
        }
        mScroller.reset();
        invalidate();
    }

    /**
     * 设置数据源
     *
     * @param collection 数据源
     */
    public void setDataSource(Collection<? extends CharSequence> collection) {
        dataSource.clear();
        if (collection != null && collection.size() > 0) {
            dataSource.addAll(collection);
        }
        mScroller.reset();
        invalidate();
    }

    /**
     * @return 滚轮滑动监听
     */
    public OnWheelChangedListener getOnWheelChangedListener() {
        return mScroller.onWheelChangedListener;
    }

    /**
     * 设置滚轮滑动监听
     *
     * @param onWheelChangedListener 滚轮滑动监听
     */
    public void setOnWheelChangedListener(OnWheelChangedListener onWheelChangedListener) {
        mScroller.onWheelChangedListener = onWheelChangedListener;
    }
}
