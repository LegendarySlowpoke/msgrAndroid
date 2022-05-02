package com.restur.msgrtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.restur.msgrtest.consts.ApplicationData;

public class PostRegistration extends AppCompatActivity {

    private TextView idBox;
    private TextView tagBox;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_registration);

        //Initialize
        this.idBox = findViewById(R.id.postRegistrationLabelDataId);
        this.tagBox = findViewById(R.id.postRegistrationLabelDataTag);
        this.continueButton = findViewById(R.id.postRegistrationContinueButton);

        idBox.setText(String.valueOf(ApplicationData.getOwner().getId()));
        tagBox.setText(ApplicationData.getOwner().getUserTAG());

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(PostRegistration.this, ChatListActivity.class));
            }
        });

    }
}