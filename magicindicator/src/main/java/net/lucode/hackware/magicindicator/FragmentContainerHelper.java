package net.lucode.hackware.magicindicator;

import android.animation.Animator;
import android.animation.ValueAnimator;


/**
 * 使得MagicIndicator在FragmentContainer中使用
 * <p>
 * Created by hackware on 2016/9/4.
 */

public class FragmentContainerHelper implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private MagicIndicator mMagicIndicator;
    private ValueAnimator mScrollAnimator;

    public FragmentContainerHelper(MagicIndicator magicIndicator) {
        mMagicIndicator = magicIndicator;
    }

    public void handlePageSelected(int selectedIndex) {
        if (mScrollAnimator == null || !mScrollAnimator.isRunning()) {
            mMagicIndicator.onPageScrollStateChanged(ScrollState.SCROLL_STATE_SETTLING);
        }
        mMagicIndicator.onPageSelected(selectedIndex);
        float currentPositionOffsetSum = 0.0f; // position = 0, positionOffset = 0.0f
        if (mScrollAnimator != null) {
            currentPositionOffsetSum = (Float) mScrollAnimator.getAnimatedValue();
            mScrollAnimator.cancel();
            mScrollAnimator = null;
        }
        mScrollAnimator = new ValueAnimator();
        mScrollAnimator.setFloatValues(currentPositionOffsetSum, selectedIndex);    // position = selectedIndex, positionOffset = 0.0f
        mScrollAnimator.addUpdateListener(this);
        mScrollAnimator.addListener(this);
        mScrollAnimator.start();
    }

    public MagicIndicator getMagicIndicator() {
        return mMagicIndicator;
    }

    public void setMagicIndicator(MagicIndicator magicIndicator) {
        mMagicIndicator = magicIndicator;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float positionOffsetSum = (Float) animation.getAnimatedValue();
        int position = (int) positionOffsetSum;
        float positionOffset = positionOffsetSum - position;
        mMagicIndicator.onPageScrolled(position, positionOffset, 0);
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mMagicIndicator.onPageScrollStateChanged(ScrollState.SCROLL_STATE_IDLE);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }
}
