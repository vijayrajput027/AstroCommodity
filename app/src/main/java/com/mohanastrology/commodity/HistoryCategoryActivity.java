package com.mohanastrology.commodity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HistoryCategoryActivity extends AppCompatActivity {

    private Button btncommodity,btncurrency;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_category);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btncommodity=(Button)findViewById(R.id.btnCommodity);
        btncurrency=(Button)findViewById(R.id.btnCurrency);

        btncommodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),HistoryActivity.class);
                intent.putExtra("id",1);
                startActivity(intent);
            }
        });

        btncurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),HistoryActivity.class);
                intent.putExtra("id",2);
                startActivity(intent);
            }
        });

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
