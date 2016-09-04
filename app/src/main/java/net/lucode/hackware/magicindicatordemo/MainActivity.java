package net.lucode.hackware.magicindicatordemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.SimpleViewPagerDelegate;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.TriangularPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.DummyPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;
import net.lucode.hackware.magicindicatordemo.ext.navigator.ScaleCircleNavigator;
import net.lucode.hackware.magicindicatordemo.ext.titles.ColorFlipPagerTitleView;
import net.lucode.hackware.magicindicatordemo.ext.titles.ScaleTransitionPagerTitleView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    String[] channels = new String[]{"CUPCAKE", "DONUT", "ECLAIR", "GINGERBREAD", "HONEYCOMB", "ICE_CREAM_SANDWICH", "JELLY_BEAN", "KITKAT", "LOLLIPOP", "M", "NOUGAT"};
    private List<String> mDataList = Arrays.asList(channels);

    private ViewPager mViewPager;
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
            TextView textView = (TextView) object;
            String text = textView.getText().toString();
            int index = mDataList.indexOf(text);
            if (index >= 0) {
                return index;
            }
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mDataList.get(position);
        }
    };

    private FragmentContainerHelper mFragmentContainerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter);

        // 今日头条式
        final MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setSkimOver(true);  // 跨多个item切换时中间的item呈现 "掠过" 效果
        int padding = UIUtil.getScreenWidth(this) / 2;
        commonNavigator.setRightPadding(padding);
        commonNavigator.setLeftPadding(padding);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mDataList.get(index));
                clipPagerTitleView.setTextColor(Color.parseColor("#f2c4c4"));
                clipPagerTitleView.setClipColor(Color.WHITE);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
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
        SimpleViewPagerDelegate.with(magicIndicator, mViewPager).delegate();

        // 当前页不定位到中间
        final MagicIndicator magicIndicator1 = (MagicIndicator) findViewById(R.id.magic_indicator1);
        final CommonNavigator commonNavigator1 = new CommonNavigator(this);
        commonNavigator1.setScrollPivotX(0.25f);
        commonNavigator1.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#c8e6c9"));
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setTextSize(12);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setYOffset(UIUtil.dip2px(context, 3));
                indicator.setColors(Color.parseColor("#ffffff"));
                return indicator;
            }
        });
        magicIndicator1.setNavigator(commonNavigator1);
        SimpleViewPagerDelegate.with(magicIndicator1, mViewPager).delegate();

        // 当前页始终定位到中间
        final MagicIndicator magicIndicator2 = (MagicIndicator) findViewById(R.id.magic_indicator2);
        final CommonNavigator commonNavigator2 = new CommonNavigator(this);
        commonNavigator2.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

                if (index == 3) {
                    TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.simple_count_badge_layout, null);
                    textView.setText("3");
                    badgePagerTitleView.setBadgeView(textView);
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UIUtil.dip2px(context, 6)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
                }

                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#88ffffff"));
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);

                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(Color.parseColor("#40c4ff"));
                return indicator;
            }
        });
        magicIndicator2.setNavigator(commonNavigator2);
        SimpleViewPagerDelegate.with(magicIndicator2, mViewPager).delegate();

        // 动态增加、删除小红点
        commonNavigator2.postDelayed(new Runnable() {
            @Override
            public void run() {
                commonNavigator2.setReselectWhenLayout(false);

                BadgePagerTitleView badgePagerTitleView = (BadgePagerTitleView) commonNavigator2.getPagerTitleView(3);
                badgePagerTitleView.setBadgeView(null);

                BadgePagerTitleView badgePagerTitleView1 = (BadgePagerTitleView) commonNavigator2.getPagerTitleView(2);
                TextView textView = (TextView) LayoutInflater.from(badgePagerTitleView1.getContext()).inflate(R.layout.simple_count_badge_layout, null);
                textView.setText("1");
                badgePagerTitleView1.setBadgeView(textView);
                badgePagerTitleView1.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UIUtil.dip2px(badgePagerTitleView1.getContext(), 6)));
                badgePagerTitleView1.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));

                badgePagerTitleView1.post(new Runnable() {
                    @Override
                    public void run() {
                        commonNavigator2.setReselectWhenLayout(true);
                    }
                });
            }
        }, 5000);

        // 自适应模式
        final MagicIndicator magicIndicator3 = (MagicIndicator) findViewById(R.id.magic_indicator3);
        final CommonNavigator commonNavigator3 = new CommonNavigator(this);
        commonNavigator3.setAdjustMode(true);  // 自适应模式
        commonNavigator3.setSkimOver(true);
        commonNavigator3.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : 3;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                final BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);
                badgePagerTitleView.setAutoCancelBadge(false);

                if (index == 0 || index == 2) {
                    TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.simple_count_badge_layout, null);
                    textView.setText("3");
                    badgePagerTitleView.setBadgeView(textView);
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CENTER_X, -UIUtil.dip2px(context, 6)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.TOP, UIUtil.dip2px(context, 2)));
                }

                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#88ffffff"));
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                        badgePagerTitleView.setBadgeView(null);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);

                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineHeight(UIUtil.dip2px(context, 4));
                indicator.setRoundRadius(UIUtil.dip2px(context, 2));
                indicator.setColors(Color.parseColor("#40c4ff"));
                return indicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                if (index == 0) {
                    return 2.0f;
                } else {
                    return 1.0f;
                }
            }
        });
        magicIndicator3.setNavigator(commonNavigator3);

        // 显示分割线
        LinearLayout titleContainer = commonNavigator3.getTitleContainer(); // 务必在magicIndicator.setNavigator之后调用
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerPadding(UIUtil.dip2px(this, 15));
        titleContainer.setDividerDrawable(getResources().getDrawable(R.drawable.simple_splitter));

