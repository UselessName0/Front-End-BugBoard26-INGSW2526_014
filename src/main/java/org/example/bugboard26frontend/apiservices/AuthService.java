package org.example.bugboard26frontend.apiservices;

import org.example.bugboard26frontend.entita.LoginResponse;
import org.example.bugboard26frontend.entita.Utente;
import org.example.bugboard26frontend.exceptions.InvalidCredentialsException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private final ApiClient api = ApiClient.getApiClient();

    public boolean login(String email, String password) throws Exception {

        Map<String, String> credenziali = new HashMap<>();
        credenziali.put("email", email);
        credenziali.put("password", password);

        String jsonBody = api.getMapper().writeValueAsString(credenziali);

        String url = api.getBaseUrl() + "/users/auth/login";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            LoginResponse dati = api.getMapper().readValue(response.body(), LoginResponse.class);

            api.setAuthToken(dati.getToken());
            api.setAdmin(dati.isAdmin());
            api.setCurrentUserId(dati.getUserId());
            if(dati.isAdmin()) {
                System.out.println("Login effettuato con successo. Utente Admin.");
            } else {
                System.out.println("Login effettuato con successo. Utente Standard.");
            }
            return true;
        } else if (response.statusCode() == 401) {
            throw new InvalidCredentialsException("Credenziali non valide.");
        
        } else if (response.statusCode() == 500) {
            throw new IOException("Credenziali non valide.");
            
        } else {
            throw new IOException("Errore durante il login: " + response.statusCode());
        }
    }

    public Utente getUtenteLoggato() {
        Long userId = api.getCurrentUserId();
        UtenteService userService = new UtenteService();
        try {
            return userService.getUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void logout() throws IOException, InterruptedException {
        String token = api.getAuthToken();

        if (token == null || token.isEmpty()) {
            return;
        }

        String url = api.getBaseUrl() + "/users/auth/logout";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        api.setAuthToken(null);
        api.setAdmin(false);
        api.setCurrentUserId(null);

        if(response.statusCode() == 200) {
            System.out.println("[Chiamata API] Logout effettuato con successo.");
        } else {
            throw new IOException("Errore durante il logout: " + response.statusCode());
        }
    }
}
