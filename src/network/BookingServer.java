package network;

import controller.BookingController;
import handler.RequestHandler;
import service.AuthService;
import service.BookingService;
import repository.DatabaseRepository;
import util.FileLogger;

import java.io.*;
import java.net.*;

public class BookingServer {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        DatabaseRepository repository = new DatabaseRepository();
        AuthService authService = new AuthService(repository);
        BookingService bookingService = new BookingService(repository);
        BookingController controller = new BookingController(authService, bookingService, repository);
        RequestHandler handler = new RequestHandler(controller);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("✅ Server started on port " + PORT);
            System.out.println("Waiting for clients...");


            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("📡 New client connected: " + clientSocket.getInetAddress());

                // ✅ 8. THREADS - каждый клиент в отдельном потоке
                new Thread(() -> handleClient(clientSocket, handler)).start();
            }
        } catch (IOException e) {
            System.err.println("❌ Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket, RequestHandler handler) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String requestLine = in.readLine();
            if (requestLine != null) {
                System.out.println("📝 Received: " + requestLine);
                String response = handler.handle(requestLine);
                System.out.println("💬 Response: " + response);
                out.println(response);
            }

        } catch (IOException e) {
            System.err.println("❌ Client handling error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("👋 Client disconnected");
        }
    }
}