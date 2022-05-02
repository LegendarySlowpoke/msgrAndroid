package com.restur.msgrtest.models;

import android.os.Build;
import android.text.PrecomputedText;
import android.widget.Toast;

import com.restur.msgrtest.connection.Connector;
import com.restur.msgrtest.consts.ApplicationData;
import com.restur.msgrtest.consts.ModelTypes;
import com.restur.msgrtest.exceptions.ConnectionException;
import com.restur.msgrtest.exceptions.LogInException;
import com.restur.msgrtest.exceptions.NotificationException;
import com.restur.msgrtest.serviceWorkers.FileWorkers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class ModelOwner implements Serializable {
    //Necessary info
    private Long id;
    private String userTAG;
    private String name;

    //Additional info
    private String surname;
    //Contact info
    private String phoneNumber;
    private String email;

    //Confidential info
    private String passHash;
    private Boolean isLoggedIn = false;

    //Service info
    private LinkedList<String> changesLog = new LinkedList<>();
    //ChatInfo
    private List<ModelChat> userChats;


    //Constructor
    public ModelOwner(JSONObject jsonOwnerData) throws LogInException {
        try {
            this.id = jsonOwnerData.getLong("id");
            this.userTAG = jsonOwnerData.getString("userTag");
            this.name = jsonOwnerData.getString("userName");

            this.phoneNumber = jsonOwnerData.getString("userPhone");
            //todo Server not sending return: should be fixed pn server side
            //this.email = jsonOwnerData.getString("email");

            this.surname = jsonOwnerData.getString("userSurname");
            //Saving to file(should be save with loggedIn set to False)
            ApplicationData.saveToFile(ModelTypes.OWNER);
            //Setting logged in to true
            this.isLoggedIn = true;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new LogInException("Unable to create modelOwner: " + e.getMessage());
        }
    }

    //Methods


    //Setters & Getters
    public Long getId() { return id; }

    public String getUserTAG() {
        return userTAG;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassHash() {
        return passHash;
    }

    public List<ModelChat> getUserChats() {
        return userChats;
    }

    //Login checkers & switchers
    public boolean isLoggedInClientSide() { return isLoggedIn; }

    public void checkLoggedInServerSide() throws LogInException, ConnectionException {
        Connector connector = new Connector();
            connector.checkLoggedIn(this.id,
                    ApplicationData.getPhoneId(ApplicationData.getAppContext()));
    }

    //todo should be deleted or implemented
    /*
    public void setLoggedIn(String pass) throws LogInException, NotificationException {
        if (this.isLoggedIn) {
            this.isLoggedIn = false;
        } else {
            Connector connector = new Connector();
            //todo CHANGE IT
            String response = connector.loginRequest(this.userTAG, passHash);
            if (connector.getResponseCode() == 200) {
                //using local private method compareOwnerData()
                String compareResult = compareOwnerData(connector.getJSONUserData());
                if (compareResult.equals("kokoko")) {
                    this.isLoggedIn = true;
                } else if (compareResult.equals("changesLoaded")){
                    //todo add exception description
                    this.isLoggedIn = true;
                    throw new NotificationException("Logged in! Loaded changes: "
                        + changesLog.getLast().split("::")[1]);
                } else {
                    throw new LogInException(compareResult);
                }
            } else {
                //todo implement if code != 200
                throw new LogInException(response);
            }
        }
    }
     */


    //results: "kokoko" - is ok,
    //          "changesLoaded" - is if changes were loaded to owner info from server
    //                '::' - is separator btwn time stamp and changes
    //          any other result - is String to be thrown as LogInException description
    //todo should be implemented
    private String compareOwnerData(JSONObject received) {
        try {
            boolean changesLoaded = false;
            String currentChanges = "";
            if (received.getLong("id") != this.id ||
                    this.userTAG.equals(received.getString("userTag"))) {
                throw new LogInException("Tag and ID number mismatch");
            }
            if (!this.name.equals(received.getString("name"))) {
                this.name = received.getString("name");
                currentChanges += " name";
                changesLoaded = true;
            }
            if (!this.surname.equals(received.getString("surname"))) {
                this.surname = received.getString("surname");
                currentChanges += " surname";
                changesLoaded = true;
            }
            //todo should enable this code when receiving email from server will be fixed;
            /*
            if (!this.email.equals(received.getString("email"))) {
                this.email = received.getString("email"));
                changes += " email";
                changesLoaded = true;
            }
             */
            if (!this.phoneNumber.equals(received.getString("phoneNumber"))) {
                this.phoneNumber = received.getString("phoneNumber");
                currentChanges += " phoneNumber";
                changesLoaded = true;
            }
            //todo maybe should implement extra comparisons

            if (changesLoaded) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    changesLog.add(LocalDateTime.now() + "::" + currentChanges);
                }
                changesLog.add("No logTime"+ "::" + currentChanges);
            return "changesLoaded";
            } else {
                return "kokoko";
            }
        } catch (LogInException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unexpected exception";
        }
    }

    //Misc
    @Override
    public String toString() {
        return "ModelOwner{" +
                "id=" + id +
                ", userTAG='" + userTAG + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", surname='" + surname + '\'' +
                ", passHash='" + passHash + '\'' +
                ", userChats=" + userChats +
                '}';
    }
}
