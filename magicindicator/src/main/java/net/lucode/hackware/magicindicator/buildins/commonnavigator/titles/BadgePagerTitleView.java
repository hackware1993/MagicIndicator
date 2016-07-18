package net.lucode.hackware.magicindicator.buildins.commonnavigator.titles;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

/**
 * 支持显示角标的title，角标布局可自定义
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/7/18.
 */
public class BadgePagerTitleView extends FrameLayout implements IPagerTitleView {
    private IPagerTitleView mInnerPagerTitleView;
    private View mBadgeView;
    private boolean mAutoCancelBadge = true;

    public BadgePagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        if (mInnerPagerTitleView != null) {
            mInnerPagerTitleView.onSelected(index, totalCount);
        }
        if (mAutoCancelBadge) {
            setBadgeView(null);
        }
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        if (mInnerPagerTitleView != null) {
            mInnerPagerTitleView.onDeselected(index, totalCount);
        }
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        if (mInnerPagerTitleView != null) {
            mInnerPagerTitleView.onLeave(index, totalCount, leavePercent, leftToRight);
        }
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        if (mInnerPagerTitleView != null) {
            mInnerPagerTitleView.onEnter(index, totalCount, enterPercent, leftToRight);
        }
    }

    public IPagerTitleView getInnerPagerTitleView() {
        return mInnerPagerTitleView;
    }

    public void setInnerPagerTitleView(IPagerTitleView innerPagerTitleView) {
        if (mInnerPagerTitleView == innerPagerTitleView) {
            return;
        }
        mInnerPagerTitleView = innerPagerTitleView;
        removeAllViews();
        if (mInnerPagerTitleView instanceof View) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
            addView((View) mInnerPagerTitleView, lp);
        }
        if (mBadgeView != null) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            addView(mBadgeView, lp);
        }
    }

    public View getBadgeView() {
        return mBadgeView;
    }

    public void setBadgeView(View badgeView) {
        if (mBadgeView == badgeView) {
            return;
        }
        mBadgeView = badgeView;
        removeAllViews();
        if (mInnerPagerTitleView instanceof View) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
            addView((View) mInnerPagerTitleView, lp);
        }
        if (mBadgeView != null) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            addView(mBadgeView, lp);
        }
    }

    public boolean isAutoCancelBadge() {
        return mAutoCancelBadge;
    }

    public void setAutoCancelBadge(boolean autoCancelBadge) {
        mAutoCancelBadge = autoCancelBadge;
    }
}
