package com.aec.smartbazzar.cartPackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aec.smartbazzar.MainScreen;
import com.aec.smartbazzar.models.CartItem;
import com.aec.smartbazzar.models.OrderItem;
import com.aec.smartbazzar.models.OrderList;
import com.aec.smartbazzar.networkUtils.NetworkConstants;
import com.aec.smartbazzar.R;
import com.aec.smartbazzar.utlis.PersistentData;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public class CartActivity extends AppCompatActivity {

    LinearLayout empty_cartLayout, notEmpty_cartLayout;
    CartItemAdapter adapter;
    RecyclerView recyclerView;
    TextView totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        empty_cartLayout = findViewById(R.id.empty_cart_layout);
        notEmpty_cartLayout = findViewById(R.id.not_empty_cart_layout);
        totalPrice = findViewById(R.id.cart_total_price);

        recyclerView = findViewById(R.id.cart_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        adapter = new CartItemAdapter(PersistentData.cartList, recyclerView, this);

        if(PersistentData.cartList.isEmpty()){
            notEmpty_cartLayout.setVisibility(View.GONE);
            empty_cartLayout.setVisibility(View.VISIBLE);
        }
        else{
            empty_cartLayout.setVisibility(View.GONE);
            notEmpty_cartLayout.setVisibility(View.VISIBLE);

            recyclerView.setAdapter(adapter);
            updateTotalPrice();
        }

        findViewById(R.id.confirm_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

   public void shopNow(View view){
        startActivity(new Intent(this, MainScreen.class));
        finish();
   }

    public void updateTotalPrice(){
        if(PersistentData.cartList.isEmpty()){
            notEmpty_cartLayout.setVisibility(View.GONE);
            empty_cartLayout.setVisibility(View.VISIBLE);
        }
        totalPrice.setText("Rs " + CartItem.getTotalPrice(PersistentData.cartList));
    }

    private void placeOrder(){
        final ProgressDialog loadingDialog = new ProgressDialog(this); // this = YourActivity
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setMessage("Placing your Order. Please wait...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        OrderList orderList = new OrderList();

        orderList.userId = PersistentData.myself.getU_id();
        for(CartItem cartItem : PersistentData.cartList){
            orderList.itemList.add(new OrderItem(cartItem.getItem().getI_id(), cartItem.getQuantity()));
        }

        Gson gson = new Gson();
        final String requestBody = gson.toJson(orderList);


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = NetworkConstants.HOST_URL + "order/place";

        //TODO Dialogs
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String str;
                Log.i("response", response);
                if(response.equals("801")) {
                    PersistentData.cartList.clear();
                    updateTotalPrice();
                    loadingDialog.dismiss();
                    showSucessDialog();

                }else{
                    str = "Unable to place your Order. Try after sometime";
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.setMessage(error.getMessage());
                loadingDialog.setCancelable(true);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    private void showSucessDialog(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.dialog_sucess, null);
        final AlertDialog sucessDialog = new AlertDialog.Builder(this).create();
        sucessDialog.setView(dialogView);
        dialogView.findViewById(R.id.dialog_close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sucessDialog.dismiss();
            }
        });
        sucessDialog.show();

    }
}