package com.mohanastrology.commodity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.mohanastrology.commodity.constant.AppUrl;
import com.mohanastrology.commodity.javafiles.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SolutionActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    /*private static String solutionUrl ="http://mohangautam-001-site8.btempurl.com/app/solution.php";*/
    String result,solution,category;
    String category_id,subcategory_id,problem_id;
    TextView tvsolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);


        Intent intent=getIntent();
        category_id= intent.getStringExtra("cat_id");
        subcategory_id=intent.getStringExtra("subcat_id");
        problem_id=intent.getStringExtra("prob_id");
        category=intent.getStringExtra("cat_title");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvsolution=(TextView)findViewById(R.id.tvsolution);



        new SolutionAsync().execute();
    }

    public class SolutionAsync extends AsyncTask<String, String, JSONObject> {
        private JSONParser jsonParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SolutionActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            HashMap<String, String> params = new HashMap<>();
            params.put("cat_id",category_id);
            params.put("sub_cat_id",subcategory_id);
            params.put("prob_id",problem_id);

            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.solutionUrl, "POST", params);
            try {
                if (json != null) {
                    {
                        result = json.getString("success");
                        Log.i("result", result);
                        if (result.equals("1")) {
                            JSONArray jsonArray = json.getJSONArray("solutions");
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                solution = jsonObject.getString("solution");

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

                tvsolution.setText(solution);
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
