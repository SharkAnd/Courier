package dell.courier.courier;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import dell.courier.R;
import dell.courier.client.model.Order;

public class Road {


    public Road() {
    }

    public static HashMap<String, LatLng> markers(GeoApiContext context, List<Order> orderList){

        HashMap<String, LatLng> hashMap = new HashMap<>();
        LatLng latLng = new LatLng();


        for (int i=0; i<orderList.size(); i++){
            String address = orderList.get(i).getStreet()+" "+orderList.get(i).getNumber_house()+" "+orderList.get(i).getNumber_entrance();
            GeocodingResult[] result = new GeocodingResult[0];
            try {
                result = GeocodingApi.geocode(context,"Минск"+" "+address).await();
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            hashMap.put(address,result[0].geometry.location);
        }
        return hashMap;
    }

    public static PolylineOptions route(GeoApiContext context, List<LatLng> places){

        DirectionsResult res = new DirectionsResult();
        DirectionsApiRequest result = DirectionsApi.newRequest(context)
                .mode(TravelMode.DRIVING)
                .optimizeWaypoints(true)
                .origin(places.get(0))//Место старта
                .destination(places.get(places.size()-1))//Пункт назначения
                .waypoints(places.subList(1,places.size()).toArray(new com.google.maps.model.LatLng[0]));
        try {
            res = result.await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<com.google.maps.model.LatLng> path = res.routes[0].overviewPolyline.decodePath();
        PolylineOptions line = new PolylineOptions();
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

        for (int i = 0; i < path.size(); i++) {
            line.add(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
            latLngBuilder.include(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
        }
        line.width(16f).color(R.color.colorPrimary);
        return line;

    }

}
