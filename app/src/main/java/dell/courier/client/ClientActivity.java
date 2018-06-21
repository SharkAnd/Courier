package dell.courier.client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import dell.courier.BaseActivity;
import dell.courier.MainActivity;
import dell.courier.R;
import dell.courier.client.fragments.OrdersClientFragment;
import dell.courier.client.fragments.ProfileClientFragment;

import static dell.courier.MainActivity.STATUS_PROFILE;

public class ClientActivity extends BaseActivity {

    private FragmentPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()){

            private final Fragment[] fragments = new Fragment[]{
                    new ProfileClientFragment(),
                    new OrdersClientFragment()
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

        mViewPager = findViewById(R.id.client_viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
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
            startActivity(new Intent(ClientActivity.this, MainActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}