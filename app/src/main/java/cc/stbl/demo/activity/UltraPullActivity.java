package cc.stbl.demo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import cc.stbl.demo.R;
import cc.stbl.demo.adapter.BannerPagerAdapter;
import cc.stbl.demo.util.Toaster;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class UltraPullActivity extends AppCompatActivity {

    private PtrFrameLayout mPtrLayout;
    private ViewPager mViewPager;
    private BannerPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultra_pull);

        mPtrLayout = (PtrFrameLayout) findViewById(R.id.layout_ptr);
        mPtrLayout.disableWhenHorizontalMove(false);
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrLayout.refreshComplete();
                    }
                }, 5000);
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("http://desk.fd.zol-img.com.cn/t_s1920x1080c5/g5/M00/0F/0D/ChMkJ1gyrq2IQiRcAAxu-yQ2xsMAAX8QQM6NvgADG8T113.jpg?downfile=1483427791612.jpg");
        urlList.add("http://desk.fd.zol-img.com.cn/t_s1920x1080c5/g5/M00/0F/0D/ChMkJ1gyrqOIaPZ0AAl0Kle1WJkAAX8QAFgBIUACXRC781.jpg?downfile=148342783141.jpg");
        urlList.add("http://desk.fd.zol-img.com.cn/t_s1920x1080c5/g5/M00/0F/0D/ChMkJlgyrqSIVRV6AA1iwnVeVuwAAX8QAI627IADWLa770.jpg?downfile=1483427860251.jpg");
        urlList.add("http://desk.fd.zol-img.com.cn/t_s1920x1080c5/g5/M00/0F/0D/ChMkJ1gyrqeIYS6YAAqXOO0nxs0AAX8QAOh3y0ACpdQ842.jpg?downfile=148342788792.jpg");
        urlList.add("http://desk.fd.zol-img.com.cn/t_s1920x1080c5/g5/M00/0F/0D/ChMkJlgyrqqIUs5iAAojZpn5I88AAX8QQIGqBsACiN-149.jpg?downfile=1483427912765.jpg");
        mAdapter = new BannerPagerAdapter(urlList);
        mViewPager.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BannerPagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toaster.show("点击第" + position + "项");
            }
        });

        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toaster.show("点击了第一项");
            }
        });
    }
}
