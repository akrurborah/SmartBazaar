package com.aec.smartbazzar.searchPackage;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aec.smartbazzar.ItemDetailsActivity;
import com.aec.smartbazzar.models.Item;
import com.aec.smartbazzar.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class ItemSearchAdapter extends RecyclerView.Adapter<ItemSearchAdapter.MySearchViewHolder> implements Filterable{

    private SearchActivity activity;
    private ArrayList<Item> itemList;
    private ArrayList<Item> filteredList;

    public ItemSearchAdapter(SearchActivity activity, ArrayList<Item> itemList){
        this.activity = activity;
        this.itemList = itemList;
        filteredList = new ArrayList<>();
        getFilter();
    }

    @NonNull
    @Override
    public ItemSearchAdapter.MySearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row_item, parent, false);

        return new MySearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemSearchAdapter.MySearchViewHolder holder, final int position) {
        final Item item = filteredList.get(position);
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(item.getPrice());
        holder.img.setImageURI(Uri.parse(item.getImg_url()));

        holder.searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ItemDetailsActivity.class);
                intent.putExtra("item", filteredList.get(position));
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }


    public static class MySearchViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView img;
        TextView itemName;
        TextView itemPrice;
        RelativeLayout searchItem;

        public MySearchViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_searchactivity);
            itemName = itemView.findViewById(R.id.search_item_name);
            itemPrice = itemView.findViewById(R.id.search_item_price);
            searchItem = itemView.findViewById(R.id.search_item);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()){
                    filteredList = itemList;
                }else{
                    ArrayList<Item> list = new ArrayList<>();
                    for(Item item : itemList){
                        if(item.getName().toLowerCase().contains(charString.toLowerCase()))
                            list.add(item);
                    }
                    filteredList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Item>) results.values;
                notifyDataSetChanged();
                activity.showEmptyText(filteredList.isEmpty());
            }
        };
    }
}
