package it.polimi.tiw.projects.dao;

import it.polimi.tiw.projects.beans.Image;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ImageDAO {
        
	private Connection con;
	public ImageDAO(Connection connection) {
		this.con = connection;
	}

	public String insertImage(double latitude,
							  double longitude,
							  String comune,
							  String regione,
							  String provenienza,
							  Date date,
							  String risoluzione,
							  String campagnaName,
							  InputStream imageStream
							  ) throws SQLException {

		String query = "INSERT INTO Immagine VALUES(?,?,?,?,?,?,?,?,?,?)";
		try(PreparedStatement pstatement = con.prepareStatement(query);){
			pstatement.setNull(1,java.sql.Types.INTEGER);
			pstatement.setDouble(2, latitude);
			pstatement.setDouble(3, longitude);
			pstatement.setString(4, comune);
			pstatement.setString(5, regione);
			pstatement.setString(6, provenienza);
			pstatement.setDate(7, date);
			pstatement.setString(8, risoluzione);
			pstatement.setString(9, campagnaName);
			pstatement.setBlob(10, imageStream);
			pstatement.executeUpdate();
			return "OK";
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public List<Image> findImagesByCampagnaName(String campagnaName) throws SQLException {
		List<Image> images = new ArrayList<Image>();

		String query = "SELECT * FROM Immagine WHERE CampagnaName = ? ORDER BY DataDiRecupero asc";
		ResultSet result = null;
		PreparedStatement pstatement = null;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setString(1, campagnaName);
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
				image.setCampagnaName(campagnaName);
				image.setFoto(Base64.getEncoder().encodeToString(result.getBytes("Foto")));

				images.add(image);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e);
		} finally {
			try {
				result.close();
			} catch (Exception e1) {
				throw new SQLException(e1);
			}
			try {
				pstatement.close();
			} catch (Exception e2) {
				throw new SQLException(e2);
			}
		}
		return images;
	}
}
