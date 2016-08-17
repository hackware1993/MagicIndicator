package net.lucode.hackware.magicindicator.abs;

import net.lucode.hackware.magicindicator.MagicIndicator;

/**
 * 使用委托机制，简化使用
 * Created by hackware on 2016/8/17.
 */

public abstract class AbsDelegate {
    public IPagerNavigator delegateMagic(MagicIndicator magicIndicator, IPagerNavigator pagerNavigator) {
        return pagerNavigator;
    }
}
