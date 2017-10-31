package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterUserActivity extends Activity {

    EditText regPassword, regEmail, regUsername, confirmPass;
    Button regRegister;
    String email, username, password, passwordCheck;
    Boolean emailTest, usernameTest, passwordTest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //declare variables
        regEmail = (EditText) findViewById(R.id.EditTextEmail);
        regUsername = (EditText) findViewById(R.id.EditTextUsername2);
        regPassword = (EditText) findViewById(R.id.EditTextPassword2);
        confirmPass = (EditText) findViewById(R.id.EditTextConfirmPassword);
        regRegister = (Button) findViewById(R.id.registerButtonRegister);


        regRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //intialize stings values
                email = regEmail.getText().toString();
                password = regPassword.getText().toString();
                passwordCheck = confirmPass.getText().toString();

                //calls functions
                checkEmail(email);
                checkPassword(password);

                if (emailTest == true && passwordTest == true) {
                    Toast.makeText(RegisterUserActivity.this, "Valid Registration", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    //checks if email meets criteria
    public String checkEmail(String email) {

        //intialize stings values
        email = regEmail.getText().toString();
        //length is working
        //System.out.println("Email length : " + email.length());
        //checks if email contains @
        emailTest = false;
        if (email.contains("@") && email.contains(".com")) {
            if (email.length() > 6) {
                //Toast.makeText(RegisterUserActivity.this, "Valid Email", Toast.LENGTH_SHORT).show();
                emailTest = true;
            } else
                Toast.makeText(RegisterUserActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(RegisterUserActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
        }
        return email;
    }

    //checks if passwords were typed the same
    public String checkPassword(String password) {

        passwordTest = false;

        if (password.equals(passwordCheck)) {

            if (password.length() > 5) {
                passwordTest = true;
                return password;
            } else {
                Toast.makeText(RegisterUserActivity.this, "Min 6 Pass Chars", Toast.LENGTH_SHORT).show();
                return password;
            }

        }
        Toast.makeText(RegisterUserActivity.this, "Password Dont Match", Toast.LENGTH_SHORT).show();
        return password;
    }
}
