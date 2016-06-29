package net.lucode.hackware.magicindicator.buildins.commonnavigator.titles;

import android.content.Context;
import android.view.View;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

/**
 * 空指示器标题
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class DummyPagerTitleView extends View implements IPagerTitleView {

    public DummyPagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onSelected(int index) {
    }

    @Override
    public void onDeselected(int index) {
    }

    @Override
    public void onLeave(int index, float leavePercent, boolean leftToRight) {
    }

    @Override
    public void onEnter(int index, float enterPercent, boolean leftToRight) {
    }
}
