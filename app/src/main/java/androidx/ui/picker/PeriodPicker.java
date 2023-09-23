package androidx.ui.picker;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.ui.R;
import androidx.ui.wheel.WheelView;

/**
 * 时段选择器
 */
public class PeriodPicker extends DatePicker implements OnWheelPickerChangedListener, CompoundButton.OnCheckedChangeListener {

    private RadioGroup period_group;
    private RadioButton rbt_start;
    private TextView tv_separation;
    private RadioButton rbt_end;
    private long boundaryStart;
    private long boundaryEnd;
    private long startTime;
    private OnPeriodPickerConfirmListener onPeriodPickerConfirmListener;

    public PeriodPicker(Context context) {
        super(context);
        setOnWheelPickerChangedListener(this);
        setTitle(getContext().getString(R.string.date_pick_title));
    }

    @Override
    public int getHeaderLayoutResId() {
        return R.layout.ui_header_period;
    }

    @Override
    protected void onCreateHeader(ViewGroup parent) {
        super.onCreateHeader(parent);
        period_group = parent.findViewById(R.id.period_group);
        rbt_start = parent.findViewById(R.id.rbt_start);
        tv_separation = parent.findViewById(R.id.tv_separation);
        rbt_end = parent.findViewById(R.id.rbt_end);
        rbt_start.setOnCheckedChangeListener(this);
        rbt_end.setOnCheckedChangeListener(this);
    }

    @Override
    public void onWheelPickerChanged(WheelPicker picker, WheelView wheel) {
        if (picker.getCurrentIndex() == picker.getCount() - 1) {
            String data = getSelected(true);
            if (rbt_start.isChecked()) {
                rbt_start.setText(data);
                startTime = getSelectedTimeInMillis();
                if (rbt_end.getText().toString().length() == 0) {
                    boundaryStart = getBoundaryStart();
                    boundaryEnd = getBoundaryEnd();
                    rbt_end.setText(data);
                }
            }
            if (rbt_end.isChecked()) {
                rbt_end.setText(data);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.rbt_start && isChecked) {
            setBoundary(boundaryStart, boundaryEnd);
        }
        if (buttonView.getId() == R.id.rbt_end && isChecked) {
            setBoundary(startTime, boundaryEnd);
        }
        notifyDataSetChanged();
    }

    /**
     * @return 时段组View
     */
    public RadioGroup getPeriodGroup() {
        return period_group;
    }

    /**
     * @return 分割View
     */
    public TextView getSeparation() {
        return tv_separation;
    }

    /**
     * @return 选择开始
     */
    public RadioButton getStartRadio() {
        return rbt_start;
    }

    /**
     * @return 选择结束
     */
    public RadioButton getEndRadio() {
        return rbt_end;
    }

    /**
     * 设置时段确认监听
     *
     * @param onPeriodPickerConfirmListener 时段确认监听
     */
    public void setOnPeriodPickerConfirmListener(OnPeriodPickerConfirmListener onPeriodPickerConfirmListener) {
        this.onPeriodPickerConfirmListener = onPeriodPickerConfirmListener;
    }

    @Override
    protected void onConfirm() {
        super.onConfirm();
        if (onPeriodPickerConfirmListener != null) {
            String start = rbt_start.getText().toString();
            String end = rbt_end.getText().toString();
            onPeriodPickerConfirmListener.onPeriodPickerConfirm(this, start, end);
        }
    }

}
