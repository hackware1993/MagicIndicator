package net.lucode.hackware.magicindicator;

import android.support.v4.view.ViewPager;

import net.lucode.hackware.magicindicator.abs.AbsDelegate;
import net.lucode.hackware.magicindicator.abs.IPagerNavigator;

/**
 * ViewPager委托类，使得MagicIndicator和ViewPager集成更方便
 * Created by hackware on 2016/8/17.
 */

public class SimpleViewPagerDelegate extends AbsDelegate {
    private ViewPager mViewPager;

    public SimpleViewPagerDelegate(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public IPagerNavigator delegateMagic(final MagicIndicator magicIndicator, IPagerNavigator pagerNavigator) {
        if (mViewPager == null) {
            return pagerNavigator;
        }

        // 很简单，都能看懂
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });

        return pagerNavigator;
    }
}
