package com.example.arcitecture_components;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CountriesHolder> {

    private List<Countries> countriesList;
   // private ItemClickListener clickListener;

    public RecyclerAdapter(List<Countries> countriesList) {
        this.countriesList = countriesList;
        //this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CountriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        return new CountriesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CountriesHolder holder, int position) {
        //holder.bindView(countriesList.get(position));
        final Countries g = countriesList.get(position);
        RecyclerAdapter.CountriesHolder viewHolderOne = (RecyclerAdapter.CountriesHolder) holder;
        viewHolderOne.Name.setText(g.getName());
        viewHolderOne.Code.setText(g.getCode());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clickListener.onItemClick();
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("color", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("key",true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countriesList.size();
    }

    static class CountriesHolder extends RecyclerView.ViewHolder {
        TextView Name;
        TextView Code;
        public CountriesHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.Name);
            Code = itemView.findViewById(R.id.Code);
        }

       /* public void bindView(Countries countries) {
            ((TextView)itemView).setText(countries.getName());
        }*/
    }
    public interface ItemClickListener{
        public void onItemClick();

    }
}

