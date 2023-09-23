package androidx.ui.app;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.ui.R;

/**
 * 提示框
 */
public class AppToast {

    /**
     * 构建Toast
     *
     * @param context  上下文
     * @param gravity  位置
     * @param xOffset  横向偏移量
     * @param yOffset  纵向偏移量
     * @param duration 时长
     * @param msg      内容
     * @return
     */
    public static Toast makeText(Context context, int gravity, int xOffset, int yOffset, int duration, String msg) {
        Toast toast = new Toast(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.ui_toast, null);
        TextView textView = contentView.findViewById(R.id.android_toast_text);
        textView.setText(msg);
        toast.setView(contentView);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setDuration(duration);
        return toast;
    }

    /**
     * 找到文字View
     *
     * @param toast 提示对象
     * @return
     */
    public static TextView findTextView(Toast toast) {
        return toast.getView().findViewById(R.id.android_toast_text);
    }

    /**
     * 提示
     *
     * @param context 上下文
     * @param msg     内容
     */
    public static void show(Context context, String msg) {
        int yOffset = context.getResources().getDimensionPixelOffset(R.dimen.toast_y_offset);
        makeText(context, Gravity.BOTTOM, 0, yOffset, Toast.LENGTH_SHORT, msg).show();
    }

    /**
     * 提示
     *
     * @param context 上下文
     * @param msg     内容
     * @param yOffset 纵向偏移量
     */
    public static void show(Context context, String msg, int yOffset) {
        makeText(context, Gravity.BOTTOM, 0, yOffset, Toast.LENGTH_SHORT, msg).show();
    }

}
