package com.mohanastrology.commodity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mohanastrology.commodity.constant.AppUrl;
import com.mohanastrology.commodity.javafiles.InternetConnection;
import com.mohanastrology.commodity.javafiles.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class VastuResult extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvtips;
    private Button btnknowMore;
    private ProgressDialog pDialog;
    private String  category_id,result,vastu_id,subCategory_Id,material;
    final String type="vastu";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Calendar myCalendar;
    private  SimpleDateFormat sdf;
    private int year;
    private int month;
    private int day;

    static final int DATE_PICKER_ID = 1111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vastu_result);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("VastuTips");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvtips=(TextView)findViewById(R.id.tv_tips);
        btnknowMore=(Button)findViewById(R.id.btnKnowMore);

        Intent intent=getIntent();
        vastu_id=intent.getStringExtra("vastu_id");
        category_id=intent.getStringExtra("category_id");
        subCategory_Id=intent.getStringExtra("subCategory_id");

        new LuckImprovementResultAsync().execute();

        btnknowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
    }

    class LuckImprovementResultAsync extends AsyncTask<String,String,JSONObject>
    {
        private JSONParser jsonParser=new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=new ProgressDialog(VastuResult.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            // pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            HashMap<String,String> params=new HashMap<>();
            params.put("cat_id", vastu_id);
            params.put("sub_cat_id", category_id);
            params.put("sub_child_id", subCategory_Id);

            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.vastuResult, "POST", params);

            try {
                if (json != null) {
                    {
                        result=json.getString("success");

                        Log.i("result", result);
                        if(result.equals("1")) {
                            JSONArray jsonArray = json.getJSONArray("vastu_result");
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                material = jsonObject.getString("material");

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
                tvtips.setText(material);
            }else{
                Toast.makeText(getBaseContext(), "Failed..", Toast.LENGTH_SHORT).show();            }
        }
    }
    private void dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.gatherinfo);
        dialog.setCancelable(true);
        dialog.setTitle("Please Submit Form");
        final EditText etName=(EditText)dialog.findViewById(R.id.editname);
        final EditText etEmail=(EditText)dialog.findViewById(R.id.editEmail);
        final EditText etContactNo=(EditText)dialog.findViewById(R.id.editContactNo);
        final EditText etDOB=(EditText)dialog.findViewById(R.id.editDOB);
        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(VastuResult.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                etDOB.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        },  c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
               dpd.show();;
            }
        });

        final EditText etPOB=(EditText)dialog.findViewById(R.id.editPOB);
        final EditText etTOB=(EditText)dialog.findViewById(R.id.editTOB);
         etTOB.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 final Calendar c = Calendar.getInstance();
                 TimePickerDialog tpd = new TimePickerDialog(VastuResult.this,
                         new TimePickerDialog.OnTimeSetListener() {

                             @Override
                             public void onTimeSet(TimePicker view, int hourOfDay,
                                                   int minute) {
                                 etTOB.setText(hourOfDay + ":" + minute);
                             }
                         },c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                 tpd.show();
             }
         });

        Button btnsubmit=(Button)dialog.findViewById(R.id.btndialogsubmit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=etName.getText().toString();
                String email=etEmail.getText().toString();
                String mobile=etContactNo.getText().toString();
                String dob=etDOB.getText().toString();
                String pob=etPOB.getText().toString();
                String tob=etTOB.getText().toString();

        if (name.trim().equals("")) {
            Toast.makeText(getBaseContext(), "Please enter Name!", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please enter Email!", Toast.LENGTH_SHORT).show();
        } else if(!email.matches(emailPattern)) {
            Toast.makeText(getBaseContext(), "Please enter Valid Email Id!", Toast.LENGTH_SHORT).show();
         } else if(mobile.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please enter mobile No!", Toast.LENGTH_SHORT).show();
        }else if(mobile.length() < 10) {
            Toast.makeText(getBaseContext(), "Please enter Valid mobile No!", Toast.LENGTH_SHORT).show();
         }else if( dob.isEmpty()) {
             Toast.makeText(getBaseContext(), "Please enter Date of Birth!", Toast.LENGTH_SHORT).show();
        }else if(pob.isEmpty()) {
               Toast.makeText(getBaseContext(), "Please enter Place of Birth!", Toast.LENGTH_SHORT).show();
         }else if(tob.isEmpty()){
               Toast.makeText(getBaseContext(), "Please enter Time of Birth!", Toast.LENGTH_SHORT).show();
          }else{
                    new KnowMoreAsync().execute(name,email,mobile,dob,pob,tob);
          };


            }
        });
        dialog.show();

    }

    class KnowMoreAsync extends AsyncTask<String,String,JSONObject>
    {
        private JSONParser jsonParser=new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=new ProgressDialog(VastuResult.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            // pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            HashMap<String,String> params=new HashMap<>();
            params.put("name", args[0]);
            params.put("email", args[1]);
            params.put("mobile", args[2]);
            params.put("dob", args[3]);
            params.put("pob", args[4]);
            params.put("tob", args[5]);
            params.put("type",type);

            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.knowMoreUrl, "POST", params);
            try {
                if (json != null) {
                    String result = json.getString("success");
                    Log.i("result", "" + result);
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
