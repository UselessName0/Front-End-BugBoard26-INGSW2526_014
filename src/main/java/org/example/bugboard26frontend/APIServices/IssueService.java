package org.example.bugboard26frontend.APIServices;

import com.fasterxml.jackson.core.type.TypeReference;
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
    private final String ISSUE_BASE_URL;

    public IssueService() {
        this.ISSUE_BASE_URL = api.getBaseUrl() + "/issues";
    }

    // =================================================================================
    //                               METODI PUBBLICI (GET)
    // =================================================================================

    public List<Issue> getIssues(String testo, Stato stato, Tipo tipo, String ordinaPer, String direzione, int page, int size) throws Exception {
        StringBuilder query = new StringBuilder("?");

        if (testo != null && !testo.isBlank()) query.append("q=").append(encode(testo)).append("&");
        if (stato != null) query.append("stato=").append(encode(stato.name())).append("&");
        if (tipo != null) query.append("tipo=").append(encode(tipo.name())).append("&");
        if (ordinaPer != null) query.append("ordinaPer=").append(encode(ordinaPer)).append("&");
        if (direzione != null) query.append("dir=").append(encode(direzione)).append("&");

        query.append("page=").append(page).append("&");
        query.append("size=").append(size);

        String url = ISSUE_BASE_URL + "/search" + query;
        System.out.println("Chiamata API a: " + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return sendRequestList(request);
    }

    public Issue getIssueById(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ISSUE_BASE_URL + "/" + id))
                .GET()
                .build();

        return sendRequestSingle(request);
    }

    public List<Issue> getIssueAssegnateA(Long userId, int page, int size) throws Exception {
        String url = ISSUE_BASE_URL + "/assigned?userId=" + userId + "&page=" + page + "&size=" + size;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return sendRequestList(request);
    }

    public List<Issue> getIssueCreateDaUtente(Long userId) throws Exception {
        String url = ISSUE_BASE_URL + "/createdBy/" + userId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return sendRequestList(request);
    }

    public int contaIssueCreateDaUtente(Long userId) throws Exception {
        String url = ISSUE_BASE_URL + "/countPerUtente?userId=" + userId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return Integer.parseInt(response.body());
        } else {
            throw new Exception("Errore conteggio issue (" + response.statusCode() + "): " + response.body());
        }
    }

    // =================================================================================
    //                    METODI DI MODIFICA (POST, PUT, DELETE)
    // =================================================================================

    public Issue creaIssue(Issue nuovaIssue) throws Exception {
        String jsonBody = api.getMapper().writeValueAsString(nuovaIssue);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ISSUE_BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return sendRequestSingle(request);
    }

    public Issue aggiornaIssue(Issue issueAggiornata) throws Exception {
        String jsonBody = api.getMapper().writeValueAsString(issueAggiornata);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ISSUE_BASE_URL + "/" + issueAggiornata.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return sendRequestSingle(request);
    }

    public Issue settaAssegnatario(Long issueId, Long userId) throws Exception {
        String url = ISSUE_BASE_URL + "/assegna/" + issueId + "?userId=" + userId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        return sendRequestSingle(request);
    }

    public Issue cambiaStato(Long issueId, Stato nuovoStato) throws Exception {
        String url = ISSUE_BASE_URL + "/" + issueId + "/status?nuovoStato=" + nuovoStato.name();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        return sendRequestSingle(request);
    }

    public void eliminaIssue(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ISSUE_BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 204) {
            throw new Exception("Errore eliminazione issue (" + response.statusCode() + "): " + response.body());
        }
    }

    // =================================================================================
    //                            METODI PRIVATI (HELPERS)
    // =================================================================================

     //Gestisce l'invio della richiesta e la deserializzazione di un SINGOLO oggetto Issue.

    private Issue sendRequestSingle(HttpRequest request) throws Exception {
        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return api.getMapper().readValue(response.body(), Issue.class);
        } else {
            throw new Exception("Errore API (" + response.statusCode() + "): " + response.body());
        }
    }

     // Gestisce l'invio della richiesta e la deserializzazione di una lista di oggetti Issue.
     // usa TypeReference per evitare il linkedhashmap crash.
    private List<Issue> sendRequestList(HttpRequest request) throws Exception {
        HttpResponse<String> response = api.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return api.getMapper().readValue(
                    response.body(),
                    new TypeReference<List<Issue>>() {}
            );
        } else {
            throw new Exception("Errore API (" + response.statusCode() + "): " + response.body());
        }
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}