package com.ahmedfeko.corona_virus_tracker_feko;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LastThreeMonthsActivity extends AppCompatActivity {

    int failCount =0;
    String countryName;
    List<Integer> dates;
    TextView countryTv;
    Toolbar toolbar;
    List<Integer> previousKeys;
    TextView thisMonthCasesTv;
    TextView thisMonthPercentCasesTv;
    TextView lastMonthCasesTv;
    TextView lastMonthPercentCasesTv;
    TextView SecondPreviousMonthCasesTv;
    TextView SecondPreviousMonthPercentCasesTv;
    TextView[] textViews;
    List<Double> allCases = new ArrayList<>();
    TextView firstMonth;
    TextView secondMonth;
    TextView thirdMonth;
    Geocoder geocoder;
    double percent1;
    double percent2;
    double percent3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_three_months);

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//
//                final InterstitialAd mInterstitialAd = new InterstitialAd(getApplicationContext());
//                mInterstitialAd.setAdUnitId("ca-app-pub-6990486336142688/1020458813");
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                mInterstitialAd.setAdListener(new AdListener(){
//                    @Override
//                    public void onAdLoaded() {
//                        mInterstitialAd.show();
//                    }
//                    @Override
//                    public void onAdFailedToLoad(int i) {
//                    }
//                });
//            }
//        });
        dates = new ArrayList<>();

        countryTv = findViewById(R.id.countryTv);
        toolbar=  findViewById(R.id.toolbar);
        thisMonthCasesTv =  findViewById(R.id.thisMonthCasesTv);
        thisMonthPercentCasesTv =  findViewById(R.id.thisMonthPercentCasesTv);
        lastMonthCasesTv =  findViewById(R.id.lastMonthCasesTv);
        lastMonthPercentCasesTv =  findViewById(R.id.lastMonthPercentCasesTv);
        SecondPreviousMonthCasesTv =  findViewById(R.id.SecondPreviousMonthCasesTv);
        SecondPreviousMonthPercentCasesTv =  findViewById(R.id.SecondPreviousMonthPercentCasesTv);

        firstMonth =  findViewById(R.id.firstMonth);
        secondMonth =  findViewById(R.id.secondMonth);
        thirdMonth =  findViewById(R.id.thirdMonth);
        textViews = new TextView[]{thisMonthCasesTv,lastMonthCasesTv,SecondPreviousMonthCasesTv};

        geocoder = new Geocoder(LastThreeMonthsActivity.this,Locale.getDefault());
        final ImageView menuIv3= findViewById(R.id.menuIv3);
        menuIv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(3,new Intent(getApplicationContext(),MapsActivity.class).putExtra("country",countryName));
                finish();
            }
        });

        countryName = getIntent().getStringExtra("countryName");
        ImageView globeImage = findViewById(R.id.imageView);
        globeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuIv3.performClick();
            }
        });
        ImageView menuImage= findViewById(R.id.menuIv);
        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(LastThreeMonthsActivity.this, toolbar);
                popup.getMenuInflater().inflate(R.menu.options_menu_last_three, popup.getMenu());
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
                            case R.id.growthRateItem:
                                if(countryName!=null){
                                    startActivityForResult(new Intent(LastThreeMonthsActivity.this,CountryActivity.class).putExtra("countryName",countryName),0);
                                    finish();
                                }
                                return true;
                            case R.id.totalAmountItem:
                                setResult(5);
                                finish();
                                return true;
                            case R.id.lastThreeItem:
                                if(!countryName.equals("World Wide")){
                                    startActivityForResult(new Intent(LastThreeMonthsActivity.this,LastThreeMonthsActivity.class).putExtra("countryName",countryName),0);
                                    finish();
                                }else{
                                    startActivityForResult(new Intent(LastThreeMonthsActivity.this,LastThreeMonthsActivity.class).putExtra("countryName","World Wide"),0);
                                    finish();
                                }
                                return true;
                            case R.id.identifyItem:
                                startActivity(new Intent(LastThreeMonthsActivity.this,InfoActivity.class).putExtra("countryName",countryName));
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

        countryTv.setText(countryName);
        fetchFromApi(countryName);

        SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy",Locale.getDefault());
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.MONTH, 0);
        firstMonth.setText(format.format(cal2.getTime()));
        cal2.add(Calendar.MONTH, -1);
        secondMonth.setText(format.format(cal2.getTime()));
        cal2.add(Calendar.MONTH, -1);
        thirdMonth.setText(format.format(cal2.getTime()));

    }
    public void fetchFromApi(final String country){
        final ProgressDialog dialog = ProgressDialog.show(LastThreeMonthsActivity.this,null,null,true);

        dialog.setContentView(R.layout.progress_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        final ImageView progressImage = dialog.findViewById(R.id.progressAnimated);
        progressImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.loading_znimated));
     //   final ArrayList<BarEntry> countryValues = new ArrayList<>();
        String url = "";

        Log.d("ttt",url);
        String[] countries = country.split(" ");
        if(country.equals("World Wide")){
             url = "https://coronavirus-map.p.rapidapi.com/v1/spots/summary";
        }else{
            if(countries.length > 1){
                if(countries.length == 2){
                    url = "https://coronavirus-map.p.rapidapi.com/v1/spots/year?region="+countries[0]+"%20"+countries[1];
                    Log.d("ttt",url);
                }else if(countries.length == 3){
                    url = "https://coronavirus-map.p.rapidapi.com/v1/spots/year?region="+countries[0]+"%20"+countries[1]+"%20"+countries[2];
                }else if(countries.length == 4){
                    url = "https://coronavirus-map.p.rapidapi.com/v1/spots/year?region="+countries[0]+"%20"+countries[1]+"%20"+countries[2]+"%20"+countries[3];
                }else if(countries.length == 5){
                    url = "https://coronavirus-map.p.rapidapi.com/v1/spots/year?region="+countries[0]+"%20"+countries[1]+"%20"+countries[2]+"%20"+countries[3]+"%20"+countries[4];
                }
            }else{
                url = "https://coronavirus-map.p.rapidapi.com/v1/spots/year?region="+country;
                Log.d("ttt",url);
            }
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject mainObject =  response.getJSONObject("data");
                    Iterator<String> keys =  mainObject.keys();
                    previousKeys = new ArrayList<>();
                    int position =0;
                    while(keys.hasNext()){

                        String key = keys.next();
                        JSONObject object =mainObject.getJSONObject(key);

                        int cases = object.getInt("total_cases") - object.getInt("deaths") - object.getInt("recovered");
                        if(cases == 0) continue;


                        int key1 = Integer.parseInt(key.split("-")[1]);

                        if(!previousKeys.contains(key1)){
                            allCases.add((double) cases);
                            Log.d("ttt","cases: "+allCases.size()+" "+cases);
                        if (previousKeys.size() < 3) {
                                previousKeys.add(key1);
                                textViews[position].setText(String.format(Locale.getDefault(),"%,d", cases));
                                position++;
                            }
                        }
                    }


                    if(allCases.size() > 1) {

                        double diff1 = allCases.get(0) - allCases.get(1);
                        double divided1 = (diff1 / allCases.get(1));
                         percent1 = Math.round((divided1 * 100) * 100) / 100;
                        thisMonthPercentCasesTv.setText(percent1 + "%");
                        thisMonthPercentCasesTv.setTextColor(Color.rgb(253,57,81));
                        thisMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_red, 0, 0, 0);
                    }
                    if(allCases.size() > 2) {
                        double diff2 = allCases.get(1) - allCases.get(2);
                        double divided2 = (diff2 / allCases.get(2));
                         percent2 = Math.round((divided2 * 100) * 100) / 100;
                        lastMonthPercentCasesTv.setText(percent2 + "%");
                        if(allCases.get(0) > allCases.get(1)){
                            lastMonthPercentCasesTv.setTextColor(Color.rgb(0,197,113));
                            lastMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_green, 0, 0, 0);

                            thisMonthPercentCasesTv.setTextColor(Color.rgb(253,57,81));
                            thisMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_red, 0, 0, 0);
                        }else{
                            thisMonthPercentCasesTv.setTextColor(Color.rgb(0,197,113));
                            thisMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_green, 0, 0, 0);

                            lastMonthPercentCasesTv.setTextColor(Color.rgb(253,57,81));
                            lastMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_red, 0, 0, 0);
                        }

                        if(allCases.get(1) > allCases.get(2)){
                            SecondPreviousMonthPercentCasesTv.setTextColor(Color.rgb(0,197,113));
                            SecondPreviousMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_green, 0, 0, 0);

                            lastMonthPercentCasesTv.setTextColor(Color.rgb(253,57,81));
                            lastMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_red, 0, 0, 0);
                        }else{
                            lastMonthPercentCasesTv.setTextColor(Color.rgb(0,197,113));
                            lastMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_green, 0, 0, 0);

                            SecondPreviousMonthPercentCasesTv.setTextColor(Color.rgb(253,57,81));
                            SecondPreviousMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_red, 0, 0, 0);
                        }
                    }else{
                        lastMonthPercentCasesTv.setText("100%");
                    }
                    if(allCases.size()>3){
                        double diff3 = allCases.get(2)-allCases.get(3);
                        double divided3 =(diff3/allCases.get(3));
                         percent3 = Math.round((divided3*100)*100)/100;
                        SecondPreviousMonthPercentCasesTv.setText(percent3+"%");

                        if(allCases.get(2) > allCases.get(3)){
                            SecondPreviousMonthPercentCasesTv.setTextColor(Color.rgb(0,197,113));
                            SecondPreviousMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_green, 0, 0, 0);

                            lastMonthPercentCasesTv.setTextColor(Color.rgb(253,57,81));
                            lastMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_red, 0, 0, 0);
                        }else{
                            lastMonthPercentCasesTv.setTextColor(Color.rgb(0,197,113));
                            lastMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_green, 0, 0, 0);

                            SecondPreviousMonthPercentCasesTv.setTextColor(Color.rgb(253,57,81));
                            SecondPreviousMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_red, 0, 0, 0);
                       }

                        if(allCases.get(3)>0){
                            SecondPreviousMonthPercentCasesTv.setTextColor(Color.rgb(253,57,81));
                            SecondPreviousMonthPercentCasesTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_red, 0, 0, 0);
                        }
                    }else{
                        SecondPreviousMonthPercentCasesTv.setText("100%");
                    }

                  dialog.dismiss();
                } catch (JSONException e) {
                    failCount++;
                    if(failCount < 2){
                        try {
                            Address address = geocoder.getFromLocationName(countryName, 1).get(0);
                            fetchFromApi(address.getCountryCode());
                        } catch (IOException e2) {
                            Log.d("ttt",e2.getMessage());
                            progressImage.clearAnimation();
                            dialog.dismiss();
                            Thread.currentThread().interrupt();
                            e2.printStackTrace();
                        }
                    }else{
                        Log.d("ttt",e.getMessage());
                        progressImage.clearAnimation();
                        dialog.dismiss();
                        Thread.currentThread().interrupt();
                    }
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                failCount++;
                if(failCount < 2) {
                    try {
                        Address address = geocoder.getFromLocationName(countryName, 1).get(0);
                        fetchFromApi(address.getCountryCode());
                    } catch (IOException e) {
                        Log.d("ttt",e.getMessage());
                        progressImage.clearAnimation();
                        dialog.dismiss();
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }else{
                    progressImage.clearAnimation();
                    dialog.dismiss();
                    Thread.currentThread().interrupt();
                }
                progressImage.clearAnimation();
                dialog.dismiss();

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("x-rapidapi-host", "coronavirus-map.p.rapidapi.com");
                params.put("x-rapidapi-key", "1fb7627ee8mshf96cc54cb26632ap1a9163jsnec5362defe84");
                return params;
            }
        };
        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == 3){
                setResult(3,new Intent(getApplicationContext(),MapsActivity.class).putExtra("country",countryName));
                finish();
            }
    }
}
