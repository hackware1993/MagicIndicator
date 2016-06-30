package net.lucode.hackware.magicindicator.buildins.commonnavigator.model;

/**
 * 保存指示器标题的坐标
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class PositionData {
    public int mLeft;
    public int mTop;
    public int mRight;
    public int mBottom;
    public int mContentLeft;
    public int mContentTop;
    public int mContentRight;
    public int mContentBottom;

    public int width() {
        return mRight - mLeft;
    }

    public int height() {
        return mBottom - mTop;
    }

    public int contentWidth() {
        return mContentRight - mContentLeft;
    }

    public int contentHeight() {
        return mContentBottom - mContentTop;
    }

    public int horizontalCenter() {
        return mLeft + width() / 2;
    }

    public int verticalCenter() {
        return mTop + height() / 2;
    }
}
