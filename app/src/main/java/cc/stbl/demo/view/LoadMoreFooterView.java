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

public class LoadMoreFooterView extends RelativeLayout implements RefreshLayout.Callback {

    private TextView mTv;
    private ProgressBar mPb;

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
    public View getView() {
        return this;
    }

    @Override
    public int getTriggerHeight() {
        return 120;
    }

    @Override
    public void onPrepare() {
        Logger.e("footer onPrepare");
    }

    @Override
    public void onDrag(int y) {
        Logger.e("footer onDrag, y = " + y);
        if (y <= 120) {
            //mTv.setText("上拉加载更多");
        } else {
            // mTv.setText("释放立即加载");
        }
    }

    @Override
    public void onRelease() {
        Logger.e("footer onRelease");
    }

    @Override
    public void onComplete() {
        Logger.e("footer onComplete");
    }

    @Override
    public void onReset() {
        Logger.e("footer onReset");
    }

}
