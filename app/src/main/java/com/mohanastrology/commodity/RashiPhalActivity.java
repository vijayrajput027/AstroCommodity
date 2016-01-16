package com.mohanastrology.commodity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.mohanastrology.commodity.adapter.GridViewAdapter;
import com.mohanastrology.commodity.javafiles.RashiphalPojo;

import java.util.ArrayList;
import java.util.List;

public class RashiPhalActivity extends AppCompatActivity implements GridViewAdapter.ViewOnClickButtonListner {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    GridViewAdapter mAdapter;
    Toolbar toolbar;
    private List<RashiphalPojo> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rashi_phal);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rashi Phal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mItems=new ArrayList<RashiphalPojo>();
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mItems.add(new RashiphalPojo("Aries", R.drawable.aries_icon));
        mItems.add(new RashiphalPojo("Taurus",R.drawable.tarus_icon));
        mItems.add(new RashiphalPojo("Gemini", R.drawable.gemini_icon));
        mItems.add(new RashiphalPojo("Cancer",R.drawable.scorpio_icon));
        mItems.add(new RashiphalPojo("Leo",R.drawable.leo_icon));
        mItems.add(new RashiphalPojo("Virgo",R.drawable.virgo_icon));
        mItems.add(new RashiphalPojo("Libra",R.drawable.libra_icon));
        mItems.add(new RashiphalPojo("Scorpio",R.drawable.scorpio_icon));
        mItems.add(new RashiphalPojo("Sagittarius",R.drawable.sagittarius_icon));
        mItems.add(new RashiphalPojo("Capricorn",R.drawable.capricorn_icon));
        mItems.add(new RashiphalPojo("Aquarius",R.drawable.aquarius_icon));
        mItems.add(new RashiphalPojo("Pisces",R.drawable.pisces_icon));


        mAdapter = new GridViewAdapter(this,mItems);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setonclick(this);
    }


    @Override
    public void onItemClick(int position) {
        String commodity_name=mItems.get(position).getrashiName();
        Intent intent=new Intent(getApplicationContext(),DetailRashiphalActivity.class);
        intent.putExtra("id",position+1);
        intent.putExtra("name",commodity_name);
        startActivity(intent);
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
