package net.lucode.hackware.magicindicator.buildins.commonnavigator;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import net.lucode.hackware.magicindicator.NavigatorHelper;
import net.lucode.hackware.magicindicator.R;
import net.lucode.hackware.magicindicator.abs.IPagerNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的ViewPager指示器，包含PagerTitle和PagerIndicator
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class CommonNavigator extends FrameLayout implements IPagerNavigator, NavigatorHelper.OnNavigatorScrollListener {
    private HorizontalScrollView mScrollView;
    private LinearLayout mTitleContainer;
    private LinearLayout mIndicatorContainer;
    private IPagerIndicator mIndicator;

    private CommonNavigatorAdapter mAdapter;
    private NavigatorHelper mNavigatorHelper;
    private boolean mFitMode;   // 自适应模式
    private boolean mAlwaysScrollToCenter;  // 当前页始终居中显示
    private boolean mSmoothScroll = true;
    private boolean mFollowTouch = true;
    private float mScrollPivotX = 0.5f;
    private List<PositionData> mPositionList = new ArrayList<PositionData>();
    private int mRightPadding;
    private int mLeftPadding;
    private DataSetObserver mObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            mNavigatorHelper.setTotalCount(mAdapter.getCount());
            init();
        }

        @Override
        public void onInvalidated() {
        }
    };

    public CommonNavigator(Context context) {
        super(context);
        mNavigatorHelper = new NavigatorHelper();
        mNavigatorHelper.setNavigatorScrollListener(this);
    }

    @Override
    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mNavigatorHelper.setTotalCount(mAdapter.getCount());
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setFitMode(boolean is) {
        mFitMode = is;
    }

    public CommonNavigatorAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(CommonNavigatorAdapter adapter) {
        if (mAdapter == adapter) {
            return;
        }
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mObserver);
            adapter.notifyDataSetChanged();
        } else {
            mNavigatorHelper.clear();
            init();
        }
    }

    private void init() {
        removeAllViews();

        View root;
        if (mFitMode) {
            root = LayoutInflater.from(getContext()).inflate(R.layout.pager_navigator_layout_no_scroll, this);
        } else {
            root = LayoutInflater.from(getContext()).inflate(R.layout.pager_navigator_layout, this);
        }

        mScrollView = (HorizontalScrollView) root.findViewById(R.id.scroll_view);   // mFitMode为true时，mScrollView为null
        if (!mFitMode) {
            mScrollView.setPadding(mLeftPadding, 0, mRightPadding, 0);
        }

        mIndicatorContainer = (LinearLayout) root.findViewById(R.id.indicator_container);
        mTitleContainer = (LinearLayout) root.findViewById(R.id.title_container);

        initTitlesAndIndicator();
    }

    /**
     * 初始化title和indicator
     */
    private void initTitlesAndIndicator() {
        for (int i = 0, j = mNavigatorHelper.getTotalCount(); i < j; i++) {
            IPagerTitleView v = mAdapter.getItemView(getContext(), i);
            if (v instanceof View) {
                View view = (View) v;
                LinearLayout.LayoutParams lp;
                if (mFitMode) {
                    lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    lp.weight = 1;
                } else {
                    lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                }
                mTitleContainer.addView(view, lp);
            }
        }
        if (mAdapter != null) {
            mIndicator = mAdapter.getIndicator(getContext());
            if (mIndicator instanceof View) {
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mIndicatorContainer.addView((View) mIndicator, lp);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mAdapter != null) {
            preparePositionData();
        }
    }

    private void preparePositionData() {
        // 获取title的位置信息，为打造不同的指示器、各种效果提供可能
        List<PositionData> dataList = new ArrayList<PositionData>();
        for (int i = 0, j = mNavigatorHelper.getTotalCount(); i < j; i++) {
            View v = mTitleContainer.getChildAt(i);
            PositionData data = new PositionData();
            data.mLeft = v.getLeft();
            data.mRight = v.getRight();
            data.mTop = v.getTop();
            data.mBottom = v.getBottom();
            if (v instanceof IMeasurablePagerTitleView) {
                IMeasurablePagerTitleView view = (IMeasurablePagerTitleView) v;
                data.mContentLeft = view.getContentLeft();
                data.mContentTop = view.getContentTop();
                data.mContentRight = view.getContentRight();
                data.mContentBottom = view.getContentBottom();
            } else {
                data.mContentLeft = data.mLeft;
                data.mContentTop = data.mTop;
                data.mContentRight = data.mRight;
                data.mContentBottom = data.mBottom;
            }
            dataList.add(data);
        }

        mPositionList = new ArrayList<PositionData>(dataList);

        // 将title的位置信息设置到指示器，并定位到当前位置
        if (mIndicator != null) {
            mIndicator.onPositionDataProvide(dataList);
            mIndicator.onPageScrolled(mNavigatorHelper.getCurrentIndex(), 0.0f, 0);
        }

        // 初始化title的位置
        onPageSelected(mNavigatorHelper.getCurrentIndex());
        onPageScrolled(mNavigatorHelper.getCurrentIndex(), 0.0f, 0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mAdapter != null) {
            mNavigatorHelper.onPageScrolled(position, positionOffset, positionOffsetPixels);
            if (mIndicator != null) {
                mIndicator.onPageScrolled(mNavigatorHelper.getSafeIndex(position), positionOffset, positionOffsetPixels);
            }

            // 手指跟随滚动
            if (mFollowTouch && mScrollView != null && mPositionList.size() > 0) {
                int nextPosition = Math.min(mPositionList.size() - 1, position + 1);
                PositionData current = mPositionList.get(position);
                PositionData next = mPositionList.get(nextPosition);
                float scrollTo = current.horizontalCenter() - mScrollView.getWidth() * mScrollPivotX;
                float nextScrollTo = next.horizontalCenter() - mScrollView.getWidth() * mScrollPivotX;
                mScrollView.scrollTo((int) (scrollTo + (nextScrollTo - scrollTo) * positionOffset) + (mLeftPadding + mRightPadding) / 2, 0);
            }
        }
    }

    public float getScrollPivotX() {
        return mScrollPivotX;
    }

    public void setScrollPivotX(float scrollPivotX) {
        mScrollPivotX = scrollPivotX;
    }

    @Override
    public void onPageSelected(int position) {
        if (mAdapter != null) {
            mNavigatorHelper.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mAdapter != null) {
            mNavigatorHelper.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onAttachToMagicIndicator() {
        init();
    }

    @Override
    public void onDetachFromMagicIndicator() {
    }

    public IPagerIndicator getPagerIndicator() {
        return mIndicator;
    }

    public boolean isAlwaysScrollToCenter() {
        return mAlwaysScrollToCenter;
    }

    public void setAlwaysScrollToCenter(boolean is) {
        mAlwaysScrollToCenter = is;
    }

    @Override
    public void onEnter(int index, float enterPercent, boolean leftToRight) {
        if (mTitleContainer == null) {
            return;
        }
        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPagerTitleView) {
            ((IPagerTitleView) v).onEnter(index, enterPercent, leftToRight);
        }
    }

    @Override
    public void onLeave(int index, float leavePercent, boolean leftToRight) {
        if (mTitleContainer == null) {
            return;
        }
        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPagerTitleView) {
            ((IPagerTitleView) v).onLeave(index, leavePercent, leftToRight);
        }
    }

    public boolean isSmoothScroll() {
        return mSmoothScroll;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        mSmoothScroll = smoothScroll;
    }

    public boolean isFollowTouch() {
        return mFollowTouch;
    }

    public void setFollowTouch(boolean followTouch) {
        mFollowTouch = followTouch;
    }

    @Override
    public void onSelected(int index) {
        if (mTitleContainer == null) {
            return;
        }
        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPagerTitleView) {
            ((IPagerTitleView) v).onSelected(index);
        }
        if (!mFitMode && !mFollowTouch && mScrollView != null && mPositionList.size() > 0) {
            PositionData current = mPositionList.get(index);
            if (mAlwaysScrollToCenter) {
                float scrollTo = current.horizontalCenter() - mScrollView.getWidth() * mScrollPivotX;
                if (mSmoothScroll) {
                    mScrollView.smoothScrollTo((int) (scrollTo), 0);
                } else {
                    mScrollView.scrollTo((int) (scrollTo), 0);
                }
            } else {
                // 如果当前项被部分遮挡，则滚动显示完全
                if (mScrollView.getScrollX() > current.mLeft) {
                    if (mSmoothScroll) {
                        mScrollView.smoothScrollTo(current.mLeft, 0);
                    } else {
                        mScrollView.scrollTo(current.mLeft, 0);
                    }
                } else if (mScrollView.getScrollX() + getWidth() < current.mRight + mRightPadding + mLeftPadding) {
                    if (mSmoothScroll) {
                        mScrollView.smoothScrollTo(current.mRight - getWidth() + mLeftPadding + mRightPadding, 0);
                    } else {
                        mScrollView.scrollTo(current.mRight - getWidth() + mLeftPadding + mRightPadding, 0);
                    }
                }
            }
        }
    }

    public int getRightPadding() {
        return mRightPadding;
    }

    public void setRightPadding(int rightPadding) {
        mRightPadding = rightPadding;
    }

    public int getLeftPadding() {
        return mLeftPadding;
    }

    public void setLeftPadding(int leftPadding) {
        mLeftPadding = leftPadding;
    }

    @Override
    public void onDeselected(int index) {
        if (mTitleContainer == null) {
            return;
        }
        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPagerTitleView) {
            ((IPagerTitleView) v).onDeselected(index);
        }
    }
}
