package dell.courier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ScrollView scrollView;
   // private FirebaseAuth mAuth;
    private long back_pressed;
    //private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    private EditText emailSingIn;
    private EditText passwordSingIn;
  //  private EditText mFirstName;
  //  private EditText mLastName;
  //  private EditText mPhoneNumber;
  //  private EditText mEmail;
  //  private EditText mPassword;
    private LinearLayout registrationFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView = findViewById(R.id.scrollView);
        scrollView.setVerticalScrollBarEnabled(false);
        registrationFrame = findViewById(R.id.registration_frame);
        emailSingIn = findViewById(R.id.email_singIn);
        passwordSingIn = findViewById(R.id.password_singIn);
        mFirstName = findViewById(R.id.first_name);
        mLastName = findViewById(R.id.last_name);
        mPhoneNumber = findViewById(R.id.phone_number);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.registration_button).setOnClickListener(this);
        findViewById(R.id.save_personal_info_button).setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button){
            attemptLogin();
            //singIn(emailSingIn.getText().toString(),passwordSingIn.getText().toString(),getBaseContext());
        } else if (view.getId() == R.id.registration_button){
            scrollView.setVisibility(View.GONE);
            registrationFrame.setVisibility(View.VISIBLE);
        } else if (view.getId() == R.id.save_personal_info_button){
            createAccount(mEmail.getText().toString(),mPassword.getText().toString(),getBaseContext());
        }
    }

    @Override
    public void onBackPressed() {
        if (registrationFrame.isShown()){
            scrollView.setVisibility(View.VISIBLE);
            registrationFrame.setVisibility(View.GONE);
        } else if (back_pressed + 2000 < System.currentTimeMillis()){
            Toast.makeText(this, "Нажмите еще раз для выхода!", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
        back_pressed = System.currentTimeMillis();
    }


    private void attemptLogin() {

        emailSingIn.setError(null);
        passwordSingIn.setError(null);

        String email = emailSingIn.getText().toString();
        String password = passwordSingIn.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check password
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordSingIn.setError("error_invalid_password");
            focusView = passwordSingIn;
            cancel = true;
        }

        // Check email
        if (TextUtils.isEmpty(email)) {
            emailSingIn.setError("error_field_required");
            focusView = emailSingIn;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailSingIn.setError("error_invalid_email");
            focusView = emailSingIn;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            singIn(email,password,getBaseContext());
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
    /*
    //вход
    private void singIn(String email, String password){

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        hideProgressDialog();
                        checkCourier(task.getResult().getUser());
                    } else {
                        // If sign in fails
                        hideProgressDialog();
                        Toast.makeText(MainActivity.this, "Ошибка! Вход не выполнен",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //создание нового профиля
    private void createAccount (String email, String password){

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    hideProgressDialog();
                    if (task.isSuccessful()){
                        writeNewClient(task.getResult().getUser());
                        startActivity(new Intent(MainActivity.this,ClientActivity.class));
                    } else {
                        Toast.makeText(MainActivity.this, "Failed creating",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // проверка курьера
    private void checkCourier(final FirebaseUser currentUser){
        final String[] status = new String[1];
        myRef.child("courier").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(currentUser.getUid()).getValue()!=null) {
                    saveStatus("courier");
                    startActivity(new Intent(MainActivity.this,CourierActivity.class));
                } else {
                    saveStatus("client");
                    startActivity(new Intent(MainActivity.this,ClientActivity.class));
                }
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // запись нового аккаунта клиента
    private void writeNewClient(FirebaseUser user){

        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String phone = mPhoneNumber.getText().toString();
        String email = mEmail.getText().toString();

        Client client = new Client(user.getUid(),firstName,lastName,phone,email);

        myRef.child("clients").child(user.getUid()).setValue(client);

        saveStatus("client");
    }
*/
}
