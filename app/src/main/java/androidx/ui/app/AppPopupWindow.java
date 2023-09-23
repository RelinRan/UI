package androidx.ui.app;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.IdRes;

/**
 * 弹出窗体
 */
public abstract class AppPopupWindow extends PopupWindow implements View.OnClickListener {

    public final static int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    public final static int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;

    private View contentView;
    private View cover;
    private Context context;

    public AppPopupWindow(Context context) {
        super(context);
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(getContentLayoutResId(), null);
        setContentView(contentView);
        onViewCreated(contentView);
        setWidth(getLayoutWidth());
        setHeight(getLayoutHeight());
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        onViewCreated(contentView);
    }

    /**
     * @return 上下文对象
     */
    public Context getContext() {
        return context;
    }

    /**
     * @return 内容资源Id
     */
    public abstract int getContentLayoutResId();

    /**
     * @return 布局宽度
     */
    public int getLayoutWidth() {
        return MATCH_PARENT;
    }

    /**
     * @return 布局高度
     */
    public int getLayoutHeight() {
        return WRAP_CONTENT;
    }

    /**
     * View创建
     *
     * @param contentView 内容视图
     */
    protected void onViewCreated(View contentView) {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 添加点击事件
     * @param ids view ids
     */
    protected void addClick(int... ids) {
        for (int i = 0; i < ids.length; i++) {
            findViewById(ids[i]).setOnClickListener(this);
        }
    }

    /**
     * 添加点击事件
     * @param views views
     */
    protected void addClick(View... views) {
        for (int i = 0; i < views.length; i++) {
            views[i].setOnClickListener(this);
        }
    }

    /**
     * 查找控件
     *
     * @param id  view id
     * @param <T>
     * @return
     */
    public final <T extends View> T findViewById(@IdRes int id) {
        if (id == View.NO_ID) {
            return null;
        }
        return contentView.findViewById(id);
    }

    /**
     * 设置覆盖View
     *
     * @param cover 覆盖View
     */
    public void setCover(View cover) {
        this.cover = cover;
    }

    /**
     * 设置覆盖View是否可见
     *
     * @param visibility
     */
    public void setCoverVisibility(int visibility) {
        if (cover != null) {
            cover.setVisibility(visibility);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setCoverVisibility(GONE);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        setCoverVisibility(VISIBLE);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        setCoverVisibility(VISIBLE);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        setCoverVisibility(VISIBLE);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        setCoverVisibility(VISIBLE);
    }
}
