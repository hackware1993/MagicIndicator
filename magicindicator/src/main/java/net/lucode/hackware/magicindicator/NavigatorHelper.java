package net.lucode.hackware.magicindicator;

/**
 * 方便扩展IPagerNavigator的帮助类
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class NavigatorHelper {
    private int mCurrentIndex;
    private int mTotalCount;

    private OnNavigatorScrollListener mNavigatorScrollListener;

    public NavigatorHelper() {
    }

    public int getSafeIndex(int index) {
        return Math.max(Math.min(index, mTotalCount - 1), 0);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        position = getSafeIndex(position);
        if (position == getCurrentIndex()) {
            int enterIndex = getSafeIndex(position + 1);
            if (position != enterIndex) {
                if (mNavigatorScrollListener != null) {
                    mNavigatorScrollListener.onLeave(position, positionOffset, true);
                    mNavigatorScrollListener.onEnter(enterIndex, positionOffset, true);
                }
            }
        } else if (position == getCurrentIndex() - 1) {
            if (mNavigatorScrollListener != null) {
                mNavigatorScrollListener.onLeave(getCurrentIndex(), 1.0f - positionOffset, false);
                mNavigatorScrollListener.onEnter(position, 1.0f - positionOffset, false);
            }
        }
    }

    public void onPageSelected(int position) {
        mCurrentIndex = getSafeIndex(position);
    }

    public void onPageScrollStateChanged(int state) {
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

    public OnNavigatorScrollListener getNavigatorScrollListener() {
        return mNavigatorScrollListener;
    }

    public void setNavigatorScrollListener(OnNavigatorScrollListener navigatorScrollListener) {
        mNavigatorScrollListener = navigatorScrollListener;
    }

    public void clear() {
        mCurrentIndex = 0;
        mTotalCount = 0;
    }

    public interface OnNavigatorScrollListener {
        void onEnter(int index, float positionOffset, boolean leftToRight);

        void onLeave(int index, float positionOffset, boolean leftToRight);
    }
}
