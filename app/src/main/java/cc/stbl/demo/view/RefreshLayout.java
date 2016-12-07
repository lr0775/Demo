package cc.stbl.demo.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import cc.stbl.demo.R;
import cc.stbl.demo.util.Logger;

/**
 * Created by Administrator on 2016/12/6.
 */

public class RefreshLayout extends ViewGroup {

    private static final int INVALID_COORDINATE = -1;
    private static final int INVALID_POINTER = -1;

    private boolean mRefreshEnabled = true;
    private boolean mLoadMoreEnabled = true;

    private int mTouchSlop;

    private View mHeaderView;
    private View mTargetView;
    private View mFooterView;

    private int mLayoutHeight;
    private int mHeaderWidth;
    private int mHeaderHeight;
    private int mTargetWidth;
    private int mTargetHeight;
    private int mFooterWidth;
    private int mFooterHeight;

    private int mActivePointerId;
    private float mInitDownX;
    private float mInitDownY;
    private float mLastX;
    private float mLastY;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        mHeaderView = findViewById(R.id.swipe_refresh_header);
        mTargetView = findViewById(R.id.swipe_target);
        mFooterView = findViewById(R.id.swipe_load_more_footer);
        if (mTargetView == null) {
            return;
        }
        if (mHeaderView != null) {
            mHeaderView.setVisibility(GONE);
        }
        if (mFooterView != null) {
            mFooterView.setVisibility(GONE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTargetView == null) {
            return;
        }
        mLayoutHeight = getMeasuredHeight();
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderWidth = mHeaderView.getMeasuredWidth();
            mHeaderHeight = mHeaderView.getMeasuredHeight();
        }
        measureChild(mTargetView, widthMeasureSpec, heightMeasureSpec);
        mTargetWidth = mTargetView.getMeasuredWidth();
        mTargetHeight = mTargetView.getMeasuredHeight();
        if (mFooterView != null) {
            measureChild(mFooterView, widthMeasureSpec, heightMeasureSpec);
            mFooterWidth = mFooterView.getMeasuredWidth();
            mFooterHeight = mFooterView.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mTargetView == null) {
            return;
        }
        if (mHeaderView != null) {
            mHeaderView.layout(0, -mHeaderHeight, mHeaderWidth, 0);
        }
        mTargetView.layout(0, 0, mTargetWidth, mTargetHeight);
        if (mFooterView != null) {
            mFooterView.layout(0, mLayoutHeight, mFooterWidth, mLayoutHeight + mFooterHeight);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Logger.e("onInterceptTouchEvent action down");
                mActivePointerId = ev.getPointerId(0);
                mInitDownX = getMotionEventX(ev, mActivePointerId);
                mInitDownY = getMotionEventY(ev, mActivePointerId);
                if (mInitDownX == INVALID_COORDINATE || mInitDownY == INVALID_COORDINATE) {
                    return false;
                }
                mLastX = mInitDownX;
                mLastY = mInitDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                Logger.e("onInterceptTouchEvent action move");
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                float x = getMotionEventX(ev, mActivePointerId);
                float y = getMotionEventY(ev, mActivePointerId);
                float initDiffX = x - mInitDownX;
                float initDiffY = y - mInitDownY;
                mLastX = x;
                mLastY = y;
                boolean moved = Math.abs(initDiffY) > mTouchSlop && Math.abs(initDiffY) > Math.abs(initDiffX);
                boolean triggerCondition = moved && (initDiffY > 0 ? onCheckCanRefresh() : onCheckCanLoadMore());
                return triggerCondition;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                Logger.e("onTouchEvent action move");
                float x = getMotionEventX(ev, mActivePointerId);
                float y = getMotionEventY(ev, mActivePointerId);
                float diffX = x - mLastX;
                float diffY = y - mLastY;
                mLastX = x;
                mLastY = y;
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                Logger.e("onTouchEvent action pointer down");
                onSecondPointerDown(ev);
                mLastX = getMotionEventX(ev, mActivePointerId);
                mLastY = getMotionEventY(ev, mActivePointerId);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Logger.e("onTouchEvent action pointer up");
                onSecondPointerUp(ev);
                mLastX = getMotionEventX(ev, mActivePointerId);
                mLastY = getMotionEventY(ev, mActivePointerId);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER;
                break;
        }
        return super.onTouchEvent(ev);
    }

    private float getMotionEventX(MotionEvent event, int pointerId) {
        int index = event.findPointerIndex(pointerId);
        if (index < 0) {
            return INVALID_COORDINATE;
        }
        return event.getX(pointerId);
    }

    private float getMotionEventY(MotionEvent event, int pointerId) {
        int index = event.findPointerIndex(pointerId);
        if (index < 0) {
            return INVALID_COORDINATE;
        }
        return event.getY(pointerId);
    }

    private void onSecondPointerDown(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId != INVALID_POINTER) {
            mActivePointerId = pointerId;
        }
    }

    private void onSecondPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            int newPointerIndex = (pointerIndex == 0 ? 1 : 0);
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private boolean onCheckCanRefresh() {
        return mRefreshEnabled && !ViewCompat.canScrollVertically(mTargetView, -1);
    }

    private boolean onCheckCanLoadMore() {
        return mLoadMoreEnabled && !ViewCompat.canScrollVertically(mTargetView, 1);
    }
}
