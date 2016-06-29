package net.lucode.hackware.magicindicator.buildins.commonnavigator.titles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import net.lucode.hackware.magicindicator.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

/**
 * 类似今日头条切换效果的指示器标题
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class ClipPagerTitleView extends View implements IPagerTitleView {
    private String mText;
    private int mTextSize;
    private int mTextColor;
    private int mClipColor;
    private boolean mLeftToRight;
    private float mClipPercent;

    private Paint mPaint;
    private Rect mTextBounds = new Rect();

    public ClipPagerTitleView(Context context) {
        super(context);
        mTextSize = UIUtil.dip2px(context, 16);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        int padding = UIUtil.dip2px(context, 10);
        setPadding(padding, 0, padding, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureTextBounds();
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int result = size;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                int width = mTextBounds.width() + getPaddingLeft() + getPaddingRight();
                result = Math.min(width, size);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = mTextBounds.width() + getPaddingLeft() + getPaddingRight();
                break;
            default:
                break;
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int result = size;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                int height = mTextBounds.height() + getPaddingTop() + getPaddingBottom();
                result = Math.min(height, size);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = mTextBounds.height() + getPaddingTop() + getPaddingBottom();
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x = (getWidth() - mTextBounds.width()) / 2;
        int y = (getHeight() + mTextBounds.height()) / 2;
        mPaint.setColor(mTextColor);
        canvas.drawText(mText, x, y, mPaint);
        mPaint.setColor(mClipColor);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        if (mLeftToRight) {
            canvas.clipRect(0, 0, getWidth() * mClipPercent, getHeight());
        } else {
            canvas.clipRect(getWidth() * (1 - mClipPercent), 0, getWidth(), getHeight());
        }
        canvas.drawText(mText, x, y, mPaint);
        canvas.restore();
    }

    @Override
    public void onSelect(int index) {
    }

    @Override
    public void onDeselect(int index) {
    }

    @Override
    public void onLeave(int index, float leavePercent, boolean leftToRight) {
        mLeftToRight = !leftToRight;
        mClipPercent = 1.0f - leavePercent;
        invalidate();
    }

    @Override
    public void onEnter(int index, float enterPercent, boolean leftToRight) {
        mLeftToRight = leftToRight;
        mClipPercent = enterPercent;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void measureTextBounds() {
        mPaint.getTextBounds(mText, 0, mText == null ? 0 : mText.length(), mTextBounds);
    }

    public boolean isLeftToRight() {
        return mLeftToRight;
    }

    public void setLeftToRight(boolean leftToRight) {
        mLeftToRight = leftToRight;
    }

    public float getClipPercent() {
        return mClipPercent;
    }

    public void setClipPercent(float clipPercent) {
        mClipPercent = clipPercent;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getClipColor() {
        return mClipColor;
    }

    public void setClipColor(int clipColor) {
        mClipColor = clipColor;
    }
}
