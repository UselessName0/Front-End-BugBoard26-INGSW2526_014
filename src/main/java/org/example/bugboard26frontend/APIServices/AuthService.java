package org.example.bugboard26frontend.APIServices;

import org.example.bugboard26frontend.Entita.LoginResponse;
import org.example.bugboard26frontend.Entita.Utente;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private final ApiClient api = ApiClient.getApiClient();

    public boolean Login(String email, String password) throws Exception {

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
            throw new Exception("Credenziali non valide.");
        
        } else if (response.statusCode() == 500) {
            throw new Exception("Credenziali non valide.");
            
        } else {
            throw new Exception("Errore durante il login: " + response.statusCode());
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
}
