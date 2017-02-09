package cc.stbl.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.stbl.demo.R;

/**
 * Created by Administrator on 2017/1/4.
 */

public class LoadMoreFooterView extends RelativeLayout implements RefreshLayout.UpdateViewCallback {

    private TextView mTv;
    private ProgressBar mPb;

    private int mStatus = Integer.MIN_VALUE;

    private boolean mTrigger;

    public LoadMoreFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.footer_view_load_more, this);
        mTv = (TextView) findViewById(R.id.tv);
        mPb = (ProgressBar) findViewById(R.id.pb);
    }

    @Override
    public void setStatus(int status) {
        if (mStatus != status) {
            switch (status) {
                case -1:
                    mTv.setText("上拉加载更多");
                    mPb.setVisibility(INVISIBLE);
                    break;
                case -2:
                    mTv.setText("加载中...");
                    mPb.setVisibility(VISIBLE);
                    break;
                case -3:
                    mTv.setText("加载完成");
                    mPb.setVisibility(INVISIBLE);
                    break;
            }
            mStatus = status;
        }
    }

    @Override
    public void onTopChange(int top) {
        if (!mTrigger) {
            if (top < -getMeasuredHeight()) {
                mTrigger = true;
                mTv.setText("释放立即加载");
            }
        } else {
            if (top >= -getMeasuredHeight()) {
                mTrigger = false;
                mTv.setText("上拉加载更多");
            }
        }
    }
}
