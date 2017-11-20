package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.content.Intent;
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

//import android.support.annotation.NonNull;

public class MainActivity extends Activity {

    // Declare Varibles
    EditText usernameInput, passwordInput;
    Button registerButton, signinButton;

    //firebase Auth
    private static FirebaseAuth singinAuth;
    //user contact info
    UserClass userInfo;

    //Strings
    String inputUsername, inputPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Initalize variables EditText
        usernameInput = (EditText) findViewById(R.id.EditTextUsername);
        passwordInput = (EditText) findViewById(R.id.EditTextPassword);
        //Initalize variables Buttons
        registerButton = (Button) findViewById(R.id.ButtonRegister);
        signinButton = (Button) findViewById(R.id.ButtonLogin);

        //Auth
        singinAuth = FirebaseAuth.getInstance();
        // contact info
        userInfo = new UserClass();


        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //sets varables that were entered in
                inputUsername = usernameInput.getText().toString().trim();
                inputPassword = passwordInput.getText().toString();

                //Toast.makeText(MainActivity.this, "Email: " + inputUsername, Toast.LENGTH_SHORT).show();

                //checks if password and username are not empty
                checkEmail(inputUsername, inputPassword);



            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // String key = dbRef.push().getKey();

                // dbRef.child(key).setValue("hwllow, jkldsj");


                // dbRef.push();
                Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
                startActivityForResult(intent, 2);

            }
        });

    }

    public void checkEmail(String emailX, String passwordX) {

        passwordX = "000000000";
        if (!emailX.equals("")) {
            //authenticats user infomation and password
            singinAuth.createUserWithEmailAndPassword(emailX, passwordX)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //if successful user added to database
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete();
                                //insert into database
                                //firebaseDatabase.child("users").child(user.getUid()).setValue(userInfo);
                                //task is successful
                                Toast.makeText(MainActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();

                            } else {
                                // failed
                               // Toast.makeText(MainActivity.this, "Email is Good", Toast.LENGTH_SHORT).show();
                                emailIsGood();
                                //Toast.makeText(MainActivity.this, "return type  " + typeToReturn[0], Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

        } else {
            Toast.makeText(MainActivity.this, "Fill In Email", Toast.LENGTH_SHORT).show();

        }
    }

    public void emailIsGood() {

        //sets varables that were entered in
        inputUsername = usernameInput.getText().toString().trim();
        inputPassword = passwordInput.getText().toString();

        if (inputPassword.length() < 6) {
                        Toast.makeText(MainActivity.this, "Invalid Password Length", Toast.LENGTH_SHORT).show();
        } else {

                        //checks if user password matches user email
                        singinAuth.signInWithEmailAndPassword(inputUsername, inputPassword)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //user password and email match
                                        if (task.isSuccessful()) {

                                            //check password and username before proceeding to login page
                                            Intent intent = new Intent(MainActivity.this, LoginSuccessActivity.class);
                                            intent.putExtra("USERNAME", inputUsername);
                                            intent.putExtra("PASSWORD", inputPassword);
                                            startTrackerService();
                                            startActivityForResult(intent, 1);


                                        } else {
                                            // password does not match email
                                            Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
        }

    }

    private void startTrackerService() {
        Intent i = new Intent(this, TrackerService.class);
        i.putExtra("USER", inputUsername.toString());
        i.putExtra("PASSWORD", inputPassword.toString());
        startService(i);
        finish();
    }

    public void passwordReset(View v) {

        final String emailAddress = usernameInput.getText().toString();
        String passwordX = "000000000";

        if (!emailAddress.equals("")) {
            //authenticats user infomation and password
            singinAuth.createUserWithEmailAndPassword(emailAddress, passwordX)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //if successful user added to database
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                //deletes current user
                                user.delete();
                                Toast.makeText(MainActivity.this, "No Email Exist ", Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, "Create New Account ", Toast.LENGTH_SHORT).show();

                            } else {
                                // call function to send email
                                sendEmailReset(emailAddress);
                            }
                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "Fill In Email", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendEmailReset(String Email){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //reset password part
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //sends email to user to reset password
        auth.sendPasswordResetEmail(Email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Password Reset has been Emailed ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}
