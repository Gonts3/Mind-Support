package com.ultimate.mindsupport.counsellor;

import com.ultimate.mindsupport.AccountManager;
import com.ultimate.mindsupport.ProblemManager;
import com.ultimate.mindsupport.SessionManager;
import com.ultimate.mindsupport.User;

import java.util.ArrayList;

public class Counsellor extends User {

    private String fname;
    private String lname;
    private String reg_no;

    public String getReg_no() {
        return reg_no;
    }



    // Constructor (problems list is initialized inside)
    public Counsellor(String id, String fname, String lname, String reg_no) {
        super(id);
        this.fname = fname;
        this.lname = lname;
        SessionManager.saveCounsellorSession(id);
    }



    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }
    public void setFname(String firstname, AccountManager.AccountCallback callback) {
        AccountManager.ChangeCounsellorName(this.getId(),firstname,"", callback);
        fname = firstname;

    }
    public void setLname(String lastname, AccountManager.AccountCallback callback) {
        AccountManager.ChangeCounsellorName(this.getId(),"",lastname, callback);
        lname = lastname;

    }
    public void setNames(String firstname,String lastname, AccountManager.AccountCallback callback) {
        if(lname.isEmpty()){
            setFname(firstname,callback);
            return;
        }
        if(fname.isEmpty()){
            setLname(lastname,callback);
            return;
        }
        AccountManager.ChangeCounsellorName(this.getId(),firstname,lastname, callback);
        this.fname = firstname;
        this.lname = lastname;
    }

    public void getProblems(ProblemManager.ProblemListCallback callback) {
            ProblemManager.GetCounsellorProblems(this.getId(),callback);
    }
    // Add a problem to the list
    public void addProblems(ArrayList<String> problem, ProblemManager.ProblemCallback callback) {
        ProblemManager.AddCounsellorProblems(this.getId(), problem,callback);
    }

    // Remove a problem from the list
    public void removeProblem(String problem) {


        //TODO Create a php script for api
    }
}
