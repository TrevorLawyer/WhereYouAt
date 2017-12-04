package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SettingsActivity extends Activity {
    private Button reset;
    private Button signOut;
    private Button contactsupport;
    private Button profilePic;
    private Button changeUsername;
    int RESULT_LOAD_IMG = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        reset = (Button)findViewById(R.id.reset);
        signOut = (Button) findViewById(R.id.signOut);
        profilePic =(Button) findViewById(R.id.picture);
        contactsupport = (Button) findViewById(R.id.settingSupport);
        changeUsername = (Button) findViewById(R.id.username_button);


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

            }

        });

        changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ChangeUsernameActivity.class);
                startActivity(intent);
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, PasswordResetActivity.class);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("Do you want to signout of WhereYouAt?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                                finish();
                                LoginSuccessActivity.instance.finish();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        contactsupport.setOnClickListener(new View.OnClickListener() {
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
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                uploadFile(bitmap);
            }
            catch(IOException io){}

        }else {
            Toast.makeText(SettingsActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }
    private void uploadFile(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://mobileapps-final.appspot.com/UserPics");
        StorageReference mountainImagesRef = storageRef.child(user.getUid() + "/" + "userpic" + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(SettingsActivity.this, "Profile Picture Not Updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SettingsActivity.this, "Profile Picture Updated", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
