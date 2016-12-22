package cc.stbl.demo.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import cc.stbl.demo.R;

/**
 * Created by Administrator on 2016/12/6.
 * 用onInterceptTouchEvent在targetView是ScrollView时，会有问题，要设置ScrollView的android:overScrollMode="never"才行,
 * SwipeToLoadLayout也有这个问题，PtrFrameLayout没有这个问题。
 */

public class RefreshLayout extends ViewGroup {

    private static final int INVALID_COORDINATE = -1;
    private static final int INVALID_POINTER = -1;

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
    private float mFirstX;
    private float mFirstY;
    private float mLastX;
    private float mLastY;

    private int mAttacheStatus;
    private int[] mActionArray;

    private Scroller mScroller;
    private int mScrollLastY;

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
        mActionArray = new int[2];
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
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mActionArray[0] = MotionEvent.ACTION_DOWN;
                mActionArray[1] = 0;
                mActivePointerId = ev.getPointerId(0);
                mFirstX = getMotionEventX(ev, mActivePointerId);
                mFirstY = getMotionEventY(ev, mActivePointerId);
                if (mFirstX == INVALID_COORDINATE || mFirstY == INVALID_COORDINATE) {
                    return false;
                }
                mLastX = mFirstX;
                mLastY = mFirstY;
                mScroller.forceFinished(true);
                if (mTargetView.getTop() == 0) {
                    mAttacheStatus = -3;
                }
                super.dispatchTouchEvent(ev);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mActionArray[1] == Integer.MAX_VALUE) {
                    return super.dispatchTouchEvent(ev);
                }
                mActionArray[1] = MotionEvent.ACTION_MOVE;
                float x = getMotionEventX(ev, mActivePointerId);
                float y = getMotionEventY(ev, mActivePointerId);
                float diffX = x - mFirstX;
                float diffY = y - mFirstY;
                float offsetX = x - mLastX;
                float offsetY = y - mLastY;
                mLastX = x;
                mLastY = y;
                if (mAttacheStatus == -3) {
                    if (Math.abs(diffX) > mTouchSlop && Math.abs(diffX) > 2 * Math.abs(diffY)) {
                        mAttacheStatus = -2;
                    }
                }
                if (mAttacheStatus == -2) {
                    return super.dispatchTouchEvent(ev);
                }
                if (mAttacheStatus <= 0) {
                    if (Math.abs(diffY) > mTouchSlop) {
                        if (offsetY > 0) {
                            if (onCheckCanRefresh()) {
                                mStatus = 1;
                                mAttacheStatus = 1;
                            } else {
                                mAttacheStatus = -1;
                            }
                        } else if (offsetY < 0) {
                            if (onCheckCanLoadMore()) {
                                mStatus = -1;
                                mAttacheStatus = 1;
                            } else {
                                mAttacheStatus = -1;
                            }
                        }
                    }
                }
                if (mAttacheStatus == 1) {
                    fingerScroll(offsetY);
                    return true;
                }
            }
            break;
            case MotionEvent.ACTION_POINTER_DOWN:
                onSecondPointerDown(ev);
                mLastX = getMotionEventX(ev, mActivePointerId);
                mLastY = getMotionEventY(ev, mActivePointerId);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondPointerUp(ev);
                mLastX = getMotionEventX(ev, mActivePointerId);
                mLastY = getMotionEventY(ev, mActivePointerId);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER;
                if (mAttacheStatus == 1) {
                    onActivePointerUp();
                    MotionEvent e = MotionEvent.obtain(ev.getDownTime(), ev.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL, ev.getX(), ev.getY(), ev.getMetaState());
                    super.dispatchTouchEvent(e);
                    return true;
                }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept && mActionArray[1] == 0) {
            mActionArray[1] = Integer.MAX_VALUE;
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    private void fingerScroll(float offsetY) {
        int top = mTargetView.getTop();
        float ratio = -0.001f * Math.abs(top) + 1;
        int offset = (int) (offsetY * ratio);
        float y = top + offset;
        if ((mStatus > 0 && y <= 0) || (mStatus < 0 && y >= 0)) {
            offset = -top;
            mAttacheStatus = 0;
        }
        updateScroll(offset);
    }

    private float getMotionEventX(MotionEvent event, int pointerId) {
        int index = event.findPointerIndex(pointerId);
        if (index < 0) {
            return INVALID_COORDINATE;
        }
        return event.getX(index);
    }

    private float getMotionEventY(MotionEvent event, int pointerId) {
        int index = event.findPointerIndex(pointerId);
        if (index < 0) {
            return INVALID_COORDINATE;
        }
        return event.getY(index);
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

}
