package dell.courier;

import android.app.ProgressDialog;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dell.courier.client.ClientActivity;
import dell.courier.client.model.Client;
import dell.courier.courier.CourierActivity;


public class BaseActivity extends AppCompatActivity {

    public static final String STATUS_PROFILE = "status_profile";

    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
   EditText mFirstName ;
    EditText   mLastName ;
    EditText   mPhoneNumber;
    EditText  mEmail;
    EditText   mPassword ;
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //показать ProgressDialog
    public void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Загрузка...");
        }
        progressDialog.show();
    }

    //скрыть ProgressDialog
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    //сохранить статус клиента
    public void saveStatus(String status){
        preferences = getApplication().getSharedPreferences(STATUS_PROFILE,MODE_PRIVATE);
        preferences.edit().putString(STATUS_PROFILE, status).apply();
    }

    //загрузить статус клиента
    public String loadStatus(){
        preferences = getApplication().getSharedPreferences(STATUS_PROFILE,MODE_PRIVATE);
        return preferences.getString(STATUS_PROFILE,"");
    }

    public static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    //вход
    public void singIn(String email, String password, Context context){

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        hideProgressDialog();
                        checkCourier(task.getResult().getUser(),context);
                    } else {
                        // If sign in fails
                        hideProgressDialog();
                        Toast.makeText(context, "Ошибка! Вход не выполнен",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //создание нового профиля
    public void createAccount (String email, String password, Context context){

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    hideProgressDialog();
                    if (task.isSuccessful()){
                        writeNewClient(task.getResult().getUser());
                        startActivity(new Intent(context,ClientActivity.class));
                    } else {
                        Toast.makeText(context, "Failed creating",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // проверка курьера
    public void checkCourier(final FirebaseUser currentUser, Context context){
        final String[] status = new String[1];
        myRef.child("courier").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(currentUser.getUid()).getValue()!=null) {
                    saveStatus("courier");
                    startActivity(new Intent(context,CourierActivity.class));
                } else {
                    saveStatus("client");
                    startActivity(new Intent(context,ClientActivity.class));
                }
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // запись нового аккаунта клиента
    public void writeNewClient(FirebaseUser user){

        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String phone = mPhoneNumber.getText().toString();
        String email = mEmail.getText().toString();

        Client client = new Client(user.getUid(),firstName,lastName,phone,email);

        myRef.child("clients").child(user.getUid()).setValue(client);

        saveStatus("client");
    }
}