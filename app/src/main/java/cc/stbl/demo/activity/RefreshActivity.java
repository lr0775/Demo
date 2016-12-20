package cc.stbl.demo.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

import cc.stbl.demo.R;
import cc.stbl.demo.util.ImageUtils;
import cc.stbl.demo.util.Toaster;

public class RefreshActivity extends BaseActivity {

    private ViewPager mViewPager;
    private ViewPager mViewPager2;
    private BannerPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
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

    public static class BannerPagerAdapter extends PagerAdapter {

        private ArrayList<String> mList;
        private OnItemClickListener onItemClickListener;

        public BannerPagerAdapter(ArrayList<String> bannerList) {
            mList = bannerList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageUtils.load(mList.get(position), view);
            container.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            onItemClickListener = listener;
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }

    }

}
