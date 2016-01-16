package com.mohanastrology.commodity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mohanastrology.commodity.constant.AppUrl;
import com.mohanastrology.commodity.javafiles.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class NumerologyActivity extends AppCompatActivity {
    private Calendar myCalendar;
    private EditText etDob;
    private String month, day;
    private Button btnsubmit;

    private  TextView tv_finance,tvcarrier,tvhealth,tvmarragepartner,tvcharashusband,tvcharaswife,
            tvfriendship,tvluckydaydate,tvlucklotteries,tvluckycolor,tvluckystone,tvimportantyear,
            tvpositivequality,tvnegativequality;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();
    String finance,characteristic,resultmonth,resultday,carrier,health,marriage_partner,
            husband_char,wife_char,friendship,luckydays_date,luck_lotteries,
            lucky_colour,luckystone,importantyear,positive_qualities,negative_qualities,result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numerology);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setviewResorce();

        etDob = (EditText) findViewById(R.id.editDOB);
        btnsubmit=(Button)findViewById(R.id.btnsubmit);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }
        };

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar = Calendar.getInstance();
                new DatePickerDialog(NumerologyActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  dob=etDob.getText().toString();
                 day=dob.substring(0,2);
                 month=dob.substring(3,4);
                new NumerologyAsyc().execute(day,month);
            }
        });
    }

    private void setviewResorce()
    {
        tv_finance=(TextView)findViewById(R.id.tv_finance);
        tvcarrier=(TextView)findViewById(R.id.tvcarrier);
        tvhealth=(TextView)findViewById(R.id.tvhealth);
        tvmarragepartner=(TextView)findViewById(R.id.tvmarragepartner);
        tvcharashusband=(TextView)findViewById(R.id.tvcharashusband);
        tvcharaswife=(TextView)findViewById(R.id.tvcharaswife);
        tvfriendship=(TextView)findViewById(R.id.tvfriendship);
        tvluckydaydate=(TextView)findViewById(R.id.tvluckydaydate);
        tvlucklotteries=(TextView)findViewById(R.id.tvlucklotteries);
        tvluckycolor=(TextView)findViewById(R.id.tvluckycolor);
        tvluckystone=(TextView)findViewById(R.id.tvluckystone);
        tvimportantyear=(TextView)findViewById(R.id.tvimportantyear);
        tvpositivequality=(TextView)findViewById(R.id.tvpositivequality);
        tvnegativequality=(TextView)findViewById(R.id.tvnegativequality);



    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; // In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDob.setText(sdf.format(myCalendar.getTime()));
    }

    public class NumerologyAsyc extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NumerologyActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            HashMap<String, String> params = new HashMap<>();
            params.put("day", args[0]);
            params.put("month", args[1]);

            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.numerologyUrl, "POST", params);
            try {
                if (json != null) {
                    {
                        result = json.getString("success");

                        Log.i("result", result);
                        if (result.equals("1")) {


                            JSONArray jsonArray = json.getJSONArray("numerology");
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                resultday = jsonObject.getString("day");
                                resultmonth = jsonObject.getString("month");
                                characteristic = jsonObject.getString("characteristic");
                                finance = jsonObject.getString("finance");
                                carrier = jsonObject.getString("vocation");
                                health = jsonObject.getString("health");
                                marriage_partner = jsonObject.getString("marriage_partner");
                                husband_char = jsonObject.getString("husband_char");
                                wife_char = jsonObject.getString("wife_char");
                                friendship = jsonObject.getString("friendship");
                                luckydays_date = jsonObject.getString("luckydays_date");
                                luck_lotteries = jsonObject.getString("luck_lotteries");
                                lucky_colour = jsonObject.getString("lucky_colour");
                                luckystone = jsonObject.getString("luckystone");
                                importantyear = jsonObject.getString("importantyear");
                                positive_qualities = jsonObject.getString("positive_qualities");
                                negative_qualities = jsonObject.getString("negative_qualities");

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
                Toast.makeText(NumerologyActivity.this, "Successfully..", Toast.LENGTH_SHORT).show();

                tv_finance.setText(finance);
                tvcarrier.setText(carrier);
                tvhealth.setText(health);
                tvmarragepartner.setText(marriage_partner);
                tvcharashusband.setText(husband_char);
                tvcharaswife.setText(wife_char);
                tvfriendship.setText(friendship);
                tvluckydaydate.setText(luckydays_date);
                tvlucklotteries.setText(luck_lotteries);
                tvluckycolor.setText(lucky_colour);
                tvluckystone.setText(luckystone);
                tvimportantyear.setText(importantyear);
                tvpositivequality.setText(positive_qualities);
                tvnegativequality.setText(negative_qualities);


            } else {
                Toast.makeText(NumerologyActivity.this, "Failed..", Toast.LENGTH_SHORT).show();
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


