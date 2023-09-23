package androidx.ui.picker;

public interface OnPeriodPickerConfirmListener {

    /**
     * 时段选择确认
     *
     * @param picker 选择器
     * @param start  开始
     * @param end    结束
     */
    void onPeriodPickerConfirm(PeriodPicker picker, String start, String end);

}
