package dell.courier.client.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import dell.courier.R;

public class OrderViewHolder extends RecyclerView.ViewHolder{

     TextView status;
     TextView dateCreated;
     TextView dateDelivery;
     TextView goods;
     TextView price;
     TextView address;

    public OrderViewHolder(View itemView) {
        super(itemView);
        status = itemView.findViewById(R.id.status_order_item);
        dateCreated = itemView.findViewById(R.id.date_created_order_item);
        dateDelivery = itemView.findViewById(R.id.date_delivery_order_item);
        goods = itemView.findViewById(R.id.goods_order_item);
        price = itemView.findViewById(R.id.price_order_item);
        address = itemView.findViewById(R.id.address_order_item);
    }
}
