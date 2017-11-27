package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeUsernameActivity extends Activity {

    EditText currentUsernameEdit;
    TextView currentUsername;
    Button confirmNewUsername;
    FirebaseUser currentUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    String newUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        confirmNewUsername = (Button) findViewById(R.id.confirmChange);
        currentUsernameEdit = (EditText) findViewById(R.id.usernameEdit);
        newUsername = currentUsernameEdit.getText().toString();
        currentUsername = (TextView) findViewById(R.id.usernameView);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("users").child(currentUser.getUid()).child("username");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    String name = dataSnapshot.getValue(String.class);
                    currentUsername.setText(name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        confirmNewUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUsername = currentUsernameEdit.getText().toString();
                if (!newUsername.equals("")) {
                    if (newUsername.length() < 4) {
                        Toast.makeText(ChangeUsernameActivity.this,
                                "Username must be at least 4 characters in length", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        mDatabase.getReference("users").child(currentUser.getUid())
                                .child("username").setValue(newUsername);
                        Toast.makeText(ChangeUsernameActivity.this,
                                "Username changed successfully!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangeUsernameActivity.this, "Nothing Entered",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}