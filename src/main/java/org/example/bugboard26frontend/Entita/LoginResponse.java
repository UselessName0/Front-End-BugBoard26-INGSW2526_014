package org.example.bugboard26frontend.Entita;

public class LoginResponse {
    private String token;
    private boolean IsAdmin;
    private Long userId;

    public  LoginResponse() {}

    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

    public boolean isAdmin() {return IsAdmin;}
    public void setAdmin(boolean admin) {IsAdmin = admin;}

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}
}
