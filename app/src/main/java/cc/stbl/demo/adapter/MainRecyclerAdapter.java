package cc.stbl.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cc.stbl.demo.App;
import cc.stbl.demo.R;

/**
 * Created by Administrator on 2016/10/17.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> mList;
    private LayoutInflater mInflater;

    private AdapterInterface mInterface;

    public MainRecyclerAdapter(ArrayList<String> list) {
        mList = list;
        mInflater = LayoutInflater.from(App.getContext());

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(mInflater.inflate(R.layout.item_main_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            MyHolder h = (MyHolder) holder;
            h.bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {

        TextView mTitleTv;

        MyHolder(View itemView) {
            super(itemView);
            mTitleTv = (TextView) itemView.findViewById(R.id.tv_title);
        }

        private void bind(final int position) {
            final String title = mList.get(position);
            mTitleTv.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mInterface != null) {
                        mInterface.onItemClick(position, title);
                    }
                }
            });
        }
    }

    public void setInterface(AdapterInterface i) {
        mInterface = i;
    }

    public interface AdapterInterface {
        void onItemClick(int position, String title);
    }
}
