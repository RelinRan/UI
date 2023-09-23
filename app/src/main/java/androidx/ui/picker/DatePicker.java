package androidx.ui.picker;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.ui.R;
import androidx.ui.text.Time;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期选择器
 */
public class DatePicker extends WheelPicker {

    public static final String TAG = DatePicker.class.getSimpleName();
    public static final String UNIT[] = {"年", "月", "日", "时", "分", "秒"};
    /**
     * 界限 - 开始时间
     */
    private long start;
    /**
     * 界限 - 结束时间
     */
    private long end;
    /**
     * 选中时间
     */
    private long selected;
    /**
     * 确认监听
     */
    private OnDatePickerConfirmListener onDatePickerConfirmListener;

    public DatePicker(Context context) {
        super(context, 6);
        setTitle(getContext().getString(R.string.date_pick_title));
        //初始化单位
        for (int i = 0; i < 6; i++) {
            setUnit(i, UNIT[i]);
        }
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        setSelected(time);
        calendar.add(Calendar.YEAR, 80);
        setBoundary(time, calendar.getTimeInMillis());
    }

    /**
     * @return 界限开始
     */
    public long getBoundaryStart() {
        return start;
    }

    /**
     * @return 界限结束
     */
    public long getBoundaryEnd() {
        return end;
    }

