package androidx.ui.text;

import android.widget.EditText;
import android.widget.TextView;

/**
 * 为空判断
 */
public class Text {

    /**
     * 判断是否为空
     *
     * @param editText 编辑控件
     * @return
     */
    public static boolean isEmpty(EditText editText) {
        if (editText == null) {
            return true;
        }
        if (editText.getText().toString().length() == 0) {
            return true;
        }
        if (editText.getText().toString().equals("null")) {
            return true;
        }
        if (editText.getText().toString().length()==0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为空
     *
     * @param textView 显示控件
     * @return
     */
    public static boolean isEmpty(TextView textView) {
        if (textView == null) {
            return true;
        }
        if (textView.getText().toString().length() == 0) {
            return true;
        }
        if (textView.getText().toString().equals("null")) {
            return true;
        }
        if (textView.getText().toString().length()==0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为空
     *
     * @param value 内容
     * @return
     */
    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0 || value.toLowerCase().equals("null");
    }

    /**
     * 是否已赋值，不为空
     *
     * @param value 内容
     * @return
     */
    public static boolean isAssign(String value) {
        return !isEmpty(value);
    }

    /**
     * 是否已赋值，不为空
     *
     * @param value 内容
     * @return
     */
    public static boolean isAssign(TextView value) {
        return !isEmpty(value);
    }

    /**
     * 是否已赋值，不为空
     *
     * @param value 内容
     * @return
     */
    public static boolean isAssign(EditText value) {
        return !isEmpty(value);
    }

    /**
     * 获取控件值
     *
     * @param textView 显示控件
     * @return
     */
    public static String from(TextView textView) {
        if (textView == null) {
            return "";
        }
        return from(textView.getText().toString());
    }

    /**
     * 处理空数据
     *
     * @param editText 编辑控件
     * @return
     */
    public static String from(EditText editText) {
        if (editText == null) {
            return "";
        }
        return from(editText.getText().toString());
    }

    /**
     * 处理空数据
     *
     * @param content
     * @return
     */
    public static String from(String content) {
        if (content == null) {
            return "";
        }
        if (isEmpty(content)) {
            return "";
        }
        return content;
    }

}
