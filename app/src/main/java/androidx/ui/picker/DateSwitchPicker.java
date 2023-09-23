package androidx.ui.picker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.ui.R;

/**
 * 切换时间选择器
 */
public class DateSwitchPicker extends DatePicker implements CompoundButton.OnCheckedChangeListener {

    private RadioGroup switch_group;
    private RadioButton rbt_year;
    private RadioButton rbt_month;
    private RadioButton rbt_day;
    private OnDateSwitchPickerConfirmListener onDateSwitchPickerConfirmListener;

    public DateSwitchPicker(Context context) {
        super(context);
        setTitle(getContext().getString(R.string.date_pick_title));
    }

    @Override
    public int getHeaderLayoutResId() {
        return R.layout.ui_header_date_switch;
    }

    @Override
    protected void onCreateHeader(ViewGroup parent) {
        super.onCreateHeader(parent);
        switch_group = parent.findViewById(R.id.switch_group);
        rbt_year = parent.findViewById(R.id.rbt_year);
        rbt_month = parent.findViewById(R.id.rbt_month);
        rbt_day = parent.findViewById(R.id.rbt_day);
        rbt_year.setOnCheckedChangeListener(this);
        rbt_month.setOnCheckedChangeListener(this);
        rbt_day.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.rbt_year && isChecked) {
            setVisibility(0, View.VISIBLE);
            setVisibility(new int[]{1, 2, 3, 4, 5}, View.GONE);
        }
        if (id == R.id.rbt_month && isChecked) {
            setVisibility(new int[]{0, 1}, View.VISIBLE);
            setVisibility(new int[]{2, 3, 4, 5}, View.GONE);
        }
        if (id == R.id.rbt_day && isChecked) {
            setVisibility(new int[]{0, 1, 2}, View.VISIBLE);
            setVisibility(new int[]{3, 4, 5}, View.GONE);
        }
    }

    /**
     * @return 切换组View
     */
    public RadioGroup getSwitchGroup() {
        return switch_group;
    }

    /**
     * @return 年份切换View
     */
    public RadioButton getYearRadio() {
        return rbt_year;
    }

    /**
     * @return 月份切换View
     */
    public RadioButton getMonthRadio() {
        return rbt_month;
    }

    /**
     * @return 日切换View
     */
    public RadioButton getDayRadio() {
        return rbt_day;
    }

    /**
     * 设置日期切换选择确认监听
     *
     * @param onDateSwitchPickerConfirmListener 日期切换选择确认监听
     */
    public void setOnDateSwitchPickerConfirmListener(OnDateSwitchPickerConfirmListener onDateSwitchPickerConfirmListener) {
        this.onDateSwitchPickerConfirmListener = onDateSwitchPickerConfirmListener;
    }

    @Override
    protected void onConfirm() {
        super.onConfirm();
        if (onDateSwitchPickerConfirmListener != null) {
            onDateSwitchPickerConfirmListener.onDateSwitchPickerConfirm(this, getSelected(true));
        }
    }

}
