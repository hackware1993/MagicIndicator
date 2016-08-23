package net.lucode.hackware.magicindicator;

import android.support.v4.view.ViewPager;

import net.lucode.hackware.magicindicator.abs.AbsDelegate;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

/**
 * Created by hackware on 2016/8/18.
 */

public class EasyViewPagerDelegate extends AbsDelegate {
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private CommonNavigator mCommonNavigator;
    private int mSelectedColor;
    private int mNormalColor;
    private int mLineColor;
    private float mLineHeight;

    private EasyViewPagerDelegate(MagicIndicator magicIndicator, ViewPager viewPager) {
        mMagicIndicator = magicIndicator;
        mViewPager = viewPager;
    }

    public static EasyViewPagerDelegate with(MagicIndicator magicIndicator, ViewPager viewPager) {
        return new EasyViewPagerDelegate(magicIndicator, viewPager);
    }

    /**
     * selectedColor is like 0xffffff，not color resId
     *
     * @param selectedColor
     * @return
     */
    public EasyViewPagerDelegate selectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
        return this;
    }

    public EasyViewPagerDelegate normalColor(int normalColor) {
        mNormalColor = normalColor;
        return this;
    }

    public EasyViewPagerDelegate lineColor(int lineColor) {
        mLineColor = lineColor;
        return this;
    }

    public void bind() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mMagicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mMagicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mMagicIndicator.onPageScrollStateChanged(state);
            }
        });
    }

    @Override
    public void delegate() {
    }
}
