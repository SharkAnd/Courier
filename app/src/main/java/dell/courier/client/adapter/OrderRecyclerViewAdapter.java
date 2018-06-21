package dell.courier.client.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.util.List;

import dell.courier.R;
import dell.courier.client.model.Order;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderViewHolder>{

    private List<Order> orderList;
    private Context context;

    public OrderRecyclerViewAdapter(List<Order> orderList) {
        this.orderList=orderList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {

        Order order = orderList.get(position);
        String dc = DateUtils.formatDateTime(context,
                order.getDateCreatedL(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME);
        String dd = DateUtils.formatDateTime(context,
                order.getDateDelivery(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME);
        String address = order.getStreet()+" "+order.getNumber_house()+" "+order.getNumber_entrance()+" "+order.getNumber_apartment();

        holder.status.setText(order.getStatus());
        holder.dateCreated.setText(dc);
        holder.dateDelivery.setText(dd);
        holder.goods.setText(order.getGoods());
        holder.price.setText(String.valueOf(order.getPrice())+" "+"BYN");
        holder.address.setText(address);

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
