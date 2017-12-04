package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsersOnline extends Activity { //implements GoogleApiClient.ConnectionCallbacks,
    //GoogleApiClient.OnConnectionFailedListener,
        //LocationListener{

    DatabaseReference userOnlineRef;
    DatabaseReference currUserRef;
    DatabaseReference counterRef;
    DatabaseReference locationRef;
    FirebaseRecyclerAdapter<UserClass, UsersOnlineViewHolder> adapter;
    RecyclerView userOnlineList;
    RecyclerView.LayoutManager layoutManager;
//    private static final int MY_PERMISSION_REQUEST_CODE = 77;
//    private static final int PLAY_SERVICES_REQUEST = 78;
//    private static int UPDATE_INTERVAL = 5000;
//    private static int FASTEST_INTERVAL = 3000;
//    private static int DISTANCE = 10;
//    private LocationRequest mLocationRequest;
//    private FusedLocationProviderClient mFusedLocationProviderClient;
//    private GoogleApiClient mGoogleApiClient;
//    private Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_online);

        userOnlineList = (RecyclerView) findViewById(R.id.onlineList);
        userOnlineList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        userOnlineList.setLayoutManager(layoutManager);

        userOnlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        locationRef = FirebaseDatabase.getInstance().getReference("locations");
        counterRef = FirebaseDatabase.getInstance().getReference("lastOnline");
        currUserRef = FirebaseDatabase.getInstance().getReference("lastOnline")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
//                (this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[] {
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_PERMISSION_REQUEST_CODE);
//        }
//        else {
//            if (checkServices()) {
//                buildGoogleApiClient();
//                createLocationRequest();
//                displayLocation();
//            }
//        }

        setPresence();
        updateOnlineList();
    }

    private void updateOnlineList() {
        adapter = new FirebaseRecyclerAdapter<UserClass, UsersOnlineViewHolder>(UserClass.class,
                R.layout.users_online_layout, UsersOnlineViewHolder.class, counterRef) {
            @Override
            protected void populateViewHolder(UsersOnlineViewHolder viewHolder, UserClass model, int position) {
                viewHolder.emailText.setText(model.getEmail());
            }
        };
        adapter.notifyDataSetChanged();
        userOnlineList.setAdapter(adapter);
    }

    private void setPresence() {

        userOnlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Boolean.class)) {
                //if (dataSnapshot != null) {
                    currUserRef.onDisconnect().removeValue();
                    counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue
                            (new UserClass(FirebaseAuth.getInstance().getCurrentUser().getEmail(), "online"));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        counterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserClass user = postSnapshot.getValue(UserClass.class);
                    Log.d("LOG", "" + user.getEmail() + " is now " + user.getStatus());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

//    private boolean checkServices() {
//        GoogleApiAvailability gApi = GoogleApiAvailability.getInstance();
//        int result = gApi.isGooglePlayServicesAvailable(this);
//        if (result != ConnectionResult.SUCCESS) {
//            if (gApi.isUserResolvableError(result)) {
//                gApi.getErrorDialog(this, result, PLAY_SERVICES_REQUEST).show();
//            }
//            else {
//                Toast.makeText(this, "Device Not Supported", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    private void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        displayLocation();
//        startLocationUpdates();
//    }
//
//    private void startLocationUpdates() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
//                (this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSION_REQUEST_CODE: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (checkServices()) {
//                        buildGoogleApiClient();
//                        createLocationRequest();
//                        displayLocation();
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        mLastLocation = location;
//        displayLocation();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.disconnect();
//        }
//        super.onStop();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        checkServices();
//    }
//
//    private void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
//        mLocationRequest.setSmallestDisplacement(DISTANCE);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//    private void displayLocation() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
//                (this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        mFusedLocationProviderClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//                            locationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                    .setValue(new TrackedUser(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
//                                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
//                                            String.valueOf(mLastLocation.getLatitude()),
//                                            String.valueOf(mLastLocation.getLongitude())));
//                        }
//                    }
//                });
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//            locationRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                    .setValue(new TrackedUser(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
//                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
//                            String.valueOf(mLastLocation.getLatitude()),
//                            String.valueOf(mLastLocation.getLongitude())));
//        }
//        else {
//            Toast.makeText(this, "Couldn't get location data", Toast.LENGTH_SHORT).show();
//        }
//    }
}
