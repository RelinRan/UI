package androidx.ui.picker;


import androidx.ui.wheel.WheelView;

public interface OnWheelPickerChangedListener {

    /**
     * 滚轮选择器改变监听
     *
     * @param picker 选择器
     * @param wheel  滚轮View
     */
    void onWheelPickerChanged(WheelPicker picker, WheelView wheel);

}
