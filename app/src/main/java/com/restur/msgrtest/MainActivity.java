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

import com.restur.msgrtest.connection.Connector;
import com.restur.msgrtest.consts.ApplicationData;
import com.restur.msgrtest.models.ModelChat;
import com.restur.msgrtest.serviceWorkers.HasherWorker;
import com.restur.msgrtest.consts.PathsToServer;
import com.restur.msgrtest.exceptions.LogInException;
import com.restur.msgrtest.models.ModelOwner;
import com.restur.msgrtest.serviceWorkers.JSONWorker;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //UI fields
    //Text views etc
    private TextView infoLabel;
    private View backLayout;

    //Buttons & text edits
    private TextView registrationTxtBtn;
    private EditText tagBox;
    private EditText passBox;
    private Button submitButton;

    //Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize
            if (ApplicationData.getOwner() == null) {
                System.out.println("\n\n\t\tApplicationData.getOwner()\n\n");
                ApplicationData.setAppContext(getApplicationContext());
                ApplicationData.getPhoneId(this);
            }

        //Checking login status
        if (ApplicationData.getOwner() != null) {
            if (ApplicationData.getOwner().isLoggedInClientSide()) {
                System.out.println("=====================================================\n" +
                        "MainActivity - checking login status - LOGGED IN!");
                finish();
                startActivity(new Intent(MainActivity.this, ChatListActivity.class));

            }
        }

        //Initializing
        setContentView(R.layout.activity_main);
        //Main app vars & entities
        //ApplicationData.setAppContext(getApplicationContext());
        //ApplicationData.setPhoneId(this);

        //Local vars
        backLayout = findViewById(R.id.mainBackLayout);

        tagBox = findViewById(R.id.registrationTagField);
        passBox = findViewById(R.id.mainPassField);
        infoLabel = findViewById(R.id.mainInfoLabel);

        submitButton = findViewById(R.id.registrationSubmitButton);
        registrationTxtBtn = findViewById(R.id.mainRegistrationTxtbtn);

        //Checking if ModelOwner is read from file
        if (ApplicationData.getOwner() == null) {
            System.out.println("=====================================================\n"
                    + "MainActivity - checking login status - No owner model!");
        } else {
            System.out.println("=====================================================\n" +
                    "MainActivity - checking login status - owner model, needs password.");
            tagBox.setText(ApplicationData.getOwner().getUserTAG());
        }

        //On click listeners
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoLabel.setText("");
            }
        });

        registrationTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoLabel.setText("");
                finish();
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                String userTag = tagBox.getText().toString().trim().replace(" ", "");
                String pass = passBox.getText().toString();
                String deviceIdHash = ApplicationData.getPhoneId(ApplicationData.getAppContext());
                if (userTag.equals("") || pass.equals("")) {
                    infoLabel.setText(getResources().getString(R.string.main_error_enter_name_and_pass));
                } else if (!userTag.matches("[a-zA-Z0-9]+")) {
                    infoLabel.setText(getResources().getString(R.string.main_error_enter_proper_tag));
                } else {
                    String passHash = HasherWorker.encryptThisString(passBox.getText().toString());
                    String loginRequest = PathsToServer.getServerPath() +
                            PathsToServer.getUserPath() +
                            "login?userTag=" + userTag + "&pass=" + passHash + "&deviceIdHash="
                            + ApplicationData.getPhoneId(ApplicationData.getAppContext());
                    System.out.println("Sent request is: " + loginRequest);
                    String[] req = new String[3];
                    req[0] = userTag;
                    req[1] = passHash;
                    req[2] = deviceIdHash;
                    new GetUrlData().execute(req);
                }
            }
        });
    }


    private class GetUrlData extends AsyncTask<String, String, String> {

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            infoLabel.setText("Request sent...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Connector connector = new Connector();
            //strings[0] = userTag; strings[1] = passHash; strings[2] = deviceIdHash;
            String result =  connector.loginRequest(strings[0],strings[1], strings[2]);
            if (result.equals("Logged in!")) {
                try {
                    //Loading all required data from Server(ModelOwner, all modelChat + modelUser)
                    ApplicationData.setOwner(new ModelOwner(connector.getJSONUserData()));
                    Connector connector1 = new Connector();
                    for (JSONObject jsonObject : connector1.getChatList(ApplicationData.getOwner().getId(),
                            strings[2])) {
                        System.out.println("MainActivity: Loop with obj " + jsonObject.toString());
                        ModelChat chatModel = JSONWorker.receiveJSONObjectChat(jsonObject);
                        if (!ApplicationData.ifChatModelInChatMap(chatModel)) {
                            ApplicationData.addChatModelToChatList(chatModel);
                        }
                    }


                    //todo ALL data is always loaded from server. Should be created function which
                    // compares loaded data with data on server

                    // ApplicationData.getOwner()
                    finish();
                    //ApplicationData.getOwner().setLoggedIn(strings[1]);
                    startActivity(new Intent(MainActivity.this, ChatListActivity.class));
                } catch (LogInException e) {
                    return e.getMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                    return e.getMessage();
                } /*catch (NotificationException e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(ApplicationData.getAppContext(), e.getMessage(),
                            Toast.LENGTH_SHORT);
                    toast.show();
                    }*/
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                infoLabel.setText("Error has happen");
            } else {
                super.onPostExecute(result);
                infoLabel.setText(result);
                passBox.setText("");
            }
        }
    }
}