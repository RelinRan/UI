package androidx.ui.wheel;

public interface OnWheelChangedListener {

    /**
     * 滚轮改变监听
     *
     * @param view     滚轮
     * @param oldIndex 旧位置
     * @param newIndex 新位置
     */
    void onChanged(WheelView view, int oldIndex, int newIndex);

}
