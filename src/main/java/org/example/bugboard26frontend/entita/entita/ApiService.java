package org.example.bugboard26frontend.entita.entita;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class ApiService {

    private static final String BASE_URL = "http://localhost:8080/api/users";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper(); // Per il JSON

    public Utente login(String email, String password) throws Exception {
        // 1. Preparo i dati da inviare
        Map<String, String> datiLogin = new HashMap<>();
        datiLogin.put("email", email);
        datiLogin.put("password", password);

        String jsonBody = mapper.writeValueAsString(datiLogin);

        // 2. Costruisco la richiesta POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // 3. Invio e aspetto la risposta
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
}
