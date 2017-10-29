package edu.uco.tlawyer.whereyouat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class RegisterUserActivity extends AppCompatActivity {

    EditText regPassword, regNumber, regUsername, confirmPass;
    Button regRegister;
    String passwordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        regNumber = (EditText) findViewById(R.id.EditTextNumber2);
        regUsername = (EditText) findViewById(R.id.EditTextUsername2);
        regPassword = (EditText) findViewById(R.id.EditTextPassword2);
        confirmPass = (EditText) findViewById(R.id.EditTextConfirmPassword);


    }
}
