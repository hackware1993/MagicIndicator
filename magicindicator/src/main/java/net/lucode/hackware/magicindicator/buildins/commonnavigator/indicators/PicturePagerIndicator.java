package net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.List;

/**
 * 图片指示器 传入图片
 */
public class PicturePagerIndicator extends View implements IPagerIndicator {

    public static final int Center_Bottom_Gravity = 1;  // 下方中心
    public static final int End_Bottom_Gravity = 2;     // 下方靠右
    public static final int Start_Bottom_Gravity = 3;   // 下方靠左

    private List<PositionData> mPositionDataList;

    private float mRadius;
    private float firstPoint;
    private float nextPoint;

    private int itemWidth = 0;
    private int itemHeight = 0;

    private int picGravity = 1;  // 图片位置  1: 下方中心  2： 下方靠右 3： 下方靠左

    private Interpolator mStartInterpolator = new LinearInterpolator();

    public PicturePagerIndicator(Context context) {
        super(context);
        init(context);
    }

    public PicturePagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PicturePagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Paint mPaint;
    private Path mPath = new Path();
    private Bitmap mBitmap = null;

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmap == null) return;
//        canvas.drawBitmap(mBitmap, 0, 0, null);
        // 指定图片绘制区域
        Rect src = new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight());
        // 指定图片在屏幕上显示的区域(原图大小)
        Rect dst = null;
        if(picGravity == 1){  // 居中绘制 高度占满
            dst =  new Rect(
                    (int)mRadius - mBitmap.getWidth(),
                    itemHeight - mBitmap.getHeight(),
                    (int)mRadius,
                    itemHeight);
        }else if(picGravity == 2){  // 右下角绘制
            dst =  new Rect(
                    (int)mRadius - mBitmap.getWidth(),
                    itemHeight - mBitmap.getHeight(),
                    (int)mRadius,
                    itemHeight);
        }
        assert dst != null;
        canvas.drawBitmap(mBitmap, src ,dst,null);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }

        // 计算锚点位置
        PositionData current = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position);
        PositionData next = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position + 1);

        itemWidth =  current.width() ;
        itemHeight = current.height();

        if(picGravity == End_Bottom_Gravity){
            float leftX = current.mLeft;
            float rightX = next.mLeft;
            mRadius = next.mLeft + next.width() * mStartInterpolator.getInterpolation(positionOffset);
            firstPoint = leftX;
            nextPoint = rightX;
        }else if(picGravity == Center_Bottom_Gravity){
            float leftX = current.mLeft + ((current.mRight - current.mLeft) >> 1);
            float rightX = next.mLeft + ((next.mRight - next.mLeft) >> 1);
            mRadius = leftX + (rightX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
            firstPoint = leftX;
            nextPoint = rightX;
        }
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDataList = dataList;
    }


    public void setmBitmap(Bitmap bitmap){
        this.mBitmap = bitmap;
    }

    public void setPicGravity(int picGravity){
        this.picGravity = picGravity;
    }
}
