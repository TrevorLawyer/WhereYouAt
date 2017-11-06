package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by trevo on 11/5/2017.
 */

public class NewContactActivity extends Activity {
    String firstName, lastName, email, phone;
    String result;
    EditText fname, lname;
    ArrayList<String> stringRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        stringRef = getIntent().getStringArrayListExtra("Data");
        fname = (EditText) findViewById(R.id.ContactFirstName);
        lname = (EditText) findViewById(R.id.ContactLastName);
        Button addContact = (Button) findViewById(R.id.addContactConfirm);
        addContact.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                firstName = fname.getText().toString();
                lastName = lname.getText().toString();
                result = firstName + " " + lastName;
                Intent intent = new Intent();
                intent.putExtra("RESULT", result);
                setResult(RESULT_OK, intent);
                finish();

            }
        });


    }

}
