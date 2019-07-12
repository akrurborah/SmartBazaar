package com.aec.smartbazzar.authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aec.smartbazzar.MainScreen;
import com.aec.smartbazzar.models.Users;
import com.aec.smartbazzar.networkUtils.NetworkConstants;
import com.aec.smartbazzar.R;
import com.aec.smartbazzar.notifications.MyFirebaseMessagingService;
import com.aec.smartbazzar.utlis.PersistentData;
import com.aec.smartbazzar.utlis.ResponseCode;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {

    EditText phone_textbox, name_textbox, addr_textbox;
    ShowHidePasswordEditText password_textbox;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        phone_textbox = findViewById(R.id.mob_no);
        password_textbox = findViewById(R.id.password_edit_Text);
        name_textbox = findViewById(R.id.name);
        addr_textbox = findViewById(R.id.address);
        Button submitButton = findViewById(R.id.submit_btn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = validateInput();
                if(! valid){
                    Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                }else{
                    postToServer(phone_textbox.getText().toString(), password_textbox.getText().toString(),
                            name_textbox.getText().toString(), addr_textbox.getText().toString());
                }
            }
        });

    }

    private void postToServer(final String phone, String password, String name, String addr) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = NetworkConstants.HOST_URL + "user/add";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals(ResponseCode.PHONE_NUMBER_ALREADY_EXIST)){
                            Toast.makeText(getApplicationContext(), "Phone Number Already Exist", Toast.LENGTH_SHORT).show();
                            phone_textbox.setText("");
                            password_textbox.setText("");
                        } else{
                            addToSharedPreference();
                            Gson gson = new Gson();
                            PersistentData.myself = gson.fromJson(response, Users.class);
                            startActivity(new Intent(getBaseContext(), MainScreen.class));
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("ph", phone_textbox.getText().toString());
                params.put("pw", hashGenerate(password_textbox.getText().toString()));
                params.put("name", name_textbox.getText().toString());
                params.put("addr", addr_textbox.getText().toString());
                params.put("fcm", getFcmToken());
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void addToSharedPreference() {
        sharedPreferences = getSharedPreferences("mySharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", phone_textbox.getText().toString());
        editor.putString("pw", hashGenerate(password_textbox.getText().toString()));
        editor.commit();
    }

    boolean validateInput(){
        boolean valid = true;

        if(TextUtils.isEmpty(phone_textbox.getText() ))
            return false;
        else if (! TextUtils.isDigitsOnly(phone_textbox.getText()))
            return false;
        else if (phone_textbox.getText().length() != 10)
            return false;

        if(TextUtils.isEmpty(name_textbox.getText()))
            return  false;
        if(TextUtils.isEmpty(password_textbox.getText()))
            return false;
        if (TextUtils.isEmpty(addr_textbox.getText()))
            return false;

        return valid;
    }

    private String hashGenerate(String password) {
        StringBuilder sb = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getFcmToken() {
        sharedPreferences = getSharedPreferences("mySharedPrefs", MODE_PRIVATE);
        if( sharedPreferences.contains("fcmToken")) {
            return sharedPreferences.getString("fcmToken", "");
        } else{
            return "";
        }
    }

}
