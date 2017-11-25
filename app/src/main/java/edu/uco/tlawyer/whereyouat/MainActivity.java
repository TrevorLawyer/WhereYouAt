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
    private FirebaseAuth singinAuth;

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


        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //sets varables that were entered in
                inputUsername = usernameInput.getText().toString().trim();
                inputPassword = passwordInput.getText().toString();

                //Toast.makeText(MainActivity.this, "Email: " + inputUsername, Toast.LENGTH_SHORT).show();
                checkEmail(inputUsername);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //forward to create new account
                Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
                startActivityForResult(intent, 2);

            }
        });

    }

    public void checkEmail(final String emailX) {

        String password2 = "0000000000";

        //checks if email is empty
        if(!emailX.equals("")) {

            if(!inputUsername.contains("@gmail.com")){
                inputUsername = inputUsername.concat("@gmail.com");
                //Toast.makeText(MainActivity.this, "Email: " + inputUsername, Toast.LENGTH_SHORT).show();
            }

            //authenticats user email by seeing if it allows to create an account
            singinAuth.createUserWithEmailAndPassword(emailX, password2)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //account create user does not exist.
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                //delete user just created
                                user.delete();
                                //prompt user to create account
                                Toast.makeText(MainActivity.this, "No Email Exist ", Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, "Create New Account ", Toast.LENGTH_SHORT).show();
                            } else {
                                // if failed, email already exist and move on to verify passoword
                                //Toast.makeText(MainActivity.this, "Loggin In", Toast.LENGTH_SHORT).show();
                                signUserIn(emailX);
                            }

                        }
                    });
        }
        else{
            Toast.makeText(MainActivity.this, "Fill Out Email Address", Toast.LENGTH_SHORT).show();
        }
    }
    public void signUserIn(String email){

        inputPassword = passwordInput.getText().toString();

        if(!inputPassword.equals("")) {
            //checks if user password matches user email
            singinAuth.signInWithEmailAndPassword(email, inputPassword)
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
        }else{
            Toast.makeText(MainActivity.this, "Fill Out Password", Toast.LENGTH_SHORT).show();
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

        String password2 = "0000000000";

        //gets email address
        String inputUsername = usernameInput.getText().toString().trim();

        //checks if email is empty and is a gmail account
        if(!inputUsername.equals("")) {

            //authenticats user email by seeing if it allows to create an account
            singinAuth.createUserWithEmailAndPassword(inputUsername, password2)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //account create user does not exist.
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                //delete user just created
                                user.delete();
                                //prompt user to create account
                                Toast.makeText(MainActivity.this, "No Email Exist ", Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, "Create New Account ", Toast.LENGTH_SHORT).show();
                            } else {
                                // if failed, email already exist and move on to verify passoword
                                //Toast.makeText(MainActivity.this, "Loggin In", Toast.LENGTH_SHORT).show();
                                sentReset();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(MainActivity.this, "Fill Out Email Address", Toast.LENGTH_SHORT).show();
        }

    }
    public void sentReset(){
        //reset password part
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = usernameInput.getText().toString();

        //sends email to user to reset password
        auth.sendPasswordResetEmail(emailAddress)
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
