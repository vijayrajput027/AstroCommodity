package com.mohanastrology.commodity;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mohanastrology.commodity.constant.AppUrl;
import com.mohanastrology.commodity.javafiles.ApplicationStatus;
import com.mohanastrology.commodity.javafiles.InternetConnection;
import com.mohanastrology.commodity.javafiles.JSONParser;

import org.json.JSONObject;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity implements
        OnClickListener, OnCheckedChangeListener {
    private EditText editName, editEmail ,editMobile, editPassword;
    private CheckBox termAndCondition;
    private Button btnRegister;
    private String name, email, mobile, password;
  //  private String registerUrl = "http://mohangautam-001-site8.btempurl.com/app/user_registration.php";
    private boolean isTermSelected = false;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();
    private ApplicationStatus status;
    private TextView conditionText, privacyText;
    private String result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setViewResources();
        pDialog = new ProgressDialog(this);

    }

    private void setViewResources() {
        status = new ApplicationStatus(this);
        editName = (EditText) findViewById(R.id.editname);
        editEmail = (EditText) findViewById(R.id.editemail);
        editMobile = (EditText) findViewById(R.id.editmobile);
        editPassword = (EditText) findViewById(R.id.editpassword);
        termAndCondition = (CheckBox) findViewById(R.id.checkboxterm);
        btnRegister = (Button) findViewById(R.id.btnregister);
        conditionText = (TextView) findViewById(R.id.textconditon);
        privacyText = (TextView) findViewById(R.id.textprivacy);
        conditionText.setOnClickListener(this);
        privacyText.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        termAndCondition.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textconditon:
                conditionDialog();
                break;
            case R.id.textprivacy:
                conditionDialog();
                break;
            case R.id.btnregister:
                registerValidation();
                break;
            default:
                break;
        }
    }

    private void registerValidation() {
        name = editName.getText().toString();
        email = editEmail.getText().toString();
        mobile = editMobile.getText().toString();
        password = editPassword.getText().toString();

        if (!name.trim().equals("") && name != null) {
            if (email != null && !email.isEmpty()) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches()) {
                    if (mobile != null && !mobile.isEmpty()) {
                        System.out.println(mobile);
                        if (mobile.length() >= 10) {
                            if (!password.trim().equals("")
                                    && password != null) {
                                if (isTermSelected) {
                                    if (InternetConnection
                                            .checkNetworkConnection(this)) {
                                        new LoginAsync().execute();
                                    } else {
                                        Toast.makeText(
                                                getBaseContext(),
                                                getResources()
                                                        .getString(
                                                                R.string.no_internet),
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                } else {
                                    Toast.makeText(
                                            getBaseContext(),
                                            "Please select Terms & Condition!",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            } else {
                                Toast.makeText(getBaseContext(),
                                        "Please enter Password!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext(),
                                    "Please Enter Valid mobile No !", Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Please Enter mobile No", Toast.LENGTH_LONG)
                                    .show();
                        }
                }   else {
                    Toast.makeText(getApplicationContext(),
                            "Please Enter Valid Email Id !", Toast.LENGTH_LONG)
                            .show();
                }

            } else {
                Toast.makeText(getBaseContext(), "Please enter Email!",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getBaseContext(), "Please enter Name!",
                    Toast.LENGTH_SHORT).show();
        }
    }
    class LoginAsync extends AsyncTask<Void, Void, JSONObject> {
        int success;

        // Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            // pDialog.setCancelable(true);
            pDialog.show();
        }

        @SuppressWarnings("deprecation")
        protected JSONObject doInBackground(Void... args) {
            HashMap<String,String> params = new HashMap<>();
            params.put("name", name);
            params.put("email", email);
            params.put("mobile", mobile);
            params.put("password", password);
            params.put("agreement", "true");

            Log.i("name", "" + name);
            Log.i("email", ""+email);
            Log.i("mobile", ""+mobile);
            Log.i("password", ""+password);


            JSONObject json = jsonParser.makeHttpRequest(AppUrl.registerUrl, "POST",
                    params);
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

            if (result.equals("0")) {
                Toast.makeText(getBaseContext(), "Registration Fail",
                        Toast.LENGTH_SHORT).show();
            } else if (result.equals("1")) {
                status.setEmail(email);
                status.setLoginStatus(true);
                Intent otpIntent = new Intent(RegistrationActivity.this,
                        EnterOTPActivity.class);
                startActivity(otpIntent);
                finish();
            } else if (result.equals("2")) {
                Toast.makeText(getBaseContext(), "Email Already Register",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isTermSelected = isChecked;
    }


    private void conditionDialog() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog dialog = new Dialog(RegistrationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.terms_condition_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout((6 * width)/7, LayoutParams.WRAP_CONTENT);


        Button pay = (Button) dialog.findViewById(R.id.okButton);
        // if button is clicked, close the custom dialog
        pay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}