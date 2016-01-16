package com.mohanastrology.commodity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mohanastrology.commodity.adapter.DailyAdapter;
import com.mohanastrology.commodity.constant.AppUrl;
import com.mohanastrology.commodity.javafiles.ApplicationStatus;
import com.mohanastrology.commodity.javafiles.JSONParser;
import com.mohanastrology.commodity.utility.AvenuesParams;
import com.mohanastrology.commodity.utility.ServiceUtility;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class IntraDay extends AppCompatActivity implements View.OnClickListener, DailyAdapter.ViewOnButtonClickListner {

    private JSONParser jParser=new JSONParser();
    private ProgressDialog pDialog;
    private List<String> listItem;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DailyAdapter adapter;
    private String result,dateValues,category_name,user_email,category_id,sub_category_id,price,parent_id;
    private ImageButton addBtn;
    private EditText dateValue;
    private TextView priceValue;
    private Button submit_btn;
    private Calendar myCalendar;
    private int value;
    private ApplicationStatus status;
    private String size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intra_day);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("IntraDay");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        try {
            String price = intent.getStringExtra("price");
            value = Integer.parseInt(price);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        parent_id=intent.getStringExtra("parent_id");
        category_name=intent.getStringExtra("commodity_name");
        category_id=intent.getStringExtra("category_id");
        sub_category_id=intent.getStringExtra("subcategory_id");

        priceValue=(TextView)findViewById(R.id.priceValue);
        dateValue=(EditText)findViewById(R.id.dateValue);
        addBtn=(ImageButton)findViewById(R.id.addBtn);
        status=new ApplicationStatus(this);
        user_email=status.getEmail();

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int month=Calendar.getInstance().get(Calendar.MONTH);
        int dayOfMonth=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String firstDate=dayOfMonth+"/"+(month+1)+"/"+thisYear;

        dateValue.setText(firstDate);

        submit_btn=(Button)findViewById(R.id.submit);
        dateValue.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);

        listItem=new ArrayList<String>();
        recyclerView=(RecyclerView)findViewById(R.id.listRecyclerViewIntra);
        mLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter=new DailyAdapter(IntraDay.this,listItem);
        recyclerView.setAdapter(adapter);
        adapter.setonclick(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.addBtn:
                String strAddMe = dateValue.getText().toString();
                if(strAddMe != null && !strAddMe.isEmpty()){
                    //  if (strAddMe.equals(listItem)) {
                    listItem.add(strAddMe);
                    int itemSize = listItem.size();
                    price=String.valueOf(itemSize*value);
                    priceValue.setText("Price :" + " " + itemSize * value);
                  //  dateValue.getText().clear();
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(IntraDay.this, "please select date first !", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.dateValue:
                myCalendar = Calendar.getInstance();
                new DatePickerDialog(IntraDay.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.submit:
                dateValues=listItem.toString().replace("[", "").replace("]", "")
                        .replace(", ", ",");

                 paymentDialog("1");


                break;
        }
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
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

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; // In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateValue.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onItemClick(int position) {
        listItem.remove(position);
        adapter.notifyDataSetChanged();
        int itemSize = listItem.size();
        size=String.valueOf(itemSize);
        price=String.valueOf(itemSize*value);
        priceValue.setText("Price :" + " " + 1);

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
    public class GetIntraAync extends AsyncTask<String,String,JSONObject> {

        int success;

        // Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(IntraDay.this);
            pDialog.setMessage("Loading....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... url) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("user_id", "niraj.semi@gmail.com");
            params.put("parent_id",parent_id);
            params.put("item_date", dateValues);
            params.put("category",category_id);
            params.put("sub_category",sub_category_id);

            JSONObject json = jParser
                    .makeHttpRequest(AppUrl.intraUrl, "POST", params);
            try {
                if (json != null) {
                    result = json.getString("success");
                    Log.i("result", "" + result);
                    if (result.equals("1")) {
                        Log.i("successfully", "done");
                    }
            return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // After completing background task Dismiss the progress dialog
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (result.equals("0")) {
                Toast.makeText(getBaseContext(), "Server Error!",
                        Toast.LENGTH_SHORT).show();
            } else if (result.equals("1")) {
                Toast.makeText(getBaseContext(), "Work Done!",
                        Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        }
        }

    private void paymentDialog(final String price) {
        final Dialog dialog = new Dialog(IntraDay.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.payment_initial_screen);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        final EditText accessCode;
        final EditText merchantId;
        final EditText currency;
        final TextView amount;
        final EditText orderId;
        final EditText rsaKeyUrl;
        final EditText redirectUrl;
        final EditText cancelUrl;

        accessCode = (EditText) dialog.findViewById(R.id.accessCode);
        merchantId = (EditText) dialog.findViewById(R.id.merchantId);
        orderId = (EditText) dialog.findViewById(R.id.orderId);
        currency = (EditText) dialog.findViewById(R.id.currency);
        amount = (TextView) dialog.findViewById(R.id.amount);
        rsaKeyUrl = (EditText) dialog.findViewById(R.id.rsaUrl);
        redirectUrl = (EditText) dialog.findViewById(R.id.redirectUrl);
        cancelUrl = (EditText) dialog.findViewById(R.id.cancelUrl);

        amount.setText(price);
        // generating order number
        Integer randomNum = ServiceUtility.randInt(0, 9999999);
        orderId.setText(randomNum.toString());

        Button pay = (Button) dialog.findViewById(R.id.nextButton);
        // if button is clicked, close the custom dialog
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String vAccessCode = ServiceUtility
                        .chkNull(accessCode.getText()).toString().trim();
                String vMerchantId = ServiceUtility
                        .chkNull(merchantId.getText()).toString().trim();
                String vCurrency = ServiceUtility.chkNull(currency.getText())
                        .toString().trim();
                String vAmount = ServiceUtility.chkNull(amount.getText())
                        .toString().trim();
                if (!vAccessCode.equals("") && !vMerchantId.equals("")
                        && !vCurrency.equals("") && !vAmount.equals("")) {
                    Intent intent = new Intent(IntraDay.this,
                            PaymentWebViewActivity.class);
                    intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility
                            .chkNull(accessCode.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility

                            .chkNull(merchantId.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility
                            .chkNull(orderId.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility
                            .chkNull(currency.getText()).toString().trim());
                    // changes by vijay @ 21072015
                    intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility
                            .chkNull(amount.getText()).toString().trim());
                    // intent.putExtra(AvenuesParams.AMOUNT, "1");
                    intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility
                            .chkNull(redirectUrl.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility
                            .chkNull(cancelUrl.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility
                            .chkNull(rsaKeyUrl.getText()).toString().trim());

                    startActivityForResult(intent, 1001);
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == 1001) {
          new PurchaseEntryAsync().execute(AppUrl.purchaseUrl);
        }

    }

    class PurchaseEntryAsync extends AsyncTask<String, Void, JSONObject> {
        int success;

        // Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(IntraDay.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... url) {

            HashMap<String,String> params=new HashMap<>();

            params.put("parent_id", parent_id);
            params.put("user_id", "niraj.semi@gmail.com");
            params.put("category_id", category_id);
            params.put("sub_cat_id", sub_category_id);
            params.put("no_of_prediction", size);

            JSONObject json = jParser
                    .makeHttpRequest(AppUrl.purchaseUrl, "POST", params);
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
            if (result != null) {
                if (result.equals("0")) {
                    Toast.makeText(getBaseContext(), "Server Error!",
                            Toast.LENGTH_SHORT).show();
                } else if (result.equals("1")) {
                       new GetIntraAync().execute();
                }
            }

        }
    }
    }


