package com.ultimate.mindsupport;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class User {
    private String id;

    public User(String id) {
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
