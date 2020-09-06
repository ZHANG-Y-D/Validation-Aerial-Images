package it.polimi.tiw.projects.dao;

import it.polimi.tiw.projects.beans.Campaign;
import it.polimi.tiw.projects.beans.Image;
import it.polimi.tiw.projects.beans.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class WorkerDAO {
	private Connection con;
	private String name;
	
	public WorkerDAO(Connection connection, String name) {
		this.con = connection;
		this.name = name;
	}


	public List<String> getSubscribeCampaigns() throws SQLException{
		List<String> campaigns = new ArrayList<String>();

		String query = "SELECT distinct i.campagnaName FROM iscrizione AS i left join campagna AS c  ON i.CampagnaName = c.Name WHERE i.LavoratoreName = ? AND c.Stato = 1;";
		PreparedStatement pstatement = con.prepareStatement(query);
		ResultSet result = null;
		try {
			pstatement.setString(1, name);
			result = pstatement.executeQuery();

			while (result.next()){
				String campaign = result.getString("CampagnaName");
				campaigns.add(campaign);

			}
		}catch (SQLException e){
			throw new SQLException(e);
		}

		return campaigns;

	}

	public List<String> getNotSubscribeCampaigns() throws  SQLException{
		List<String> campaigns = new ArrayList<String>();
		String query = "SELECT Name FROM campagna WHERE stato = 1 AND Name NOT IN (SELECT distinct i.CampagnaName FROM iscrizione AS i right join campagna AS c  ON i.CampagnaName = c.Name WHERE i.LavoratoreName = ? AND c.Stato = 1);";

		PreparedStatement pstatement = con.prepareStatement(query);
		ResultSet result = null;
		try {
			pstatement.setString(1, name);
			result = pstatement.executeQuery();

			while (result.next()){
				String campaign = result.getString("Name");
				campaigns.add(campaign);

			}
		}catch (SQLException e){
			throw new SQLException(e);
		}

		return campaigns;
	}


	public String subscribeToCampaign(String campaign) throws SQLException{
		String query = "INSERT INTO iscrizione(LavoratoreName, CampagnaName) VALUES(?,?);";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, name);
			pstatement.setString(2, campaign);
			pstatement.executeUpdate();
			return "OK";
		} catch (SQLException e) {
			return e.getMessage();
		}

	}


	//TODO CampagnaDao has already this function
	public boolean isStarted(String campaign) throws SQLException{
		ResultSet result = null;

		String query = "SELECT Stato FROM campagna WHERE Name = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query)){
			pstatement.setString(1, campaign);
			result = pstatement.executeQuery();
			result.next();

			return result.getInt("Stato") == 1;

		} catch (SQLException e) {
			throw new SQLException(e);
		}finally {
			if(result!= null){
				result.close();
			}
		}
	}

	public String insertAnnotation(int imageId, Date date,int validita, String fiducia, String note) throws  SQLException {

		String query = "INSERT INTO annotazione VALUES(?,?,?,?,?,?)";

		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, imageId);
			pstatement.setString(2, name);
			pstatement.setDate(3, date);
			pstatement.setInt(4, validita);
			pstatement.setString(5, fiducia);
			pstatement.setString(6, note);
			pstatement.executeUpdate();
			return "OK";
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public List<Image> notAnnotatedImage (String campaignName) throws SQLException{
		List<Image> images = new ArrayList<Image>();

		String query = "SELECT * FROM immagine AS i LEFT JOIN annotazione AS a ON i.Id=a.IdImmagine WHERE a.LavoratoreName !=? AND i.CampagnaName = ?";
		ResultSet result = null;
		try (PreparedStatement pstatement = con.prepareStatement(query)){
			pstatement.setString(1, name);
			pstatement.setString(2, campaignName);
			result = pstatement.executeQuery();
			while (result.next()) {
				Image image = new Image();
				image.setId(result.getInt("Id"));
				image.setLatitude(result.getDouble("Latitudine"));
				image.setLongitude(result.getDouble("Longitudine"));
				image.setComune(result.getString("Comune"));
				image.setRegione(result.getString("Regione"));
				image.setProvenienza(result.getString("Provenienza"));
				image.setDate(new Date(result.getTimestamp("DataDiRecupero").getTime()));
				image.setRisoluzione(result.getString("Risoluzione"));
				image.setCampagnaName(campaignName);
				image.setFoto(Base64.getEncoder().encodeToString(result.getBytes("Foto")));

				images.add(image);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e);
		} finally {
			if (result != null){
				result.close();
			}
		}
		return images;
	}

}
