package androidx.ui.dialog;

public interface OnMessageDialogListener {

    /**
     * 取消
     *
     * @param dialog 提示对象
     */
    void onMessageDialogCancel(MessageDialog dialog);

    /**
     * 确认
     *
     * @param dialog 提示对象
     */
    void onMessageDialogConfirm(MessageDialog dialog);

}
