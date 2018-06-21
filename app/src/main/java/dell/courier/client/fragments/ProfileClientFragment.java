package dell.courier.client.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dell.courier.BaseActivity;
import dell.courier.R;
import dell.courier.client.model.Client;

public class ProfileClientFragment extends Fragment {

    private ProgressBar progressBar;
    private LinearLayout layout;
    private TextView firsName;
    private TextView lastName;
    private TextView email;
    private TextView phone;
    private Client client;

    public ProfileClientFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("clients");
        final String user = BaseActivity.getUid();

        reference.child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                client = dataSnapshot.getValue(Client.class);
                if (client!=null) {
                    firsName.setText(client.firstName);
                    lastName.setText(client.lastName);
                    email.setText(client.email);
                    phone.setText(client.phone);
                } else {
                    return;
                }
                progressBar.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_client_profile, container, false);
        firsName = rootView.findViewById(R.id.client_firstName);
        lastName = rootView.findViewById(R.id.client_lastName);
        email = rootView.findViewById(R.id.client_email);
        phone = rootView.findViewById(R.id.client_phone);
        progressBar = rootView.findViewById(R.id.progress_profile_layout);
        layout = rootView.findViewById(R.id.client_profile_layout);
        return rootView;
    }
}