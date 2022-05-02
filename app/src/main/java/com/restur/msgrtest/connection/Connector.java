package com.restur.msgrtest.connection;

import static com.restur.msgrtest.connection.CreateUrlRequest.createLoadAllChatsRequest;
import static com.restur.msgrtest.connection.CreateUrlRequest.createLoginRequest;
import static com.restur.msgrtest.connection.CreateUrlRequest.createRegistrationRequest;

import com.restur.msgrtest.exceptions.ConnectionException;
import com.restur.msgrtest.exceptions.LogInException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;


//todo MAKE class connector more usable. All connections with server should pass ONLY with this class
public class Connector {

    private JSONObject JSONUserData;
    private HttpURLConnection connection = null;
    private BufferedReader bufReader = null;
    private int responseCode = 0;

    private boolean createConnection(URL url) {
        try {
            connection = (HttpURLConnection) url.openConnection();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean connectToServer() {
        try {
            connection.connect();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean setRequestMethod(String requestType) {
        try {
            connection.setRequestMethod(requestType);
            return true;
        } catch (ProtocolException e) {
            e.getMessage();
            return false;
        }
    }

    public String loginRequest(String tag, String passHash, String deviceIdHash) {
        try {
            URL url = new URL(createLoginRequest(tag, passHash, deviceIdHash));
            if (createConnection(url)) {
                connectToServer();
                setRequestMethod("GET");
                InputStream inputStream;
                responseCode = connection.getResponseCode();
                System.out.println("\t\t Connector\tloginRequest()\tConnection: code: " + responseCode +
                        ", response message " + connection.getResponseMessage());
                if (responseCode == 200) {
                    inputStream = connection.getInputStream();
                    bufReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = bufReader.readLine()) != null) {
                        buffer.append(line).append("\n");
                        System.out.println(line);
                    }
                    try {
                        JSONUserData = new JSONObject(buffer.toString());
                        System.out.println("JSON object received: " + JSONUserData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return "Logged in!";

                } else {
                    inputStream = connection.getErrorStream();
                    bufReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = bufReader.readLine()) != null) {
                        System.out.println(line);
                        buffer.append(line).append("\n");
                    }
                    return buffer.toString();
                }
            }
        } catch (MalformedURLException e) {
            System.out.println("ERROR MalformedURLException!!! " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR IOException!!! " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("FINAL!!!");
            assert connection != null;
            connection.disconnect();
            try {
                if (bufReader != null)
                    bufReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;

    }
    //todo finish this!
    public boolean checkLoggedIn(Long userId, String deviceIdHash) throws LogInException,
            ConnectionException {

        URL url = null;
        try {
            url = new URL(CreateUrlRequest.createCheckLogInStatus(userId, deviceIdHash));
        if (createConnection(url)) {
                //Sending request
                setRequestMethod("GET");
                connectToServer();
                if (connection.getResponseCode() == 200) {
                    return true;
                } else {
                    BufferedReader bufReader =
                            new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = bufReader.readLine()) != null) {
                        System.out.println(line);
                        buffer.append(line).append("\n");
                    }
                    throw new LogInException(buffer.toString());
                }
            } else {
                throw new ConnectionException("Unable to create connection");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConnectionException("Error happen: " + e.getMessage());
        }
    }

    public String registrationRequest(String jsonString) {
        try {
            URL url = new URL(createRegistrationRequest());
            if (createConnection(url)) {
                //Sending request
                setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connectToServer();
                //Sending JSON object
                try(OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                //Reading response
                responseCode = connection.getResponseCode();
                InputStream inputStream;
                System.out.println("Connection: code: " + responseCode +
                        ", response message " + connection.getResponseMessage());
                if (connection.getResponseCode() == 200) {
                    inputStream = connection.getInputStream();
                    bufReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = bufReader.readLine()) != null) {
                        System.out.println(line);
                        buffer.append(line).append("\n");
                    }
                    return buffer.toString();

                } else {
                    inputStream = connection.getErrorStream();
                    bufReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = bufReader.readLine()) != null) {
                        System.out.println(line);
                        buffer.append(line).append("\n");
                    }
                    return buffer.toString();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
        return "¯\\_(ツ)_/¯";
    }

    public List<JSONObject> getChatList(Long id, String phoneId) {
        //todo FINISH
        try {
            URL url = new URL(createLoadAllChatsRequest(id, phoneId));
            if (createConnection(url)) {
                connectToServer();
                setRequestMethod("GET");
                InputStream inputStream;
                responseCode = connection.getResponseCode();
                System.out.println("Connection: code: " + responseCode +
                        ", response message " + connection.getResponseMessage());
                if (responseCode == 200) {
                    inputStream = connection.getInputStream();
                    bufReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = bufReader.readLine()) != null) {
                        buffer.append(line).append("\n");
                        System.out.println(line);
                    }
                    System.out.println("\n\n\n\n===" + buffer.toString() + "===\n\n\n\n");

                    JSONArray arrayJson = new JSONArray(buffer.toString());
                    List<JSONObject> listJson = new LinkedList<>();

                    for (int i = 0; i < arrayJson.length(); i++) {
                        listJson.add(arrayJson.getJSONObject(i));
                    }

                    return listJson;
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

        public int getResponseCode() {
            return this.responseCode;
        }

        public JSONObject getJSONUserData() {
        return JSONUserData;
    }
}