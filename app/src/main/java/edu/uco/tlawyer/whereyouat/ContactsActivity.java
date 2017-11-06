package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class ContactsActivity extends Activity {
    static ArrayList<String> fname = new ArrayList<>();
    ArrayAdapter<String> adapter;
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


        fname.add("Addison Toscani");
        fname.add("David Babb");
        fname.add("Seth Hoewll");
        fname.add("Trevor Lawyer");
       Collections.sort(fname);
        ListView list = (ListView) findViewById(R.id.contactList);
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, fname);
        list.setAdapter(adapter);

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