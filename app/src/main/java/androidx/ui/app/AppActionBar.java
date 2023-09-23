package androidx.ui.app;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.ui.R;
import androidx.ui.type.Position;

/**
 * 应用标题栏
 */
public class AppActionBar implements View.OnClickListener {

    private AppCompatActivity activity;
    private ActionBar actionBar;
    private Toolbar toolbar;

    private FrameLayout appActionBarView;
    private ImageView backIconView;
    private TextView backTextView;
    private TextView titleView;
    private ImageView menuIconView;
    private TextView menuTextView;
    private TextView menuNumberTextView;

    public AppActionBar(AppCompatActivity activity) {
        this.activity = activity;
        onCreateView();
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public Context getContext() {
        return activity;
    }

    /**
     * 获取Activity
     *
     * @return
     */
    public AppCompatActivity getActivity() {
        return activity;
    }

    /**
     * 获取ActionBar
     *
     * @return
     */
    public ActionBar getActionBar() {
        return actionBar;
    }

    /**
     * 获取Toolbar
     *
     * @return
     */
    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * 获取内容Layout
     *
     * @return
     */
    public int getAppActionBarLayoutResId() {
        return R.layout.ui_app_action_bar;
    }

    /**
     * 获取ActionBar布局参数
     *
     * @return
     */
    public ActionBar.LayoutParams getActionBarLayoutParams() {
        return new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
    }

    /**
     * 创建View
     */
    protected void onCreateView() {
        View contentView = LayoutInflater.from(getContext()).inflate(getAppActionBarLayoutResId(), null, false);
        ActionBar.LayoutParams layoutParams = getActionBarLayoutParams();
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL;
        actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0f);//设置不显示底部阴影分割线
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(contentView, layoutParams);
            toolbar = (Toolbar) contentView.getParent();
            toolbar.setContentInsetsAbsolute(0, 0);
        }
        onViewCreated(contentView);
    }

    /**
     * View创建完成
     *
     * @param contentView 内容View
     */
    protected void onViewCreated(View contentView) {
        appActionBarView = contentView.findViewById(R.id.app_action_bar);
        backIconView = contentView.findViewById(R.id.app_action_bar_back_icon);
        backTextView = contentView.findViewById(R.id.app_action_bar_back_text);
        titleView = contentView.findViewById(R.id.app_action_bar_title);
        menuIconView = contentView.findViewById(R.id.app_action_bar_menu_icon);
        menuTextView = contentView.findViewById(R.id.app_action_bar_menu_text);
        menuNumberTextView = contentView.findViewById(R.id.app_action_bar_menu_number);
        menuNumberTextView.setVisibility(View.INVISIBLE);
        backIconView.setOnClickListener(this);
        backTextView.setOnClickListener(this);
        titleView.setOnClickListener(this);
        menuIconView.setOnClickListener(this);
        menuTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.app_action_bar_back_icon || id == R.id.app_action_bar_back_text) {
            if (onAppActionBarClickListener != null) {
                onAppActionBarClickListener.onBackClick(v);
            }
        } else if (id == R.id.app_action_bar_title) {
            if (onAppActionBarClickListener != null) {
                onAppActionBarClickListener.onTitleClick(v);
            }
        } else if (id == R.id.app_action_bar_menu_icon || id == R.id.app_action_bar_menu_text) {
            if (onAppActionBarClickListener != null) {
                onAppActionBarClickListener.onMenuClick(v);
            }
        }
    }

    /**
     * 设置View内间距
     *
     * @param v        控件
     * @param position 位置{@link Position#ALL}
     * @param padding  间距
     */
    public void setViewPadding(View v, Position position, int padding) {
        int leftPadding = v.getPaddingLeft();
        int topPadding = v.getPaddingTop();
        int rightPadding = v.getPaddingRight();
        int bottomPadding = v.getPaddingBottom();
        if (position == Position.ALL) {
            v.setPadding(padding, padding, padding, padding);
        }
        if (position == Position.HORIZONTAL) {
            v.setPadding(padding, topPadding, padding, bottomPadding);
        }
        if (position == Position.VERTICAL) {
            v.setPadding(leftPadding, padding, rightPadding, padding);
        }
        if (position == Position.LEFT) {
            v.setPadding(padding, topPadding, rightPadding, bottomPadding);
        }
        if (position == Position.TOP) {
            v.setPadding(leftPadding, padding, rightPadding, bottomPadding);
        }
        if (position == Position.RIGHT) {
            v.setPadding(leftPadding, topPadding, padding, bottomPadding);
        }
        if (position == Position.BOTTOM) {
            v.setPadding(leftPadding, topPadding, rightPadding, padding);
        }
    }

    /**
     * 设置View外间距
     *
     * @param v        控件
     * @param position 位置{@link Position#ALL}
     * @param margin   间距
     */
    public void setViewMargin(View v, Position position, int margin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        int leftMargin = params.leftMargin;
        int topMargin = params.topMargin;
        int rightMargin = params.rightMargin;
        int bottomMargin = params.bottomMargin;
        if (position == Position.ALL) {
            leftMargin = margin;
            topMargin = margin;
            rightMargin = margin;
            bottomMargin = margin;
        }
        if (position == Position.HORIZONTAL) {
            leftMargin = margin;
            rightMargin = margin;
        }
        if (position == Position.VERTICAL) {
            topMargin = margin;
            bottomMargin = margin;
        }
        if (position == Position.LEFT) {
            leftMargin = margin;
        }
        if (position == Position.TOP) {
            topMargin = margin;
        }
        if (position == Position.RIGHT) {
            rightMargin = margin;
        }
        if (position == Position.BOTTOM) {
            bottomMargin = margin;
        }
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        params.rightMargin = rightMargin;
        params.bottomMargin = bottomMargin;
        v.setLayoutParams(params);
    }

    /**
     * 显示
     */
    public void show() {
        actionBar.show();
    }

    /**
     * 隐藏
     */
    public void hide() {
        actionBar.hide();
    }

    /**
     * 设置状态栏文字颜色
     *
     * @param dark 是否黑色
     */
    public void setStatusBarTextColor(boolean dark) {
        AppStatusBar.setTextColor(activity, dark);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color 颜色值
     */
    public void setStatusBarColor(int color) {
        AppStatusBar.setColor(activity, color);
    }

    /**
     * 设置侵入式
     *
     * @param statusBarTextDark 状态栏文字颜色
     */
    public void setImmersed(boolean statusBarTextDark) {
        hide();
        setBackgroundColor(Color.TRANSPARENT);
        setStatusBarTextColor(statusBarTextDark);
    }

    /**
     * 设置背景
     *
     * @param resId 资源id
     */
    public void setBackgroundResource(@DrawableRes int resId) {
        appActionBarView.setBackgroundResource(resId);
    }

    /**
     * 设置背景颜色
     *
     * @param color 颜色值
     */
    public void setBackgroundColor(@ColorInt int color) {
        setBackgroundColor(color, true);
    }

    /**
     * 设置背景颜色
     *
     * @param color          颜色值
     * @param applyStatusBar 是否应用到状态栏
     */
    public void setBackgroundColor(@ColorInt int color, boolean applyStatusBar) {
        appActionBarView.setBackgroundColor(color);
        setStatusBarTextColor(color == Color.WHITE || color == Color.TRANSPARENT);
        if (applyStatusBar) {
            setStatusBarColor(color);
        }
    }

    /**
     * 获取返回按钮View
     *
     * @return
     */
    public ImageView getBackIconView() {
        return backIconView;
    }

    /**
     * 设置返回按钮
     *
     * @param dark 是否黑色
     */
    public void setBackIcon(boolean dark) {
        backIconView.setImageResource(dark ? R.mipmap.ui_action_bar_back_black : R.mipmap.ui_action_bar_back_white);
    }

    /**
     * 设置返回按钮
     *
     * @param resId 资源id
     */
    public void setBackIconResource(@DrawableRes int resId) {
        backIconView.setImageResource(resId);
    }

    /**
     * 设置返回按钮内间距
     *
     * @param position 位置{@link Position#ALL}
     * @param padding  间距
     */
    public void setBackIconPadding(Position position, int padding) {
        setViewPadding(backIconView, position, padding);
    }

    /**
     * 获取返回文字View
     *
     * @return
     */
    public TextView getBackTextView() {
        return backTextView;
    }

    /**
     * 返回文字
     *
     * @param text 文字
     */
    public void setBackText(String text) {
        backTextView.setText(text);
    }

    /**
     * 返回文字大小
     *
     * @param size
     */
    public void setBackTextSize(int size) {
        backTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 返回文字颜色
     *
     * @param color
     */
    public void setBackTextColor(@ColorInt int color) {
        backTextView.setTextColor(color);
    }

    /**
     * 返回文字内间距
     *
     * @param position 位置{@link Position#ALL}
     * @param padding  间距
     */
    public void setBackTextPadding(Position position, int padding) {
        setViewPadding(backTextView, position, padding);
    }

    /**
     * 返回文字外间距
     *
     * @param position 位置{@link Position#ALL}
     * @param margin   间距
     */
    public void setBackTextMargin(Position position, int margin) {
        setViewMargin(backTextView, position, margin);
    }

    /**
     * 获取标题View
     *
     * @return
     */
    public TextView getTitleView() {
        return titleView;
    }

    /**
     * 设置标题
     *
     * @param text 文字
     */
    public void setTitle(String text) {
        titleView.setText(text);
    }

    /**
     * 设置标题文字大小
     *
     * @param size 大小
     */
    public void setTitleTextSize(int size) {
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置文字颜色
     *
     * @param color 颜色值
     */
    public void setTitleTextColor(@ColorInt int color) {
        titleView.setTextColor(color);
    }

    /**
     * 获取菜单按钮View
     *
     * @return
     */
    public ImageView getMenuIconView() {
        return menuIconView;
    }

    /**
     * 设置菜单按钮
     *
     * @param resId 资源id
     */
    public void setMenuIcon(@DrawableRes int resId) {
        menuIconView.setImageResource(resId);
    }

    /**
     * 设置菜单按钮内间距
     *
     * @param position 位置{@link Position#ALL}
     * @param padding  间距
     */
    public void setMenuIconPadding(Position position, int padding) {
        setViewPadding(menuIconView, position, padding);
    }

    /**
     * 设置菜单按钮外间距
     *
     * @param position 位置{@link Position#ALL}
     * @param margin   间距
     */
    public void setMenuIconMargin(Position position, int margin) {
        setViewMargin(menuIconView, position, margin);
    }

    /**
     * 获取菜单文字View
     *
     * @return
     */
    public TextView getMenuTextView() {
        return menuTextView;
    }

    /**
     * 设置菜单文字
     *
     * @param text 文字
     */
    public void setMenuText(String text) {
        menuTextView.setText(text);
    }

    /**
     * 设置菜单文字大小
     *
     * @param size 大小
     */
    public void setMenuTextSize(int size) {
        menuTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置菜单文字颜色
     *
     * @param color 颜色值
     */
    public void setMenuTextColor(@ColorInt int color) {
        menuTextView.setTextColor(color);
    }

    /**
     * 设置菜单文字内间距
     *
     * @param position 位置{@link Position#ALL}
     * @param padding  间距
     */
    public void setMenuTextPadding(Position position, int padding) {
        setViewPadding(menuTextView, position, padding);
    }

    /**
     * 设置菜单文字外间距
     *
     * @param position 位置{@link Position#ALL}
     * @param margin   间距
     */
    public void setMenuTextMargin(Position position, int margin) {
        setViewMargin(menuTextView, position, margin);
    }

    /**
     * 设置菜单文字
     *
     * @param visibility 是否可见
     */
    public void setMenuNumberVisibility(int visibility) {
        menuNumberTextView.setVisibility(visibility);
    }

    /**
     * 设置菜单文字
     *
     * @param text 文字
     */
    public void setMenuNumberText(String text) {
        text = TextUtils.isEmpty(text) ? "0" : text;
        menuNumberTextView.setText(text);
        menuNumberTextView.setVisibility(text.equals("0") ? View.GONE : View.VISIBLE);
    }

    /**
     * 设置菜单文字
     *
     * @param text       文字
     * @param visibility 是否可见
     */
    public void setMenuNumberText(String text, int visibility) {
        text = TextUtils.isEmpty(text) ? "0" : text;
        menuNumberTextView.setText(text);
        menuNumberTextView.setVisibility(visibility);
    }

    /**
     * 设置菜单数量文字大小
     *
     * @param size 大小
     */
    public void setMenuNumberTextSize(int size) {
        menuNumberTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置菜单数量文字颜色
     *
     * @param color 颜色值
     */
    public void setMenuNumberTextColor(@ColorInt int color) {
        menuNumberTextView.setTextColor(color);
    }

    /**
     * 设置菜单数量内间距
     *
     * @param position 位置{@link Position#ALL}
     * @param padding  间距
     */
    public void setMenuNumberTextPadding(Position position, int padding) {
        setViewPadding(menuNumberTextView, position, padding);
    }

    /**
     * 设置菜单数量外间距
     *
     * @param position 位置{@link Position#ALL}
     * @param margin   间距
     */
    public void setMenuNumberTextMargin(Position position, int margin) {
        setViewMargin(menuNumberTextView, position, margin);
    }

    /**
     * 点击事件
     */
    private OnAppActionBarClickListener onAppActionBarClickListener;

    /**
     * 设置点击事件
     *
     * @param onAppActionBarClickListener 点击事件
     */
    public void setOnAppActionBarClickListener(OnAppActionBarClickListener onAppActionBarClickListener) {
        this.onAppActionBarClickListener = onAppActionBarClickListener;
    }

    public interface OnAppActionBarClickListener {

        /**
         * 返回点击
         *
         * @param v
         */
        void onBackClick(View v);

        /**
         * 标题点击
         *
         * @param v
         */
        void onTitleClick(View v);

        /**
         * 菜单点击
         *
         * @param v
         */
        void onMenuClick(View v);

    }

}
