package com.mohanastrology.commodity;

import android.app.ProgressDialog;
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
import com.mohanastrology.commodity.javafiles.InternetConnection;
import com.mohanastrology.commodity.javafiles.JSONParser;

import org.json.JSONObject;

import java.util.HashMap;

public class ForgetPasswordActivity extends AppCompatActivity implements OnClickListener{
    private Toolbar mToolbar;
    private EditText emailText;
    private Button submit;
    //private String forgetPasswordUrl = "http://mohangautam-001-site8.btempurl.com/app/forgot_password.php";
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_password);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setViewResources();
    }

    private void setViewResources(){
        pDialog = new ProgressDialog(this);
        emailText = (EditText)findViewById(R.id.editemail);
        submit = (Button)findViewById(R.id.btnsubmit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsubmit:
                checkEmailValidation();
                break;
            default:
                break;
        }
    }
    private void checkEmailValidation(){
        String email = null;
        email = emailText.getText().toString();
        if(!email.trim().equals("")&&email !=null){
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (InternetConnection.checkNetworkConnection(this)) {
                    new ForgetPasswordAsync().execute(email);
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT)
                            .show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Please Enater Valid Email Id !", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getBaseContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
        }
    }
    class ForgetPasswordAsync extends AsyncTask<String, Void, JSONObject> {

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
            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.forgetPasswordUrl, "POST", params);
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
                Toast.makeText(getBaseContext(), "Email not Register", Toast.LENGTH_SHORT).show();
            }else if(result.equals("1")){
                Toast.makeText(getBaseContext(), "Please Check Your Email", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
