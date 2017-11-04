package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends Activity {
    Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        reset = (Button)findViewById(R.id.reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, PasswordResetActivity.class);
                startActivity(intent);
            }
        });

    }
}
