package cc.stbl.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import cc.stbl.demo.R;
import cc.stbl.demo.adapter.AlbumRecyclerAdapter;
import cc.stbl.demo.constant.KEY;
import cc.stbl.demo.model.Picture;
import cc.stbl.demo.task.AlbumTask;
import cc.stbl.demo.weapon.TaskCallback;
import cc.stbl.demo.weapon.TaskError;

/**
 * Created by Administrator on 2016/10/17.
 */

public class AlbumActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private AlbumRecyclerAdapter mAdapter;
    private ArrayList<Picture> mList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra(KEY.TITLE));
        setContentView(R.layout.activity_album);
        initView();
        getAllPicture();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mList = new ArrayList<>();
        mAdapter = new AlbumRecyclerAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setInterface(new AlbumRecyclerAdapter.AdapterInterface() {
            @Override
            public void onItemClick(int position, Picture item) {
                switch (position) {
                    case 0:

                        break;
                }
            }
        });
    }

    private void getAllPicture() {
        mTaskManager.start(AlbumTask.getAllPicture()
                .setCallback(new TaskCallback<ArrayList<Picture>>() {
                    @Override
                    public void onError(TaskError e) {
                        Toast.makeText(mActivity, e.msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ArrayList<Picture> result) {
                        mList.clear();
                        mList.addAll(result);
                        mAdapter.notifyDataSetChanged();
                    }
                }));
    }
}
