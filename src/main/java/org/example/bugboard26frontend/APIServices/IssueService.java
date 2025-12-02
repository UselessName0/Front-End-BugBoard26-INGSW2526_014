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

    public Issue getIssueById(Long id) throws Exception {
        String url = api.getBaseUrl() + "/issues/" + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return api.getMapper().readValue(
                    response.body(),
                    Issue.class);
        } else  {
            throw new Exception("Errore ottenendo issue con ID " + id + ": " + response.statusCode());
        }
    }

    public Issue creaIssue(Issue nuovaIssue) throws Exception {
        String url = api.getBaseUrl() + "/issues";

        String requestBody = api.getMapper().writeValueAsString(nuovaIssue);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return api.getMapper().readValue(
                    response.body(),
                    Issue.class);
        } else  {
            throw new Exception("Errore creando issue: " + response.statusCode());
        }
    }

    public Issue aggiornaIssue(Issue issueAggiornata) throws Exception {
        String url = api.getBaseUrl() + "/issues/" + issueAggiornata.getId();

        String requestBody = api.getMapper().writeValueAsString(issueAggiornata);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return api.getMapper().readValue(
                    response.body(),
                    Issue.class);
        } else  {
            throw new Exception("Errore aggiornando issue con ID " + issueAggiornata.getId() + ": " + response.statusCode());
        }
    }

    public void eliminaIssue(Long id) throws Exception {
        String url = api.getBaseUrl() + "/issues/" + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Errore eliminando issue con ID " + id + ": " + response.statusCode());
        }
    }

    public int contaIssueCreateDaUtente(Long userId) throws Exception {
        String url = api.getBaseUrl() + "/issues/countPerUtente?userId=" + userId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return Integer.parseInt(response.body());
        } else  {
            throw new Exception("Errore ottenendo il conteggio delle issue create dall'utente con ID " + userId + ": " + response.statusCode());
        }
    }

    public Issue settaAssegnatario(Long issueId, Long userId) throws Exception {
        String url = api.getBaseUrl() + "/issues/assegna/" + issueId + "?userId=" + userId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return api.getMapper().readValue(
                    response.body(),
                    Issue.class);
        } else  {
            throw new Exception("Errore settando assegnatario per issue con ID " + issueId + ": " + response.statusCode());
        }
    }

    public List<Issue> getIssueAssegnateA(Long userId, int page, int size) throws Exception {
        String url = api.getBaseUrl() + "/issues/assigned?userId=" + userId + "&page=" + page + "&size=" + size;

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
            throw new Exception("Errore ottenendo le issue assegnate all'utente con ID " + userId + ": " + response.statusCode());
        }
    }

    public Issue cambiaStato(Long issueId, Stato nuovoStato) throws Exception {
        String url = api.getBaseUrl() + "/issues/" + issueId + "/status?nuovoStato=" + nuovoStato.name();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return api.getMapper().readValue(
                    response.body(),
                    Issue.class);
        } else  {
            throw new Exception("Errore cambiando stato per issue con ID " + issueId + ": " + response.statusCode());
        }
    }

    public List<Issue> getIssueCreateDaUtente(Long userId) throws Exception {
        String url = api.getBaseUrl() + "/issues/createdBy/" + userId;

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
            throw new Exception("Errore ottenendo le issue create dall'utente con ID " + userId + ": " + response.statusCode());
        }
    }


}
