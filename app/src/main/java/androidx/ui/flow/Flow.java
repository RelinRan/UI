package androidx.ui.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.ui.R;
import androidx.ui.recycler.ViewHolder;


import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局View
 */
public class Flow extends FlowLayout {

    private int itemCount;
    private int listItem;
    private DefAdapter adapter;

    public Flow(Context context) {
        super(context);
        initAttributeSet(context, null);
    }

    public Flow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(context, attrs);
    }

    public Flow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
    }

    /**
     * 设置数据适配器
     *
     * @param adapter 适配器
     */
    public void setAdapter(FlowAdapter adapter) {
        adapter.setFlowLayout(this);
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化属性
     *
     * @param context 上下文
     * @param attrs   属性
     */
    protected void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Flow);
            itemCount = array.getInt(R.styleable.Flow_itemCount, 0);
            listItem = array.getResourceId(R.styleable.Flow_listItem, -1);
            initDefAdapter();
            array.recycle();
        }
    }

    /**
     * 初始化默认Adapter
     */
    protected void initDefAdapter() {
        if (listItem != -1) {
            adapter = new DefAdapter(getContext(), listItem);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < itemCount; i++) {
                list.add("");
            }
            adapter.setItems(list);
            setAdapter(adapter);
        }
    }

    private class DefAdapter extends FlowAdapter {

        private int layout;

        public DefAdapter(Context context, int layout) {
            super(context);
            this.layout = layout;
        }

        @Override
        protected int getItemLayoutResId(int viewType) {
            return layout;
        }

        @Override
        protected void onItemBindViewHolder(ViewHolder holder, int position) {

        }
    }

}