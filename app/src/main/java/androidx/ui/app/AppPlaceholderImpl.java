package androidx.ui.app;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.ui.R;


import java.util.HashMap;
import java.util.Map;

public class AppPlaceholderImpl implements AppPlaceholder {

    protected final static int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    protected final static int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    private Context context;
    private FrameLayout parent;
    private View appContentView;
    private View placeholderView;
    private ImageView imageView;
    private TextView textView;
    private Map<Integer, String> textMap;
    private Map<Integer, Integer> drawableMap;

    public AppPlaceholderImpl(AppLayout appLayout) {
        onCreateView(appLayout);
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public Context getContext() {
        return context;
    }

    /**
     * 初始化占位数据Map
     */
    protected void initPlaceholderMap() {
        drawableMap = new HashMap<>();
        textMap = new HashMap<>();
        setPlaceholder(PLACEHOLDER_EMPTY, R.mipmap.ui_placeholder_empty, "没有找到您要的信息");
        setPlaceholder(PLACEHOLDER_ERROR, R.mipmap.ui_placeholder_error, "数据加载失败！请刷新重试");
        setPlaceholder(PLACEHOLDER_NET, R.mipmap.ui_placeholder_net, "暂无网络连接");
        setPlaceholder(PLACEHOLDER_MSG, R.mipmap.ui_placeholder_msg, "暂无消息");
    }

    /**
     * 获取占位参数
     *
     * @return
     */
    protected FrameLayout.LayoutParams obtainPlaceholderParams() {
        FrameLayout.LayoutParams placeholderParams = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        placeholderParams.gravity = Gravity.CENTER;
        return placeholderParams;
    }

    /**
     * 创建全部View
     *
     * @param appLayout 应用页面Layout
     */
    protected void onCreateView(AppLayout appLayout) {
        initPlaceholderMap();
        context = appLayout.getAppContext();
        parent = new FrameLayout(context);
        parent.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        //Activity、Fragment布局
        if (appLayout.getContentViewLayoutResId() != 0) {
            appContentView = LayoutInflater.from(context).inflate(appLayout.getContentViewLayoutResId(), parent, false);
            parent.addView(appContentView);
        }
        //Placeholder布局
        placeholderView = onCreatePlaceholder(context);
        parent.addView(placeholderView, obtainPlaceholderParams());
        onViewCreated(parent, placeholderView);
    }

    /**
     * 创建占位布局
     *
     * @param context 上下文
     * @return
     */
    protected View onCreatePlaceholder(Context context) {
        LinearLayout placeholderView = new LinearLayout(context);
        placeholderView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        placeholderView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        imageView = new ImageView(context);
        imageView.setImageResource(R.mipmap.ui_placeholder_empty);
        placeholderView.addView(imageView, params);
        textView = new TextView(context);
        textView.setTextColor(Color.parseColor("#ACABAB"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setText("没有找到您要的信息");
        params.topMargin = (int) (context.getResources().getDisplayMetrics().density * 10);
        placeholderView.addView(textView, params);
        return placeholderView;
    }

    /**
     * 布局创建完成
     *
     * @param parent      父级
     * @param contentView 占位View
     */
    public void onViewCreated(ViewGroup parent, View contentView) {
        hidePlaceholder();
    }

    /**
     * 获取Activity、Fragment xml布局View(不包含placeholder)
     *
     * @return
     */
    public View getAppContentView() {
        return appContentView;
    }

    /**
     * 获取父级
     *
     * @return
     */
    public FrameLayout getParent() {
        return parent;
    }

    /**
     * 设置背景颜色
     *
     * @param color
     */
    public void setBackgroundColor(@ColorInt int color) {
        parent.setBackgroundColor(color);
    }

    /**
     * 设置背景资源
     *
     * @param resId
     */
    public void setBackgroundResource(@DrawableRes int resId) {
        parent.setBackgroundResource(resId);
    }

    /**
     * 获取占位View
     *
     * @return
     */
    public View getPlaceholderView() {
        return placeholderView;
    }

    /**
     * 删除内容View
     */
    public void removePlaceholderView() {
        if (placeholderView.getParent() != null) {
            parent.removeView(placeholderView);
        }
    }

    /**
     * 设置占位View
     *
     * @param placeholderView 占位视图
     */
    public void setPlaceholderView(View placeholderView) {
        removePlaceholderView();
        this.placeholderView = placeholderView;
        parent.addView(placeholderView, obtainPlaceholderParams());
    }

    /**
     * 设置占位视图
     *
     * @param layoutResId 资源布局ID
     */
    public void setPlaceholderView(@LayoutRes int layoutResId) {
        removePlaceholderView();
        placeholderView = LayoutInflater.from(getContext()).inflate(layoutResId, parent, false);
        parent.addView(placeholderView, obtainPlaceholderParams());
    }

    /**
     * 获取图片View
     *
     * @return
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * 获取文字View
     *
     * @return
     */
    public TextView getTextView() {
        return textView;
    }

    /**
     * 设置图片资源
     *
     * @param resId
     */
    public void setImageResource(@DrawableRes int resId) {
        imageView.setImageResource(resId);
    }

    /**
     * 设置文字
     *
     * @param text
     */
    public void setText(String text) {
        textView.setText(text);
    }

    /**
     * 设置文字大小
     *
     * @param size
     */
    public void setTextSize(int size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public void setTextColor(@ColorInt int color) {
        textView.setTextColor(color);
    }

    @Override
    public void setPlaceholder(int type, int resId, String text) {
        drawableMap.put(type, resId);
        textMap.put(type, text);
    }

    @Override
    public void setPlaceholder(int type, int resId) {
        drawableMap.put(type, resId);
    }

    @Override
    public void setPlaceholder(int type, String text) {
        textMap.put(type, text);
    }

    @Override
    public void showPlaceholder(int type) {
        placeholderView.setVisibility(View.VISIBLE);
        setImageResource(drawableMap.get(type));
        setText(textMap.get(type));
    }

    @Override
    public void hidePlaceholder() {
        placeholderView.setVisibility(View.GONE);
    }

}
