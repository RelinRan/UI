package androidx.ui.app;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.ui.R;
import androidx.ui.widget.LoadingView;

/**
 * 正在加载实现类
 */
public class AppLoadingImpl extends AppDialog implements AppLoading {

    private FrameLayout loadingGroupParentView;
    private LinearLayout loadingGroupView;
    private LoadingView loadingView;
    private TextView loadingTextView;

    public AppLoadingImpl(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.ui_loading;
    }

    @Override
    public int getLayoutWidth() {
        return WRAP_CONTENT;
    }

    @Override
    public int getLayoutHeight() {
        return WRAP_CONTENT;
    }

    @Override
    public int getThemeResId() {
        return THEME_TRANSPARENT;
    }

    @Override
    public void onViewCreated(View contentView) {
        loadingGroupParentView = contentView.findViewById(R.id.android_loading_group_parent);
        loadingGroupView = contentView.findViewById(R.id.android_loading_group);
        loadingView = contentView.findViewById(R.id.android_loading);
        loadingTextView = contentView.findViewById(R.id.android_loading_txt);
    }

    @Override
    public void showLoading() {
        loadingTextView.setVisibility(View.GONE);
        loadingView.start();
        show();
    }

    @Override
    public void showLoading(String msg) {
        loadingTextView.setVisibility(View.VISIBLE);
        loadingTextView.setText(msg);
        loadingView.start();
        show();
    }

    @Override
    public void dismissLoading() {
        loadingView.cancel();
        dismiss();
    }

    /**
     * Loading组合的父类View - dialog整体
     *
     * @return
     */
    public FrameLayout getLoadingGroupParentView() {
        return loadingGroupParentView;
    }

    /**
     * Loading组合View
     *
     * @return
     */
    public LinearLayout getLoadingGroupView() {
        return loadingGroupView;
    }

    /**
     * LoadingView
     *
     * @return
     */
    public LoadingView getLoadingView() {
        return loadingView;
    }

    /**
     * Loading文字View
     *
     * @return
     */
    public TextView getLoadingTextView() {
        return loadingTextView;
    }

}
