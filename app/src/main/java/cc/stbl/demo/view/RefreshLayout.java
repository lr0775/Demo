package cc.stbl.demo.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.util.ArrayList;

import cc.stbl.demo.R;

/**
 * Created by Administrator on 2016/12/6.
 * 用onInterceptTouchEvent在targetView是ScrollView时，会有问题，要设置ScrollView的android:overScrollMode="never"才行,
 * SwipeToLoadLayout也有这个问题，PtrFrameLayout没有这个问题。
 */

public class RefreshLayout extends ViewGroup {

    private boolean mRefreshEnabled = true;
    private boolean mLoadMoreEnabled = true;

    private int mStatus;

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
    private float mInitialMotionX;
    private float mInitialMotionY;
    private float mLastMotionX;
    private float mLastMotionY;
    private boolean mAttached = true;

    private Scroller mScroller;
    private int mScrollLastY;

    private ArrayList<InnerPageChangeListener> mListenerList;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context, new DecelerateInterpolator());
        mListenerList = new ArrayList<>();
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isHorizontalScroll()) {
            return super.dispatchTouchEvent(ev);
        }
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mLastMotionX = mInitialMotionX = ev.getX();
                mLastMotionY = mInitialMotionY = ev.getY();
                mActivePointerId = ev.getPointerId(0);

                mScroller.forceFinished(true);
                if (mTargetView.getTop() == 0) {
                    mAttached = true;
                }
                super.dispatchTouchEvent(ev);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                final int activePointerId = mActivePointerId;
                if (activePointerId == -1) {
                    // If we don't have a valid id, the touch down wasn't on content.
                    break;
                }
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    // A child has consumed some touch events and put us into an inconsistent
                    // state.
                    break;
                }
                final float x = ev.getX(pointerIndex);
                final float xDiff = x - mLastMotionX;
                final float xDistance = x - mInitialMotionX;
                final float y = ev.getY(pointerIndex);
                final float yDiff = y - mLastMotionY;
                final float yDistance = y - mInitialMotionY;
                mLastMotionX = x;
                mLastMotionY = y;

                if (mAttached) {
                    if (Math.abs(yDistance) > mTouchSlop && (!(Math.abs(xDistance) * 0.5f > Math.abs(yDistance)))) {
                        if (yDiff > 0) {
                            if (onCheckCanRefresh()) {
                                mStatus = 1;
                                mAttached = false;
                            }
                        } else {
                            if (onCheckCanLoadMore()) {
                                mStatus = -1;
                                mAttached = false;
                            }
                        }
                    }
                }
                if (!mAttached) {
                    int top = mTargetView.getTop();
                    float ratio = -0.001f * Math.abs(top) + 1;
                    int offset = (int) (yDiff * ratio);
                    float coorY = top + offset;
                    if ((mStatus > 0 && coorY <= 0) || (mStatus < 0 && coorY >= 0)) {
                        offset = -top;
                        mAttached = true;
                    }
                    updateScroll(offset);
                    if (mAttached) {
                        ev.setAction(MotionEvent.ACTION_DOWN);
                        return super.dispatchTouchEvent(ev);
                    }
                    return true;
                }
            }
            break;
            case MotionEvent.ACTION_POINTER_DOWN:
                final int index = MotionEventCompat.getActionIndex(ev);
                final float x = ev.getX(index);
                final float y = ev.getY(index);
                mLastMotionX = x;
                mLastMotionY = y;
                mActivePointerId = ev.getPointerId(index);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastMotionX = ev.getX(newPointerIndex);
                    mLastMotionY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                mLastMotionX = ev.getX(ev.findPointerIndex(mActivePointerId));
                mLastMotionY = ev.getY(ev.findPointerIndex(mActivePointerId));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = -1;
                if (!mAttached) {
                    onActivePointerUp();
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean onCheckCanRefresh() {
        return mRefreshEnabled && !ViewCompat.canScrollVertically(mTargetView, -1);
    }

    private boolean onCheckCanLoadMore() {
        if (mTargetView instanceof ViewGroup) {
            ViewGroup layout = (ViewGroup) mTargetView;
            int childCount = layout.getChildCount();
            if (childCount == 0) {
                return false;
            }
            View child = layout.getChildAt(childCount - 1);
            if (child.getBottom() < mTargetView.getHeight() - mTargetView.getPaddingBottom()) {
                return false;
            }
        }
        return mLoadMoreEnabled && !ViewCompat.canScrollVertically(mTargetView, 1);
    }

    private void onActivePointerUp() {
        int top = mTargetView.getTop();
        mScrollLastY = 0;
        mScroller.startScroll(0, 0, 0, -top, 300);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!mScroller.computeScrollOffset()) {
            return;
        }
        int currY = mScroller.getCurrY();
        int offset = currY - mScrollLastY;
        mScrollLastY = currY;
        updateScroll(offset);
    }

    private void updateScroll(int offset) {
        if (mStatus > 0) {
            mHeaderView.offsetTopAndBottom(offset);
        } else if (mStatus < 0) {
            mFooterView.offsetTopAndBottom(offset);
        }
        mTargetView.offsetTopAndBottom(offset);
        invalidate();
    }

    public void addViewPagerListener(ViewPager... pagers) {
        for (ViewPager vp : pagers) {
            InnerPageChangeListener listener = new InnerPageChangeListener();
            vp.addOnPageChangeListener(listener);
            mListenerList.add(listener);
        }
    }

    private boolean isHorizontalScroll() {
        for (InnerPageChangeListener listener : mListenerList) {
            if (!listener.mIsIdle) {
                return true;
            }
        }
        return false;
    }

    private static class InnerPageChangeListener implements ViewPager.OnPageChangeListener {

        public boolean mIsIdle = true;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mIsIdle = (state == ViewPager.SCROLL_STATE_IDLE);
        }
    }

}
