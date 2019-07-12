package com.aec.smartbazzar.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aec.smartbazzar.ItemDetailsActivity;
import com.aec.smartbazzar.MainScreen;
import com.aec.smartbazzar.models.Category;
import com.aec.smartbazzar.models.Item;
import com.aec.smartbazzar.R;
import com.aec.smartbazzar.utlis.PersistentData;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class MainScreenFragment extends Fragment {

    private static MainScreen mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainScreen) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.layout_recyclerview, container, false);
        setupRecyclerView(recyclerView);
        return recyclerView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        List<Item> items = new ArrayList<>();

        if(MainScreenFragment.this.getArguments().getString("type").equals("ALL"))
            items = PersistentData.getItemList();
        for (Category c : PersistentData.getCategoryList() ) {
            if(MainScreenFragment.this.getArguments().getString("type").equals(c.getCat_name())){
                items = PersistentData.itemMap.get(c.getCat_name());
            }
        }

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(recyclerView, items));
    }

    private static class SimpleStringRecyclerViewAdapter extends  RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder>{

        private List<Item> mValues;
        private RecyclerView mRecyclerView;
        private RecyclerView.ViewHolder holder;
        private int position;

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, List<Item> items) {
            mRecyclerView =recyclerView;
            mValues = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
            return new ViewHolder(view);
        }

        public void onViewRecycled(ViewHolder holder) {
            if (holder.mImageView.getController() != null) {
                holder.mImageView.getController().onDetach();
            }
            if (holder.mImageView.getTopLevelDrawable() != null) {
                holder.mImageView.getTopLevelDrawable().setCallback(null);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            final Uri uri = Uri.parse(mValues.get(position).getImg_url());
            final  String name = mValues.get(position).getName();
            final  String price = mValues.get(position).getPrice();
            holder.mImageView.setImageURI(uri);
            holder.nameTextView.setText(name);
            holder.priceTextView.setText("Rs. " + price);


            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ItemDetailsActivity.class);
                    intent.putExtra("item", mValues.get(position));
                    mActivity.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final SimpleDraweeView mImageView;
            public final LinearLayout mLayoutItem;
            public final TextView nameTextView;
            public final TextView priceTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = view.findViewById(R.id.image1);
                mLayoutItem = view.findViewById(R.id.layout_item);
                nameTextView = view.findViewById(R.id.item_name);
                priceTextView = view.findViewById(R.id.item_price);

            }
        }
    }
}
