package cc.stbl.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import cc.stbl.demo.App;
import cc.stbl.demo.R;
import cc.stbl.demo.model.Picture;
import cc.stbl.demo.util.ImageUtils;

/**
 * Created by Administrator on 2016/10/17.
 */

public class AlbumRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Picture> mList;
    private LayoutInflater mInflater;

    private AdapterInterface mInterface;

    public AlbumRecyclerAdapter(ArrayList<Picture> list) {
        mList = list;
        mInflater = LayoutInflater.from(App.getContext());

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(mInflater.inflate(R.layout.item_album_recycler, parent, false));
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

        ImageView mPictureIv;

        MyHolder(View itemView) {
            super(itemView);
            mPictureIv = (ImageView) itemView.findViewById(R.id.iv_picture);
        }

        private void bind(final int position) {
            final Picture item = mList.get(position);
            ImageUtils.load(item.file, mPictureIv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mInterface != null) {
                        mInterface.onItemClick(position, item);
                    }
                }
            });
        }
    }

    public void setInterface(AdapterInterface i) {
        mInterface = i;
    }

    public interface AdapterInterface {
        void onItemClick(int position, Picture item);
    }
}
