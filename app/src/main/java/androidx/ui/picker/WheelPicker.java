package androidx.ui.picker;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.ui.R;
import androidx.ui.app.AppDialog;
import androidx.ui.wheel.Wheel3DView;
import androidx.ui.wheel.WheelView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 滚轮选择器对话框
 */
public class WheelPicker extends AppDialog implements View.OnClickListener {

    private int count;
    private TextView tv_cancel;
    private TextView tv_title;
    private TextView tv_confirm;
    private FrameLayout header;
    private LinearLayout group;
    private FrameLayout footer;
    protected List<Wheel3DView> wheels;
    private Map<Integer, String> unitMap;
    private int currentIndex = 0;
    private OnWheelPickerChangedListener onWheelPickerChangedListener;
    private OnWheelPickerConfirmListener onWheelPickerConfirmListener;

    public WheelPicker(Context context, int count) {
        super(context);
        this.count = count;
        onCreateHeader(header);
        onCreateWheel(count);
        onCreateFooter(footer);
    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.ui_date_picker_dialog;
    }

    @Override
    public int getLayoutWidth() {
        return MATCH_PARENT;
    }

    @Override
    public int getLayoutHeight() {
        return WRAP_CONTENT;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int getWindowAnimationResId() {
        return WINDOW_ANIM_BOTTOM;
    }

    @Override
    public void onViewCreated(View contentView) {
        tv_cancel = contentView.findViewById(R.id.tv_cancel);
        tv_title = contentView.findViewById(R.id.tv_title);
        tv_confirm = contentView.findViewById(R.id.tv_confirm);
        header = contentView.findViewById(R.id.header);
        group = contentView.findViewById(R.id.group);
        footer = contentView.findViewById(R.id.footer);
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    /**
     * 获取滚轮个数
     *
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * 获取当前下标
     *
     * @return
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * 获取当前滚轮
     *
     * @return
     */
    public WheelView getCurrentWheel() {
        return getWheel(currentIndex);
    }

    /**
     * 获取取消文本对象
     *
     * @return
     */
    public TextView getCancel() {
        return tv_cancel;
    }

    /**
     * 获取标题文本对象
     *
     * @return
     */
    public TextView getTitle() {
        return tv_title;
    }

    /**
     * 获取确认文本对象
     *
     * @return
     */
    public TextView getConfirm() {
        return tv_title;
    }

    /**
     * 设置取消文字
     *
     * @param text 文字
     */
    public void setCancel(String text) {
        tv_cancel.setText(text);
    }

    /**
     * 设置标题文字
     *
     * @param text 文字
     */
    public void setTitle(String text) {
        tv_title.setText(text);
    }

    /**
     * 设置确认文字
     *
     * @param text 文字
     */
    public void setConfirm(String text) {
        tv_confirm.setText(text);
    }

    /**
     * @param unit  单位
     * @param value 文字
     * @return 无单位字符
     */
    public String trimUnit(String unit, String value) {
        return value.replace(unit, "");
    }

    /**
     * 设置日期可见性
     *
     * @param index      滚轮位置
     * @param visibility 可见性{@link android.view.View#VISIBLE } or {@link android.view.View#GONE }
     */
    public void setVisibility(int[] index, int visibility) {
        int wheelSize = getWheels().size();
        for (int i = 0; i < index.length; i++) {
            if (i < wheelSize) {
                getWheel(index[i]).setVisibility(visibility);
            }
        }
        int count = getGroup().getChildCount();
        int goneCount = 0;
        for (int i = 0; i < count; i++) {
            View view = getGroup().getChildAt(i);
            if (view.getVisibility() == View.GONE) {
                goneCount++;
            }
        }
        getGroup().setWeightSum(count - goneCount);
    }

    /**
     * 设置日期可见性
     *
     * @param index      滚轮位置
     * @param visibility 可见性{@link android.view.View#VISIBLE } or {@link android.view.View#GONE }
     */
    public void setVisibility(int index, int visibility) {
        if (index >= getWheels().size()) {
            return;
        }
        getWheel(index).setVisibility(visibility);
        int count = getGroup().getChildCount();
        int goneCount = 0;
        for (int i = 0; i < count; i++) {
            View view = getGroup().getChildAt(i);
            if (view.getVisibility() == View.GONE) {
                goneCount++;
            }
        }
        getGroup().setWeightSum(count - goneCount);
    }

    /**
     * @param index 滚轮下标
     * @return 滚轮可见性
     */
    public int getVisibility(int index) {
        if (index >= getWheels().size()) {
            return View.GONE;
        }
        return getWheel(index).getVisibility();
    }

    /**
     * 设置滚轮单位
     *
     * @param index 滚轮位置
     * @param name  单位名称
     */
    public void setUnit(int index, String name) {
        unitMap.put(index, name);
    }

    /**
     * @param index 滚轮位置
     * @return 滚轮单位
     */
    public String getUnit(int index) {
        return unitMap.get(index);
    }

    /**
     * @param trimUnit 是否去掉单位
     * @return 选中结果
     */
    public String getSelected(boolean trimUnit) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            if (getVisibility(i) == View.VISIBLE) {
                String text = getWheel(i).getCurrentItem().toString();
                text = trimUnit ? trimUnit(getUnit(i), text) : text;
                sb.append(text);
                if (i < 2) {
                    sb.append("-");
                } else if (i < 3) {
                    sb.append(" ");
                } else {
                    sb.append(":");
                }
            }
        }
        if (sb.length() > 0 && sb.toString().endsWith(":")) {
            sb.deleteCharAt(sb.lastIndexOf(":"));
        }
        if (sb.length() > 0 && sb.toString().endsWith("-")) {
            sb.deleteCharAt(sb.lastIndexOf("-"));
        }
        return sb.toString();
    }

