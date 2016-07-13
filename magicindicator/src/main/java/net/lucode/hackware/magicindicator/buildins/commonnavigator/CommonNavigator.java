package net.lucode.hackware.magicindicator.buildins.commonnavigator;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.ViewPager;
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

    /**
     * 提供给外部的参数配置
     */
    /****************************************************/
    private boolean mAdjustMode;   // 自适应模式，为true表示title均分宽度，适用于数目固定的、少量的title
    private boolean mEnablePivotScroll; // 启动中心点滚动
    private float mScrollPivotX = 0.5f; // 滚动中心点 0.0f - 1.0f
    private boolean mSmoothScroll = true;   // 是否平滑滚动，适用于 !mAdjustMode && !mFollowTouch
    private boolean mFollowTouch = true;    // 是否手指跟随滚动
    private int mRightPadding;
    private int mLeftPadding;
    private boolean mIndicatorOnTop;    // 指示器在title上方，默认为下方
    /****************************************************/

    // 保存每个title的位置信息，为扩展indicator提供保障
    private List<PositionData> mPositionList = new ArrayList<PositionData>();

    private DataSetObserver mObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            mNavigatorHelper.setTotalCount(mAdapter.getCount());    // 如果使用helper，应始终保证helper中的totalCount为最新
            init();
        }

        @Override
        public void onInvalidated() {
            // 没什么用，暂不做处理
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

    public boolean isAdjustMode() {
        return mAdjustMode;
    }

    public void setAdjustMode(boolean is) {
        mAdjustMode = is;
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
            mNavigatorHelper.clear();   // 重要
            init();
        }
    }

    private void init() {
        removeAllViews();

        View root;
        if (mAdjustMode) {
            root = LayoutInflater.from(getContext()).inflate(R.layout.pager_navigator_layout_no_scroll, this);
        } else {
            root = LayoutInflater.from(getContext()).inflate(R.layout.pager_navigator_layout, this);
        }

        mScrollView = (HorizontalScrollView) root.findViewById(R.id.scroll_view);   // mAdjustMode为true时，mScrollView为null
        if (!mAdjustMode) {
            mScrollView.getChildAt(0).setPadding(mLeftPadding, 0, mRightPadding, 0);    // TODO padding的效果尚未达到预期
        }

        mIndicatorContainer = (LinearLayout) root.findViewById(R.id.indicator_container);
        mTitleContainer = (LinearLayout) root.findViewById(R.id.title_container);
        if (mIndicatorOnTop) {
            mIndicatorContainer.getParent().bringChildToFront(mIndicatorContainer);
        }

        initTitlesAndIndicator();
    }

    /**
     * 初始化title和indicator
     */
    private void initTitlesAndIndicator() {
        for (int i = 0, j = mNavigatorHelper.getTotalCount(); i < j; i++) {
            IPagerTitleView v = mAdapter.getTitleView(getContext(), i);
            if (v instanceof View) {
                View view = (View) v;
                LinearLayout.LayoutParams lp;
                if (mAdjustMode) {
                    lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    lp.weight = mAdapter.getTitleWeight(getContext(), i);
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

        // 将title的位置信息设置到指示器
        if (mIndicator != null) {
            mIndicator.onPositionDataProvide(dataList);
        }

        // 初始化title的位置
        if (mNavigatorHelper.getScrollState() == ViewPager.SCROLL_STATE_IDLE) {
            onPageSelected(mNavigatorHelper.getCurrentIndex());
            onPageScrolled(mNavigatorHelper.getCurrentIndex(), 0.0f, 0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mAdapter != null) {

            mNavigatorHelper.onPageScrolled(position, positionOffset, positionOffsetPixels);
            if (mIndicator != null) {
                mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            // 手指跟随滚动
            if (mScrollView != null && mPositionList.size() > 0) {
                if (mFollowTouch) {
                    int currentPosition = Math.min(mPositionList.size() - 1, position);
                    int nextPosition = Math.min(mPositionList.size() - 1, position + 1);
                    PositionData current = mPositionList.get(currentPosition);
                    PositionData next = mPositionList.get(nextPosition);
                    float scrollTo = current.horizontalCenter() - mScrollView.getWidth() * mScrollPivotX;
                    float nextScrollTo = next.horizontalCenter() - mScrollView.getWidth() * mScrollPivotX;
                    mScrollView.scrollTo((int) (scrollTo + (nextScrollTo - scrollTo) * positionOffset), 0);
                } else if (!mEnablePivotScroll) {
                    // TODO 实现待选中项完全显示出来
                }
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
            if (mIndicator != null) {
                mIndicator.onPageSelected(position);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mAdapter != null) {
            mNavigatorHelper.onPageScrollStateChanged(state);
            if (mIndicator != null) {
                mIndicator.onPageScrollStateChanged(state);
            }
        }
    }

    @Override
    public void onAttachToMagicIndicator() {
        init(); // 将初始化延迟到这里
    }

    @Override
    public void onDetachFromMagicIndicator() {
    }

    public IPagerIndicator getPagerIndicator() {
        return mIndicator;
    }

    public boolean isEnablePivotScroll() {
        return mEnablePivotScroll;
    }

    public void setEnablePivotScroll(boolean is) {
        mEnablePivotScroll = is;
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        if (mTitleContainer == null) {
            return;
        }
        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPagerTitleView) {
            ((IPagerTitleView) v).onEnter(index, totalCount, enterPercent, leftToRight);
        } else if (v instanceof ViewGroup) {    // 尝试兼容第三方的角标框架如BadgeView，后面自己写一个BadgePagerTitleView
            View child = ((ViewGroup) v).getChildAt(0);
            if (child instanceof IPagerTitleView) {
                ((IPagerTitleView) child).onEnter(index, totalCount, enterPercent, leftToRight);
            }
        }
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        if (mTitleContainer == null) {
            return;
        }
        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPagerTitleView) {
            ((IPagerTitleView) v).onLeave(index, totalCount, leavePercent, leftToRight);
        } else if (v instanceof ViewGroup) {    // 尝试兼容第三方的角标框架如BadgeView，后面自己写一个BadgePagerTitleView
            View child = ((ViewGroup) v).getChildAt(0);
            if (child instanceof IPagerTitleView) {
                ((IPagerTitleView) child).onLeave(index, totalCount, leavePercent, leftToRight);
            }
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
    public void onSelected(int index, int totalCount) {
        if (mTitleContainer == null) {
            return;
        }
        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPagerTitleView) {
            ((IPagerTitleView) v).onSelected(index, totalCount);
        } else if (v instanceof ViewGroup) {    // 尝试兼容第三方的角标框架如BadgeView，后面自己写一个BadgePagerTitleView
            View child = ((ViewGroup) v).getChildAt(0);
            if (child instanceof IPagerTitleView) {
                ((IPagerTitleView) child).onSelected(index, totalCount);
            }
        }
        if (!mAdjustMode && !mFollowTouch && mScrollView != null && mPositionList.size() > 0) {
            int currentIndex = Math.min(mPositionList.size() - 1, index);
            PositionData current = mPositionList.get(currentIndex);
            if (mEnablePivotScroll) {
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
                } else if (mScrollView.getScrollX() + getWidth() < current.mRight) {
                    if (mSmoothScroll) {
                        mScrollView.smoothScrollTo(current.mRight - getWidth(), 0);
                    } else {
                        mScrollView.scrollTo(current.mRight - getWidth(), 0);
                    }
                }
            }
        }
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        if (mTitleContainer == null) {
            return;
        }
        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPagerTitleView) {
            ((IPagerTitleView) v).onDeselected(index, totalCount);
        } else if (v instanceof ViewGroup) {    // 尝试兼容第三方的角标框架如BadgeView，后面自己写一个BadgePagerTitleView
            View child = ((ViewGroup) v).getChildAt(0);
            if (child instanceof IPagerTitleView) {
                ((IPagerTitleView) child).onDeselected(index, totalCount);
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

    public boolean isIndicatorOnTop() {
        return mIndicatorOnTop;
    }

    public void setIndicatorOnTop(boolean indicatorOnTop) {
        mIndicatorOnTop = indicatorOnTop;
    }
}
