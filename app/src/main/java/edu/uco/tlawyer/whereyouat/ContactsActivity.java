package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

public class ContactsActivity extends Activity {
    String fname[]={"Petyr","Robert","Ramsey","Sandor","Khal","Tormund","Theon",
            "Jaime","Oberyn","Jorah","Daario","Podrick","Tyene","Davos",
            "Jon","Arya","Daenerys","Samwell","Brienne","Margaery"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Arrays.sort(fname);
        ListView list = (ListView) findViewById(R.id.contactList);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, fname);
        list.setAdapter(adapter);
    }
}
