package interfaces;

public interface IAuthService {
    String login(String username, String password);
    String register(String username, String password);
}