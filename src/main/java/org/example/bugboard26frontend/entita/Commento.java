package org.example.bugboard26frontend.entita;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true) // <--- FONDAMENTALE: Se il backend manda campi extra, non crasha!
public class Commento {

    //ATTRIBUTI
    private Long id;
    private Issue issue;
    private Utente utente;
    private String contenuto;
    private Set<Utente> miPiace = new HashSet<>();

    // CORREZIONE QUI: Ho messo la P maiuscola per combaciare col Backend
    @JsonProperty("numeroMiPiace")
    private int numeroMiPiace;

    //COSTRUTTORI
    public Commento() {};

    //METODI
    //Getter&Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public Set<Utente> getMiPiace() { return miPiace; }

    public void setMiPiace(Set<Utente> miPiace) { this.miPiace = miPiace; }

    // CORREZIONE GETTER (P maiuscola)
    public int getNumeroMiPiace() {
        return numeroMiPiace;
    }

    // CORREZIONE SETTER (P maiuscola)
    public void setNumeroMiPiace(int numeroMiPiace) {
        this.numeroMiPiace = numeroMiPiace;
    }
}