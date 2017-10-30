package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by trevo on 10/29/2017.
 */

public class MessengerActivity extends Activity implements
        GoogleApiClient.OnConnectionFailedListener{



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
