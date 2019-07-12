package com.aec.smartbazzar.orders;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aec.smartbazzar.R;
import com.aec.smartbazzar.models.OrderListItem;
import com.choota.dev.ctimeago.TimeAgo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private List<OrderListItem> orderList;
    private Context mContext;

    public OrderAdapter(List<OrderListItem> orderList, Context mContext) {
        this.orderList = orderList;
        this.mContext = mContext;
    }

    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.MyViewHolder holder, int position) {
        final OrderListItem item = orderList.get(position);
        holder.orderId.setText(item.getOrderID());
        holder.orderItemName.setText(item.getItemName());
        holder.orderQty.setText(item.getQty());
        holder.Total.setText(item.getTotal());
        holder.orderTime.setText(getFormattedTime(item.getOrderTime()));
        holder.orderStatus.setText(getStatus(item.getStatus()));
        holder.img.setImageURI(Uri.parse(item.getImg()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private String getStatus(String status) {
        String result = "N/A";
        if (status.contentEquals("801")){
            result = "ORDER PLACED";
        }else if (status.contentEquals("802")){
            result = "ORDER APPROVED";
        }else if (status.contentEquals("803")){
            result = "ORDER CANCELLED";
        }else if (status.contentEquals("804")){
            result = "ORDER DELIVERED";
        }
        return result;
    }

    private String getFormattedTime(String orderTime) {
        TimeAgo timeAgo = new TimeAgo().locale(mContext);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        String result = "N/A";
        try {
            date =formatter.parse(orderTime);
            result = timeAgo.getTimeAgo(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView img;
        TextView orderId;
        TextView orderItemName;
        TextView orderQty;
        TextView Total;
        TextView orderTime;
        TextView orderStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.order_img);
            orderId = itemView.findViewById(R.id.order_id);
            orderItemName = itemView.findViewById(R.id.order_item_name);
            orderQty = itemView.findViewById(R.id.order_item_qty);
            Total = itemView.findViewById(R.id.order_total_price);
            orderTime = itemView.findViewById(R.id.order_time);
            orderStatus = itemView.findViewById(R.id.order_status);
        }
    }
}
