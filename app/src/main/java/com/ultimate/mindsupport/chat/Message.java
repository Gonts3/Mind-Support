package com.ultimate.mindsupport.chat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    public int senderId;
    public String content;
    public String timestamp; // formatted timestamp string

    public Message(int senderId, String content, String rawTimestamp) {
        this.senderId = senderId;
        this.content = content;
        this.timestamp = formatTimestamp(rawTimestamp);
    }

    private String formatTimestamp(String raw) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = parser.parse(raw);

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, HH:mm"); // example: "28 May, 15:22"
            return formatter.format(date);
        } catch (Exception e) {
            // If parsing fails, just return the raw string as fallback
            return raw;
        }
    }
}
