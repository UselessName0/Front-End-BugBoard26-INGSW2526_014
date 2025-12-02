package org.example.bugboard26frontend.APIServices;

import org.example.bugboard26frontend.Entita.Utente;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class UtenteService {

    private final ApiClient api = ApiClient.getApiClient();

    public Utente getUserById(Long userId) {
        return null;
    }

    public Utente CreaUtente(Utente nuovoUtente) throws Exception{

        if (!api.isAdmin()) {
            throw new RuntimeException("Operazione non permessa per utenti non admin.");
        }

        String url = api.getBaseUrl() + "/users/creaUtente";

        String jsonBody = api.getMapper().writeValueAsString(nuovoUtente);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + api.getAuthToken())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201 || response.statusCode() == 200) {
            return api.getMapper().readValue(response.body(), Utente.class);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non sei loggato!");
        } else if (response.statusCode() == 403) {
            throw new RuntimeException("Non sei autorizzato a creare un nuovo utente!");
        } else {
            throw new RuntimeException("Errore durante la creazione dell'utente: " + response.statusCode());
        }
    }

    public List<Utente> GetAllUtenti() throws Exception {

        String url = api.getBaseUrl() + "/users/all";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + api.getAuthToken())
                .GET()
                .build();

        HttpResponse<String> response = api.getClient().send(request,HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return api.getMapper().readValue(
                    response.body(),
                    api.getMapper().getTypeFactory().constructCollectionType(List.class, Utente.class));
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non sei loggato!");
        } else {
            throw new RuntimeException("Errore ottenendo la lista degli utenti: " + response.statusCode());
        }
    }

}