    /**
     * 创建头部
     *
     * @param parent 父级
     */
    protected void onCreateHeader(ViewGroup parent) {
        int layout = getHeaderLayoutResId();
        if (layout != 0) {
            addHeader(LayoutInflater.from(getContext()).inflate(layout, parent, false));
        }
    }

    /**
     * 创建滚轮
     *
     * @param count 个数
     */
    public void onCreateWheel(int count) {
        unitMap = new HashMap<>();
        wheels = new ArrayList<>();
        group.setWeightSum(count);
        for (int i = 0; i < count; i++) {
            Wheel3DView view = new Wheel3DView(getContext());
            view.setTag(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, MATCH_PARENT);
            params.weight = 1;
            view.setLayoutParams(params);
            view.setOnWheelChangedListener((v, oldIndex, newIndex) -> {
                currentIndex = (int) v.getTag();
                if (onWheelPickerChangedListener != null) {
                    onWheelPickerChangedListener.onWheelPickerChanged(this, v);
                }
            });
            group.addView(view);
            wheels.add(view);
            unitMap.put(i, "");
        }
    }

    /**
     * 创建脚部
     *
     * @param parent 父级
     */
    protected void onCreateFooter(ViewGroup parent) {
        int layout = getFooterLayoutResId();
        if (layout != 0) {
            addFooter(LayoutInflater.from(getContext()).inflate(layout, parent, false));
        }
    }

    /**
     * @return 头部布局资源
     */
    public int getHeaderLayoutResId() {
        return 0;
    }

    /**
     * @return 滚轮控件头部
     */
    public FrameLayout getHeader() {
        return header;
    }

    /**
     * 添加头部
     *
     * @param view 头部View
     */
    public void addHeader(View view) {
        header.addView(view);
    }

    /**
     * 添加头部
     *
     * @param view   头部View
     * @param params 属性参数
     */
    public void addHeader(View view, ViewGroup.LayoutParams params) {
        header.addView(view, params);
    }

    /**
     * @return 头部布局资源
     */
    public int getFooterLayoutResId() {
        return 0;
    }

    /**
     * @return 滚轮控件脚部
     */
    public FrameLayout getFooter() {
        return footer;
    }

    /**
     * 添加脚部
     *
     * @param view 头部View
     */
    public void addFooter(View view) {
        footer.addView(view);
    }

    /**
     * 添加脚部
     *
     * @param view   头部View
     * @param params 属性参数
     */
    public void addFooter(View view, ViewGroup.LayoutParams params) {
        footer.addView(view, params);
    }

    /**
     * 设置滚轮选择器改变监听
     *
     * @param onWheelPickerChangedListener 滚轮选择器改变监听
     */
    public void setOnWheelPickerChangedListener(OnWheelPickerChangedListener onWheelPickerChangedListener) {
        this.onWheelPickerChangedListener = onWheelPickerChangedListener;
    }

    /**
     * 设置滚轮选择器确认监听
     *
     * @param onWheelPickerConfirmListener 滚轮选择器确认监听
     */
    public void setOnWheelPickerConfirmListener(OnWheelPickerConfirmListener onWheelPickerConfirmListener) {
        this.onWheelPickerConfirmListener = onWheelPickerConfirmListener;
    }

    /**
     * @return 滚轮组合
     */
    public LinearLayout getGroup() {
        return group;
    }

    /**
     * 获取滚轮View集合
     *
     * @return
     */
    public List<Wheel3DView> getWheels() {
        return wheels;
    }

    /**
     * 获取滚路对象
     *
     * @param position 滚轮集合位置
     * @return
     */
    public Wheel3DView getWheel(int position) {
        return wheels.get(position);
    }

    /**
     * 设置滚轮item位置
     *
     * @param index    滚轮集合下标
     * @param position 滚轮位置
     */
    public void setWheelsPosition(int index, int position) {
        if (index < wheels.size()) {
            getWheel(index).setCurrentIndex(position);
        }
    }

    /**
     * 设置数据源
     *
     * @param index      滚轮集合位置
     * @param collection 数据
     */
    public void setWheelsDataSource(int index, Collection<? extends CharSequence> collection) {
        getWheel(index).setDataSource(collection);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
            onCancel();
        }
        if (id == R.id.tv_confirm) {
            onConfirm();
        }
    }

    /**
     * 取消
     */
    protected void onCancel() {
        dismiss();
    }

    /**
     * 确认
     */
    protected void onConfirm() {
        if (onWheelPickerConfirmListener != null) {
            onWheelPickerConfirmListener.onWheelPickerConfirm(this);
        }
    }

}
