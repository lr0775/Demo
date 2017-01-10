package cc.stbl.demo.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
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

    private OnRefreshListener mRefreshListener;
    private OnLoadMoreListener mLoadMoreListener;

    private int mStatus;

    private int mTouchSlop;

    private Callback mHeaderView;
    private View mContentView;
    private Callback mFooterView;

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
    private boolean mAttached = true;

    private SparseBooleanArray mHorizontalMap;

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
        mHorizontalMap = new SparseBooleanArray();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        mHeaderView = (Callback) findViewById(R.id.refresh_header_view);
        mContentView = findViewById(R.id.content_view);
        mFooterView = (Callback) findViewById(R.id.load_more_footer_view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mContentView == null) {
            return;
        }
        mLayoutHeight = getMeasuredHeight();
        if (mHeaderView != null) {
            measureChild(mHeaderView.getView(), widthMeasureSpec, heightMeasureSpec);
            mHeaderWidth = mHeaderView.getView().getMeasuredWidth();
            mHeaderHeight = mHeaderView.getView().getMeasuredHeight();
        }
        measureChild(mContentView, widthMeasureSpec, heightMeasureSpec);
        mTargetWidth = mContentView.getMeasuredWidth();
        mTargetHeight = mContentView.getMeasuredHeight();
        if (mFooterView != null) {
            measureChild(mFooterView.getView(), widthMeasureSpec, heightMeasureSpec);
            mFooterWidth = mFooterView.getView().getMeasuredWidth();
            mFooterHeight = mFooterView.getView().getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mContentView == null) {
            return;
        }
        if (mHeaderView != null) {
            mHeaderView.getView().layout(0, -mHeaderHeight, mHeaderWidth, 0);
        }
        mContentView.layout(0, 0, mTargetWidth, mTargetHeight);
        if (mFooterView != null) {
            mFooterView.getView().layout(0, mLayoutHeight, mFooterWidth, mLayoutHeight + mFooterHeight);
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
                mActivePointerId = ev.getPointerId(0);
                mFirstX = getMotionEventX(ev, mActivePointerId);
                mFirstY = getMotionEventY(ev, mActivePointerId);
                if (mFirstX == INVALID_COORDINATE || mFirstY == INVALID_COORDINATE) {
                    return false;
                }
                mLastX = mFirstX;
                mLastY = mFirstY;
                mScroller.forceFinished(true);
                if (mContentView.getTop() == 0) {
                    mAttached = true;
                }
                super.dispatchTouchEvent(ev);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                float x = getMotionEventX(ev, mActivePointerId);
                float y = getMotionEventY(ev, mActivePointerId);
                float diffX = x - mFirstX;
                float diffY = y - mFirstY;
                float offsetX = x - mLastX;
                float offsetY = y - mLastY;
                mLastX = x;
                mLastY = y;
                if (mAttached) {
                    if (Math.abs(diffY) > mTouchSlop && (!(Math.abs(diffX) * 0.5f > Math.abs(diffY)))) {
                        if (offsetY > 0) {
                            if (onCheckCanRefresh()) {
                                mStatus = 1;
                                mAttached = false;
                                mHeaderView.onPrepare();
                            }
                        } else {
                            if (onCheckCanLoadMore()) {
                                mStatus = -1;
                                mAttached = false;
                                mFooterView.onPrepare();
                            }
                        }
                    }
                }
                if (!mAttached) {
                    int top = mContentView.getTop();
                    if (mStatus > 0) {
                        mHeaderView.onDrag(top);
                    } else if (mStatus < 0) {
                        mFooterView.onDrag(-top);
                    }
                    float ratio = -0.002f * Math.abs(top) + 1;
                    int offset = (int) (offsetY * ratio);
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
                onSecondPointerDown(ev);
                mLastX = getMotionEventX(ev, mActivePointerId);
                mLastY = getMotionEventY(ev, mActivePointerId);
                if (!mAttached) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondPointerUp(ev);
                mLastX = getMotionEventX(ev, mActivePointerId);
                mLastY = getMotionEventY(ev, mActivePointerId);
                if (!mAttached) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER;
                if (!mAttached) {
                    onActivePointerUp();
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.dispatchTouchEvent(ev);
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
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
        return mRefreshEnabled && !ViewCompat.canScrollVertically(mContentView, -1);
    }

    private boolean onCheckCanLoadMore() {
        if (mContentView instanceof ViewGroup) {
            ViewGroup layout = (ViewGroup) mContentView;
            int childCount = layout.getChildCount();
            if (childCount == 0) {
                return false;
            }
            View child = layout.getChildAt(childCount - 1);
            if (child.getBottom() < mContentView.getHeight() - mContentView.getPaddingBottom()) {
                return false;
            }
        }
        return mLoadMoreEnabled && !ViewCompat.canScrollVertically(mContentView, 1);
    }

    private void onActivePointerUp() {
        int top = mContentView.getTop();
        if (mStatus > 0) {
            mHeaderView.onRelease();
            if (top > mHeaderView.getTriggerHeight()) {
                top -= mHeaderView.getTriggerHeight();
            }
        } else if (mStatus < 0) {
            mFooterView.onRelease();
            if (top < -mFooterView.getTriggerHeight()) {
                top += mFooterView.getTriggerHeight();
            }
        }
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
        if (mStatus > 0 && mContentView.getTop() == mHeaderView.getTriggerHeight()) {
            if (mRefreshListener != null) {
                mRefreshListener.onRefresh();
            }
        } else if (mStatus < 0 && mContentView.getTop() == -mFooterView.getTriggerHeight()) {
            if (mLoadMoreListener != null) {
                mLoadMoreListener.onLoadMore();
            }
        }
    }

    private void updateScroll(int offset) {
        if (mStatus > 0) {
            mHeaderView.getView().offsetTopAndBottom(offset);
        } else if (mStatus < 0) {
            mFooterView.getView().offsetTopAndBottom(offset);
        }
        mContentView.offsetTopAndBottom(offset);
        invalidate();
        if (mContentView.getTop() == 0) {
            if (mStatus > 0) {
                mHeaderView.onReset();
            } else if (mStatus < 0) {
                mFooterView.onReset();
            }
        }
    }

    private boolean isHorizontalScroll() {
        for (int i = 0; i < mHorizontalMap.size(); i++) {
            if (!mHorizontalMap.valueAt(i)) {
                return true;
            }
        }
        return false;
    }

    public void setVeritcalScrollEnabled(int key, boolean enabled) {
        mHorizontalMap.put(key, enabled);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshListener = listener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public void autoRefresh() {
        mScrollLastY = 0;
        mScroller.startScroll(0, 0, 0, 120, 300);
        invalidate();
    }

    public void completeRefresh() {
        mScrollLastY = 0;
        mScroller.startScroll(0, 0, 0, -120, 300);
        invalidate();
    }

    //--------------外部要实现的接口----------------------------------------------------------------------------------

    public void autoLoadMore() {
        mScrollLastY = 0;
        mScroller.startScroll(0, 0, 0, -120, 300);
        invalidate();
    }

    //---------------外部调用方法------------------------------------------------------------------------

    public void completeLoadMore() {


        mScrollLastY = 0;
        mScroller.startScroll(0, 0, 0, 120, 300);
        invalidate();
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface Callback {

        View getView();

        int getTriggerHeight();

        void onPrepare();

        void onDrag(int y);

        void onRelease();

        void onComplete();

        void onReset();
    }

}
