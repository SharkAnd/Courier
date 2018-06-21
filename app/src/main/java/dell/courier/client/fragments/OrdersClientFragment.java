package dell.courier.client.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dell.courier.BaseActivity;
import dell.courier.R;
import dell.courier.client.ClientActivity;
import dell.courier.client.CreateNewOrderActivity;
import dell.courier.client.adapter.OrderRecyclerViewAdapter;
import dell.courier.client.model.Order;

public class OrdersClientFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private OrderRecyclerViewAdapter orderRecyclerViewAdapter;
    private List<Order> orderList;
    private List<String> key;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("clients").child(BaseActivity.getUid()).child("orders");

    public OrdersClientFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        loadOrders();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_client_orders, container, false);
        orderList = new ArrayList<>();
        key = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.orders_client_list);
        recyclerView.setHasFixedSize(true);
        orderRecyclerViewAdapter = new OrderRecyclerViewAdapter(orderList);
        recyclerView.setAdapter(orderRecyclerViewAdapter);
        fab = rootView.findViewById(R.id.create_new_order);
        fab.setOnClickListener((View view) ->{
            startActivity(new Intent(getActivity(), CreateNewOrderActivity.class));
        });
        return rootView;
    }

    private void loadOrders(){

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                orderList.add(dataSnapshot.getValue(Order.class));
                orderRecyclerViewAdapter.notifyDataSetChanged();

                key.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Order order = dataSnapshot.getValue(Order.class);
                String keys = dataSnapshot.getKey();

                int index = key.indexOf(keys);

                orderList.set(index,order);

                orderRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String keys = dataSnapshot.getKey();
                int index = key.indexOf(keys);
                orderList.remove(index);

                orderRecyclerViewAdapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                orderRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
