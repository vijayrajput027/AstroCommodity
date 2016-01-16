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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mohanastrology.commodity.adapter.DailyAdapter;
import com.mohanastrology.commodity.constant.AppUrl;
import com.mohanastrology.commodity.javafiles.ApplicationStatus;
import com.mohanastrology.commodity.javafiles.DateDisplayUtils;
import com.mohanastrology.commodity.javafiles.JSONParser;
import com.mohanastrology.commodity.utility.AvenuesParams;
import com.mohanastrology.commodity.utility.ServiceUtility;
import com.mohanastrology.commodity.widget.SimpleDatePickerDialog;
import com.mohanastrology.commodity.widget.SimpleDatePickerDialogFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DailyActivity extends AppCompatActivity implements SimpleDatePickerDialog.OnDateSetListener
        ,DailyAdapter.ViewOnButtonClickListner,View.OnClickListener
  {
      private JSONParser jParser=new JSONParser();
      private ProgressDialog pDialog;
      //public static String dailyUrl="http://mohangautam-001-site8.btempurl.com/app/commodity_purchase_entry.php";
      private DailyAdapter dailyAdapter;
      private EditText etDate;
      private RecyclerView recyclerView;
      private ImageButton addButton;
      private TextView tvprice;
      private Button btnsubmit;
      private List<String> list;
      private Toolbar mToolbar;
      private int priceValue;
      private String result,user_email,category_name,dateValues,category_id,sub_category_id,price,parent_id;
      private ApplicationStatus status;
     // private static String purchaseUrl ="http://mohangautam-001-site8.btempurl.com/app/purchase_services.php";
      private String size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Daily");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        String price=intent.getStringExtra("price");
        try {
        priceValue=Integer.parseInt(price);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        parent_id=intent.getStringExtra("parent_id");
        category_name=intent.getStringExtra("commodity_name");
        category_id=intent.getStringExtra("category_id");
        sub_category_id=intent.getStringExtra("subcategory_id");

        status=new ApplicationStatus(this);
        user_email=status.getEmail();

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<String>();

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int Month=Calendar.getInstance().get(Calendar.MONTH);
        String firstDate=(Month+1)+"/"+thisYear;

        etDate=(EditText)findViewById(R.id.editDate);
        tvprice=(TextView)findViewById(R.id.priceValue);
        btnsubmit=(Button)findViewById(R.id.submit);

        etDate.setText(firstDate);
        if(list.isEmpty())
        {
            tvprice.setText("");
        }
        addButton=(ImageButton)findViewById(R.id.addItem);
        etDate.setOnClickListener(this);
        addButton.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);

        dailyAdapter=new DailyAdapter(DailyActivity.this, list);
        recyclerView.setAdapter(dailyAdapter);
        dailyAdapter.setonclick(this);

    }
      @Override
      public void onClick(View v) {
          switch (v.getId())
          {
              case R.id.editDate:

                  displaySimpleDatePickerDialogFragment();
                  break;

              case R.id.addItem:

                  list.add(etDate.getText().toString());
                  dailyAdapter.notifyDataSetChanged();
                  int itemSize = list.size();
                  price=String.valueOf(itemSize*priceValue);
                  tvprice.setText("Price :" + " " + String.valueOf(itemSize * priceValue));
                  break;

              case R.id.submit:
                  dateValues=list.toString().replace("[", "").replace("]", "")
                          .replace(", ", ",");
                 paymentDialog("1");

                  break;
          }
      }
    @Override
    public void onDateSet(int year, int monthOfYear) {
        etDate.setText(DateDisplayUtils.formatMonthYear(year, monthOfYear));

    }
        private void displaySimpleDatePickerDialogFragment() {
            SimpleDatePickerDialogFragment datePickerDialogFragment;
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            datePickerDialogFragment = SimpleDatePickerDialogFragment.getInstance(
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
            datePickerDialogFragment.setOnDateSetListener(this);
            datePickerDialogFragment.show(getFragmentManager(), null);
        }


      @Override
      public void onItemClick(int position) {
          list.remove(position);
          dailyAdapter.notifyDataSetChanged();
          int itemSize = list.size();
          size=String.valueOf(itemSize);
          price=String.valueOf(itemSize*priceValue);
          tvprice.setText("Price :" + " " + 1);

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



      public class GetDailyAync extends AsyncTask<String,String,JSONObject> {

          int success;
          // Before starting background thread Show Progress Dialog
          @Override
          protected void onPreExecute() {
              super.onPreExecute();
              pDialog = new ProgressDialog(DailyActivity.this);
              pDialog.setMessage("Loading....");
              pDialog.setIndeterminate(false);
              pDialog.setCancelable(false);
              pDialog.show();
          }

          @Override
          protected JSONObject doInBackground(String... urll) {
              HashMap<String, String> params = new HashMap<String, String>();
              params.put("parent_id",parent_id);
              params.put("user_id","niraj.semi@gmail.com");
              params.put("item_date", dateValues);
              params.put("category",category_id);
              params.put("sub_category",sub_category_id);
              JSONObject json = jParser
                      .makeHttpRequest(AppUrl.dailyUrl, "POST", params);
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
                  Toast.makeText(getBaseContext(), "Success...!",
                          Toast.LENGTH_SHORT).show();
                  dailyAdapter.notifyDataSetChanged();
              }

          }
      }

      private void paymentDialog(final String price) {
          final Dialog dialog = new Dialog(DailyActivity.this);
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
                      Intent intent = new Intent(DailyActivity.this,
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
                      new GetDailyAync().execute();
                  }
              }

          }
      }
  }
