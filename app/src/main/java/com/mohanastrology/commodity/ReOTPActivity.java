package com.mohanastrology.commodity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.mohanastrology.commodity.constant.AppUrl;
import com.mohanastrology.commodity.javafiles.ApplicationStatus;
import com.mohanastrology.commodity.javafiles.InternetConnection;
import com.mohanastrology.commodity.javafiles.JSONParser;

import org.json.JSONObject;
import java.util.HashMap;

public class ReOTPActivity extends AppCompatActivity implements OnClickListener{
    private Toolbar mToolbar;
    private EditText emailMobile;
    private Button  getOTP;
    private String email;
    //String changeMobileUrl = "http://mohangautam-001-site8.btempurl.com/app/change_mobile.php";
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();
    private ApplicationStatus status;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_otp);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setViewResources();
    }
    private void setViewResources(){
        status = new ApplicationStatus(this);
        pDialog = new ProgressDialog(this);
        emailMobile = (EditText)findViewById(R.id.editmobile);
        getOTP = (Button)findViewById(R.id.btngetotp);
        getOTP.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btngetotp:
                checkEmailValidation();
                break;
            default:
                break;
        }
    }
    private void checkEmailValidation(){
        String mobile = null;
        email = status.getEmail();
        mobile = emailMobile.getText().toString();
        if(!mobile.trim().equals("")&&mobile !=null){
            if (InternetConnection.checkNetworkConnection(this)) {
                new GenerateOTPAsync().execute(email,mobile);
            } else {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT)
                        .show();
            }
        }else{
            Toast.makeText(getBaseContext(), "Please Enter Mobile", Toast.LENGTH_SHORT).show();
        }
    }
    class GenerateOTPAsync extends AsyncTask<String, Void, JSONObject> {

        // Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args) {
            HashMap<String,String> params=new HashMap<>();
            params.put("email", args[0]);
            params.put("mobile", args[1]);
            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.changeMobileUrl, "POST", params);
            try {
                if (json != null) {
                    result = json.getString("success");
                    Log.i("result", "" + result);
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // After completing background task Dismiss the progress dialog
        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if(result.equals("2")){
                Toast.makeText(getBaseContext(), "Error Try Again", Toast.LENGTH_SHORT).show();
            }else if(result.equals("1")){
                Intent reGenerateIntent = new Intent(ReOTPActivity.this, EnterOTPActivity.class);
                startActivity(reGenerateIntent);
                finish();
            }

        }
    }
}

