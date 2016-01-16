package com.mohanastrology.commodity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.mohanastrology.commodity.adapter.CommodityAdapter;
import com.mohanastrology.commodity.constant.AppUrl;
import com.mohanastrology.commodity.javafiles.CommodityPojo;
import com.mohanastrology.commodity.javafiles.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LuckImprovementActivity extends AppCompatActivity implements CommodityAdapter.ViewOnClickButtonListner {

    private List<CommodityPojo> listitem;
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private CommodityAdapter adapter;
    private JSONParser jsonParser = new JSONParser();
    private String result;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luck_improvement);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Luck Improvement");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new LuckImprovementAsync().execute();

        listitem = new ArrayList<CommodityPojo>();
        adapter = new CommodityAdapter(LuckImprovementActivity.this, listitem);
        recyclerView.setAdapter(adapter);
        adapter.setonclick(this);

    }

    @Override
    public void onItemClick(int position) {
        int id = listitem.get(position).getId();
        String luckImprovement_id = String.valueOf(id);
        Intent intent = new Intent(getApplicationContext(), LuckImprvementCategory.class);
        intent.putExtra("luckImprovement_id", luckImprovement_id);
        startActivity(intent);
    }

    class LuckImprovementAsync extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LuckImprovementActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... param) {
            HashMap<String, String> params = new HashMap<>();

            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.luckimprovementURL, "POST", params);
            try {
                if (json != null) {
                    {
                        result = json.getString("success");
                        if (result.equals("1")) {
                            JSONArray jsonArray = json.getJSONArray("category_name");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String title = jsonObject.getString("category");
                                listitem.add(new CommodityPojo(id, title));
                            }
                        }
                    }
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (result.equals("1")) {
                adapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "Successfully..", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getBaseContext(), "Failed..", Toast.LENGTH_SHORT).show();
            }
        }
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
