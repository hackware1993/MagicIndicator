package net.lucode.hackware.magicindicator.buildins.commonnavigator.abs;

/**
 * 抽象的指示器标题，适用于CommonNavigator
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public interface IPagerTitleView {
    /**
     * 被选中
     */
    void onSelect(int index);

    /**
     * 未被选中
     */
    void onDeselect(int index);

    /**
     * 离开
     *
     * @param offset 离开的百分比, 0.0f - 1.0f
     */
    void onLeave(int index, float offset, boolean leftToRight);

    /**
     * 进入
     *
     * @param offset 进入的百分比, 0.0f - 1.0f
     */
    void onEnter(int index, float offset, boolean leftToRight);

    /**
     * ViewPager滚动变化对调
     *
     * @param state
     */
    void onPageScrollStateChanged(int state);
}
