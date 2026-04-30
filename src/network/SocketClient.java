package network;

import java.io.*;
import java.net.*;

public class SocketClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 6000;

    public static String sendCommand(String command) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(command);
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                if (response.length() > 0) {
                    response.append("\n");
                }
                response.append(line);
            }
            return response.toString();

        } catch (IOException e) {
            return "ERROR Connection failed: " + e.getMessage();
        }
    }

    
    public static void main(String[] args) {
        System.out.println("=== Testing Socket Client ===\n");

        String response1 = sendCommand("LOGIN alice pass");
        System.out.println("LOGIN alice pass → " + response1);

        String response2 = sendCommand("BOOK 1 Hilton 2026-05-01");
        System.out.println("BOOK 1 Hilton 2026-05-01 → " + response2);

        String response3 = sendCommand("GET_BOOKINGS 1");
        System.out.println("GET_BOOKINGS 1 → " + response3);

        String response4 = sendCommand("LOGIN wrong wrong");
        System.out.println("LOGIN wrong wrong → " + response4);

        String response5 = sendCommand("REGISTER newuser pass123");
        System.out.println("REGISTER newuser pass123 → " + response5);


        System.out.println("=== Testing New Commands ===\n");


String hotels = sendCommand("GET_HOTELS");
System.out.println("GET_HOTELS → \n" + hotels);


String search = sendCommand("SEARCH Астана");
System.out.println("SEARCH Астана → \n" + search);


String detail = sendCommand("HOTEL_DETAIL 1");
System.out.println("HOTEL_DETAIL 1 → " + detail);


String myHotels = sendCommand("MY_HOTELS 13");
System.out.println("MY_HOTELS 13 → \n" + myHotels);
     }

     
}
