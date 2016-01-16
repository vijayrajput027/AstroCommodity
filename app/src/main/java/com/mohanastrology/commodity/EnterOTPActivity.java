package com.mohanastrology.commodity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.mohanastrology.commodity.constant.AppUrl;
import com.mohanastrology.commodity.javafiles.ApplicationStatus;
import com.mohanastrology.commodity.javafiles.InternetConnection;
import com.mohanastrology.commodity.javafiles.JSONParser;

import org.json.JSONObject;

import java.util.HashMap;

public class EnterOTPActivity extends AppCompatActivity implements OnClickListener {
    private Toolbar mToolbar;
    private Button btnSubmit;
    private EditText editOtp;
    private TextView regenerateOTP, changeMobile;
   /* private final String enterOTPUrl = "http://mohangautam-001-site8.btempurl.com/app/validate_otp.php";
    private final String reSendUTPUrl = "http://mohangautam-001-site8.btempurl.com/app/resend_otp.php";*/
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();
    private ApplicationStatus status;
    private String result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setViewResources();
    }

    private void setViewResources() {
        pDialog = new ProgressDialog(this);
        status = new ApplicationStatus(this);
        btnSubmit = (Button) findViewById(R.id.btnsubmit);
        editOtp = (EditText) findViewById(R.id.editotp);
        regenerateOTP = (TextView)findViewById(R.id.textResend);
        changeMobile = (TextView)findViewById(R.id.textChangeNumber);
        btnSubmit.setOnClickListener(this);
        regenerateOTP.setOnClickListener(this);
        changeMobile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsubmit:
                checkLoginValidation();
                break;
            case R.id.textResend:
                new ReSendOTPAsync().execute(status.getEmail());
                break;
            case R.id.textChangeNumber:
                Intent reGenerateIntent = new Intent(EnterOTPActivity.this, ReOTPActivity.class);
                startActivity(reGenerateIntent);
                finish();
                break;
            default:
                break;
        }

    }
    private void checkLoginValidation(){
        String otp = editOtp.getText().toString();
        if(!otp.trim().equals("")&&otp!=null){
            if (InternetConnection.checkNetworkConnection(this)) {
                new LoginAsync().execute(status.getEmail(),otp);
            } else {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT)
                        .show();
            }
        }else{
            Toast.makeText(getBaseContext(), "Please enter otp", Toast.LENGTH_SHORT).show();
        }
    }

    class LoginAsync extends AsyncTask<String, Void, JSONObject> {

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
            HashMap<String,String> params = new HashMap<>();
            params.put("email", args[0]);
            params.put("otp", args[1]);
            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.enterOTPUrl, "POST", params);
            try {
                if (json != null) {
                    result = json.getString("success");
                    Log.i("result", ""+result);
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

            if(result.equals("0")){
                Toast.makeText(getBaseContext(), "OTP Not Match. Try Again!", Toast.LENGTH_SHORT).show();
            }else if(result.equals("1")){
                status.setLoginStatus(true);
                Intent splashIntent = new Intent(EnterOTPActivity.this, OTPSplashActivity.class);
                startActivity(splashIntent);
                finish();
            }
        }
    }

    class ReSendOTPAsync extends AsyncTask<String, Void, JSONObject> {

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
            HashMap<String,String> params = new HashMap<>();
            params.put("email", args[0]);
            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.reSendUTPUrl, "POST", params);
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

            if(result.equals("0")){
                Toast.makeText(getBaseContext(), "OTP Not Generated", Toast.LENGTH_SHORT).show();
            }else if(result.equals("1")){
                Toast.makeText(getBaseContext(), "Enter New OTP", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
