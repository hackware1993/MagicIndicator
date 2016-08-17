package net.lucode.hackware.magicindicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import net.lucode.hackware.magicindicator.abs.AbsDelegate;
import net.lucode.hackware.magicindicator.abs.IPagerNavigator;

/**
 * 整个框架的入口，核心
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class MagicIndicator extends FrameLayout {
    private IPagerNavigator mNavigator;
    private AbsDelegate mDelegate;

    public MagicIndicator(Context context) {
        super(context);
    }

    public MagicIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mNavigator != null) {
            mNavigator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    public void onPageSelected(int position) {
        if (mNavigator != null) {
            mNavigator.onPageSelected(position);
        }
    }

    public void onPageScrollStateChanged(int state) {
        if (mNavigator != null) {
            mNavigator.onPageScrollStateChanged(state);
        }
    }

    public IPagerNavigator getNavigator() {
        return mNavigator;
    }

    public void setNavigator(IPagerNavigator navigator) {
        setNavigator(navigator, false);
    }

    private void setNavigator(IPagerNavigator navigator, boolean ignoreDelegate) {
        if (!ignoreDelegate && mDelegate != null) {
            navigator = mDelegate.delegateMagic(this, navigator);
        }
        if (mNavigator == navigator) {
            return;
        }
        if (mNavigator != null) {
            mNavigator.onDetachFromMagicIndicator();
        }
        mNavigator = navigator;
        removeAllViews();
        if (mNavigator instanceof View) {
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView((View) mNavigator, lp);
            mNavigator.onAttachToMagicIndicator();
        }
    }

    public void setDelegate(AbsDelegate delegate) {
        mDelegate = delegate;
        if (mDelegate != null) {
            IPagerNavigator navigator = mDelegate.delegateMagic(this, mNavigator);
            setNavigator(navigator, true);
        }
    }
}
