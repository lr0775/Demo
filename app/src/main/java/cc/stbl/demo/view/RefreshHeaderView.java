package cc.stbl.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.stbl.demo.R;
import cc.stbl.demo.util.Logger;

/**
 * Created by Administrator on 2017/1/4.
 */

public class RefreshHeaderView extends RelativeLayout implements RefreshLayout.Callback {

    private TextView mTv;
    private ProgressBar mPb;

    public RefreshHeaderView(Context context) {
        this(context, null);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.header_view_refresh, this);

        mTv = (TextView) findViewById(R.id.tv);
        mPb = (ProgressBar) findViewById(R.id.pb);

    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public int getTriggerHeight() {
        return 120;
    }

    @Override
    public void onPrepare() {
        Logger.e("header onPrepare");
        mTv.setText("下拉刷新");
    }

    @Override
    public void onDrag(int y) {
        Logger.e("header onDrag, y = " + y);
        if (y <= 120) {
            //mTv.setText("下拉刷新");
        } else {
            //mTv.setText("释放立即刷新");
        }
    }

    @Override
    public void onRelease() {
        Logger.e("header onRelease");
    }

    @Override
    public void onComplete() {
        Logger.e("header onComplete");
        mTv.setText("刷新成功");
    }

    @Override
    public void onReset() {
        Logger.e("header onReset");
        mTv.setText("下拉刷新");
    }

}
