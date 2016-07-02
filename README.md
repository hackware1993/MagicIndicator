# MagicIndicator
A powerful and extensible ViewPager indicator framework.

![magicindicaotor.gif](https://github.com/hackware1993/MagicIndicator/blob/master/magicindicator.gif)

# Usage
Simple steps, you can integrate **MagicIndicator**:

1. checkout out **MagicIndicator**, which contains source code and demo.
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
  android:background="@color/activity_bg"
  android:orientation="vertical"
  tools:context="net.lucode.hackware.magicindicatordemo.MainActivity">
  
  <net.lucode.hackware.magicindicator.MagicIndicator
      android:id="@+id/magic_indicator"
      android:layout_width="match_parent"
      android:layout_height="@dimen/navigator_common_height"
      android:background="#d43d3d" />
  
  <android.support.v4.view.ViewPager
      android:id="@+id/view_pager"
      android:layout_width="match_parent"
      android:layout_height="100dp"
      android:background="@android:color/white" />
  
  </LinearLayout>
  ```

4. find **magicindicator** through code, initialize it:

  ```
  final MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
  final CommonNavigator commonNavigator = new CommonNavigator(this);
  commonNavigator.setAdapter(new CommonNavigatorAdapter() {
  
  @Override
  public int getCount() {
      return mDataList == null ? 0 : mDataList.size();
  }
  
  @Override
  public IPagerTitleView getItemView(Context context, final int index) {
      ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
      clipPagerTitleView.setText(mDataList.get(index));
      clipPagerTitleView.setTextColor(Color.parseColor("#f2c4c4"));
      clipPagerTitleView.setClipColor(Color.WHITE);
      clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              mPager.setCurrentItem(index);
          }
      });
      return clipPagerTitleView;
  }
  
  @Override
  public IPagerIndicator getIndicator(Context context) {
      return null;
  }
  });
  magicIndicator.setNavigator(commonNavigator);
  ```
5. bind **magicindicator** to ViewPager:

  ```
  mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
  
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
  ```

Now, emjoy yourself !

More effects adding...

# Who developed?

hackware1993@gmail.com

cfb1993@163.com

An intermittent perfectionist

Visit [My Blog](http://hackware.lucode.net) for more articles about MagicIndicator.
