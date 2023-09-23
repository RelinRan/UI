package androidx.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.ui.R;
import androidx.ui.app.AppDialog;

/**
 * 消息提示
 */
public class MessageDialog extends AppDialog implements View.OnClickListener {

    private TextView tv_title;
    private TextView tv_content;
    private View v_horizontal_divider;
    private TextView tv_cancel;
    private View v_vertical_divider;
    private TextView tv_confirm;
    private OnMessageDialogListener onMessageDialogListener;

    public MessageDialog(@NonNull Context context) {
        super(context);
    }

    public MessageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public MessageDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.ui_message_dialog;
    }

    @Override
    public int getLayoutWidth() {
        return getWidthByDisplayMetrics(0.75F);
    }

    @Override
    public int getLayoutHeight() {
        return WRAP_CONTENT;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public int getWindowAnimationResId() {
        return WINDOW_ANIM_SCALE;
    }

    @Override
    public void onViewCreated(View contentView) {
        tv_title = contentView.findViewById(R.id.tv_title);
        tv_content = contentView.findViewById(R.id.tv_content);
        v_horizontal_divider = contentView.findViewById(R.id.v_horizontal_divider);
        tv_cancel = contentView.findViewById(R.id.tv_cancel);
        v_vertical_divider = contentView.findViewById(R.id.v_vertical_divider);
        tv_confirm = contentView.findViewById(R.id.tv_confirm);
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    /**
     * @return 标题View
     */
    public TextView getTitle() {
        return tv_title;
    }

    /**
     * 设置标题
     *
     * @param text
     */
    public void setTitle(String text) {
        tv_title.setText(text);
    }

    /**
     * 设置标题是否可见
     *
     * @param visibility 可见性{@link View#VISIBLE} or {@link View#GONE}
     */
    public void setTitleVisibility(int visibility) {
        tv_title.setVisibility(visibility);
    }

    /**
     * @return 内容View
     */
    public TextView getContent() {
        return tv_content;
    }

    /**
     * 设置消息内容
     *
     * @param msg 内容
     */
    public void setContent(String msg) {
        tv_content.setText(msg);
    }

    /**
     * @return 水平分割线View
     */
    public View getHorizontalDivider() {
        return v_horizontal_divider;
    }

    /**
     * 设置水平分割线颜色
     *
     * @param color 颜色值
     */
    public void setHorizontalDividerColor(int color) {
        v_horizontal_divider.setBackgroundColor(color);
    }

    /**
     * @return 取消View
     */
    public TextView getCancel() {
        return tv_cancel;
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
     * @return 垂直分割线View
     */
    public View getVerticalDivider() {
        return v_vertical_divider;
    }

    /**
     * 设置垂直分割线颜色
     *
     * @param color 颜色值
     */
    public void setVerticalDividerColor(int color) {
        v_vertical_divider.setBackgroundColor(color);
    }

    /**
     * @return 确认View
     */
    public TextView getConfirm() {
        return tv_confirm;
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
     * 设置消息监听
     *
     * @param onMessageDialogListener 监听
     */
    public void setOnMessageDialogListener(OnMessageDialogListener onMessageDialogListener) {
        this.onMessageDialogListener = onMessageDialogListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
            onCancel(v);
        }
        if (id == R.id.tv_confirm) {
            onConfirm(v);
        }
    }

    protected void onCancel(View v) {
        if (onMessageDialogListener != null) {
            onMessageDialogListener.onMessageDialogCancel(this);
        }
    }

    protected void onConfirm(View v) {
        if (onMessageDialogListener != null) {
            onMessageDialogListener.onMessageDialogConfirm(this);
        }
    }

}
