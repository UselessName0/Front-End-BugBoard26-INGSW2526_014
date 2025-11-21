package org.example.bugboard26frontend.entita.entita;

public class Utente {

    //ATTRIBUTI
    private Long id;
    private Issue issue;
    private Utente utente;
    String contenuto;
    int mipiace;

    //COSTRUTTORI
    public Utente() {};

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

    public int getMipiace() {
        return mipiace;
    }

    public void setMipiace(int mipiace) {
        this.mipiace = mipiace;
    }
}
