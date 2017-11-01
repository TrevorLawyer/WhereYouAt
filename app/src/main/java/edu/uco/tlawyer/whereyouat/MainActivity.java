package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity {

    // Declare Varibles
    EditText usernameInput, passwordInput;
    Button registerButton, signinButton;

    //firebase Auth
    private FirebaseAuth fbAuth;

    //firebase database
    FirebaseDatabase database;
    DatabaseReference dbRef;
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
        fbAuth = FirebaseAuth.getInstance();

        //database intialize
        database = FirebaseDatabase.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("Artist");

        //send values to database  https://mobileapps-final.firebaseio.com/



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

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String name = user.getDisplayName();


//                        //fbAuth.signInWithCredential(showell2121);
//                        fbAuth.signInWithEmailAndPassword("showell2121", "showell2121")
//                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        if (task.isSuccessful()) {
//                                            // Sign in success, update UI with the signed-in user's information
//                                            Log.d(TAG, "signInWithEmail:success");
//                                            FirebaseUser user = fbAuth.getCurrentUser();
//                                            updateUI(user);
//                                        } else {
//                                            // If sign in fails, display a message to the user.
//                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                                            Toast.makeText(MainActivity.this, "Authentication failed.",
//                                                    Toast.LENGTH_SHORT).show();
//                                            updateUI(null);
//                                        }
//
//                                        // ...
//                                    }
//                                });
                        //check password and username before proceeding to login page
                        Intent intent = new Intent(MainActivity.this, LoginSuccessActivity.class);
                        startActivityForResult(intent, 1);

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

    }
}
