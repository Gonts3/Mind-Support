package com.ultimate.mindsupport;

import com.ultimate.mindsupport.client.Client;

public class CurrentUser {
    private static User current;

    private CurrentUser() {} // prevent instantiation

    public static void set(User user) {
        current = user;
        setLoggedIn(true);
    }

    public static User get() {
        return current;
    }

    public static void clear() {
        current = null;
    }

    public static boolean isClient() {
        return current instanceof Client;
    }

    public static boolean isCounsellor() {
        return current instanceof Counsellor;
    }
    public static Client getClient() {
        if (current instanceof Client) {
            return (Client) current;
        }
        return null;
    }
    public static Counsellor getCounsellor() {
        if (current instanceof Counsellor) {
            return (Counsellor) current;
        }
        return null;
    }
    public static boolean isLoggedIn() {
        return SessionManager.isLoggedIn();
    }

    public static void setLoggedIn(boolean value) {
        SessionManager.setLoggedIn(value);
    }



}
