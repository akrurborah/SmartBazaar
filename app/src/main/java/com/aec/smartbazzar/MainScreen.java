package com.aec.smartbazzar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aec.smartbazzar.cartPackage.CartActivity;
import com.aec.smartbazzar.models.Category;
import com.aec.smartbazzar.networkUtils.NetworkConstants;
import com.aec.smartbazzar.orders.OrderActivity;
import com.aec.smartbazzar.searchPackage.SearchActivity;
import com.aec.smartbazzar.utlis.JSONUtility;
import com.aec.smartbazzar.utlis.PersistentData;
import com.aec.smartbazzar.fragments.MainScreenFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView textCartItemCount;
    private SwipeRefreshLayout swipeRefresh;
    private int tabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        swipeRefresh = findViewById(R.id.refresh_layout);
        swipeRefresh.setOnRefreshListener(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);
        getDataFromServer();
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
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_cart:
                startActivity(new Intent(this, CartActivity.class));
                break;
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.drawer_exit:
                moveTaskToBack(true);
                break;
            case R.id.drawer_cart:
                startActivity(new Intent(this, CartActivity.class));
                break;
            case R.id.contact_us:
                showContactUs();
                break;
            case R.id.my_orders:
                startActivity(new Intent(this, OrderActivity.class));
        }
        mDrawerLayout.closeDrawer(Gravity.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        MainScreenFragment fragment;
        Bundle bundle;

        for (Category c : PersistentData.getCategoryList()) {
            fragment = new MainScreenFragment();
            bundle = new Bundle();
            bundle.putString("type", c.getCat_name());
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, c.getCat_name());
        }
        viewPager.setAdapter(adapter);
    }

    RequestQueue queue;
    JsonArrayRequest requestItems;

    private void getDataFromServer() {
        String url = NetworkConstants.HOST_URL;
        queue = Volley.newRequestQueue(MainScreen.this);

        requestItems = new JsonArrayRequest(Request.Method.GET, url + "item/get", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                PersistentData.setItemList(JSONUtility.parseItemJSON(response));
                PersistentData.itemMap = PersistentData.setItemMap(PersistentData.getCategoryList(), PersistentData.getItemList());
                if (viewPager != null) {
                    swipeRefresh.setRefreshing(false);
                    setupViewPager(viewPager);
                    tabLayout.setupWithViewPager(viewPager);
                    tabLayout.setScrollPosition(tabPosition, 0f, true);
                    viewPager.setCurrentItem(tabPosition);
                    findViewById(R.id.waiting).setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

        JsonArrayRequest requestCategory = new JsonArrayRequest(Request.Method.GET, url + "cat/get", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                PersistentData.setCategoryList(JSONUtility.parseCategoryJSON(response));
                queue.add(requestItems);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(requestCategory);
    }

    @Override
    public void onRefresh() {
        tabPosition = tabLayout.getSelectedTabPosition();
        getDataFromServer();
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void showContactUs(){
        new FancyGifDialog.Builder(this)
                .setTitle("ABOUT US")
                .setMessage("STAY HUNGRY STAY FOOLISH\nakrurborah@outlook.com")
                .setPositiveBtnText("Close")
                .setPositiveBtnBackground("#004010")
                .isCancellable(true)
                .setNegativeBtnBackground("#1b9b3b")
                .setNegativeBtnText("Mail")
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto","akrurborah@outlook.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Smaart Bazaar");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }
                })
                .setGifResource(R.drawable.about_us_gif)
                .build();
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
    }
}