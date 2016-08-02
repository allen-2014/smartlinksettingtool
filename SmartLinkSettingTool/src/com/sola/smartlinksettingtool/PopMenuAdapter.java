package com.sola.smartlinksettingtool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;



public class PopMenuAdapter extends BaseAdapter {

    private static final int INVALID_LAYOUT_ID = -1;

    protected Context context;
    protected int layoutId = INVALID_LAYOUT_ID;
    protected List<PopMenuItem> items;

    public PopMenuAdapter(Context context, int layoutId, PopMenuItem[] items) {
        this.context = context;
        this.layoutId = layoutId;
        this.items = Arrays.asList(items);
    }

    public PopMenuAdapter(Context context, int layoutId, List<PopMenuItem> items) {
        this.context = context;
        this.layoutId = layoutId;
        this.items = items;
    }

    protected int getAdapterLayoutId() {
        return layoutId == INVALID_LAYOUT_ID ? R.layout.pop_menu_item_layout : layoutId;
    }

    public void add(PopMenuItem item) {
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public PopMenuItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getItemId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
        	convertView = LayoutInflater.from(context).inflate(getAdapterLayoutId(), parent, false);
            holder = new ViewHolder();
            holder.prefix = convertView.findViewById(R.id.pop_menu_item_prefix);
            holder.suffix = convertView.findViewById(R.id.pop_menu_item_suffix);
            holder.content = convertView.findViewById(R.id.pop_menu_item_content);
            convertView.setTag(holder);
        } else {
            //view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        PopMenuItem item = getItem(position);
        if (holder.prefix != null) {
            bindPrefix(holder.prefix, position, item);
        }
        if (holder.suffix != null) {
            bindSuffix(holder.suffix, position, item);
        }
        if (holder.content != null) {
            bindContent(holder.content, position, item);
        }
        return convertView;
    }

    protected void bindPrefix(View view, int position, PopMenuItem item) {
        ((ImageView) view).setImageResource(item.getIconId());
    }

    protected void bindSuffix(View view, int position, PopMenuItem item) {
        view.setVisibility(item.isNoticeable() ? View.VISIBLE : View.INVISIBLE);
    }

    protected void bindContent(View view, int position, PopMenuItem item) {
        ((TextView) view).setText(item.getContent());
    }

    private class ViewHolder {
        View prefix;
        View suffix;
        View content;
    }
}
