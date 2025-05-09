package com.ultimate.mindsupport;

import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;

public class HTTPClient {
    private static OkHttpClient client;

    public static OkHttpClient getClient() {
        if (client == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            client = new OkHttpClient.Builder()
                    .cookieJar(new JavaNetCookieJar(cookieManager))
                    .build();
        }
        return client;
    }
}
