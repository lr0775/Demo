package cc.stbl.demo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

import cc.stbl.demo.R;
import cc.stbl.demo.adapter.BannerPagerAdapter;
import cc.stbl.demo.util.Toaster;
import cc.stbl.demo.view.refresh.OnRefreshListener;
import cc.stbl.demo.view.refresh.SwipeToLoadLayout;

public class SwipeActivity extends AppCompatActivity {

    private SwipeToLoadLayout mLayout;
    private ViewPager mViewPager;
    private ViewPager mViewPager2;
    private BannerPagerAdapter mAdapter;

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
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager2 = (ViewPager) findViewById(R.id.view_pager2);
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("http://img1.gamersky.com/image2016/11/20161116_lyx_285_13/gamersky_01origin_01_2016111618178C7.jpg");
        urlList.add("http://img1.gamersky.com/image2016/11/20161116_lyx_285_13/gamersky_02origin_03_201611161817222.jpg");
        urlList.add("http://img1.gamersky.com/image2016/12/20161210_zl_91_3/gamersky_01origin_01_201612101716868.jpg");
        urlList.add("http://img1.gamersky.com/image2016/12/20161210_zl_91_3/gamersky_06origin_11_201612101716587.jpg");
        urlList.add("http://img1.gamersky.com/image2016/12/20161210_zl_91_3/gamersky_08origin_15_201612101716396.jpg");
        mAdapter = new BannerPagerAdapter(urlList);
        mViewPager.setAdapter(mAdapter);
        ArrayList<String> urlList2 = new ArrayList<>();
        urlList2.addAll(urlList);
        Collections.reverse(urlList2);
        mViewPager2.setAdapter(new BannerPagerAdapter(urlList2));
        mAdapter.setOnItemClickListener(new BannerPagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toaster.show("点击第" + position + "项");
            }
        });
    }
}
