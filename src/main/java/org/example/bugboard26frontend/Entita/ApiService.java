package org.example.bugboard26frontend.Entita;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiService {

    private static final String BASE_URL = "http://localhost:8080/api/users";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper(); // Per il JSON

    // Chiamata API per il login dell'utente
    public Utente login(String email, String password) throws Exception {
        Map<String, String> datiLogin = new HashMap<>();
        datiLogin.put("email", email);
        datiLogin.put("password", password);
        String jsonBody = mapper.writeValueAsString(datiLogin);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // 4. Se OK, trasformo il JSON ricevuto in oggetto User (che conterrà il token)
            // Nota: Il backend deve restituire un oggetto User o una mappa compatibile
            // Per ora assumiamo che il backend restituisca un JSON che mappa su User
            return mapper.readValue(response.body(), Utente.class);
        } else {
            throw new RuntimeException("Login fallito: " + response.body());
        }
    }

    // Chiamata API per caricamento della tabella centrale della dashboard
    public List<Issue>  getTutteLeIssue() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/issues"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            String jsonRicevuto = response.body();

            return mapper.readValue(jsonRicevuto, new TypeReference<List<Issue>>(){});
        } else {
            throw new RuntimeException("Errore nel caricamento delle issue. Codice: " + response.statusCode());
        }
    }

    // Chiamata API per creazione di una nuova issue
    public Issue creaIssue(Issue nuovaIssue) throws Exception {
        String jsonBody = mapper.writeValueAsString(nuovaIssue);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/issues")) // POST su /api/issues
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return mapper.readValue(response.body(), Issue.class);
        } else {
            throw new RuntimeException("Errore creazione issue: " + response.body());
        }
    }
}
