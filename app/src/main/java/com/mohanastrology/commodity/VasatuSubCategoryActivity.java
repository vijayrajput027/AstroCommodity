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
import android.util.Log;
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

public class VasatuSubCategoryActivity extends AppCompatActivity implements  CommodityAdapter.ViewOnClickButtonListner{

    private RecyclerView recyclerView;
    private CommodityAdapter adapter;
    private ProgressDialog pDialog;
    private List<CommodityPojo> listitem;
    private String result;
    private Toolbar mtoolBar;
    String category_id,vastu_id,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vasatu_sub_category);

        mtoolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolBar);
        getSupportActionBar().setTitle("VastuSubCategory");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        vastu_id=intent.getStringExtra("vastu_id");
        category_id=intent.getStringExtra("category_id");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new VastuSubCategoryAsync().execute();

        listitem = new ArrayList<>();
        adapter = new CommodityAdapter(VasatuSubCategoryActivity.this, listitem);
        recyclerView.setAdapter(adapter);
        adapter.setonclick(this);
    }

    @Override
    public void onItemClick(int position) {
        int id=listitem.get(position).getId();
        String subCategory_id=String.valueOf(id);
        Intent intent=new Intent(getApplicationContext(),VastuResult.class);
        intent.putExtra("category_id",category_id);
        intent.putExtra("vastu_id",vastu_id);
        intent.putExtra("subCategory_id",subCategory_id);
        startActivity(intent);
    }

    class VastuSubCategoryAsync extends AsyncTask<String,String,JSONObject>
    {

        private JSONParser jsonParser=new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=new ProgressDialog(VasatuSubCategoryActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            // pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            HashMap<String,String> params=new HashMap<>();
            params.put("cat_id",vastu_id);
            params.put("sub_cat_id",category_id);

            JSONObject json= jsonParser
                    .makeHttpRequest(AppUrl.getVastuSubCategoryUrl, "POST", params);

            try {
                if (json != null) {
                    {
                        result=json.getString("success");

                        Log.i("result", result);
                        if(result.equals("1")) {
                            JSONArray jsonArray = json.getJSONArray("vastu_sub_child_category_name");
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String sub_category = jsonObject.getString("child_sub_category");
                                listitem.add(new CommodityPojo(id, sub_category));
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
                Toast.makeText(getBaseContext(), "Successfully..", Toast.LENGTH_SHORT).show();

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

