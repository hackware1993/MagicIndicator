# MagicIndicator
A powerful, customizable and extensible ViewPager indicator framework.

![magicindicaotor.gif](https://github.com/hackware1993/MagicIndicator/blob/master/magicindicator.gif)

# Usage
Simple steps, you can integrate **MagicIndicator**:

1. checkout out **MagicIndicator**, which contains source code and demo
2. import module **magicindicator** and add dependency:

  ```
  compile project(':magicindicator')
  ```
  
3. add **magicindicator** to your layout xml:

  ```
  <?xml version="1.0" encoding="utf-8"?>
  <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:orientation="vertical"
      tools:context="net.lucode.hackware.magicindicatordemo.MainActivity">
  
      <net.lucode.hackware.magicindicator.MagicIndicator
          android:id="@+id/magic_indicator"
          android:layout_width="match_parent"
          android:layout_height="40dp" />
  
      <android.support.v4.view.ViewPager
          android:id="@+id/view_pager"
          android:layout_width="match_parent"
          android:layout_height="0dp"
          android:layout_weight="1" />
  
  </LinearLayout>
  ```

4. find **magicindicator** through code, initialize it:

  ```
  MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
  CommonNavigator commonNavigator = new CommonNavigator(this);
  commonNavigator.setAdapter(new CommonNavigatorAdapter() {
  
      @Override
      public int getCount() {
          return mTitleDataList == null ? 0 : mTitleDataList.size();
      }
  
      @Override
      public IPagerTitleView getTitleView(Context context, final int index) {
          ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
          colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
          colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
          colorTransitionPagerTitleView.setText(mTitleDataList.get(index));
          colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  mViewPager.setCurrentItem(index);
              }
          });
          return colorTransitionPagerTitleView;
      }
  
      @Override
      public IPagerIndicator getIndicator(Context context) {
          LinePagerIndicator indicator = new LinePagerIndicator(context);
          indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
          return indicator;
      }
  });
  magicIndicator.setNavigator(commonNavigator);
  ```
  
5. bind **magicindicator** to ViewPager:

  ```
  SimpleViewPagerDelegate.with(magicIndicator, mViewPager).delegate();
  ```

# Extend
**MagicIndicator** is easy to extend:

1. extend **IPagerTitleView** to custom tab:

  ```
  public class MyPagerTitleView extends View implements IPagerTitleView {
  
      public MyPagerTitleView(Context context) {
          super(context);
      }
  
      @Override
      public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
      }
  
      @Override
      public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
      }
  
      @Override
      public void onSelected(int index, int totalCount) {
      }
  
      @Override
      public void onDeselected(int index, int totalCount) {
      }
  }
  ```

2. extend **IPagerIndicator** to custom indicator:

  ```
  public class MyPagerIndicator extends View implements IPagerIndicator {
  
      public MyPagerIndicator(Context context) {
          super(context);
      }
  
      @Override
      public void onPageSelected(int position) {
      }
  
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }
  
      @Override
      public void onPageScrollStateChanged(int state) {
      }
  
      @Override
      public void onPositionDataProvide(List<PositionData> dataList) {
      }
  }
  ```
  
Now, enjoy yourself!

More effects adding...

[![Codewake](https://www.codewake.com/badges/ask_question.svg)](https://www.codewake.com/p/magicindicator)

# Who developed?

hackware1993@gmail.com

cfb1993@163.com

QQ Group：373360748

An intermittent perfectionist.

Visit [My Blog](http://hackware.lucode.net) for more articles about MagicIndicator.
