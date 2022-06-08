package net.lucode.hackware.magicindicatordemo.ext.navigator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

import net.lucode.hackware.magicindicator.abs.IPagerNavigator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hackware on 2016/7/24.
 */

public class DummyCircleNavigator extends View implements IPagerNavigator {
    private int mRadius;
    private int mCircleColor;
    private int mStrokeWidth;
    private int mCircleSpacing;
    private int mCircleCount;

    private int mCurrentIndex;
    private List<PointF> mCirclePoints = new ArrayList<PointF>();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DummyCircleNavigator(Context context) {
        super(context);
        mRadius = UIUtil.dip2px(context, 3);
        mCircleSpacing = UIUtil.dip2px(context, 8);
        mStrokeWidth = UIUtil.dip2px(context, 1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        prepareCirclePoints();
    }

    private void prepareCirclePoints() {
        mCirclePoints.clear();
        if (mCircleCount > 0) {
            int y = getHeight() / 2;
            int measureWidth = mCircleCount * mRadius * 2 + (mCircleCount - 1) * mCircleSpacing;
            int centerSpacing = mRadius * 2 + mCircleSpacing;
            int startX = (getWidth() - measureWidth) / 2 + mRadius;
            for (int i = 0; i < mCircleCount; i++) {
                PointF pointF = new PointF(startX, y);
                mCirclePoints.add(pointF);
                startX += centerSpacing;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDeselectedCircles(canvas);
        drawSelectedCircle(canvas);
    }

    private void drawDeselectedCircles(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mCircleColor);
        for (int i = 0, j = mCirclePoints.size(); i < j; i++) {
            PointF pointF = mCirclePoints.get(i);
            canvas.drawCircle(pointF.x, pointF.y, mRadius, mPaint);
        }
    }

    private void drawSelectedCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        if (mCirclePoints.size() > 0) {
            float selectedCircleX = mCirclePoints.get(mCurrentIndex).x;
            canvas.drawCircle(selectedCircleX, getHeight() / 2, mRadius, mPaint);
        }
    }

    // 被添加到 magicindicator 时调用
    @Override
    public void onAttachToMagicIndicator() {
    }

    // 从 magicindicator 上移除时调用
    @Override
    public void onDetachFromMagicIndicator() {
    }

    // 当指示数目改变时调用
    @Override
    public void notifyDataSetChanged() {
        prepareCirclePoints();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
        invalidate();
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
        invalidate();
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        invalidate();
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        mRadius = radius;
        prepareCirclePoints();
        invalidate();
    }

    public int getCircleSpacing() {
        return mCircleSpacing;
    }

    public void setCircleSpacing(int circleSpacing) {
        mCircleSpacing = circleSpacing;
        prepareCirclePoints();
        invalidate();
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public int getCircleCount() {
        return mCircleCount;
    }

    /**
     * notifyDataSetChanged应该紧随其后调用
     *
     * @param circleCount
     */
    public void setCircleCount(int circleCount) {
        mCircleCount = circleCount;
    }
}
