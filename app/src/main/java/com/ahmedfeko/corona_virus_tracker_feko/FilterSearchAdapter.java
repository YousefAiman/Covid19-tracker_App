package com.ahmedfeko.corona_virus_tracker_feko;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterSearchAdapter extends RecyclerView.Adapter<FilterSearchAdapter.searchviewholder> implements Filterable {
    List<CountryCase> searches ;
    List<CountryCase> filteredSearches;
    Context context;
    int itemLayoutId;
    int height;
    FilterSearchAdapter(Context context, List<CountryCase> searches, int itemLayoutId) {
        this.searches = searches;
        this.filteredSearches = searches;
        this.context = context;
        this.itemLayoutId = itemLayoutId;
        DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
        height = displaymetrics.heightPixels / 16;
    }

    @NonNull
    @Override
    public searchviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new searchviewholder(LayoutInflater.from(context).inflate(R.layout.search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull searchviewholder holder, int position) {
        final CountryCase countryCase = filteredSearches.get(position);
        holder.itemView.getLayoutParams().height = height;
        holder.countrySearchTv.setText(countryCase.getCountry());
        holder.casesSearchTv.setText(String.format(Locale.getDefault(),"%,d", countryCase.getCases()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MapsActivity)context).startCountryActivity(countryCase);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return filteredSearches.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterString = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();
                final List<CountryCase> list = searches;
                int count = list.size();
                final ArrayList<CountryCase> nlist = new ArrayList<CountryCase>(count);
                CountryCase filterableString;
                for (int i = 0; i < count; i++) {
                    filterableString = list.get(i);
                    if (filterableString.getCountry().toLowerCase().contains(filterString.toLowerCase())) {
                        nlist.add(filterableString);
                    }
                }
                results.values = nlist;
                results.count = nlist.size();
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredSearches = (ArrayList<CountryCase>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class searchviewholder extends RecyclerView.ViewHolder {
        TextView countrySearchTv;
        TextView casesSearchTv;
        searchviewholder(@NonNull View itemView) {
            super(itemView);
            countrySearchTv = itemView.findViewById(R.id.countrySearchTv);
            casesSearchTv = itemView.findViewById(R.id.casesSearchTv);
        }
    }
}
