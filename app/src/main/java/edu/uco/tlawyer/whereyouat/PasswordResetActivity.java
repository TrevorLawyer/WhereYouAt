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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordResetActivity extends Activity {
    String email,currentPass,newPass1,newPass2;
    EditText uCurrent,uPass1,uPass2;
    Button confirm;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        uCurrent = (EditText)findViewById(R.id.currentPass);
        uPass1 = (EditText)findViewById(R.id.newPass1);
        uPass2 = (EditText)findViewById(R.id.newPass2);

        confirm = (Button)findViewById(R.id.confirmButton);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPass = uCurrent.getText().toString();
                newPass1 = uPass1.getText().toString();
                newPass2 = uPass2.getText().toString();
                if (uPass1.length() > 6 && uPass2.length() > 6) {
                    if (newPass1.equals(newPass2)) {
                        AuthCredential credential = EmailAuthProvider.getCredential(email, currentPass);
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(newPass1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(PasswordResetActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(PasswordResetActivity.this, "ERROR PASSWORD NOT UPDATED", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(PasswordResetActivity.this, "AUTH FAILED", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                } else {
                    Toast.makeText(PasswordResetActivity.this, "INVALD PASSWORD LENGTH", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }
}
