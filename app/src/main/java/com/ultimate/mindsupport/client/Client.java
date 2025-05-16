package com.ultimate.mindsupport.client;

import com.ultimate.mindsupport.SessionManager;
import com.ultimate.mindsupport.User;

public class Client extends User {
    private String id;
    private String username;
    private String problemId;

    // Constructor
    public Client(String id, String username, String problemId) {
        super(id);
        this.username = username;
        this.problemId = problemId;
        SessionManager.saveClientSession(id,username, problemId);
    }

    // Getters

    public String getUsername() {
        return username;
    }

    public String getProblemId() {
        return problemId;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
        SessionManager.saveClientSession(id,username,problemId);
    }

    public void setUsername(String username) {
        this.username = username;
        SessionManager.saveClientSession(id,username,problemId);
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
        SessionManager.saveClientSession(id,username,problemId);
    }
}
