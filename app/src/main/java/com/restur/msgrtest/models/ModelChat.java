package com.restur.msgrtest.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelChat implements Serializable, Comparable<ModelChat>{
    //Main data
    private final Long id;
    private String chatName;
    private Long creatorId;
    private Long invitedUserId;

    private boolean democracy;

    private LinkedList<ModelMessage> messages = new LinkedList<>();

    //todo maybe should be deleted
    //additional
    private Map<Long, ModelMessage> mapMessages;

    public ModelChat(Long id, String chatName, Long creatorId,
                     Long invitedUserId, boolean democracy) {
        this.id = id;
        this.chatName = chatName;
        this.creatorId = creatorId;
        this.invitedUserId = invitedUserId;
        this.democracy = democracy;
    }

    //todo should be checked
    public void addAllMessages(List<ModelMessage> messages) {
        //Sorting messages & adding them to LinkedList
        Collections.sort(messages, ModelMessage.Comparators.ID);
        this.messages.addAll(messages);
    }

    //Getters
    public Long getId() {
        return id;
    }

    public String getChatName() {
        return chatName;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public Long getInvitedUserId() {
        return invitedUserId;
    }

    public LinkedList<ModelMessage> getMessages() {
        return messages;
    }

    public boolean isDemocracy() {
        return democracy;
    }

    public Map<Long, ModelMessage> getMapMessages() {
        return mapMessages;
    }

    public String getLastMessageString() {
        String message = null;
        try {
            message = messages.getLast().getMessage();
        } catch (Exception e) {

        }
        if (message == null) {
            return "No messages";
        }
        return message;
    }

    @Override
    public int compareTo(ModelChat modelChat) {
        return mapMessages.get(mapMessages.size()).getCreated().compareTo(
                modelChat.getMapMessages().get(modelChat.getMapMessages().size()).getCreated());
    }

    @Override
    public String toString() {
        return "ModelChat{" +
                "id=" + id +
                ", chatName='" + chatName + '\'' +
                ", creatorId=" + creatorId +
                ", invitedUserId=" + invitedUserId +
                ", democracy=" + democracy +
                ", messages=" + messages +
                ", mapMessages=" + mapMessages +
                '}';
    }
}
