package net.lucode.hackware.magicindicator.buildins.commonnavigator;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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

    private DataSetObserver mObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            refresh();
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
            mNavigatorHelper.setTotalCount(mAdapter.getCount());
        } else {
            mNavigatorHelper.clear();
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

        mScrollView = (HorizontalScrollView) root.findViewById(R.id.scroll_view);   // mFill为true时，mScrollView为null
        mIndicatorContainer = (LinearLayout) root.findViewById(R.id.indicator_container);
        mTitleContainer = (LinearLayout) root.findViewById(R.id.title_container);

        refresh();
    }

    /**
     * 刷新视图
     */
    public void refresh() {
        if (mTitleContainer == null || mIndicatorContainer == null) {
            return;
        }

        mTitleContainer.removeAllViews();   // 清空所有view
        mIndicatorContainer.removeAllViews();

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

        // 如果view可见，在onPreDraw中可以取到坐标点
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                preparePositionData();
                return false;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {   // 当view不可见时，在onPreDraw中取到的坐标点全是0，需要在这里重新取
            preparePositionData();
        }
    }

    private void preparePositionData() {
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
        if (mIndicator != null) {
            mIndicator.onPositionDataProvide(dataList);
            mIndicator.onPageScrolled(mNavigatorHelper.getCurrentIndex(), 0.0f, 0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mNavigatorHelper.onPageScrolled(position, positionOffset, positionOffsetPixels);
        if (mIndicator != null) {
            mIndicator.onPageScrolled(mNavigatorHelper.getSafeIndex(position), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mNavigatorHelper.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mNavigatorHelper.onPageScrollStateChanged(state);
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

    public void setAlwaysScrollToCenter(boolean is) {
        mAlwaysScrollToCenter = is;
    }

    @Override
    public void onEnter(int index, float enterPercent, boolean leftToRight) {
        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPagerTitleView) {
            ((IPagerTitleView) v).onEnter(index, enterPercent, leftToRight);
        }
    }

    @Override
    public void onLeave(int index, float leavePercent, boolean leftToRight) {
        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPagerTitleView) {
            ((IPagerTitleView) v).onLeave(index, leavePercent, leftToRight);
        }
    }

    private boolean mSmoothScroll = true;

    public boolean isSmoothScroll() {
        return mSmoothScroll;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        mSmoothScroll = smoothScroll;
    }

    @Override
    public void onSelected(int index) {
        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPagerTitleView) {
            ((IPagerTitleView) v).onSelected(index);
        }
        if (!mFitMode) {
            // 滚动定位到该项
            View target = mTitleContainer.getChildAt(index);
            if (target != null) {
                if (mAlwaysScrollToCenter) {
                    int centerX = target.getLeft() + (target.getRight() - target.getLeft()) / 2;
                    if (centerX - mScrollView.getWidth() / 2 <= 0) {
                        if (mSmoothScroll) {
                            mScrollView.smoothScrollTo(0, 0);
                        } else {
                            mScrollView.scrollTo(0, 0);
                        }
                    } else {
                        if (mSmoothScroll) {
                            mScrollView.smoothScrollTo(centerX - mScrollView.getWidth() / 2, 0);
                        } else {
                            mScrollView.scrollTo(centerX - mScrollView.getWidth() / 2, 0);
                        }
                    }
                } else {
                    if (target.getLeft() - mScrollView.getScrollX() < 0) {
                        if (mSmoothScroll) {
                            mScrollView.smoothScrollTo(target.getLeft(), 0);
                        } else {
                            mScrollView.scrollTo(target.getLeft(), 0);
                        }
                    } else if (mScrollView.getWidth() + mScrollView.getScrollX() < target.getRight()) {
                        if (mSmoothScroll) {
                            mScrollView.smoothScrollTo(target.getRight() - mScrollView.getWidth(), 0);
                        } else {
                            mScrollView.scrollTo(target.getRight() - mScrollView.getWidth(), 0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDeselected(int index) {
        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPagerTitleView) {
            ((IPagerTitleView) v).onDeselected(index);
        }
    }
}
