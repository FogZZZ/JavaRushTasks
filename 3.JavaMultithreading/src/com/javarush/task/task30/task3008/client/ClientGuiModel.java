package com.javarush.task.task30.task3008.client;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ClientGuiModel {
    private final Set<String> allUserNames = new HashSet<>();
    private final Set<String> privateChats = new HashSet<>();
    private String myName;
    private String newMessage;
    private boolean privateBack;

    public Set<String> getAllUserNames() {
        return Collections.unmodifiableSet(allUserNames);
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyName() {
        return myName;
    }

    public String getNewMessage() {
        if (privateBack) {
            privateBack = false;
            newMessage = newMessage.substring(newMessage.indexOf(":") + 1, newMessage.length());
            return newMessage;
        }
        else {
            return newMessage;
        }
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }

    public String getUserName() {
        if (newMessage == null || !newMessage.contains(":"))
            return null;
        return newMessage.substring(0, newMessage.indexOf(":"));
    }

    public void addUser(String newUserName) {
        allUserNames.add(newUserName);
    }

    public void deleteUser(String userName) {
        allUserNames.remove(userName);
    }

    public void addPrivateChat(String fromUser) {
        privateChats.add(fromUser);
    }

    public void deletePrivateChat(String fromUser) {
        privateChats.remove(fromUser);
    }

    public boolean isPrivateChatOpened(String fromUser) {
        return  privateChats.contains(fromUser);
    }

    public void setPrivateBack(boolean privateBack) {
        this.privateBack = privateBack;
    }
}
