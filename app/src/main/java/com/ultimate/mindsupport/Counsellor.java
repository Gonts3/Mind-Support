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

    private ArrayList<String>problems;

    // Constructor (problems list is initialized inside)
    public Counsellor(String id, String fname, String lname, String reg_no) {
        super(id);
        this.fname = fname;
        this.lname = lname;
        this.problems = new ArrayList<>();
        SessionManager.saveCounsellorSession(id,reg_no, fname, lname);
    }



    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public List<String> getProblems() {
        return problems;
    }
    // Add a problem to the list
    public void addProblem(String problem) {
        this.problems.add(problem);
    }

    // Remove a problem from the list
    public void removeProblem(String problem) {
        this.problems.remove(problem);
    }
}
