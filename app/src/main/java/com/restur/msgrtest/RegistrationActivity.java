package com.restur.msgrtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.restur.msgrtest.connection.Connector;
import com.restur.msgrtest.consts.ApplicationData;
import com.restur.msgrtest.serviceWorkers.JSONWorker;
import com.restur.msgrtest.exceptions.LogInException;
import com.restur.msgrtest.models.ModelOwner;

import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {

    //Vars
    Connector connector;
    JSONObject userJSONData;

    //UI fields
    //Text views etc
    private TextView infoLabel;
    private View backLayout;

    //Buttons & text edits;
    private EditText tagBox;
    private EditText nameBox;
    private EditText lastnameBox;
    private EditText phoneBox;
    private EditText emailBox;
    private EditText passBox;
    private EditText passRepeatBox;
    private Button sendRequestButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Initialize
        backLayout = findViewById(R.id.registrationBackLayout);
        infoLabel = findViewById(R.id.registrationInfoLabel);

        tagBox = findViewById(R.id.registrationTagField);
        nameBox = findViewById(R.id.registrationNameField);
        lastnameBox = findViewById(R.id.registrationLastNameField);
        phoneBox = findViewById(R.id.registrationPhoneField);
        emailBox = findViewById(R.id.registrationEmailField);

        passBox = findViewById(R.id.registrationPassField);
        passRepeatBox = findViewById(R.id.registrationPassRepeatField);

        sendRequestButton = findViewById(R.id.registrationSubmitButton);
        backButton = findViewById(R.id.registration_back_button);

        final JSONObject[] objectToSend = new JSONObject[1];

        //On-click listeners
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFieldColors();
                infoLabel.setText("REGISTRATION");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFieldColors();
                finish();
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFieldColors();
                //Create vars
                boolean isInfoOk = true;

                String tag = tagBox.getText().toString();
                String name = nameBox.getText().toString();
                String lastname = lastnameBox.getText().toString();
                String phone = phoneBox.getText().toString();
                String email = emailBox.getText().toString();

                String password = passBox.getText().toString();
                String passwordRepeat = passRepeatBox.getText().toString();

                //Perform checks on fields
                //tag
                if (tag.equals("")) {
                    tagBox.setBackgroundColor(getResources().getColor(R.color.error_textedit_back));
                    isInfoOk = false;
                } else if (!tag.matches("[a-zA-Z0-9]+")) {
                    tagBox.setBackgroundColor(getResources().getColor(R.color.error_textedit_back));
                    isInfoOk = false;
                }
                //name
                if (name.equals("")) {
                    nameBox.setBackgroundColor(getResources().getColor(R.color.error_textedit_back));
                    isInfoOk = false;
                } else if (!name.matches("[a-zA-Z]+")) {
                    nameBox.setBackgroundColor(getResources().getColor(R.color.error_textedit_back));
                    isInfoOk = false;
                }
                //lastname
                if (!lastname.equals("")) {
                    if (!lastname.matches("[a-zA-Z]+")) {
                        lastnameBox.setBackgroundColor(getResources().getColor(R.color.error_textedit_back));
                        isInfoOk = false;
                    }
                }
                //phone
                if (!phone.equals("")) {
                    if (!phone.matches("\\+?\\d+")) {
                        phoneBox.setBackgroundColor(getResources().getColor(R.color.error_textedit_back));
                        isInfoOk = false;
                    }
                }
                //email
                if (!email.equals("")) {
                    email = email.trim();
                    if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                        emailBox.setBackgroundColor(getResources().getColor(R.color.error_textedit_back));
                        isInfoOk = false;
                    }
                }
                //passwords
                if (password.equals("") || passwordRepeat.equals("")) {
                    passBox.setBackgroundColor(getResources().getColor(R.color.error_textedit_back));
                    passRepeatBox.setBackgroundColor(getResources().getColor(R.color.error_textedit_back));
                    isInfoOk = false;
                } else if (!password.equals(passwordRepeat)) {
                    passBox.setBackgroundColor(getResources().getColor(R.color.error_textedit_back));
                    passRepeatBox.setBackgroundColor(getResources().getColor(R.color.error_textedit_back));
                    isInfoOk = false;
                }


                //If all data is ok:
                if (isInfoOk) {
                    userJSONData = JSONWorker.createObjectForRegistration(tag,
                            name, lastname, phone, email, password);
                    new SendUrlData().execute(userJSONData.toString());
                }
            }

        });
    }


    private class SendUrlData extends AsyncTask<String, String, String> {
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            infoLabel.setText("Request sent...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            connector = new Connector();
            return connector.registrationRequest(strings[0]);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                infoLabel.setText("Error has happen");
            } else {
                infoLabel.setText("REGISTRATION");
                super.onPostExecute(result);
                //infoLabel.setText(result);
                if (connector.getResponseCode() == 200) {
                    //With code 200 you received "Registered" in buffer.
                    try {
                        ApplicationData.setOwner(new ModelOwner(new JSONObject(result)));
                        resetFieldColors();
                        resetFieldInput();
                        System.out.println("Printing owner data: " +
                                ApplicationData.getOwner().toString());
                        finish();
                        startActivity(new Intent(RegistrationActivity.this,
                                PostRegistration.class));
                    } catch (LogInException e) {
                        e.printStackTrace();
                        infoLabel.setText("Error:" + e);
                    } catch (Exception e) {
                        e.printStackTrace();
                        infoLabel.setText("Error! Olarm onotole!!!!");
                        Toast.makeText(RegistrationActivity.this,  e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    System.out.println("Response is " + connector.getResponseCode());
                    Toast.makeText(RegistrationActivity.this, result,
                            Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    private void resetFieldColors() {
        tagBox.setBackgroundColor(0);
        nameBox.setBackgroundColor(0);
        lastnameBox.setBackgroundColor(0);
        phoneBox.setBackgroundColor(0);
        emailBox.setBackgroundColor(0);
        passBox.setBackgroundColor(0);
        passRepeatBox.setBackgroundColor(0);
    }

    private void resetFieldInput() {
        tagBox.setText("");
        nameBox.setText("");
        lastnameBox.setText("");
        phoneBox.setText("");
        emailBox.setText("");
        passBox.setText("");
        passRepeatBox.setText("");
    }
}
