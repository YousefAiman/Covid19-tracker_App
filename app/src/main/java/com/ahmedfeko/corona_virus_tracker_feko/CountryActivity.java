package com.ahmedfeko.corona_virus_tracker_feko;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CountryActivity extends AppCompatActivity {
    int failCount =0;
    BarChart barChart;
    String countryName;
    List<Integer> dates;
    ImageView menuIv;
    Toolbar toolbar;
    List<Float> previousKeys;
    int thisMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                final InterstitialAd mInterstitialAd = new InterstitialAd(getApplicationContext());
//                mInterstitialAd.setAdUnitId("ca-app-pub-6990486336142688/1020458813");
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                mInterstitialAd.setAdListener(new AdListener(){
//                    @Override
//                    public void onAdLoaded() {
//                        mInterstitialAd.show();
//                        Log.d("ttt","loaded");
//                    }
//                    @Override
//                    public void onAdFailedToLoad(int i) {
//                        Log.d("ttt","Failed");
//                    }
//                });
//            }
//        });

        dates = new ArrayList<>();

        menuIv =  findViewById(R.id.menuIv);
        barChart = findViewById(R.id.barChart);

        toolbar=  findViewById(R.id.toolbar);

        final ImageView menuIv3= findViewById(R.id.menuIv3);
        menuIv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(3,new Intent(getApplicationContext(),MapsActivity.class).putExtra("country",countryName));
                finish();
            }
        });
        countryName = getIntent().getStringExtra("countryName");
        TextView countryTv = findViewById(R.id.countryTv);
        countryTv.setText(countryName);

        ImageView globeImage= findViewById(R.id.imageView3);

        globeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuIv3.performClick();
            }
        });

        ImageView menuId= findViewById(R.id.menuId);

        menuId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(CountryActivity.this, toolbar);
                popup.getMenuInflater().inflate(R.menu.options_menu_country, popup.getMenu());
                Menu menu = popup.getMenu();
                for(int i=0;i<menu.size();i++){
                    MenuItem item =menu.getItem(i);
                    SpannableString s = new SpannableString(item.getTitle());
                    s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.yellow)), 0, s.length(), 0);
                    item.setTitle(s);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.selectCountryItem:
                                setResult(4);
                                finish();
                                return true;
                            case R.id.totalAmountItem:
                                setResult(5);
                                finish();
                                return true;
                            case R.id.lastThreeItem:
                                if(!countryName.equals("World Wide")){
                                    startActivity(new Intent(CountryActivity.this,LastThreeMonthsActivity.class).putExtra("countryName",countryName));
                                    finish();
                                }else{
                                    startActivity(new Intent(CountryActivity.this,LastThreeMonthsActivity.class).putExtra("countryName","World Wide"));
                                    finish();
                                }
                                return true;
                            case R.id.identifyItem:

                                startActivity(new Intent(CountryActivity.this,InfoActivity.class).putExtra("countryName",countryName));
                                finish();
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popup.show();
            }
        });

        final ImageView yearIv = findViewById(R.id.yearIv);
        final TextView yearTv = findViewById(R.id.yearTv);

        if(!countryName.equals("World Wide")){
//            fetchFromApi(countryName,1);
        }else{
//            fetchWorldStats(1);
        }

        yearIv.setImageResource(R.drawable.ic_year_red);
        yearTv.setTextColor(getResources().getColor(R.color.red));

        final ImageView monthIv = findViewById(R.id.monthIv);
        final TextView monthTv = findViewById(R.id.monthTv);

        final ImageView weekIv = findViewById(R.id.weekIv);
        final TextView weekTv = findViewById(R.id.weekTv);

        final ImageView dayIv = findViewById(R.id.dayIv);
        final TextView dayTv = findViewById(R.id.dayTv);

        yearIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearIv.setImageResource(R.drawable.ic_year_red);
                yearTv.setTextColor(getResources().getColor(R.color.red));

                monthIv.setImageResource(R.drawable.ic_month);
                monthTv.setTextColor(getResources().getColor(R.color.textGrey));

                weekIv.setImageResource(R.drawable.ic_week);
                weekTv.setTextColor(getResources().getColor(R.color.textGrey));

                dayIv.setImageResource(R.drawable.ic_day);
                dayTv.setTextColor(getResources().getColor(R.color.textGrey));


                if(!countryName.equals("World Wide")){
                    Log.d("ttt","country name: "+countryName);
//                    fetchFromApi(countryName,1);
                }else{
//                    fetchWorldStats(1);
                }

            }
        });

        monthIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                monthIv.setImageResource(R.drawable.ic_month_red);
                monthTv.setTextColor(getResources().getColor(R.color.red));

                yearIv.setImageResource(R.drawable.ic_year);
                yearTv.setTextColor(getResources().getColor(R.color.textGrey));

                weekIv.setImageResource(R.drawable.ic_week);
                weekTv.setTextColor(getResources().getColor(R.color.textGrey));

                dayIv.setImageResource(R.drawable.ic_day);
                dayTv.setTextColor(getResources().getColor(R.color.textGrey));

                fetchMonthlyProgression(countryName);

            }
        });

        weekIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                weekIv.setImageResource(R.drawable.ic_week_red);
                weekTv.setTextColor(getResources().getColor(R.color.red));


                yearIv.setImageResource(R.drawable.ic_year);
                yearTv.setTextColor(getResources().getColor(R.color.textGrey));

                monthIv.setImageResource(R.drawable.ic_month);
                monthTv.setTextColor(getResources().getColor(R.color.textGrey));

                dayIv.setImageResource(R.drawable.ic_day);
                dayTv.setTextColor(getResources().getColor(R.color.textGrey));

                if(!countryName.equals("World Wide")){
                    fetchWeeklyProgression(countryName);
                }else{

//                    fetchWorldStats(3);
                }

            }
        });

        dayIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dayIv.setImageResource(R.drawable.ic_day_red);
                dayTv.setTextColor(getResources().getColor(R.color.red));

                yearIv.setImageResource(R.drawable.ic_year);
                yearTv.setTextColor(getResources().getColor(R.color.textGrey));

                monthIv.setImageResource(R.drawable.ic_month);
                monthTv.setTextColor(getResources().getColor(R.color.textGrey));

                weekIv.setImageResource(R.drawable.ic_week);
                weekTv.setTextColor(getResources().getColor(R.color.textGrey));

                if(!countryName.equals("World Wide")){
                    fetchDailyProgression(countryName);
                }else{
                    fetchDailyProgression("World Wide");
                }
            }
        });


        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    private void fetchDailyProgression(final String country){

        final ProgressDialog progressDialog = showProgressDialog();
        final ImageView progressAnimated = progressDialog.findViewById(R.id.progressAnimated);

        final ArrayList<BarEntry> countryValues = new ArrayList<>();

                final Calendar cal = Calendar.getInstance(Locale.getDefault());
                final String todayFormatted = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(cal.getTime());
                Log.d("ttt","todayFormatted: "+todayFormatted);

//                final String countryCode = getCountryCode(countryName);
//                Log.d("ttt","countryCode: "+countryCode);

                final String url = "https://covid-193.p.rapidapi.com/history?country="+
                        (country.equals("World Wide")?"All":country) +"&day="+todayFormatted;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {

           new Thread(new Runnable() {
            @Override
            public void run() {

                        try {
                            if(!response.has("errors") || response.getJSONArray("errors").length() == 0){

                                previousKeys = new ArrayList<>();

                                final JSONArray responseArray = response.getJSONArray("response");

                                for(int i=0;i<responseArray.length();i++){
                                    JSONObject object =responseArray.getJSONObject(i);
                                    float dateFloat = Float.parseFloat(object.getString("time").split("T")[1].substring(0,5).replace(":","."));

                                    Log.d("ttt","hour and minute: "+dateFloat);

                                    if(!previousKeys.contains(dateFloat)){

                                        final int cases = object.getJSONObject("cases").getInt("active");
                                        if(cases == 0)continue;

                                        previousKeys.add(dateFloat);
                                        countryValues.add(new BarEntry(dateFloat,cases));
                                    }
                                }

                                final BarDataSet barDataSet = new BarDataSet(countryValues, "Country cases");
                                barDataSet.setValueTextColor(Color.rgb(207,207,207));
                                barDataSet.setColor(Color.rgb(254,14,44));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        BarData barData =new BarData();
                                        barData.addDataSet(barDataSet);
                                        barChart.setData(barData);
                                        barChart.setFitBars(true);
                                        barChart.setBorderWidth(2);
                                        barChart.invalidate();
                                        progressAnimated.clearAnimation();
                                        progressDialog.dismiss();

                                    }
                                });

                            }
                        } catch (JSONException e) {
                            failCount++;
                            if(failCount < 2){
                                Log.d("ttt",e.getMessage());
                                fetchDailyProgression(countryName);
                                Thread.currentThread().interrupt();
                            }else{
                                Log.d("ttt",e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressAnimated.clearAnimation();

                                        progressDialog.dismiss();

                                    }
                                });
                                Thread.currentThread().interrupt();
                            }
                            e.printStackTrace();
                        }


            }
        }).start();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ttt","repionse error; " +error.toString());
                        progressDialog.dismiss();
                    }
                }){

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("x-rapidapi-host", "covid-193.p.rapidapi.com");
                        params.put("x-rapidapi-key", "1fb7627ee8mshf96cc54cb26632ap1a9163jsnec5362defe84");
                        return params;

                    }
                };



        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }

    private void fetchWeeklyProgression(final String country){

        final ProgressDialog progressDialog = showProgressDialog();
        final ImageView progressAnimated = progressDialog.findViewById(R.id.progressAnimated);

        final ArrayList<BarEntry> countryValues = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {


                final Calendar cal = Calendar.getInstance(Locale.getDefault());
                final String todayFormatted = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(cal.getTime());
                Log.d("ttt","todayFormatted: "+todayFormatted);

                final String countryCode = getCountryCode(country).toUpperCase();
                Log.d("ttt","countryCode: "+countryCode);

                final String url = "https://covid-19-global-tracker-with-regional-data.p.rapidapi.com/api/covid/weeklyRegionalTotalCases/"+countryCode;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.has("success") && response.getBoolean("success")){

                                JSONObject mainObject =  response.getJSONObject("data");
                                Iterator<String> keys =  mainObject.keys();
                                previousKeys = new ArrayList<>();

                                while(keys.hasNext()){

                                    String key = keys.next();
                                    int cases = mainObject.getInt(key);

                                    if(cases == 0) continue;

                                    float dayDate = Integer.parseInt(key.split("-")[1]);

                                    if(!previousKeys.contains(dayDate)){
                                        previousKeys.add(dayDate);
                                        countryValues.add(new BarEntry(dayDate,cases));
                                    }

                                }

                                final BarDataSet barDataSet =new BarDataSet(countryValues, "Country cases");
                                barDataSet.setValueTextColor(Color.rgb(207,207,207));
                                barDataSet.setColor(Color.rgb(254,14,44));

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        BarData barData =new BarData();
                                        barData.addDataSet(barDataSet);
                                        barChart.setData(barData);
                                        barChart.setFitBars(true);
                                        barChart.setBorderWidth(10);
                                        barChart.invalidate();
                                        progressAnimated.clearAnimation();
                                        progressDialog.dismiss();

                                    }
                                });

                            }
                        } catch (JSONException e) {
                            failCount++;
                            if(failCount < 2){
                                Log.d("ttt",e.getMessage());
                                fetchDailyProgression(countryName);
                                Thread.currentThread().interrupt();
                            }else{
                                Log.d("ttt",e.getMessage());
                                progressAnimated.clearAnimation();
                                progressDialog.dismiss();
                                Thread.currentThread().interrupt();
                            }
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ttt","error message: "+error.getMessage());
                        progressDialog.dismiss();
                    }
                }){

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("x-authorization", "6179002e-6646-4852-be37-572758a58cbb");
                        params.put("x-rapidapi-host", "covid-19-global-tracker-with-regional-data.p.rapidapi.com");
                        params.put("x-rapidapi-key", "1fb7627ee8mshf96cc54cb26632ap1a9163jsnec5362defe84");
                        return params;

                    }
                };

                AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

            }
        }).start();


    }

    private void fetchMonthlyProgression(final String country){

        final ProgressDialog progressDialog = showProgressDialog();
        final ImageView progressAnimated = progressDialog.findViewById(R.id.progressAnimated);

        final ArrayList<BarEntry> countryValues = new ArrayList<>();
        final int currentMonth = Calendar.getInstance(Locale.getDefault()).get(Calendar.MONTH);

        new Thread(new Runnable() {
            @Override
            public void run() {


                String url = "https://covid-19-v1.p.rapidapi.com/v1/";

                if(country.equals("World Wide")){
                    url = url.concat("allhistorical");
                }else{
                    url = url.concat("historical?country="+country);
                }


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.has("status") && response.getString("status").equals("success")){


                                JSONObject mainObject;

                                if(country.equals("World Wide")){
                                    mainObject =  response.getJSONObject("data").getJSONObject("cases");
                                }else{
                                    mainObject =  response.getJSONObject("data").getJSONObject("timeline").getJSONObject("cases");
                                }


                                Iterator<String> keys =  mainObject.keys();
                                previousKeys = new ArrayList<>();

                                while(keys.hasNext()){

                                    String key = keys.next();
                                    int cases = mainObject.getInt(key);

                                    if(cases == 0) continue;

                                    int month = Integer.parseInt(key.split("/")[0]);
                                    if(month == currentMonth){
                                        float day = Integer.parseInt(key.split("/")[1]);
                                        if(!previousKeys.contains(day)){
                                            previousKeys.add(day);
                                            countryValues.add(new BarEntry(day,cases));
                                        }
                                    }
                                }

                                final BarDataSet barDataSet =new BarDataSet(countryValues, "Country cases");
                                barDataSet.setValueTextColor(Color.rgb(207,207,207));
                                barDataSet.setColor(Color.rgb(254,14,44));

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        BarData barData =new BarData();
                                        barData.addDataSet(barDataSet);
                                        barChart.setData(barData);
                                        barChart.setFitBars(true);
                                        barChart.setBorderWidth(10);
                                        barChart.invalidate();
                                        progressAnimated.clearAnimation();
                                        progressDialog.dismiss();

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            failCount++;
                            if(failCount < 2){
                                fetchMonthlyProgression(countryName);
                                Thread.currentThread().interrupt();
                            }else{
                                Log.d("ttt",e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressAnimated.clearAnimation();

                                        progressDialog.dismiss();

                                    }
                                });
                                Thread.currentThread().interrupt();
                            }
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }){

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("x-rapidapi-host", "covid-19-v1.p.rapidapi.com");
                        params.put("x-rapidapi-key", "1fb7627ee8mshf96cc54cb26632ap1a9163jsnec5362defe84");
                        return params;

                    }
                };

                AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

            }
        }).start();


    }

    private ProgressDialog showProgressDialog(){
        final ProgressDialog dialog = ProgressDialog.show(CountryActivity.this,null,null,true);
        dialog.setContentView(R.layout.progress_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        final ImageView progressImage = dialog.findViewById(R.id.progressAnimated);
        progressImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.loading_znimated));
        return dialog;
    }

    private String getCountryCode(String countryName) {

        String[] isoCountryCodes = Locale.getISOCountries();
        Locale locale;
        String name;

        for (String code : isoCountryCodes) {
            locale = new Locale("en", code);
            name = locale.getDisplayCountry(locale);

            if(name.equals(countryName)){
                return code;
            }
        }

        return null;
    }
