package net.lucode.hackware.magicindicator;

import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.util.SparseBooleanArray;

/**
 * 方便扩展IPagerNavigator的帮助类，将ViewPager的3个回调方法转换成
 * onSelected、onDeselected、onEnter等回调，方便扩展
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class NavigatorHelper {
    private int mCurrentIndex;
    private int mTotalCount;
    private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

    // 转换后的回调
    private OnNavigatorScrollListener mNavigatorScrollListener;

    // 记录有哪些item处于未选中、未完全leave状态
    private SparseBooleanArray mDeselectedItems = new SparseBooleanArray();
    private SparseArray<Float> mLeavedPercents = new SparseArray<Float>();

    private float mLastPositionOffsetSum;
    private int mLastIndex;
    private boolean mSkimOver;

    public NavigatorHelper() {
    }

    public int getSafeIndex(int index) {
        return Math.max(Math.min(index, mTotalCount - 1), 0);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mNavigatorScrollListener != null) {
            float currentPositionOffsetSum = position + positionOffset;
            boolean leftToRight = currentPositionOffsetSum >= mLastPositionOffsetSum;
            int safePosition = getSafeIndex(position);
            if (mScrollState != ViewPager.SCROLL_STATE_IDLE) {
                int enterIndex;
                int leaveIndex;
                float enterPercent;
                float leavePercent;
                if (leftToRight) {
                    enterIndex = getSafeIndex(position + 1);
                    enterPercent = positionOffset;
                    leaveIndex = safePosition;
                    leavePercent = positionOffset;
                } else {
                    enterIndex = safePosition;
                    enterPercent = 1.0f - positionOffset;
                    leaveIndex = getSafeIndex(safePosition + 1);
                    leavePercent = 1.0f - positionOffset;
                }
                for (int i = 0, j = mTotalCount; i < j; i++) {
                    if (i == enterIndex || i == leaveIndex) {
                        continue;
                    }
                    Float leavedPercent = mLeavedPercents.get(i, 0.0f);
                    if (leavedPercent != 1.0f) {
                        mNavigatorScrollListener.onLeave(i, mTotalCount, 1.0f, leftToRight);
                        mLeavedPercents.put(i, 1.0f);
                    }
                }
                if (enterIndex == leaveIndex && (mSkimOver || mScrollState == ViewPager.SCROLL_STATE_DRAGGING || (enterIndex == mCurrentIndex || enterIndex == mLastIndex))) {
                    if (enterIndex == mTotalCount - 1 && mLeavedPercents.get(enterIndex) != 0.0f && enterPercent == 0.0f && leftToRight) {
                        mNavigatorScrollListener.onEnter(enterIndex, mTotalCount, 1.0f, true);
                        mLeavedPercents.put(enterIndex, 0.0f);
                    }
                    return;
                }
                if ((1.0f - mLeavedPercents.get(enterIndex, 0.0f) != enterPercent) && (mSkimOver || mScrollState == ViewPager.SCROLL_STATE_DRAGGING || (enterIndex == mCurrentIndex || enterIndex == mLastIndex))) {
                    mNavigatorScrollListener.onEnter(enterIndex, mTotalCount, enterPercent, leftToRight);
                    mLeavedPercents.put(enterIndex, 1.0f - enterPercent);
                }
                if (mLeavedPercents.get(leaveIndex, 0.0f) != leavePercent && (mSkimOver || mScrollState == ViewPager.SCROLL_STATE_DRAGGING || (leaveIndex == mCurrentIndex || leaveIndex == mLastIndex))) {
                    if (leftToRight && leaveIndex == getCurrentIndex() && leavePercent == 0.0f) {
                        mNavigatorScrollListener.onEnter(leaveIndex, mTotalCount, 1.0f, true);
                    } else {
                        mNavigatorScrollListener.onLeave(leaveIndex, mTotalCount, leavePercent, leftToRight);
                    }
                    mLeavedPercents.put(leaveIndex, leavePercent);
                }
            } else {    // 在IDLE状态下收到了onPageScrolled回调，表示完全滚动到了某一页
                mLastIndex = mCurrentIndex;
                mCurrentIndex = safePosition;
                for (int i = 0, j = mTotalCount; i < j; i++) {
                    if (i == safePosition) {
                        continue;
                    }
                    boolean deselected = mDeselectedItems.get(i);
                    if (!deselected) {
                        mNavigatorScrollListener.onDeselected(i, mTotalCount);
                        mDeselectedItems.put(i, true);
                    }
                    Float leavedPercent = mLeavedPercents.get(i, 0.0f);
                    if (leavedPercent != 1.0f) {
                        mNavigatorScrollListener.onLeave(i, mTotalCount, 1.0f, leftToRight);
                        mLeavedPercents.put(i, 1.0f);
                    }
                }
                mNavigatorScrollListener.onEnter(safePosition, mTotalCount, 1.0f, false);
                mLeavedPercents.put(safePosition, 0.0f);
                mNavigatorScrollListener.onSelected(safePosition, mTotalCount);
                mDeselectedItems.put(safePosition, false);
            }
            mLastPositionOffsetSum = currentPositionOffsetSum;
        }
    }

    public void onPageSelected(int position) {
        int currentIndex = setCurrentIndex(position);
        if (mNavigatorScrollListener != null) {
            mNavigatorScrollListener.onSelected(currentIndex, mTotalCount);
            mDeselectedItems.put(currentIndex, false);
            for (int i = 0, j = mTotalCount; i < j; i++) {
                if (i == currentIndex) {
                    continue;
                }
                boolean deselected = mDeselectedItems.get(i);
                if (!deselected) {
                    mNavigatorScrollListener.onDeselected(i, mTotalCount);
                    mDeselectedItems.put(i, true);
                }
            }
        }
    }

    public void onPageScrollStateChanged(int state) {
        mScrollState = state;
    }

    public int getCurrentIndex() {
        return getSafeIndex(mCurrentIndex);
    }

    private int setCurrentIndex(int currentIndex) {
        mLastIndex = mCurrentIndex;
        mCurrentIndex = getSafeIndex(currentIndex);
        return mCurrentIndex;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(int totalCount) {
        clear();
        mTotalCount = totalCount;
    }

    public int getScrollState() {
        return mScrollState;
    }

    public boolean isSkimOver() {
        return mSkimOver;
    }

    public void setSkimOver(boolean skimOver) {
        mSkimOver = skimOver;
    }

    public OnNavigatorScrollListener getNavigatorScrollListener() {
        return mNavigatorScrollListener;
    }

    public void setNavigatorScrollListener(OnNavigatorScrollListener navigatorScrollListener) {
        mNavigatorScrollListener = navigatorScrollListener;
    }

    private void clear() {
        mTotalCount = 0;
        mCurrentIndex = 0;
        mLastIndex = 0;
        mLastPositionOffsetSum = 0.0f;
        mScrollState = ViewPager.SCROLL_STATE_IDLE;
        mDeselectedItems.clear();
        mLeavedPercents.clear();
    }

    public interface OnNavigatorScrollListener {
        void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight);

        void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight);

        void onSelected(int index, int totalCount);

        void onDeselected(int index, int totalCount);
    }
}
