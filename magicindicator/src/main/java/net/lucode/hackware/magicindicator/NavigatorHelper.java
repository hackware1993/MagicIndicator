package net.lucode.hackware.magicindicator;

import android.support.v4.view.ViewPager;

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

    private OnNavigatorScrollListener mNavigatorScrollListener;

    public NavigatorHelper() {
    }

    public int getSafeIndex(int index) {
        return Math.max(Math.min(index, mTotalCount - 1), 0);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mNavigatorScrollListener != null) {
            int safePosition = getSafeIndex(position);
            if (mScrollState != ViewPager.SCROLL_STATE_IDLE) {
                boolean leftToRight = safePosition >= getCurrentIndex();
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

                mNavigatorScrollListener.onEnter(enterIndex, mTotalCount, enterPercent, leftToRight);
                mNavigatorScrollListener.onLeave(leaveIndex, mTotalCount, leavePercent, leftToRight);

                // 简单粗暴，有待优化
                for (int i = 0, j = mTotalCount; i < j; i++) {
                    if (i == enterIndex || i == leaveIndex) {
                        continue;
                    }
                    mNavigatorScrollListener.onLeave(i, mTotalCount, 1.0f, false);
                }
            } else {
                // 在IDLE状态下收到了onPageScrolled回调，表示完全滚动到了某一页
                mCurrentIndex = safePosition;
                mNavigatorScrollListener.onEnter(safePosition, mTotalCount, 1.0f, false);
                mNavigatorScrollListener.onSelected(safePosition, mTotalCount);
                for (int i = 0, j = mTotalCount; i < j; i++) {
                    if (i == safePosition) {
                        continue;
                    }
                    mNavigatorScrollListener.onLeave(i, mTotalCount, 1.0f, false);
                    mNavigatorScrollListener.onDeselected(i, mTotalCount);
                }
            }
        }
    }

    public void onPageSelected(int position) {
        int currentIndex = setCurrentIndex(position);
        if (mNavigatorScrollListener != null) {
            mNavigatorScrollListener.onSelected(currentIndex, mTotalCount);
            for (int i = 0, j = mTotalCount; i < j; i++) {
                if (i == currentIndex) {
                    continue;
                }
                mNavigatorScrollListener.onDeselected(i, mTotalCount);
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
    }

    public interface OnNavigatorScrollListener {
        void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight);

        void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight);

        void onSelected(int index, int totalCount);

        void onDeselected(int index, int totalCount);
    }
}
