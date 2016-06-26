package net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import net.lucode.hackware.magicindicator.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.List;


/**
 * 包裹住内容区域的指示器，类似天天快报的切换效果，需要和IMeasurablePagerTitleView配合使用
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class WrapPagerIndicator extends View implements IPagerIndicator {
    protected int mVerticalPadding;
    protected int mHorizontalPadding;
    protected int mFillColor;
    protected float mRoundRadius;
    protected Interpolator mStartInterpolator = new LinearInterpolator();
    protected Interpolator mEndInterpolator = new LinearInterpolator();

    protected List<PositionData> mPositionDataList;
    protected Paint mPaint;

    private RectF mRect = new RectF();
    private boolean mRoundRadiusSet;

    public WrapPagerIndicator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        setVerticalPadding(UIUtil.dip2px(context, 6));
        setHorizontalPadding(UIUtil.dip2px(context, 10));
    }

    public Paint getPaint() {
        return mPaint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mFillColor);
        canvas.drawRoundRect(mRect, mRoundRadius, mRoundRadius, mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }

        // 计算锚点位置
        int nextPosition = Math.min(mPositionDataList.size() - 1, position + 1);
        PositionData current = mPositionDataList.get(position);
        PositionData next = mPositionDataList.get(nextPosition);

        float left = current.mContentLeft - mHorizontalPadding + (next.mContentLeft - current.mContentLeft) * mEndInterpolator.getInterpolation(positionOffset);
        float top = current.mContentTop - mVerticalPadding;
        float right = current.mContentRight + mHorizontalPadding + (next.mContentRight - current.mContentRight) * mStartInterpolator.getInterpolation(positionOffset);
        float bottom = current.mContentBottom + mVerticalPadding;

        mRect = new RectF(left, top, right, bottom);

        if (!mRoundRadiusSet) {
            mRoundRadius = mRect.height() / 2;
        }

        invalidate();
    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDataList = dataList;
    }

    public int getVerticalPadding() {
        return mVerticalPadding;
    }

    public void setVerticalPadding(int verticalPadding) {
        mVerticalPadding = verticalPadding;
    }

    public int getHorizontalPadding() {
        return mHorizontalPadding;
    }

    public void setHorizontalPadding(int horizontalPadding) {
        mHorizontalPadding = horizontalPadding;
    }

    public int getFillColor() {
        return mFillColor;
    }

    public void setFillColor(int fillColor) {
        mFillColor = fillColor;
    }

    public float getRoundRadius() {
        return mRoundRadius;
    }

    public void setRoundRadius(float roundRadius) {
        mRoundRadius = roundRadius;
        mRoundRadiusSet = true;
    }

    public Interpolator getStartInterpolator() {
        return mStartInterpolator;
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }

    public Interpolator getEndInterpolator() {
        return mEndInterpolator;
    }

    public void setEndInterpolator(Interpolator endInterpolator) {
        mEndInterpolator = endInterpolator;
        if (mEndInterpolator == null) {
            mEndInterpolator = new LinearInterpolator();
        }
    }
}
