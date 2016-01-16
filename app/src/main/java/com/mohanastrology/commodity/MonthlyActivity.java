package com.mohanastrology.commodity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MonthlyActivity extends AppCompatActivity implements DailyAdapter.ViewOnButtonClickListner,
        View.OnClickListener{

    private JSONParser jParser=new JSONParser();
    private ProgressDialog pDialog;
    //public static String monthlyUrl="http://mohangautam-001-site8.btempurl.com/app/commodity_purchase_entry.php";
    private ApplicationStatus status;
    private Spinner quarterMonthSpinner,yearSpinner;
    private ImageButton addBtn;
    private RecyclerView recyclerView;
    private String Quarter[]={"Select Quarter","1st Quarter","2nd Quarter","3rd Quarter","4th Quarter"};
    private ArrayAdapter<String> quarterAdapter;
    private ArrayAdapter<String> yearAdapter;
    private List<String> yearList,finalList;
    private String quarter,year,seletDate;
    private DailyAdapter dailyAdapter;
    private Toolbar mToolbar;
    private int priceValue,size;
    private TextView tvPrice;
    private Button btnsubmit;
    private String user_email,category_name,dateValues,result,category_id,sub_category_id,price,listsize,parent_id;
    //private static String purchaseUrl ="http://mohangautam-001-site8.btempurl.com/app/purchase_services.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Monthly");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        String price=intent.getStringExtra("price");
        parent_id=intent.getStringExtra("parent_id");
        category_name=intent.getStringExtra("commodity_name");
        category_id=intent.getStringExtra("category_id");
        sub_category_id=intent.getStringExtra("subcategory_id");

        try {
            priceValue = Integer.parseInt(price);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        status=new ApplicationStatus(this);
        user_email=status.getEmail();

        quarterMonthSpinner=(Spinner)findViewById(R.id.sp_qurter_month);
        yearSpinner=(Spinner)findViewById(R.id.sp_year);
        addBtn=(ImageButton)findViewById(R.id.addItem);
        tvPrice=(TextView)findViewById(R.id.priceValue);
        btnsubmit=(Button)findViewById(R.id.btnprocees);

        yearList=new ArrayList<String>();
        finalList=new ArrayList<String>();

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=thisYear;i<=2050;i++)
        {
            yearList.add(Integer.toString(i));
        }
        quarterAdapter = new ArrayAdapter<String>(this,R.layout.item,Quarter);

        quarterMonthSpinner.setAdapter(quarterAdapter);
        quarterMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quarter = quarterMonthSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
         yearAdapter = new ArrayAdapter<String>(this,R.layout.item, yearList);
         yearSpinner.setAdapter(yearAdapter);
         yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 year = yearSpinner.getSelectedItem().toString();
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

        addBtn.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);

        dailyAdapter=new DailyAdapter(MonthlyActivity.this,finalList);
        recyclerView.setAdapter(dailyAdapter);
        dailyAdapter.setonclick(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.addItem:
                seletDate = quarter + " " + year;
                finalList.add(seletDate);
                dailyAdapter.notifyDataSetChanged();
                size = finalList.size();
                price=String.valueOf(size*priceValue);
                tvPrice.setText("Price :" + " " + String.valueOf(size * priceValue));

                break;

            case R.id.btnprocees:
                dateValues=finalList.toString().replace("[", "").replace("]", "")
                        .replace(", ", ",");
                paymentDialog("1");

                break;
        }
    }
    @Override
    public void onItemClick(int position) {
        finalList.remove(position);
        dailyAdapter.notifyDataSetChanged();
        int size=finalList.size();
        listsize=String.valueOf(size);
        tvPrice.setText("Price :" + " " + String.valueOf(size * priceValue));

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

    public class GetMonthlyAync extends AsyncTask<String,String,JSONObject> {

        int success;
        // Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MonthlyActivity.this);
            pDialog.setMessage("Loading....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... urll) {
            HashMap<String, String> params = new HashMap<String, String>();

            params.put("parent_id",parent_id);
            params.put("user_id", "niraj.semi@gmail.com");
            params.put("item_date", dateValues);
            params.put("category",category_id);
            params.put("sub_category",sub_category_id);

            JSONObject json = jParser
                    .makeHttpRequest(AppUrl.monthlyUrl, "POST", params);
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
                dailyAdapter.notifyDataSetChanged();
            }

        }
    }

    private void paymentDialog(final String price) {
        final Dialog dialog = new Dialog(MonthlyActivity.this);
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
                    Intent intent = new Intent(MonthlyActivity.this,
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
            params.put("no_of_prediction", listsize);

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
                    new GetMonthlyAync().execute();
                }
            }

        }
    }
}
