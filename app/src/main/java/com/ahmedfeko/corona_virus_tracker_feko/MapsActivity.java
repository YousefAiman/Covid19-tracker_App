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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{
    List<Marker> markers = new ArrayList<>();
    private GoogleMap mMap;
    SearchView countrySv;
    Geocoder geocoder;
    SupportMapFragment mapFragment;
    int failCount;
    TextView affectedTv;
    TextView deathsTv;
    TextView totalTv;
   // TextView dischargedTv;
    TextView timeInfoTv;

    Marker marker;
    ArrayList<WeightedLatLng> weightedLatLngs;
    ProgressDialog progressDialog;
    String countryName;
    ArrayList<CountryCase> countriesAutoList;
    ImageView menuIv;
    int optionSelected =-1;
    MenuItem menuItem;
    Toolbar toolbar;
    FrameLayout frameLayout;
    ImageView menuIv3;
    LinearLayout bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    TextView countryInfoTv;
    int cases1 = 0;
    int cases2 = 0;
    int difference = 0;
    FragmentManager fragmentManager;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        geocoder = new Geocoder(MapsActivity.this,Locale.getDefault());
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        MobileAds.initialize(MapsActivity.this);
         bottomSheet = findViewById(R.id.infoBottomSheet);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        int height = displaymetrics.heightPixels;
//        CoordinatorLayout.LayoutParams  params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.height = height / 3;
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);

        bottomSheet.getLayoutParams().height = height/3;
        bottomSheet.requestLayout();
        bottomSheetBehavior.onLayoutChild(coordinatorLayout, bottomSheet, ViewCompat.LAYOUT_DIRECTION_LTR);
         bottomSheetBehavior.setHideable(false);
         bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        affectedTv =  bottomSheet.findViewById(R.id.affectedTv);
        deathsTv =  bottomSheet.findViewById(R.id.deathsTv);
        totalTv =  bottomSheet.findViewById(R.id.totalTv);
         mAdView = bottomSheet.findViewById(R.id.adView);


      //  bottomSheet.setLayoutParams(params);

        countryInfoTv =  bottomSheet.findViewById(R.id.countryInfoTv);
        timeInfoTv =  bottomSheet.findViewById(R.id.timeInfoTv);

        countryInfoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        countrySv = findViewById(R.id.countrySv);
        menuIv =  findViewById(R.id.menuIv);
        toolbar=  findViewById(R.id.toolbar);
         frameLayout=findViewById(R.id.frameLayout);

    MobileAds.initialize(this);


        SearchView.SearchAutoComplete searchText = countrySv.findViewById(androidx.appcompat.R.id.search_src_text);

        searchText.setTextColor(getResources().getColor(R.color.textWhite));
        searchText.setHintTextColor(getResources().getColor(R.color.textGrey));
        searchText.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
         fragmentManager = getSupportFragmentManager();
         menuIv3= findViewById(R.id.menuIv3);


        menuIv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        countrySv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuIv3.setVisibility(View.VISIBLE);
                countrySv.onActionViewExpanded();
                    frameLayout.setVisibility(View.VISIBLE);
                    bottomSheet.setVisibility(View.INVISIBLE);
                    //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    FragmentTransaction searchTransaction = fragmentManager.beginTransaction();
                    searchTransaction.replace(R.id.frameLayout,new searchFragment()).commit();
            }
        });

        menuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MapsActivity.this, toolbar);
                popup.getMenuInflater().inflate(R.menu.options_menu, popup.getMenu());
                Menu menu = popup.getMenu();
                for(int i=0;i<menu.size();i++){
                    MenuItem item =menu.getItem(i);
                    SpannableString s = new SpannableString(item.getTitle());
                    s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.yellow)), 0, s.length(), 0);
                    item.setTitle(s);
                }
                if (optionSelected != -1) {
                    switch (optionSelected) {
                        case 1:
                            menuItem = menu.findItem(R.id.selectCountryItem);
                            break;
                        case 2:
                            menuItem = menu.findItem(R.id.growthRateItem);
                          break;
                        case 3:
                            menuItem = menu.findItem(R.id.totalAmountItem);
                            break;
                        case 4:
                            menuItem = menu.findItem(R.id.lastThreeItem);
                            break;
                    }
                    for(int i=0;i<menu.size();i++){
                        MenuItem item =menu.getItem(i);
                        if(item == menuItem){
                            SpannableString s = new SpannableString(menuItem.getTitle());
                            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.pink)), 0, s.length(), 0);
                            menuItem.setTitle(s);
                            continue;
                        }
                        SpannableString s = new SpannableString(item.getTitle());
                        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.yellow)), 0, s.length(), 0);
                        item.setTitle(s);
                    }

                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.selectCountryItem:
                                if(frameLayout.getVisibility() == View.INVISIBLE){
                                    bottomSheet.setVisibility(View.INVISIBLE);
                                    frameLayout.setVisibility(View.VISIBLE);
                                    FragmentTransaction searchTransaction = fragmentManager.beginTransaction();
                                    searchTransaction.replace(R.id.frameLayout,new searchFragment()).commit();
                                }
                                optionSelected = 1;
                                return true;
                            case R.id.growthRateItem:
                                if(countryName!=null){
                                    startActivityForResult(new Intent(MapsActivity.this,CountryActivity.class).putExtra("countryName",countryName),0);
                                }
                                optionSelected = 2;
                                return true;
                            case R.id.totalAmountItem:
