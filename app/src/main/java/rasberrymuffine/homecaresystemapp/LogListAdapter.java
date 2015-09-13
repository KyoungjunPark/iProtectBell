package rasberrymuffine.homecaresystemapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 예림 on 2015-09-13.
 */
public class LogListAdapter extends BaseAdapter {
    private Context mContext;

    private List<LogItem> mItems = new ArrayList<LogItem>();

    public LogListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(LogItem it) {
        mItems.add(it);
    }

    public void setListItems(List<LogItem> lit) {
        mItems = lit;
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int position) {
        return mItems.get(position);
    }

    public boolean areAllItemsSelectable() {
        return false;
    }

    public boolean isSelectable(int position) {
        try {
            return mItems.get(position).isSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LogItemView itemView;
        if(convertView == null) {
            itemView = new LogItemView(mContext, mItems.get(position));
        }
        else {
            itemView = (LogItemView) convertView;
        }

        itemView.setIcon(mItems.get(position).getIcon());
        itemView.setText(0, mItems.get(position).getData(0));
        itemView.setText(1, mItems.get(position).getData(1));
        itemView.setText(2, mItems.get(position).getData(2));

        return itemView;
    }
}
