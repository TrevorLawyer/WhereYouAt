package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUserActivity extends Activity {

    EditText regPassword, regEmail, regPhoneNumber, confirmPass, username;
    Button regRegister;
    String email, phoneNumber, password, passwordCheck , usernameString;
    Boolean emailTest, phoneTest, passwordTest, usernameBool = false;


    //athentication
    private FirebaseAuth auth;

    //firebase database
    DatabaseReference firebaseDatabase;
    //user contact info
    UserClass userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //declare variables
        regEmail = (EditText) findViewById(R.id.EditTextEmail);
        regPhoneNumber = (EditText) findViewById(R.id.EditPhoneNumber);
        regPassword = (EditText) findViewById(R.id.EditTextPassword2);
        confirmPass = (EditText) findViewById(R.id.EditTextConfirmPassword);
        regRegister = (Button) findViewById(R.id.registerButtonRegister);
        username = (EditText) findViewById(R.id.EditTextUsername2);

        // athentication intialization
        auth = FirebaseAuth.getInstance();
        // database intialization
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        // contact info
        userInfo = new UserClass();

        regRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //intialize stings values
                email = regEmail.getText().toString().trim();
                password = regPassword.getText().toString();
                passwordCheck = confirmPass.getText().toString();
                phoneNumber = regPhoneNumber.getText().toString();
                usernameString = username.getText().toString();

                //calls functions
                checkEmail(email);
                checkPhoneNumber(phoneNumber);
                checkUsername(usernameString);
                checkPassword(password);

                //insert into database
//                DatabaseReference conditionRef = firebaseDatabase.child("condition");
//                conditionRef.setValue("cold");


//                //insert into database
//                firebaseDatabase.child("user").child("dd").setValue("hello");


                if (emailTest == true && passwordTest == true && phoneTest == true && usernameBool == true) {


                    // Toast.makeText(RegisterUserActivity.this, "Valid Registration", Toast.LENGTH_SHORT).show();

                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterUserActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        userInfo.setEmail(email);
                                        userInfo.setPhoneNumber(phoneNumber);
                                        userInfo.setUsername(usernameString);



                                        //insert into database
                                        firebaseDatabase.child("users").child(user.getUid()).setValue(userInfo);

                                        //taks is successful
                                        Toast.makeText(RegisterUserActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // failed
                                        Toast.makeText(RegisterUserActivity.this, "Account With Email Already Exist", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }

            }

            private void updateUI(FirebaseUser user) {
            }
        });


    }

    //checks if username is valid
    public void checkUsername(String x){
        //special characters
        String specialCharacters=" !#$%&'()*+,-./:;<=>?@[]^_`{|}~";

        for(int i = 0; i < x.length(); i++) {

            //test if evey value of username is in special Chars, converts char to string
            if (specialCharacters.contains(Character.toString(x.charAt(i)))) {
                Toast.makeText(RegisterUserActivity.this, "Username Contains Special Chars", Toast.LENGTH_SHORT).show();
                usernameBool = false;
                return;
            }
        }
        if(x.equals("") || x.isEmpty() ){
            Toast.makeText(RegisterUserActivity.this, "Username is Empty", Toast.LENGTH_SHORT).show();
            usernameBool = false;
            return;
        }
        usernameBool = true;
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

    public void checkPhoneNumber(String number) {
        if (number.length() != 10 || number.isEmpty() || number.equals("")) {
            phoneTest = false;
            Toast.makeText(RegisterUserActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        phoneTest = true;

    }

//    //checks if user is signed in
//    @Override
//    public void onStart(){
//        super.onStart();
//        //checks if user is singed in (non-nul)
//        FirebaseUser currentUser = auth.getCurrentUser();
//        updateUI(currentUser);
//    }
}
