package com.mohanastrology.commodity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mohanastrology.commodity.Fragment.TomorrowRashiphalFragment;
import com.mohanastrology.commodity.Fragment.TodayRashiphalFragment;
import com.mohanastrology.commodity.Fragment.YesterdayRashiphalFragment;
import com.mohanastrology.commodity.adapter.ViewPagerAdapter;

public class DetailRashiphalActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int rashiId;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rashiphal);

        Bundle bundle = getIntent().getExtras();
        rashiId = bundle.getInt("id");
        name=bundle.getString("name");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TodayRashiphalFragment(), "Today");
        adapter.addFragment(new TomorrowRashiphalFragment(), "Tomorrow");
        adapter.addFragment(new YesterdayRashiphalFragment(), "Yesterday");
        viewPager.setAdapter(adapter);
    }

    public int getRashiId() {
        return rashiId;
    }
    public String getRashiName()
    {
        return name;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
