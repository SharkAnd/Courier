package dell.courier.courier.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import dell.courier.R;

public class MapCourierFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

   // private MapView mapView;
    private GoogleMap mMap;
    private SupportMapFragment fragment;
    private List<LatLng> places = new ArrayList<>();
    private Bundle mapViewBundleB = null;
    private SupportMapFragment mapFragment = new SupportMapFragment();

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public MapCourierFragment() {
    }

    public static MapCourierFragment newInstance(){
        return new MapCourierFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        fragment.onCreate(savedInstanceState);
      //  fragment.getMapAsync(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courier_map, container, false);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragment = new SupportMapFragment();
        transaction.add(R.id.frag_container,fragment).commit();

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
//        mapFragment.getChildFragmentManager();
        //mapView = view.findViewById(R.id.mapC);
        fragment.onCreateView(inflater,container,mapViewBundle);



        return view;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        fragment.onSaveInstanceState(mapViewBundle);
    }



    @Override
    public void onResume() {
        super.onResume();
       // fragment.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragment.onDestroyView();
    }

    @Override
    public void onStart() {
        fragment.onStart();
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        fragment.onStop();
    }

    @Override
    public void onPause() {
        fragment.onPause();
        super.onPause();

    }

    @Override
    public void onDestroy() {
        fragment.onDestroy();
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        fragment.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragment.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this::onMapLongClick);
    }

    @Override
    public void onMapLongClick(com.google.android.gms.maps.model.LatLng latLng) {

      //  LatLng latLng1 = new LatLng(latLng.latitude,latLng.longitude);
      //  GeocodingResult[] result = new GeocodingResult[0];

        mMap.addMarker(new MarkerOptions().position(latLng).title("aa"));
        places.add(new LatLng(latLng.latitude,latLng.longitude));

    }

}
