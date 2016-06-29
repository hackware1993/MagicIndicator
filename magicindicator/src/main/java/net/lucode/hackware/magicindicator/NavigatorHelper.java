package net.lucode.hackware.magicindicator;

/**
 * 方便扩展IPagerNavigator的帮助类
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class NavigatorHelper {
    private int mCurrentIndex;
    private int mTotalCount;
    private int mScrollState;

    private OnNavigatorScrollListener mNavigatorScrollListener;

    public NavigatorHelper() {
    }

    public int getSafeIndex(int index) {
        return Math.max(Math.min(index, getTotalCount() - 1), 0);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mNavigatorScrollListener != null) {
            int safePosition = getSafeIndex(position);
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
            mNavigatorScrollListener.onEnter(enterIndex, enterPercent, leftToRight);
            mNavigatorScrollListener.onLeave(leaveIndex, leavePercent, leftToRight);
            for (int i = 0; i < getTotalCount(); i++) {
                if (i == enterIndex || i == leaveIndex) {
                    continue;
                }
                mNavigatorScrollListener.onLeave(i, 1.0f, false);
            }
        }
    }

    public void onPageSelected(int position) {
        setCurrentIndex(position);
    }

    public void onPageScrollStateChanged(int state) {
        setScrollState(state);
    }

    public int getCurrentIndex() {
        return getSafeIndex(mCurrentIndex);
    }

    public void setCurrentIndex(int currentIndex) {
        mCurrentIndex = getSafeIndex(currentIndex);
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
        setTotalCount(0);
        setCurrentIndex(0);
    }

    public interface OnNavigatorScrollListener {
        void onEnter(int index, float enterPercent, boolean leftToRight);

        void onLeave(int index, float leavePercent, boolean leftToRight);
    }
}
