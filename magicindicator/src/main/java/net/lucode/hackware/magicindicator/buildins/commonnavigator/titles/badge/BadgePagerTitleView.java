package net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

/**
 * 支持显示角标的title，角标布局可自定义
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/7/18.
 */
public class BadgePagerTitleView extends FrameLayout implements IMeasurablePagerTitleView {
    private IPagerTitleView mInnerPagerTitleView;
    private View mBadgeView;
    private boolean mAutoCancelBadge = true;

    private BadgeRule mXBadgeRule;
    private BadgeRule mYBadgeRule;

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
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            addView((View) mInnerPagerTitleView, lp);
        }
        if (mBadgeView != null) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            addView((View) mInnerPagerTitleView, lp);
        }
        if (mBadgeView != null) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            addView(mBadgeView, lp);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mInnerPagerTitleView instanceof View && mBadgeView != null) {
            int[] position = new int[14];   // 14种角标定位方式
            View v = (View) mInnerPagerTitleView;
            position[0] = v.getLeft();
            position[1] = v.getTop();
            position[2] = v.getRight();
            position[3] = v.getBottom();
            if (mInnerPagerTitleView instanceof IMeasurablePagerTitleView) {
                IMeasurablePagerTitleView view = (IMeasurablePagerTitleView) mInnerPagerTitleView;
                position[4] = view.getContentLeft();
                position[5] = view.getContentTop();
                position[6] = view.getContentRight();
                position[7] = view.getContentBottom();
            } else {
                for (int i = 4; i < 8; i++) {
                    position[i] = position[i - 4];
                }
            }
            position[8] = v.getWidth() / 2;
            position[9] = v.getHeight() / 2;
            position[10] = position[4] / 2;
            position[11] = position[5] / 2;
            position[12] = position[6] + (position[2] - position[6]) / 2;
            position[13] = position[7] + (position[3] - position[7]) / 2;

            // 根据设置的BadgeRule调整角标的位置
            if (mXBadgeRule != null) {
                int x = position[mXBadgeRule.getAnchor().ordinal()];
                int offset = mXBadgeRule.getOffset();
                int newLeft = x + offset;
                mBadgeView.offsetLeftAndRight(newLeft - mBadgeView.getLeft());
            }
            if (mYBadgeRule != null) {
                int y = position[mYBadgeRule.getAnchor().ordinal()];
                int offset = mYBadgeRule.getOffset();
                int newTop = y + offset;
                mBadgeView.offsetTopAndBottom(newTop - mBadgeView.getTop());
            }
        }
    }

    @Override
    public int getContentLeft() {
        if (mInnerPagerTitleView instanceof IMeasurablePagerTitleView) {
            return getLeft() + ((IMeasurablePagerTitleView) mInnerPagerTitleView).getContentLeft();
        }
        return getLeft();
    }

    @Override
    public int getContentTop() {
        if (mInnerPagerTitleView instanceof IMeasurablePagerTitleView) {
            return ((IMeasurablePagerTitleView) mInnerPagerTitleView).getContentTop();
        }
        return getTop();
    }

    @Override
    public int getContentRight() {
        if (mInnerPagerTitleView instanceof IMeasurablePagerTitleView) {
            return getLeft() + ((IMeasurablePagerTitleView) mInnerPagerTitleView).getContentRight();
        }
        return getRight();
    }

    @Override
    public int getContentBottom() {
        if (mInnerPagerTitleView instanceof IMeasurablePagerTitleView) {
            return ((IMeasurablePagerTitleView) mInnerPagerTitleView).getContentBottom();
        }
        return getBottom();
    }

    public BadgeRule getXBadgeRule() {
        return mXBadgeRule;
    }

    public void setXBadgeRule(BadgeRule badgeRule) {
        if (badgeRule != null) {
            BadgeAnchor anchor = badgeRule.getAnchor();
            if (anchor != BadgeAnchor.LEFT
                    && anchor != BadgeAnchor.RIGHT
                    && anchor != BadgeAnchor.CONTENT_LEFT
                    && anchor != BadgeAnchor.CONTENT_RIGHT
                    && anchor != BadgeAnchor.CENTER_X
                    && anchor != BadgeAnchor.LEFT_EDGE_CENTER_X
                    && anchor != BadgeAnchor.RIGHT_EDGE_CENTER_X) {
                throw new IllegalArgumentException("x badge rule is wrong.");
            }
        }
        mXBadgeRule = badgeRule;
    }

    public BadgeRule getYBadgeRule() {
        return mYBadgeRule;
    }

    public void setYBadgeRule(BadgeRule badgeRule) {
        if (badgeRule != null) {
            BadgeAnchor anchor = badgeRule.getAnchor();
            if (anchor != BadgeAnchor.TOP
                    && anchor != BadgeAnchor.BOTTOM
                    && anchor != BadgeAnchor.CONTENT_TOP
                    && anchor != BadgeAnchor.CONTENT_BOTTOM
                    && anchor != BadgeAnchor.CENTER_Y
                    && anchor != BadgeAnchor.TOP_EDGE_CENTER_Y
                    && anchor != BadgeAnchor.BOTTOM_EDGE_CENTER_Y) {
                throw new IllegalArgumentException("y badge rule is wrong.");
            }
        }
        mYBadgeRule = badgeRule;
    }

    public boolean isAutoCancelBadge() {
        return mAutoCancelBadge;
    }

    public void setAutoCancelBadge(boolean autoCancelBadge) {
        mAutoCancelBadge = autoCancelBadge;
    }
}
