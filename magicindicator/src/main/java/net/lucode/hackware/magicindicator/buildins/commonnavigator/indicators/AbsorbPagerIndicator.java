package net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators;

import android.content.Context;
import android.graphics.Color;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import net.lucode.hackware.magicindicator.ArgbEvaluatorHolder;
import net.lucode.hackware.magicindicator.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

/**
 * 弹性的水平指示器，带颜色渐变
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class AbsorbPagerIndicator extends LinePagerIndicator {
    private int mAnchorDistance;

    public AbsorbPagerIndicator(Context context) {
        super(context);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        setStartInterpolator(new AccelerateInterpolator());
        setEndInterpolator(new DecelerateInterpolator());
        setAnchorDistance(UIUtil.dip2px(context, 5));
    }

    @Override
    public void setLineHeight(float lineHeight) {
        super.setLineHeight(lineHeight);
        setRoundRadius(lineHeight / 2);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }

        // 计算颜色
        if (mColorList != null && mColorList.size() > 0) {
            int currentColor = Color.parseColor(mColorList.get(position % mColorList.size()));
            int nextColor = Color.parseColor(mColorList.get((position + 1) % mColorList.size()));
            int color = (Integer) ArgbEvaluatorHolder.eval(positionOffset, currentColor, nextColor);
            mPaint.setColor(color);
        }

        // 计算锚点位置
        int nextPosition = Math.min(mPositionDataList.size() - 1, position + 1);
        PositionData current = mPositionDataList.get(position);
        PositionData next = mPositionDataList.get(nextPosition);

        float leftX = current.mLeft + (current.mRight - current.mLeft) / 2;
        float rightX = next.mLeft + (next.mRight - next.mLeft) / 2;

        float startX = leftX - mAnchorDistance + (rightX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
        setStartX(startX);

        float endX = leftX + mAnchorDistance + (rightX - leftX) * mEndInterpolator.getInterpolation(positionOffset);
        setEndX(endX);

        invalidate();
    }

    public int getAnchorDistance() {
        return mAnchorDistance;
    }

    public void setAnchorDistance(int anchorDistance) {
        mAnchorDistance = anchorDistance;
    }
}
