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
    private FirebaseDatabase myData;
    private DatabaseReference myRef = null;
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myData = FirebaseDatabase.getInstance();
        myRef = myData.getReference("users").child(user.getUid()).child("contactList");


            //DatabaseReference contactRef = myRef.child("users").child(user.getUid()).child("contactList");
        //myRef.child(user.getUid()).child("contactList");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    fname.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String name = postSnapshot.getValue(String.class);
                        fname.add(name);
                    }
                    Collections.sort(fname);
                    adapter.notifyDataSetChanged();
//                    GenericTypeIndicator<List<String>> gti = new GenericTypeIndicator<List<String>>() {};
//                    List<String> myList = dataSnapshot.getValue(gti);
//                    for (int i = 0; i < myList.size(); i++) {
//                        fname.add(myList.get(i));
//                    }
//                    fname.addAll(myList);
//                    Collections.sort(fname);
//                    ListView list = (ListView) findViewById(R.id.contactList);
//                    adapter = new android.widget.ArrayAdapter(ContactsActivity.this,
//                            android.R.layout.simple_list_item_1, fname);
//                    list.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



//        String email = user.getEmail();
//
//        if (email.equals("david.babb81@gmail.com")) {
//
//                fname.add("Addison Toscani");
//                fname.add("Seth Howell");
//                fname.add("Trevor Lawyer");
//
//        }
//        else if (email.equals("devtrev88@gmail.com") || email.equals("trevorlawyer@gmail.com")) {
//
//                fname.add("Addison Toscani");
//                fname.add("David Babb");
//                fname.add("Seth Howell");
//
//        }
//        else if (email.equals("sonobyani@gmail.com")) {
//
//                fname.add("Addison Toscani");
//                fname.add("David Babb");
//                fname.add("Trevor Lawyer");
//
//        }
//        else {
//
//                fname.add("David Babb");
//                fname.add("Seth Howell");
//                fname.add("Trevor Lawyer");
//
//        }

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