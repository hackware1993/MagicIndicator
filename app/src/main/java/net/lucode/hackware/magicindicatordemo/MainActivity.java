package net.lucode.hackware.magicindicatordemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.UIUtil;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.AbsorbPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.TriangularPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.DummyPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ScaleTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ViewPager mPager;
    private List<String> mDataList = new ArrayList<String>();

    String[] channels = new String[]{"科技", "汽车", "互联网", "hackware.lucode.net", "奇闻异事", "搞笑", "段子", "趣图", "健康", "时尚", "教育", "星座", "育儿", "游戏", "家居", "美食", "旅游", "历史", "情感"};

    {
        for (int i = 0; i < channels.length; i++) {
            mDataList.add(channels[i]);
        }
    }

    private PagerAdapter mAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView textView = new TextView(container.getContext());
            textView.setText(mDataList.get(position));
            textView.setGravity(Gravity.CENTER);
            container.addView(textView);
            return textView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);

        // 今日头条式
        final MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setFollowTouch(false);
        commonNavigator.setRightPadding(UIUtil.dip2px(MainActivity.this, 50));
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
                return null;    // 没有指示器，因为title的指示作用已经很明显了
            }
        });
        magicIndicator.setNavigator(commonNavigator);

        // 当前页不定位到中间
        final MagicIndicator magic_indicator1 = (MagicIndicator) findViewById(R.id.magic_indicator1);
        final CommonNavigator commonNavigator1 = new CommonNavigator(this);
        commonNavigator1.setFollowTouch(false);
        commonNavigator1.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(mDataList.get(index));
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setWrapContentMode(true);
                List<String> colorList = new ArrayList<String>();
                colorList.add("#ff4a42");
                colorList.add("#fcde64");
                colorList.add("#73e8f4");
                colorList.add("#76b0ff");
                colorList.add("#c683fe");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
        magic_indicator1.setNavigator(commonNavigator1);

        // 当前页始终定位到中间
        final MagicIndicator magic_indicator2 = (MagicIndicator) findViewById(R.id.magic_indicator2);
        final CommonNavigator commonNavigator2 = new CommonNavigator(this);
        commonNavigator2.setFollowTouch(false);
        commonNavigator2.setAlwaysScrollToCenter(true);
        commonNavigator2.setScrollPivotX(0.15f);
        commonNavigator2.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(mDataList.get(index));
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                List<String> colorList = new ArrayList<String>();
                colorList.add("#ff4a42");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
        magic_indicator2.setNavigator(commonNavigator2);

        // 自适应模式
        final MagicIndicator magic_indicator3 = (MagicIndicator) findViewById(R.id.magic_indicator3);
        final CommonNavigator commonNavigator3 = new CommonNavigator(this);
        commonNavigator3.setFitMode(true);  // 自适应模式
        commonNavigator3.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : 3;
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(mDataList.get(index));
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineHeight(UIUtil.dip2px(context, 6));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                List<String> colorList = new ArrayList<String>();
                colorList.add("#fcde64");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
        magic_indicator3.setNavigator(commonNavigator3);

        // 自适应模式，带插值器
        final MagicIndicator magic_indicator4 = (MagicIndicator) findViewById(R.id.magic_indicator4);
        final CommonNavigator commonNavigator4 = new CommonNavigator(this);
        commonNavigator4.setFitMode(true);  // 自适应模式
        commonNavigator4.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : 2;
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(mDataList.get(index));
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
                indicator.setLineHeight(UIUtil.dip2px(context, 1));
                List<String> colorList = new ArrayList<String>();
                colorList.add("#76b0ff");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
        magic_indicator4.setNavigator(commonNavigator4);

        // 缩放 + 颜色渐变
        final MagicIndicator magic_indicator5 = (MagicIndicator) findViewById(R.id.magic_indicator5);
        final CommonNavigator commonNavigator5 = new CommonNavigator(this);
        commonNavigator5.setAlwaysScrollToCenter(true);
        commonNavigator5.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ScaleTransitionPagerTitleView colorTransitionPagerTitleView = new ScaleTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(mDataList.get(index));
                colorTransitionPagerTitleView.setTextSize(18);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
                indicator.setLineHeight(UIUtil.dip2px(context, 1));
                List<String> colorList = new ArrayList<String>();
                colorList.add("#c683fe");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
        magic_indicator5.setNavigator(commonNavigator5);

        // 只有指示器，没有title
        final MagicIndicator magic_indicator6 = (MagicIndicator) findViewById(R.id.magic_indicator6);
        final CommonNavigator commonNavigator6 = new CommonNavigator(this);
        commonNavigator6.setFitMode(true);
        commonNavigator6.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : 5;
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                return new DummyPagerTitleView(context);
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineHeight(UIUtil.dip2px(context, 5));
                List<String> colorList = new ArrayList<String>();
                colorList.add("#76b0ff");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
        magic_indicator6.setNavigator(commonNavigator6);

        // 带吸附效果
        final MagicIndicator magic_indicator7 = (MagicIndicator) findViewById(R.id.magic_indicator7);
        final CommonNavigator commonNavigator7 = new CommonNavigator(this);
        commonNavigator7.setAlwaysScrollToCenter(true);
        commonNavigator7.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.BLACK);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                AbsorbPagerIndicator indicator = new AbsorbPagerIndicator(context);
                indicator.setLineHeight(UIUtil.dip2px(context, 6));
                List<String> colorList = new ArrayList<String>();
                colorList.add("#ff4a42");
                colorList.add("#fcde64");
                colorList.add("#73e8f4");
                colorList.add("#76b0ff");
                colorList.add("#c683fe");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
        magic_indicator7.setNavigator(commonNavigator7);

        // 贝塞尔曲线
        final MagicIndicator magic_indicator8 = (MagicIndicator) findViewById(R.id.magic_indicator8);
        final CommonNavigator commonNavigator8 = new CommonNavigator(this);
        commonNavigator8.setAlwaysScrollToCenter(true);
        commonNavigator8.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ScaleTransitionPagerTitleView colorTransitionPagerTitleView = new ScaleTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(mDataList.get(index));
                colorTransitionPagerTitleView.setTextSize(18);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
                List<String> colorList = new ArrayList<String>();
                colorList.add("#ff4a42");
                colorList.add("#fcde64");
                colorList.add("#73e8f4");
                colorList.add("#76b0ff");
                colorList.add("#c683fe");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
        magic_indicator8.setNavigator(commonNavigator8);

        // 天天快报式
        final MagicIndicator magic_indicator9 = (MagicIndicator) findViewById(R.id.magic_indicator9);
        final CommonNavigator commonNavigator9 = new CommonNavigator(this);
        commonNavigator9.setAlwaysScrollToCenter(true);
        commonNavigator9.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#e94220"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(Color.parseColor("#ebe4e3"));
                return indicator;
            }
        });
        magic_indicator9.setNavigator(commonNavigator9);

        // 小尖角式
        final MagicIndicator magic_indicator10 = (MagicIndicator) findViewById(R.id.magic_indicator10);
        final CommonNavigator commonNavigator10 = new CommonNavigator(this);
        commonNavigator10.setScrollPivotX(0.15f);
        commonNavigator10.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#e94220"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                TriangularPagerIndicator indicator = new TriangularPagerIndicator(context);
                indicator.setLineColor(Color.parseColor("#e94220"));
                return indicator;
            }
        });
        magic_indicator10.setNavigator(commonNavigator10);

        // 圆圈式
        final MagicIndicator magic_indicator11 = (MagicIndicator) findViewById(R.id.magic_indicator11);
        final CircleNavigator circleNavigator = new CircleNavigator(this);
        circleNavigator.setCircleCount(mDataList.size());
        circleNavigator.setCircleColor(Color.RED);
        circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                mPager.setCurrentItem(index);
            }
        });
        magic_indicator11.setNavigator(circleNavigator);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
                magic_indicator1.onPageScrolled(position, positionOffset, positionOffsetPixels);
                magic_indicator2.onPageScrolled(position, positionOffset, positionOffsetPixels);
                magic_indicator3.onPageScrolled(position, positionOffset, positionOffsetPixels);
                magic_indicator4.onPageScrolled(position, positionOffset, positionOffsetPixels);
                magic_indicator5.onPageScrolled(position, positionOffset, positionOffsetPixels);
                magic_indicator6.onPageScrolled(position, positionOffset, positionOffsetPixels);
                magic_indicator7.onPageScrolled(position, positionOffset, positionOffsetPixels);
                magic_indicator8.onPageScrolled(position, positionOffset, positionOffsetPixels);
                magic_indicator9.onPageScrolled(position, positionOffset, positionOffsetPixels);
                magic_indicator10.onPageScrolled(position, positionOffset, positionOffsetPixels);
                magic_indicator11.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                magicIndicator.onPageSelected(position);
                magic_indicator1.onPageSelected(position);
                magic_indicator2.onPageSelected(position);
                magic_indicator3.onPageSelected(position);
                magic_indicator4.onPageSelected(position);
                magic_indicator5.onPageSelected(position);
                magic_indicator6.onPageSelected(position);
                magic_indicator7.onPageSelected(position);
                magic_indicator8.onPageSelected(position);
                magic_indicator9.onPageSelected(position);
                magic_indicator10.onPageSelected(position);
                magic_indicator11.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
                magic_indicator1.onPageScrollStateChanged(state);
                magic_indicator2.onPageScrollStateChanged(state);
                magic_indicator3.onPageScrollStateChanged(state);
                magic_indicator4.onPageScrollStateChanged(state);
                magic_indicator5.onPageScrollStateChanged(state);
                magic_indicator6.onPageScrollStateChanged(state);
                magic_indicator7.onPageScrollStateChanged(state);
                magic_indicator8.onPageScrollStateChanged(state);
                magic_indicator9.onPageScrollStateChanged(state);
                magic_indicator10.onPageScrollStateChanged(state);
                magic_indicator11.onPageScrollStateChanged(state);
            }
        });

        mPager.setCurrentItem(1);

/*        mPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDataList.clear();
                mDataList.add("欢迎关注");
                mDataList.add("我的博客");
                mDataList.add("hackware.lucode.net");
                commonNavigator.notifyDataSetChanged();
                commonNavigator1.notifyDataSetChanged();
                commonNavigator2.notifyDataSetChanged();
                commonNavigator3.notifyDataSetChanged();
                commonNavigator4.notifyDataSetChanged();
                commonNavigator5.notifyDataSetChanged();
                commonNavigator6.notifyDataSetChanged();
                commonNavigator7.notifyDataSetChanged();
                commonNavigator8.notifyDataSetChanged();
                commonNavigator9.notifyDataSetChanged();
                commonNavigator10.notifyDataSetChanged();
                circleNavigator.setCount(mDataList.size());
                circleNavigator.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            }
        }, 10000);*/

//        mPager.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                commonNavigator.setAdapter(null);
//            }
//        }, 5000);
    }
}
