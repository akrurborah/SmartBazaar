package com.aec.smartbazzar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aec.smartbazzar.authentication.LoginActivity;
import com.aec.smartbazzar.imageCache.ImagePipelineConfigFactory;
import com.aec.smartbazzar.models.Users;
import com.aec.smartbazzar.networkUtils.NetworkConstants;
import com.aec.smartbazzar.utlis.PersistentData;
import com.aec.smartbazzar.utlis.ResponseCode;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;


public class SplashScreen extends AppCompatActivity {


    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.i("token", newToken);
                sharedPreferences = getSharedPreferences("mySharedPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("fcmToken", newToken);
                editor.commit();
            }
        });

        NewtonCradleLoading cradleLoading;
        cradleLoading = findViewById(R.id.cradle);
        cradleLoading.start();
        cradleLoading.setLoadingColor(Color.WHITE);
        PersistentData.cartList = new ArrayList<>();
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    try {
                        wait(1000);
                        checkIfOnline();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void checkIfOnline(){
        if(isConnected()){
            requestServer();

        } else {
            final RelativeLayout relativeLayout = findViewById(R.id.splash_layout);
            final Snackbar snackbar = Snackbar
                    .make(relativeLayout, "No Internet Connection. Turn on..", Snackbar.LENGTH_INDEFINITE)
                    .setAction("GO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
            snackbar.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this){
                        while(!isConnected()) {
                            try {
                                wait(1000);
                                if (isConnected())
                                    snackbar.dismiss();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        snackbar.dismiss();
                        requestServer();
                    }
                }
            }).start();
        }
    }

    private void requestServer() {
        sharedPreferences = getSharedPreferences("mySharedPrefs", MODE_PRIVATE);
        if( sharedPreferences.contains("username") && sharedPreferences.contains("pw")) {
            authenticateLogin(sharedPreferences.getString("username", ""), sharedPreferences.getString("pw", ""));
        } else{
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            this.finish();
        }
    }

    void authenticateLogin(String userName, final String password){

        String url = NetworkConstants.HOST_URL + "user/login?ph=" + userName + "&pw=" + password;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contentEquals(ResponseCode.INVALID_LOGIN_DETAILS)){
                            Toast.makeText(getApplicationContext(), "Unable to Login", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Gson gson = new Gson();
                            PersistentData.myself = gson.fromJson(response, Users.class);
                            Log.i("myself", PersistentData.myself.toString());
                            startActivity(new Intent(getApplicationContext(), MainScreen.class));
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

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    try {
                        wait(1500);
                        checkIfOnline();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}