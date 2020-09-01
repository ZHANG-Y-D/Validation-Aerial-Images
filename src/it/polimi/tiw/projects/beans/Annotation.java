package it.polimi.tiw.projects.beans;

import java.sql.Date;

public class Annotation {
	
	private int idImmagine;
	private String lavoratoreName;
	private Date dataCreazione;
	private Boolean validita;
	private String fiducia;
	private String note;

    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    public void setLavoratoreName(String lavoratoreName) {
        this.lavoratoreName = lavoratoreName;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public void setValidita(Boolean validita) {
        this.validita = validita;
    }

    public void setFiducia(String fiducia) {
        this.fiducia = fiducia;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getIdImmagine() {
        return idImmagine;
    }

    public String getLavoratoreName() {
        return lavoratoreName;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public Boolean getValidita() {
        return validita;
    }

    public String getFiducia() {
        return fiducia;
    }

    public String getNote() {
        return note;
    }


}