    /**
     * 设置界限时间
     *
     * @param start 开始
     * @param end   结束
     */
    public void setBoundary(long start, long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * 设置日期界限
     *
     * @param start   开始
     * @param end     结束
     * @param pattern
     */
    public void setBoundary(String start, String end, String pattern) {
        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
            Log.e(TAG, "setBoundary start or end is empty.");
            return;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            long startTime = dateFormat.parse(start).getTime();
            long endTime = dateFormat.parse(end).getTime();
            setBoundary(startTime, endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置时间
     *
     * @param millis 毫秒
     */
    public void setSelected(long millis) {
        selected = millis;
        if (selected > end) {
            Log.e(TAG, "selected time > end time.");
        }
        if (selected < start) {
            Log.e(TAG, "selected time < start time.");
        }
    }

    /**
     * 设置时间
     *
     * @param text    时间文字
     * @param pattern 时间格式
     */
    public void setSelected(String text, String pattern) {
        try {
            setSelected(new SimpleDateFormat(pattern).parse(text).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 选中毫秒单位时间
     */
    public long getSelectedTimeInMillis() {
        return selected;
    }

    /**
     * @param pattern 时间格式
     * @return 格式化选中时间
     */
    public String getSelectedFormatDate(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(selected));
    }

    /**
     * 设置日期选择器年份
     *
     * @param startYear    开始年份
     * @param endYear      结束年份
     * @param selectedYear 选择年份
     */
    protected void setDatePickYear(int startYear, int endYear, int selectedYear) {
        List<String> years = new ArrayList<>();
        int yearIndex = 0;
        String unit = getUnit(0);
        for (int i = startYear; i <= endYear; i++) {
            if (i == selectedYear) {
                yearIndex = i - startYear;
            }
            years.add(i + unit);
        }
        getWheel(0).setDataSource(years);
        getWheel(0).setCurrentIndex(yearIndex);
        getWheel(0).setOnWheelChangedListener((view, oldIndex, newIndex) -> {
            int year = Integer.parseInt(trimUnit(unit, view.getCurrentItem().toString()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(start);
            int startMonth = calendar.get(Calendar.MONTH) + 1;
            calendar.setTimeInMillis(end);
            int endMonth = calendar.get(Calendar.MONTH) + 1;
            calendar.setTimeInMillis(selected);
            int selectedMonth = calendar.get(Calendar.MONTH) + 1;
            calendar.set(Calendar.YEAR, year);
            setSelected(calendar.getTimeInMillis());
            setDatePickMonth(startYear, endYear, year, startMonth, endMonth, selectedMonth);
        });
    }

    /**
     * 设置日期选择器月份
     *
     * @param startYear     开始年份
     * @param endYear       结束年份
     * @param selectedYear  选择年份
     * @param startMonth    开始年份
     * @param endMonth      结束年份
     * @param selectedMonth 选择月份
     */
    protected void setDatePickMonth(int startYear, int endYear, int selectedYear,
                                    int startMonth, int endMonth, int selectedMonth) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String unit = getUnit(1);
        List<String> months = new ArrayList<>();
        int monthIndex = 0;
        //选择年份和开始年份相同
        if (selectedYear == startYear) {
            for (int i = startMonth; i < 13; i++) {
                if (i == selectedMonth) {
                    monthIndex = i - startMonth;
                }
                months.add(decimalFormat.format(i) + unit);
            }
        } else if (selectedYear == endYear) {
            //选择年份和结束年份相同
            for (int i = 1; i <= endMonth; i++) {
                if (i == selectedMonth) {
                    monthIndex = i - 1;
                }
                months.add(decimalFormat.format(i) + unit);
            }
        } else {
            for (int i = 1; i < 13; i++) {
                if (i == selectedMonth) {
                    monthIndex = i - 1;
                }
                months.add(decimalFormat.format(i) + unit);
            }
        }
        getWheel(1).setDataSource(months);
        getWheel(1).setOnWheelChangedListener(null);
        getWheel(1).setCurrentIndex(monthIndex);
        getWheel(1).setOnWheelChangedListener((view, oldIndex, newIndex) -> {
            int month = Integer.parseInt(trimUnit(unit, view.getCurrentItem().toString()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(start);
            int startDay = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.setTimeInMillis(end);
            int endDay = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.setTimeInMillis(selected);
            int selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.MONTH, month - 1);
            setSelected(calendar.getTimeInMillis());
            setDatePickerDay(startYear, endYear, selectedYear, startMonth, endMonth, month, startDay, endDay, selectedDay);
        });
    }

    /**
     * 设置日期选择器天
     *
     * @param startYear     开始年份
     * @param endYear       结束年份
     * @param selectedYear  选择年份
     * @param startMonth    开始年份
     * @param endMonth      结束年份
     * @param selectedMonth 选择月份
     * @param startDay      开始日期
     * @param endDay        结束日期
     * @param selectedDay   选择日期
     */
    protected void setDatePickerDay(int startYear, int endYear, int selectedYear,
                                    int startMonth, int endMonth, int selectedMonth,
                                    int startDay, int endDay, int selectedDay
    ) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String unit = getUnit(2);
        List<String> days = new ArrayList<>();
        int dayIndex = 0;
        if (selectedYear == startYear && selectedMonth == startMonth) {
            for (int i = startDay; i <= Time.days(selectedYear, selectedMonth); i++) {
                if (i == selectedDay) {
                    dayIndex = i - startDay;
                }
                days.add(decimalFormat.format(i) + unit);
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth) {
            for (int i = 1; i <= endDay; i++) {
                if (i == selectedDay) {
                    dayIndex = i - 1;
                }
                days.add(decimalFormat.format(i) + unit);
            }
        } else {
            for (int i = 1; i <= Time.days(selectedYear, selectedMonth); i++) {
                if (i == selectedDay) {
                    dayIndex = i - 1;
                }
                days.add(decimalFormat.format(i) + unit);
            }
        }
        getWheel(2).setDataSource(days);
        getWheel(2).setOnWheelChangedListener(null);
        getWheel(2).setCurrentIndex(dayIndex);
        getWheel(2).setOnWheelChangedListener((view, oldIndex, newIndex) -> {
            int day = Integer.parseInt(trimUnit(unit, view.getCurrentItem().toString()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(start);
            int startHour = calendar.get(Calendar.HOUR_OF_DAY);
            calendar.setTimeInMillis(end);
            int endHour = calendar.get(Calendar.HOUR_OF_DAY);
            calendar.setTimeInMillis(selected);
            int selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
            int month = calendar.get(Calendar.MONTH) + 1;
            calendar.set(Calendar.DAY_OF_MONTH, day);
            setSelected(calendar.getTimeInMillis());
            setDatePickerHour(startYear, endYear, selectedYear, startMonth, endMonth, month, startDay, endDay, day, startHour, endHour, selectedHour);
        });
    }

    /**
     * 设置日期选择器小时
     *
     * @param startYear     开始年份
     * @param endYear       结束年份
     * @param selectedYear  选择年份
     * @param startMonth    开始年份
     * @param endMonth      结束年份
     * @param selectedMonth 选择月份
     * @param startDay      开始日期
     * @param endDay        开始日期
     * @param selectedDay   选择日期
     * @param startHour     开始小时
     * @param endHour       结束小时
     * @param selectedHour  选择小时
     */
    protected void setDatePickerHour(int startYear, int endYear, int selectedYear,
                                     int startMonth, int endMonth, int selectedMonth,
                                     int startDay, int endDay, int selectedDay,
                                     int startHour, int endHour, int selectedHour
    ) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String unit = getUnit(3);
        List<String> hours = new ArrayList<>();
        int hourIndex = 0;
        if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
            for (int i = startHour; i <= 23; i++) {
                if (i == selectedHour) {
                    hourIndex = i - startHour;
                }
                hours.add(decimalFormat.format(i) + unit);
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
            for (int i = 0; i <= endHour; i++) {
                if (i == selectedHour) {
                    hourIndex = i;
                }
                hours.add(decimalFormat.format(i) + unit);
            }
        } else {
            for (int i = 0; i <= 23; i++) {
                if (i == selectedHour) {
                    hourIndex = i;
                }
                hours.add(decimalFormat.format(i) + unit);
            }
        }
        getWheel(3).setDataSource(hours);
        getWheel(3).setOnWheelChangedListener(null);
        getWheel(3).setCurrentIndex(hourIndex);
        getWheel(3).setOnWheelChangedListener((view, oldIndex, newIndex) -> {
            int hour = Integer.parseInt(trimUnit(unit, view.getCurrentItem().toString()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(start);
            int startMinute = calendar.get(Calendar.MINUTE);
            calendar.setTimeInMillis(end);
            int endMinute = calendar.get(Calendar.MINUTE);
            calendar.setTimeInMillis(selected);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int selectedMinute = calendar.get(Calendar.MINUTE);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            setSelected(calendar.getTimeInMillis());
            setDatePickerMinute(startYear, endYear, selectedYear, startMonth, endMonth, selectedMonth, startDay, endDay, day, startHour, endHour, hour, startMinute, endMinute, selectedMinute);
        });
    }

    /**
     * 设置日期选择器小时
     *
     * @param startYear      开始年份
     * @param endYear        结束年份
     * @param selectedYear   选择年份
     * @param startMonth     开始年份
     * @param endMonth       结束年份
     * @param selectedMonth  选择月份
     * @param startDay       开始日期
     * @param endDay         开始日期
     * @param selectedDay    选择日期
     * @param startHour      开始小时
     * @param endHour        结束小时
     * @param selectedHour   选择小时
     * @param startMinute    开始分钟
     * @param endMinute      结束分钟
     * @param selectedMinute 选择分钟
     */
    protected void setDatePickerMinute(int startYear, int endYear, int selectedYear,
                                       int startMonth, int endMonth, int selectedMonth,
                                       int startDay, int endDay, int selectedDay,
                                       int startHour, int endHour, int selectedHour,
                                       int startMinute, int endMinute, int selectedMinute
    ) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String unit = getUnit(4);
        List<String> minutes = new ArrayList<>();
        int minuteIndex = 0;
        if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
            for (int i = startMinute; i <= 59; i++) {
                if (i == selectedMinute) {
                    minuteIndex = i - startMinute;
                }
                minutes.add(decimalFormat.format(i) + unit);
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
            for (int i = 0; i <= endMinute; i++) {
                if (i == selectedMinute) {
                    minuteIndex = i;
                }
                minutes.add(decimalFormat.format(i) + unit);
            }
        } else {
            for (int i = 0; i <= 59; i++) {
                if (i == selectedMinute) {
                    minuteIndex = i;
                }
                minutes.add(decimalFormat.format(i) + unit);
            }
        }
        getWheel(4).setDataSource(minutes);
        getWheel(4).setCurrentIndex(minuteIndex);
        getWheel(4).setOnWheelChangedListener((view, oldIndex, newIndex) -> {
            int minute = Integer.parseInt(trimUnit(unit, view.getCurrentItem().toString()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(start);
            int startSecond = calendar.get(Calendar.SECOND);
            calendar.setTimeInMillis(end);
            int endSecond = calendar.get(Calendar.SECOND);
            calendar.setTimeInMillis(selected);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int selectedSecond = calendar.get(Calendar.SECOND);
            calendar.set(Calendar.MINUTE, minute);
            setSelected(calendar.getTimeInMillis());
            setDatePickerSecond(startYear, endYear, year, startMonth, endMonth, month, startDay, endDay, day, startHour, endHour, hour, startMinute, endMinute, minute, startSecond, endSecond, selectedSecond);
        });
    }

    /**
     * 设置日期选择器小时
     *
     * @param startYear      开始年份
     * @param endYear        结束年份
     * @param selectedYear   选择年份
     * @param startMonth     开始年份
     * @param endMonth       结束年份
     * @param startDay       开始日期
     * @param selectedDay    选择日期
     * @param startHour      开始小时
     * @param endHour        结束小时
     * @param selectedHour   选择小时
     * @param startMinute    开始分钟
     * @param endMinute      结束分钟
     * @param selectedMinute 选择分钟
     * @param startSecond    开始秒数
     * @param endSecond      结束秒数
     * @param selectedSecond 选择秒数
     */
    protected void setDatePickerSecond(int startYear, int endYear, int selectedYear,
                                       int startMonth, int endMonth, int selectedMonth,
                                       int startDay, int endDay, int selectedDay,
                                       int startHour, int endHour, int selectedHour,
                                       int startMinute, int endMinute, int selectedMinute,
                                       int startSecond, int endSecond, int selectedSecond
    ) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String unit = getUnit(5);
        List<String> seconds = new ArrayList<>();
        int secondIndex = 0;
        if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour && selectedMinute == startMinute) {
            for (int i = startSecond; i <= 59; i++) {
                if (i == selectedSecond) {
                    secondIndex = i - startSecond;
                }
                seconds.add(decimalFormat.format(i) + unit);
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour && selectedMinute == endMinute) {
            for (int i = 0; i <= endSecond; i++) {
                if (i == selectedSecond) {
                    secondIndex = i;
                }
                seconds.add(decimalFormat.format(i) + unit);
            }
        } else {
            for (int i = 0; i <= 59; i++) {
                if (i == selectedSecond) {
                    secondIndex = i;
                }
                seconds.add(decimalFormat.format(i) + unit);
            }
        }
        getWheel(5).setDataSource(seconds);
        getWheel(5).setCurrentIndex(secondIndex);
    }

    /**
     * 通知数据已改变
     */
    public void notifyDataSetChanged() {
        setDatePickDataSource(start, end, selected);
    }

    /**
     * 设置时间选择器数据源
     *
     * @param start
     * @param end
     * @param selected
     */
    protected void setDatePickDataSource(long start, long end, long selected) {
        Calendar calendar = Calendar.getInstance();
        //=========================[开始]=========================
        calendar.setTimeInMillis(start);
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH) + 1;
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        int startHour = calendar.get(Calendar.HOUR_OF_DAY);
        int startMinute = calendar.get(Calendar.MINUTE);
        int startSecond = calendar.get(Calendar.SECOND);
        Log.i(TAG, "start time " + startYear + "-" + startMonth + "-" + startDay + " " + startHour + ":" + startMinute + ":" + startSecond);
        //=========================[结束]=========================
        calendar.setTimeInMillis(end);
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);
        int endHour = calendar.get(Calendar.HOUR_OF_DAY);
        int endMinute = calendar.get(Calendar.MINUTE);
        int endSecond = calendar.get(Calendar.SECOND);
        Log.i(TAG, "end time " + endYear + "-" + endMonth + "-" + endDay + " " + endHour + ":" + endMinute + ":" + endSecond);
        //=========================[选择]=========================
        calendar.setTimeInMillis(selected);
        int selectedYear = calendar.get(Calendar.YEAR);
        int selectedMonth = calendar.get(Calendar.MONTH) + 1;
        int selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        int selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        int selectedMinute = calendar.get(Calendar.MINUTE);
        int selectedSecond = calendar.get(Calendar.SECOND);
        Log.i(TAG, "selected time " + selectedYear + "-" + selectedMonth + "-" + selectedDay + " " + selectedHour + ":" + selectedMinute + ":" + selectedSecond);
        //=========================[年份]=========================
        setDatePickYear(startYear, endYear, selectedYear);
        //=========================[月份]=========================
        setDatePickMonth(startYear, endYear, selectedYear, startMonth, endMonth, selectedMonth);
        //=========================[日期]=========================
        setDatePickerDay(startYear, endYear, selectedYear, startMonth, endMonth, selectedMonth, startDay, endDay, selectedDay);
        //=========================[小时]=========================
        setDatePickerHour(startYear, endYear, selectedYear, startMonth, endMonth, selectedMonth, startDay, endDay, selectedDay, startHour, endHour, selectedHour);
        //=========================[分钟]=========================
        setDatePickerMinute(startYear, endYear, selectedYear, startMonth, endMonth, selectedMonth, startDay, endDay, selectedDay, startHour, endHour, selectedHour, startMinute, endMinute, selectedMinute);
        //=========================[秒数]=========================
        setDatePickerSecond(startYear, endYear, selectedYear, startMonth, endMonth, selectedMonth, startDay, endDay, selectedDay, startHour, endHour, selectedHour, startMinute, endMinute, selectedMinute, startSecond, endSecond, selectedSecond);
    }

    @Override
    public void show() {
        notifyDataSetChanged();
        super.show();
    }

    /**
     * 设置日期选择监听
     *
     * @param onDatePickerConfirmListener 日期选择监听
     */
    public void setOnDatePickerConfirmListener(OnDatePickerConfirmListener onDatePickerConfirmListener) {
        this.onDatePickerConfirmListener = onDatePickerConfirmListener;
    }

    @Override
    protected void onConfirm() {
        super.onConfirm();
        if (onDatePickerConfirmListener != null) {
            onDatePickerConfirmListener.onDatePickerConfirm(this, getSelected(true));
        }
    }

}
