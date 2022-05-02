package com.restur.msgrtest.serviceWorkers;

import com.restur.msgrtest.consts.ApplicationData;
import com.restur.msgrtest.models.ModelChat;
import com.restur.msgrtest.models.ModelMessage;
import com.restur.msgrtest.models.ModelOwner;
import com.restur.msgrtest.models.ModelUser;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONWorker {
    //Creating JSON objects
    public static JSONObject createObjectForRegistration(String tag, String name, String lastname,
                                                         String phone, String email, String pass) {
        JSONObject object = new JSONObject();
        try {
            object.put("userTAG", tag);
            object.put("name", name);
            if (!lastname.equals("")) object.put("surname", lastname);
            if (!email.equals("")) object.put("email", email);
            if (!phone.equals("")) object.put("phoneNumber", phone);
            object.put("passHash", HasherWorker.encryptThisString(pass));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println(object);
        return object;
    }



    //todo maybe should be created comparator for JSON objects

    //Reading JSON objects
    //todo should be finished OR DELETED!
    public static ModelOwner receiveJSONObjectOwner(JSONObject ownerJSON) {
        return null;
    }


    public static ModelChat receiveJSONObjectChat(JSONObject chatJSON) throws JSONException {
        try {
            Long chatId = chatJSON.getLong("id");
            String chatName = chatJSON.getString("chatName");

            ModelUser creatorModel =
                    ModelUser.toModelUserFromJson(chatJSON.getJSONObject("creatorModel"));
            if (!creatorModel.getId().equals(ApplicationData.getOwner().getId()) &&
            !ApplicationData.ifUserModelInUserMap(creatorModel)) {
                ApplicationData.addUserModelToUserMap(creatorModel);
            }

            ModelUser invitedUser =
                    ModelUser.toModelUserFromJson(chatJSON.getJSONObject("invitedModel"));
            if (!invitedUser.getId().equals(ApplicationData.getOwner().getId()) &&
            !ApplicationData.ifUserModelInUserMap(invitedUser)) {
                ApplicationData.addUserModelToUserMap(invitedUser);
            }

            Long creatorId = creatorModel.getId();
            Long invitedUserId = invitedUser.getId();
            boolean democracy = chatJSON.getBoolean("democracy");
            //Return new modelChat entity and saving it to ApplicationData
            return new ModelChat(chatId, chatName, creatorId, invitedUserId, democracy);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new JSONException("Unable to read chat");
    }


    //todo should be finished
    public static ModelMessage receiveJSONObjectMessage(JSONObject message) {
        return null;
    }
}
