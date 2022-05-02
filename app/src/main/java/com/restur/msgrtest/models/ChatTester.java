package com.restur.msgrtest.models;

public class ChatTester {

    //must have for developing
    private String name;
    private final long id;
    private String lastMsg;// = "bla bla bla bla";


    //should be implemented while needed
    private String chatFile;

    public ChatTester(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public ChatTester(String name, long id, String lastMsg) {
        this.name = name;
        this.id = id;
        this.lastMsg = lastMsg;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getChatFile() {
        return chatFile;
    }

    public void setChatFile(String chatFile) {
        this.chatFile = chatFile;
    }
}