//    public void fetchFromApi(final String country, final int type){
//        final ProgressDialog dialog = ProgressDialog.show(CountryActivity.this,null,null,true);
//
//        dialog.setContentView(R.layout.progress_layout);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
//        final ImageView progressImage = dialog.findViewById(R.id.progressAnimated);
//        progressImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.loading_znimated));
//        final ArrayList<BarEntry> countryValues = new ArrayList<>();
//        String url;
//        switch(type){
//            case 1:
//                 url = "https://coronavirus-map.p.rapidapi.com/v1/spots/year?region="+country;
//                break;
//            case 2:
//                 url = "https://coronavirus-map.p.rapidapi.com/v1/spots/day?region="+country;
//                break;
//            case 3:
//                 url = "https://coronavirus-map.p.rapidapi.com/v1/spots/week?region="+country;
//                break;
//            case 4:
//                url = "https://coronavirus-map.p.rapidapi.com/v1/spots/month?region="+country;
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + type);
//        }
//
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONObject mainObject =  response.getJSONObject("data");
//                    Iterator<String> keys =  mainObject.keys();
//                    previousKeys = new ArrayList<>();
//                    int month =0;
//                        while(keys.hasNext()){
//
//                            String key = keys.next();
//                            int cases = mainObject.getJSONObject(key).getInt("total_cases");
//                            if(cases == 0) continue;
//
//                            if(type == 1){
//
//                                int key1 = Integer.parseInt(key.split("-")[1]);
//
//                                if(!previousKeys.contains(key1)){
//                                    previousKeys.add(key1);
//                                    countryValues.add(new BarEntry(key1,cases));
//                                }
//
//                            }else if(type  == 2){
//                                countryValues.add(new BarEntry(Integer.parseInt(key.split(" ")[1].split(":")[0]),cases));
//                            }else if(type == 3){
//
//                                if(month==0){
//                                    month = Integer.parseInt(key.split("-")[1]);
//                                }
//
//                                if(month == Integer.parseInt(key.split("-")[1])){
//                                    countryValues.add(new BarEntry(Integer.parseInt(key.split("-")[2]),cases));
//                                }
//                            }else{
//                                if(thisMonth==0){
//                                    thisMonth = Integer.parseInt(key.split("-")[1]);
//                                }
//                                if(Integer.parseInt(key.split("-")[1]) == thisMonth){
//                                    countryValues.add(new BarEntry(Integer.parseInt(key.split("-")[2]),cases));
//                                }
//                            }
//                        }
//            if(!keys.hasNext()){
//                    BarDataSet barDataSet =new BarDataSet(countryValues, "Country cases");
//                    barDataSet.setValueTextColor(Color.rgb(207,207,207));
//                    barDataSet.setColor(Color.rgb(254,14,44));
//                    BarData barData =new BarData();
//                    barData.addDataSet(barDataSet);
//                    barChart.setData(barData);
//                    barChart.setFitBars(true);
//                    barChart.setBorderWidth(10);
//                    barChart.invalidate();
//
//                    progressImage.clearAnimation();
//
//                    dialog.dismiss();
//}
//                } catch (JSONException e) {
//                    failCount++;
//                    if(failCount < 2){
//
//                        fetchFromApi(countryName,type);
//                        Thread.currentThread().interrupt();
//                    }else{
//                    Log.d("ttt",e.getMessage());
//                        progressImage.clearAnimation();
//                        dialog.dismiss();
//                        Thread.currentThread().interrupt();
//                    }
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
//            }
//        }){
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> params = new HashMap<>();
//                params.put("x-rapidapi-host", "coronavirus-map.p.rapidapi.com");
//                params.put("x-rapidapi-key", "1fb7627ee8mshf96cc54cb26632ap1a9163jsnec5362defe84");
//                return params;
//            }
//        };
//        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
//    }

