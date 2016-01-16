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

public class CommodityActivity extends AppCompatActivity implements CommodityAdapter.ViewOnClickButtonListner{

    private List<CommodityPojo> listitem;
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private CommodityAdapter adapter;
    private JSONParser jsonParser=new JSONParser();
    private String result,category_title;
    private Toolbar mToolbar;
    String parent_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);

        Intent intent=getIntent();
        category_title=intent.getStringExtra("category_title");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(category_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        parent_Id=String.valueOf(intent.getIntExtra("category_id",0));

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new CommodityitemAsync().execute();
        listitem=new ArrayList<CommodityPojo>();
        adapter=new CommodityAdapter(CommodityActivity.this,listitem);
        recyclerView.setAdapter(adapter);
        adapter.setonclick(this);
    }

    @Override
    public void onItemClick(int position) {
        int id=listitem.get(position).getId();
        String category_id=String.valueOf(id);
        String commodity_name=listitem.get(position).getTitle();
        Intent intent=new Intent(getApplicationContext(),CommodityServices.class);
        intent.putExtra("parent_id",parent_Id);
        intent.putExtra("category_id",category_id);
        intent.putExtra("commodity_name",commodity_name);
        startActivity(intent);
    }

    class CommodityitemAsync extends AsyncTask<String,String,JSONObject>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=new ProgressDialog(CommodityActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... param) {
            HashMap<String,String> params=new HashMap<>();
            params.put("parent_id",parent_Id);
            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.commodityUrl, "POST", params);
            try {
                if (json != null) {
                    {
                       result=json.getString("success");
                        if(result.equals("1")) {
                            JSONArray jsonArray = json.getJSONArray("category_name");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String title=jsonObject.getString("category");
                                int id=jsonObject.getInt("id");
                                listitem.add(new CommodityPojo(id,title));
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
            if(result.equals("1")){
                adapter.notifyDataSetChanged();
               // Toast.makeText(getBaseContext(), "Successfully..", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getBaseContext(), "Failed..", Toast.LENGTH_SHORT).show();            }
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
