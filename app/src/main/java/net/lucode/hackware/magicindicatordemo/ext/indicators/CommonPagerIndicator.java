package net.lucode.hackware.magicindicatordemo.ext.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.List;

/**
 * 通用的indicator，支持外面设置Drawable
 * Created by hackware on 2016/11/14.
 */

public class CommonPagerIndicator extends View implements IPagerIndicator {
    public static final int MODE_MATCH_EDGE = 0;   // drawable宽度 == title宽度 - 2 * mXOffset
    public static final int MODE_WRAP_CONTENT = 1;    // drawable宽度 == title内容宽度 - 2 * mXOffset
    public static final int MODE_EXACTLY = 2;

    private int mMode;  // 默认为MODE_MATCH_EDGE模式
    private Drawable mIndicatorDrawable;

    // 控制动画
    private Interpolator mStartInterpolator = new LinearInterpolator();
    private Interpolator mEndInterpolator = new LinearInterpolator();

    private float mDrawableHeight;
    private float mDrawableWidth;
    private float mYOffset;
    private float mXOffset;

    private List<PositionData> mPositionDataList;
    private Rect mDrawableRect = new Rect();

    public CommonPagerIndicator(Context context) {
        super(context);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mIndicatorDrawable == null) {
            return;
        }

        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }

        // 计算锚点位置
        PositionData current = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position);
        PositionData next = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position + 1);

        float leftX;
        float nextLeftX;
        float rightX;
        float nextRightX;
        if (mMode == MODE_MATCH_EDGE) {
            leftX = current.mLeft + mXOffset;
            nextLeftX = next.mLeft + mXOffset;
            rightX = current.mRight - mXOffset;
            nextRightX = next.mRight - mXOffset;
            mDrawableRect.top = (int) mYOffset;
            mDrawableRect.bottom = (int) (getHeight() - mYOffset);
        } else if (mMode == MODE_WRAP_CONTENT) {
            leftX = current.mContentLeft + mXOffset;
            nextLeftX = next.mContentLeft + mXOffset;
            rightX = current.mContentRight - mXOffset;
            nextRightX = next.mContentRight - mXOffset;
            mDrawableRect.top = (int) (current.mContentTop - mYOffset);
            mDrawableRect.bottom = (int) (current.mContentBottom + mYOffset);
        } else {    // MODE_EXACTLY
            leftX = current.mLeft + (current.width() - mDrawableWidth) / 2;
            nextLeftX = next.mLeft + (next.width() - mDrawableWidth) / 2;
            rightX = current.mLeft + (current.width() + mDrawableWidth) / 2;
            nextRightX = next.mLeft + (next.width() + mDrawableWidth) / 2;
            mDrawableRect.top = (int) (getHeight() - mDrawableHeight - mYOffset);
            mDrawableRect.bottom = (int) (getHeight() - mYOffset);
        }

        mDrawableRect.left = (int) (leftX + (nextLeftX - leftX) * mStartInterpolator.getInterpolation(positionOffset));
        mDrawableRect.right = (int) (rightX + (nextRightX - rightX) * mEndInterpolator.getInterpolation(positionOffset));
        mIndicatorDrawable.setBounds(mDrawableRect);

        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIndicatorDrawable != null) {
            mIndicatorDrawable.draw(canvas);
        }
    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDataList = dataList;
    }

    public Drawable getIndicatorDrawable() {
        return mIndicatorDrawable;
    }

    public void setIndicatorDrawable(Drawable indicatorDrawable) {
        mIndicatorDrawable = indicatorDrawable;
    }

    public Interpolator getStartInterpolator() {
        return mStartInterpolator;
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
    }

    public Interpolator getEndInterpolator() {
        return mEndInterpolator;
    }

    public void setEndInterpolator(Interpolator endInterpolator) {
        mEndInterpolator = endInterpolator;
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        if (mode == MODE_EXACTLY || mode == MODE_MATCH_EDGE || mode == MODE_WRAP_CONTENT) {
            mMode = mode;
        } else {
            throw new IllegalArgumentException("mode " + mode + " not supported.");
        }
    }

    public float getDrawableHeight() {
        return mDrawableHeight;
    }

    public void setDrawableHeight(float drawableHeight) {
        mDrawableHeight = drawableHeight;
    }

    public float getDrawableWidth() {
        return mDrawableWidth;
    }

    public void setDrawableWidth(float drawableWidth) {
        mDrawableWidth = drawableWidth;
    }

    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float yOffset) {
        mYOffset = yOffset;
    }

    public float getXOffset() {
        return mXOffset;
    }

    public void setXOffset(float xOffset) {
        mXOffset = xOffset;
    }
}
