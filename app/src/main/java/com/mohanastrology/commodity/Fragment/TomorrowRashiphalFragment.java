package com.mohanastrology.commodity.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mohanastrology.commodity.DetailRashiphalActivity;
import com.mohanastrology.commodity.R;
import com.mohanastrology.commodity.javafiles.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TomorrowRashiphalFragment extends Fragment {
    private TextView tvluckpercentage,tvluckno,tvluckycolor,
    tvluckygemstone,tvlalkitabprediction,tvfavoiriteplanet,
    tvunfavoiriteplanet,tvauspicious,tvinauspicious,tvshowaspicious,tvasuspiciouswork,
    tvaspiciousfood,tvinaspiciousfood,tvrashiname;
    private static String commodityserviceUrl="http://mohangautam-001-site8.btempurl.com/app/rashiphal.php";
    private ProgressDialog pDialog;
    private JSONParser jsonParser=new JSONParser();
    String date,rash_id,result;

    String dailyremedy,luckpercentage,sub_category,favourite_planet,
            un_favourite_planet,lucky_no ,lucky_color,lucky_gem,lalkitab_prediction,lalkitab_remedy,
            auspicious_akshar_rasi,in_auspicious_akshar_rasi,show_auspicious_time ,auspicious_work,
            auspicious_food,in_auspicious_work,in_auspicious_food,luckno,name;

    public TomorrowRashiphalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_today_rashiphal, container, false);

        int id=((DetailRashiphalActivity)getActivity()).getRashiId();
        name=((DetailRashiphalActivity)getActivity()).getRashiName();
        rash_id=String.valueOf(id);

        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        date=dateFormat.format(cal.getTime());

        tvrashiname=(TextView)rootView.findViewById(R.id.tvrashiname);
        tvluckpercentage=(TextView)rootView.findViewById(R.id.tvluckpercentage);
        tvluckno=(TextView)rootView.findViewById(R.id.tvluckno);
        tvluckycolor=(TextView)rootView.findViewById(R.id.tvluckcolor);
        tvluckygemstone=(TextView)rootView.findViewById(R.id.tvluckygemstone);
        tvlalkitabprediction=(TextView)rootView.findViewById(R.id.tvlalkitab);
        tvfavoiriteplanet=(TextView)rootView.findViewById(R.id.tvfavouriteplanet);
        tvunfavoiriteplanet=(TextView)rootView.findViewById(R.id.tvunfavourite);
        tvauspicious=(TextView)rootView.findViewById(R.id.tvauspicious);
        tvinauspicious=(TextView)rootView.findViewById(R.id.tvinaspicious);
        tvshowaspicious=(TextView)rootView.findViewById(R.id.tvshowauspicious);
        tvasuspiciouswork=(TextView)rootView.findViewById(R.id.tvauspiciouswork);
        tvaspiciousfood=(TextView)rootView.findViewById(R.id.tvaspisiousfood);
        tvinaspiciousfood=(TextView)rootView.findViewById(R.id.tvinaspiciousfood);

       new TomorrowAsync().execute();


        return rootView;
    }


    public class TomorrowAsync extends AsyncTask<String,String,JSONObject>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            HashMap<String,String> params=new HashMap<>();
            params.put("rasi_no", rash_id);
            params.put("date",date);

            JSONObject json = jsonParser
                    .makeHttpRequest(commodityserviceUrl, "POST", params);
            try {
                if (json != null) {
                    {
                        result=json.getString("success");

                          Log.i("result", result);
                          if(result.equals("1")) {

                            JSONArray jsonArray = json.getJSONArray("rashiphal");
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                luckpercentage = jsonObject.getString("luck_per");
                                favourite_planet = jsonObject.getString("fav_planet");
                                 un_favourite_planet = jsonObject.getString("unfav_planet");
                                lucky_no = jsonObject.getString("lucky_no");
                                lucky_color = jsonObject.getString("color");
                                lucky_gem = jsonObject.getString("gem_stone");
                                lalkitab_prediction = jsonObject.getString("lal_kitab_pre");
                               lalkitab_remedy = jsonObject.getString("lalkitab_sabdhania");
                                auspicious_akshar_rasi = jsonObject.getString("auspicious_akshar_rasi");
                                in_auspicious_akshar_rasi = jsonObject.getString("inauspicious_akshar_rasi");
                                show_auspicious_time = jsonObject.getString("show_aus_time");
                                auspicious_work = jsonObject.getString("auspicious_work");
                                auspicious_food = jsonObject.getString("auspicious_food_item");
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
                Toast.makeText(getActivity(), "Successfully..", Toast.LENGTH_SHORT).show();

                tvrashiname.setText(name);
                tvluckpercentage.setText(luckpercentage.concat("%"));
                tvluckno.setText(lucky_no);
                tvluckycolor.setText(lucky_color);
                tvlalkitabprediction.setText(lalkitab_prediction);
                tvfavoiriteplanet.setText(favourite_planet);
                tvunfavoiriteplanet.setText(un_favourite_planet);
                tvauspicious.setText(auspicious_akshar_rasi);
                tvinauspicious.setText(in_auspicious_akshar_rasi);
                tvshowaspicious.setText(show_auspicious_time);
                tvasuspiciouswork.setText(auspicious_work);
                tvaspiciousfood.setText(auspicious_food);
                tvinaspiciousfood.setText(in_auspicious_food);
            }
            else
            {
                Toast.makeText(getActivity(), "Failed..", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
