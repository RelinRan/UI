package androidx.ui.picker;

public interface OnDatePickerConfirmListener {

    /**
     * 日期选择器确认
     *
     * @param picker 选择器
     * @param date   日期字符
     */
    void onDatePickerConfirm(DatePicker picker, String date);

}
