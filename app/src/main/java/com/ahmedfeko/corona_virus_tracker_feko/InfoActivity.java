package com.ahmedfeko.corona_virus_tracker_feko;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


//            MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
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


//        AdView mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


        TextView phoneNumberTv = findViewById(R.id.phoneNumberTv);
        phoneNumberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("تم نسخ رقم الهاتف!","+966 55 888 5719");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(InfoActivity.this, "Phone number was copied to the clipboard!", Toast.LENGTH_LONG).show();
            }
        });
        final String countryName = getIntent().getStringExtra("countryName");
        ImageView globeImage = findViewById(R.id.imageView2);
        globeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView globeImage2 = findViewById(R.id.menuIv3);
        globeImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final Toolbar toolbar=  findViewById(R.id.toolbar);

        ImageView menuId = findViewById(R.id.menuId);
        menuId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(InfoActivity.this, toolbar);
                popup.getMenuInflater().inflate(R.menu.options_menu_info, popup.getMenu());
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
                                    startActivityForResult(new Intent(InfoActivity.this,CountryActivity.class).putExtra("countryName",countryName),0);
                                    finish();
                                }
                                return true;
                            case R.id.totalAmountItem:
                                setResult(5);
                                finish();
                                return true;
                            case R.id.lastThreeItem:
                                if(!countryName.equals("World Wide")){
                                    startActivityForResult(new Intent(InfoActivity.this,LastThreeMonthsActivity.class).putExtra("countryName",countryName),0);
                                    finish();
                                }else{
                                    startActivityForResult(new Intent(InfoActivity.this,LastThreeMonthsActivity.class).putExtra("countryName","World Wide"),0);
                                    finish();
                                }
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
}
