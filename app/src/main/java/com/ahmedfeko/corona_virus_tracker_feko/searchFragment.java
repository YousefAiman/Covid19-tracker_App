package com.ahmedfeko.corona_virus_tracker_feko;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class searchFragment extends Fragment {
    RecyclerView searchRv;
    ImageView nameUpImage;
    ImageView nameDownImage;
    ImageView countDownImage;
    ImageView countUpImage;
    public searchFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        searchRv = view.findViewById(R.id.searchRv);
        nameUpImage = view.findViewById(R.id.nameUpImage);
        nameDownImage = view.findViewById(R.id.nameDownImage);
        countDownImage = view.findViewById(R.id.countDownImage);
        countUpImage = view.findViewById(R.id.countUpImage);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ArrayList<CountryCase> countryCases = ((MapsActivity)getActivity()).countriesAutoList;
        SearchView searchView = ((MapsActivity)getActivity()).countrySv;
        final FilterSearchAdapter recentSearchAdapter = new FilterSearchAdapter(getContext(), countryCases, R.layout.search_item);
        searchRv.setLayoutManager(new LinearLayoutManager(getContext()));
        searchRv.setAdapter(recentSearchAdapter);

        nameUpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Collections.sort(countryCases, new Comparator<CountryCase>() {
                    @Override
                    public int compare(CountryCase countryCase, CountryCase t1) {
                        return countryCase.getCountry().compareTo(t1.getCountry());
                    }
                });
                recentSearchAdapter.notifyDataSetChanged();
            }
        });

        nameDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(countryCases, new Comparator<CountryCase>() {
                    @Override
                    public int compare(CountryCase countryCase, CountryCase t1) {
                        return t1.getCountry().compareTo(countryCase.getCountry());
                    }
                });
                recentSearchAdapter.notifyDataSetChanged();
            }
        });

        countDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(countryCases, new Comparator<CountryCase>() {
                    @Override
                    public int compare(CountryCase countryCase, CountryCase t1) {
                        return t1.getCases() - countryCase.getCases();
                    }
                });
                recentSearchAdapter.notifyDataSetChanged();
            }
        });
        countUpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(countryCases, new Comparator<CountryCase>() {
                    @Override
                    public int compare(CountryCase countryCase, CountryCase t1) {
                        return countryCase.getCases() - t1.getCases();
                    }
                });
                recentSearchAdapter.notifyDataSetChanged();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                recentSearchAdapter.getFilter().filter(s);
                return true;
            }
        });
    }
}
