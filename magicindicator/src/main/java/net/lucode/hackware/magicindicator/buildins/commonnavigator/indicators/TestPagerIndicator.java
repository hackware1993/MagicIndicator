package net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.List;


/**
 * 用于测试的指示器，可用来检测自定义的IMeasurablePagerTitleView是否正确测量内容区域
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class TestPagerIndicator extends View implements IPagerIndicator {
    protected Paint mPaint;
    protected int mOutRectColor;
    protected int mInnerRectColor;
    protected RectF mOutRect;
    protected RectF mInnerRect;

    protected List<PositionData> mPositionDataList;

    public TestPagerIndicator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        setOutRectColor(Color.RED);
        setInnerRectColor(Color.GREEN);
    }

    public int getOutRectColor() {
        return mOutRectColor;
    }

    public void setOutRectColor(int outRectColor) {
        mOutRectColor = outRectColor;
    }

    public int getInnerRectColor() {
        return mInnerRectColor;
    }

    public void setInnerRectColor(int innerRectColor) {
        mInnerRectColor = innerRectColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mOutRect != null) {
            mPaint.setColor(mOutRectColor);
            canvas.drawRect(mOutRect, mPaint);
        }
        if (mInnerRect != null) {
            mPaint.setColor(mInnerRectColor);
            canvas.drawRect(mInnerRect, mPaint);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }

        // 计算锚点位置
        int currentPosition = Math.min(mPositionDataList.size() - 1, position);
        int nextPosition = Math.min(mPositionDataList.size() - 1, position + 1);
        PositionData current = mPositionDataList.get(currentPosition);
        PositionData next = mPositionDataList.get(nextPosition);

        float outLeft = current.mLeft + (next.mLeft - current.mLeft) * positionOffset;
        float outRight = current.mRight + (next.mRight - current.mRight) * positionOffset;
        mOutRect = new RectF(outLeft, current.mTop, outRight, current.mBottom);

        float innerLeft = current.mContentLeft + (next.mContentLeft - current.mContentLeft) * positionOffset;
        float innerRight = current.mContentRight + (next.mContentRight - current.mContentRight) * positionOffset;
        mInnerRect = new RectF(innerLeft, current.mContentTop, innerRight, current.mContentBottom);

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
