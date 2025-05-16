package com.ultimate.mindsupport;

import com.ultimate.mindsupport.client.Client;

public class CurrentUser {
    private static User current;

    private CurrentUser() {} // prevent instantiation

    public static void set(User user) {
        current = user;
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
}
