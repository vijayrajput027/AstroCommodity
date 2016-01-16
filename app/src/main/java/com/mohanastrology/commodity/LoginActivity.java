package com.mohanastrology.commodity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private Button btnLogin, btnJoinNow;
    private EditText editUserName, editPassword;
    private TextView forgetPassword;
    private JSONParser jsonParser = new JSONParser();
    private ApplicationStatus status;
    private String userName = null, password = null;
    public static Activity fa;
    private String result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fa=this;
        setViewResources();

    }

    private void setViewResources() {

        status = new ApplicationStatus(this);
        btnLogin = (Button) findViewById(R.id.btnlogin);
        btnJoinNow = (Button) findViewById(R.id.btnjoin);
        editUserName = (EditText) findViewById(R.id.editusername);
        editPassword = (EditText) findViewById(R.id.editpassword);
        forgetPassword = (TextView) findViewById(R.id.textforgetpassword);
        forgetPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnJoinNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnlogin:
                checkLoginValidation();
                break;
            case R.id.btnjoin:
                Intent registrationIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(registrationIntent);
                break;
            case R.id.textforgetpassword:
                Intent forgetPaawordIntent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(forgetPaawordIntent);
                break;

            default:
                break;
        }

    }
    private void checkLoginValidation(){
        userName = editUserName.getText().toString();
        password = editPassword.getText().toString();
        if(!userName.trim().equals("")&&userName !=null){
            if(!password.trim().equals("")&&password !=null){
                if (InternetConnection.checkNetworkConnection(this)) {
                    new LoginAsync().execute(userName, password);
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT)
                            .show();
                }
            }else {
                Toast.makeText(getBaseContext(), "Please enter Password", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getBaseContext(), "Please enter User name", Toast.LENGTH_SHORT).show();
        }
    }

    class LoginAsync extends AsyncTask<String, Void, JSONObject> {
        int success;
        private ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
        // Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            // pDialog.setCancelable(true);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args) {
            HashMap<String,String> params = new HashMap<>();
            params.put("email", userName);
            params.put("password",password);
            JSONObject json = jsonParser
                    .makeHttpRequest(AppUrl.loginUrl, "POST", params);
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
            if(result.equals("1")){
                status.setLoginStatus(true);
                status.setEmail(userName);
                Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
                finish();
            }
            else if(result.equals("0")){
                Toast.makeText(getBaseContext(), "Please Check Username & Password", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
