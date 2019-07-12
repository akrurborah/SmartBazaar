package com.aec.smartbazzar.authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aec.smartbazzar.MainScreen;
import com.aec.smartbazzar.models.Users;
import com.aec.smartbazzar.networkUtils.NetworkConstants;
import com.aec.smartbazzar.utlis.PersistentData;
import com.aec.smartbazzar.R;
import com.aec.smartbazzar.utlis.ResponseCode;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    String userName, password;
    EditText phNumber_textbox;
    ShowHidePasswordEditText password_textbox;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phNumber_textbox = findViewById(R.id.ph_number);
        password_textbox = findViewById(R.id.password_edit_Text);

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( TextUtils.isEmpty(phNumber_textbox.getText()) || TextUtils.isEmpty(password_textbox.getText()))
                    Toast.makeText(getApplicationContext(), "Fields Empty", Toast.LENGTH_SHORT).show();
                else{
                    userName = phNumber_textbox.getText().toString();
                    password = password_textbox.getText().toString();

                    authenticateLogin(userName, password);
                }
            }
        });

        TextView textView = findViewById(R.id.new_acc);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });
    }

    void authenticateLogin(final String userName, final String password){

        String url = NetworkConstants.HOST_URL + "user/login?ph=" + userName + "&pw=" + hashGenerate(password);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contentEquals(ResponseCode.INVALID_LOGIN_DETAILS)){
                            Toast.makeText(getApplicationContext(), "Invalid Details", Toast.LENGTH_SHORT).show();
                            password_textbox.setText("");

                        } else {
                            addToSharedPreference();
                            Gson gson = new Gson();
                            PersistentData.myself = gson.fromJson(response, Users.class);
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

    private void addToSharedPreference() {
        sharedPreferences = getSharedPreferences("mySharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", phNumber_textbox.getText().toString());
        editor.putString("pw", hashGenerate(password_textbox.getText().toString()));
        editor.commit();
    }

    private String hashGenerate(String password) {
        StringBuilder sb = null;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
