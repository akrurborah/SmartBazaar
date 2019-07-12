package com.aec.smartbazzar;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aec.smartbazzar.models.CartItem;
import com.aec.smartbazzar.models.Item;
import com.aec.smartbazzar.searchPackage.SearchActivity;
import com.aec.smartbazzar.utlis.PersistentData;
import com.facebook.drawee.view.SimpleDraweeView;

import com.aec.smartbazzar.cartPackage.CartActivity;

public class ItemDetailsActivity extends AppCompatActivity {

    Item item;
    SimpleDraweeView imgView;
    TextView itemName, itemDetails, itemPrice;
    TextView buttonAddToCart, buttonBuyNow;
    TextView textCartItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        item = (Item) getIntent().getSerializableExtra("item");

        final Uri uri = Uri.parse(item.getImg_url());
        imgView = findViewById(R.id.image1);
        imgView.setImageURI(uri);
        itemName = findViewById(R.id.item_name);
        itemName.setText(item.getName());
        itemPrice = findViewById(R.id.item_price);
        itemPrice.setText(item.getPrice());
        itemDetails = findViewById(R.id.item_details);
        itemDetails.setText("\u2022 " + item.getDetails());

        buttonAddToCart = findViewById(R.id.text_action_addtocart);

        if(isOnCart(item)) {
            buttonAddToCart.setText("GO TO CART");
        }

        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(isOnCart(item)){
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    }else{
                        PersistentData.cartList.add(new CartItem(item, "1"));
                        setupBadge();
                        buttonAddToCart.setText("GO TO CART");
                    }
                }
        });

        buttonBuyNow = findViewById(R.id.text_action_buynow);
        buttonBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOnCart(item)) {
                    PersistentData.cartList.add(new CartItem(item, "1"));
                    setupBadge();
                }
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isOnCart(item)) {
            buttonAddToCart.setText("GO TO CART");
        }else{
            buttonAddToCart.setText("ADD TO CART");
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        /*actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("action bar", " on click---->");
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }
        });*/
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setupBadge();
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    private boolean isOnCart(Item item){
        for ( CartItem cartItem : PersistentData.cartList) {
            if(cartItem.getItem().getI_id().equalsIgnoreCase(item.getI_id()))
                return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            /*case R.id.action_cart :
                startActivity(new Intent(this, CartActivity.class));
                break;*/
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {
        if (PersistentData.cartList != null) {
            if (PersistentData.cartList.size() == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(PersistentData.cartList.size()));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
        invalidateOptionsMenu();
    }

    public void gotoCart(MenuItem menu){
        startActivity(new Intent(this, CartActivity.class));
    }
}