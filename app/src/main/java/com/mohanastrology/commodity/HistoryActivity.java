package com.mohanastrology.commodity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mohanastrology.commodity.adapter.HistoryAdapter;
import com.mohanastrology.commodity.constant.AppUrl;
import com.mohanastrology.commodity.database.ApplicationDatabaseHelper;
import com.mohanastrology.commodity.javafiles.ApplicationStatus;
import com.mohanastrology.commodity.javafiles.HistoryPojo;
import com.mohanastrology.commodity.javafiles.InternetConnection;
import com.mohanastrology.commodity.javafiles.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.HistoryClickListner {
    private List<HistoryPojo> listitem, offlinelistitem;
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private JSONParser jsonParser = new JSONParser();
    //private static String commodityUrl = "http://mohangautam-001-site8.btempurl.com/app/commodity_history.php";
    private String result;
    private Toolbar mToolbar;
    private ApplicationDatabaseHelper databaseHelper;
    private String imagename, date, user_id, parent_id;
    private int imageid;
    private ApplicationStatus status;
    private String spinnerCategory,spinnerSubCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        parent_id = String.valueOf(intent.getIntExtra("id", 0));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        status = new ApplicationStatus(this);
        user_id = status.getEmail();

        databaseHelper = new ApplicationDatabaseHelper(this);
        listitem = new ArrayList<HistoryPojo>();

        if (InternetConnection.checkNetworkConnection(this)) {
             new CommodityitemAsync().execute();
        }
        else if(parent_id.equals("1"))
        {
            listitem = databaseHelper.getOfflineCommodityImage();
        }
        else if(parent_id.equals("2")){
                listitem = databaseHelper.getOfflineCurrencyyImage();
        }
        adapter = new HistoryAdapter(HistoryActivity.this, listitem);
        recyclerView.setAdapter(adapter);
        adapter.setonClickListner(this);
    }

    @Override
    public void onitemClick(int position) {
        String id = listitem.get(position).getId();
        String date = listitem.get(position).getDate();

        try {

            Intent intent = new Intent(this, ImageDetail.class);
            intent.putExtra("imageID", id);
            intent.putExtra("date", date);
            intent.putExtra("position", position);
            intent.putExtra("parent_id",parent_id);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class CommodityitemAsync extends AsyncTask<String, String, JSONObject> {
        URL url = null;
        Bitmap bitmapImage = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HistoryActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... param) {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", "niraj.semi@gmail.com");
            params.put("parent_id", parent_id);
            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.historyUrl, "POST", params);

            try {
                if (json != null) {
                    {
                        result = json.getString("success");
                        if (result.equals("1")) {
                            JSONArray jsonArray = json.getJSONArray("commodity_history");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id= jsonObject.getString("id");
                                imageid = Integer.valueOf(id);
                                date = jsonObject.getString("date");
                                String category = jsonObject.getString("category");
                                String subcategory = jsonObject.getString("sub_category");
                                try {
                                    String image = jsonObject.getString("material");
                                    imagename = image.substring(image.lastIndexOf('/') + 1, image.lastIndexOf('.'));
                                    url = new URL(image);
                                    bitmapImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                }
                                catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                                if(parent_id.equals("1")) {
                                    if (!databaseHelper.checkCommodityImage(imageid)) {
                                        databaseHelper.saveToSdCardwithCommodityDB(bitmapImage, imagename, date, category, imageid, subcategory);
                                    }
                                }else {
                                    if (!databaseHelper.checkCurrencyImage(imageid)) {
                                        databaseHelper.saveToSdCardwithCurrencyDB(bitmapImage, imagename, date, category, imageid, subcategory);
                                    }
                                }
                                listitem.add(new HistoryPojo(id,imagename, bitmapImage, date, category, subcategory));
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
   case  R.id.filter:
              dialog();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.filter_dialog);
        dialog.setCancelable(true);

        final Spinner sp_category = (Spinner) dialog.findViewById(R.id.spinnerCategory);
        final Spinner sp_subcategory = (Spinner) dialog.findViewById(R.id.spinneSubCategory);
         Button btnsubmit=(Button)dialog.findViewById(R.id.btndialogsubmit);
        if(parent_id.equals("1")) {
            dialog.setTitle("Filter Commodity");
            ArrayList<String> list_category = new ArrayList<String>();
            list_category.add("Silver");
            list_category.add("Copper");
            list_category.add("Gold");
            list_category.add("Diamond");

            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_spinner_item, list_category);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            sp_category.setAdapter(categoryAdapter);

            sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerCategory = sp_category.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            ArrayList<String> list_subCategory = new ArrayList<String>();
            list_subCategory.add("Intra");
            list_subCategory.add("Daily");
            list_subCategory.add("Monthly");

            ArrayAdapter<String> subCategoryadapter = new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_spinner_item, list_subCategory);
            subCategoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            sp_subcategory.setAdapter(subCategoryadapter);

            sp_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSubCategory = sp_subcategory.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            btnsubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listitem.clear();
                    offlinelistitem = new ArrayList<HistoryPojo>();
                    offlinelistitem = databaseHelper.getOfflineFilterCommodity(spinnerCategory, spinnerSubCategory);
                    Toast.makeText(getApplicationContext(),""+offlinelistitem,Toast.LENGTH_LONG).show();
                    adapter = new HistoryAdapter(HistoryActivity.this, offlinelistitem);
                    recyclerView.setAdapter(adapter);
                    adapter.setonClickListner(HistoryActivity.this);
                    dialog.dismiss();
                }
            });

        } else {
            dialog.setTitle("Filter Currency");
            ArrayList<String> list_category = new ArrayList<String>();
            list_category.add("EUR TO INR");

            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_spinner_item, list_category);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            sp_category.setAdapter(categoryAdapter);

            sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerCategory = sp_category.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            ArrayList<String> list_subCategory = new ArrayList<String>();
            list_subCategory.add("Intra");
            list_subCategory.add("Daily");
            list_subCategory.add("Monthly");

            ArrayAdapter<String> subCategoryadapter = new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_spinner_item, list_subCategory);
            subCategoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            sp_subcategory.setAdapter(subCategoryadapter);
            sp_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSubCategory = sp_subcategory.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            btnsubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listitem.clear();
                    offlinelistitem = new ArrayList<HistoryPojo>();
                    offlinelistitem = databaseHelper.getOfflineFilterCurrency(spinnerCategory, spinnerSubCategory);
                    adapter = new HistoryAdapter(HistoryActivity.this, offlinelistitem);
                    recyclerView.setAdapter(adapter);
                    adapter.setonClickListner(HistoryActivity.this);
                    dialog.dismiss();
                }
            });

        }
        dialog.show();
    }
}
