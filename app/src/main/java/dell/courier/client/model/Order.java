package dell.courier.client.model;


import com.google.firebase.database.IgnoreExtraProperties;
import com.google.maps.model.LatLng;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class Order {

    private String status;
    private String uID;
    private long dateCreatedL;
    private long dateDelivery;
    private String goods;
    private float price;
    private String street;
    private int number_house;
    private String number_entrance;
    private int number_apartment;
    private LatLng latLng;


    public Order() {
    }

    public Order(String status, String uID, long dateCreatedL, long dateDelivery, String goods, float price, String street, int number_house, String number_entrance, int number_apartment, LatLng latLng) {
        this.status = status;
        this.uID = uID;
        this.dateCreatedL = dateCreatedL;
        this.dateDelivery = dateDelivery;
        this.goods = goods;
        this.price = price;
        this.street = street;
        this.number_house = number_house;
        this.number_entrance = number_entrance;
        this.number_apartment = number_apartment;
        this.latLng = latLng;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public long getDateCreatedL() {
        return dateCreatedL;
    }

    public void setDateCreatedL(long dateCreatedL) {
        this.dateCreatedL = dateCreatedL;
    }

    public long getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(long dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber_house() {
        return number_house;
    }

    public void setNumber_house(int number_house) {
        this.number_house = number_house;
    }

    public String getNumber_entrance() {
        return number_entrance;
    }

    public void setNumber_entrance(String number_entrance) {
        this.number_entrance = number_entrance;
    }

    public int getNumber_apartment() {
        return number_apartment;
    }

    public void setNumber_apartment(int number_apartment) {
        this.number_apartment = number_apartment;
    }
}
