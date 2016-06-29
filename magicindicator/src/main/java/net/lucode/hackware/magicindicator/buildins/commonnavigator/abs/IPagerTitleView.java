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
    void onSelected(int index);

    /**
     * 未被选中
     */
    void onDeselected(int index);

    /**
     * 离开
     *
     * @param leavePercent 离开的百分比, 0.0f - 1.0f
     */
    void onLeave(int index, float leavePercent, boolean leftToRight);

    /**
     * 进入
     *
     * @param enterPercent 进入的百分比, 0.0f - 1.0f
     */
    void onEnter(int index, float enterPercent, boolean leftToRight);
}
