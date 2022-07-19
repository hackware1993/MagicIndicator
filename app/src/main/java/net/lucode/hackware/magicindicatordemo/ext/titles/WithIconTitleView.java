package net.lucode.hackware.magicindicatordemo.ext.titles;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;
import net.lucode.hackware.magicindicatordemo.R;

public class WithIconTitleView extends FrameLayout implements IMeasurablePagerTitleView {
    protected int mSelectedColor;
    protected int mNormalColor;
    private TextView mTabWithIconText;

    public WithIconTitleView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.simple_title_with_icon_layout, this, true);
        mTabWithIconText = findViewById(R.id.tab_with_icon_text);
    }

    @Override
    public int getContentLeft() {
        return getLeft() + mTabWithIconText.getLeft();
    }

    @Override
    public int getContentTop() {
        return getTop();
    }

    @Override
    public int getContentRight() {
        return getLeft() + mTabWithIconText.getRight();
    }

    @Override
    public int getContentBottom() {
        return getBottom();
    }

    @Override
    public void onSelected(int index, int totalCount) {
        mTabWithIconText.setTextColor(mSelectedColor);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        mTabWithIconText.setTextColor(mNormalColor);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
    }

    public int getNormalColor() {
        return mNormalColor;
    }

    public void setNormalColor(int normalColor) {
        mNormalColor = normalColor;
    }

}
