package org.example.bugboard26frontend.APIServices;

import org.example.bugboard26frontend.Entita.Issue;
import org.example.bugboard26frontend.Enums.Stato;
import org.example.bugboard26frontend.Enums.Tipo;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class IssueService {

    private final ApiClient api = ApiClient.getApiClient();

    public List<Issue> getIssues(String testo, Stato stato, Tipo tipo, String ordinaPer, String direzione, int page, int size) throws Exception
    {
        StringBuilder query = new StringBuilder("?");


        if (testo != null && !testo.isBlank()) {
            query.append("q=").append(URLEncoder.encode(testo, StandardCharsets.UTF_8)).append("&");
        }

        if( stato != null ) {
            query.append("stato=").append(URLEncoder.encode(stato.name(), StandardCharsets.UTF_8)).append("&");
        }

        if( tipo != null ) {
            query.append("tipo=").append(URLEncoder.encode(tipo.name(), StandardCharsets.UTF_8)).append("&");
        }

        if( ordinaPer != null ) query.append("ordinaPer=").append(URLEncoder.encode(ordinaPer, StandardCharsets.UTF_8)).append("&");
        if( direzione != null ) query.append("dir=").append(URLEncoder.encode(direzione,  StandardCharsets.UTF_8)).append("&");

        query.append("page=").append(page).append("&");
        query.append("size=").append(size);

        String url = api.getBaseUrl() + "/issues/search" + query.toString();

        //debug
        System.out.println("Chiamata API a: " + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return api.getMapper().readValue(
                    response.body(),
                    List.class);
        } else  {
            throw new Exception("Errore ricerca" + response.statusCode());
        }
    }
}
