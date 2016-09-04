package net.lucode.hackware.magicindicator;

import android.support.v4.view.ViewPager;

/**
 * ViewPager委托类，使得MagicIndicator和ViewPager集成更方便
 * Created by hackware on 2016/8/17.
 */

public class SimpleViewPagerDelegate {
    private ViewPager mViewPager;
    private MagicIndicator mMagicIndicator;

    private SimpleViewPagerDelegate(MagicIndicator magicIndicator, ViewPager viewPager) {
        mMagicIndicator = magicIndicator;
        mViewPager = viewPager;
    }

    public static SimpleViewPagerDelegate with(MagicIndicator magicIndicator, ViewPager viewPager) {
        return new SimpleViewPagerDelegate(magicIndicator, viewPager);
    }

    public void delegate() {
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
}
