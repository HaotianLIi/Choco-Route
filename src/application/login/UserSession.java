package application.login;

public class UserSession {
    private static UserSession instance;
    private int userId;
    private String username;
    private String role;

    private UserSession(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public static void setInstance(int userId, String username, String role) {
        instance = new UserSession(userId, username, role);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }

    public static void clear() {
        instance = null;
    }
}