package dell.courier.courier.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dell.courier.BaseActivity;
import dell.courier.R;
import dell.courier.client.adapter.OrderRecyclerViewAdapter;
import dell.courier.client.fragments.OrdersClientFragment;
import dell.courier.client.model.Order;
import dell.courier.courier.Road;

public class OrdersCourierFragment extends Fragment implements View.OnClickListener {

    private OrdersCourier ordersCourier;
    GoogleMap map;
    private Button dateOrders;
    private Button letsGo;
    private RecyclerView recyclerView;
    private OrderRecyclerViewAdapter orderRecyclerViewAdapter;
    private List<Order> orderList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders");
    private DatePickerDialog.OnDateSetListener date;
    private Calendar calendar = Calendar.getInstance();
    private ChildEventListener listener;
    private GeoApiContext context;
    private Road road = new Road();

    public OrdersCourierFragment() {
    }


    @SuppressLint("ValidFragment")
    public OrdersCourierFragment(GoogleMap map) {
        this.map=map;
    }


    public interface OrdersCourier{
        void showMap(List<Order> orderList);
        void showProgressDialog();
        void hideProgressDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ordersCourier = (OrdersCourier) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = new GeoApiContext.Builder()
                .apiKey("AIzaSyCqYbIw8QZKxe0F-DnlbQqaKYrzvgAeiBE")
                .build();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        loadOrdersByDate(calendar);
    }

    private void loadOrdersByDate(Calendar calendar) {

        if (listener != null){
            ref.removeEventListener(listener);
        }
        orderList.clear();
        orderRecyclerViewAdapter.notifyDataSetChanged();
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                orderList.add(dataSnapshot.getValue(Order.class));
                orderRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                orderRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                orderRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                orderRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref = FirebaseDatabase.getInstance().getReference("orders").child(String.valueOf(calendar.get(Calendar.YEAR)))
                .child(String.valueOf(calendar.get(Calendar.MONTH)))
                .child(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        ref.addChildEventListener(listener);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_courier_orders, container, false);
        dateOrders = view.findViewById(R.id.date_orders_button);
        letsGo = view.findViewById(R.id.to_navigator_button);
        recyclerView = view.findViewById(R.id.orders_courier_list);
        recyclerView.setHasFixedSize(true);
        orderRecyclerViewAdapter = new OrderRecyclerViewAdapter(orderList);
        recyclerView.setAdapter(orderRecyclerViewAdapter);
        dateOrders.setOnClickListener(this::onClick);
        letsGo.setOnClickListener(this::onClick);
        dateOrders.setText(DateUtils.formatDateTime(getActivity(),
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));


        date = (datePicker, year, month, day) -> {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,day);
            dateOrders.setText(DateUtils.formatDateTime(getActivity(),
                    calendar.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

            loadOrdersByDate(calendar);
        };

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id==R.id.date_orders_button){
            setDate();
        } else if (id==R.id.to_navigator_button){
            toNavigator();
        }
    }

    private void toNavigator() {

        ordersCourier.showMap(orderList);

    }

    private void setDate() {
        new DatePickerDialog(getActivity(),
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }
}
