package util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger {
    private static final String LOG_FILE = "booking.log";
    
    public static void log(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            out.println(timestamp + " - " + message);
            out.flush();  // принудительно сохраняем
            
            // Для отладки — выведем в консоль, что лог записан
            System.out.println("📝 Log written: " + message);
            
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void log(String command, String result) {
        log(command + " → " + result);
    }
}