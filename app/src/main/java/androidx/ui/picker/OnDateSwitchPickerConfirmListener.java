package androidx.ui.picker;

public interface OnDateSwitchPickerConfirmListener {

    /**
     * 日期切换选择确认
     *
     * @param picker 选择器
     * @param date   时间
     */
    void onDateSwitchPickerConfirm(DateSwitchPicker picker, String date);

}
