package org.example.bugboard26frontend.entita;

public class LoginResponse {
    private String token;
    private boolean isAdmin;
    private Long userId;

    public  LoginResponse() {}

    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

    public boolean isAdmin() {return isAdmin;}
    public void setAdmin(boolean admin) {
        isAdmin = admin;}

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}
}
