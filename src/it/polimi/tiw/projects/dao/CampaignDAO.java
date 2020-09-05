package it.polimi.tiw.projects.dao;

import it.polimi.tiw.projects.beans.Campaign;
import it.polimi.tiw.projects.beans.CampaignStatus;
import it.polimi.tiw.projects.beans.Image;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CampaignDAO {

	private Connection con;
	private String name;

	public CampaignDAO(Connection connection, String name) {
		this.con = connection;
		this.name = name;
	}

	public List<Integer> countImage() throws SQLException {
		List<Integer> imagesId = new ArrayList<Integer>();

		//String query = "SELECT COUNT(*) AS NumberImages FROM immagine WHERE CampagnaName = ?";
		String query = "SELECT Id FROM immagine WHERE CampagnaName = ?";
		ResultSet result = null;
		PreparedStatement pstatement = con.prepareStatement(query);
		try {
			pstatement.setString(1, this.name);
			result = pstatement.executeQuery();
			while (result.next()) {
//				Image img = new Image();
//				img.setId(result.getInt("idImmagine"));
				int i = result.getInt("Id");
				imagesId.add(i);
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		}

		return imagesId;
	}

	public int countAnnotationPerImage(int imageId) throws SQLException {
		int counter = 0;
		ResultSet result = null;

		String query = "SELECT COUNT(*) AS NumberAnnotation FROM annotazione WHERE IdImmagine = ?";

		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, imageId);
			result = pstatement.executeQuery();
			while (result.next()) {
				counter = result.getInt("NumberAnnotation");
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			if (result != null){
				result.close();
			}
		}


		return counter;

	}

	//todo se unire countAnnotation con isAnnotationInconflics
	public boolean isAnnotationInConflicts(int imageId) throws SQLException {
		int counter = 0;
		ResultSet result = null;

		String query = "SELECT COUNT(DISTINCT) AS Number FROM annotazione WHERE idImmagine = ?";

		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			pstatement.setInt(1, imageId);
			result = pstatement.executeQuery();
			while (result.next()){
				counter = result.getInt("Number");
			}


		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			if (result != null){
				result.close();
			}
		}

		if (counter <= 1) {
			return false;
		} else {
			return true;
		}

	}

	public Campaign getCampaignDetails() throws SQLException {
		ResultSet result = null;
		Campaign campaign = new Campaign();

		String query = "select * from Campagna WHERE Name = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			pstatement.setString(1, this.name);
			result = pstatement.executeQuery();

			while (result.next()) {
				campaign.setName(this.name);
				campaign.setClient(result.getString("Committente"));
				campaign.setManager(result.getString("ManagerName"));
				campaign.setStatus(result.getInt("Stato"));
			}
		} catch (SQLException throwable) {
			throwable.printStackTrace();
		} finally {
			if (result != null){
				result.close();
			}
		}
		return campaign;
	}
}
