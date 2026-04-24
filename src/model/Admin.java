package model;

public class Admin extends User {
    private String adminLevel;
    
    public Admin(int id, String username, String password, String adminLevel) {
        super(id, username, password);
        this.adminLevel = adminLevel;
    }
    
    public String getAdminLevel() { return adminLevel; }
    
    @Override
    public String getRole() {
        return "ADMIN";
    }
    
    @Override
    public String toString() {
        return "Admin{id=" + id + ", username='" + username + "', level=" + adminLevel + "}";
    }
}