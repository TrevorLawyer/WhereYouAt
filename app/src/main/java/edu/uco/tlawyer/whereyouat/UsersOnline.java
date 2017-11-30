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

public class UsersOnline extends Activity {

    DatabaseReference userOnlineRef;
    DatabaseReference currUserRef;
    DatabaseReference counterRef;
    FirebaseRecyclerAdapter<UserClass, UsersOnlineViewHolder> adapter;
    RecyclerView userOnlineList;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_online);

        userOnlineList = (RecyclerView) findViewById(R.id.onlineList);
        userOnlineList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        userOnlineList.setLayoutManager(layoutManager);

        userOnlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        counterRef = FirebaseDatabase.getInstance().getReference("lastOnline");
        currUserRef = FirebaseDatabase.getInstance().getReference("lastOnline")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

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
                    Log.d("LOG", ""+ user.getEmail() + " is now "+user.getStatus());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
