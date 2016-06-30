package net.lucode.hackware.magicindicator.buildins.circlenavigator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import net.lucode.hackware.magicindicator.NavigatorHelper;
import net.lucode.hackware.magicindicator.UIUtil;
import net.lucode.hackware.magicindicator.abs.IPagerNavigator;

import java.util.ArrayList;
import java.util.List;

/**
 * 圆圈式的指示器
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class CircleNavigator extends View implements IPagerNavigator, NavigatorHelper.OnNavigatorScrollListener {
    private int mRadius;
    private int mCircleColor;
    private int mStrokeWidth;
    private int mCircleSpacing;
    private Interpolator mStartInterpolator = new LinearInterpolator();
    private Paint mPaint;

    private NavigatorHelper mNavigatorHelper;
    private List<PointF> mCirclePoints = new ArrayList<PointF>();
    private float mIndicatorX;

    public CircleNavigator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mNavigatorHelper = new NavigatorHelper();
        mNavigatorHelper.setNavigatorScrollListener(this);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setRadius(UIUtil.dip2px(context, 3));
        setCircleSpacing(UIUtil.dip2px(context, 8));
        setStrokeWidth(UIUtil.dip2px(context, 1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircles(canvas);
        drawIndicator(canvas);
    }

    private void drawCircles(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mCircleColor);
        for (int i = 0, j = mCirclePoints.size(); i < j; i++) {
            PointF pointF = mCirclePoints.get(i);
            canvas.drawCircle(pointF.x, pointF.y, mRadius, mPaint);
        }
    }

    private void drawIndicator(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mIndicatorX, getHeight() / 2, mRadius, mPaint);
    }

    private void prepareCirclePoints() {
        mCirclePoints.clear();
        int count = mNavigatorHelper.getTotalCount();
        int y = getHeight() / 2;
        int measureWidth = count * mRadius * 2 + (count - 1) * mCircleSpacing;
        int centerSpacing = mRadius * 2 + mCircleSpacing;
        int startX = (getWidth() - measureWidth) / 2 + mRadius;
        for (int i = 0; i < count; i++) {
            PointF pointF = new PointF(startX, y);
            mCirclePoints.add(pointF);
            startX += centerSpacing;
        }
        mIndicatorX = mCirclePoints.get(mNavigatorHelper.getCurrentIndex()).x;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mNavigatorHelper.onPageScrolled(position, positionOffset, positionOffsetPixels);

        if (mCirclePoints.isEmpty()) {
            return;
        }

        int nextPosition = Math.min(mCirclePoints.size() - 1, position + 1);
        PointF current = mCirclePoints.get(position);
        PointF next = mCirclePoints.get(nextPosition);

        mIndicatorX = current.x + (next.x - current.x) * mStartInterpolator.getInterpolation(positionOffset);

        invalidate();
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
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        prepareCirclePoints();
    }

    @Override
    public void onAttachToMagicIndicator() {
    }

    @Override
    public void notifyDataSetChanged() {
        prepareCirclePoints();
        invalidate();
    }

    @Override
    public void onDetachFromMagicIndicator() {
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        mRadius = radius;
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

    public int getCircleSpacing() {
        return mCircleSpacing;
    }

    public void setCircleSpacing(int circleSpacing) {
        mCircleSpacing = circleSpacing;
        invalidate();
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

    public int getCount() {
        return mNavigatorHelper.getTotalCount();
    }

    public void setCount(int count) {
        mNavigatorHelper.setTotalCount(count);  // 此处不调用invalidate，让外部调用notifyDataSetChanged
    }

    @Override
    public void onEnter(int index, float enterPercent, boolean leftToRight) {
    }

    @Override
    public void onLeave(int index, float leavePercent, boolean leftToRight) {
    }

    @Override
    public void onSelected(int index) {
    }

    @Override
    public void onDeselected(int index) {
    }
}
