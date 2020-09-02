package it.polimi.tiw.projects.beans;

import java.util.Date;

public class Image {

	private double latitude;
	private double longitude;
	private String comune;
	private String regione;
	private String provenienza;
	private Date date;
	private String risoluzione;
	private String campagnaName;
	private String foto;


	public String getRisoluzione() {
		return risoluzione;
	}

	public void setRisoluzione(String risoluzione) {
		this.risoluzione = risoluzione;
	}


	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getFoto() {
		return foto;
	}

	public String getComune() {
		return comune;
	}

	public String getRegione() {
		return regione;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public Date getDate() {
		return date;
	}

	public String getCampagnaName() {
		return campagnaName;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setCampagnaName(String campagnaName) {
		this.campagnaName = campagnaName;
	}

}
