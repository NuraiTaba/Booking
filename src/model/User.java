package model;

public class User {
    protected int id;
    protected String username;
    protected String password;
    
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    
    public String getRole() {
        return "USER";
    }
    
    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }
}