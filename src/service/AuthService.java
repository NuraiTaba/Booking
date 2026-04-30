package service;

import repository.DatabaseRepository;
import exception.ValidationException;
import exception.DatabaseException;
import interfaces.IAuthService;


public class AuthService implements IAuthService {  
    private DatabaseRepository repo;

    public AuthService(DatabaseRepository repo) {
        this.repo = repo;
    }
    @Override
    public String login(String username, String password) {
        try {
            validateCredentials(username, password);
            Integer userId = repo.login(username, password);
            if (userId != null) {
                return "SUCCESS " + userId;
            }
            return "ERROR Invalid credentials";
        } catch (ValidationException e) {
            return "ERROR " + e.getMessage();
        }
    }
    @Override
    public String register(String username, String password) {
        try {
            validateCredentials(username, password);
            validatePassword(password);
            
            boolean success = repo.register(username, password, 0); 
            if (success) {
                return "SUCCESS User registered";
            }
            return "ERROR Username already exists";
        } catch (ValidationException e) {
            return "ERROR " + e.getMessage();
        }
    }
    
    private void validateCredentials(String username, String password) throws ValidationException {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }
    }
    
    private void validatePassword(String password) throws ValidationException {
        if (password.length() < 3) {
            throw new ValidationException("Password must be at least 3 characters");
        }
    }
}