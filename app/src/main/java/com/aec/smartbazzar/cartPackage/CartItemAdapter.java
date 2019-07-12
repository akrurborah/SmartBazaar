package com.aec.smartbazzar.cartPackage;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aec.smartbazzar.models.CartItem;
import com.aec.smartbazzar.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder>{

    private List<CartItem> cartList;
    private RecyclerView mRecyclerview;
    private Context mContext;

    public CartItemAdapter(List<CartItem> cartList, RecyclerView mRecyclerview, Context mContext) {
        this.cartList = cartList;
        this.mRecyclerview = mRecyclerview;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CartItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlist_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartItemAdapter.MyViewHolder holder, final int position) {
        final CartItem cartItem = cartList.get(position);
        holder.itemName.setText(cartItem.getItem().getName());
        holder.itemPrice.setText(cartItem.getItem().getPrice());
        holder.itemQty.setText(cartItem.getQuantity());
        holder.img.setImageURI(Uri.parse(cartItem.getItem().getImg_url()));

        holder.removeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartList.remove(position);
                notifyDataSetChanged();
                if( mContext instanceof CartActivity){
                    ((CartActivity)mContext).updateTotalPrice();
                }
            }
        });
        holder.addAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = Integer.valueOf(cartItem.getQuantity()) + 1;
                cartItem.setQuantity(String.valueOf(x));
                notifyDataSetChanged();
                if( mContext instanceof CartActivity){
                    ((CartActivity)mContext).updateTotalPrice();
                }
            }
        });
        holder.lessAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = Integer.valueOf(cartItem.getQuantity());
                if( x == 1 )
                    Toast.makeText(mContext, "You have Minimum Quantity", Toast.LENGTH_SHORT).show();
                else{
                    x--;
                    cartItem.setQuantity(String.valueOf(x));
                    notifyDataSetChanged();
                    if( mContext instanceof CartActivity){
                        ((CartActivity)mContext).updateTotalPrice();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView img;
        TextView itemName;
        TextView itemPrice;
        TextView itemQty;
        LinearLayout removeAction;
        ImageView addAction, lessAction;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image_cartlist);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemQty = itemView.findViewById(R.id.item_qty);
            removeAction = itemView.findViewById(R.id.layout_action_remove);
            addAction = itemView.findViewById(R.id.qty_add);
            lessAction = itemView.findViewById(R.id.qty_less);
        }
    }
}