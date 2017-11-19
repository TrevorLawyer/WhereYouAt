package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends Activity {

    // Declare Varibles
    EditText usernameInput, passwordInput;
    Button registerButton, signinButton;
    TextView support;

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
        support = (TextView) findViewById(R.id.support);
        //Initalize variables Buttons
        registerButton = (Button) findViewById(R.id.ButtonRegister);
        signinButton = (Button) findViewById(R.id.ButtonLogin);

        //Auth
        singinAuth = FirebaseAuth.getInstance();


        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //sets varables that were entered in
                inputUsername = usernameInput.getText().toString();
                inputPassword = passwordInput.getText().toString();

                // System.out.println(" input :" +inputUsername + ":");

                //checks if password and username are not empty
                if (!inputPassword.matches("") && !inputUsername.matches("")) {

                    if (inputPassword.length() < 6) {
                        Toast.makeText(MainActivity.this, "Invalid Password Length", Toast.LENGTH_SHORT).show();
                    } else {

//                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        String name = user.getDisplayName();

                        singinAuth.signInWithEmailAndPassword(inputUsername, inputPassword)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            //check password and username before proceeding to login page
                                            Intent intent = new Intent(MainActivity.this, LoginSuccessActivity.class);
                                            intent.putExtra("USERNAME", inputUsername);
                                            intent.putExtra("PASSWORD", inputPassword);
                                            startTrackerService();
                                            startActivityForResult(intent, 1);


                                        } else {
                                            // failed ot log in
                                            Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                    }

                } else {
                    Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
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
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eintent = new Intent(Intent.ACTION_SEND);
                eintent.setType("plain/text");
                eintent.putExtra(Intent.EXTRA_EMAIL, new String[] { "whereyouat.tech@gmail.com" });
                eintent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                eintent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(eintent, ""));
            }
        });

    }

    private void startTrackerService() {
        Intent i = new Intent(this, TrackerService.class);
        i.putExtra("USER", inputUsername.toString());
        i.putExtra("PASSWORD", inputPassword.toString());
        startService(i);
        finish();
    }

    public void passwordReset(View v) {
        //loop through all auth users
        int counter = 0;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //reset password part
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = usernameInput.getText().toString();
        // checks if user null
        if (user != null && !emailAddress.equals("")) {
            //Toast.makeText(MainActivity.this, "in IF ", Toast.LENGTH_SHORT).show();
            // going through all athenticated users
            for (UserInfo profile : user.getProviderData()) {
                //Toast.makeText(MainActivity.this, "in FOR ", Toast.LENGTH_SHORT).show();
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

                //if statement to check if emailed enter matches email the for loop is looking at
                if (profile.getEmail().equals(emailAddress)) {
                    //incrememnts int to show password reset is happening
                    counter++;
                    //sends email to user to reset password
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Reset Successful ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
            }
            //checks if counter was incremented by the email being in the database
            if (counter != 0) {
                Toast.makeText(MainActivity.this, "Password Reset has been Emailed ", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(MainActivity.this, "No Email Exist ", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Create New Account ", Toast.LENGTH_SHORT).show();}
        }else
            Toast.makeText(MainActivity.this, "Fill Out Email Address ", Toast.LENGTH_SHORT).show();


    }
}
