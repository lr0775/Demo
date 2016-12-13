package cc.stbl.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cc.stbl.demo.R;
import cc.stbl.demo.view.refresh.OnRefreshListener;
import cc.stbl.demo.view.refresh.SwipeToLoadLayout;

public class SwipeActivity extends AppCompatActivity {

    private SwipeToLoadLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        mLayout = (SwipeToLoadLayout) findViewById(R.id.layout_swipe);
        mLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLayout.setRefreshing(false);
                    }
                }, 300);
            }
        });
    }
}