//                                if(countryName!=null){
//                                    startActivity(new Intent(MapsActivity.this,CountryActivity.class).putExtra("countryName",countryName));
//                                }
                                fetchWorldStats();
                                optionSelected = 3;
                                return true;
                            case R.id.lastThreeItem:
                                if(!countryName.equals("World Wide")){
                                    startActivityForResult(new Intent(MapsActivity.this,LastThreeMonthsActivity.class).putExtra("countryName",countryName),0);
                                }else{
                                    startActivityForResult(new Intent(MapsActivity.this,LastThreeMonthsActivity.class).putExtra("countryName","World Wide"),0);

                                }
                                optionSelected = 4;
                                return true;
                            case R.id.identifyItem:
                                startActivity(new Intent(getApplicationContext(),InfoActivity.class));
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popup.show();
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.custom_map));


        View zoomOutView = findViewById(R.id.zoomOutView);
        TextView zoomInTv = findViewById(R.id.zoomInTv);


        zoomOutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom+1), 200, null);
            }
        });

        zoomInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom-1), 200, null);
            }
        });
        getAllCountriesCases();

        countrySv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
              return false;
           }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
               mapMarker(latLng);
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
              startActivityForResult(new Intent(MapsActivity.this,LastThreeMonthsActivity.class).putExtra("countryName",countryName),0);
            }
        });


    }

    public void fetchFromApi(final String country, final String ISO2countryCode ,final String ISO3countryCode){

        String url="";
        String[] countries = country.split(" ");
            if(countries.length > 1){
                if(countries.length == 2){
                    url = "https://covid-193.p.rapidapi.com/statistics?country="+countries[0]+"-"+countries[1];
                }else if(countries.length == 3){
                    url = "https://covid-193.p.rapidapi.com/statistics?country="+countries[0]+"-"+countries[1]+"-"+countries[2];
                }else if(countries.length == 4){
                    url = "https://covid-193.p.rapidapi.com/statistics?country="+countries[0]+"-"+countries[1]+"-"+countries[2]+"-"+countries[3];
                }else if(countries.length == 5){
                    url = "https://covid-193.p.rapidapi.com/statistics?country="+countries[0]+"-"+countries[1]+"-"+countries[2]+"-"+countries[3]+"-"+countries[4];
                }
            }else{
                url = "https://covid-193.p.rapidapi.com/statistics?country="+country;
            }

            Log.d("ttt",url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response !=null) {
                        JSONArray errorArray = response.getJSONArray("errors");
                        if (errorArray.length() == 0) {

                            JSONArray responseArray = response.getJSONArray("response");

                            JSONObject countryObject = responseArray.getJSONObject(0);

                           // countryName = countryObject.getString("country");

                            JSONObject countryCases = countryObject.getJSONObject("cases");

                            int criticalCases = countryCases.getInt("critical");

                            int recoveredCases = countryCases.getInt("recovered");
                            int newCases = 0;
                           if(!countryCases.isNull("new")){
                               newCases = countryCases.getInt("new");
                           }
                           if(newCases == 0 ){
                               fetchPreviousDayCases(countryName);
                           }else{
                               int activeCases = countryCases.getInt("active");
                               int totalCases = countryCases.getInt("total");
                               marker.setTitle(countryName+"_"+String.format(Locale.getDefault(),"%,d", activeCases));
                               marker.showInfoWindow();
                               JSONObject countryDeaths = countryObject.getJSONObject("deaths");
                               int totalDeaths = countryDeaths.getInt("total");
                               String time =countryObject.getString("time");
                               if(time.contains("T")){
                                   time =  time.replace('T',' ');
                                   time = time.substring(0, time.indexOf("+"));
                               }
                               showBottomInfoSheet(newCases,totalDeaths,totalCases,time);
                               progressDialog.dismiss();
                           }
                        }
                    }
                } catch (JSONException e) {
                    Log.d("ttt",e.getMessage());
                    Thread.currentThread().interrupt();
                    failCount++;
                    if(failCount == 1) {
                        Log.d("ttt","RETRYING 1");
                        fetchFromApi(ISO2countryCode,ISO2countryCode,ISO2countryCode);
                    }else if(failCount == 2) {
                        Log.d("ttt","RETRYING 2");
                        fetchFromApi(ISO3countryCode,ISO3countryCode,ISO3countryCode);
                    }else if(failCount == 3){
                        Log.d("ttt","RETRYING 3");
                        fetchCountryNameFromCode(ISO2countryCode,ISO3countryCode);
                    }
                        e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
                Log.d("ttt",error.getMessage());
            failCount++;
            if(failCount == 1) {
                Log.d("ttt","RETRYING 1");
                fetchFromApi(ISO2countryCode,ISO2countryCode,ISO2countryCode);
            }else if(failCount == 2) {
                Log.d("ttt","RETRYING 2");
                fetchFromApi(ISO3countryCode,ISO3countryCode,ISO3countryCode);
            }else if(failCount == 3){
                Log.d("ttt","RETRYING 3");
                fetchCountryNameFromCode(ISO2countryCode,ISO3countryCode);
            }
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

    public void fetchCountryNameFromCode(final String countryCode, final String ISO3countryCode){
        final String url = "https://coronavirus-map.p.rapidapi.com/v1/spots/day?region="+countryCode;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response !=null) {

                        JSONObject mainObject =  response.getJSONObject("data");
                        Iterator<String> keys =  mainObject.keys();
                        String key = keys.next();
                        JSONObject dayData =  mainObject.getJSONObject(key);

                        int confirmedCases = dayData.getInt("total_cases");
                        final int recoveredCases = dayData.getInt("recovered");
                        final int countryDeaths = dayData.getInt("deaths");
                        final int criticalCases = dayData.getInt("critical");

                        marker.setTitle(countryName+"_"+String.format(Locale.getDefault(),"%,d", confirmedCases));
                        marker.showInfoWindow();

                        Date date = new Date();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String currentDay = df.format(date);
                        cal.add(Calendar.DAY_OF_MONTH,-1);
                        String previousDay = df.format(cal.getTime());
                        String currentDayUrl = "https://coronavirus-map.p.rapidapi.com/v1/spots/day?date="+currentDay+"&region="+countryCode;
                        final String previousDayUrl = "https://coronavirus-map.p.rapidapi.com/v1/spots/day?date="+previousDay+"&region="+countryCode;
//                      fetchCurrentDayCases(currentDayUrl,previousDayUrl);
//                      fetchPreviousDayCases(previousDayUrl);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,currentDayUrl, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response !=null) {
                                        JSONObject mainObject =  response.getJSONObject("data");
                                        final String key = mainObject.keys().next();
                                        cases1 = mainObject.getJSONObject(key).getInt("total_cases");
                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,previousDayUrl, null, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    if(response !=null) {
                                                        JSONObject mainObject =  response.getJSONObject("data");
                                                        cases2 = mainObject.getJSONObject(mainObject.keys().next()).getInt("total_cases");
                                                        difference = cases1 - cases2;
                                                        showBottomInfoSheet(difference,countryDeaths,cases1,key);
                                                    }
                                                } catch (JSONException e) {
                                                    Thread.currentThread().interrupt();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Thread.currentThread().interrupt();
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
                                } catch (JSONException e) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Thread.currentThread().interrupt();
                                progressDialog.dismiss();
                                if(failCount == 3){
                                    failCount++;
                                    fetchCountryNameFromCode(countryCode,ISO3countryCode);
                                }else{
                                    progressDialog.dismiss();
                                    fetchWorldStats();
                                }
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
                        progressDialog.dismiss();
                    }else{
                        if(failCount == 3){
                            failCount++;
                            fetchCountryNameFromCode(countryCode,ISO3countryCode);
                        }else{
                            progressDialog.dismiss();
                            fetchWorldStats();
                        }

                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Thread.currentThread().interrupt();
                    if(failCount == 3){
                        failCount++;
                        fetchCountryNameFromCode(countryCode,ISO3countryCode);
                    }else{

                        fetchWorldStats();
                    }
                    Toast.makeText(getApplicationContext(), "An Error Occurred! retry again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Thread.currentThread().interrupt();
                fetchWorldStats();
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
    public void onResume() {
        super.onResume();
        mapFragment.onResume();
    }

    public void startCountryActivity(CountryCase countryCase){
       startActivityForResult(new Intent(MapsActivity.this,LastThreeMonthsActivity.class).putExtra("countryName",countryCase.getCountry()),0);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mapFragment.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mapFragment.onStop();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bottomSheet.setVisibility(View.VISIBLE);
        if (requestCode == 0) {
            if (resultCode == 2) {
                if(frameLayout!=null){
                    if(frameLayout.getVisibility() == View.VISIBLE){
                        menuIv3.setVisibility(View.INVISIBLE);
                        frameLayout.setVisibility(View.INVISIBLE);
                        countrySv.clearFocus();
                        countrySv.onActionViewCollapsed();
                    }
                }
            }else if(resultCode == 3){
                if(frameLayout!=null){
                    if(frameLayout.getVisibility() == View.VISIBLE){
                        menuIv3.setVisibility(View.INVISIBLE);
                        frameLayout.setVisibility(View.INVISIBLE);
                        countrySv.clearFocus();
                        countrySv.onActionViewCollapsed();
//
//                        final InterstitialAd mInterstitialAd = new InterstitialAd(getApplicationContext());
//                        mInterstitialAd.setAdUnitId("ca-app-pub-6990486336142688/1020458813");
//                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                        mInterstitialAd.setAdListener(new AdListener(){
//                            @Override
//                            public void onAdLoaded() {
//                                mInterstitialAd.show();
//                            }
//                            @Override
//                            public void onAdFailedToLoad(int i) {
//                            }
//                        });

                        List<Address> addresses;
                        try {
                            addresses = geocoder.getFromLocationName(data.getStringExtra("country"), 1);
                            for (Marker marker : markers) {
                                marker.remove();
                            }
                            for(Address address:addresses) {
                                if (address.getCountryName() != null) {
                                    countryName = address.getCountryName();
                                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                                    mapMarker(latLng);
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }else{
                    super.onBackPressed();
                }
            }else if(resultCode == 4){
                if(frameLayout.getVisibility() == View.INVISIBLE){
                    frameLayout.setVisibility(View.VISIBLE);
                    FragmentTransaction searchTransaction = fragmentManager.beginTransaction();
                    searchTransaction.replace(R.id.frameLayout,new searchFragment()).commit();
                }
            }else if(resultCode == 5){

                fetchWorldStats();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapFragment.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapFragment.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(frameLayout!=null){
            if(frameLayout.getVisibility() == View.VISIBLE){
                menuIv3.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.INVISIBLE);
                bottomSheet.setVisibility(View.VISIBLE);
            }else{
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }

        countrySv.clearFocus();
        countrySv.onActionViewCollapsed();

    }


    public void mapMarker(LatLng latLng){

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        for(Marker marker:markers){
            marker.remove();
        }

        try {
         List<Address> selectedAddresses=geocoder.getFromLocation(latLng.latitude,latLng.longitude,3);
         if(selectedAddresses!=null && !selectedAddresses.isEmpty()){
             for(Address address: selectedAddresses){
                if(address.getCountryName()!=null){
                    mMap.setInfoWindowAdapter(new CustomWindowAdapter(MapsActivity.this));
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

                    JSONObject obj = new JSONObject(loadJSONFromAsset());
                    Locale locale = new Locale("", address.getCountryCode());
                    JSONObject countryObject = obj.getJSONObject(locale.getISO3Country());


                    Log.d("ttt",locale.getISO3Country());
                    JSONObject southWestObject = countryObject.getJSONObject("sw");
                    JSONObject northEastObject = countryObject.getJSONObject("ne");
                    long swLat = southWestObject.getLong("lat");
                    long swLng = southWestObject.getLong("lon");

                    long neLat = northEastObject.getLong("lat");
                    long neLng = northEastObject.getLong("lon");

                    LatLng latLng1 = new LatLng(swLat, swLng);
                    LatLng latLng2 = new LatLng(neLat, neLng);

                    LatLngBounds latLngBounds = new LatLngBounds(latLng1, latLng2);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));

                    countryName = address.getCountryName();
                    markers.add(marker);
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }else if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            }
                            return false;
                        }
                    });
                    failCount = 0;
                    progressDialog = ProgressDialog.show(MapsActivity.this,null,null,true);
                    progressDialog.setContentView(R.layout.progress_layout);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    progressDialog.show();
                    final ImageView progressImage = progressDialog.findViewById(R.id.progressAnimated);
                    progressImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.loading_znimated));
                    if(address.getCountryName().contains("-")){

                        fetchFromApi(address.getCountryName().replace("-"," "),address.getCountryCode(),locale.getISO3Country());
                    }else{
                        fetchFromApi(address.getCountryName(),address.getCountryCode(),locale.getISO3Country());
                    }
                    Log.d("ttt","country code "+address.getCountryCode()+" ISO3 code "+locale.getISO3Country());
                    break;
                }
             }
         }else{
             fetchCodeFromApi(latLng.latitude,latLng.longitude);
         }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            fetchWorldStats();
        }
    }

    public void fetchWorldStats(){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        countryName = "World Wide";
        for(Marker marker:markers){
            marker.remove();
        }
        if(frameLayout.getVisibility() == View.VISIBLE) {
            menuIv3.setVisibility(View.INVISIBLE);
            frameLayout.setVisibility(View.INVISIBLE);
            countrySv.clearFocus();
            countrySv.onActionViewCollapsed();
        }

            progressDialog = ProgressDialog.show(MapsActivity.this,null,null,true);
        progressDialog.setContentView(R.layout.progress_layout);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        final ImageView progressImage = progressDialog.findViewById(R.id.progressAnimated);
        progressImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.loading_znimated));
        String url = "https://coronavirus-map.p.rapidapi.com/v1/summary/latest";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response !=null) {
                        JSONObject mainObject =  response.getJSONObject("data");
                        JSONObject dayData =  mainObject.getJSONObject("summary");

                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(mainObject.getLong("generated_on") * 1000L);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
                        String time = df.format(cal.getTime());
                        showBottomInfoSheet(dayData.getInt("active_cases"),
                                dayData.getInt("deaths"),
                                dayData.getInt("total_cases")
                                ,time);
                        mMap.moveCamera(CameraUpdateFactory
                                .newCameraPosition(new CameraPosition.Builder()
                                        .target(new LatLng(0,0))
                                        .build()));
                        progressImage.clearAnimation();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {

                    progressImage.clearAnimation();
                    progressDialog.dismiss();
                    Thread.currentThread().interrupt();
                    Toast.makeText(getApplicationContext(), "An Error Occurred! retry again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressImage.clearAnimation();
               progressDialog.dismiss();
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

    private void getAllCountriesCases() {
        countriesAutoList = new ArrayList<>();
        final int stroke =Color.parseColor("#FFFF0000");
        final int fill =Color.parseColor("#66FF0000");
        weightedLatLngs= new ArrayList<>();


        final ProgressDialog mapDialog = ProgressDialog.show(MapsActivity.this,null,null,true);
        mapDialog.setContentView(R.layout.progress_layout);
        mapDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mapDialog.show();
       final ImageView progressImage = mapDialog.findViewById(R.id.progressAnimated);
       progressImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.loading_znimated));
        String url = "https://covid19-data.p.rapidapi.com/all";

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if(response !=null) {
                        CircleOptions circle = new CircleOptions().strokeColor(stroke).fillColor(fill).strokeWidth(1);
                            for(int i=0;i<response.length();i++){
                                JSONObject countryObject = response.getJSONObject(i);
                                int cases = countryObject.getInt("confirmed");

                                CountryCase countryCase = new CountryCase(countryObject.getString("country"),cases);
                                countriesAutoList.add(countryCase);
                                mMap.addCircle(circle.center(new LatLng(countryObject.getLong("latitude"),countryObject.getLong("longitude")))
                                        .radius(cases));
                            }
                        progressImage.clearAnimation();
                            mapDialog.dismiss();
                            Intent intent = getIntent();
                            if(intent.hasExtra("lat") && intent.hasExtra("long")){
                                LatLng latLng = new LatLng(intent.getDoubleExtra("lat",0),intent.getDoubleExtra("long",0));
                                mapMarker(latLng);
                            }
                    }
                } catch (JSONException e) {
                    progressImage.clearAnimation();
                    mapDialog.dismiss();
                    Thread.currentThread().interrupt();
                    Toast.makeText(getApplicationContext(), "An Error Occurred! retry again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressImage.clearAnimation();
                mapDialog.dismiss();
                Toast.makeText(getApplicationContext(), "An Error Occurred! retry again", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("x-rapidapi-host", "covid19-data.p.rapidapi.com");
                params.put("x-rapidapi-key", "1fb7627ee8mshf96cc54cb26632ap1a9163jsnec5362defe84");
                return params;
            }
        };
        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
   }

    public void fetchCodeFromApi(final double latitude, final double longitude){

        progressDialog = ProgressDialog.show(MapsActivity.this,null,null,true);
        progressDialog.setContentView(R.layout.progress_layout);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        final ImageView progressImage = progressDialog.findViewById(R.id.progressAnimated);
        progressImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.loading_znimated));
        String url = "https://api.opencagedata.com/geocode/v1/json?key=078648c6ff684a8e851e63cbb1c8f6d8&q="+latitude+"+"+longitude+"&pretty=1&no_annotations=1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null) {
                    try {
                        JSONObject Status = response.getJSONObject("status");
                        if (Status.getString("message").equalsIgnoreCase("ok")) {
                            JSONArray Results = response.getJSONArray("results");
                            JSONObject zero = Results.getJSONObject(0);
                            JSONObject address_components = zero.getJSONObject("components");
                            mMap.setInfoWindowAdapter(new CustomWindowAdapter(MapsActivity.this));

                            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).draggable(false)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                            mMap.moveCamera(CameraUpdateFactory
                                    .newCameraPosition(new CameraPosition.Builder()
                                            .target(new LatLng(latitude, longitude))
                                            .zoom(3)
                                            .build()));
                            markers.add(marker);
                            failCount = 0;
                            if(address_components.getString("country")!=null && !address_components.getString("country").isEmpty()){
                                Log.d("ttt",address_components.getString("country"));
                                fetchFromApi(address_components.getString("country"), address_components.getString("country_code"),address_components.getString("ISO_3166-1_alpha-3"));
                                Log.d("ttt",address_components.getString("country")+" address");
                            }else{
                                progressImage.clearAnimation();
                                progressDialog.dismiss();
                                fetchWorldStats();
                            }
                        } else {
                            progressImage.clearAnimation();
                            progressDialog.dismiss();
                            fetchWorldStats();
                        }
                    } catch (JSONException e) {
                        progressImage.clearAnimation();
                        progressDialog.dismiss();
                        fetchWorldStats();
                        e.printStackTrace();
                    }
                }else{
                    progressImage.clearAnimation();
                    progressDialog.dismiss();
                    fetchWorldStats();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressImage.clearAnimation();
                progressDialog.dismiss();
                fetchWorldStats();
                Toast.makeText(getApplicationContext(), "An Error Occurred! retry again", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void showBottomInfoSheet(int affected,int deaths,int total,String time){

        if(countryName !=null){
            if(!countryName.equals("World Wide")){
                countryInfoTv.setText(countryName);
            }else{
                countryInfoTv.setText("World Wide");
            }
        }else{
            countryInfoTv.setText("World Wide");
        }
            totalTv.setText(String.format(Locale.getDefault(),"%,d", total));
            affectedTv.setText(String.format(Locale.getDefault(),"%,d", affected));
            deathsTv.setText(String.format(Locale.getDefault(),"%,d", deaths));
            //criticalTv.setText(String.format(Locale.getDefault(),"%,d", critical));
           // dischargedTv.setText(String.format(Locale.getDefault(),"%,d", discharged));
            timeInfoTv.setText(time);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


    public void fetchPreviousDayCases(String country){

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        cal.add(Calendar.DAY_OF_MONTH,-1);
        String previousDay = df.format(cal.getTime());
        String url = "https://covid-193.p.rapidapi.com/history?day="+previousDay+"&country="+country;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response !=null) {
                        JSONArray errorArray = response.getJSONArray("errors");
                        if (errorArray.length() == 0) {

                            JSONObject countryObject = response.getJSONArray("response").getJSONObject(0);
                            JSONObject countryCases = countryObject.getJSONObject("cases");
                            int newCases = 0;
                            if(!countryCases.isNull("new")){
                                newCases = countryCases.getInt("new");
                            }
                            int activeCases = countryCases.getInt("active");
                            marker.setTitle(countryName+"_"+String.format(Locale.getDefault(),"%,d", activeCases));

                            marker.showInfoWindow();

                            JSONObject countryDeaths = countryObject.getJSONObject("deaths");


                            String time =countryObject.getString("time");
                            if(time.contains("T")){
                                time =  time.replace('T',' ');
                                time = time.substring(0, time.indexOf("+"));
                            }
                            showBottomInfoSheet(newCases,countryDeaths.getInt("total"),countryCases.getInt("total"),time);
                            progressDialog.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    Log.d("ttt", Objects.requireNonNull(e.getMessage()));
                    Thread.currentThread().interrupt();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Thread.currentThread().interrupt();
                Log.d("ttt", Objects.requireNonNull(error.getMessage()));
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

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("countries_bounds.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
