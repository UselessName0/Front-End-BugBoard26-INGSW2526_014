package org.example.bugboard26frontend.apiservices;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.bugboard26frontend.entita.Utente;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class UtenteService {

    private final ApiClient api = ApiClient.getApiClient();
    private final String USER_BASE_URL;

    public UtenteService() {
        this.USER_BASE_URL = api.getBaseUrl() + "/users";
    }

    // =================================================================================
    //                               METODI PUBBLICI
    // =================================================================================

    public Utente getUserById(Long userId) throws Exception {
        HttpRequest request = newBuilder(USER_BASE_URL + "/" + userId)
                .GET()
                .build();

        return sendRequestSingle(request);
    }

    public Utente creaUtente(Utente nuovoUtente) throws Exception {
        if (!api.isAdmin()) {
            throw new RuntimeException("Operazione non permessa: devi essere Admin.");
        }

        String jsonBody = api.getMapper().writeValueAsString(nuovoUtente);

        HttpRequest request = newBuilder(USER_BASE_URL + "/creaUtente")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return sendRequestSingle(request);
    }

    public List<Utente> getAllUtenti() throws Exception {
        HttpRequest request = newBuilder(USER_BASE_URL + "/all")
                .GET()
                .build();

        return sendRequestList(request);
    }

    // =================================================================================
    //                            METODI PRIVATI (HELPERS)
    // =================================================================================

     // Crea un builder per la richiesta HTTP aggiungendo AUTOMATICAMENTE il token
     // se l'utente è loggato. Risparmia di scrivere l'header ogni volta.

    private HttpRequest.Builder newBuilder(String url) {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url));

        // Se abbiamo un token salvato, lo aggiungiamo agli header
        if (api.getAuthToken() != null && !api.getAuthToken().isEmpty()) {
            builder.header("Authorization", "Bearer " + api.getAuthToken());
        }

        return builder;
    }

    //Gestisce l'invio della richiesta e la deserializzazione di un SINGOLO oggetto Utente.
    private Utente sendRequestSingle(HttpRequest request) throws Exception {
        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Gestione specifica degli errori comuni per gli utenti
        if (response.statusCode() == 401) throw new Exception("Non sei loggato o la sessione è scaduta.");
        if (response.statusCode() == 403) throw new Exception("Accesso negato: non hai i permessi necessari.");

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return api.getMapper().readValue(response.body(), Utente.class);
        } else {
            throw new Exception("Errore API Utenti (" + response.statusCode() + "): " + response.body());
        }
    }

    //Gestisce l'invio della richiesta e la deserializzazione di una LISTA di oggetti Utente.
    private List<Utente> sendRequestList(HttpRequest request) throws Exception {
        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 401) throw new Exception("Non sei loggato o la sessione è scaduta.");

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return api.getMapper().readValue(
                    response.body(),
                    new TypeReference<List<Utente>>() {}
            );
        } else {
            throw new Exception("Errore API Utenti (" + response.statusCode() + "): " + response.body());
        }
    }
}