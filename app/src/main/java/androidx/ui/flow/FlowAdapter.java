package androidx.ui.flow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.ui.recycler.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局适配器
 */
public abstract class FlowAdapter<T> implements ViewHolder.OnItemClickLister {

    private List<T> items;
    private Context context;
    private FlowLayout flowLayout;
    /**
     * 构建流式布局适配器
     *
     * @param context 上下文
     */
    public FlowAdapter(Context context) {
        this.context = context;
    }

    /**
     * 设置布局
     *
     * @param layout 布局
     */
    public void setFlowLayout(FlowLayout layout) {
        this.flowLayout = layout;
    }

    /**
     * @return 上下文
     */
    public Context getContext() {
        return context;
    }

    /**
     * @return 流式布局
     */
    public FlowLayout getFlowLayout() {
        return flowLayout;
    }

    /**
     * @return 数据
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * @return 个数
     */
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    /**
     * @param position 位置
     * @return 视图类型
     */
    protected int getItemViewType(int position) {
        return position;
    }

    /**
     * @param viewType 视图类型
     * @return item布局资源
     */
    protected abstract int getItemLayoutResId(int viewType);

    /**
     * 通知数据源改变
     */
    public void notifyDataSetChanged() {
        if (flowLayout == null) {
            return;
        }
        flowLayout.removeAllViews();
        for (int i = 0; i < getItemCount(); i++) {
            onCreate(flowLayout, i);
        }
    }

    /**
     * @param viewType 视图类型
     * @return item视图
     */
    protected View getItemView(int viewType) {
        FrameLayout parent = new FrameLayout(getContext());
        View itemView = LayoutInflater.from(getContext()).inflate(getItemLayoutResId(viewType), parent, false);
        parent.addView(itemView);
        return parent;
    }

    /**
     * @param parent   父级
     * @param position 位置
     */
    protected void onCreate(FlowLayout parent, int position) {
        View itemView = getItemView(getItemViewType(position));
        ViewHolder holder = new ViewHolder(itemView, getItemViewType(position));
        holder.setItemPosition(position);
        holder.setOnItemClickLister(this);
        onItemBindViewHolder(holder, position);
        parent.addView(itemView);
    }

    /**
     * 视图数据绑定
     *
     * @param holder   视图容器
     * @param position 位置
     */
    protected abstract void onItemBindViewHolder(ViewHolder holder, int position);

    /**
     * 删除item
     *
     * @param position
     */
    public void removeItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 添加item
     *
     * @param item 个体
     */
    public void addItem(T item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        notifyDataSetChanged();
    }

    /**
     * 添加item
     *
     * @param position 位置
     * @param item     个体
     */
    public void addItem(int position, T item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(position, item);
        notifyDataSetChanged();
    }

    /**
     * 添加到首位
     *
     * @param item 个体
     */
    public void addFirst(T item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(0, item);
        notifyDataSetChanged();
    }

    /**
     * 添加到末尾
     *
     * @param item 个体
     */
    public void addLast(T item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        int position = getItemCount() == 0 ? 0 : getItemCount() - 1;
        items.add(position, item);
        notifyDataSetChanged();
    }

    /**
     * @param position 位置
     * @return item
     */
    public T getItem(int position) {
        return items.get(position);
    }

    /**
     * 设置数据
     *
     * @param data 数据
     */
    public void setItems(List<T> data) {
        this.items = data;
        notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View v, int position) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(this, v, position);
        }
    }

    /**
     * Item点击事件
     */
    private OnItemClickListener<T> onItemClickListener;

    /**
     * 设置Item点击事件
     *
     * @param onItemClickListener Item点击事件
     */
    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Item点击事件回调
     *
     * @param <T>
     */
    public interface OnItemClickListener<T> {

        /**
         * Item点击
         *
         * @param adapter  适配器
         * @param v        数据
         * @param position 位置
         */
        void onItemClick(FlowAdapter<T> adapter, View v, int position);

    }

}
