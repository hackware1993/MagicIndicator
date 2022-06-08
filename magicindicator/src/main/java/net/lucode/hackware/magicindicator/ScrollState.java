package net.lucode.hackware.magicindicator;

/**
 * 自定义滚动状态，消除对ViewPager的依赖
 * Created by hackware on 2016/8/27.
 */

public interface ScrollState {
    int SCROLL_STATE_IDLE = 0;
    int SCROLL_STATE_DRAGGING = 1;
    int SCROLL_STATE_SETTLING = 2;
}
