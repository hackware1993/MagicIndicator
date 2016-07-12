package net.lucode.hackware.magicindicator;

import android.support.v4.view.ViewPager;
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
    private SparseBooleanArray mTotalLeavedItems = new SparseBooleanArray();

    private float mLastPositionOffsetSum;

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
                    boolean totalLeaved = mTotalLeavedItems.get(i);
                    if (!totalLeaved) {
                        mNavigatorScrollListener.onLeave(i, mTotalCount, 1.0f, false);
                        mTotalLeavedItems.put(i, true);
                    }
                }
                if (!mTotalLeavedItems.get(enterIndex) || enterPercent != 0.0f) {
                    mNavigatorScrollListener.onEnter(enterIndex, mTotalCount, enterPercent, leftToRight);
                }
                mTotalLeavedItems.put(enterIndex, enterPercent == 0.0f);
                if (!mTotalLeavedItems.get(leaveIndex) || leavePercent != 1.0f) {
                    mNavigatorScrollListener.onLeave(leaveIndex, mTotalCount, leavePercent, leftToRight);
                }
                mTotalLeavedItems.put(leaveIndex, leavePercent == 1.0f);
            } else {    // 在IDLE状态下收到了onPageScrolled回调，表示完全滚动到了某一页
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
                    boolean totalLeaved = mTotalLeavedItems.get(i);
                    if (!totalLeaved) {
                        mNavigatorScrollListener.onLeave(i, mTotalCount, 1.0f, false);
                        mTotalLeavedItems.put(i, true);
                    }
                }
                mNavigatorScrollListener.onEnter(safePosition, mTotalCount, 1.0f, false);
                mTotalLeavedItems.put(safePosition, false);
                mNavigatorScrollListener.onSelected(safePosition, mTotalCount);
                mDeselectedItems.put(safePosition, false);
            }
            mLastPositionOffsetSum = position + positionOffset;
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

    public int setCurrentIndex(int currentIndex) {
        mCurrentIndex = getSafeIndex(currentIndex);
        return mCurrentIndex;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
        mDeselectedItems.clear();
        mTotalLeavedItems.clear();
    }

    public int getScrollState() {
        return mScrollState;
    }

    public void setScrollState(int scrollState) {
        mScrollState = scrollState;
    }

    public OnNavigatorScrollListener getNavigatorScrollListener() {
        return mNavigatorScrollListener;
    }

    public void setNavigatorScrollListener(OnNavigatorScrollListener navigatorScrollListener) {
        mNavigatorScrollListener = navigatorScrollListener;
    }

    public void clear() {
        mTotalCount = 0;
        mCurrentIndex = 0;
        mScrollState = ViewPager.SCROLL_STATE_IDLE;
        mDeselectedItems.clear();
        mTotalLeavedItems.clear();
    }

    public interface OnNavigatorScrollListener {
        void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight);

        void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight);

        void onSelected(int index, int totalCount);

        void onDeselected(int index, int totalCount);
    }
}
