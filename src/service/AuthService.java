package service;

import repository.DatabaseRepository;

public class AuthService {
    private DatabaseRepository repo;

    public AuthService(DatabaseRepository repo) {
        this.repo = repo;
    }

    public String login(String username, String password) {
        Integer userId = repo.login(username, password);
        if (userId != null) {
            return "SUCCESS " + userId;
        }
        return "ERROR Invalid credentials";
    }
}