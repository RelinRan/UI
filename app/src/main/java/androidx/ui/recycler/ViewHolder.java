package androidx.ui.recycler;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter控件容器
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    /**
     * Item视图
     */
    public View itemView;
    /**
     * 视图类型
     */
    public int viewType;
    /**
     * 位置
     */
    public int itemPosition = -1;

    public ViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public ViewHolder(View itemView, int viewType) {
        super(itemView);
        this.itemView = itemView;
        this.viewType = viewType;
    }

    /**
     * 获取视图类型
     *
     * @return
     */
    public int getViewType() {
        return viewType;
    }

    /**
     * 设置视图类型
     *
     * @param viewType
     */
    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    /**
     * 设置ItemView
     *
     * @return
     */
    public View getItemView() {
        return itemView;
    }

    /**
     * 设置itemView
     *
     * @param itemView
     */
    public void setItemView(View itemView) {
        this.itemView = itemView;
    }

    /**
     * 设置Item位置，非RecyclerView的Adapter设置,
     *
     * @param position 位置
     */
    public void setItemPosition(int position) {
        this.itemPosition = position;
    }

    /**
     * @return Item位置
     */
    public int getItemPosition() {
        return itemPosition == -1 ? getAdapterPosition() : itemPosition;
    }

    /**
     * @param id view id
     * @return item中view
     */
    public <T extends View> T find(@IdRes int id) {
        return itemView.findViewById(id);
    }

    /**
     * 获取控件
     *
     * @param cls 类名
     * @param id  view id
     * @param <T> view
     * @return
     */
    public <T extends View> T find(Class<T> cls, @IdRes int id) {
        return itemView.findViewById(id);
    }

    /**
     * 添加点击事件
     *
     * @param id 控件id
     * @return ViewHolder
     */
    public ViewHolder addItemClick(@IdRes int id) {
        addItemClick(find(id));
        return this;
    }

    /**
     * 添加点击事件
     *
     * @param v 控件
     * @return ViewHolder
     */
    public ViewHolder addItemClick(View v) {
        if (v != null) {
            v.setOnClickListener(new ItemClick(onItemClickLister));
        }
        return this;
    }

    /**
     * Item点击
     */
    private class ItemClick implements View.OnClickListener {

        private OnItemClickLister onItemClickLister;

        public ItemClick(OnItemClickLister onItemClickLister) {
            this.onItemClickLister = onItemClickLister;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickLister != null) {
                onItemClickLister.onItemClick(v, getItemPosition());
            }
        }
    }

    /**
     * ViewHolder点击事件
     */
    private OnItemClickLister onItemClickLister;

    /**
     * 设置ViewHolder点击事件
     *
     * @param onItemClickLister
     */
    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    public interface OnItemClickLister {

        /**
         * ViewHolder点击事件
         *
         * @param v 控件
         */
        void onItemClick(View v, int position);

    }

    /**
     * 添加焦点监听
     *
     * @param id 控件id
     * @return
     */
    public ViewHolder addItemFocus(@IdRes int id) {
        addItemFocus(id);
        return this;
    }

    /**
     * 添加焦点监听
     *
     * @param v 控件
     * @return
     */
    public ViewHolder addItemFocus(View v) {
        if (v != null) {
            v.setOnFocusChangeListener(new ItemViewFocus(onItemFocusChangeListener));
        }
        return this;
    }

    /**
     * item焦点改变监听
     */
    private OnItemFocusChangeListener onItemFocusChangeListener;

    /**
     * 设置item焦点改变监听
     *
     * @param onItemFocusChangeListener
     */
    public void setOnItemFocusChangeListener(OnItemFocusChangeListener onItemFocusChangeListener) {
        this.onItemFocusChangeListener = onItemFocusChangeListener;
    }

    public interface OnItemFocusChangeListener {

        /**
         * 焦点事件
         *
         * @param v        控件
         * @param position 位置
         * @param hasFocus 是否有焦点
         */
        void onItemFocusChange(View v, int position, boolean hasFocus);

    }

    /**
     * Item焦点
     */
    private class ItemViewFocus implements View.OnFocusChangeListener {

        /**
         * 焦点改变监听
         */
        private OnItemFocusChangeListener onItemFocusChangeListener;

        public ItemViewFocus(OnItemFocusChangeListener onItemFocusChangeListener) {
            this.onItemFocusChangeListener = onItemFocusChangeListener;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (onItemFocusChangeListener != null) {
                onItemFocusChangeListener.onItemFocusChange(v, getItemPosition(), hasFocus);
            }
        }
    }

}
