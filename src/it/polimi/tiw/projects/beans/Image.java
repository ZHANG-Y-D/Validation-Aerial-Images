package it.polimi.tiw.projects.beans;

import java.util.Date;

public class Image {
	private int id;
	private int latitude;
	private int longitude;
	private String comune;
	private String regione;
	private String provenienza;
	private Date date;
	private String manager;
	
	
	public int getID(){
		return id;
	}
	
	public int getLatutide(){
		return latitude;
	}
	
	public int getLongitude(){
		return longitude;
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
	
	public String getManager() {
		return manager;
	}
	
	
	public void setId(int i) {
		id = i;
	}
	
	public void setLatitude(int i) {
		latitude = i;
	}
	
	public void setLongitude(int i) {
		longitude = i;
	}
	
	public void setComune(String name) {
		comune = name;
	}
	
	public void setRegione(String name) {
		regione = name;
	}
	
	public void setManager(String name) {
		manager = name;
	}
	
	
	public void setProvenienza(String name) {
		provenienza = name;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	

	

}
