package org.example.bugboard26frontend.APIServices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bugboard26frontend.Entita.Issue;
import org.example.bugboard26frontend.Entita.Utente;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiClient {

    private static ApiClient apiClient;
    private static final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper(); // Per il JSON

    private String authToken; // Per memorizzare il token di autenticazione
    private boolean isAdmin; // Per memorizzare se l'utente è admin
    private Long currentUserId; // Per memorizzare l'ID dell'utente loggato

    private ApiClient() {
    }

    public static ApiClient getApiClient() {
        if(apiClient == null) {
            apiClient = new ApiClient();
        }
        return apiClient;
    }

    public HttpClient getClient() {return client;}
    public ObjectMapper getMapper() {return mapper;}
    public String getBaseUrl() {return BASE_URL;}

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }


}
