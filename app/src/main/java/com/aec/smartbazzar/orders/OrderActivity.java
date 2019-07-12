package com.aec.smartbazzar.orders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aec.smartbazzar.MainScreen;
import com.aec.smartbazzar.R;
import com.aec.smartbazzar.models.OrderListItem;
import com.aec.smartbazzar.networkUtils.NetworkConstants;
import com.aec.smartbazzar.utlis.PersistentData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    RelativeLayout empty_Layout;
    OrderAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        empty_Layout = findViewById(R.id.empty_order_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.order_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        adapter = new OrderAdapter(PersistentData.orderListItems, this);
        recyclerView.setAdapter(adapter);
        updateView();
        getDataFromServer();

    }

    private void updateView(){
        if(PersistentData.orderListItems.isEmpty() || PersistentData.orderListItems.size() == 0){
            recyclerView.setVisibility(View.GONE);
            empty_Layout.setVisibility(View.VISIBLE);
        }
        else{
            empty_Layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    private void getDataFromServer(){
        PersistentData.orderListItems.clear();
        String url = NetworkConstants.HOST_URL + "order/getmyorder?uid=" + PersistentData.myself.getU_id();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if( response != null || response != ""){
                            addToPersistantData(response);
                            updateView();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void shopNow(View view){
        startActivity(new Intent(this, MainScreen.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    private void addToPersistantData(String data){
        Gson gson = new Gson();
        try {
            OrderListItem item;
            JSONArray jsonArray = new JSONArray(data);
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                item = gson.fromJson(object.toString(), OrderListItem.class);
                PersistentData.orderListItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
