package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ContactsActivity extends Activity {
    ArrayList<String> fname = new ArrayList<>();
    ArrayAdapter<String> adapter;
    UserClass currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_contacts);
        Button addContact = (Button) findViewById(R.id.contactButton) ;
        addContact.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(ContactsActivity.this, NewContactActivity.class);
                mIntent.putStringArrayListExtra("Data", fname);
                startActivityForResult(mIntent, 2);
                Collections.sort(fname);
                adapter.notifyDataSetChanged();
            }
        });

        currentUser = new UserClass();
//        currentUser.contactList.add("James");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ref.child("users").child(user.getUid()).child("contactList")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String databaseContact = dataSnapshot.getValue(String.class);
                        currentUser.contactList.add(databaseContact);
                        //currentUser.contactList.add(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        fname.addAll(currentUser.contactList);
//        fname.add("Addison Toscani");
//        fname.add("David Babb");
//        fname.add("Seth Howell");
//        fname.add("Trevor Lawyer");
       Collections.sort(fname);
        ListView list = (ListView) findViewById(R.id.contactList);
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, fname);
        list.setAdapter(adapter);

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestCode) {
            if (resultCode == RESULT_OK) {
                fname.add(data.getStringExtra("RESULT"));
                Collections.sort(fname);
                adapter.notifyDataSetChanged();
            }
        }
    }

}