package handler;

import controller.BookingController;
import util.FileLogger;

public class RequestHandler {
    private BookingController controller;
    
    public RequestHandler(BookingController controller) {
        this.controller = controller;
    }

    public String handle(String request) {
        String response = controller.processCommand(request);
        FileLogger.log(request, response);
        return response;
    }
}