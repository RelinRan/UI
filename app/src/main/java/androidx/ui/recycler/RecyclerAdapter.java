package androidx.ui.recycler;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.ui.util.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Recycler使用的基础Adapter
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter implements ViewHolder.OnItemClickLister, ViewHolder.OnItemFocusChangeListener {

    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 数据对象
     */
    private List<T> data;
    /**
     * 空视图
     */
    private View placeholder;
    /**
     * View容器
     */
    private ViewHolder viewHolder;

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    /**
     * 获取Item布局资源
     *
     * @return
     */
    protected abstract int getItemLayoutResId(int viewType);

    /**
     * 获取Item布局视图
     *
     * @param parent   父级
     * @param viewType 类型
     * @return
     */
    protected View getItemView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(getContext()).inflate(getItemLayoutResId(viewType), parent, false);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(getItemView(parent, viewType));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        viewHolder = (ViewHolder) vh;
        viewHolder.setItemPosition(position);
        viewHolder.setOnItemClickLister(this);
        viewHolder.setOnItemFocusChangeListener(this);
        onItemBindViewHolder(viewHolder, position);
    }

    /**
     * 绑定数据
     *
     * @param holder   控件容器
     * @param position 位置
     */
    protected abstract void onItemBindViewHolder(ViewHolder holder, int position);

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int itemCount = Size.of(data);
        if (placeholder != null) {
            placeholder.setVisibility(itemCount == 0 ? View.VISIBLE : View.GONE);
        }
        return itemCount;
    }

    /**
     * @param id 颜色Id
     * @return 颜色
     */
    public int getColor(@ColorRes int id) {
        return getContext().getResources().getColor(id);
    }

    /**
     * @param color 颜色字符
     * @return 颜色
     */
    public int parseColor(String color) {
        return Color.parseColor(color);
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
     * 设置数据源
     *
     * @param data
     */
    public void setItems(List<T> data) {
        setItems(data, true);
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setItems(List<T> data, boolean notify) {
        this.data = data;
        if (placeholder != null) {
            placeholder.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
        if (notify) {
            notifyDataSetChanged();
        }
    }

    /**
     * 设置分页数据
     *
     * @param page 页面
     * @param data 数据
     */
    public void setPageItems(int page, List<T> data) {
        setPageItems(1, page, data);
    }

    /**
     * 设置分页数据
     *
     * @param init 初始页
     * @param page 当前页
     * @param data 数据
     */
    public void setPageItems(int init, int page, List<T> data) {
        if (page == init) {
            setItems(data);
        } else {
            addItems(data);
        }
    }

    /**
     * 添加Items
     *
     * @param data
     */
    public void addItems(List<T> data) {
        int size = data == null ? 0 : data.size();
        int positionStart = getItemCount();
        getItems().addAll(data);
        if (size > 0) {
            notifyItemRangeInserted(positionStart, size);
            notifyItemRangeChanged(positionStart, size);
        }
    }

    /**
     * 添加Item
     *
     * @param t
     */
    public void addItem(T t) {
        int positionStart = getItemCount();
        if (t != null) {
            getItems().add(t);
        }
        notifyItemInserted(positionStart);
        notifyItemRangeChanged(positionStart, 1);
    }

    /**
     * 添加item
     *
     * @param position 位置
     * @param item     实体
     */
    public void addItem(int position, T item) {
        if (item != null) {
            getItems().add(position, item);
            notifyItemInserted(position);
            notifyItemRangeChanged(position, getItemCount() - position);
        }
    }

    /**
     * 首位添加
     *
     * @param item 实体
     */
    public void addFirst(T item) {
        if (getItemCount() == 0) {
            addItem(item);
        } else {
            addItem(0, item);
        }
    }

    /**
     * 添加到末尾
     *
     * @param item 实体
     */
    public void addLast(T item) {
        if (getItemCount() == 0) {
            addItem(item);
        } else {
            int index = getItemCount();
            addItem(index, item);
        }
    }

    /**
     * 删除Item
     *
     * @param position 位置
     */
    public void removeItem(int position) {
        int count = getItemCount();
        if (count > 0 && position < count) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, count - position);
        }
    }

    /**
     * 删除Item
     *
     * @param positionStart 开始位置
     * @param itemCount     个数
     */
    public void removeItems(int positionStart, int itemCount) {
        int count = getItemCount();
        if (count > 0) {
            data.removeAll(data.subList(positionStart, itemCount));
            notifyItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    /**
     * 移动Item
     *
     * @param fromPosition 开始位置
     * @param toPosition   目标位置
     */
    public void swapItem(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        notifyItemRangeChanged(0, getItemCount());
    }

    /**
     * 获取数据
     *
     * @return
     */
    public List<T> getItems() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    /**
     * 获取Item
     *
     * @param position 位置
     * @return
     */
    public T getItem(int position) {
        if (getItemCount() == 0) {
            return null;
        }
        if (data == null) {
            return null;
        }
        return data.get(position);
    }

    /**
     * 获取空视图
     *
     * @return
     */
    public View getPlaceholder() {
        return placeholder;
    }

    /**
     * 设置空视图
     *
     * @param placeholder 视图
     */
    public void setPlaceholder(View placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public void onItemClick(View v, int position) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(this, v, position);
        }
    }

    @Override
    public void onItemFocusChange(View v, int position, boolean hasFocus) {
        if (onItemFocusChangeListener != null) {
            onItemFocusChangeListener.onItemFocusChange(this, v, position, hasFocus);
        }
    }

    /**
     * Item点击事件
     */
    private OnItemClickListener<T> onItemClickListener;

    /**
     * 设置Item点击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 获取Item点击事件
     *
     * @return
     */
    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
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
        void onItemClick(RecyclerAdapter<T> adapter, View v, int position);

    }

    /**
     * 设置焦点改变点击事件
     */
    public OnItemFocusChangeListener<T> onItemFocusChangeListener;

    /**
     * 获取焦点改变事件
     *
     * @return
     */
    public OnItemFocusChangeListener<T> getOnItemFocusChangeListener() {
        return onItemFocusChangeListener;
    }

    /**
     * 获取焦点改变事件
     *
     * @param onItemFocusChangeListener
     */
    public void setOnItemFocusChangeListener(OnItemFocusChangeListener<T> onItemFocusChangeListener) {
        this.onItemFocusChangeListener = onItemFocusChangeListener;
    }

    /**
     * 焦点改变事件
     *
     * @param <T>
     */
    public interface OnItemFocusChangeListener<T> {

        /**
         * 焦点修改
         *
         * @param adapter  适配器
         * @param v        控件
         * @param position 位置
         * @param hasFocus 是否获取焦点
         */
        void onItemFocusChange(RecyclerAdapter<T> adapter, View v, int position, boolean hasFocus);

    }

}
