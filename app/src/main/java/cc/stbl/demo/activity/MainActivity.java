package cc.stbl.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import cc.stbl.demo.R;
import cc.stbl.demo.adapter.MainRecyclerAdapter;
import cc.stbl.demo.constant.KEY;

public class MainActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private MainRecyclerAdapter mAdapter;
    private ArrayList<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setData();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mList = new ArrayList<>();
        mAdapter = new MainRecyclerAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setInterface(new MainRecyclerAdapter.AdapterInterface() {
            @Override
            public void onItemClick(int position, String title) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(mActivity, AlbumActivity.class);
                        intent.putExtra(KEY.TITLE, title);
                        startActivity(intent);
                        break;
                    case 1:
                        startActivity(new Intent(mActivity, SampleSizeActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(mActivity, NoteActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(mActivity, RefreshActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(mActivity, SwipeActivity.class));
                        break;
                }
            }
        });
    }

    private void setData() {
        mList.add("图库");
        mList.add("SampleSize");
        mList.add("Note");
        mList.add("Refresh");
        mList.add("Swipe");
    }
}
