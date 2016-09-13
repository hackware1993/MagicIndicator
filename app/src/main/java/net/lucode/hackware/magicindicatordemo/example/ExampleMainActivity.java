package net.lucode.hackware.magicindicatordemo.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.lucode.hackware.magicindicatordemo.R;

public class ExampleMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_main_layout);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scrollable_tab:
                startActivity(new Intent(this, ScrollableTabExampleActivity.class));
                break;
            case R.id.fixed_tab:
                startActivity(new Intent(this, FixedTabExampleActivity.class));
                break;
            case R.id.dynamic_tab:
                startActivity(new Intent(this, DynamicTabExampleActivity.class));
                break;
            case R.id.no_tab_only_indicator:
                startActivity(new Intent(this, NoTabOnlyIndicatorExampleActivity.class));
                break;
            case R.id.tab_with_badge_view:
                startActivity(new Intent(this, BadgeTabExampleActivity.class));
                break;
            case R.id.work_with_fragment_container:
                startActivity(new Intent(this, FragmentContainerExampleActivity.class));
                break;
            case R.id.load_custom_layout:
                startActivity(new Intent(this, LoadCustomLayoutExampleActivity.class));
                break;
            case R.id.custom_navigator:
                startActivity(new Intent(this, CustomNavigatorExampleActivity.class));
                break;
            default:
                break;
        }
    }
}
