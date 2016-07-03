package net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import net.lucode.hackware.magicindicator.ArgbEvaluatorHolder;
import net.lucode.hackware.magicindicator.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.List;

/**
 * 贝塞尔曲线ViewPager指示器，带颜色渐变
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class BezierPagerIndicator extends View implements IPagerIndicator {
    private List<PositionData> mPositionDataList;

    private float mLeftCircleRadius;
    private float mLeftCircleX;
    private float mRightCircleRadius;
    private float mRightCircleX;

    private float mYOffset;
    private float mMaxCircleRadius;
    private float mMinCircleRadius;

    private Paint mPaint;
    private Path mPath = new Path();

    private List<String> mColorList;
    private Interpolator mStartInterpolator = new AccelerateInterpolator();
    private Interpolator mEndInterpolator = new DecelerateInterpolator();

    public BezierPagerIndicator(Context context) {
        super(context);
        init(context);
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new AccelerateInterpolator();
        }
    }

    public void setEndInterpolator(Interpolator endInterpolator) {
        mEndInterpolator = endInterpolator;
        if (mEndInterpolator == null) {
            mEndInterpolator = new DecelerateInterpolator();
        }
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        setMaxCircleRadius(UIUtil.dip2px(context, 3.5));
        setMinCircleRadius(UIUtil.dip2px(context, 2));
        setYOffset(UIUtil.dip2px(context, 1.5));
    }

    public float getMaxCircleRadius() {
        return mMaxCircleRadius;
    }

    public void setMaxCircleRadius(float maxCircleRadius) {
        mMaxCircleRadius = maxCircleRadius;
    }

    public float getMinCircleRadius() {
        return mMinCircleRadius;
    }

    public void setMinCircleRadius(float minCircleRadius) {
        mMinCircleRadius = minCircleRadius;
    }

    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float yOffset) {
        mYOffset = yOffset;
    }

    public void setColorList(List<String> colorList) {
        mColorList = colorList;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mLeftCircleX, getHeight() - mYOffset - mMaxCircleRadius, mLeftCircleRadius, mPaint);
        canvas.drawCircle(mRightCircleX, getHeight() - mYOffset - mMaxCircleRadius, mRightCircleRadius, mPaint);
        drawBezierCurve(canvas);
    }

    /**
     * 绘制贝塞尔曲线
     *
     * @param canvas
     */
    private void drawBezierCurve(Canvas canvas) {
        mPath.reset();
        float y = getHeight() - mYOffset - mMaxCircleRadius;
        mPath.moveTo(mRightCircleX, y);
        mPath.lineTo(mRightCircleX, y - mRightCircleRadius);
        mPath.quadTo(mRightCircleX + (mLeftCircleX - mRightCircleX) / 2.0f, y, mLeftCircleX, y - mLeftCircleRadius);
        mPath.lineTo(mLeftCircleX, y + mLeftCircleRadius);
        mPath.quadTo(mRightCircleX + (mLeftCircleX - mRightCircleX) / 2.0f, y, mRightCircleX, y + mRightCircleRadius);
        mPath.close();  // 闭合
        canvas.drawPath(mPath, mPaint);
    }

    protected void setLeftCircleX(float leftCircleX) {
        mLeftCircleX = leftCircleX;
    }

    protected void setLeftCircleRadius(float leftCircleRadius) {
        mLeftCircleRadius = leftCircleRadius;
    }

    protected void setRightCircleRadius(float rightCircleRadius) {
        mRightCircleRadius = rightCircleRadius;
    }

    protected void setRightCircleX(float rightCircleX) {
        mRightCircleX = rightCircleX;
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

        float rightCircleX = leftX + (rightX - leftX) * mEndInterpolator.getInterpolation(positionOffset);
        setRightCircleX(rightCircleX);

        float LeftCircleX = leftX + (rightX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
        setLeftCircleX(LeftCircleX);

        float rightCircleRadius = mMinCircleRadius + (mMaxCircleRadius - mMinCircleRadius) * mStartInterpolator.getInterpolation(positionOffset);
        setRightCircleRadius(rightCircleRadius);

        float leftCircleRadius = mMaxCircleRadius + (mMinCircleRadius - mMaxCircleRadius) * mEndInterpolator.getInterpolation(positionOffset);
        setLeftCircleRadius(leftCircleRadius);

        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDataList = dataList;
    }
}
