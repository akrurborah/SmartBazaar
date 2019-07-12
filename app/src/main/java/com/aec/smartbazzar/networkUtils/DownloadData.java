package com.aec.smartbazzar.networkUtils;

import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import com.aec.smartbazzar.utlis.JSONUtility;
import com.aec.smartbazzar.utlis.PersistentData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class DownloadData {

    private Context context;
    private String url;
    private RequestQueue queue;
    private static boolean isDownloadedItem = false;
    private static boolean isDownloadedCategories = false;


    public DownloadData(Context context) {
        this.context = context;
        url =  NetworkConstants.HOST_URL;
        queue = Volley.newRequestQueue(context);
        downloadItemData();
        downloadCategoryData();
        makeItemMap();
    }

    public void downloadItemData(){
        JsonArrayRequest requestItems = new JsonArrayRequest(Request.Method.GET, url + "item/get", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                PersistentData.setItemList(JSONUtility.parseItemJSON(response));
                isDownloadedItem = true;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(requestItems);
    }

    public void downloadCategoryData(){
        JsonArrayRequest requestCategories = new JsonArrayRequest(Request.Method.GET, url + "cat/get", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                PersistentData.setCategoryList(JSONUtility.parseCategoryJSON(response));
                isDownloadedCategories = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(requestCategories);
    }

    private void makeItemMap(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    try {
                        while (!isDownloaded()) {
                            wait(500);
                            Log.i("Waiting to dnld data", "<---->");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    PersistentData.setItemMap(PersistentData.getCategoryList(), PersistentData.getItemList());
                    Log.i("Waiting to dnld data", "<--DNLD DONE-->");
                }
            }
        }).start();
    }
    public boolean updateItems(){

        return true;
    }

    private boolean isDownloaded(){
        return !(PersistentData.getCategoryList() == null && PersistentData.getItemList() == null);
    }
}
