package com.example.arcitecture_components;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Recycler extends RecyclerView.Adapter {
    List<Countries> countries;


    public Recycler(List<Countries> countries) {
        this.countries = countries;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        view = layoutInflater.inflate(R.layout.card, parent, false);
        return new ViewHolder(view);
    }
    void updateQuizList(List<Countries> countries) {
        this.countries.clear();
        this.countries = countries;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Countries g = countries.get(position);
            ViewHolder viewHolderOne = (ViewHolder) holder;
            viewHolderOne.A_name.setText(g.getName());
            viewHolderOne.A_code.setText(g.getCode());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences shrd = view.getContext().getSharedPreferences("color", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shrd.edit();
                    editor.putBoolean("is_clicked",true);
                    Toast.makeText(view.getContext(), "Clicked",Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();

                    bundle.putInt("key",1);
                }
            });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView A_name;
        TextView A_code;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            A_name = (TextView) itemView.findViewById(R.id.Name);
            A_code = (TextView) itemView.findViewById(R.id.Code);
        }

        @Override
        public void onClick(View view) {
            SharedPreferences shrd = view.getContext().getSharedPreferences("color", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shrd.edit();
            editor.putBoolean("is_clicked",true);
            Toast.makeText(view.getContext(), "Clicked",Toast.LENGTH_SHORT).show();
        }
    }
}
