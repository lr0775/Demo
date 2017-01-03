package cc.stbl.demo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.Collections;

import cc.stbl.demo.R;
import cc.stbl.demo.adapter.BannerPagerAdapter;
import cc.stbl.demo.util.Toaster;
import cc.stbl.demo.view.RefreshLayout;

public class RefreshActivity extends BaseActivity {

    private RefreshLayout mRefreshLayout;
    private ViewPager mViewPager;
    private ViewPager mViewPager2;
    private BannerPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refresh_layout);
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
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mRefreshLayout.setVeritcalScrollEnabled(1, state == ViewPager.SCROLL_STATE_IDLE);
            }
        });
        mViewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mRefreshLayout.setVeritcalScrollEnabled(2, state == ViewPager.SCROLL_STATE_IDLE);
            }
        });
    }

}
