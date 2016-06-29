package net.lucode.hackware.magicindicator.abs;

/**
 * 抽象的ViewPager导航器
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public interface IPagerNavigator {
    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);

    void onAttachToMagicIndicator();

    void onDetachFromMagicIndicator();

    void notifyDataSetChanged();
}
