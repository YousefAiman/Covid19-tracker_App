package com.ahmedfeko.corona_virus_tracker_feko;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {
    View window;
    Context context;
    DisplayMetrics displaymetrics;
    int height;
    int width;
    public CustomWindowAdapter(Context context) {
        this.context = context;
        displaymetrics =context.getResources().getDisplayMetrics();
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        window = LayoutInflater.from(context).inflate(R.layout.marker_window, null);
        window.setLayoutParams(new ViewGroup.LayoutParams(200,
                100));
    }

    private void addWindowText(Marker marker,View view){
        view.setLayoutParams(new ViewGroup.LayoutParams(200,
                100));
        String title = marker.getTitle();
        String[] info = title.split("_");
        TextView cityTv = view.findViewById(R.id.cityTv);
        TextView casesTv = view.findViewById(R.id.casesTv);
        cityTv.setText(info[0]);
        casesTv.setText(info[1]);
    }
    @Override
    public View getInfoWindow(Marker marker) {
        addWindowText(marker,window);
        window.setLayoutParams(new ViewGroup.LayoutParams(200,
                100));

        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }
}
