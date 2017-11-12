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

                        singinAuth.signInWithEmailAndPassword(inputUsername,inputPassword)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){

                                            //check password and username before proceeding to login page
                                            Intent intent = new Intent(MainActivity.this, LoginSuccessActivity.class);
                                            intent.putExtra("USERNAME", inputUsername);
                                            intent.putExtra("PASSWORD", inputPassword);
                                            startTrackerService();
                                            startActivityForResult(intent, 1);


                                        }
                                        else{
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

    }
    private void startTrackerService() {
        Intent i = new Intent(this, TrackerService.class);
        i.putExtra("USER", inputUsername.toString());
        i.putExtra("PASSWORD", inputPassword.toString());
        startService(i);
        finish();
    }
}
