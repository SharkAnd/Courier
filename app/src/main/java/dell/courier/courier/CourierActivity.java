package dell.courier.courier;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.MapLifecycleDelegate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressType;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dell.courier.BaseActivity;
import dell.courier.MainActivity;
import dell.courier.R;
import dell.courier.client.ClientActivity;
import dell.courier.client.fragments.OrdersClientFragment;
import dell.courier.client.fragments.ProfileClientFragment;
import dell.courier.client.model.Order;
import dell.courier.courier.fragments.MapCourierFragment;
import dell.courier.courier.fragments.OrdersCourierFragment;
import dell.courier.courier.fragments.ProfileCourierFragment;

import static dell.courier.MainActivity.STATUS_PROFILE;

public class CourierActivity extends AppCompatActivity implements OnMapReadyCallback,
        OrdersCourierFragment.OrdersCourier,
        ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener{

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    private ProgressDialog progressDialog;
    private GoogleMap map;
    private FragmentPagerAdapter mSectionsPagerAdapter;
    private CourierViewPager mViewPager;
    private CourierViewPager orderViewPager;
    private BottomNavigationView navigationView;
    private SupportMapFragment fragmentMap;
    private OrdersCourierFragment ordersCourierFragment;
    private GeoApiContext context;
    private Button addDot;
    private ImageView imgDot;
    private List<Order> orderList  = new ArrayList<>();
    private List<com.google.maps.model.LatLng> place = new ArrayList<>();
    private boolean start = true;
    private com.google.maps.model.LatLng origin;
    private com.google.maps.model.LatLng destination;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        toolbar = findViewById(R.id.toolbarC);
        setSupportActionBar(toolbar);


        addDot = findViewById(R.id.add_dot_map);
        imgDot = findViewById(R.id.img_dot_map);

        addDot.setOnClickListener(this::onClick);

        fragmentMap = SupportMapFragment.newInstance();
        fragmentMap.getMapAsync(this);

        ordersCourierFragment = new OrdersCourierFragment(map);

        mSectionsPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            private final Fragment[] fragments = new Fragment[]{
                    new ProfileCourierFragment(),
                    ordersCourierFragment,
                    fragmentMap

            };

            private final String[] titleName = new String[]{
                    getString(R.string.client_info),
                    getString(R.string.orders)
            };

            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleName[position];
            }

        };

        orderViewPager = findViewById(R.id.orders_now_view_pager);

        mViewPager = findViewById(R.id.courier_viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        navigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
            switch (item.getItemId()) {
                case R.id.profile:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.orders:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.map:
                    mViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        };
        navigationView.setOnNavigationItemSelectedListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(CourierActivity.this, MainActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //показать ProgressDialog
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Построение маршрута...");
        }
        progressDialog.show();
    }

    //скрыть ProgressDialog
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        context = new GeoApiContext.Builder()
                .apiKey("AIzaSyCqYbIw8QZKxe0F-DnlbQqaKYrzvgAeiBE")
                .build();

        map = googleMap;
        myLocation();
        View mapView = fragmentMap.getView();
        @SuppressLint("ResourceType") View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
        // and next place it, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                locationButton.getLayoutParams();
        // position on right bottom
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 0, 30, 30);
    }

    private void myLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

        }  else {
            map.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myLocation();

                } else {

                }
                return;
            }
        }
    }



    @Override
    public void showMap(List<Order> orderList) {
        navigationView.setSelectedItemId(R.id.map);
        mViewPager.setCurrentItem(2);
        addDot.setVisibility(View.VISIBLE);
        imgDot.setVisibility(View.VISIBLE);
        this.orderList = orderList;
        for (int i=0; i<orderList.size(); i++){
            place.add(orderList.get(i).getLatLng());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id==R.id.add_dot_map){
            addDotMap();
        }
    }

    private void addDotMap() {
        com.google.maps.model.LatLng latLng = new com.google.maps.model.LatLng(map.getCameraPosition().target.latitude,map.getCameraPosition().target.longitude);
        if (start){
            origin = latLng;
            start = false;
        } else {
            destination = latLng;
            route();
        }

    }

    private void route() {

        showProgressDialog();
        final DirectionsResult[] result = {null};
/*
        DirectionsApiRequest request = DirectionsApi.newRequest(context)
                .mode(TravelMode.DRIVING)
                .optimizeWaypoints(true)
                .origin(origin)//Место старта
                .destination(destination)//Пункт назначения
                .waypoints(place.subList(0,place.size()).toArray(new com.google.maps.model.LatLng[0]));

        request.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                List<com.google.maps.model.LatLng> path = result.routes[0].overviewPolyline.decodePath();
                PolylineOptions line = new PolylineOptions();
                LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
                for (int i = 0; i < path.size(); i++) {
                    line.add(new LatLng(path.get(i).lat, path.get(i).lng));
                    latLngBuilder.include(new LatLng(path.get(i).lat, path.get(i).lng));
                }
                line.width(16f).color(R.color.colorPrimary);

                runOnUiThread(() -> {
                    for (int i=0; i<orderList.size(); i++){
                        String address = orderList.get(i).getStreet()+" "+orderList.get(i).getNumber_house()+" "+orderList.get(i).getNumber_entrance()+" "+orderList.get(i).getNumber_apartment();
                        LatLng latLng = new LatLng(orderList.get(i).getLatLng().lat,orderList.get(i).getLatLng().lng);
                        map.addMarker(new MarkerOptions().position(latLng).title(address));
                    }
                    addDot.setVisibility(View.GONE);
                    imgDot.setVisibility(View.GONE);
                    map.addPolyline(line);
                    hideProgressDialog();
                });
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
*/
        new Thread(() -> {
            try {
                result[0] = DirectionsApi.newRequest(context)
                        .mode(TravelMode.DRIVING)
                        .optimizeWaypoints(true)
                        .origin(origin)//Место старта
                        .destination(destination)//Пункт назначения
                        .waypoints(place.subList(0,place.size()).toArray(new com.google.maps.model.LatLng[0]))
                        .await();
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            List<com.google.maps.model.LatLng> path = result[0].routes[0].overviewPolyline.decodePath();
            PolylineOptions line = new PolylineOptions();
            LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

            String a = result[0].geocodedWaypoints[1].placeId;

            try {
                GeocodingResult[] result1 = GeocodingApi.newRequest(context).address("минск голубева 15").await();
                com.google.maps.model.LatLng q = result1[0].geometry.location;
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            

            for (int i = 0; i < path.size(); i++) {
                line.add(new LatLng(path.get(i).lat, path.get(i).lng));
                latLngBuilder.include(new LatLng(path.get(i).lat, path.get(i).lng));
            }
            line.width(16f).color(R.color.colorPrimary);


            runOnUiThread(() -> {
                for (int i=0; i<orderList.size(); i++){
                    String address = orderList.get(i).getStreet()+" "+orderList.get(i).getNumber_house()+" "+orderList.get(i).getNumber_entrance()+" "+orderList.get(i).getNumber_apartment();
                    LatLng latLng = new LatLng(orderList.get(i).getLatLng().lat,orderList.get(i).getLatLng().lng);
                    map.addMarker(new MarkerOptions().position(latLng).title(address));
                }
                addDot.setVisibility(View.GONE);
                imgDot.setVisibility(View.GONE);
                 map.addPolyline(line);
               hideProgressDialog();
            });

        }).start();



    }

    private void orderNow(){

    }
}