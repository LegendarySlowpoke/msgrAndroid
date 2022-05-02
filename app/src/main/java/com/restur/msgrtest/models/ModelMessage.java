package com.restur.msgrtest.models;

import java.io.Serializable;
import java.sql.Time;
import java.util.Comparator;

public class ModelMessage implements Serializable, Comparable<ModelMessage> {

    private Long messageId;
    private Long sender;
    private String message;
    private Time created;
    private Time timeModified;
    private Time timeDeleted;


    //Getters & setters
    public Long getMessageId() { return messageId; }

    public void setMessageId(Long messageId) { this.messageId = messageId; }

    public Long getSender() { return sender; }

    public void setSender(Long sender) { this.sender = sender; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public Time getCreated() { return created; }

    public void setCreated(Time created) { this.created = created; }

    public Time getTimeModified() { return timeModified; }

    public void setTimeModified(Time timeModified) { this.timeModified = timeModified; }

    public Time getTimeDeleted() { return timeDeleted; }

    public void setTimeDeleted(Time timeDeleted) { this.timeDeleted = timeDeleted; }

    @Override
    public int compareTo(ModelMessage modelMessage) {
        return Comparators.ID.compare(this, modelMessage);
    }

    public static class Comparators {
        public static Comparator<ModelMessage> ID = new Comparator<ModelMessage>() {
            @Override
            public int compare(ModelMessage modelMessage1, ModelMessage modelMessage2) {
                return modelMessage1.messageId.compareTo(modelMessage2.messageId);
            }
        };
    }
}
