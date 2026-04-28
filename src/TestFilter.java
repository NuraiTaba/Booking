import network.SocketClient;

public class TestFilter {
    public static void main(String[] args) {
        String response = SocketClient.sendCommand("FILTER 30000 60000 4");
        System.out.println("FILTER 30000 60000 4 →");
        System.out.println(response);
    }
}