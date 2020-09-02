package it.polimi.tiw.projects.beans;

import java.sql.Date;

public class Annotation {

    private double latitudeImmagine;
    private double longitudeImmagine;
	private String lavoratoreName;
	private Date dataCreazione;
	private Boolean validita;
	private String fiducia;
	private String note;


    public double getLatitudeImmagine() {
        return latitudeImmagine;
    }

    public void setLatitudeImmagine(double latitudeImmagine) {
        this.latitudeImmagine = latitudeImmagine;
    }

    public double getLongitudeImmagine() {
        return longitudeImmagine;
    }

    public void setLongitudeImmagine(double longitudeImmagine) {
        this.longitudeImmagine = longitudeImmagine;
    }

    public String getLavoratoreName() {
        return lavoratoreName;
    }

    public void setLavoratoreName(String lavoratoreName) {
        this.lavoratoreName = lavoratoreName;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Boolean getValidita() {
        return validita;
    }

    public void setValidita(Boolean validita) {
        this.validita = validita;
    }

    public String getFiducia() {
        return fiducia;
    }

    public void setFiducia(String fiducia) {
        this.fiducia = fiducia;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
