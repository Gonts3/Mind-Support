package com.ultimate.mindsupport;

import java.util.ArrayList;

import java.util.List;

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

    public void getProblems(ProblemManager.ProblemListCallback callback) {
            ProblemManager.GetCounsellorProblems(this.getId(),callback);
    }
    // Add a problem to the list
    public void addProblems(ArrayList<String> problem, ProblemManager.ProblemCallback callback) {
        ProblemManager.AddCounsellorProblems(this.getId(), problem,callback);
        //TODO Create a php script for api
    }

    // Remove a problem from the list
    public void removeProblem(String problem) {


        //TODO Create a php script for api
    }
}
