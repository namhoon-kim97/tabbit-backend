package com.jungle.Tabbit.domain.notification.subscriber;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class NotificationFailureLogger {
    private static final String LOG_FILE_PATH = "/home/ubuntu/tabbit-backend/notification-failures.log";

    public static synchronized void log(String messageId, Object rawData, Exception e) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write("==== Notification Failure ====\n");
            writer.write("Timestamp   : " + LocalDateTime.now() + "\n");
            writer.write("Message ID  : " + messageId + "\n");
            writer.write("Raw Payload : " + rawData.toString() + "\n");
            writer.write("Exception   : " + e.toString() + "\n");
            writer.write("\n");
        } catch (IOException ioException) {
            System.err.println("🚨 Failed to write to failure log: " + ioException);
        }
    }
}
