package net.lucode.hackware.magicindicator.buildins.commonnavigator.titles;

import android.content.Context;

import net.lucode.hackware.magicindicator.ArgbEvaluatorHolder;


/**
 * 两种颜色过渡的指示器标题
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class ColorTransitionPagerTitleView extends SimplePagerTitleView {

    public ColorTransitionPagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onSelect(int index) {
    }

    @Override
    public void onDeselect(int index) {
    }

    @Override
    public void onLeave(int index, float leavePercent, boolean leftToRight) {
        int color = (Integer) ArgbEvaluatorHolder.eval(leavePercent, mSelectedColor, mNormalColor);
        setTextColor(color);
    }

    @Override
    public void onEnter(int index, float enterPercent, boolean leftToRight) {
        int color = (Integer) ArgbEvaluatorHolder.eval(enterPercent, mNormalColor, mSelectedColor);
        setTextColor(color);
    }
}
