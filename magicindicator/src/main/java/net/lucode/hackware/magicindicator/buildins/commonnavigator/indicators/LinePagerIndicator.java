package net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import net.lucode.hackware.magicindicator.ArgbEvaluatorHolder;
import net.lucode.hackware.magicindicator.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.List;

/**
 * 直线viewpager指示器，带颜色渐变
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class LinePagerIndicator extends View implements IPagerIndicator {
    protected List<PositionData> mPositionDataList;

    protected float mYOffset;
    protected float mLineHeight;
    protected float mRoundRadius;
    protected boolean mWrapContentMode;
    protected Interpolator mStartInterpolator = new LinearInterpolator();
    protected Interpolator mEndInterpolator = new LinearInterpolator();

    protected float mStartX;
    protected float mEndX;

    protected Paint mPaint;
    protected List<String> mColorList;

    public LinePagerIndicator(Context context) {
        super(context);
        init(context);
    }

    protected void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        setLineHeight(UIUtil.dip2px(context, 3));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        RectF rectF = new RectF(mStartX, getHeight() - mLineHeight - mYOffset, mEndX, getHeight() - mYOffset);
        canvas.drawRoundRect(rectF, mRoundRadius, mRoundRadius, mPaint);
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

        float leftX = mWrapContentMode ? current.mContentLeft : current.mLeft;
        float nextLeftX = mWrapContentMode ? next.mContentLeft : next.mLeft;
        float rightX = mWrapContentMode ? current.mContentRight : current.mRight;
        float nextRightX = mWrapContentMode ? next.mContentRight : next.mRight;

        float startX = leftX + (nextLeftX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
        setStartX(startX);

        float endX = rightX + (nextRightX - rightX) * mEndInterpolator.getInterpolation(positionOffset);
        setEndX(endX);

        invalidate();
    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDataList = dataList;
    }

    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float yOffset) {
        mYOffset = yOffset;
    }

    protected float getStartX() {
        return mStartX;
    }

    protected void setStartX(float startX) {
        mStartX = startX;
    }

    protected float getEndX() {
        return mEndX;
    }

    protected void setEndX(float endX) {
        mEndX = endX;
    }

    public float getLineHeight() {
        return mLineHeight;
    }

    public void setLineHeight(float lineHeight) {
        mLineHeight = lineHeight;
    }

    public float getRoundRadius() {
        return mRoundRadius;
    }

    public void setRoundRadius(float roundRadius) {
        mRoundRadius = roundRadius;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public List<String> getColorList() {
        return mColorList;
    }

    public void setColorList(List<String> colorList) {
        mColorList = colorList;
    }

    public boolean isWrapContentMode() {
        return mWrapContentMode;
    }

    public void setWrapContentMode(boolean wrapContentMode) {
        mWrapContentMode = wrapContentMode;
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
