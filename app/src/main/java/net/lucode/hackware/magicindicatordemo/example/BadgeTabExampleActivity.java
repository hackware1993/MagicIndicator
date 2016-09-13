package net.lucode.hackware.magicindicatordemo.example;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;
import net.lucode.hackware.magicindicatordemo.R;
import net.lucode.hackware.magicindicatordemo.ext.titles.ScaleTransitionPagerTitleView;

import java.util.Arrays;
import java.util.List;

public class BadgeTabExampleActivity extends AppCompatActivity {
    private static final String[] CHANNELS = new String[]{"KITKAT", "NOUGAT", "DONUT"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private ExamplePagerAdapter mExamplePagerAdapter = new ExamplePagerAdapter(mDataList);

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge_tab_example_layout);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mExamplePagerAdapter);

        initMagicIndicator1();
        initMagicIndicator2();
        initMagicIndicator3();
        initMagicIndicator4();
    }

    private void initMagicIndicator1() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator1);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                final BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#88ffffff"));
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                        badgePagerTitleView.setBadgeView(null); // cancel badge when click tab
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);

                // setup badge
                if (index != 2) {
                    TextView badgeTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.simple_count_badge_layout, null);
                    badgeTextView.setText("" + (index + 1));
                    badgePagerTitleView.setBadgeView(badgeTextView);
                } else {
                    ImageView badgeImageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.simple_red_dot_badge_layout, null);
                    badgePagerTitleView.setBadgeView(badgeImageView);
                }

                // set badge position
                if (index == 0) {
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_LEFT, -UIUtil.dip2px(context, 6)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
                } else if (index == 1) {
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UIUtil.dip2px(context, 6)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
                } else if (index == 2) {
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CENTER_X, -UIUtil.dip2px(context, 3)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_BOTTOM, UIUtil.dip2px(context, 2)));
                }

                // don't cancel badge when tab selected
                badgePagerTitleView.setAutoCancelBadge(false);

                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.parseColor("#40c4ff"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerPadding(UIUtil.dip2px(this, 15));
        titleContainer.setDividerDrawable(getResources().getDrawable(R.drawable.simple_splitter));
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    private void initMagicIndicator2() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator2);
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

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
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);

                // setup badge
                if (index == 1) {
                    ImageView badgeImageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.simple_red_dot_badge_layout, null);
                    badgePagerTitleView.setBadgeView(badgeImageView);
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CENTER_X, -UIUtil.dip2px(context, 3)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_BOTTOM, UIUtil.dip2px(context, 2)));
                }

                // cancel badge when click tab, default true
                badgePagerTitleView.setAutoCancelBadge(true);

                return badgePagerTitleView;
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

            @Override
            public float getTitleWeight(Context context, int index) {
                if (index == 0) {
                    return 2.0f;
                } else if (index == 1) {
                    return 1.2f;
                } else {
                    return 1.0f;
                }
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    private void initMagicIndicator3() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator3);
        magicIndicator.setBackgroundResource(R.drawable.round_indicator_bg);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mDataList.get(index));
                clipPagerTitleView.setTextColor(Color.parseColor("#e94220"));
                clipPagerTitleView.setClipColor(Color.WHITE);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(clipPagerTitleView);

                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                float navigatorHeight = context.getResources().getDimension(R.dimen.common_navigator_height);
                float borderWidth = UIUtil.dip2px(context, 1);
                float lineHeight = navigatorHeight - 2 * borderWidth;
                indicator.setLineHeight(lineHeight);
                indicator.setRoundRadius(lineHeight / 2);
                indicator.setYOffset(borderWidth);
                indicator.setColors(Color.parseColor("#bc2a2a"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    private void initMagicIndicator4() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator4);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setText(mDataList.get(index));
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
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setColors(Color.WHITE);
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(BadgeTabExampleActivity.this, 15);
            }
        });
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }
}
