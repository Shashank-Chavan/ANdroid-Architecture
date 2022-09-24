package com.example.arcitecture_components;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Recycler recycler;
    int  mins = 5 ,secs = 60;
    ProgressDialog spinner;
    Layout layout;
    String s,m;
    CountryListViewModel countryListViewModel;
    ProgressDialog dialog;
    AlertDialog.Builder ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView countMin = findViewById(R.id.minutes);
        TextView countSec = findViewById(R.id.seconds);

        new CountDownTimer(30000,1000){
            @Override
            public void onTick(long l) {
                if(secs==0){
                    mins--;secs = 59;
                   // m = mins.toString();
                   // s = secs.toString();
                }
                //countMin.setText(String.valueOf(mins));
                //countSec.setText(String.valueOf(secs));
                secs--;
            }
            @Override
            public void onFinish() {

            }
        }.start();
        ViewModelProvider viewModelProvider = new ViewModelProvider(MainActivity.this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        countryListViewModel = viewModelProvider.get(CountryListViewModel.class);
        /*countMin.setText(String.valueOf(countryListViewModel.mins));
        countSec.setText(String.valueOf(countryListViewModel.secs));*/
        countryListViewModel.getCountryLiveDatamins().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                countMin.setText(String.valueOf(integer));
            }
        });
        countryListViewModel.getCountryLiveDatasecs().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                countSec.setText(String.valueOf(integer));
            }
        });
        setUpLiveData();
        /*ConstraintLayout constraintLayout = findViewById(R.id.Container);
        constraintLayout.setBackgroundResource(R.color.purple_200);
        List<Countries>countriesList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.Reycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recycler = new Recycler(countriesList);
        recyclerView.setAdapter(recycler);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        new CountriesAsyncTask().execute("https://api.printful.com/countries");
        SharedPreferences sharedPreferences = getSharedPreferences("color", Context.MODE_PRIVATE);
        Boolean is_Clicked = sharedPreferences.getBoolean("is_clicked", false);
        if(is_Clicked){
            String s = "shashank";
            countMin.setText(s);
            ConstraintLayout Layout = findViewById(R.id.Container);
            Layout.setBackgroundResource(R.color.purple_200);
        }
        Bundle bundle = new Bundle();
        int i = bundle.getInt("key");
        if(i==1){
            ConstraintLayout constraintLayout = findViewById(R.id.Container);
            constraintLayout.setBackgroundResource(R.color.purple_200);
            countMin.setText(String.valueOf(i));
        }*/

        /*countryListViewModel.getCountryListLive().observe(this, new Observer<List<Countries>>() {
            @Override
            public void onChanged(List<Countries> countries) {
               /* Recycler recycler =new Recycler(countries);
                RecyclerView recyclerView =findViewById(R.id.Reycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(recycler);*/
               /* handleCountryList(countries);
                }
        });
            countryListViewModel.getCountryResponseLive().observe(this, new Observer<CountryListViewModel.RequestStatus>() {
            @Override
            public void onChanged(CountryListViewModel.RequestStatus requestStatus) {
                dialog = new ProgressDialog(MainActivity.this);
                switch (requestStatus)
                {
                    case IN_PROGRESS:

                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setTitle("Fetching games");
                        dialog.setMessage("Please wait...");
                        dialog.setIndeterminate(true);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        break;
                    case SUCCEEDED:
                        dialog.dismiss();
                        break;
                    case FAILED:
                    //ad = new AlertDialog.Builder(MainActivity.this).setTitle("Request Failed").setMessage("Fetching country request failed");
                    //ad.show();


                }
            }
        });*/
    }
    private void setUpLiveData() {
        countryListViewModel.getCountryListLive().observe(this, new Observer<List<Countries>>() {
            @Override
            public void onChanged(List<Countries> countriesList) {
                handleCountryList(countriesList);
            }
        });
        countryListViewModel.getCountryResponseLive().observe(this, new Observer<CountryListViewModel.RequestStatus>() {
            @Override
            public void onChanged(CountryListViewModel.RequestStatus requestStatus) {
                handleRequestStatus(requestStatus);
            }
        });
        countryListViewModel.getcolor().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                //if(aBoolean)
                //{
                    ConstraintLayout constraintLayout = findViewById(R.id.Container);
                    constraintLayout.setBackgroundResource(R.color.purple_200);
                //}
            }
        });

    }
    private void handleCountryList(List<Countries> countries) {
        RecyclerAdapter recycler =new RecyclerAdapter(countries);
        RecyclerView recyclerView =findViewById(R.id.Recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(recycler);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        Toast.makeText(MainActivity.this,"recycler view Implemented",Toast.LENGTH_SHORT).show();
    }
    private void handleRequestStatus(CountryListViewModel.RequestStatus requestStatus){
        //dialog = new ProgressDialog(MainActivity.this);
        switch (requestStatus)
        {
            case IN_PROGRESS:
                if(dialog==null) {
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setTitle("Fetching countries....");
                    dialog.setMessage("Please wait...");
                    dialog.setIndeterminate(true);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
                dialog.show();
                break;
            case SUCCEEDED:
                if(dialog!=null) {
                    dialog.dismiss();
                }
                break;
            case FAILED:
                ad = new AlertDialog.Builder(MainActivity.this).setTitle("Request Failed").setMessage("Fetching country request failed");
                ad.show();
                Toast.makeText(this,"Countries list is unavailable",Toast.LENGTH_SHORT).show();
                break;

        }
    }
    }

    /*private class CountriesAsyncTask extends AsyncTask<String,String,List<Countries>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner = new ProgressDialog(MainActivity.this);
        spinner.setMessage("Wait....its downloading");
        spinner.setIndeterminate(false);
        spinner.setCancelable(false);
        spinner.show();
        }

        @Override
        protected List<Countries> doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            List<Countries> countries = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                BufferedReader br =new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder json  = new StringBuilder();
                String line;
                while((line = br.readLine())!=null){
                    json.append(line);
                }

                JSONObject jsonObject = new JSONObject(json.toString());
                JSONArray jsonArray =jsonObject.getJSONArray("result");
                int count = 0;
                String q,o;
                while(count<jsonArray.length())
                {
                    JSONObject QuizObject = jsonArray.getJSONObject(count);
                    q = QuizObject.getString("name");
                    o = QuizObject.getString("code");

                    Countries quiz1 = new Countries(q,o);
                    countries.add(quiz1);
                    count++;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return countries;
        }

        @Override
        protected void onPostExecute(List<Countries> countriesList) {
            super.onPostExecute(countriesList);
            if(countriesList != null) {
                // spinner.hide();
                recycler.updateQuizList(countriesList);
                spinner.dismiss();
            }else {
                spinner.show();
            }
        }
    }*/

