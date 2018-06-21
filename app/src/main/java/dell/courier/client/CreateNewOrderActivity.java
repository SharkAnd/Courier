package dell.courier.client;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dell.courier.BaseActivity;
import dell.courier.R;
import dell.courier.client.model.Order;

public class CreateNewOrderActivity extends BaseActivity implements View.OnClickListener{

    private String year;
    private String monthOfYear;
    private String dayOfMonth;
    private Spinner mGoods;
    private EditText street;
    private EditText houseNumber;
    private EditText entranceNumber;
    private EditText apartmentNumber;
    private Button date;

    private FloatingActionButton mFab;
    private Calendar calendar = Calendar.getInstance();
    private Calendar now = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener d;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = mDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_order);

        mGoods = findViewById(R.id.goods);
        street = findViewById(R.id.street);
        houseNumber = findViewById(R.id.number_house);
        entranceNumber = findViewById(R.id.number_entrance);
        apartmentNumber = findViewById(R.id.number_apartment);
        date = findViewById(R.id.button_date);
        mFab = findViewById(R.id.save_order);
        date.setOnClickListener(this);


        mFab.setOnClickListener((view -> {
            writeNewOrder();
            finish();
        }));

        d = (view, year, monthOfYear, dayOfMonth) -> {
            this.year = String.valueOf(year);
            this.monthOfYear = String.valueOf(monthOfYear);
            this.dayOfMonth = String.valueOf(dayOfMonth);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            date.setText(DateUtils.formatDateTime(this,
                    calendar.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
        };

    }

    private void writeNewOrder() {

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyCqYbIw8QZKxe0F-DnlbQqaKYrzvgAeiBE")
                .build();


        String address = "Минск"+" "+street.getText().toString()+" "+houseNumber.getText().toString()+" "+entranceNumber.getText().toString();

        GeocodingApiRequest request = GeocodingApi.newRequest(context).address(address);

        request.setCallback(new PendingResult.Callback<GeocodingResult[]>() {
            @Override
            public void onResult(GeocodingResult[] result) {
                String goods = mGoods.getSelectedItem().toString();
                Order order = new Order("не доставлен", getUid(), now.getTimeInMillis(),calendar.getTimeInMillis(),goods,1,street.getText().toString(),Integer.valueOf(houseNumber.getText().toString()),entranceNumber.getText().toString(),Integer.valueOf(apartmentNumber.getText().toString()),result[0].geometry.location);
                String key = myRef.child("clients").child(getUid()).child("orders").push().getKey();
                Map<String,Object> child = new HashMap<>();
                child.put("/clients/"+getUid()+"/orders/"+key,order);
                child.put("/orders/"+year+"/"+monthOfYear+"/"+dayOfMonth+"/"+key,order);
                myRef.updateChildren(child);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });



    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id==R.id.button_date){
            setDate();
        }
    }

    private void setDate(){
        new DatePickerDialog(this,
                d,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }


}
