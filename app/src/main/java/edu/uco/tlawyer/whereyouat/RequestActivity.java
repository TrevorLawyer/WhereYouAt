package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RequestActivity extends Activity implements RequestFragment.PickContactListener {

    private Button selectUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        selectUserButton = (Button) findViewById(R.id.selectUser);

        selectUserButton.setOnClickListener(new View.OnClickListener() {
            // Creates list of registered users from which to send a request
            @Override
            public void onClick(View view) {
                RequestFragment cFrag = new RequestFragment();
                cFrag.show(getFragmentManager(), "contacts");
            }
        });
    }

    @Override
    public void onContactClick(int i, DialogFragment dialogFragment) {
        // Sends a notification to a registered user.
        // Not fully functional until database is working.
        Notification.Builder n = new Notification.Builder(getApplicationContext());
        n.setSmallIcon(R.drawable.ic_icon_notify);
        n.setAutoCancel(true);
        n.setContentTitle("Notification from: Where You At");
        n.setContentText("You've sent a tracking request to: " + RequestFragment.users[i].getName());
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, n.build());
    }
}