//        SimpleViewPagerDelegate.with(magicIndicator3, mViewPager).delegate();
        mFragmentContainerHelper = new FragmentContainerHelper(magicIndicator3, 3);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mFragmentContainerHelper.onPageSelected(position);  // 模拟在FragmentContainer中使用
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // 自适应模式，带插值器
        final MagicIndicator magicIndicator4 = (MagicIndicator) findViewById(R.id.magic_indicator4);
        final CommonNavigator commonNavigator4 = new CommonNavigator(this);
        commonNavigator4.setAdjustMode(true);  // 自适应模式
        commonNavigator4.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : 2;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.BLACK);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
                indicator.setLineHeight(UIUtil.dip2px(context, 1));
                indicator.setColors(Color.parseColor("#76b0ff"));
                return indicator;
            }
        });
        magicIndicator4.setNavigator(commonNavigator4);
        SimpleViewPagerDelegate.with(magicIndicator4, mViewPager).delegate();

        // 缩放 + 颜色渐变
        final MagicIndicator magicIndicator5 = (MagicIndicator) findViewById(R.id.magic_indicator5);
        final CommonNavigator commonNavigator5 = new CommonNavigator(this);
        commonNavigator5.setEnablePivotScroll(true);
        commonNavigator5.setScrollPivotX(0.8f);
        commonNavigator5.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.setNormalColor(Color.parseColor("#616161"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#f57c00"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
                indicator.setYOffset(UIUtil.dip2px(context, 39));
                indicator.setLineHeight(UIUtil.dip2px(context, 1));
                indicator.setColors(Color.parseColor("#f57c00"));
                return indicator;
            }
        });
        magicIndicator5.setNavigator(commonNavigator5);
        SimpleViewPagerDelegate.with(magicIndicator5, mViewPager).delegate();

        // 只有指示器，没有title
        final MagicIndicator magicIndicator6 = (MagicIndicator) findViewById(R.id.magic_indicator6);
        final CommonNavigator commonNavigator6 = new CommonNavigator(this);
        commonNavigator6.setAdjustMode(true);
        commonNavigator6.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : 5;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                return new DummyPagerTitleView(context);
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineHeight(UIUtil.dip2px(context, 5));
                indicator.setColors(Color.parseColor("#76b0ff"));
                return indicator;
            }
        });
        magicIndicator6.setNavigator(commonNavigator6);
        SimpleViewPagerDelegate.with(magicIndicator6, mViewPager).delegate();

        // 带吸附效果
        final MagicIndicator magicIndicator7 = (MagicIndicator) findViewById(R.id.magic_indicator7);
        final CommonNavigator commonNavigator7 = new CommonNavigator(this);
        commonNavigator7.setScrollPivotX(0.65f);
        commonNavigator7.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#9e9e9e"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#00c853"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 6));
                indicator.setLineWidth(UIUtil.dip2px(context, 10));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#00c853"));
                return indicator;
            }
        });
        magicIndicator7.setNavigator(commonNavigator7);
        SimpleViewPagerDelegate.with(magicIndicator7, mViewPager).delegate();

        // 贝塞尔曲线
        final MagicIndicator magicIndicator8 = (MagicIndicator) findViewById(R.id.magic_indicator8);
        final CommonNavigator commonNavigator8 = new CommonNavigator(this);
        commonNavigator8.setEnablePivotScroll(true);
        commonNavigator8.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.BLACK);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
                indicator.setColors(Color.parseColor("#ff4a42"), Color.parseColor("#fcde64"), Color.parseColor("#73e8f4"), Color.parseColor("#76b0ff"), Color.parseColor("#c683fe"));
                return indicator;
            }
        });
        magicIndicator8.setNavigator(commonNavigator8);
        SimpleViewPagerDelegate.with(magicIndicator8, mViewPager).delegate();

        // 天天快报式
        final MagicIndicator magicIndicator9 = (MagicIndicator) findViewById(R.id.magic_indicator9);
        final CommonNavigator commonNavigator9 = new CommonNavigator(this);
        commonNavigator9.setScrollPivotX(0.35f);
        commonNavigator9.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#e94220"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
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
        magicIndicator9.setNavigator(commonNavigator9);
        SimpleViewPagerDelegate.with(magicIndicator9, mViewPager).delegate();

        // 小尖角式
        final MagicIndicator magicIndicator10 = (MagicIndicator) findViewById(R.id.magic_indicator10);
        final CommonNavigator commonNavigator10 = new CommonNavigator(this);
        commonNavigator10.setScrollPivotX(0.15f);
        commonNavigator10.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#e94220"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
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
        magicIndicator10.setNavigator(commonNavigator10);
        SimpleViewPagerDelegate.with(magicIndicator10, mViewPager).delegate();

        // 圆圈式
        final MagicIndicator magicIndicator11 = (MagicIndicator) findViewById(R.id.magic_indicator11);
        final CircleNavigator circleNavigator = new CircleNavigator(this);
        circleNavigator.setCircleCount(mDataList.size());
        circleNavigator.setCircleColor(Color.RED);
        magicIndicator11.setNavigator(circleNavigator);
        SimpleViewPagerDelegate.with(magicIndicator11, mViewPager).delegate();

        MagicIndicator magicIndicator13 = (MagicIndicator) findViewById(R.id.magic_indicator13);
        ScaleCircleNavigator scaleCircleNavigator = new ScaleCircleNavigator(this);
        scaleCircleNavigator.setCircleCount(mDataList.size());
        magicIndicator13.setNavigator(scaleCircleNavigator);
        SimpleViewPagerDelegate.with(magicIndicator13, mViewPager).delegate();

        // 通用式
        final MagicIndicator magicIndicator12 = (MagicIndicator) findViewById(R.id.magic_indicator12);
        CommonNavigator commonNavigator12 = new CommonNavigator(this);
        commonNavigator12.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(MainActivity.this);
                commonPagerTitleView.setContentView(R.layout.simple_pager_title_layout);

                // 初始化
                final ImageView titleImg = (ImageView) commonPagerTitleView.findViewById(R.id.title_img);
                titleImg.setImageResource(R.mipmap.ic_launcher);
                final TextView titleText = (TextView) commonPagerTitleView.findViewById(R.id.title_text);
                titleText.setText(mDataList.get(index));

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        titleText.setTextColor(Color.RED);
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        titleText.setTextColor(Color.BLACK);
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                        titleImg.setScaleX(1.3f + (0.8f - 1.3f) * leavePercent);
                        titleImg.setScaleY(1.3f + (0.8f - 1.3f) * leavePercent);
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                        titleImg.setScaleX(0.8f + (1.3f - 0.8f) * enterPercent);
                        titleImg.setScaleY(0.8f + (1.3f - 0.8f) * enterPercent);
                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magicIndicator12.setNavigator(commonNavigator12);
        SimpleViewPagerDelegate.with(magicIndicator12, mViewPager).delegate();
    }
}
