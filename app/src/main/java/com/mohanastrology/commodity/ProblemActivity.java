package com.mohanastrology.commodity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.mohanastrology.commodity.adapter.SpinnerforAdapter;
import com.mohanastrology.commodity.constant.AppUrl;
import com.mohanastrology.commodity.javafiles.JSONParser;
import com.mohanastrology.commodity.javafiles.ProblemSolutionPojo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProblemActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    /*private static String categoryUrl ="http://mohangautam-001-site8.btempurl.com/app/pro_category.php";
    private static String subCategoryUrl ="http://mohangautam-001-site8.btempurl.com/app/pro_sub_category.php";*/
    String result,category_title;
   // private static String problemUrl="http://mohangautam-001-site8.btempurl.com/app/problem.php";
    Spinner spCategory,spSubCategory,spProblem;
    Button btnSubmit;
    ArrayList<ProblemSolutionPojo> categoryList,subCategoryList,problemList;
    String category_id,subCategory_id,prob_id;
    SpinnerforAdapter categoryadapter,problemAdapter,subcategoryadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Problem & Solution");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spCategory = (Spinner) findViewById(R.id.categorySpinner);
        spSubCategory = (Spinner) findViewById(R.id.subCategorySpinner);
        spProblem = (Spinner) findViewById(R.id.problemSpinner);
        btnSubmit = (Button) findViewById(R.id.btnsubmit);

        categoryList = new ArrayList<ProblemSolutionPojo>();
        subCategoryList = new ArrayList<ProblemSolutionPojo>();
        problemList = new ArrayList<ProblemSolutionPojo>();

        new CategoryAsyc().execute();

        categoryadapter=new SpinnerforAdapter(this,categoryList);
        spCategory.setAdapter(categoryadapter);

        subcategoryadapter=new SpinnerforAdapter(this,subCategoryList);
        spSubCategory.setAdapter(subcategoryadapter);

        problemAdapter=new SpinnerforAdapter(this,problemList);
        spProblem.setAdapter(problemAdapter);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProblemSolutionPojo item =(ProblemSolutionPojo) parent.getItemAtPosition(position);
                category_id=item.getId();
                category_title=item.getName();
                new SubCategoryAsyc().execute();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spProblem.setVisibility(View.VISIBLE);
                ProblemSolutionPojo item =(ProblemSolutionPojo) parent.getItemAtPosition(position);
                subCategory_id=item.getId();

                new ProblemAsyc().execute();

                spProblem.setAdapter(problemAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spProblem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                btnSubmit.setVisibility(View.VISIBLE);
                ProblemSolutionPojo item =(ProblemSolutionPojo) parent.getItemAtPosition(position);
                prob_id=item.getId();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SolutionActivity.class);
                intent.putExtra("cat_id",category_id);
                intent.putExtra("cat_title",category_title);
                intent.putExtra("subcat_id",subCategory_id);
                intent.putExtra("prob_id",prob_id);
                startActivity(intent);
            }
        });
    }

    public class CategoryAsyc extends AsyncTask<String, String, JSONObject> {
        private JSONParser jsonParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProblemActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            HashMap<String, String> params = new HashMap<>();

            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.problemCategoryUrl, "POST", params);
            try {
                if (json != null) {
                    {
                        result = json.getString("success");
                        Log.i("result", result);
                        if (result.equals("1")) {
                            JSONArray jsonArray = json.getJSONArray("category_name");
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String category = jsonObject.getString("category");
                                categoryList.add(new ProblemSolutionPojo(id,category));


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
                Toast.makeText(getBaseContext(), "Successfully..", Toast.LENGTH_SHORT).show();
                spCategory.setAdapter(categoryadapter);
                categoryadapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getBaseContext(), "Failed..", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class SubCategoryAsyc extends AsyncTask<String, String, JSONObject> {
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProblemActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            HashMap<String, String> params = new HashMap<>();
           params.put("cat_id",category_id);
            JSONObject jsonone = jsonParser
                    .makeHttpRequest(AppUrl.problemSubCategoryUrl, "POST", params);
            try {
                if (jsonone != null) {
                    {
                        result = jsonone.getString("success");
                        Log.i("result", result);
                        if (result.equals("1")) {
                            JSONArray jsonArray = jsonone.getJSONArray("sub_category_name");
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String subCategory = jsonObject.getString("sub_category");
                                subCategoryList.add(new ProblemSolutionPojo(id, subCategory));
                            }
                        }
                    }
                    return jsonone;
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
                Toast.makeText(getBaseContext(), "Successfully..", Toast.LENGTH_SHORT).show();
                subcategoryadapter.notifyDataSetChanged();

            }
            else
            {
                Toast.makeText(getBaseContext(), "Failed..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ProblemAsyc extends AsyncTask<String,String,JSONObject>
    {
        private JSONParser jsonParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProblemActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            HashMap<String,String> params=new HashMap<>();
            params.put("cat_id",category_id);
            params.put("sub_cat_id",subCategory_id);

            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.problemUrl, "POST", params);
            try {
                if (json != null) {
                    {
                        result=json.getString("success");

                        Log.i("result", result);
                        if(result.equals("1")) {
                            JSONArray jsonArray = json.getJSONArray("problems");
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String problem = jsonObject.getString("problem");
                                problemList.add(new ProblemSolutionPojo(id, problem));

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
                Toast.makeText(getBaseContext(), "Successfully..", Toast.LENGTH_SHORT).show();
                problemAdapter.notifyDataSetChanged();
            }
            else
            {
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
