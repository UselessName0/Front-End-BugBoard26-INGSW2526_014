package org.example.bugboard26frontend.APIServices;

import com.fasterxml.jackson.core.type.TypeReference;
import eu.hansolo.fx.countries.tools.Api;
import org.example.bugboard26frontend.Entita.Commento;

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

}
