package com.ultimate.mindsupport.client;

import android.widget.Toast;

import com.ultimate.mindsupport.AccountManager;
import com.ultimate.mindsupport.LoginManager;
import com.ultimate.mindsupport.ProblemManager;
import com.ultimate.mindsupport.SessionManager;
import com.ultimate.mindsupport.TestingActivity;
import com.ultimate.mindsupport.User;

public class Client extends User {

    private String username;
    private String problemId;

    // Constructor
    public Client(String id, String username, String problemId) {
        super(id);
        this.username = username;
        this.problemId = problemId;
        SessionManager.saveClientSession(id);
    }

    // Getters

    public String getUsername() {
        return username;
    }

    public String getProblemId() {
        return problemId;
    }

    // Setters

    public void setUsername(String username,AccountManager.AccountCallback callback) {
        AccountManager.ChangeClientName(this.getId(),username,callback);
        this.username = username;
        //TODO Create a php script for api
    }

    public void setProblemId(String problemId, ProblemManager.ProblemCallback callback) {
        this.problemId = problemId;
        ProblemManager.AddClientProblem(problemId, this.getId(), callback);

    }

}
