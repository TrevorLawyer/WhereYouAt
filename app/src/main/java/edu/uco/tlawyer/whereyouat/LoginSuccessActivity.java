package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

public class LoginSuccessActivity extends Activity implements OnMapReadyCallback {
    GoogleMap gmap;
    MapFragment mmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        mmap = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.trackmap);
        mmap.getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);



    }

    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contacts:
                Intent intent = new Intent(LoginSuccessActivity.this, ContactsActivity.class);
                startActivity(intent);
                return true;
            case R.id.requests:
                Intent reqIntent = new Intent(LoginSuccessActivity.this, RequestActivity.class);
                startActivity(reqIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}