package org.example.bugboard26frontend.apiservices;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.bugboard26frontend.entita.Commento;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CommentoService {

    public ApiClient api = ApiClient.getApiClient();

    public List<Commento> getCommentiByIssueId(Long issueId) throws Exception {
        String url = api.getBaseUrl() + "/commenti/Issue/" + issueId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return api.getMapper().readValue(
                    response.body(),
                    new TypeReference<List<Commento>>() {});
        } else  {
            throw new Exception("Errore nel recupero dei commenti: " + response.statusCode());
        }
    }

    public Commento creaCommento(Commento nuovoCommento) throws Exception {
        String url = api.getBaseUrl() + "/commenti/creaCommento";

        String jsonBody = api.getMapper().writeValueAsString(nuovoCommento);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + api.getAuthToken())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201 || response.statusCode() == 200) {
            return api.getMapper().readValue(response.body(), Commento.class);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non sei loggato!");
        } else {
            throw new RuntimeException("Errore durante la creazione del commento: " + response.statusCode());
        }
    }

    public Commento getAllCommentiUtente(Long idUtente) throws Exception {
        String url = api.getBaseUrl() + "/commenti/Utente/" + idUtente;

        String jsonBody = api.getMapper().writeValueAsString(idUtente);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + api.getAuthToken())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return api.getMapper().readValue(response.body(), Commento.class);
        } else {
            throw new RuntimeException("Errore durante la creazione del commento: " + response.statusCode());
        }
    }

    public Commento aumentaMiPiace(Commento commentoAggiornato) throws Exception {
        String url = api.getBaseUrl() + "/commenti/like/" + commentoAggiornato.getId();

        String jsonBody = api.getMapper().writeValueAsString(commentoAggiornato);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + api.getAuthToken())
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return api.getMapper().readValue(response.body(), Commento.class);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non sei loggato!");
        } else {
            throw new RuntimeException("Errore durante l'aggiornamento del like: " + response.statusCode());
        }
    }

    public Commento togliMiPiace(Commento commentoAggiornato) throws Exception {
        String url = api.getBaseUrl() + "/commenti/dislike/" + commentoAggiornato.getId();

        String jsonBody = api.getMapper().writeValueAsString(commentoAggiornato);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + api.getAuthToken())
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return api.getMapper().readValue(response.body(), Commento.class);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non sei loggato!");
        } else {
            throw new RuntimeException("Errore durante l'aggiornamento del unlike: " + response.statusCode());
        }
    }

    public List<Commento> getCommentiByUser (Long userId) throws Exception {
        String url = api.getBaseUrl() + "/commenti/utente/" + userId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return api.getMapper().readValue(
                    response.body(),
                    new TypeReference<List<Commento>>() {});
        } else  {
            throw new Exception("Errore nel recupero dei commenti dell'utente: " + response.statusCode());
        }
    }

    public void eliminaCommento(Long id) throws Exception {
        String url = api.getBaseUrl() + "/commenti/" + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + api.getAuthToken())
                .DELETE()
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Errore eliminando commento con ID " + id + ": " + response.statusCode());
        }
    }

    public void toggleLike(Long commentoId) throws Exception {
        String url = api.getBaseUrl() + "/commenti/" + commentoId + "/miPiace";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + api.getAuthToken())
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Errore nel toggle del like per il commento con ID " + commentoId + ": " + response.statusCode());
        }
    }


}
