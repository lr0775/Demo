package cc.stbl.demo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import cc.stbl.demo.R;
import cc.stbl.demo.adapter.BannerPagerAdapter;
import cc.stbl.demo.adapter.RefreshListAdapter;
import cc.stbl.demo.util.Logger;
import cc.stbl.demo.util.Toaster;
import cc.stbl.demo.view.RefreshLayout;

public class RefreshListViewActivity extends BaseActivity {

    private RefreshLayout mRefreshLayout;
    private ViewPager mViewPager;
    private ViewPager mViewPager2;
    private BannerPagerAdapter mAdapter;

    private ListViewAutoScrollHelper mScrollHelper;

    private ArrayList<String> mList;
    private ListView mListView;
    private RefreshListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_listview);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refresh_layout);
        View headerView = getLayoutInflater().inflate(R.layout.header_listview, null);
        mViewPager = (ViewPager) headerView.findViewById(R.id.view_pager);
        mViewPager2 = (ViewPager) headerView.findViewById(R.id.view_pager2);
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("http://desk.fd.zol-img.com.cn/t_s1920x1080c5/g5/M00/0F/0D/ChMkJ1gyrq2IQiRcAAxu-yQ2xsMAAX8QQM6NvgADG8T113.jpg?downfile=1483427791612.jpg");
        urlList.add("http://desk.fd.zol-img.com.cn/t_s1920x1080c5/g5/M00/0F/0D/ChMkJ1gyrqOIaPZ0AAl0Kle1WJkAAX8QAFgBIUACXRC781.jpg?downfile=148342783141.jpg");
        urlList.add("http://desk.fd.zol-img.com.cn/t_s1920x1080c5/g5/M00/0F/0D/ChMkJlgyrqSIVRV6AA1iwnVeVuwAAX8QAI627IADWLa770.jpg?downfile=1483427860251.jpg");
        urlList.add("http://desk.fd.zol-img.com.cn/t_s1920x1080c5/g5/M00/0F/0D/ChMkJ1gyrqeIYS6YAAqXOO0nxs0AAX8QAOh3y0ACpdQ842.jpg?downfile=148342788792.jpg");
        urlList.add("http://desk.fd.zol-img.com.cn/t_s1920x1080c5/g5/M00/0F/0D/ChMkJlgyrqqIUs5iAAojZpn5I88AAX8QQIGqBsACiN-149.jpg?downfile=1483427912765.jpg");
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
        mListView = (ListView) findViewById(R.id.content_view);
        mListView.addHeaderView(headerView);
        mList = new ArrayList<>();
        mListAdapter = new RefreshListAdapter(mActivity, mList);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toaster.show("点击了第 " + (position - 1) + " 项");
            }
        });

        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Logger.e("onRefresh");
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new RefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Logger.e("onLoadMore");
            }
        });

        addData();
    }

    private void addData() {
        for (int i = 0; i < 10; i++) {
            mList.add("第 " + i + " 项");
        }
        mListAdapter.notifyDataSetChanged();
    }

}
