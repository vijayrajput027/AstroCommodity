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

import com.mohanastrology.commodity.adapter.CommodityServiceAdapter;
import com.mohanastrology.commodity.constant.AppUrl;
import com.mohanastrology.commodity.javafiles.ServicePojo;
import com.mohanastrology.commodity.javafiles.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommodityServices extends AppCompatActivity implements CommodityServiceAdapter.ViewClickListner {

    private RecyclerView recyclerView;
    private CommodityServiceAdapter adapter;
    private ProgressDialog pDialog;
    private JSONParser jsonParser=new JSONParser();
  //  private static String commodityserviceUrl="http://mohangautam-001-site8.btempurl.com/app/sub_category_list.php";
    private List<ServicePojo> listitem;
    private String  category_id,result,commodity_name,subcategory_id,parent_id;
    private Toolbar mtoolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_services);

        Intent intent=getIntent();
        parent_id=intent.getStringExtra("parent_id");
        category_id=intent.getStringExtra("category_id");
        commodity_name=intent.getStringExtra("commodity_name");
        mtoolBar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mtoolBar);
        getSupportActionBar().setTitle(commodity_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new CommodityServiceAsyc().execute(category_id);
        listitem=new ArrayList<ServicePojo>();
        adapter=new CommodityServiceAdapter(CommodityServices.this,listitem);
        recyclerView.setAdapter(adapter);
        adapter.setonclick(this);
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

    @Override
    public void onItemClick(int position) {
        String price=listitem.get(position).getPrice();
        int subCommodity_id=listitem.get(position).getId();
        subcategory_id=String.valueOf(subCommodity_id);
        switch (position) {
            case 0:
                Intent intraDayIntent = new Intent(getApplicationContext(), IntraDay.class);
                intraDayIntent.putExtra("price", price);
                intraDayIntent.putExtra("subcategory_id", subcategory_id);
                intraDayIntent.putExtra("category_id", category_id);
                intraDayIntent.putExtra("commodity_name", commodity_name);
                intraDayIntent.putExtra("parent_id",parent_id);
                startActivity(intraDayIntent);
                break;
            case 1:
                Intent dailyIntent = new Intent(getApplicationContext(), DailyActivity.class);
                dailyIntent.putExtra("price",price);
                dailyIntent.putExtra("subcategory_id", subcategory_id);
                dailyIntent.putExtra("category_id", category_id);
                dailyIntent.putExtra("commodity_name",commodity_name);
                dailyIntent.putExtra("parent_id",parent_id);
                startActivity(dailyIntent);
                break;
            case 2:
                Intent monthlyIntent = new Intent(getApplicationContext(), MonthlyActivity.class);
                monthlyIntent.putExtra("price",price);
                monthlyIntent.putExtra("subcategory_id", subcategory_id);
                monthlyIntent.putExtra("category_id", category_id);
                monthlyIntent.putExtra("commodity_name",commodity_name);
                monthlyIntent.putExtra("parent_id",parent_id);
                startActivity(monthlyIntent);
                break;
           }
        }

    public class CommodityServiceAsyc extends AsyncTask<String,String,JSONObject>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=new ProgressDialog(CommodityServices.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            // pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            HashMap<String,String> params=new HashMap<>();
            params.put("category_id", category_id);
            params.put("parent_id",parent_id);
            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.commodityServiceUrl, "POST", params);
            try {
                if (json != null) {
                    {
                        result=json.getString("success");

                        Log.i("result",result);
                        if(result.equals("1")) {
                            JSONArray jsonArray = json.getJSONArray("sub_category");
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String price = jsonObject.getString("price");
                                String sub_category = jsonObject.getString("sub_category");

                                listitem.add(new ServicePojo(id, sub_category, price));
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
               //  Toast.makeText(getBaseContext(), "Successfully..", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Failed..", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