//    public void fetchWorldStats(final int type){
//
//        final ProgressDialog dialog = ProgressDialog.show(CountryActivity.this,null,null,true);
//        dialog.setContentView(R.layout.progress_layout);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
//        final ImageView progressImage = dialog.findViewById(R.id.progressAnimated);
//        progressImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.loading_znimated));
//        String url = "https://coronavirus-map.p.rapidapi.com/v1/spots/summary";
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    if(response !=null) {
//                        JSONObject mainObject =  response.getJSONObject("data");
//                        final ArrayList<BarEntry> countryValues = new ArrayList<>();
//                        Iterator<String> keys =  mainObject.keys();
//                        previousKeys = new ArrayList<>();
//                        if(type == 1){
//                            while(keys.hasNext()){
//                                if(previousKeys.size() == 12) break;
//                                    String key = keys.next();
//                                    int cases = mainObject.getJSONObject(key).getInt("total_cases");
//                                    int key1 = Integer.parseInt(key.split("-")[1]);
//                                    if(!previousKeys.contains(key1)){
//                                        previousKeys.add(key1);
//                                        countryValues.add(new BarEntry(key1,cases));
//                                    }
//                            }
//                        }else if(type == 2){
//                                String key = keys.next();
//                                int cases = mainObject.getJSONObject(key).getInt("total_cases");
//                                int key1 = Integer.parseInt(key.split("-")[2]);
//                                countryValues.add(new BarEntry(key1,cases));
//                        }else if(type == 3){
//                            while(keys.hasNext()){
//                                if(previousKeys.size() == 7) break;
//                                String key = keys.next();
//                                int cases = mainObject.getJSONObject(key).getInt("total_cases");
//                                int key1 = Integer.parseInt(key.split("-")[2]);
//                                if(!previousKeys.contains(key1)){
//                                    previousKeys.add(key1);
//                                    countryValues.add(new BarEntry(key1,cases));
//                                }
//                            }
//                        }else if(type == 4){
//                            DateFormat dateFormat = new SimpleDateFormat("MM",Locale.getDefault());
//                            Date date = new Date();
//                           int currentMonth= Integer.parseInt(dateFormat.format(date));
//
//                            while(keys.hasNext()){
//                                //if(previousKeys.size() == 30) break;
//                                String key = keys.next();
//                                int cases = mainObject.getJSONObject(key).getInt("total_cases");
//                                int key1 = Integer.parseInt(key.split("-")[1]);
//                                int key2 = Integer.parseInt(key.split("-")[2]);
//                                if(key1 == currentMonth){
//                                    countryValues.add(new BarEntry(key2,cases));
//                                }
//                            }
//                        }
//
//
//                            BarDataSet barDataSet =new BarDataSet(countryValues, "Country cases");
//                            barDataSet.setValueTextColor(Color.rgb(207,207,207));
//                            barDataSet.setColor(Color.rgb(254,14,44));
//
//                            BarData barData =new BarData();
//                            barData.addDataSet(barDataSet);
//                            barChart.setData(barData);
//                            barChart.notifyDataSetChanged();
//                            barChart.setFitBars(true);
//                            barChart.setBorderWidth(10);
//                            barChart.invalidate();
//                            progressImage.clearAnimation();
//                            dialog.dismiss();
//                    }
//                } catch (JSONException e) {
//                    progressImage.clearAnimation();
//                    dialog.dismiss();
//                    Thread.currentThread().interrupt();
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressImage.clearAnimation();
//                dialog.dismiss();
//            }
//        }){
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> params = new HashMap<>();
//                params.put("x-rapidapi-host", "coronavirus-map.p.rapidapi.com");
//                params.put("x-rapidapi-key", "1fb7627ee8mshf96cc54cb26632ap1a9163jsnec5362defe84");
//                return params;
//            }
//        };
//        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 3){
            setResult(3,new Intent(getApplicationContext(),MapsActivity.class).putExtra("country",countryName));
            finish();
        }
    }
}