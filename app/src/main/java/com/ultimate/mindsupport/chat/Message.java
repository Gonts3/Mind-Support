package com.ultimate.mindsupport.chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
            // Parse server time (likely UTC)
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            parser.setTimeZone(TimeZone.getTimeZone("UTC")); // tell it the input is in UTC
            Date date = parser.parse(raw);

            // Format to local time for display
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, HH:mm");
            formatter.setTimeZone(TimeZone.getDefault()); // convert to device's local time
            return formatter.format(date);
        } catch (Exception e) {
            return raw;
        }
    }
}
