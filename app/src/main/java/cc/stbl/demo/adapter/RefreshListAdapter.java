package cc.stbl.demo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cc.stbl.demo.R;

/**
 * Created by Administrator on 2017/1/12.
 */

public class RefreshListAdapter extends BaseAdapter {

    private ArrayList<String> mList;
    private LayoutInflater mInflater;
    private Activity mActivity;

    public RefreshListAdapter(Activity activity, ArrayList<String> list) {
        mActivity = activity;
        mList = list;
        mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_refresh, parent, false);
            holder.mTv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTv.setText(mList.get(position));
        return convertView;
    }

    private class ViewHolder {
        TextView mTv;
    }
}
