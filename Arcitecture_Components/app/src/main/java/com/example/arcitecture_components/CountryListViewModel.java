package com.example.arcitecture_components;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountryListViewModel extends AndroidViewModel implements Response.Listener<String>,Response.ErrorListener{
    int mins = 5,secs = 60;
    MutableLiveData<Integer> minutes = new MutableLiveData<>();
    MutableLiveData<Integer> seconds = new MutableLiveData<>();
    MutableLiveData<List<Countries>> countriesLiveData = new MutableLiveData<>();
    MutableLiveData<RequestStatus> requestLiveData = new MutableLiveData<>();
    MutableLiveData<Boolean> color = new MutableLiveData<>();
    private RequestQueue requestQueue;
    public CountryListViewModel(@NonNull Application application) {

        super(application);
        requestQueue = Volley.newRequestQueue(application);
        requestLiveData.postValue(RequestStatus.IN_PROGRESS);
        fetchData();
        /*JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://api.publicapis.org/entries?category=Games", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Countries> countriesList =new ArrayList<>();
                try {
                    JSONObject jsonObject =new JSONObject(String.valueOf(response));
                    response = jsonObject.optJSONArray("entries");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<response.length();i++) {
                    try {
                        JSONObject jsonObject1 = response.getJSONObject(i);
                        String name =  jsonObject1.optString("name");
                        String code = jsonObject1.optString("code");
                        Countries list = new Countries(name,code);
                        countriesList.add(list);
                    }

                    catch (JSONException e) {
                        e.printStackTrace();
                        requestLiveData.postValue(RequestStatus.FAILED);
                    }

                }
                countriesLiveData.postValue(countriesList);
                requestLiveData.postValue(RequestStatus.SUCCEEDED);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestLiveData.postValue(RequestStatus.FAILED);
            }
        });
        requestQueue.add(jsonArrayRequest);*/
        new CountDownTimer(300000,1000){
            @Override
            public void onTick(long l) {
                if(secs==0){
                    mins--;secs = 59;
                    minutes.setValue(mins);
                    seconds.setValue(secs);
                    // m = mins.toString();
                    // s = secs.toString();
                }
                secs--;
                minutes.setValue(mins);
                seconds.setValue(secs);
            }
            @Override
            public void onFinish() {

            }
        }.start();
    }
    public void refetchGames() {
        requestLiveData.postValue(RequestStatus.IN_PROGRESS);
        fetchData();
    }
    public LiveData<Integer> getCountryLiveDatamins(){
        return minutes;
    }
    public LiveData<Integer> getCountryLiveDatasecs(){
        return seconds;
    }
    public LiveData<List<Countries>> getCountryListLive(){
        return countriesLiveData;
    }
    public LiveData<RequestStatus> getCountryResponseLive(){
        return requestLiveData;
    }
    public LiveData<Boolean> getcolor(){
        return color;
    }
    SharedPreferences getshared = getApplication().getSharedPreferences("color", Context.MODE_PRIVATE);



    @Override
    public void onErrorResponse(VolleyError error) {
        requestLiveData.postValue(RequestStatus.FAILED);
    }
    @Override
    public void onResponse(String response) {
        try {
            List<Countries> countriesList = parseResponse(response);
            countriesLiveData.postValue(countriesList);
            requestLiveData.postValue(RequestStatus.SUCCEEDED);
        } catch (JSONException e) {
            e.printStackTrace();
            requestLiveData.postValue(RequestStatus.FAILED);
        }
    }
    private void fetchData()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"https://api.printful.com/countries",this,this);
        requestQueue.add(stringRequest);
    }
    private List<Countries> parseResponse(String response) throws JSONException {
        List<Countries> countries = new ArrayList<>();
        JSONObject res = new JSONObject(response);
        JSONArray entries = res.optJSONArray("result");

        if (entries == null) {
            return countries;
        }

        for (int i = 0; i < entries.length(); i++) {
            JSONObject obj = (JSONObject) entries.get(i);
            String name = obj.optString("name");
            String code = obj.optString("code");
            Countries country = new Countries(name,code);
            countries.add(country);
        }

        return countries;
    }
    public enum RequestStatus{
        /* Show API is in progress. */
        IN_PROGRESS,

        /* Show API request is failed. */
        FAILED,

        /* Show API request is successfully completed. */
        SUCCEEDED
    }
}
