package dell.courier;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dell.courier.client.ClientActivity;
import dell.courier.courier.CourierActivity;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String status = loadStatus();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && status.equals("courier")){
            startActivity(new Intent(this, CourierActivity.class));
        } else if (user != null && status.equals("client")){
            startActivity(new Intent(this, ClientActivity.class));
        } else {
            startActivity(new Intent(this,MainActivity.class));
        }
        finish();
    }
}
