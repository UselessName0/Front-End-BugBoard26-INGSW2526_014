package org.example.bugboard26frontend.entita.entita;

import java.time.LocalDateTime;

public class Issue {

    //ATTRIBUTI
    private Long id;
    private String titolo;
    private String descrizione;
    private String priorita;
    private String tipo;
    private String stato;
    private LocalDateTime dataCreazione;
    private Utente utente;
    private Utente assegnatario;

    //COSTRUTTORI
    public Issue() {};

    //METODI
    //Getter&Setter
    public Long getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getPriorita() {
        return priorita;
    }

    public String getTipo() {
        return tipo;
    }

    public String getStato() {
        return stato;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public Utente getUtente() {
        return utente;
    }

    public Utente getAssegnatario() {
        return assegnatario;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setPriorita(String priorita) {
        this.priorita = priorita;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public void setAssegnatario(Utente assegnatario) {
        this.assegnatario = assegnatario;
    }
}
