package com.restur.msgrtest.consts;

import android.content.Context;
import android.view.Display;

import com.restur.msgrtest.databinding.ChatMainBinding;
import com.restur.msgrtest.exceptions.FileWorkerException;
import com.restur.msgrtest.models.ModelChat;
import com.restur.msgrtest.models.ModelMessage;
import com.restur.msgrtest.models.ModelOwner;
import com.restur.msgrtest.models.ModelUser;
import com.restur.msgrtest.serviceWorkers.DeviceInfoWorker;
import com.restur.msgrtest.serviceWorkers.FileWorkers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ApplicationData {
    //File names
    private final static String ownerModelFileName = "q1.msgr";
    private final static String chatModelListFileName = "q2.msgr";
    private final static String userModelFileName = "q3.msgr";
    private final static String messageModelModelFileName = "q4.msgr";

    //Entities
    private static Context appContext;

    private static ModelOwner owner;
    private static Map<Long, ModelChat> chatList = new HashMap<>();
    private static Map<Long, ModelUser> userList = new HashMap<>();
    private static Map<Long, ModelMessage> messageList = new HashMap<>();

    //READING ownerModel, chatList, userList and messageList from file
    static {
        try {
            ModelUser modelUser;
            ModelChat modelChat;
            ModelMessage modelMessage;
            //Owner
            try {
                owner = (ModelOwner) FileWorkers.readFileObject(ApplicationData.ownerModelFileName);
            } catch (FileWorkerException e) {
                e.printStackTrace();
            }
            //UserList
            try {
                for (Object jsonObject :
                        FileWorkers.readFileArray(ApplicationData.userModelFileName)) {
                    modelUser = (ModelUser) jsonObject;
                    userList.put(modelUser.getId(), modelUser);
                }
            } catch (FileWorkerException e) {
                e.printStackTrace();
            }
            //ChatList
            try {
                for (Object jsonObject :
                        FileWorkers.readFileArray(ApplicationData.chatModelListFileName)) {
                    modelChat = (ModelChat) jsonObject;
                    chatList.put(modelChat.getId(), modelChat);
                }
            } catch (FileWorkerException e) {
                e.printStackTrace();
            }
            //MessageList
            try {
                for (Object jsonObject :
                        FileWorkers.readFileArray(ApplicationData.messageModelModelFileName)) {
                    modelMessage = (ModelMessage) jsonObject;
                    messageList.put(modelMessage.getMessageId(), modelMessage);
                }
            }catch (FileWorkerException e) {
                e.printStackTrace(); }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //SAVING data to file
    public static void saveToFile(ModelTypes modelType) {
        switch (modelType) {
            case OWNER:
                FileWorkers.writeFile(owner, ApplicationData.ownerModelFileName);
                break;
            case USER:
                FileWorkers.writeFile(userList, ApplicationData.userModelFileName);
                break;
            case CHAT:
                FileWorkers.writeFile(chatList, ApplicationData.chatModelListFileName);
                break;
            case MESSAGE:
                FileWorkers.writeFile(messageList, ApplicationData.messageModelModelFileName);
                break;
        }
    }

    //Setters & Getters
    public static void setAppContext(Context appContext) {
        ApplicationData.appContext = appContext;
    }
    public static Context getAppContext() {
        return appContext;
    }

    public static void setOwner(ModelOwner modelOwner) {
        owner = modelOwner;
    }
    public static ModelOwner getOwner() {
        return owner;
    }

    public static String getPhoneId(Context context) {
        return DeviceInfoWorker.getImeiHashThisDevice(context);
    }

    public static String getOwnerModelFileName() {
        return ownerModelFileName;
    }

    public static ArrayList<ModelChat> getChatListOrdered() {
        ArrayList<ModelChat> chatListToSend = new ArrayList<>();
        for (ModelChat value : chatList.values()) {
            chatListToSend.add(value);
        }
        //todo should be fixed; try block should be removed!
        try {
            Collections.sort(chatListToSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatListToSend;
    }
    public static Map<Long, ModelChat> getChatMap() {
        return chatList;
    }

    public static Map<Long, ModelUser> getUserMap() {
        return userList;
    }

    public static Map<Long, ModelMessage> getMessageMap() {
        return messageList;
    }

    //Methods for working with userList
    public static boolean ifUserModelInUserMap(ModelUser userToCheck) {
        if (userList.containsKey(userToCheck.getId())) {
                return true;
            }
        return false;
    }
    public static void addUserModelToUserMap(ModelUser modelToAdd) {
        userList.put(modelToAdd.getId(), modelToAdd);
        ApplicationData.saveToFile(ModelTypes.USER);
    }
    public static void deleteUserModelFromUserList(ModelUser modelUser) {
        userList.remove(modelUser);
        ApplicationData.saveToFile(ModelTypes.USER);
    }

    public static ModelUser getUserModelFromUserMap(Long id) {
        return userList.get(id);
    }

    //Methods for working with chatList
    public static boolean ifChatModelInChatMap(ModelChat chatToCheck) {
            if (chatList.containsKey(chatToCheck.getId())) {
                return true;
            }
        return false;
    }
    public static void addChatModelToChatList(ModelChat modelToAdd) {
        chatList.put(modelToAdd.getId(), modelToAdd);
        ApplicationData.saveToFile(ModelTypes.CHAT);
    }
    public static void deleteChatModelFromChatList(ModelChat modelChat) {
        chatList.remove(modelChat);
        ApplicationData.saveToFile(ModelTypes.CHAT);
    }

}