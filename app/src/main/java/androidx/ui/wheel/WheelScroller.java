package androidx.ui.wheel;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.Scroller;

/**
 * 滚轮滑动逻辑处理
 */
public class WheelScroller extends Scroller {

    /**
     * 滚轮动画时间
     */
    public static final int JUSTIFY_DURATION = 400;
    /**
     * 滚动偏移量
     */
    private int mScrollOffset;
    private float lastTouchY;
    private boolean isScrolling;
    protected final WheelView mWheelView;
    private VelocityTracker mVelocityTracker;
    protected OnWheelChangedListener onWheelChangedListener;

    /**
     * 构造滑动器
     *
     * @param context   上下文
     * @param wheelView 滚轮
     */
    public WheelScroller(Context context, WheelView wheelView) {
        super(context);
        mWheelView = wheelView;
    }

    /**
     * 计算滑动速度
     */
    public void computeScroll() {
        if (isScrolling) {
            isScrolling = computeScrollOffset();
            doScroll(getCurrY() - mScrollOffset);
            if (isScrolling) {
                mWheelView.postInvalidate();
            } else {
                // 滚动结束后，重新调整位置
                justify();
            }
        }
    }

    private int currentIndex = -1;

    /**
     * 处理滑动
     *
     * @param distance 滑动距离
     */
    private void doScroll(int distance) {
        mScrollOffset += distance;
        if (!mWheelView.isCyclic()) {
            // 限制滚动边界
            final int maxOffset = (mWheelView.getItemSize() - 1) * mWheelView.mItemHeight;
            if (mScrollOffset < 0) {
                mScrollOffset = 0;
            } else if (mScrollOffset > maxOffset) {
                mScrollOffset = maxOffset;
            }
        }
        notifyWheelChangedListener();
    }

    /**
     * 通知滚轮改变监听
     */
    protected void notifyWheelChangedListener() {
        int oldValue = currentIndex;
        int newValue = getCurrentIndex();
        if (oldValue != newValue) {
            currentIndex = newValue;
            if (onWheelChangedListener != null) {
                onWheelChangedListener.onChanged(mWheelView, oldValue, newValue);
            }
        }
    }

    /**
     * @return 当前位置
     */
    public int getCurrentIndex() {
        final int itemHeight = mWheelView.mItemHeight;
        final int itemSize = mWheelView.getItemSize();
        if (itemSize == 0) return -1;

        int itemIndex;
        if (mScrollOffset < 0) {
            itemIndex = (mScrollOffset - itemHeight / 2) / itemHeight;
        } else {
            itemIndex = (mScrollOffset + itemHeight / 2) / itemHeight;
        }
        int currentIndex = itemIndex % itemSize;
        if (currentIndex < 0) {
            currentIndex += itemSize;
        }
        return currentIndex;
    }

    /**
     * 设置当前位置
     *
     * @param index    位置
     * @param animated 是否采用动画
     */
    public void setCurrentIndex(int index, boolean animated) {
        int position = index * mWheelView.mItemHeight;
        int distance = position - mScrollOffset;
        if (distance == 0) return;
        if (animated) {
            isScrolling = true;
            startScroll(0, mScrollOffset, 0, distance, JUSTIFY_DURATION);
            mWheelView.invalidate();
        } else {
            doScroll(distance);
            mWheelView.invalidate();
        }
    }

    /**
     * @return item位置
     */
    public int getItemIndex() {
        return mWheelView.mItemHeight == 0 ? 0 : mScrollOffset / mWheelView.mItemHeight;
    }

    /**
     * @return item偏移量
     */
    public int getItemOffset() {
        return mWheelView.mItemHeight == 0 ? 0 : mScrollOffset % mWheelView.mItemHeight;
    }

    /**
     * 重置
     */
    public void reset() {
        isScrolling = false;
        mScrollOffset = 0;
        currentIndex = -1;
        notifyWheelChangedListener();
        forceFinished(true);
    }

    /**
     * 当滚轮结束滑行后，调整滚轮的位置，需要调用该方法
     */
    protected void justify() {
        final int itemHeight = mWheelView.mItemHeight;
        final int offset = mScrollOffset % itemHeight;
        if (offset > 0 && offset < itemHeight / 2) {
            isScrolling = true;
            startScroll(0, mScrollOffset, 0, -offset, JUSTIFY_DURATION);
            mWheelView.invalidate();
        } else if (offset >= itemHeight / 2) {
            isScrolling = true;
            startScroll(0, mScrollOffset, 0, itemHeight - offset, JUSTIFY_DURATION);
            mWheelView.invalidate();
        } else if (offset < 0 && offset > -itemHeight / 2) {
            isScrolling = true;
            startScroll(0, mScrollOffset, 0, -offset, JUSTIFY_DURATION);
            mWheelView.invalidate();
        } else if (offset <= -itemHeight / 2) {
            isScrolling = true;
            startScroll(0, mScrollOffset, 0, -itemHeight - offset, JUSTIFY_DURATION);
            mWheelView.invalidate();
        }
    }

    /**
     * 触摸事件处理
     *
     * @param event 事件
     * @return 是否处理当前事件
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchY = event.getY();
                forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float touchY = event.getY();
                int deltaY = (int) (touchY - lastTouchY);
                if (deltaY != 0) {
                    doScroll(-deltaY);
                    mWheelView.invalidate();
                }
                lastTouchY = touchY;
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocityY = mVelocityTracker.getYVelocity();

                if (Math.abs(velocityY) > 0) {
                    isScrolling = true;
                    fling(0, mScrollOffset, 0, (int) -velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    mWheelView.invalidate();
                } else {
                    justify();
                }
            case MotionEvent.ACTION_CANCEL:
                // 当触发抬起、取消事件后，回收VelocityTracker
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }
        return true;
    }

}
